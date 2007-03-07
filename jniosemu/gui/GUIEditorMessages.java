package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import jniosemu.events.*;

public class GUIEditorMessages extends JPanel 
                       implements ActionListener, EventObserver {
	
	private EventManager eventManager;
	
	private JTextArea editorMessages;
	
	/**
	 * Editor constructor.
	 */
	public GUIEditorMessages(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
    // add events to listen to
    this.eventManager.addEventObserver("DEBUG", this);
	}

	/**
	 * Setup GUI components and attributes.
	 */	
	private void setup()
	{
		editorMessages = new JTextArea("Editor messages...\n", 5, 60);
		editorMessages.setEditable(false);

		// put scrollbars around editor text area
		JScrollPane editorMessagesScrollPane =
		    new JScrollPane(editorMessages,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(editorMessagesScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Receive incoming events from event manager.
	 *
	 * @param  eventIdentifier String identifying the event
	 * @param  obj             Object associated with event by sender
	 */
	public void update(String eventIdentifier, Object obj)
	{
		String debugMessage = (String) obj;
		
		editorMessages.append(eventIdentifier + ": " + debugMessage + "\n");
	}

	/**
	 * Invoked when a GUI action occurs.
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }
	
}