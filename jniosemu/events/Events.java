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
	 * New document created.
	 */	
	public static final String EVENTID_NEW_DONE = "NEW_DONE";

	/**
	 * A document has been opened.
	 */	
	public static final String EVENTID_OPENED = "OPENED";

	/**
	 * Document has been saved.
	 */	
	public static final String EVENTID_SAVED = "SAVED";

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
	 * Start compile requested from GUI.
	 */	
	public static final String EVENTID_GUI_COMPILE = "GUI_COMPILE";

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
	
	/**
	 * Exception has occured.
	 */
	public static final String EVENTID_EXCEPTION = "EXCEPTION";
	
	/**
	 * Change tab in GUI.
	 */
	public static final String EVENTID_CHANGE_TAB = "CHANGE_TAB";
	
	/**
	 * Compilation of current source code successfully done.
	 */
	public static final String EVENTID_COMPILATION_DONE = "COMPILATION_DONE";	
}
