package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the LEDs.
 */
 public class GUIIOLEDs extends JPanel
											 implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * Number of LEDs to display.
	 */
	private static int LED_COUNT = 4;

	/**
	 * Array for GUI representation of each LED.
	 */
	private JLabel[] leds = new JLabel[LED_COUNT];

	/**
	 * Contains graphical icon image for LED.
	 */
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
		this.eventManager.addEventObserver(Events.EVENTID_UPDATE_LEDS, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIIOLEDs
	 */
	private void setup()
	{
		this.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(3, 3, 0, 0),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
			)
		);
		
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
	 * Set state of led.
	 *
	 * @checks   Verify that ledIndex is a valid index. If not, print
	 *           warning and return.
	 * @calledby updateLeds
	 *
	 * @param  ledIndex  index of led
	 * @param  state     state to set
	 */
	public void setLed(int ledIndex, boolean state)
	{
		if (ledIndex < 0 || ledIndex >= LED_COUNT)
		{
			System.out.println("GUIIOLEDs.setLED(): Invalid ledIndex " + ledIndex);
			return;
		}
			
		if (state)
			leds[ledIndex].setIcon(icons[1]);
		else
			leds[ledIndex].setIcon(icons[0]);
	}

	/**
	 * Update led states.
	 *
	 * @calls     setLed()
	 * @calledby  update()
	 *
	 * @param  states  states of available leds 
	 */
	public void updateLeds(Vector<Boolean> states)
	{
		for(int i = 0; i < states.size(); i++)
		{
			setLed(i, states.get(i).booleanValue());
		}
	}
	
	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_UPDATE_LEDS))
		{
			updateLeds( (Vector<Boolean>) obj );
		}
	}
}
