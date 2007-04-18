package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import jniosemu.events.*;

/** 
 * Creates and manages the GUI component of the emulator message view.
 */
public class GUIEmulatorMessages extends JPanel 
                       implements EventObserver {
	
	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;
	
	/**
	 * List to contain messages.
	 */	
	private JList msgList;
	
	/**
	 * List model used by the JList.
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
	public GUIEmulatorMessages(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
    // add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.EMULATOR_READY,
			EventManager.EVENT.EMULATOR_ERROR
		};
    this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIEmulatorMessages
	 */
	private void setup()
	{
		listModel = new DefaultListModel();
		
		msgList = new JList(listModel);
		msgList.setBackground(Color.WHITE);
		msgList.setFont(new Font("Monospaced", Font.PLAIN, 11));

		// put scrollbars around editor text area
		JScrollPane emulatorMessagesScrollPane =
		    new JScrollPane(msgList,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(emulatorMessagesScrollPane, BorderLayout.CENTER);
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case EMULATOR_READY:
				listModel.clear();
				break;
			case EMULATOR_ERROR:
				String[] result = ((String) obj).split("\n");
				for (int x = 0; x < result.length; x++)
				{
					// convert tabs to spaces
					result[x] = result[x].replaceAll("\t", "    ");
					
					listModel.addElement(result[x]);
				}
				
				break;
		}
	}

}
