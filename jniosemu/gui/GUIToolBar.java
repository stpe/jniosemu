package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;

import jniosemu.events.*;

/**
 * Toolbar in GUI.
 */
public class GUIToolBar extends JToolBar
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
	 * @calledby  GUIManager.setup()
	 * @calls     setup()
	 *
	 * @param  eventManager  The Event Manager object.
	 */
	public GUIToolBar(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      buttons created and added to toolbar
	 * @calledby  GUIToolBar
	 * @calls     makeButton()
	 */
	private void setup()
	{
		this.setFloatable(false);
		this.setRollover(true);

		JButton button = null;

		button = makeButton("new", Events.EVENTID_NEW,
												"Create new document",
												"New");
		this.add(button);

		button = makeButton("open", Events.EVENTID_OPEN,
												"Open document",
												"Open...");
		this.add(button);

		button = makeButton("save", Events.EVENTID_SAVE,
												"Save document as",
												"Save");
		this.add(button);

		button = makeButton("saveas", Events.EVENTID_SAVE_AS,
												"Save as different file name",
												"Save As...");
		this.add(button);

		this.addSeparator();

		button = makeButton("compile", Events.EVENTID_GUI_COMPILE,
												"Compile source code",
												"Compile");
		this.add(button);

		button = makeButton("run", Events.EVENTID_RUN,
												"Run compiled code in emulator",
												"Run");
		this.add(button);

		button = makeButton("pause", Events.EVENTID_PAUSE,
												"Pause emulation",
												"Pause");
		this.add(button);

		button = makeButton("step", Events.EVENTID_STEP,
												"Step through and execute one instruction at a time",
												"Step");
		this.add(button);

		button = makeButton("reset", Events.EVENTID_RESET,
												"Reset emulator",
												"Reset");
		this.add(button);

		this.addSeparator();

		button = makeButton("variable_view", Events.EVENTID_VIEW_VARIABLES,
												"View variables",
												"Variable View");
		this.add(button);

		button = makeButton("memory_view", Events.EVENTID_VIEW_MEMORY,
												"View memory",
												"Memory View");
		this.add(button);
	}

	/**
	 * Creates a toolbar button.
	 *
	 * @calledby  GUIToolBar
	 *
	 * @param  imageName      filename of image
	 * @param  actionCommand  command for action
	 * @param  toolTipText    text to display when hover mouse over button
	 * @param  altText        a brief textual description of the image
	 * @return                instance of button
	 */
	private JButton makeButton(String imageName,
														 String actionCommand,
														 String toolTipText,
														 String altText) {
			// look for the image.
			String imgLocation = "graphics/toolbar/"
													 + imageName
													 + ".png";

			// create and initialize the button.
			JButton button = new JButton();
			button.setActionCommand(actionCommand);
			button.setToolTipText(toolTipText);
			button.addActionListener(this);

			button.setIcon(new ImageIcon(imgLocation, altText));

			return button;
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
