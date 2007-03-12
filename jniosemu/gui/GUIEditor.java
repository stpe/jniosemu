package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.io.IOException;
import jniosemu.events.*;
import jniosemu.editor.*;

/** 
 * Creates and manages the GUI component of the editor view.
 */
public class GUIEditor extends JPanel 
                       implements ActionListener, EventObserver {
	
	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;
	
	/**
	 * Editor is the utility class to manage I/O.
	 */
	private Editor editor;
	
	/**
	 * The text area component.
	 */
	private JTextArea textArea;

	/**
	 * File chooser used for open/save dialogs.
	 */
	private final JFileChooser fc = new JFileChooser();

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
	public GUIEditor(EventManager eventManager)
	{
		super();
		
		this.eventManager = eventManager;
		
		this.editor = new Editor();
		
		setup();
		
		// add events to listen to
    this.eventManager.addEventObserver(Events.EVENTID_NEW, this);
		this.eventManager.addEventObserver(Events.EVENTID_OPEN, this);
		this.eventManager.addEventObserver(Events.EVENTID_SAVE, this);
		this.eventManager.addEventObserver(Events.EVENTID_GUI_COMPILE, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIEditor
	 */
	private void setup()
	{
		textArea = new JTextArea();

		// put scrollbars around editor text area
		JScrollPane editorScrollPane =
		    new JScrollPane(textArea,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(editorScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Invoked when a GUI action occurs, forwards it as
	 * an event to the EventManager object.
	 *
	 * @calls     EventManager.sendEvent()
	 *
	 * @param  e  action event object
	 */
  public void actionPerformed(ActionEvent e) {
  		eventManager.sendEvent(e.getActionCommand());
  }

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_NEW))
		{
			newDocument();
		}
		else if (eventIdentifier.equals(Events.EVENTID_OPEN))
		{
			openDocument();
		}
		else if (eventIdentifier.equals(Events.EVENTID_SAVE))
		{
			saveDocument();
		}
		else if (eventIdentifier.equals(Events.EVENTID_GUI_COMPILE))
		{
			prepareCompile();
		}
	}

	/**
	 * Clear editor and start with new document.
	 *
	 * @calledby  update()
	 * @calls     EventManager.sendEvent()	 
	 */
	private void newDocument()
	{
		// clear editor
		textArea.setText("");
		
		eventManager.sendEvent(Events.EVENTID_NEW_DONE);
		// change tab to editor tab (if not current)
		eventManager.sendEvent(Events.EVENTID_CHANGE_TAB, new Integer(GUIManager.TAB_EDITOR));
	}

	/**
	 * Show open file dialog and triggers file open event
	 * if file is successful selected.
	 *
	 * @pre       Instance of FileChooser fc is created.
	 * @calledby  update()
	 * @calls     EventManager.sendEvent(), Editor.read()
	 */
	private void openDocument()
	{
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			java.io.File file = fc.getSelectedFile();

			try 
			{
				String content = editor.read(file.toString());
				
				textArea.setText(content);

				// send event of successfully opened document			
				eventManager.sendEvent(Events.EVENTID_OPENED);
				// change tab to editor tab (if not current)
				eventManager.sendEvent(Events.EVENTID_CHANGE_TAB, new Integer(GUIManager.TAB_EDITOR));
			}
			catch (IOException e)
			{
				eventManager.sendEvent(Events.EVENTID_EXCEPTION, e);
			}
		}
	}

	/**
	 * Show save file dialog and triggers file save event
	 * if file is successful selected.
	 *
	 * @pre       Instance of FileChooser fc is created.
	 * @calledby  update()
	 * @calls     EventManager.sendEvent(), Editor.write()
	 */
	private void saveDocument()
	{
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			java.io.File file = fc.getSelectedFile();

			try 
			{
				editor.write(file.toString(), textArea.getText());

				// send event of successfully saved document			
				eventManager.sendEvent(Events.EVENTID_SAVED);
				// change tab to editor tab (if not current)
				eventManager.sendEvent(Events.EVENTID_CHANGE_TAB, new Integer(GUIManager.TAB_EDITOR));
			} 
			catch (IOException e)
			{
				eventManager.sendEvent(Events.EVENTID_EXCEPTION, e);
			}
		}
	}

	/**
	 * Prepare source code and send it as event object
	 * when triggering compilation.
	 *
	 * @calledby update()
	 * @calls    EventManager.sendEvent()
	 */
	private void prepareCompile()
	{
		// send source code with event
		eventManager.sendEvent(Events.EVENTID_COMPILE, textArea.getText());
	}

}