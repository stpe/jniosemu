/** 
 * Register view in emulator.
 */
package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import jniosemu.events.*;

public class GUIRegisters extends JPanel 
                       implements ActionListener, EventObserver {

	private EventManager eventManager;

	private JList registerList;
	private DefaultListModel listModel;

	/**
	 * GUI component constructor.
	 *
	 * @param  eventManager
	 */
	public GUIRegisters(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
		// add events to listen to
    this.eventManager.addEventObserver(Events.EVENTID_NEW, this);		
	}

	/**
	 * Setup GUI components and attributes.
	 */	
	private void setup()
	{
		this.setPreferredSize(new Dimension(70, 0));

		// registers
		listModel = new DefaultListModel();
		for (int i = 1; i < 25; i++)
		{
			listModel.addElement(new Integer(i));	
		}

		registerList = new JList(listModel);
		registerList.setCellRenderer(new RegisterCellRenderer());
		
		// scrollbars
		JScrollPane scrollPane = new JScrollPane(registerList);

		// put everything into the emulator panel
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);		
	}

	/**
	 * Receive incoming events from event manager.
	 *
	 * @param  eventIdentifier String identifying the event
	 * @param  obj             Object associated with event by sender
	 */
	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_NEW))
		{
//			editor.setText("");
		}
	}
	
	/**
	 * Invoked when a GUI action occurs.
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }

	/**
	 * Custom cell renderer for JList
	 */
  class RegisterCellRenderer extends JLabel
                         implements ListCellRenderer {

			private Object registerObject;

      public RegisterCellRenderer() {
          setOpaque(true);
          setHorizontalAlignment(CENTER);
          setVerticalAlignment(CENTER);
      }

      /*
       * Render cell.
       */
      public Component getListCellRendererComponent(
                                         JList list,
                                         Object value,
                                         int index,
                                         boolean isSelected,
                                         boolean cellHasFocus) {

					this.registerObject = value;
					setText("."); // trigger repaint

          if (isSelected) {
              setBackground(list.getSelectionBackground());
              setForeground(list.getSelectionForeground());
          } else {
              setBackground(list.getBackground());
              setForeground(list.getForeground());
          }

					if (index % 5 == 0)
					{
						setBackground(new Color(100, 255, 100));
					}
					if (index % 7 == 0)
					{
						setBackground(new Color(255, 100, 100));
					}

          return this;
      }

		/**
		 * Custom painting.
		 */
    public void paintComponent(Graphics g) {
	   	super.paintComponent(g);

      if (isOpaque()) 
      { 
      		// paint background
          g.setColor(getBackground());
          g.fillRect(0, 0, getWidth(), getHeight());
      }

			FontMetrics metrics = g.getFontMetrics(getFont());

			g.setColor(new Color(0, 0, 255));
			g.drawString("#" + this.registerObject.toString(), 2, 11);

			String tmp = "0x" + this.registerObject.toString();	

			g.drawString(tmp, getWidth()-metrics.stringWidth(tmp), 11);
    }

  }

}