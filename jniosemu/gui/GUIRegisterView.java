package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jniosemu.events.*;
import jniosemu.emulator.register.*;
import jniosemu.Utilities;

/** 
 * Creates and manages the GUI component of viewing a register
 * in dec/hex/bin in the emulator.
 */
public class GUIRegisterView extends JPanel 
											 implements EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;
	
	private JTextField hexField;
	private JTextField binField;
	private JTextField decField;
	
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
	public GUIRegisterView(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.REGISTER_VIEW_SELECT,
			EventManager.EVENT.COMPILER_COMPILE,
			EventManager.EVENT.EMULATOR_CLEAR
		};				
		this.eventManager.addEventObserver(events, this);		
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIRegisterView
	 */	
	private void setup()
	{
		// put everything into the emulator panel
		this.setLayout(new SpringLayout());
		
		JLabel label = new JLabel("Hex:", JLabel.TRAILING);
		this.add(label);
		
		hexField = new JTextField();
		hexField.setFont(new Font("Monospaced", Font.PLAIN, 11));
		hexField.setHorizontalAlignment(JTextField.RIGHT);
		hexField.setEditable(false);
		label.setLabelFor(hexField);
		this.add(hexField);

		label = new JLabel("Bin:", JLabel.TRAILING);
		this.add(label);
		
		binField = new JTextField();
		binField.setFont(new Font("Monospaced", Font.PLAIN, 11));
		binField.setHorizontalAlignment(JTextField.RIGHT);
		binField.setEditable(false);
		label.setLabelFor(binField);
		this.add(binField);

		label = new JLabel("Dec:", JLabel.TRAILING);
		this.add(label);
		
		decField = new JTextField();
		decField.setFont(new Font("Monospaced", Font.PLAIN, 11));
		decField.setHorizontalAlignment(JTextField.RIGHT);
		decField.setEditable(false);
		label.setLabelFor(decField);
		this.add(decField);

		SpringUtilities.makeCompactGrid(
			this, 
			3, 2,
			2, 2, 2, 2
		);
	}

	private void setRegisterView(Register reg)
	{
		if (reg == null)
		{
			hexField.setText("");
			binField.setText("");
			decField.setText("");

			return;
		}
		
		int value = reg.getValue();
		
		hexField.setText( Utilities.intToHexString(value) );
		hexField.setCaretPosition(hexField.getDocument().getLength());
		binField.setText( Utilities.intToBinaryString(value) );
		binField.setCaretPosition(binField.getDocument().getLength());
		decField.setText( Integer.toString(value) );
		decField.setCaretPosition(decField.getDocument().getLength());
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case REGISTER_VIEW_SELECT:
				setRegisterView( (Register) obj );
				break;
			case COMPILER_COMPILE:
			case EMULATOR_CLEAR:
				setRegisterView(null);
				break;
		}
	}

}