package jniosemu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.Utilities;

/**
 * The GUI Manager creates all components of the GUI and
 * dictates their layout. It also manages general actions
 * of the GUI not related to specific components.
 */
public class GUIManager
	implements EventObserver {

  /**
   * Application window width.
   */
	public static final int WINDOW_WIDTH = 800;

  /**
   * Application window height.
   */
	public static final int WINDOW_HEIGHT = 600;

	public static final Color HIGHLIGHT_GREEN          = new Color(220, 255, 220);
	public static final Color HIGHLIGHT_SELECTED_GREEN = new Color(150, 185, 150);
	public static final Color HIGHLIGHT_RED            = new Color(255, 220, 220);
	public static final Color HIGHLIGHT_SELECTED_RED   = new Color(185, 150, 150);
	public static final Color FONT_DISABLED            = new Color(196, 196, 196);

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
	 * Splitter used between editor and editor messages.
	 */
	private JSplitPane editorSplitPane;
	
	/**
	 * Splitter used between emulator and editor messages.
	 */
	private JSplitPane emulatorSplitPane;
	
	/**
	 * Reference to editor messages object.
	 */
	private GUIEditorMessages editorMessages;

	/**
	 * Reference to emulator messages object.
	 */
	private GUIEmulatorMessages emulatorMessages;

	/**
	 * Variable View window.
	 */
	GUIVariableView frameVariableView = null;

	/**
	 * Memory View window.
	 */
	GUIMemoryView frameMemoryView = null;

	/**
	 * Serial Console window.
	 */
	GUISerialConsole frameSerialConsole = null;

	/**
	 * Console window.
	 */
	GUIConsole frameConsole = null;

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
			EventManager.EVENT.APPLICATION_START,
			EventManager.EVENT.EMULATOR_READY,
			EventManager.EVENT.EMULATOR_ERROR,
			EventManager.EVENT.EXCEPTION,
			EventManager.EVENT.MEMORY_VIEW,
			EventManager.EVENT.VARIABLE_VIEW,
			EventManager.EVENT.UART0_VIEW,
			EventManager.EVENT.UART1_VIEW,
			EventManager.EVENT.COMPILER_COMPILE,
			EventManager.EVENT.COMPILER_ERROR
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
		frame.setIconImage((new ImageIcon(Utilities.loadImage("graphics/icon.png"))).getImage());
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

		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
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

		// editor messages
		editorMessages = new GUIEditorMessages(this.eventManager);
		
		// status bar
		GUIStatusBar statusBar = new GUIStatusBar(this.eventManager);

		// split pane between editor and editor messages
		editorSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                                            editor, editorMessages);

		// remove splitpane keyboard bindings (it eats F8)
		SwingUtilities.replaceUIInputMap(editorSplitPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);

		editorPanel.add(editorSplitPane, BorderLayout.CENTER);
		editorPanel.add(statusBar, BorderLayout.PAGE_END);

		// emulator panel
		JPanel emulatorPanel = new JPanel(new BorderLayout());
		emulatorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// emulator
		GUIEmulator emulator = new GUIEmulator(this.eventManager);

		// emulator messages
		emulatorMessages = new GUIEmulatorMessages(this.eventManager);

		// split pane between editor and editor messages
		emulatorSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                                            emulator, emulatorMessages);

		// remove splitpane keyboard bindings (it eats F8)
		SwingUtilities.replaceUIInputMap(emulatorSplitPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);

		// emulator: left panel
		JPanel emulatorLeftPanel = new JPanel(new BorderLayout());

		GUIPCView pcView = new GUIPCView(this.eventManager);
		emulatorLeftPanel.add(pcView, BorderLayout.PAGE_START);
		
		GUIRegisters registers = new GUIRegisters(this.eventManager);
		emulatorLeftPanel.add(registers, BorderLayout.CENTER);

		GUIRegisterView registerView = new GUIRegisterView(this.eventManager);
		emulatorLeftPanel.add(registerView, BorderLayout.PAGE_END);

		// split pane between emulator and register view
		JSplitPane registerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		                                              emulatorLeftPanel, emulatorSplitPane);
		emulatorPanel.add(registerSplitPane, BorderLayout.CENTER);

		// remove splitpane keyboard bindings (it eats F8)
		SwingUtilities.replaceUIInputMap(registerSplitPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);

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
			new ImageIcon(Utilities.loadImage("graphics/tabs/editor.png")),
			editorPanel,
			"Edit source file"
		);

		tabbedPane.addTab(
			"Emulator",
			new ImageIcon(Utilities.loadImage("graphics/tabs/emulator.png")),
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
			case APPLICATION_START:
				toggleEditorMessages(false);
				toggleEmulatorMessages(false);
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
				toggleEmulatorMessages(false);
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
			case UART0_VIEW:
				showSerialConsole();
				break;
			case UART1_VIEW:
				showConsole();
				break;
			case COMPILER_COMPILE:
				toggleEditorMessages(false);
				break;
			case COMPILER_ERROR:
				toggleEditorMessages(true);
				break;
			case EMULATOR_ERROR:
				toggleEmulatorMessages(true);
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
		JDialog aboutDialog = new GUIAbout(frame);
		
		// aboutDialog.setIconImage(this.frame.getIconImage());
		aboutDialog.pack();
		aboutDialog.setLocationRelativeTo(this.frame);		
		aboutDialog.setVisible(true);
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
		// print stack trace to stdout
//		e.printStackTrace();

		JOptionPane.showMessageDialog(
			frame,
			e.getMessage(),
      "Error",
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
		if (frameVariableView == null)
		{
			frameVariableView = new GUIVariableView(this.eventManager);
			frameVariableView.setSize(new Dimension(340, 300));
			frameVariableView.setIconImage(this.frame.getIconImage());
			frameVariableView.setLocationRelativeTo(this.frame);
		}		
		
		frameVariableView.setVisible(true);
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
		if (frameMemoryView == null)
		{
			frameMemoryView = new GUIMemoryView(this.eventManager);
			frameMemoryView.setSize(new Dimension(280, 360));
			frameMemoryView.setIconImage(this.frame.getIconImage());
			frameMemoryView.setLocationRelativeTo(this.frame);
		}

		frameMemoryView.setVisible(true);
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
		if (frameSerialConsole == null)
		{
			frameSerialConsole = new GUISerialConsole(this.eventManager);
			frameSerialConsole.setSize(new Dimension(440, 300));
			frameSerialConsole.setIconImage(this.frame.getIconImage());
			frameSerialConsole.setLocationRelativeTo(this.frame);
		}

		frameSerialConsole.setVisible(true);		
	}

	/**
	 * Show Console window positioned in center
	 * of main window.
	 *
	 * @calls     GUIConsole
	 * @calledby  update()
	 */	
	private void showConsole()
	{
		if (frameConsole == null)
		{
			frameConsole = new GUIConsole(this.eventManager);
			frameConsole.setSize(new Dimension(440, 300));
			frameConsole.setIconImage(this.frame.getIconImage());
			frameConsole.setLocationRelativeTo(this.frame);
		}

		frameConsole.setVisible(true);		
	}

	/**
	 * Show or hide the editor error messages list.
	 *
	 * @param makeVisible  set to true to show error messages
	 */
	private void toggleEditorMessages(boolean makeVisible)
	{
		toggleMessagesSplitpane(makeVisible, editorMessages, editorSplitPane);
	}

	/**
	 * Show or hide the emulator error messages list.
	 *
	 * @param makeVisible  set to true to show error messages
	 */
	private void toggleEmulatorMessages(boolean makeVisible)
	{
		System.out.println("toggleEmulatorMessages: " + makeVisible);
		toggleMessagesSplitpane(makeVisible, emulatorMessages, emulatorSplitPane);
	}

	/**
	 * Show or hide the editor error messages list.
	 *
	 * @param makeVisible  set to true to show error messages
	 * @param msgView      component to toggle
	 * @param splitpane    splitpane to toggle
	 */
	private void toggleMessagesSplitpane(boolean makeVisible, JComponent msgView, JSplitPane splitpane)
	{
		// do nothing if state already is set
		if (makeVisible == msgView.isVisible())
			return;

		javax.swing.plaf.basic.BasicSplitPaneUI ui = (javax.swing.plaf.basic.BasicSplitPaneUI) splitpane.getUI();
		ui.getDivider().setVisible(makeVisible);

		msgView.setVisible(makeVisible);

		// set percentages the editor should occupy (messages gets the rest)
		splitpane.setDividerLocation(0.90);
	}	

}


