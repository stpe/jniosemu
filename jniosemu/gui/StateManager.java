package jniosemu.gui;

import java.util.*;
import javax.swing.AbstractButton;

import jniosemu.events.*;

/**
 * Manages enabled/disabled state of toolbar buttons
 * and menu items.
 */
public class StateManager implements EventObserver
{
	/**
	 * Manages which items are associated to which event.
	 */
	Hashtable<String, ArrayList<AbstractButton>> stateTable;

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;
	
	/**
	 * Creates an instance of stateTable.
	 */
	public StateManager(EventManager eventManager)
	{
		stateTable = new Hashtable<String, ArrayList<AbstractButton>>();
		
		this.eventManager = eventManager;
		
		// add events to listen to
		String[] events = {
			Events.EVENTID_APPLICATION_START,
			Events.EVENTID_EMULATION_READY,
			Events.EVENTID_EMULATION_START,
			Events.EVENTID_EMULATION_STOP,
			Events.EVENTID_EMULATION_END
		};
		this.eventManager.addEventObserver(events, this);		
	}
	
	/**
	 * Add event observer that listens to given event identifier.
	 *
	 * @pre       stateTable instance must exist.
	 * @post      Item added to stateTable.
	 * @calledby  GUIToolBar.setup(), GUIMenuBar.setup()
	 *
	 * @param  eventIdentifier  String identifying the event
	 * @param  obj              Object that is associated with the event
	 */
	public void addItem(String eventIdentifier, AbstractButton obj)
	{
		// create list of observers if it doesn't exist		
		if (!stateTable.containsKey(eventIdentifier))
		{
			stateTable.put(
				eventIdentifier,
				new ArrayList<AbstractButton>()
			);
		}
		
		// get list of existing observers
		ArrayList<AbstractButton> AbstractButtons = stateTable.get(eventIdentifier);
		
		// add observer to list
		AbstractButtons.add(obj);
	}

	/**
	 * Overloaded variant of addItem that takes multiple
	 * eventIdentifiers.
	 *
	 * @param  eventIdentifiers  Array of string identifying the events
	 * @param  obj               Object that is associated with the event
	 */	
	public void addItem(String[] eventIdentifiers, AbstractButton obj)
	{
		for(String eventIdentifier : eventIdentifiers)
		{
			addItem(eventIdentifier, obj);
		}
	}

	/**
	 *
	 */	
	private void setEnabled(String eventIdentifier, boolean state)
	{
		// get list of observers
		ArrayList<AbstractButton> items = stateTable.get(eventIdentifier);
		
		if (items == null)
			return;
		
		// iterate over all items associated with event
		for (AbstractButton item : items)
		{
			item.setEnabled(state);
		}
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_APPLICATION_START))
		{
			setEnabled(Events.EVENTID_RUN, false);
			setEnabled(Events.EVENTID_PAUSE, false);
			setEnabled(Events.EVENTID_STEP, false);
			setEnabled(Events.EVENTID_RESET, false);
		}
		else if (eventIdentifier.equals(Events.EVENTID_EMULATION_READY))
		{
			setEnabled(Events.EVENTID_RUN, true);
			setEnabled(Events.EVENTID_PAUSE, false);
			setEnabled(Events.EVENTID_STEP, true);
			setEnabled(Events.EVENTID_RESET, false);
		} 
		else if (eventIdentifier.equals(Events.EVENTID_EMULATION_END))
		{
			setEnabled(Events.EVENTID_RUN, false);
			setEnabled(Events.EVENTID_PAUSE, false);
			setEnabled(Events.EVENTID_STEP, false);
			setEnabled(Events.EVENTID_RESET, true);
		}
		else if (eventIdentifier.equals(Events.EVENTID_EMULATION_START))
		{
			setEnabled(Events.EVENTID_RUN, false);
			setEnabled(Events.EVENTID_PAUSE, true);
			setEnabled(Events.EVENTID_STEP, false);
			setEnabled(Events.EVENTID_RESET, true);
		}
		else if (eventIdentifier.equals(Events.EVENTID_EMULATION_STOP))
		{
			setEnabled(Events.EVENTID_RUN, true);
			setEnabled(Events.EVENTID_PAUSE, false);
			setEnabled(Events.EVENTID_STEP, true);
			setEnabled(Events.EVENTID_RESET, false);
		}
	}	
}
