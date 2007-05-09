package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the console.
 */
 public class GUIConsole extends GUIUART
                              implements ActionListener, EventObserver {

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
