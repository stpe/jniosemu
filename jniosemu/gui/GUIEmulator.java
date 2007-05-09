package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.lang.Math;

import jniosemu.events.*;
import jniosemu.emulator.*;
import jniosemu.Utilities;

/**
 * Creates and manages the GUI component of the emulator view.
 */
 public class GUIEmulator extends JPanel
											 implements MouseListener, EventObserver {

	/**
	 * Defines the width, offset and offset in percent for columns
	 * in the emulator view.
	 */
	private static int BREAKPOINT_AREA_WIDTH = 18;
	private static int INSTRUCTION_OFFSET = 110;
	private static float INSTRUCTION_OFFSET_PERCENT = 0.20f;
	private static int SOURCECODE_OFFSET = 250;
	private static float SOURCECODE_OFFSET_PERCENT = 0.50f;

	/**
	 * Colors used for highlighting currently executed line
	 * in emulatov view.
	 */
	private static Color CURRENT_LINE_COLOR = new Color(255, 255, 0);
	private static Color SIBLING_LINE_COLOR = new Color(255, 255, 220);

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

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
	private transient Program program;

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
		EventManager.EVENT[] events = {
			EventManager.EVENT.EMULATOR_BREAKPOINT_UPDATE,
			EventManager.EVENT.EMULATOR_READY,
			EventManager.EVENT.EMULATOR_CLEAR,
			EventManager.EVENT.PROGRAMCOUNTER_CHANGE,
			EventManager.EVENT.PROGRAM_CHANGE
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
		listView.setCellRenderer(
			new EmulatorCellRenderer(
				listView.getFontMetrics(listView.getFont())
			)
		);

		listView.addMouseListener(this);

		// scrollbars
		JScrollPane scrollPane = new JScrollPane(listView,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		

		// get breakpoint images
		breakPointSetIcon = new ImageIcon(Utilities.loadImage("graphics/emulator/breakpoint_set.png"));
		breakPointUnsetIcon = new ImageIcon(Utilities.loadImage("graphics/emulator/breakpoint_unset.png"));

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
		
		if (prg == null)
		{
			// clear emulator view
			listView.setModel(new DefaultListModel());
			return;
		}
		
		// set new program lines as data for jlist
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
		if (program == null)
			return;
			
		currentIndex = program.getLineNumber(addr);

		if (currentIndex != -1)
		{
			listView.ensureIndexIsVisible(currentIndex);
		}
		else
		{
			eventManager.sendEvent(EventManager.EVENT.PROGRAM_REQUEST_UPDATE);
		}
		
		listView.repaint();
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case EMULATOR_BREAKPOINT_UPDATE:
				listView.repaint();
				break;
			case EMULATOR_READY:
			case PROGRAM_CHANGE:
				setProgram( (Program) obj );
				break;
			case PROGRAMCOUNTER_CHANGE:
				setProgramCounterIndicator( ((Integer) obj).intValue() );
				break;
			case EMULATOR_CLEAR:
				setProgram(null);
				break;
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
			eventManager.sendEvent(EventManager.EVENT.EMULATOR_BREAKPOINT_TOGGLE, Integer.valueOf(index));
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
	class EmulatorCellRenderer extends JPanel
												 implements ListCellRenderer {

		/**
		 * ProgramLine object for the current cell that is drawn.
		 */
		private ProgramLine lineObj;
		
		private final int baseline;
		private final int width;
    private final int height;
		
		public EmulatorCellRenderer(FontMetrics metrics) {
			super();
			setOpaque(true);
			setFont(listView.getFont());
			System.out.println("cell font name: " + metrics.getFont().getName() + ", size: " + metrics.getFont().getSize());
			this.baseline = metrics.getAscent();
			this.height = metrics.getHeight();
			this.width = listView.getWidth();
			System.out.println("metrics baseline: " + this.baseline + ", height: " + this.height + ", width: " + this.width);
		}

    /** 
     * Return the renderers fixed size.  
     */
		public Dimension getPreferredSize()
		{
			return new Dimension(width, height);
		}
		
		/**
		 * Cell rendered methos sets background/foreground
		 * color and stores ProgramLine for row.
		 */
		public Component getListCellRendererComponent(
																			 JList list,
																			 Object value,
																			 int index,
																			 boolean isSelected,
																			 boolean cellHasFocus)
		{
			this.lineObj = (ProgramLine) value;

			setBackground(list.getBackground());
			setForeground(list.getForeground());

			return this;
		}

		/**
		 * Custom paint method bypassing standard JComponent
		 * painting to optimize performance.
		 */
		public void paintComponent(Graphics g) 
		{
			// clear background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());

			// current executing program line highlight
			ProgramLine.SIBLINGSTATUS status = lineObj.isSibling(currentIndex);

			if (status != ProgramLine.SIBLINGSTATUS.NONE) {
				g.setColor(CURRENT_LINE_COLOR);
				g.fillRect(3, 0, getWidth()-6, getHeight());
			}

			switch (status) {
				case FIRST:
					g.setColor(SIBLING_LINE_COLOR);
					g.fillRect(4, 1, getWidth()-8, getHeight()-1);
					break;
				case MIDDLE:
					g.setColor(SIBLING_LINE_COLOR);
					g.fillRect(4, 0, getWidth()-8, getHeight());
					break;
				case LAST:
					g.setColor(SIBLING_LINE_COLOR);
					g.fillRect(4, 0, getWidth()-8, getHeight()-1);
					break;
			}

			g.setColor(getForeground());

			int xOffset = 3;

			// breakpoint
			switch (lineObj.getBreakPoint())
			{
				case TRUE:
					g.drawImage(breakPointSetIcon.getImage(), xOffset, 1, null);
					break;
				case FALSE:
					g.drawImage(breakPointUnsetIcon.getImage(), xOffset, 1, null);
					break;
			}

			xOffset = BREAKPOINT_AREA_WIDTH;
			
			// opcode
			if (lineObj.getOpCode() != null)
				g.drawString(lineObj.getOpCode(), xOffset, this.baseline);

			// instruction
			if (lineObj.getInstruction() != null)
			{
				xOffset = (int) Math.max(INSTRUCTION_OFFSET, INSTRUCTION_OFFSET_PERCENT * getWidth());
				
				g.drawString(lineObj.getInstruction(), xOffset, this.baseline);
			}

			// source line
			if (lineObj.getSourceCodeLine() != null)
			{
				xOffset = (int) Math.max(SOURCECODE_OFFSET, SOURCECODE_OFFSET_PERCENT * getWidth());

				g.drawString(lineObj.getSourceCodeLine(), xOffset, this.baseline);
			}
		}

	}

}
