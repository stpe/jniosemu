package jniosemu.gui;

import jniosemu.events.*;

/**
 * Creates and manages the GUI component of the console.
 */
 public class GUIConsole extends GUIUART {

	/**
	 * Initiates the creation of GUI components and adds itself to
	 * the Event Manager as an observer.
	 *
	 * @post      eventManager reference is set for this object.
	 * @calledby  GUIManager.setup()
	 * @calls     setup(), EventManager.addEventObserver()
	 *
	 * @param  eventManager  The Event Manager object.
	 */
	public GUIConsole(EventManager eventManager)
	{
		super(
			eventManager,
			"UART_1",
			EventManager.EVENT.UART1_INPUT,
			EventManager.EVENT.UART1_OUTPUT
		);

		// setup GUI
		setup();
	}

}
