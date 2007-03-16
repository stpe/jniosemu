package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Utilities;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import jniosemu.events.*;
import jniosemu.editor.*;

/**
 * Creates and manages the GUI component of the editor view.
 */
public class GUIEditor extends JPanel
                       implements DocumentListener, CaretListener, EventObserver {

	/**
	 * Document name used for unsaved documents.
	 */
	private static final String DEFAULT_DOCUMENT_NAME = "untitled";

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
	 * Keep track if text has changed since last save.
	 */
	private boolean textHasChanged = true;

	/**
	 * Filename of current document (default "untitled").
	 */
	private String documentTitle = DEFAULT_DOCUMENT_NAME;

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
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textArea.getDocument().addDocumentListener(this);
		textArea.addCaretListener(this);

		// put scrollbars around editor text area
		JScrollPane editorScrollPane =
		    new JScrollPane(textArea,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// put everything into the editor panel
		this.setLayout(new BorderLayout());
		this.add(editorScrollPane, BorderLayout.CENTER);

		// file chooser
		fc.setCurrentDirectory(new File("."));

		textChanged(false);
	}

	/**
	 * Is called when text is changed in editor. Used to keep track
	 * of if a document is modified since last save or not.
	 * an event to the EventManager object.
	 *
	 * @calledby  insertUpdate(), removeUpdate(), changedUpdate(),
	 *            newDocument(), openDocument(), saveDocument()
	 * @calls     EventManager.sendEvent()
	 *
	 * @param  textHasChanged  Set value if text has changed
	 */
	private void textChanged(boolean textHasChanged)
	{
		if (this.textHasChanged != textHasChanged)
		{
			this.textHasChanged = textHasChanged;

			String title = this.documentTitle;

			if (textHasChanged)
				title = title + "*";

			eventManager.sendEvent(Events.EVENTID_CHANGE_WINDOW_TITLE, title);
		}
	}

	/**
	 * Exists due to DocumentListener interface. Detects
	 * when something is updated in the editor.
	 *
	 * @calls textChanged()
	 */
	public void changedUpdate(DocumentEvent e)
	{
		textChanged(true);
	}

	/**
	 * Exists due to DocumentListener interface. Detects
	 * when something is inserted in the editor.
	 *
	 * @calls textChanged()
	 */
	public void insertUpdate(DocumentEvent e)
	{
		textChanged(true);
	}

	/**
	 * Exists due to DocumentListener interface. Detects
	 * when something is removed in the editor.
	 *
	 * @calls textChanged()
	 */
	public void removeUpdate(DocumentEvent e)
	{
		textChanged(true);
	}

	/**
	 * Exists due to CaretListener interface. Detects
	 * when cursor position is changed.
	 *
	 * @calls EventManager.sendEvent()
	 *
	 * @param  e  event when cursor does change position
	 */
	public void caretUpdate(CaretEvent e)
	{
		int row = 0;
		int column = 0;
		
		try {
			// calculate row
			int y = textArea.modelToView(e.getDot()).y;
			int rowHeight = textArea.getFontMetrics( textArea.getFont() ).getHeight();

			row = y/rowHeight + 1;
			
			// calculate offset
			int offset = e.getDot();
			column = offset - Utilities.getRowStart(textArea, offset) + 1;
			
		} catch (Exception ex) { }
		
		this.eventManager.sendEvent(Events.EVENTID_EDITOR_CURSOR_CHANGE, new Point(row, column));
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
		this.documentTitle = DEFAULT_DOCUMENT_NAME;
		textChanged(false);

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
				
				this.documentTitle = file.getName();
				textArea.setText(content);
				textChanged(false);

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
				
				this.documentTitle = file.getName();
				textChanged(false);

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
