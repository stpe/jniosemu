package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.Utilities;

/**
 * Creates and manages the GUI component of the PC (Program Counter) view.
 */
 public class GUIPCView extends JPanel
											  implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * Label that displays the Program Counter address.
	 */
	private JLabel pcLabel;

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
	public GUIPCView(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();
		setPC(0);

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.PROGRAMCOUNTER_CHANGE,
			EventManager.EVENT.EMULATOR_RESET,
			EventManager.EVENT.EMULATOR_CLEAR
		};

		this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIPCView
	 */
	private void setup()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		pcLabel = new JLabel();
		
		this.add(pcLabel);
	}

	/**
	 * Set Program Counter address in GUI.
	 *
	 * @calledby update();
	 *
	 * @param  addr  Address of program counter
	 */
	public void setPC(int addr)
	{
		pcLabel.setText("PC: " + Utilities.intToHexString(addr));
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case PROGRAMCOUNTER_CHANGE:
				setPC( ((Integer) obj).intValue());
				break;
			case EMULATOR_RESET:
			case EMULATOR_CLEAR:
				setPC(0);
				break;
		}
	}

}