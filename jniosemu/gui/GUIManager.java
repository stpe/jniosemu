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
	private transient EventManager eventManager;

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
		EventManager.EVENT[] events = {
			EventManager.EVENT.ABOUT_VIEW,
			EventManager.EVENT.APPLICATION_TITLE_CHANGE,
			EventManager.EVENT.APPLICATION_TAB_CHANGE,
			EventManager.EVENT.APPLICATION_TAB_TOGGLE,
			EventManager.EVENT.EMULATOR_READY,
			EventManager.EVENT.EXCEPTION,
			EventManager.EVENT.MEMORY_VIEW,
			EventManager.EVENT.VARIABLE_VIEW,
			EventManager.EVENT.SERIAL_CONSOLE_VIEW
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
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// create and set up the window.
		frame = new JFrame("JNiosEmu");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setIconImage((new ImageIcon("graphics/icon.png")).getImage());
		frame.setContentPane(mainPanel);

		// add listener for window close
		frame.addWindowListener(
			new WindowAdapter()
    	{
    		/**
    		 * Send event when user is trying to close the window.
    		 *
    		 * @calls  EventManager.sendEvent()
    		 *
    		 * @param  e  event when window is closing
    		 */
      	public void windowClosing(WindowEvent e) {
      		eventManager.sendEvent(EventManager.EVENT.APPLICATION_EXIT);
      	}
    	}
    );

		StateManager stateManager = new StateManager(this.eventManager);
		
		// toolbar
		GUIToolBar toolBar = new GUIToolBar(this.eventManager, stateManager);
		mainPanel.add(toolBar, BorderLayout.PAGE_START);

		// menu
		GUIMenuBar menubar = new GUIMenuBar(eventManager, stateManager);
		frame.setJMenuBar(menubar);

		// all other components
		setup(mainPanel);

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
		// editor panel
		JPanel editorPanel = new JPanel(new BorderLayout());
		editorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// editor
		GUIEditor editor = new GUIEditor(this.eventManager);
		editorPanel.add(editor, BorderLayout.CENTER);

		// editor messages
		JPanel editorMessagePanel = new JPanel(new BorderLayout());
		
		GUIEditorMessages editorMessages = new GUIEditorMessages(this.eventManager);
		editorMessagePanel.add(editorMessages, BorderLayout.CENTER);
		
		// status bar
		GUIStatusBar statusBar = new GUIStatusBar(this.eventManager);
		editorMessagePanel.add(statusBar, BorderLayout.PAGE_END);
		
		editorPanel.add(editorMessagePanel, BorderLayout.PAGE_END);

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

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch(eventIdentifier) {
			case ABOUT_VIEW:
				showAbout();
				break;
			case APPLICATION_TAB_CHANGE:
				changeTab( ((Integer) obj).intValue() );
				break;
			case APPLICATION_TAB_TOGGLE:
				toggleTab();
				break;
			case APPLICATION_TITLE_CHANGE:
				setFrameTitle( (String) obj );
				break;
			case EMULATOR_READY:
				changeTab( Integer.valueOf(TAB_EMULATOR) );
				break;
			case EXCEPTION:
				showException( (Exception) obj );
				break;
			case MEMORY_VIEW:
				showMemoryView();
				break;
			case VARIABLE_VIEW:
				showVariableView();
				break;
			case SERIAL_CONSOLE_VIEW:
				showSerialConsole();
				break;
		}
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
	 * Show Variables View window positioned in center
	 * of main window.
	 *
	 * @calls     GUIVariableyView
	 * @calledby  update()
	 */
	private void showVariableView()
	{
		GUIVariableView windowFrame = new GUIVariableView(this.eventManager);
		windowFrame.setSize(new Dimension(200, 260));
		windowFrame.setLocationRelativeTo(this.frame);
		windowFrame.setVisible(true);
	}
	
	/**
	 * Show Memory View window positioned in center
	 * of main window.
	 *
	 * @calls     GUIMemoryView
	 * @calledby  update()
	 */	
	private void showMemoryView()
	{
		GUIMemoryView windowFrame = new GUIMemoryView(this.eventManager);

		windowFrame.setSize(new Dimension(240, 360));
		windowFrame.setLocationRelativeTo(this.frame);
		windowFrame.setVisible(true);		
	}

	/**
	 * Show Serial Console window positioned in center
	 * of main window.
	 *
	 * @calls     GUISerialConsole
	 * @calledby  update()
	 */	
	private void showSerialConsole()
	{
		GUISerialConsole windowFrame = new GUISerialConsole(this.eventManager);

		windowFrame.setSize(new Dimension(440, 300));
		windowFrame.setLocationRelativeTo(this.frame);
		windowFrame.setVisible(true);		
	}

}


