package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the buttons.
 */
 public class GUIIOButtons extends JPanel
											 implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	private static int BUTTON_COUNT = 4;

	private JLabel[] buttons = new JLabel[BUTTON_COUNT];

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
		icons[0] = new ImageIcon("graphics/io/button.png");
		icons[1] = new ImageIcon("graphics/io/button_pressed.png");

		// create buttons		
		for(int i = 0; i < buttons.length; i++)
		{
			buttons[i] = new JLabel(icons[0]);
			this.add(buttons[i]);
		}
	}

	/**
	 *
	 */
	public void setButton(int buttonIndex, int value)
	{
		if (buttonIndex < 0 || buttonIndex >= BUTTON_COUNT)
		{
			System.out.println("GUIIOButtons.setButton(): Invalid buttonIndex " + buttonIndex);
			return;
		}
			
		if (value == 0)
			buttons[buttonIndex].setIcon(icons[0]);
		else
			buttons[buttonIndex].setIcon(icons[1]);
	}

	/**
	 *
	 */
	public void resetButtons()
	{
		for(int i = 0; i < buttons.length; i++)
		{
			setButton(i, 0);
		}
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_UPDATE_BUTTONS))
		{
			// setButton()
		}
		else if (eventIdentifier.equals(Events.EVENTID_RESET))
		{
			resetButtons();
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