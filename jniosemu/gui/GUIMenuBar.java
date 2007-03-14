package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;

import jniosemu.events.*;

/**
 * Creates and manages menu bar in GUI.
 */
public class GUIMenuBar extends JMenuBar
												implements ActionListener {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * GUI component constructor.
	 *
	 * @param  eventManager
	 */

	/**
	 * Initiates the creation of this GUI component.
	 *
	 * @post      eventManager reference is set for this object.
	 * @calledby  GUIManager.initGUI()
	 * @calls     setup()
	 *
	 * @param  eventManager  The Event Manager object.
	 */
	public GUIMenuBar(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      menu items created and added to menu bar
	 * @calledby  GUIMenuBar
	 * @calls     createMenuItem()
	 */
	private void setup()
	{
		JMenu menu;

		// File menu
		menu = new JMenu("File");
		this.add(menu);

		menu.add( createMenuItem("New", Events.EVENTID_NEW, 
		          KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)) );
		menu.add( createMenuItem("Open", Events.EVENTID_OPEN,
		          KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)) );
		menu.add( createMenuItem("Save", Events.EVENTID_SAVE,
		          KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)) );
		          
		menu.addSeparator();
		menu.add( createMenuItem("Exit", Events.EVENTID_EXIT) );

		// Emulator menu
		menu = new JMenu("Emulator");
		this.add(menu);

		menu.add( createMenuItem("Compile", Events.EVENTID_GUI_COMPILE,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK)) );
		menu.add( createMenuItem("Run", Events.EVENTID_RUN,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0)) );
		menu.add( createMenuItem("Pause", Events.EVENTID_PAUSE) );
		menu.add( createMenuItem("Step", Events.EVENTID_STEP,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0)) );
		menu.add( createMenuItem("Reset", Events.EVENTID_RESET,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK)) );

		// View menu
		menu = new JMenu("View");
		this.add(menu);

		menu.add( createMenuItem("Memory...", Events.EVENTID_VIEW_MEMORY) );
		menu.add( createMenuItem("Variables...", Events.EVENTID_VIEW_VARIABLES) );

		menu.addSeparator();
		menu.add( createMenuItem("Toggle Editor/Emulator", Events.EVENTID_TOGGLE_TAB,
		          KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK)) );
		
		this.add(Box.createHorizontalGlue());

		// Help menu
		menu = new JMenu("Help");
		this.add(menu);

		menu.add( createMenuItem("About...", Events.EVENTID_ABOUT) );
	}

	/**
	 * Helper method to create menu item.
	 *
	 * @checks    If keyStroke is null, then don't set accelerator key.
	 * @calledby  setup(), createMenuItem()
	 *
	 * @param  text           text label of menu item
	 * @param  actionCommand  command for action
	 * @param  keyStroke      accelerator key
	 * @return                instance of menu item
	 */
	private JMenuItem createMenuItem(String text, String actionCommand, KeyStroke keyStroke)
	{
		JMenuItem item = new JMenuItem(text);
		
		if (keyStroke != null)
			item.setAccelerator(keyStroke);
		
		item.setActionCommand(actionCommand);
		item.addActionListener(this);

		return item;
	}

	/**
	 * Helper method to create menu item.
	 *
	 * @calledby  setup()
	 *
	 * @param  text           text label of menu item
	 * @param  actionCommand  command for action
	 * @return                instance of menu item
	 */
	private JMenuItem createMenuItem(String text, String actionCommand)
	{
		return createMenuItem(text, actionCommand, null);
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