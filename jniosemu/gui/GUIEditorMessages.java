package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import jniosemu.events.*;

/** 
 * Creates and manages the GUI component of the editor message view.
 */
public class GUIEditorMessages extends JPanel 
                       implements EventObserver {
	
	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private transient EventManager eventManager;
	
	/**
	 * List to contain messages.
	 */	
	private JList msgList;
	
	/**
	 * List model used by the JList.
	 */
	private DefaultListModel listModel;
	
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
	public GUIEditorMessages(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		setup();
		
    // add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.COMPILER_COMPILE,
			EventManager.EVENT.COMPILER_ERROR
		};
    this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIEditorMessages
	 */
	private void setup()
	{
		listModel = new DefaultListModel();
		
		msgList = new JList(listModel);
		msgList.setBackground(Color.WHITE);
		msgList.setFont(new Font("Monospaced", Font.PLAIN, 11));
	
		/**
		 * Add mouse listener to listen to double-clicks on a line
		 * to be able to move the caret in the editor to the line
		 * number stated in the error message.
		 */
		MouseListener mouseListener = new MouseAdapter() 
		{
			private static final String LINE_NUMBER_PREFIX = "Line ";
			
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// get click location
					int index = msgList.locationToIndex(e.getPoint());

					// unable to get location					
					if (index == -1)
						return;

					// get error message where clicked
					String errorMsg = "";
					try {
						errorMsg = (String) listModel.getElementAt(index);
					} catch(ArrayIndexOutOfBoundsException ex) {
						// no error message row at point where clicked
						return;
					}

					// get the line number
					errorMsg = errorMsg.substring(
						errorMsg.indexOf(LINE_NUMBER_PREFIX) + LINE_NUMBER_PREFIX.length(),
						errorMsg.indexOf(':')
					);

					int lineNumber = -1;
					
					try {
						lineNumber = Integer.parseInt(errorMsg);
					} catch(NumberFormatException ex) {
						// wasn't a number, do nothing
						return;
					}

					// send event to move caret in editor
					eventManager.sendEvent(EventManager.EVENT.EDITOR_MOVE_TO_LINE, Integer.valueOf(lineNumber));
				}
			}
		};	

		msgList.addMouseListener(mouseListener);	

		// put scrollbars around editor text area
		JScrollPane editorMessagesScrollPane =
		    new JScrollPane(msgList,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(editorMessagesScrollPane, BorderLayout.CENTER);
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch (eventIdentifier) {
			case COMPILER_COMPILE:
				listModel.clear();
				break;
			case COMPILER_ERROR:
				String[] result = ((String) obj).split("\n");
				for (int x = 0; x < result.length; x++)
				{
					// convert tab to spaces
					result[x] = result[x].replaceAll("\t", "    ");
					
					listModel.addElement(result[x]);
				}
				
				break;
		}
	}

}
