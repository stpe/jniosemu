package jniosemu.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Creates and manages the GUI component of the About window.
 */
public class GUIAbout extends JDialog
                      implements ActionListener {

	/**
	 * Initiates the creation of GUI components.
	 *
	 * @calledby  GUIManager.setup()
	 * @calls     setup()
	 */
	public GUIAbout(Frame owner)
	{
		super(owner, "About JNiosEmu", true);

		setup();
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIAbout
	 */
	private void setup()
	{
    // button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("About");
    button.addActionListener(this);
    buttonPanel.add(button);

    this.setLayout(new BorderLayout());
		this.add(buttonPanel);
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