/** 
 * Toolbar in GUI.
 */
package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;

import jniosemu.events.*;

public class GUIToolBar extends JToolBar
                        implements ActionListener {
	
	private EventManager eventManager;
	
	/**
	 * GUI component constructor.
	 *
	 * @param  eventManager
	 */
	public GUIToolBar(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;
		
		setup();
	}

	/**
	 * Setup GUI components and attributes.
	 */
	private void setup()
	{
		this.setFloatable(false);
		this.setRollover(true);

		JButton button = null;

		button = makeNavigationButton("new", Events.EVENTID_NEW,
                                  "Create new empty source file",
                                  "New");
		this.add(button);
		
		button = makeNavigationButton("open", Events.EVENTID_OPEN,
                                  "Open existing source file",
                                  "Open");
		this.add(button);
	
		button = makeNavigationButton("save", Events.EVENTID_SAVE,
                                  "Save source file",
                                  "Save");
		this.add(button);
		
		this.addSeparator();
		
		button = makeNavigationButton("compile", Events.EVENTID_COMPILE,
                                  "Compile source code",
                                  "Compile");
		this.add(button);

		button = makeNavigationButton("run", Events.EVENTID_RUN,
                                  "Run compiled code in emulator",
                                  "Run");
		this.add(button);

		button = makeNavigationButton("pause", Events.EVENTID_PAUSE,
                                  "Pause emulation.",
                                  "Pause");
		this.add(button);

		button = makeNavigationButton("step", Events.EVENTID_STEP,
                                  "Step through and execute one instruction at a time",
                                  "Step");
		this.add(button);

		button = makeNavigationButton("reset", Events.EVENTID_RESET,
                                  "Reset emulator",
                                  "Reset");
		this.add(button);
	}

	/**
	 * Creates a toolbar button.
	 */
	private JButton makeNavigationButton(String imageName,
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
	 * Invoked when a GUI action occurs.
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }

}
