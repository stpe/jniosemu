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
	 * Save document as different file name.
	 */	
	public static final String EVENTID_SAVE_AS = "SAVE_AS";

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
	 * Show Memory View window.
	 */	
	public static final String EVENTID_VIEW_MEMORY = "VIEW_MEMORY";

	/**
	 * Show Variables View window.
	 */	
	public static final String EVENTID_VIEW_VARIABLES = "VIEW_VARIABLES";
	
	/**
	 * Exception has occured.
	 */
	public static final String EVENTID_EXCEPTION = "EXCEPTION";
	
	/**
	 * Change tab in GUI.
	 */
	public static final String EVENTID_CHANGE_TAB = "CHANGE_TAB";
	
	/**
	 * Toggles between view the emulator or editor.
	 */		
	public static final String EVENTID_TOGGLE_TAB = "TOGGLE_TAB";
	
	/**
	 * Compilation of current source code successfully done.
	 */
	public static final String EVENTID_EMULATION_READY = "EMULATION_READY";

	/**
	 * Program counter in emulated program has changed.
	 */
	public static final String EVENTID_PC_CHANGE = "PC_CHANGE";	

	/**
	 * Breakpoint has been changed.
	 */
	public static final String EVENTID_TOGGLE_BREAKPOINT = "TOGGLE_BREAKPOINT";

	/**
	 * Breakpoint has been toggled in GUI.
	 */
	public static final String EVENTID_GUI_TOGGLE_BREAKPOINT = "GUI_TOGGLE_BREAKPOINT";

	/**
	 * Regiser in emulated program has changed.
	 */
	public static final String EVENTID_REGISTER_CHANGE = "REGISTER_CHANGE";	
	
	/**
	 * Trigger change of window title (used when text in editor is modified).
	 */
	public static final String EVENTID_CHANGE_WINDOW_TITLE = "CHANGE_WINDOW_TITLE";

	/**
	 * Update state of leds.
	 */
	public static final String EVENTID_UPDATE_LEDS = "UPDATE_LEDS";

	/**
	 * Update state of buttons.
	 */
	public static final String EVENTID_UPDATE_BUTTONS = "UPDATE_BUTTONS";

	/**
	 * Update state of dipswitches.
	 */
	public static final String EVENTID_UPDATE_DIPSWITCHES = "UPDATE_DIPSWITCHES";

	/**
	 * IO button in GUI was pressed.
	 */
	public static final String EVENTID_GUI_BUTTON_PRESSED = "GUI_BUTTON_PRESSED";

	/**
	 * IO button in GUI was released.
	 */
	public static final String EVENTID_GUI_BUTTON_RELEASED = "GUI_BUTTON_RELEASED";

	/**
	 * IO button in GUI was toggled.
	 */
	public static final String EVENTID_GUI_BUTTON_TOGGLE = "GUI_BUTTON_TOGGLE";

	/**
	 * IO dipswitches in GUI has changed.
	 */
	public static final String EVENTID_GUI_DIPSWITCHES = "GUI_DIPSWITCHES";

	/**
	 * Error occured during compilation.
	 */
	public static final String EVENTID_COMPILE_ERROR = "COMPILE_ERROR";

	/**
	 * Error occured during emulation.
	 */
	public static final String EVENTID_RUNTIME_ERROR = "RUNTIME_ERROR";

	/**
	 * Cursor changed in editor.
	 */
	public static final String EVENTID_EDITOR_CURSOR_CHANGE = "EDITOR_CURSOR_CHANGE";

	/**
	 * Insert instruction using insert menu.
	 */
	public static final String EVENTID_INSERT_INSTRUCTION = "INSERT_INSTRUCTION";

	/**
	 * Emulation started.
	 */
	public static final String EVENTID_EMULATION_START = "EMULATION_START";

	/**
	 * Emulation stoped.
	 */
	public static final String EVENTID_EMULATION_STOP = "EMULATION_STOP";

	/**
	 * Emulation ended.
	 */
	public static final String EVENTID_EMULATION_END = "EMULATION_END";
}
