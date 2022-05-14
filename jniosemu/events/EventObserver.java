package jniosemu.events;

/*
 * Classes that want to listen to events must implement
 * this interface. When an event is triggered the update
 * method is called in all listening objects.
 */
public interface EventObserver {

  /**
   * Receive incoming events from event manager.
   *
   * @calledby EventManager.sendEvent()
   * @param eventIdentifier String identifying the event
   * @param obj Object associated with event by sender
   */
  public void update(EventManager.EVENT eventIdentifier, Object obj);
}
