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
		this.eventManager.addEventObserver(Events.EVENTID_COMPILATION_DONE, this);
	}


	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIEmulator
	 */
	private void setup()
	{
		// emulator listview
		listView = new JList();
		listView.setFont(new Font("Monospaced", Font.PLAIN, 12));
		listView.setCellRenderer(new EmulatorCellRenderer());

		Vector<ProgramLine> programLines = new Vector<ProgramLine>();
		setListModel(programLines);

		// scrollbars
		JScrollPane scrollPane = new JScrollPane(listView);

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 *
	 */
	private void setListModel(Vector programLines)
	{
		listView.setListData(programLines);
	}

	/**
	 *
	 */
	private void setProgram(Program prg)
	{
		setListModel( prg.getProgramLines() );
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_COMPILATION_DONE))
		{
			setProgram( (Program) obj );
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

			FontMetrics metrics = g.getFontMetrics(getFont());

			g.setColor(new Color(0, 0, 0));

			int yOffset = 11;
			
			String tmp = null;
			
			if (lineObj.getOpCode() != null)
				g.drawString(lineObj.getOpCode(), 2, yOffset);

			if (lineObj.getInstruction() != null)
				g.drawString(lineObj.getInstruction(), 120, yOffset);
			
			if (lineObj.getSourceCodeLine() != null)
				g.drawString(lineObj.getSourceCodeLine(), 300, yOffset);


//			String tmp = "0x" + this.lineObj.toString();	

//			g.drawString(tmp, getWidth()-metrics.stringWidth(tmp), 11);
		}

	}

}