package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;
import jniosemu.emulator.memory.*;
import jniosemu.Utilities;

/**
 * Creates and manages the GUI component of the memory view.
 */
public class GUIMemoryView extends JFrame
                           implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * Panel used to contain all lists.
	 */
	private JPanel listPanel;

	/**
	 * Reference to memory blocks to display.
	 */
	private ArrayList<MemoryBlock> memoryBlocks = null;
	
	/**
	 * Lists used to display content of each memory block.
	 */
	private JList[] memoryLists = null;

	private int[] lastChanged = null;

	/**
	 * Track if a memory block should be repainted or not.
	 */
	private boolean[] memoryUpdateState = null;
	
	/**
	 * Initiates the creation of GUI components and adds itself to
	 * the Event Manager as an observer.
	 *
	 * @post      eventManager reference is set for this object.
	 * @calledby  GUIManager.setup()
	 * @calls     setup(), EventManager.addEventObserver()
	 *
	 * @param  eventManager  The Event Manager object.
	 */
	public GUIMemoryView(EventManager eventManager)
	{
		super("Memory View");

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.MEMORY_CHANGE,
			EventManager.EVENT.EMULATOR_CLEAR,
			EventManager.EVENT.EMULATOR_READY
		};

		this.eventManager.addEventObserver(events, this);

		// trigger an update
		eventManager.sendEvent(EventManager.EVENT.MEMORY_REQUEST_UPDATE);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIMemoryView
	 */
	private void setup()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// list
		listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

		JScrollPane scrollPane = new JScrollPane(listPanel);

    // button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("Close");
    button.addActionListener(this);
    buttonPanel.add(button);

    // container
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    
    contentPane.add(scrollPane, BorderLayout.CENTER);
    contentPane.add(buttonPanel, BorderLayout.PAGE_END);
	}

	/**
	 * Create display list for given memory block.
	 *
	 * @calledby           initLists()
	 * @param    memBlock  memory block to display
	 * @return             list created
	 */
	private JList addList(MemoryBlock memBlock)
	{
		JList memoryList = new JList();
	
		memoryList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		memoryList.setAlignmentX(Component.CENTER_ALIGNMENT);
		memoryList.setBackground(Color.WHITE);
		memoryList.setFont(new Font("Monospaced", Font.PLAIN, 12));
		memoryList.setCellRenderer(
			new MemoryCellRenderer(
				memoryList
			)
		);
		
		JLabel titleLabel = new JLabel(" " + memBlock.getName(), JLabel.LEFT);
		titleLabel.setLabelFor(memoryList);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) titleLabel.getMinimumSize().getHeight()));
		titleLabel.setOpaque(true);
		titleLabel.setBorder(
			BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
		);
		
		this.listPanel.add(titleLabel);
		this.listPanel.add(memoryList);		
		
		return memoryList;
	}

	/**
	 * Initiates creation of display lists for all memory blocks.
	 *
	 * @calls  addList(), updateLists()
	 */
	private void initLists()
	{
		listPanel.removeAll();
		
		memoryLists = new JList[this.memoryBlocks.size()];
		memoryUpdateState = new boolean[this.memoryBlocks.size()];
		this.lastChanged = new int[this.memoryBlocks.size()];

		for(int i = 0; i < this.memoryBlocks.size(); i++)
		{
			this.lastChanged[i] = -1;
			memoryLists[i] = this.addList( (MemoryBlock) memoryBlocks.get(i) );
			
			// redraw first time
			memoryUpdateState[i] = true;
		}
		
		this.updateLists();
	}
	
	/**
	 * Updates content of all list tagged as changed.
	 */
	private void updateLists()
	{
		for(int i = 0; i < this.memoryLists.length; i++)
		{
			MemoryBlock memBlock = (MemoryBlock) memoryBlocks.get(i);
			
			if (memBlock.isChanged(this.lastChanged[i]) || memoryUpdateState[i])
			{
				Vector<MemoryInt> memVector =  memBlock.getMemoryVector();

				if (memVector != null)
					memoryLists[i].setListData(memVector);
				
				// if memory block has changed, do also redraw next time
				// in order to remove indication
				memoryUpdateState[i] = memBlock.isChanged(this.lastChanged[i]);
				this.lastChanged[i] = memBlock.lastChanged();
			}
		}
	}

	/**
	 * Clears and empty all lists.
	 */
	private void clearLists()
	{
		if (memoryLists == null)
			return;
		
		for(int i = 0; i < this.memoryLists.length; i++)
		{
			this.lastChanged[i] = -1;

			// clear list
			memoryLists[i].setModel(new DefaultListModel());
		}
		
		this.memoryBlocks = null;
		this.memoryLists = null;
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case MEMORY_CHANGE:
				// get memory blocks if not present
				if (this.memoryBlocks == null)
					this.memoryBlocks = (ArrayList<MemoryBlock>) obj;
			
				// init if no lists exists
				if (this.memoryLists == null)
					initLists();
				else
					// otherwise update existing
					updateLists();
			
				break;
			case EMULATOR_CLEAR:
				clearLists();
				break;
			case EMULATOR_READY:
				for (int i = 0; i < this.lastChanged.length; i++)
					this.lastChanged[i] = -1;
				break;
		}
	}

	/**
	 * Invoked when a GUI action occurs..
	 *
	 * @param  e  action event object
	 */
	public void actionPerformed(ActionEvent e) {
	  setVisible(false);
	  dispose();
	}

	/**
	 * Custom cell renderer for the lists in the Memory View.
	 */
	class MemoryCellRenderer extends JPanel
												 implements ListCellRenderer {

			private MemoryInt memInt;

			private final FontMetrics metrics;
			private final int baseline;
			private final int width;
			private final int height;

			public MemoryCellRenderer(JList list) {
				super();
				setOpaque(true);
				setFont(list.getFont());
				
				this.metrics = list.getFontMetrics(list.getFont());
				
				this.baseline = metrics.getAscent();
				this.height = metrics.getHeight();
				this.width = list.getWidth();
			}

	    /** 
	     * Return the renderers fixed size.  
	     */
			public Dimension getPreferredSize()
			{
				return new Dimension(width, height);
			}
		
			/**
			 * Sets background/foreground color and stores MemoryInt object.
			 */
			public Component getListCellRendererComponent(
																				 JList list,
																				 Object value,
																				 int index,
																				 boolean isSelected,
																				 boolean cellHasFocus)
			{
				this.memInt = (MemoryInt) value;

				if (isSelected) { 
					setBackground(list.getSelectionBackground()); 
					setForeground(list.getSelectionForeground()); 
				} else { 
					setBackground(list.getBackground()); 
					setForeground(list.getForeground()); 
				} 

				return this;
			}

		/**
		 * Custom paint method bypassing standard JComponent
		 * painting to optimize performance and do custom drawing.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			// clear background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.black);
			
			int xOffset = 2;
			
			// address
			g.drawString(Utilities.intToHexString(memInt.getAddress()), xOffset, this.baseline);

			xOffset = 100;
			int spaceWidth = this.metrics.stringWidth(" ");

			// memory as hex
			byte[] b = memInt.getMemory();
			
			for (int i = 0; i < b.length; i++)
			{
				switch (memInt.getState(i))
				{
					case READ:
						g.setColor(GUIManager.HIGHLIGHT_GREEN);
						break;
					case WRITE:
						g.setColor(GUIManager.HIGHLIGHT_RED);
						break;
					default:
						g.setColor(getBackground());
						break;
				}

				String tmp = Integer.toHexString( (b[i] & 0xFF) | 0x100 ).substring(1, 3);

				int stringWidth = this.metrics.stringWidth(tmp);
				
				g.fillRect(xOffset - 2, 1, this.metrics.stringWidth(tmp) + 4, metrics.getHeight() - 3);
				g.setColor(Color.black);
				
				g.drawString(tmp, xOffset, this.baseline);
				
				xOffset = xOffset + stringWidth + spaceWidth;
			}

			// memory as ascii
			String tmp = Utilities.byteArrayToString(b);

			g.drawString(tmp, getWidth() - this.metrics.stringWidth(tmp) - 2, this.baseline);
		}

	}


}