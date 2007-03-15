package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the dipswitches.
 */
 public class GUIIODipswitches extends JPanel
											 implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * Number of dipswitches to display.
	 */
	private static int DIPSWITCH_COUNT = 4;

	/**
	 * Array for GUI representation of each dipswitch.
	 */
	private JLabel[] dipswitches = new JLabel[DIPSWITCH_COUNT];

	/**
	 * Contains graphical icon image for dipswitch.
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
	public GUIIODipswitches(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		this.eventManager.addEventObserver(Events.EVENTID_UPDATE_DIPSWITCHES, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIIODipswitches
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
		icons[0] = new ImageIcon("graphics/io/dipswitch_unset.png");
		icons[1] = new ImageIcon("graphics/io/dipswitch_set.png");

		// create dipswitches		
		for(int i = 0; i < dipswitches.length; i++)
		{
			dipswitches[i] = new JLabel(icons[0]);
			
			final int dipswitchIndex = i;
			
			dipswitches[i].addMouseListener(
				new MouseAdapter()
      	{
        	public void mouseClicked(MouseEvent e) {
        		// send event to toggle dipswitch
        		eventManager.sendEvent(Events.EVENTID_GUI_DIPSWITCHES, new Integer(dipswitchIndex));
        	}
      	}
      );
			
			this.add(dipswitches[i]);
		}
	}

	/**
	 * Set state of dipswitch.
	 *
	 * @checks   Verify that dipswitchIndex is a valid index. If not, print
	 *           warning and return.
	 * @calledby updateDipswitches
	 *
	 * @param  dipswitchIndex  index of dipswitch
	 * @param  state           state to set
	 */
	public void setDipswitch(int dipswitchIndex, boolean state)
	{
		if (dipswitchIndex < 0 || dipswitchIndex >= DIPSWITCH_COUNT)
		{
			System.out.println("GUIIODipswitches.setDipswitch(): Invalid dipswitchIndex " + dipswitchIndex);
			return;
		}
			
		if (state)
			dipswitches[dipswitchIndex].setIcon(icons[1]);
		else
			dipswitches[dipswitchIndex].setIcon(icons[0]);
	}

	/**
	 * Update dipswitch states.
	 *
	 * @calls     setDipswitch()
	 * @calledby  update()
	 *
	 * @param  states  states of available dipswitches
	 */
	public void updateDipswitches(Vector<Boolean> states)
	{
		for(int i = 0; i < states.size(); i++)
		{
			setDipswitch(i, states.get(i).booleanValue());
		}
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_UPDATE_DIPSWITCHES))
		{
			updateDipswitches( (Vector<Boolean>) obj );
		}
	}

}