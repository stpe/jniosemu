package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the emulator view.
 */
 public class GUIEmulator extends JPanel
											 implements ActionListener, EventObserver {

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
			Events.EVENTID_COMPILATION_DONE,
			Events.EVENTID_PC_CHANGE
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
		listView.setCellRenderer(new EmulatorCellRenderer());

		// scrollbars
		JScrollPane scrollPane = new JScrollPane(listView);

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Set program object to be displayed in emulator view.
	 *
	 * @calledby  update()
	 *
	 * @param  prg Program object
	 */
	private void setProgram(Program prg)
	{
		listView.setListData( prg.getProgramLines() );
	}

	/**
	 * Set which line in the emulator view corresponds to the
	 * instruction where the program counter is pointing to
	 * and updates the visual indication.
	 *
	 * @checks    Only ensure index is visible (by scrolling)
	 *            if it is not negative.
	 * @calledby  update()
	 *
	 * @param  index Index of line in program
	 */
	private void setProgramCounterIndicator(int index)
	{
		currentIndex = index;

		if (index != -1)
		{
			listView.ensureIndexIsVisible(index);
		}

		listView.repaint();
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_COMPILATION_DONE))
		{
			setProgram( (Program) obj );
		}
		else if (eventIdentifier.equals(Events.EVENTID_PC_CHANGE))
		{
			setProgramCounterIndicator( ((Integer) obj).intValue() );
		}
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


	/**
	 * Custom cell renderer for the JList in the register view.
	 */
	class EmulatorCellRenderer extends JLabel
												 implements ListCellRenderer {

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

					if (isSelected) {
							setBackground(list.getSelectionBackground());
							setForeground(list.getSelectionForeground());
					} else {
							setBackground(list.getBackground());
							setForeground(list.getForeground());
					}

					// indicate program counter
					if (index == currentIndex)
						setBackground(new Color(255, 255, 0));

					return this;
			}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isOpaque())
			{
					// paint background
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
			}

			g.setColor(new Color(0, 0, 0));

			int yOffset = 11;

			if (lineObj.getOpCode() != null)
				g.drawString(lineObj.getOpCode(), 2, yOffset);

			if (lineObj.getInstruction() != null)
				g.drawString(lineObj.getInstruction(), 120, yOffset);

			if (lineObj.getSourceCodeLine() != null)
				g.drawString(lineObj.getSourceCodeLine(), 300, yOffset);
		}

	}

}
