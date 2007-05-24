package jniosemu.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import jniosemu.Utilities;

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
    this.setLayout(new BorderLayout());

		// logo
		JLabel logo = new JLabel(new ImageIcon(Utilities.loadImage("graphics/logo.png")));
		Border itemBorder =
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(6, 6, 6, 6),
				BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					BorderFactory.createEmptyBorder(3, 3, 3, 3)
				)
			);
		logo.setBorder(itemBorder);
		
		// top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(logo, BorderLayout.PAGE_START);
		
		// labels
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));

		JLabel version = new JLabel("JNiosEmu v1.0 (2007-05-24)", JLabel.CENTER);
		JLabel website = new JLabel("http://jniosemu.codetouch.com/", JLabel.CENTER);
		JLabel copyright = new JLabel("Copyright (c) 2007 the developers", JLabel.CENTER);

		Border labelBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
		version.setBorder(labelBorder);
		website.setBorder(labelBorder);
		copyright.setBorder(labelBorder);

		labelPanel.add(version);
		labelPanel.add(website);
		labelPanel.add(copyright);
		
		topPanel.add(labelPanel, BorderLayout.PAGE_END);

		this.add(topPanel, BorderLayout.PAGE_START);
		
		// text
		JTextArea textArea = new JTextArea(6, 20);
		textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setText("Developers:\n Emil Hesslow (hesslow@kth.se)\n Stefan Pettersson (spett@kth.se)\n Christian Mörck (cmorck@kth.se)\n Martin Fryxell (mfryxell@kth.se)\n");
		
		JScrollPane textScrollPane =
		    new JScrollPane(textArea,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		textScrollPane.setBorder(itemBorder);
		
		this.add(textScrollPane, BorderLayout.CENTER);
		
    // button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("  OK  ");
    button.addActionListener(this);
    buttonPanel.add(button);

		this.add(buttonPanel, BorderLayout.PAGE_END);

		this.setResizable(false);
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