package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import jniosemu.events.*;

/** 
 * Creates and manages the GUI component of the editor message view.
 */
public class GUIEditorMessages extends JPanel 
                       implements ActionListener, EventObserver {
	
	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;
	
	/**
	 * The text area used to display messages.
	 */
	private JTextArea editorMessages;
	
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
	public GUIEditorMessages(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
    // add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.COMPILER_COMPILE,
			EventManager.EVENT.COMPILER_ERROR
		};
    this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIEditorMessages
	 */
	private void setup()
	{
		editorMessages = new JTextArea("", 5, 60);
		editorMessages.setEditable(false);
		editorMessages.setFont(new Font("Monospaced", Font.PLAIN, 12));

		// put scrollbars around editor text area
		JScrollPane editorMessagesScrollPane =
		    new JScrollPane(editorMessages,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(editorMessagesScrollPane, BorderLayout.CENTER);
		this.setVisible(false);
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case COMPILER_COMPILE:
				editorMessages.setText("");
				this.setVisible(false);
				break;
			case COMPILER_ERROR:
				// append to the end
				editorMessages.append((String) obj + "\n");
				
				// move caret to last position to force scroll
				editorMessages.setCaretPosition(editorMessages.getDocument().getLength());
				
				this.setVisible(true);
				break;
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
		try {
			EventManager.EVENT event = this.eventManager.getEvent(e.getActionCommand());
			eventManager.sendEvent(event);
		} catch (EventException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

}
