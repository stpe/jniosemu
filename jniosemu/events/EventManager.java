/**
 * EventManager manages events that may have several senders
 * and/or receivers. Events are identified by event
 * identifier strings to distinguish between different types
 * of events.
 * <p>
 * Classes that want to listen to events must implement the
 * EventObserver interface.
 * <p>
 * To listen to an event the object adds itself as an observer
 * and states which event identifier to listen to. It's possible
 * to listen to more than one event by adding the same object
 * several times using different event identifiers.
 * <p>
 * Anyone may send an event, simply call the sendEvent method.
 */
package jniosemu.events;

import java.util.*;

public class EventManager {
	
	Hashtable<String, ArrayList<EventObserver>> eventTable;
	
	public EventManager()
	{
		eventTable = new Hashtable<String, ArrayList<EventObserver>>();
	}
	
	/**
	 * Add event observer that listens to given identifier.
	 *
	 * @param  eventIdentifier String identifying the event
	 * @param  obj             Object that listens to the event
	 */
	public void addEventObserver(String eventIdentifier, EventObserver obj)
	{
		// create list of observers if it doesn't exist		
		if (!eventTable.containsKey(eventIdentifier))
		{
			eventTable.put(
				eventIdentifier,
				new ArrayList<EventObserver>()
			);
		}
		
		// get list of existing observers
		ArrayList<EventObserver> eventObservers = eventTable.get(eventIdentifier);
		
		// add observer to list
		eventObservers.add(obj);
	}
		
	/**
	 * Notifies all observers that listens to eventIdentifier.
	 *
	 * @param  eventIdentifier String identifying the event
	 * @param  obj             Object to pass along to the observer
	 */	
	public void sendEvent(String eventIdentifier, Object obj)
	{
		// get list of observers
		ArrayList<EventObserver> eventObservers = eventTable.get(eventIdentifier);
		
		if (eventObservers == null)
		{
			// debug (omit println)
			System.out.println("EventManger.sendEvent(): No observers listening to '" + eventIdentifier + "'.");
			return;
		}
		
		// iterate over all listening observers
		for (EventObserver eventObserver : eventObservers)
		{
			eventObserver.update(eventIdentifier, obj);
		}
	}
	
	/**
	 * Notifies all observers that listens to eventIdentifier but
	 * without passing along any object.
	 *
	 * @param  eventIdentifier String identifying the event
	 */
	public void sendEvent(String eventIdentifier)
	{
		sendEvent(eventIdentifier, null);
	}
	
}