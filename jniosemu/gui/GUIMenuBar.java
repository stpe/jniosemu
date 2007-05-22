package jniosemu.gui;

import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

import jniosemu.events.*;
import jniosemu.emulator.*;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.InstructionSyntax;

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
	 * Delimter character used in action command to separate
	 * event from value.
	 */
	private static final String DELIMITER_CHAR = "|";

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

		// Edit menu
		menu = new JMenu("Edit");
		this.add(menu);

		item = createMenuItem("Undo", EventManager.EVENT.EDITOR_UNDO.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.EDITOR_UNDO, item);
		menu.add(item);

		item = createMenuItem("Redo", EventManager.EVENT.EDITOR_REDO.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		stateManager.addItem(EventManager.EVENT.EDITOR_REDO, item);
		menu.add(item);


		// Insert instruction menu
		this.add( setupInstructions(stateManager) );

		// Emulator menu
		menu = new JMenu("Emulator");
		this.add(menu);

		item = createMenuItem("Assemble", EventManager.EVENT.COMPILER_COMPILE_INIT.toString(),
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

		item = createMenuItem("Step Over", EventManager.EVENT.EMULATOR_STEP_OVER.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		stateManager.addItem(EventManager.EVENT.EMULATOR_STEP_OVER, item);
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

		item = new JRadioButtonMenuItem("Slow");
		item.setActionCommand(EventManager.EVENT.EMULATOR_SPEED + DELIMITER_CHAR + EmulatorManager.SPEED.SLOW);
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);

		item = new JRadioButtonMenuItem("Normal", true);
		item.setActionCommand(EventManager.EVENT.EMULATOR_SPEED + DELIMITER_CHAR + EmulatorManager.SPEED.NORMAL);
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);

		item = new JRadioButtonMenuItem("Fast");
		item.setActionCommand(EventManager.EVENT.EMULATOR_SPEED + DELIMITER_CHAR + EmulatorManager.SPEED.FAST);
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);

		item = new JRadioButtonMenuItem("Ultra");
		item.setActionCommand(EventManager.EVENT.EMULATOR_SPEED + DELIMITER_CHAR + EmulatorManager.SPEED.ULTRA);
		item.addActionListener(this);
		speedGroup.add(item);
		submenu.add(item);


		// View menu
		menu = new JMenu("View");
		this.add(menu);

		item = createMenuItem("Variables...", EventManager.EVENT.VARIABLE_VIEW.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		stateManager.addItem(EventManager.EVENT.VARIABLE_VIEW, item);
		menu.add(item);

		item = createMenuItem("Memory...", EventManager.EVENT.MEMORY_VIEW.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		stateManager.addItem(EventManager.EVENT.MEMORY_VIEW, item);
		menu.add(item);

		menu.addSeparator();

		item = createMenuItem("UART 0...", EventManager.EVENT.UART0_VIEW.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		stateManager.addItem(EventManager.EVENT.UART0_VIEW, item);
		menu.add(item);

		item = createMenuItem("UART 1...", EventManager.EVENT.UART1_VIEW.toString(),
							KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		stateManager.addItem(EventManager.EVENT.UART1_VIEW, item);
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
	 *
	 * @param   stateManager  reference to State Manager
	 * @return  instruction menu
	 */
	private JMenu setupInstructions(StateManager stateManager)
	{
		// create instruction category submenus
		JMenu arithmeticLogicalMenu = new JMenu("Arithmetic & Logical");
		JMenu moveMenu              = new JMenu("Move");
		JMenu comparisonMenu        = new JMenu("Comparison");
		JMenu shiftRotateMenu       = new JMenu("Shift & Rotate");
		JMenu programControlMenu    = new JMenu("Program Control");
		JMenu dataTransferMenu      = new JMenu("Data Transfer");
		JMenu otherMenu             = new JMenu("Other");

		JMenu submenu;

		// add instructions to category submenus
		ArrayList<InstructionSyntax> instructions = InstructionManager.getAllInstructionSyntax();

		if (instructions != null) {
			for (InstructionSyntax instruction : instructions)
			{
				// get submenu depending on instruction category
				switch (instruction.getCategory())
				{
					case ARITHMETIC_LOGICAL:
						submenu = arithmeticLogicalMenu;
						break;
					case MOVE:
						submenu = moveMenu;
						break;
					case COMPARISON:
						submenu = comparisonMenu;
						break;
					case SHIFT_ROTATE:
						submenu = shiftRotateMenu;
						break;
					case PROGRAM_CONTROL:
						submenu = programControlMenu;
						break;
					case DATA_TRANSFER:
						submenu = dataTransferMenu;
						break;
					default:
						submenu = otherMenu;
						break;
				}

				// add instruction to submenu
				submenu.add(
					createMenuItem(instruction.getName(), EventManager.EVENT.EDITOR_INSERT_INSTRUCTION + DELIMITER_CHAR + instruction.getName())
				);
			}
		}

		JMenu menu = new JMenu("Instruction");

		// add instruction categories as submenus
		menu.add(arithmeticLogicalMenu);
		menu.add(moveMenu);
		menu.add(comparisonMenu);
		menu.add(shiftRotateMenu);
		menu.add(programControlMenu);
		menu.add(dataTransferMenu);
		menu.add(otherMenu);

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
		String actionCommand = e.getActionCommand();
		String actionValue = null;

		int delimiterIndex = actionCommand.indexOf(DELIMITER_CHAR);

		// check if action command has value
		if (delimiterIndex != -1)
		{
			actionValue = actionCommand.substring(delimiterIndex + 1);
			actionCommand = actionCommand.substring(0, delimiterIndex);
		}

		// get event based on action command
		EventManager.EVENT event;
		try {
			event = this.eventManager.getEvent(actionCommand);
		} catch (EventException ex) {
			System.out.println("Error: " + ex.getMessage());
			return;
		}

		Object eventObj = actionValue;

		switch(event)
		{
			case EMULATOR_SPEED:
				eventObj = EmulatorManager.SPEED.NORMAL;

				if (actionValue.equals(EmulatorManager.SPEED.ULTRA.toString()))
				{
					eventObj = EmulatorManager.SPEED.ULTRA;
				}
				else if (actionValue.equals(EmulatorManager.SPEED.FAST.toString()))
				{
					eventObj = EmulatorManager.SPEED.FAST;
				}
				else if (actionValue.equals(EmulatorManager.SPEED.SLOW.toString()))
				{
					eventObj = EmulatorManager.SPEED.SLOW;
				}
				break;
		}

		eventManager.sendEvent(event, eventObj);
	}

}