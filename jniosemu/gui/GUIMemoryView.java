package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;
import jniosemu.emulator.memory.*;

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

	private JPanel listPanel;
	
	private ArrayList<JList> memoryLists = null;
	
	private ArrayList<MemoryBlock> memoryBlocks = null;
	
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
			EventManager.EVENT.PROGRAMCOUNTER_CHANGE,
			EventManager.EVENT.EMULATOR_RESET,
			EventManager.EVENT.MEMORY_CHANGE
		};

		this.eventManager.addEventObserver(events, this);
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
		
		listPanel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 0, 4),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
			)
		);	

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

	private JList addList(MemoryBlock memBlock)
	{
		JList memoryList = new JList();
	
		memoryList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		memoryList.setAlignmentX(Component.CENTER_ALIGNMENT);
		memoryList.setBackground(Color.WHITE);
		memoryList.setFont(new Font("Monospaced", Font.PLAIN, 12));
		memoryList.setCellRenderer(new RegisterCellRenderer());
		memoryList.setBorder(
			BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
		);
		
		JLabel titleLabel = new JLabel(memBlock.getName(), JLabel.LEFT);
		titleLabel.setLabelFor(memoryList);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		this.listPanel.add(titleLabel);
		this.listPanel.add(memoryList);		
		
		return memoryList;
	}

	private void initLists()
	{
		listPanel.removeAll();
		
		System.out.println("init");
		memoryLists = new ArrayList<JList>();
		
		for(int i = 0; i < this.memoryBlocks.size(); i++)
		{
			memoryLists.add(
				this.addList( (MemoryBlock) memoryBlocks.get(i) )
			);
		}
		
		this.updateLists();
	}
	
	private void updateLists()
	{
		for(int i = 0; i < this.memoryLists.size(); i++)
		{
			MemoryBlock memBlock = (MemoryBlock) memoryBlocks.get(i);
			
			Vector<MemoryInt> memVector =  memBlock.getMemoryVector();
			
			if (memVector != null)
			{
				memoryLists.get(i).setListData(memVector);
				System.out.println("update: MemVector size " + memVector.size() + " for block " + memBlock.getName());
			}
			else
				System.out.println("update: MemVector null for block " + memBlock.getName());
		}		
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case PROGRAMCOUNTER_CHANGE:
				//
				break;
			case EMULATOR_RESET:
				//
				break;
			case MEMORY_CHANGE:
				if (this.memoryBlocks == null)
				{
					this.memoryBlocks = (ArrayList<MemoryBlock>) obj;
				}
			
				if (this.memoryLists == null)
				{
					initLists();
				}
				else
				{
					updateLists();
				}
			
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
	class RegisterCellRenderer extends JLabel
												 implements ListCellRenderer {

			private MemoryInt memInt;

			public RegisterCellRenderer() {
					setOpaque(true);
					setHorizontalAlignment(CENTER);
					setVerticalAlignment(CENTER);
			}

			public Component getListCellRendererComponent(
																				 JList list,
																				 Object value,
																				 int index,
																				 boolean isSelected,
																				 boolean cellHasFocus) {

					this.setFont(list.getFont());

					this.memInt = (MemoryInt) value;
					setText("."); // trigger repaint

 
					if (isSelected) { 
						setBackground(list.getSelectionBackground()); 
						setForeground(list.getSelectionForeground()); 
					} else { 
						setBackground(list.getBackground()); 
						setForeground(list.getForeground()); 
					} 

					return this;
			}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isOpaque()) 
			{ 
/*				
					// paint background
					switch (regObj.getState())
					{
						case READ:
							g.setColor(new Color(220, 255, 220));
							break;
						case WRITE:
							g.setColor(new Color(255, 220, 220));
							break;
						default:
							g.setColor(getBackground());
					}
*/
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
			}

			FontMetrics metrics = g.getFontMetrics(getFont());
	/*	
			if (regObj.getState() == Register.STATE.DISABLED)
				g.setColor(new Color(196, 196, 196));
			else
		*/
				g.setColor(new Color(0, 0, 0));
			
			g.drawString("" + memInt.getAddress(), 2, 11);

			String tmp = new String(memInt.getMemory());	

			g.drawString(tmp, getWidth()-metrics.stringWidth(tmp), 11);
		}

	}


}