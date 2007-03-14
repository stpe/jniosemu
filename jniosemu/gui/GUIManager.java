package jniosemu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import jniosemu.events.*;

/**
 * The GUI Manager creates all components of the GUI and
 * dictates their layout. It also manages general actions
 * of the GUI not related to specific components.
 */
public class GUIManager
	implements EventObserver {

	/**
	 * Tab number of editor.
	 */
	public static final int TAB_EDITOR = 0;

	/**
	 * Tab number of emulator.
	 */
	public static final int TAB_EMULATOR = 1;

	/**
	 * Main frame of GUI.
	 */
	private JFrame frame;

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	/**
	 * The tabbed pane for the editor/emulator views.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Initiates the creation of GUI components and adds itself to
	 * the Event Manager as an observer.
	 *
	 * @post      eventManager reference is set for this object.
	 * @calledby  JNiosEmu
	 * @calls     initGUI(), EventManager.addEventObserver()
	 *
	 * @param  eventManager  The Event Manager object.
	 */
	public GUIManager(EventManager eventManager)
	{
		this.eventManager = eventManager;

		// add events to listen to
		String[] events = {
			Events.EVENTID_EXCEPTION,
			Events.EVENTID_CHANGE_TAB,
			Events.EVENTID_TOGGLE_TAB,
			Events.EVENTID_EXIT,
			Events.EVENTID_ABOUT,
			Events.EVENTID_COMPILATION_DONE,
			Events.EVENTID_CHANGE_WINDOW_TITLE,
			Events.EVENTID_VIEW_VARIABLES,
			Events.EVENTID_VIEW_MEMORY
		};
		this.eventManager.addEventObserver(events, this);

		initGUI();
	}

	/**
	 * Creates the GUI frame and initates creation of sub-components.
	 *
	 * @post      Application frame instance is created.
	 * @calledby  GUIManager()
	 * @calls     setup()
	 */
	private void initGUI()
	{
		// the main panel will include all components
		JPanel mainPanel = new JPanel();

		// create and set up the window.
		frame = new JFrame("JNiosEmu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(mainPanel);

		setup(mainPanel);

		// menu
		GUIMenuBar menubar = new GUIMenuBar(eventManager);
		frame.setJMenuBar(menubar);

		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	/**
	 * Create the Swing frame and its content.
	 *
	 * @pre       Main panel instance created.
	 * @post      GUI componenets added to main panel object.
	 * @calledby  initGUI()
	 * @calls     <i>All GUI Components</i>
	 *
	 * @param  mainPanel  panel that will include all components
	 */
	private void setup(JPanel mainPanel)
	{
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// toolbar
		GUIToolBar toolBar = new GUIToolBar(this.eventManager);
		mainPanel.add(toolBar, BorderLayout.PAGE_START);

		// editor panel
		JPanel editorPanel = new JPanel(new BorderLayout());
		editorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// editor
		GUIEditor editor = new GUIEditor(this.eventManager);
		editorPanel.add(editor, BorderLayout.CENTER);

		// editor messages
		GUIEditorMessages editorMessages = new GUIEditorMessages(this.eventManager);
		editorPanel.add(editorMessages, BorderLayout.PAGE_END);

		// emulator panel
		JPanel emulatorPanel = new JPanel(new BorderLayout());
		emulatorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// emulator
		GUIEmulator emulator = new GUIEmulator(this.eventManager);
		emulatorPanel.add(emulator, BorderLayout.CENTER);

		// emulator: left panel
		JPanel emulatorLeftPanel = new JPanel(new BorderLayout());

		GUIPCView pcView = new GUIPCView(this.eventManager);
		emulatorLeftPanel.add(pcView, BorderLayout.PAGE_START);
		
		GUIRegisters registers = new GUIRegisters(this.eventManager);
		emulatorLeftPanel.add(registers, BorderLayout.CENTER);
		
		emulatorPanel.add(emulatorLeftPanel, BorderLayout.LINE_START);

		// emulator: right panel
		JPanel emulatorRightPanel = new JPanel(new BorderLayout());
		
		JPanel ioPanel = new JPanel();
		ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.PAGE_AXIS));

		GUIIOButtons buttonPanel = new GUIIOButtons(this.eventManager);
		ioPanel.add(buttonPanel);

		GUIIODipswitches dipswitchesPanel = new GUIIODipswitches(this.eventManager);
		ioPanel.add(dipswitchesPanel);
		
		GUIIOLEDs ledPanel = new GUIIOLEDs(this.eventManager);
		ioPanel.add(ledPanel);
		
		emulatorRightPanel.add(ioPanel, BorderLayout.PAGE_END);
		emulatorRightPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);
		
		emulatorPanel.add(emulatorRightPanel, BorderLayout.LINE_END);

		// tabs
		tabbedPane = new JTabbedPane();

		tabbedPane.addTab(
			"Editor",
			new ImageIcon("graphics/tabs/editor.png"),
			editorPanel,
			"Edit source file"
		);

		tabbedPane.addTab(
			"Emulator",
			new ImageIcon("graphics/tabs/emulator.png"),
			emulatorPanel,
			"Monitor emulation"
		);

		mainPanel.add(tabbedPane, BorderLayout.CENTER);
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_EXIT))
			exitFile();
		else
			if (eventIdentifier.equals(Events.EVENTID_ABOUT))
				showAbout();
		else
			if (eventIdentifier.equals(Events.EVENTID_EXCEPTION))
				showException( (Exception) obj );
		else
			if (eventIdentifier.equals(Events.EVENTID_CHANGE_TAB))
				changeTab( ((Integer) obj).intValue() );
		else
			if (eventIdentifier.equals(Events.EVENTID_TOGGLE_TAB))
				toggleTab();
		else
			if (eventIdentifier.equals(Events.EVENTID_COMPILATION_DONE))
				changeTab( new Integer(TAB_EMULATOR) );
		else
			if (eventIdentifier.equals(Events.EVENTID_CHANGE_WINDOW_TITLE))
				setFrameTitle( (String) obj );
		else
			if (eventIdentifier.equals(Events.EVENTID_VIEW_MEMORY))
				showMemoryView();
		else
			if (eventIdentifier.equals(Events.EVENTID_VIEW_VARIABLES))
				showVariableView();
	}

	/**
	 * Exit the application.
	 *
	 * @post      Application is shutdown.
	 * @calledby  update()
	 * @calls     EventManager.sendEvent()
	 */
	private void exitFile()
	{
		System.exit(0);
	}

	/**
	 * Show application About window.
	 *
	 * @calledby  update()
	 */
	private void showAbout()
	{
		JOptionPane.showMessageDialog(
			frame,
      "JNiosEmu",
			"About",
			JOptionPane.INFORMATION_MESSAGE
		);
	}

	/**
	 * Display information about exception and output
	 * stacktrace to stdout.
	 *
	 * @calledby  update()
	 *
	 * @param  e  Exception that occured
	 */
	private void showException(Exception e)
	{
		e.printStackTrace();

		JOptionPane.showMessageDialog(
			frame,
      "Exception",
			e.getMessage(),
			JOptionPane.ERROR_MESSAGE
		);
	}

	/**
	 * Change selected tab between Editor and Emulator.
	 *
	 * @calledby  update(), toggleTab()
	 *
	 * @param  tabIndex  Index of tab
	 */
	private void changeTab(int tabIndex)
	{
		tabbedPane.setSelectedIndex(tabIndex);
	}

	/**
	 * Toggle selected tab between Editor and Emulator.
	 *
	 * @calledby  update()
	 *
	 * @param  tabIndex  Index of tab
	 */
	private void toggleTab()
	{
		changeTab(1 - tabbedPane.getSelectedIndex());
	}

	/**
	 * Set title of application window.
	 *
	 * @calledby  update()
	 *
	 * @param  title  Window title
	 */
	private void setFrameTitle(String title)
	{
		frame.setTitle("JNiosEmu - [" + title + "]");
	}

	/**
	 * Show Variables View window.
	 *
	 * @calls     GUIVariableyView
	 * @calledby  update()
	 */
	private void showVariableView()
	{
		GUIVariableView frame = new GUIVariableView(this.eventManager);
		frame.setSize(new Dimension(200, 260));
		frame.setVisible(true);
	}
	
	/**
	 * Show Memory View window.
	 *
	 * @calls     GUIMemoryView
	 * @calledby  update()
	 */	
	private void showMemoryView()
	{
		GUIMemoryView frame = new GUIMemoryView(this.eventManager);
		frame.setSize(new Dimension(200, 260));
		frame.setVisible(true);		
	}

}


