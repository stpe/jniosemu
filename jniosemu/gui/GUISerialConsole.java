package jniosemu.gui;

import jniosemu.events.*;

/**
 * Creates and manages the GUI component of the serial console.
 */
 public class GUISerialConsole extends GUIUART {

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
	public GUISerialConsole(EventManager eventManager)
	{
		super(
			eventManager,
			"UART_0",
			EventManager.EVENT.UART0_INPUT,
			EventManager.EVENT.UART0_OUTPUT
		);

		// setup GUI
		setup();
	}

}