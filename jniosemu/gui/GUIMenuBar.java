/** 
 * Menu bar in GUI.
 */
package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;

import jniosemu.events.*;

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
	public GUIMenuBar(EventManager eventManager)
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
    JMenu menu;
    JMenuItem item;

		// File menu
    menu = new JMenu("File");
    this.add(menu);

		menu.add( createMenuItem("New", Events.EVENTID_NEW) );
		menu.add( createMenuItem("Open", Events.EVENTID_OPEN) );
		menu.add( createMenuItem("Save", Events.EVENTID_SAVE) );
		menu.addSeparator();
		menu.add( createMenuItem("Exit", Events.EVENTID_EXIT) );

		// Emulator menu
    menu = new JMenu("Emulator");
    this.add(menu);
		
		menu.add( createMenuItem("Compile", Events.EVENTID_COMPILE) );
		menu.add( createMenuItem("Run", Events.EVENTID_RUN) );
		menu.add( createMenuItem("Pause", Events.EVENTID_PAUSE) );
		menu.add( createMenuItem("Step", Events.EVENTID_STEP) );
		menu.add( createMenuItem("Reset", Events.EVENTID_RESET) );

		// View menu
    menu = new JMenu("View");
    this.add(menu);

		menu.add( createMenuItem("Memory...", Events.EVENTID_VIEW_MEMORY) );

    this.add(Box.createHorizontalGlue());

		// Help menu
    menu = new JMenu("Help");
    this.add(menu);

    menu.add( createMenuItem("About...", Events.EVENTID_ABOUT) );
	}

	/**
	 * Returns a created menu item.
	 */
	private JMenuItem createMenuItem(String text, String actionCommand)
	{
		JMenuItem item = new JMenuItem(text);
    item.setActionCommand(actionCommand);
    item.addActionListener(this);
    
    return item;
  }

	/**
	 * Invoked when a GUI action occurs.
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }
	
}