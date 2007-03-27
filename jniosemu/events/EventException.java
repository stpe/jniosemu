package jniosemu.events;

/**
 * Exception used to throw event related errors.
 */
public class EventException extends Exception
{
	public EventException() {
		super();
	}

	public EventException(String msg) {
		super(msg);
	}
}
