package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the console.
 */
 public class GUIConsole extends JFrame
                              implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * Text area for console.
	 */
	private JTextArea consoleTextArea;

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
	public GUIConsole(EventManager eventManager)
	{
		super("Console");

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.CONSOLE_OUTPUT,
			EventManager.EVENT.EMULATOR_RESET,
			EventManager.EVENT.EMULATOR_READY,
			EventManager.EVENT.EMULATOR_CLEAR
		};

		this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIConsole
	 */
	private void setup()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		consoleTextArea = new JTextArea();
		
		consoleTextArea.setEditable(false);
		consoleTextArea.setLineWrap(true);
		consoleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		// put scrollbars around received data text area
		JScrollPane consoleScrollPane =
		    new JScrollPane(consoleTextArea,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
    // button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("Close");
    button.addActionListener(this);
    buttonPanel.add(button);

    // container
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    contentPanel.add(consoleScrollPane, BorderLayout.CENTER);
    contentPanel.add(buttonPanel, BorderLayout.PAGE_END);
    
    contentPane.add(contentPanel, BorderLayout.CENTER);
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case CONSOLE_OUTPUT:
				// append character last
				consoleTextArea.append( ((Character) obj).toString() );
				
				// move caret to last position to force scroll
				consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());

				break;
			case EMULATOR_RESET:
			case EMULATOR_READY:
			case EMULATOR_CLEAR:
				consoleTextArea.setText("");
				break;
		}
	}

	/**
	 * Invoked when a GUI action occurs..
	 *
	 * @param  e  action event object
	 */
	public void actionPerformed(ActionEvent e) {
	  setVisible(false);
	  dispose();
	}

}