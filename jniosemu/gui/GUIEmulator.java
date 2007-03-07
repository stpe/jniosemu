/** 
 * Emulator.
 */
package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import jniosemu.events.*;

public class GUIEmulator extends JPanel 
                       implements ActionListener, EventObserver {

	private EventManager eventManager;

	private JList listView;

	/**
	 * Emulator constructor.
	 */
	public GUIEmulator(EventManager eventManager)
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
		// emulator listview
		String[] listRows = {"one", "two", "three", "four"};
		listView = new JList(listRows);

		// scrollbars
		JScrollPane scrollPane = new JScrollPane(listView);

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
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
//			editor.setText("");
		}
	}
	
	/**
	 * Invoked when a GUI action occurs.
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }

}