/** 
 * The purpose of the GUI Manager is to set the layout of
 * the GUI and create the necessary GUI objects. It also
 * manages events for common GUI tasks.
 */
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
		 * Main frame of GUI.
		 */
    private JFrame frame;

		/**
		 * File chooser used for open/save dialogs.
		 */
		private final JFileChooser fc = new JFileChooser();

		/**
		 * Reference to EventManager used to receive
		 * and send events.
		 */
		private EventManager eventManager;

    /**
     * Initiates the creation of GUI components and adds itself to
     * the Event Manager as an observer.
     *
     * @post      eventManager is set.
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
      this.eventManager.addEventObserver(Events.EVENTID_OPEN, this);
      this.eventManager.addEventObserver(Events.EVENTID_SAVE, this);
      this.eventManager.addEventObserver(Events.EVENTID_EXIT, this);
      this.eventManager.addEventObserver(Events.EVENTID_ABOUT, this);
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
      JTabbedPane tabbedPane = new JTabbedPane();

			tabbedPane.addTab(
				"Editor",
				new ImageIcon("graphics/tabs/editor.png"),
				editorPanel,
				"Edit source file"
			);
			tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

			tabbedPane.addTab(
				"Emulator",
				new ImageIcon("graphics/tabs/emulator.png"),
				emulatorPanel,
				"Monitor emulation"
			);
			tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

      mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

	/**
	 * Receive incoming events from event manager.
	 *
	 * @param  eventIdentifier String identifying the event
	 * @param  obj             Object associated with event by sender
	 */
		public void update(String eventIdentifier, Object obj)
		{
			if (eventIdentifier.equals(Events.EVENTID_EXIT))
				quitFile();
			else
				if (eventIdentifier.equals(Events.EVENTID_OPEN))
					openFile();
			else
				if (eventIdentifier.equals(Events.EVENTID_SAVE))
					saveFile();
			else
				if (eventIdentifier.equals(Events.EVENTID_ABOUT))
					showAbout();
		}

		/**
		 * Open file.
		 */
		public void openFile()
		{
			if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
	      java.io.File file = fc.getSelectedFile();
	      
	      eventManager.sendEvent(
	      	"DEBUG",
	      	"Open file: " + file.getName()
	    	);
	    }
		}

		/**
		 * Open file.
		 */
		public void saveFile()
		{
			if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
	      java.io.File file = fc.getSelectedFile();
	
	      eventManager.sendEvent(
	      	"DEBUG",
	      	"Save file: " + file.getName()
	    	);
	    }
		}

    /**
     * Quit the application.
     */
    public void quitFile()
    {
        System.exit(0);
    }

    /**
     * Show about window.
     */
    public void showAbout()
    {
        JOptionPane.showMessageDialog(frame,
                        "JNiosEmu",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE
        );
    }


}


