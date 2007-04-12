package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;
import jniosemu.Utilities;

/**
 * Creates and manages the GUI component of the buttons.
 */
 public class GUIIOButtons extends JPanel
											 implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * Number of buttons to display.
	 */
	private static int BUTTON_COUNT = 4;

	/**
	 * Array for GUI representation of each button.
	 */
	private JLabel[] buttons = new JLabel[BUTTON_COUNT];

	/**
	 * Contains graphical icon image for button.
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
	public GUIIOButtons(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		this.eventManager.addEventObserver(EventManager.EVENT.BUTTON_UPDATE, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIIOButtons
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
		icons[0] = new ImageIcon(Utilities.loadImage("graphics/io/button.png"));
		icons[1] = new ImageIcon(Utilities.loadImage("graphics/io/button_pressed.png"));

		// create buttons		
		for(int i = 0; i < buttons.length; i++)
		{
			buttons[i] = new JLabel(icons[0]);
			buttons[i].setToolTipText("SW" + (7 - i));
			
			final int buttonIndex = i;
			
			buttons[i].addMouseListener(
				new MouseAdapter()
      	{
      		/**
      		 * Send event when button is pressed.
      		 *
      		 * @checks Only send event if it is the left
      		 *         mouse button that is pressed.
      		 * @calls  EventManager.sendEvent()
      		 *
      		 * @param  e  event when mouse button is pressed
      		 */
        	public void mousePressed(MouseEvent e) {
        		if (e.getButton() == MouseEvent.BUTTON1) {
        			eventManager.sendEvent(EventManager.EVENT.BUTTON_PRESS, Integer.valueOf(buttonIndex));
        		}
        	}
        	
      		/**
      		 * Send event when button is released.
      		 *
      		 * @checks Only send event if it is the left
      		 *         mouse button that is released.
      		 * @calls  EventManager.sendEvent()
      		 *
      		 * @param  e  event when mouse button is released
      		 */
        	public void mouseReleased(MouseEvent e) {
        		if (e.getButton() == MouseEvent.BUTTON1) {
        			eventManager.sendEvent(EventManager.EVENT.BUTTON_RELEASE, Integer.valueOf(buttonIndex));
        		}
        	}
        	
      		/**
      		 * Send event when right mouse button is clicked. Used
      		 * to toggle state for button so user don't have to manually
      		 * press the button while stepping through code.
      		 *
      		 * @checks Only send event if it is the right
      		 *         mouse button that is clicked.
      		 * @calls  EventManager.sendEvent()
      		 *
      		 * @param  e  event when mouse button is clicked
      		 */
        	public void mouseClicked(MouseEvent e) {
        		if (e.getButton() == MouseEvent.BUTTON3) {
        			eventManager.sendEvent(EventManager.EVENT.BUTTON_TOGGLE, Integer.valueOf(buttonIndex));
        		}
        	}
      	}
      );			
			
			this.add(buttons[i]);
		}
	}

	/**
	 * Set state of button.
	 *
	 * @checks   Verify that buttonIndex is a valid index. If not, print
	 *           warning and return.
	 * @calledby updateButtons
	 *
	 * @param  buttonIndex  index of button
	 * @param  state        state to set
	 */
	public void setButton(int buttonIndex, boolean state)
	{
		if (buttonIndex < 0 || buttonIndex >= BUTTON_COUNT)
		{
			System.out.println("GUIIOButtons.setButton(): Invalid buttonIndex " + buttonIndex);
			return;
		}
			
		if (state)
			buttons[buttonIndex].setIcon(icons[1]);
		else
			buttons[buttonIndex].setIcon(icons[0]);
	}

	/**
	 * Update button states.
	 *
	 * @calls     setButton()
	 * @calledby  update()
	 *
	 * @param  states  states of available buttons 
	 */
	public void updateButtons(Vector<Boolean> states)
	{
		for(int i = 0; i < states.size(); i++)
		{
			setButton(i, states.get(i).booleanValue());
		}
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case BUTTON_UPDATE:
				updateButtons( (Vector<Boolean>) obj );
				break;
		}
	}

}