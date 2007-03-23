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
	private transient EventManager eventManager;

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

		setup();

		// add events to listen to
		EventManager.EVENT[] events = {
			EventManager.EVENT.APPLICATION_EXIT,
			EventManager.EVENT.COMPILER_COMPILE_INIT,
			EventManager.EVENT.DOCUMENT_NEW,
			EventManager.EVENT.DOCUMENT_OPEN,
			EventManager.EVENT.DOCUMENT_SAVE
		};

    this.eventManager.addEventObserver(events, this);
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
	 * Returns true if current document has changed since
	 * last save.
	 *
	 * @calledby GUIManager.exit()
	 *
	 * @return   true if document changed since last save
	 */
	public boolean hasChanged()
	{
		return this.textHasChanged;
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

			eventManager.sendEvent(EventManager.EVENT.APPLICATION_TITLE_CHANGE, title);
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
			
		} catch (javax.swing.text.BadLocationException ex) { 
			row = 0;
			column = 0;
		}
		
		this.eventManager.sendEvent(EventManager.EVENT.EDITOR_CURSOR_CHANGE, new Point(row, column));
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch(eventIdentifier) {
			case APPLICATION_EXIT:
				exitApplication();
				break;
			case COMPILER_COMPILE_INIT:
				prepareCompile();
				break;
			case DOCUMENT_NEW:
				newDocument();
				break;
			case DOCUMENT_OPEN:
				openDocument();
				break;
			case DOCUMENT_SAVE:
				saveDocument();
				break;
		}
	}

	/**
	 * Clear editor and start with new document. If current
	 * document has been modified since last saved, then an
	 * option to save the current document is offered to the
	 * user to prevent unintential loss of data.
	 *
	 * @calledby  update()
	 * @calls     EventManager.sendEvent(), hasChanged(), 
	 *            showSaveChangesDialog(), saveDocument()
	 */
	private void newDocument()
	{
		// notify user if changes has been made to current document
		if (hasChanged())
		{
			// show option dialog and check user response
			switch ( showSaveChangesDialog() )
			{
				case JOptionPane.YES_OPTION:
					if (saveDocument() == false)
					{
						// don't proceed if save wasn't successfull
						return;
					}
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
			}
		}		
		
		// clear editor
		textArea.setText("");
		this.documentTitle = DEFAULT_DOCUMENT_NAME;
		textChanged(false);

		eventManager.sendEvent(EventManager.EVENT.DOCUMENT_NEW_DONE);
		// change tab to editor tab (if not current)
		eventManager.sendEvent(EventManager.EVENT.APPLICATION_TAB_CHANGE, Integer.valueOf(GUIManager.TAB_EDITOR));
	}

	/**
	 * Show open file dialog and triggers file open event
	 * if file is successful selected. If current document
	 * has been modified since last saved, then an option
	 * to save the current document is offered to the user
	 * to prevent unintential loss of data.
	 *
	 * @pre       Instance of FileChooser fc is created.
	 * @calledby  update()
	 * @calls     EventManager.sendEvent(), Editor.read(), 
	 *            hasChanged(), showSaveChangesDialog(), saveDocument()
	 */
	private void openDocument()
	{
		// notify user if changes has been made to current document
		if (hasChanged())
		{
			// show option dialog and check user response
			switch ( showSaveChangesDialog() )
			{
				case JOptionPane.YES_OPTION:
					if (saveDocument() == false)
					{
						// don't proceed if save wasn't successfull
						return;
					}
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
			}
		}		
		
		// show file dialog
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			java.io.File file = fc.getSelectedFile();

			try
			{
				String content = Editor.read(file.toString());
				
				this.documentTitle = file.getName();
				textArea.setText(content);
				textChanged(false);

				// send event of successfully opened document
				eventManager.sendEvent(EventManager.EVENT.DOCUMENT_OPEN_DONE);
				// change tab to editor tab (if not current)
				eventManager.sendEvent(EventManager.EVENT.APPLICATION_TAB_CHANGE, Integer.valueOf(GUIManager.TAB_EDITOR));
			}
			catch (IOException e)
			{
				eventManager.sendEvent(EventManager.EVENT.EXCEPTION, e);
			}
		}
	}

	/**
	 * Show save file dialog and triggers file save event
	 * if file is successful selected.
	 *
	 * @pre       Instance of FileChooser fc is created.
	 * @calledby  update(), exitApplication()
	 * @calls     EventManager.sendEvent(), Editor.write()
	 *
	 * @return  true if successfully saved
	 */
	private boolean saveDocument()
	{
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			java.io.File file = fc.getSelectedFile();

			try
			{
				Editor.write(file.toString(), textArea.getText());
				
				this.documentTitle = file.getName();
				textChanged(false);

				// send event of successfully saved document
				eventManager.sendEvent(EventManager.EVENT.DOCUMENT_SAVE_DONE);
				// change tab to editor tab (if not current)
				eventManager.sendEvent(EventManager.EVENT.APPLICATION_TAB_CHANGE, Integer.valueOf(GUIManager.TAB_EDITOR));
				
				return true;
			}
			catch (IOException e)
			{
				eventManager.sendEvent(EventManager.EVENT.EXCEPTION, e);
				return false;
			}
		}
		
		return false;
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
		eventManager.sendEvent(EventManager.EVENT.COMPILER_COMPILE, textArea.getText());
	}

	/**
	 * Exit the application.
	 *
	 * @post      Application is shutdown.
	 * @checks    If current document has been modified since last save
	 *            the user is given the option save it before exiting,
	 *            cancel the exit operation or exit without saving.
	 * @calledby  update()
	 * @calls     hasChanged(), saveDocument(), showSaveChangesDialog()
	 */
	private void exitApplication()
	{
		if (hasChanged())
		{
			// show option dialog and check user response
			switch ( showSaveChangesDialog() )
			{
				case JOptionPane.YES_OPTION:
					if (saveDocument() == false)
					{
						// don't exit if save wasn't successfull
						return;
					}
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
			}
		}
		
		// exit application
		System.exit(0);		
	}

	/** 
   * Show dialog window offering user to save changes of
   * current document.
   *
   * @return  option user selected
   */
	private int showSaveChangesDialog()
	{
			// show option dialog
			return JOptionPane.showOptionDialog(
					this,
					"Save changes to " + this.documentTitle + "?",
					"JNiosEmu",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					null,
					null
			);		
	}

}
