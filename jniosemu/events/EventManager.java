package jniosemu.events;

import java.util.*;

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
 * Any object may send an event by simply calling the sendEvent
 * method.
 */
public class EventManager 
{
	/**
	 * Manages which observers are listening to which event.
	 */
	Hashtable<String, ArrayList<EventObserver>> eventTable;
	
	/**
	 * Creates an instance of eventTable.
	 */
	public EventManager()
	{
		eventTable = new Hashtable<String, ArrayList<EventObserver>>();
	}
	
	/**
	 * Add event observer that listens to given event identifier.
	 *
	 * @pre       eventTable instance must exist.
	 * @post      Observer added to eventTable.
	 * @checks
	 * @calledby  <i>EventObserver</i>
	 * @calls
	 *
	 * @param  eventIdentifier  String identifying the event
	 * @param  obj              Object that listens to the event
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
	 * Notifies all observers that listens to given event identifier.
	 *
	 * @pre       Observers must be added eventTable.
	 * @post 
	 * @checks    That there are <i>EventObservers</i> listening to
	 *            to the particular event. If none, the event is
	 *            not sent.
	 * @calledby  <i>All objects that sends an event</i>
	 * @calls     update() method of all <i>EventObservers</i>
	 *
	 * @param  eventIdentifier  String identifying the event
	 * @param  obj              Object to pass along to the observer
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
	 * @calls    sendEvent() (overloaded method)
	 *	 
	 * @param  eventIdentifier  String identifying the event
	 */
	public void sendEvent(String eventIdentifier)
	{
		sendEvent(eventIdentifier, null);
	}
	
}