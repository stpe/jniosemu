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
		
		initGUI();
		
		// add events to listen to
		this.eventManager.addEventObserver(Events.EVENTID_EXCEPTION, this);
		this.eventManager.addEventObserver(Events.EVENTID_CHANGE_TAB, this);
		this.eventManager.addEventObserver(Events.EVENTID_EXIT, this);
		this.eventManager.addEventObserver(Events.EVENTID_ABOUT, this);
		this.eventManager.addEventObserver(Events.EVENTID_COMPILATION_DONE, this);
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

		setup(mainPanel);

		// create and set up the window.
		frame = new JFrame("JNiosEmu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(mainPanel);

		// menu
		GUIMenuBar menubar = new GUIMenuBar(eventManager);
		frame.setJMenuBar(menubar);

		frame.setSize(640, 400);
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
		JPanel editorPanel = new JPanel();
		editorPanel.setLayout(new BorderLayout());
		editorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// editor
		GUIEditor editor = new GUIEditor(this.eventManager);
		editorPanel.add(editor, BorderLayout.CENTER);

		// editor messages
		GUIEditorMessages editorMessages = new GUIEditorMessages(this.eventManager);
		editorPanel.add(editorMessages, BorderLayout.PAGE_END);

		// emulator panel
		JPanel emulatorPanel = new JPanel();
		emulatorPanel.setLayout(new BorderLayout());
		emulatorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// emulator
		GUIEmulator emulator = new GUIEmulator(this.eventManager);
		emulatorPanel.add(emulator, BorderLayout.CENTER);

		// emulator: left panel
		GUIRegisters registers = new GUIRegisters(this.eventManager);
		emulatorPanel.add(registers, BorderLayout.WEST);

		// tabs
		tabbedPane = new JTabbedPane();

		tabbedPane.addTab(
			"Editor",
			new ImageIcon("graphics/tabs/editor.png"),
			editorPanel,
			"Edit source file"
		);
		tabbedPane.setMnemonicAt(TAB_EDITOR, KeyEvent.VK_1);

		tabbedPane.addTab(
			"Emulator",
			new ImageIcon("graphics/tabs/emulator.png"),
			emulatorPanel,
			"Monitor emulation"
		);
		tabbedPane.setMnemonicAt(TAB_EMULATOR, KeyEvent.VK_2);

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
			if (eventIdentifier.equals(Events.EVENTID_COMPILATION_DONE))
				changeTab( new Integer(TAB_EMULATOR) );			
				
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
	 * @param  e  Exception that occured.
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
	 * Change selected tab between Edtior and Emulator.
	 *
	 * @calledby  update()
	 *
	 * @param  tabIndex  Index of tab.
	 */
	private void changeTab(int tabIndex)
	{
		tabbedPane.setSelectedIndex(tabIndex);
	}
	
}


