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
	 * Initiates the creation of this GUI component.
	 *
	 * @post      eventManager reference is set for this object.
	 * @calledby  GUIManager.initGUI()
	 * @calls     setup()
	 *
	 * @param  eventManager  the Event Manager object
	 * @param  stateManager  the State Manager object
	 */
	public GUIMenuBar(EventManager eventManager, StateManager stateManager)
	{
		super();

		this.eventManager = eventManager;

		setup(stateManager);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      menu items created and added to menu bar
	 * @calledby  GUIMenuBar
	 * @calls     createMenuItem(), setupInstructions(), StateManager.addItem()
	 *
	 * @param  stateManager  reference to State Manager
	 */
	private void setup(StateManager stateManager)
	{
		JMenu menu;

		// File menu
		menu = new JMenu("File");
		this.add(menu);

		JMenuItem item;

		item = createMenuItem("New", Events.EVENTID_NEW, 
		          KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		stateManager.addItem(Events.EVENTID_NEW, item);
		menu.add(item);
		
		item = createMenuItem("Open...", Events.EVENTID_OPEN,
		          KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		stateManager.addItem(Events.EVENTID_OPEN, item);
		menu.add(item);
		
		item = createMenuItem("Save", Events.EVENTID_SAVE,
		          KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		stateManager.addItem(Events.EVENTID_SAVE, item);
		menu.add(item);
		
		item = createMenuItem("Save As...", Events.EVENTID_SAVE_AS,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		stateManager.addItem(Events.EVENTID_SAVE_AS, item);
		menu.add(item);
		

		menu.addSeparator();
		item = createMenuItem("Exit", Events.EVENTID_EXIT);
		menu.add(item);

		// Insert instruction menu
		
		this.add( setupInstructions(stateManager) );

		// Emulator menu
		menu = new JMenu("Emulator");
		this.add(menu);

		item = createMenuItem("Compile", Events.EVENTID_GUI_COMPILE,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK));
		stateManager.addItem(Events.EVENTID_GUI_COMPILE, item);
		menu.add(item);
		
		item = createMenuItem("Run", Events.EVENTID_RUN,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		stateManager.addItem(Events.EVENTID_RUN, item);
		menu.add(item);
		
		item = createMenuItem("Pause", Events.EVENTID_PAUSE);
		stateManager.addItem(Events.EVENTID_PAUSE, item);
		menu.add(item);
		
		item = createMenuItem("Step", Events.EVENTID_STEP,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		stateManager.addItem(Events.EVENTID_STEP, item);
		menu.add(item);
		
		item = createMenuItem("Reset", Events.EVENTID_RESET,
		          KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));
		stateManager.addItem(Events.EVENTID_RESET, item);
		menu.add(item);
		

		// View menu
		menu = new JMenu("View");
		this.add(menu);

		item = createMenuItem("Variables...", Events.EVENTID_VIEW_VARIABLES);
		stateManager.addItem(Events.EVENTID_VIEW_VARIABLES, item);
		menu.add(item);
		
		item = createMenuItem("Memory...", Events.EVENTID_VIEW_MEMORY);
		stateManager.addItem(Events.EVENTID_VIEW_MEMORY, item);
		menu.add(item);
		

		menu.addSeparator();
		item = createMenuItem("Toggle Editor/Emulator", Events.EVENTID_TOGGLE_TAB,
		          KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		stateManager.addItem(Events.EVENTID_TOGGLE_TAB, item);
		menu.add(item);
		
		this.add(Box.createHorizontalGlue());

		// Help menu
		menu = new JMenu("Help");
		this.add(menu);

		item = createMenuItem("About...", Events.EVENTID_ABOUT);
		menu.add(item);
	}

	/**
	 * Creates the Instruction menu that lists all available
	 * instructions categorized by functionality. When a menu item
	 * is selected the corresponding instruction will be inserted
	 * into the editor.
	 *
	 * @calledby  setup()
	 * @calls     StateManager.addItem()
	 *
	 * @param  stateManager  reference to State Manager
	 * @return  Instruction menu
	 */
	private JMenu setupInstructions(StateManager stateManager)
	{
		JMenu menu = new JMenu("Instruction");
		
		JMenu submenu = new JMenu("Arithmetic & Logical");
		
		menu.add(submenu);


		submenu = new JMenu("Move");
		
		menu.add(submenu);


		submenu = new JMenu("Comparison");
		
		menu.add(submenu);


		submenu = new JMenu("Shift & Rotate");
		
		menu.add(submenu);


		submenu = new JMenu("Program Control");
		
		menu.add(submenu);
		
		
		submenu = new JMenu("Data Transfer");
		
		menu.add(submenu);


		submenu = new JMenu("Other");
		
		menu.add(submenu);
		
		
		return menu;
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