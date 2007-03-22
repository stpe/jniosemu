package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;

/**
 * Creates and manages the GUI component of the PC (Program Counter) view.
 */
 public class GUIPCView extends JPanel
											  implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * Label that displays the Program Counter address.
	 */
	private JLabel pcLabel;

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
	public GUIPCView(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();
		setPC(0);

		// add events to listen to
		this.eventManager.addEventObserver(Events.EVENTID_PC_CHANGE, this);
		this.eventManager.addEventObserver(Events.EVENTID_RESET, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIPCView
	 */
	private void setup()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		pcLabel = new JLabel();
		
		this.add(pcLabel);
	}

	/**
	 * Set Program Counter address in GUI.
	 *
	 * @calledby update();
	 *
	 * @param  addr  Address of program counter
	 */
	public void setPC(int addr)
	{
		pcLabel.setText("PC: " + addr);
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_PC_CHANGE))
		{
			setPC( ((Integer) obj).intValue());
		}
		else if (eventIdentifier.equals(Events.EVENTID_RESET))
		{
			setPC(0);
		}
	}

}