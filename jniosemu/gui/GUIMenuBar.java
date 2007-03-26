package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages menu bar in GUI.
 */
public class GUIMenuBar extends JMenuBar
												implements ActionListener {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

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

		item = createMenuItem("New", EventManager.EVENT.DOCUMENT_NEW.toString(), 
		          KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.DOCUMENT_NEW, item);
		menu.add(item);
		
		item = createMenuItem("Open...", EventManager.EVENT.DOCUMENT_OPEN.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.DOCUMENT_OPEN, item);
		menu.add(item);
		
		item = createMenuItem("Save", EventManager.EVENT.DOCUMENT_SAVE.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.DOCUMENT_SAVE, item);
		menu.add(item);
		
		item = createMenuItem("Save As...", EventManager.EVENT.DOCUMENT_SAVE_AS.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		stateManager.addItem(EventManager.EVENT.DOCUMENT_SAVE_AS, item);
		menu.add(item);
		

		menu.addSeparator();
		item = createMenuItem("Exit", EventManager.EVENT.APPLICATION_EXIT.toString());
		menu.add(item);

		// Insert instruction menu
		
		this.add( setupInstructions(stateManager) );

		// Emulator menu
		menu = new JMenu("Emulator");
		this.add(menu);

		item = createMenuItem("Compile", EventManager.EVENT.COMPILER_COMPILE_INIT.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.COMPILER_COMPILE_INIT, item);
		menu.add(item);
		
		item = createMenuItem("Run", EventManager.EVENT.EMULATOR_RUN.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		stateManager.addItem(EventManager.EVENT.EMULATOR_RUN, item);
		menu.add(item);
		
		item = createMenuItem("Pause", EventManager.EVENT.EMULATOR_PAUSE.toString());
		stateManager.addItem(EventManager.EVENT.EMULATOR_PAUSE, item);
		menu.add(item);
		
		item = createMenuItem("Step", EventManager.EVENT.EMULATOR_STEP.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		stateManager.addItem(EventManager.EVENT.EMULATOR_STEP, item);
		menu.add(item);
		
		item = createMenuItem("Reset", EventManager.EVENT.EMULATOR_RESET.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.EMULATOR_RESET, item);
		menu.add(item);
		
		menu.addSeparator();

		// execution speed submenu		
		JMenu submenu = new JMenu("Execution Speed");
		menu.add(submenu);
		
		ButtonGroup speedGroup = new ButtonGroup();
		
		item = new JRadioButtonMenuItem("Full", true);
		item.setActionCommand(EmulatorManager.SPEED.FULL.toString());
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);
		
		item = new JRadioButtonMenuItem("Normal");
		item.setActionCommand(EmulatorManager.SPEED.NORMAL.toString());
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);
		item = new JRadioButtonMenuItem("Slow");
		item.setActionCommand(EmulatorManager.SPEED.SLOW.toString());
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);

		// View menu
		menu = new JMenu("View");
		this.add(menu);

		item = createMenuItem("Variables...", EventManager.EVENT.VARIABLE_VIEW.toString());
		stateManager.addItem(EventManager.EVENT.VARIABLE_VIEW, item);
		menu.add(item);
		
		item = createMenuItem("Memory...", EventManager.EVENT.MEMORY_VIEW.toString());
		stateManager.addItem(EventManager.EVENT.MEMORY_VIEW, item);
		menu.add(item);
		

		menu.addSeparator();
		item = createMenuItem("Toggle Editor/Emulator", EventManager.EVENT.APPLICATION_TAB_TOGGLE.toString(),
		          KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.APPLICATION_TAB_TOGGLE, item);
		menu.add(item);
		
		this.add(Box.createHorizontalGlue());

		// Help menu
		menu = new JMenu("Help");
		this.add(menu);

		item = createMenuItem("About...", EventManager.EVENT.ABOUT_VIEW.toString());
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
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(EmulatorManager.SPEED.FULL.toString()))
		{
			eventManager.sendEvent(EventManager.EVENT.EMULATOR_SPEED, EmulatorManager.SPEED.FULL);
		}
		else if (e.getActionCommand().equals(EmulatorManager.SPEED.NORMAL.toString()))
		{
			eventManager.sendEvent(EventManager.EVENT.EMULATOR_SPEED, EmulatorManager.SPEED.NORMAL);
		}
		else if (e.getActionCommand().equals(EmulatorManager.SPEED.SLOW.toString()))
		{
			eventManager.sendEvent(EventManager.EVENT.EMULATOR_SPEED, EmulatorManager.SPEED.SLOW);
		} 
		else
		{
			try 
			{
				EventManager.EVENT event = this.eventManager.getEvent(e.getActionCommand());
				eventManager.sendEvent(event);
			} 
			catch (Exception ex) 
			{
				
			}
		}
	}

}