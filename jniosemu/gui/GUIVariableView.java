package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.compiler.Variable;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryInt;
import jniosemu.Utilities;

/**
 * Creates and manages the GUI component of the variable view.
 */
 public class GUIVariableView extends JFrame
                              implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * Panel used to contain the list.
	 */
	private JPanel listPanel;

	/**
	 * List object used to show variables with values.
	 */
	private JList variableList;

	/**
	 * Variable memory block.
	 */
	private MemoryBlock memBlock;
	
	/**
	 * Track if variables should be redrawn.
	 */
	private boolean redrawState = true;

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
	public GUIVariableView(EventManager eventManager)
	{
		super("Variable View");

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.VARIABLE_CHANGE,
			EventManager.EVENT.VARIABLE_VECTOR,
			EventManager.EVENT.EMULATOR_CLEAR
		};

		this.eventManager.addEventObserver(events, this);
		
		// trigger an update
		eventManager.sendEvent(EventManager.EVENT.VARIABLE_REQUEST_UPDATE);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIVariableView
	 */
	private void setup()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// list
		listPanel = new JPanel(new GridLayout(1,1));
		
		listPanel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 0, 4),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
			)
		);		
		
		variableList = new JList();
		variableList.setBackground(Color.WHITE);
		variableList.setFont(new Font("Monospaced", Font.PLAIN, 12));
		variableList.setCellRenderer(
			new VariableCellRenderer(
				variableList.getFontMetrics(variableList.getFont())
			)
		);
		listPanel.add(variableList);

    // button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("Close");
    button.addActionListener(this);
    buttonPanel.add(button);

    // container
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    
    contentPane.add(listPanel, BorderLayout.CENTER);
    contentPane.add(buttonPanel, BorderLayout.PAGE_END);
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case VARIABLE_CHANGE:
				this.memBlock = (MemoryBlock) obj;
				if (this.memBlock.isChanged() || redrawState)
				{
					variableList.repaint();
					
					// if changed, redraw next time to remove indication
					redrawState = this.memBlock.isChanged();
				}
				break;
			case VARIABLE_VECTOR:
				variableList.setListData( (Vector<Variable>) obj );
				break;
			case EMULATOR_CLEAR:
				variableList.setModel(new DefaultListModel());
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
	 * Custom cell renderer for the JList in the Variable View.
	 */
	class VariableCellRenderer extends JPanel
												 implements ListCellRenderer {

		private Variable varObj;

		private final FontMetrics metrics;
		private final int baseline;
		private final int width;
    private final int height;

		public VariableCellRenderer(FontMetrics metrics) {
			super();
			setOpaque(true);
			setFont(variableList.getFont());
			
			this.baseline = metrics.getAscent();
			this.height = metrics.getHeight();
			this.width = variableList.getWidth();
			this.metrics = metrics;
		}

    /** 
     * Return the renderers fixed size.  
     */
		public Dimension getPreferredSize()
		{
			return new Dimension(width, height);
		}

		/**
		 * Get read/write/untouched state of variable.
		 *
		 * @param  var  variable to check state of
		 * @return      state of variable
		 */
		private MemoryInt.STATE getVariableState(Variable var)
		{
			MemoryInt.STATE state = MemoryInt.STATE.UNTOUCHED;
			
			// no memory block received yet
			if (memBlock == null)
				return state;
			
			int addr = var.getStartAddr();
			int endAddr = var.getStartAddr() + var.getLength();

			// loop until first non-untouched state or end of variable			
			while (state == MemoryInt.STATE.UNTOUCHED && addr < endAddr)
			{
				state = memBlock.getState(addr++);
			}
			
			return state;
		}

		/**
		 * Sets background/foreground color and stores Variables object.
		 */
		public Component getListCellRendererComponent(
																			 JList list,
																			 Object value,
																			 int index,
																			 boolean isSelected,
																			 boolean cellHasFocus) 
		{
			this.varObj = (Variable) value;

			if (isSelected)
			{ 
				switch (getVariableState(this.varObj))
				{
					case READ:
						setBackground(GUIManager.HIGHLIGHT_SELECTED_GREEN);
						break;
					case WRITE:
						setBackground(GUIManager.HIGHLIGHT_SELECTED_RED);
						break;
					default:
						setBackground(list.getSelectionBackground());
				}
			} 
			else 
			{ 
				switch (getVariableState(this.varObj))
				{
					case READ:
						setBackground(GUIManager.HIGHLIGHT_GREEN);
						break;
					case WRITE:
						setBackground(GUIManager.HIGHLIGHT_RED);
						break;
					default:
						setBackground(list.getBackground());
				}
			} 

			return this;
		}

		/**
		 * Custom paint method bypassing standard JComponent
		 * painting to optimize performance and do custom drawing.
		 */
		public void paintComponent(Graphics g) 
		{
			// don't draw anything if no memory block is received yet
			if (memBlock == null)
				return;
				
			// clear background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.black);
			
			// variable address
			g.drawString(Utilities.intToHexString(this.varObj.getStartAddr()), 2, this.baseline);

			// variable type
			g.drawString(this.varObj.getType().toString(), 90, this.baseline);
			
			// variable name
			g.drawString(this.varObj.getName(), 140, this.baseline);

			// variable value as byte array
			byte[] byteArray = this.varObj.getValue(memBlock);

			// format value
			String value = "";

			switch (this.varObj.getType())
			{
				case BYTE:
				case HWORD:
				case WORD:
					value = "0x" + Utilities.byteArrayToHexString(byteArray);
					break;
				case ASCII:
				case ASCIZ:
					value = Utilities.byteArrayToString(byteArray);
					break;
				default:
					value = "unknown type";
			}

			// draw variable value
//			String tmp = Utilities.byteArrayToBitString(byteArray);
			g.drawString(value, getWidth() - this.metrics.stringWidth(value) - 2, this.baseline);
		}

	}
	
}