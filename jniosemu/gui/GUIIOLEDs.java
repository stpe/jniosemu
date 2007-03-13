package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the LEDs.
 */
 public class GUIIOLEDs extends JPanel
											 implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	private static int LED_COUNT = 4;

	private JLabel[] leds = new JLabel[LED_COUNT];

	private ImageIcon[] icons = new ImageIcon[2];

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
	public GUIIOLEDs(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		String[] events = {
			Events.EVENTID_COMPILATION_DONE,
			Events.EVENTID_PC_CHANGE
		};
		this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIIOLEDs
	 */
	private void setup()
	{
		// load image icons
		icons[0] = new ImageIcon("graphics/io/led_off.png");
		icons[1] = new ImageIcon("graphics/io/led_on.png");

		// create LEDs		
		for(int i = 0; i < leds.length; i++)
		{
			leds[i] = new JLabel(icons[0]);
			this.add(leds[i]);
		}
	}

	/**
	 *
	 */
	public void setLed(int ledIndex, int value)
	{
		if (ledIndex < 0 || ledIndex >= LED_COUNT)
		{
			System.out.println("GUIIOLEDs.setLED(): Invalid ledIndex " + ledIndex);
			return;
		}
			
		if (value == 0)
			leds[ledIndex].setIcon(icons[0]);
		else
			leds[ledIndex].setIcon(icons[1]);
	}

	/**
	 *
	 */
	public void resetLeds()
	{
		for(int i = 0; i < leds.length; i++)
		{
			setLed(i, 0);
		}
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_UPDATE_LEDS))
		{
			// setLed()
		}
		else if (eventIdentifier.equals(Events.EVENTID_RESET))
		{
			resetLeds();
		}
	}

	/**
	 * Invoked when a GUI action occurs, forwards it as
	 * an event to the EventManager object.
	 *
	 * @calls     EventManager.sendEvent()
	 *
	 * @param  e  action event object
	 */
	public void actionPerformed(ActionEvent e) {
			eventManager.sendEvent(e.getActionCommand());
	}

}