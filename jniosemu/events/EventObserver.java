/*
 * Classes that want to listen to events must implement
 * this interface. When an event is triggered the update
 * method is called in all listening objects.
 */
package jniosemu.events;

public interface EventObserver {

	void update(String eventIdentifier, Object obj);
	
}
