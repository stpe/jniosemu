/** 
 * GUI for code editor.
 */
package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import jniosemu.events.*;

public class GUIEditor extends JPanel 
                       implements ActionListener, EventObserver {
	
	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;
	
	private JTextArea editor;
	
	/**
	 * GUI component constructor.
	 *
	 * @param  eventManager
	 */
	public GUIEditor(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
		// add events to listen to
    this.eventManager.addEventObserver(Events.EVENTID_NEW, this);		
	}

	/**
	 * Setup GUI components and attributes.
	 */	
	private void setup()
	{
		editor = new JTextArea();

		// put scrollbars around editor text area
		JScrollPane editorScrollPane =
		    new JScrollPane(editor,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(editorScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Receive incoming events from event manager.
	 *
	 * @param  eventIdentifier String identifying the event
	 * @param  obj             Object associated with event by sender
	 */
	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_NEW))
		{
			editor.setText("");
		}
	}
	
	/**
	 * Invoked when a GUI action occurs.
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }
	
}