package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the emulator view.
 */
 public class GUIEmulator extends JPanel
											 implements MouseListener, EventObserver {

	/**
	 * Defines the width of the breakpoint column. 
	 */
	private static int BREAKPOINT_AREA_WIDTH = 18;

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * List component used to display emulator code.
	 */
	private JList listView;

	/**
	 * Current line index of program counter.
	 */
	private int currentIndex;
	
	/**
	 * Current program that is emulated.
	 */
	private Program program;

	/**
	 * Icon for breakpoint that is set (active).
	 */
	private ImageIcon breakPointSetIcon;

	/**
	 * Icon for breakpoint that is unset.
	 */
	private ImageIcon breakPointUnsetIcon;

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
	public GUIEmulator(EventManager eventManager)
	{
		super();

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		String[] events = {
			Events.EVENTID_EMULATION_READY,
			Events.EVENTID_PC_CHANGE,
			Events.EVENTID_TOGGLE_BREAKPOINT
		};
		this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIEmulator
	 */
	private void setup()
	{
		currentIndex = 0;

		// emulator listview
		listView = new JList();
		listView.setFont(new Font("Monospaced", Font.PLAIN, 12));
		listView.setBackground(Color.WHITE);
		listView.setCellRenderer(new EmulatorCellRenderer());

		listView.addMouseListener(this);

		// scrollbars
		JScrollPane scrollPane = new JScrollPane(listView,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		

		// get breakpoint images
		breakPointSetIcon = new ImageIcon("graphics/emulator/breakpoint_set.png");
		breakPointUnsetIcon = new ImageIcon("graphics/emulator/breakpoint_unset.png");

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Set program object to be displayed in emulator view.
	 *
	 * @calls     Program.getProgramLines()
	 * @calledby  update()
	 *
	 * @param  prg Program object
	 */
	private void setProgram(Program prg)
	{
		this.program = prg;
		
		listView.setListData( prg.getProgramLines() );
	}

	/**
	 * Set which address in the current emulated program
	 * the program counter is pointing to and update the
	 * visual indication.
	 *
	 * @pre       setProgram() must have been called to set current
	 *            Program instance before PC may be set.
	 * @checks    Only ensure index is visible (by scrolling)
	 *            if it is not negative.
	 * @calls     Program.getLineNumber()
	 * @calledby  update()
	 *
	 * @param  addr Address of program counter
	 */
	private void setProgramCounterIndicator(int addr)
	{
		currentIndex = program.getLineNumber(addr);

		if (currentIndex != -1)
		{
			listView.ensureIndexIsVisible(currentIndex);
		}

		listView.repaint();
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_EMULATION_READY))
		{
			setProgram( (Program) obj );
		}
		else if (eventIdentifier.equals(Events.EVENTID_PC_CHANGE))
		{
			setProgramCounterIndicator( ((Integer) obj).intValue() );
		}
		else if (eventIdentifier.equals(Events.EVENTID_TOGGLE_BREAKPOINT))
		{
			listView.repaint();
		}
	}

	/**
	 * Listens to mouse clicks in the list. If the mouse click is
	 * in the breakpoint column of the emulator, a toggle breakpoint
	 * event is sent.
	 *
	 * @calls  EventManager.sendEvent();
	 *
	 * @param  e  MouseEvent object for the click
	 */
	public void mouseClicked(MouseEvent e) 
	{
		if (e.getX() <= BREAKPOINT_AREA_WIDTH)
		{
			int index = listView.locationToIndex(e.getPoint());
			eventManager.sendEvent(Events.EVENTID_GUI_TOGGLE_BREAKPOINT, new Integer(index));
		}
	}

	/**
	 * Not used, empty method. Enforced by MouseListener interface.
	 */
	public void mouseEntered(MouseEvent e) { }

	/**
	 * Not used, empty method. Enforced by MouseListener interface.
	 */
	public void mouseExited(MouseEvent e) { }

	/**
	 * Not used, empty method. Enforced by MouseListener interface.
	 */
	public void mousePressed(MouseEvent e) { }

	/**
	 * Not used, empty method. Enforced by MouseListener interface.
	 */
	public void mouseReleased(MouseEvent e) { }

	/**
	 * Custom cell renderer for the JList in the emulator view.
	 */
	class EmulatorCellRenderer extends JLabel
												 implements ListCellRenderer {

			/**
			 * ProgramLine object for the current cell that is drawn.
			 */
			private ProgramLine lineObj;
			
			public EmulatorCellRenderer() {
					setOpaque(true);
					setHorizontalAlignment(CENTER);
					setVerticalAlignment(CENTER);
			}

			public Component getListCellRendererComponent(
																				 JList list,
																				 Object value,
																				 int index,
																				 boolean isSelected,
																				 boolean cellHasFocus) {

					this.setFont(listView.getFont());

					this.lineObj = (ProgramLine) value;
					setText("."); // trigger repaint

					/*
					if (isSelected) {
							setBackground(list.getSelectionBackground());
							setForeground(list.getSelectionForeground());
					} else {
					*/
							setBackground(list.getBackground());
							setForeground(list.getForeground());
					// }

					// indicate program counter
					/*
					if (index == currentIndex)
						setBackground(new Color(255, 255, 0));
					*/

					return this;
			}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isOpaque())
			{
				ProgramLine.SIBLINGSTATUS status = lineObj.isSibling(currentIndex);

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				if (status != ProgramLine.SIBLINGSTATUS.NONE) {
					g.setColor(new Color(255, 255, 0));
					g.fillRect(3, 0, getWidth()-6, getHeight());
				}

				switch (status) {
					case FIRST:
						g.setColor(new Color(255, 255, 220));
						g.fillRect(4, 1, getWidth()-8, getHeight()-1);
						break;
					case MIDDLE:
						g.setColor(new Color(255, 255, 220));
						g.fillRect(4, 0, getWidth()-8, getHeight());
						break;
					case LAST:
						g.setColor(new Color(255, 255, 220));
						g.fillRect(4, 0, getWidth()-8, getHeight()-1);
						break;
				}
			}

			g.setColor(new Color(0, 0, 0));

			int yOffset = 12;
			
			// draw breakpoint
			switch (lineObj.getBreakPoint())
			{
				case TRUE:
					g.drawImage(breakPointSetIcon.getImage(), 3, 0, null);
					break;
				case FALSE:
					g.drawImage(breakPointUnsetIcon.getImage(), 3, 0, null);
					break;
			}

			if (lineObj.getOpCode() != null)
				g.drawString(lineObj.getOpCode(), BREAKPOINT_AREA_WIDTH, yOffset);

			if (lineObj.getInstruction() != null)
				g.drawString(lineObj.getInstruction(), 120, yOffset);

			if (lineObj.getSourceCodeLine() != null)
				g.drawString(lineObj.getSourceCodeLine(), 300, yOffset);
		}

	}

}
