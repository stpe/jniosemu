package jniosemu.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;

/**
 * Creates and manages the GUI component of the status bar.
 */
 public class GUIStatusBar extends JPanel
											  implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * Label that display current cursor position in editor.
	 */
	private JLabel cursorLabel;

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
	public GUIStatusBar(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		this.eventManager.addEventObserver(Events.EVENTID_EDITOR_CURSOR_CHANGE, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIStatusBar
	 */
	private void setup()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2, 0, 0, 0),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
			)
		);
				
		cursorLabel = new JLabel();
		setCursorLabel(new Point(1,1));
		
		this.add(cursorLabel);
	}

	/**
	 * Update displayed cursor coordinates in status bar.
	 *
	 * @calledby update();
	 *
	 * @param  p  point representing row and column of cursor
	 */
	public void setCursorLabel(Point p)
	{
		cursorLabel.setText("Line " + (int) p.getX() + ", Column " + (int) p.getY());
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_EDITOR_CURSOR_CHANGE))
		{
			setCursorLabel( (Point) obj );
		}
	}

}