package jniosemu.events;

/**
 * The purpose of this class is to contain all
 * available event ID's that are used as identifiers.
 */
public class Events {

	/**
	 * New document.
	 */	
	public static final String EVENTID_NEW = "NEW";

	/**
	 * Open document.
	 */	
	public static final String EVENTID_OPEN = "OPEN";

	/**
	 * Save document.
	 */	
	public static final String EVENTID_SAVE = "SAVE";

	/**
	 * Exit application.
	 */	
	public static final String EVENTID_EXIT = "EXIT";


	/**
	 * Show About window of application.
	 */	
	public static final String EVENTID_ABOUT = "ABOUT";
	

	/**
	 * Start compiling.
	 */	
	public static final String EVENTID_COMPILE = "COMPILE";

	/**
	 * Start emulation.
	 */	
	public static final String EVENTID_RUN = "RUN"; 

	/**
	 * Pause emulation.
	 */	
	public static final String EVENTID_PAUSE = "PAUSE";

	/**
	 * Step emulation one instruction.
	 */	
	public static final String EVENTID_STEP = "STEP";

	/**
	 * Reset emulator.
	 */	
	public static final String EVENTID_RESET = "RESET";	
	

	/**
	 * Show View Memory window.
	 */	
	public static final String EVENTID_VIEW_MEMORY = "VIEW_MEMORY";
}
