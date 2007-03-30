package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the memory view.
 */
 public class GUIMemoryView extends JFrame
                              implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;

	/**
	 * List object used to display the memory content.
	 */
	private JList memoryList;
	
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
	public GUIMemoryView(EventManager eventManager)
	{
		super("Memory View");

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.PROGRAMCOUNTER_CHANGE,
			EventManager.EVENT.EMULATOR_RESET
		};

		this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIMemoryView
	 */
	private void setup()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// list
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
		
		listPanel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 0, 4),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
			)
		);	
		
		this.doList("Minne 1", listPanel);
		this.doList("Minne 2", listPanel);
		this.doList("Minne 3", listPanel);
		this.doList("Minne 4", listPanel);
		this.doList("Minne 5", listPanel);
		this.doList("Minne 6", listPanel);
		this.doList("Minne 7", listPanel);
		this.doList("Minne 8", listPanel);

		JScrollPane scrollPane = new JScrollPane(listPanel);

    // button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("Close");
    button.addActionListener(this);
    buttonPanel.add(button);

    // container
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    
    contentPane.add(scrollPane, BorderLayout.CENTER);
    contentPane.add(buttonPanel, BorderLayout.PAGE_END);
	}

	public JList doList(String name, JPanel panel)
	{
		Vector<String> tmp = new Vector<String>();
		
		int count = 2 + (int)(Math.random() * 20);
		
		for (int i = 0; i < count; i++)
			tmp.add("#000" + i + "   123");
				
		JList memoryList = new JList(tmp);
	
		memoryList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		memoryList.setAlignmentX(Component.CENTER_ALIGNMENT);
		memoryList.setBackground(Color.WHITE);
		memoryList.setFont(new Font("Monospaced", Font.PLAIN, 12));

		memoryList.setBorder(
			BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
		);
		
		
		JLabel titleLabel = new JLabel(name, JLabel.LEFT);
		titleLabel.setLabelFor(memoryList);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		panel.add(titleLabel);
		panel.add(memoryList);		
		
		return memoryList;
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case PROGRAMCOUNTER_CHANGE:
				//
				break;
			case EMULATOR_RESET:
				//
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