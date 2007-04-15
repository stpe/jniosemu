package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.emulator.register.*;

/** 
 * Creates and manages the GUI component of the register view in the emulator.
 */
public class GUIRegisters extends JPanel 
											 implements EventObserver, ListSelectionListener {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * List component used to display register values.
	 */
	private JList registerList;
	
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
	public GUIRegisters(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
		// add events to listen to
		this.eventManager.addEventObserver(EventManager.EVENT.REGISTER_CHANGE, this);		
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIRegisters
	 */	
	private void setup()
	{
		this.setPreferredSize(new Dimension(190, 0));

		// registers
		registerList = new JList();
		registerList.setBackground(Color.WHITE);
		registerList.setFont(new Font("Monospaced", Font.PLAIN, 11));
		registerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		registerList.setCellRenderer(new RegisterCellRenderer());
		registerList.addListSelectionListener(this);
		
		// scrollbars
		JScrollPane scrollPane = new JScrollPane(registerList);

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);		
	}

	/**
	 * Set new vector of registers.
	 *
	 * @param registers Register vector to set.
	 */
	public void setRegisters(Vector<Register> registers)
	{
		// get currently selected register (if any)
		int index = registerList.getSelectedIndex();
		
		registerList.setListData(registers);
		
		// (re)set selected register
		if (index != -1)
			registerList.setSelectedIndex(index);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) 
		{
			int index = registerList.getSelectedIndex();
			
			if (index != -1)
			{
				// register selected
				Register reg = (Register) registerList.getModel().getElementAt(index);

				eventManager.sendEvent(EventManager.EVENT.REGISTER_VIEW_SELECT, reg);
			}
		}
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case REGISTER_CHANGE:
				Vector<Register> tmp = (Vector<Register>) obj;
				setRegisters( tmp );
				break;
		}
	}

	/**
	 * Custom cell renderer for the JList in the register view.
	 */
	class RegisterCellRenderer extends JLabel
												 implements ListCellRenderer {

			private Register regObj;

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

					this.setFont(registerList.getFont());

					this.regObj = (Register) value;
					setText("."); // trigger repaint
 
					if (isSelected)
					{ 
						switch (this.regObj.getState())
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
						
						setForeground(list.getSelectionForeground()); 
					} 
					else 
					{ 
						switch (this.regObj.getState())
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
						
						setForeground(list.getForeground());
					} 

					return this;
			}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isOpaque()) 
			{ 
					// paint background
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
			}

			FontMetrics metrics = g.getFontMetrics(getFont());
		
			if (regObj.getState() == Register.STATE.DISABLED)
				g.setColor(GUIManager.FONT_DISABLED);
			else
				g.setColor(Color.black);
			
			g.drawString(regObj.getName(), 2, 11);

			String tmp = regObj.getValueAsString();	

			g.drawString(tmp, getWidth() - metrics.stringWidth(tmp) - 2, 11);
		}

	}

}