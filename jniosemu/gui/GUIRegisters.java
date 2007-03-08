package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import jniosemu.events.*;

/** 
 * Creates and manages the GUI component of the register view in the emulator.
 */
public class GUIRegisters extends JPanel 
											 implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * List component used to display register values.
	 */
	private JList registerList;
	
	/**
	 * ListModel used by registerList.
	 */
	private DefaultListModel listModel;

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
		this.eventManager.addEventObserver(Events.EVENTID_NEW, this);		
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIRegisters
	 */	
	private void setup()
	{
		this.setPreferredSize(new Dimension(70, 0));

		// registers
		listModel = new DefaultListModel();
		for (int i = 1; i < 25; i++)
		{
			listModel.addElement(new Integer(i));	
		}

		registerList = new JList(listModel);
		registerList.setCellRenderer(new RegisterCellRenderer());
		
		// scrollbars
		JScrollPane scrollPane = new JScrollPane(registerList);

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);		
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_NEW))
		{
//			editor.setText("");
		}
	}
	
	/**
	 * Invoked when a GUI action occurs, forwards it as
	 * an event to the EventManager object.
	 *
	 * @calls     EventManager.sendEvent()
	 *
	 * @param  e  action event object
	 */
	public void actionPerformed(ActionEvent e) {
			eventManager.sendEvent(e.getActionCommand());
	}

	/**
	 * Custom cell renderer for the JList in the register view.
	 */
	class RegisterCellRenderer extends JLabel
												 implements ListCellRenderer {

			private Object registerObject;

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

					this.registerObject = value;
					setText("."); // trigger repaint

					if (isSelected) {
							setBackground(list.getSelectionBackground());
							setForeground(list.getSelectionForeground());
					} else {
							setBackground(list.getBackground());
							setForeground(list.getForeground());
					}

					if (index % 5 == 0)
					{
						setBackground(new Color(100, 255, 100));
					}
					if (index % 7 == 0)
					{
						setBackground(new Color(255, 100, 100));
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

			g.setColor(new Color(0, 0, 255));
			g.drawString("#" + this.registerObject.toString(), 2, 11);

			String tmp = "0x" + this.registerObject.toString();	

			g.drawString(tmp, getWidth()-metrics.stringWidth(tmp), 11);
		}

	}

}