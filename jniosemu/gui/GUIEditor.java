package jniosemu.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.Utilities;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

import jniosemu.events.*;
import jniosemu.editor.*;
import jniosemu.instruction.InstructionManager;

/**
 * Creates and manages the GUI component of the editor view.
 */
public class GUIEditor extends JPanel
                       implements UndoableEditListener, DocumentListener, CaretListener, EventObserver
{
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
	 * The text area component used for editing source documents.
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
	 * File of current document (null if document not saved).
	 */
	private File documentFile = null;

	/**
	 * Used to keep track of undo/redo for the editor.
	 */
	private UndoManager undo = new UndoManager();

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
			EventManager.EVENT.DOCUMENT_SAVE,
			EventManager.EVENT.DOCUMENT_SAVE_AS,
			EventManager.EVENT.EDITOR_INSERT_INSTRUCTION,
			EventManager.EVENT.EDITOR_UNDO,
			EventManager.EVENT.EDITOR_REDO
			
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
		fc.addChoosableFileFilter(new AsmFileFilter());

		// undo
		textArea.getDocument().addUndoableEditListener(this);

		// change caret policy to make sure it is moved
		// when using undo/redo
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

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
	 *
	 * @calledby  insertUpdate(), removeUpdate(), changedUpdate(),
	 *            newDocument(), openDocument(), saveDocument()
	 * @calls     EventManager.sendEvent()
	 *
	 * @param  textHasChanged     set value if text has changed
	 * @param  forceTitleUpdated  forces a window title update even if
	 *                            textHasChanged state is same
	 */
	private void textChanged(boolean textHasChanged, boolean forceTitleUpdate)
	{
		if (this.textHasChanged != textHasChanged || forceTitleUpdate)
		{
			this.textHasChanged = textHasChanged;

			eventManager.sendEvent(EventManager.EVENT.APPLICATION_TITLE_CHANGE, getDocumentTitle());
		}
	}

	/**
	 * Is called when text is changed in editor. Used to keep track
	 * of if a document is modified since last save or not.
	 *
	 * @calledby  insertUpdate(), removeUpdate(), changedUpdate(),
	 *            newDocument(), openDocument(), saveDocument()
	 * @calls     textChanged()
	 *
	 * @param  textHasChanged  Set value if text has changed
	 */
	private void textChanged(boolean textHasChanged)
	{
		textChanged(textHasChanged, false);
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
			case DOCUMENT_SAVE_AS:
				saveAsDocument();
				break;
			case EDITOR_INSERT_INSTRUCTION:
				insertInstruction((String) obj);
				break;
			case EDITOR_UNDO:
				this.undo();
				break;
			case EDITOR_REDO:
				this.redo();
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
		this.documentFile = null;
		textChanged(false, true);

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

				this.documentFile = file;				
				textArea.setText(content);
				textChanged(false, true);

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
	 * @calls     EventManager.sendEvent(), saveDocument()
	 *
	 * @return  true if successfully saved
	 */
	private boolean saveAsDocument()
	{
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			this.documentFile = fc.getSelectedFile();

			return saveDocument();
		}
		
		return false;
	}

	/**
	 * Save current document.
	 *
	 * @checks    If document has not been associated with a file (it has not
	 *            been saved before) then the Save As dialog is used instead.
	 * @calledby  update(), exitApplication()
	 * @calls     EventManager.sendEvent(), Editor.write(), saveAsDocument()
	 *
	 * @return  true if successfully saved
	 */
	private boolean saveDocument()
	{
		// if no current file, ask for one
		if (this.documentFile == null)
		{
			return saveAsDocument();
		}
		
		// save file
		try
		{
			Editor.write(this.documentFile.toString(), textArea.getText());
			
			textChanged(false, true);

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
					"Save changes to " + getDocumentTitle() + "?",
					"JNiosEmu",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					null,
					null
			);		
	}

	/**
	 * Returns document title based on filename (or untitled
	 * if document has not been saved.
	 *
	 * @return  document title
	 */
	private String getDocumentTitle()
	{
		String title = "";
		if (this.documentFile == null)
		{
			title = DEFAULT_DOCUMENT_NAME;
		}
		else
		{
			title = this.documentFile.getName();
		}
		
		if (textHasChanged)
			title += "*";

		return title;
	}

	/**
	 * Insert instruction at caret position in editor.
	 *
	 * @param  instruction  instruction string
	 */
	private void insertInstruction(String instruction)
	{
		// get argument of instruction
		String argument = InstructionManager.getArgument(instruction);

		int caretPos = textArea.getCaretPosition();

		// insert instruction and argument at caret position		
		textArea.insert(instruction + argument, caretPos);

		try {
			// move caret to beginning of argument
			int newCaretPos = caretPos + instruction.length();
			if (argument.length() > 0)
				newCaretPos++;

			textArea.setCaretPosition(newCaretPos);
		} catch(IllegalArgumentException e) {
			
		}
	}

	/**
	 * Implemented due to UndoableEditListener. Called
	 * whenever there is something that might be undoed in
	 * the editor.
	 *
	 * @calls updateUndoRedoState()
	 */
	public void undoableEditHappened(UndoableEditEvent e)
	{
		undo.addEdit(e.getEdit());
		
		// update menus
		updateUndoRedoState();
	}

	/**
	 * Performs an undo in the editor.
	 *
	 * @calls updateUndoRedoState()
	 */
	private void undo()
	{
		try {
			this.undo.undo();
		} catch (CannotUndoException ex) { }
		
		updateUndoRedoState();
	}
	
	/**
	 * Performs a redo in the editor.
	 *
	 * @calls updateUndoRedoState()
	 */
	private void redo()
	{
		try {
			this.undo.redo();
		} catch (CannotRedoException ex) { }
		
		updateUndoRedoState();
	}

	/**
	 * Sends event to update the state of the undo/redo
	 * menu items.
	 *
	 * @calls EventManager.sendEvent()
	 */
	private void updateUndoRedoState()
	{
		eventManager.sendEvent(EventManager.EVENT.EDITOR_UPDATE_UNDO_STATE, new Boolean(this.undo.canUndo()));
		eventManager.sendEvent(EventManager.EVENT.EDITOR_UPDATE_REDO_STATE, new Boolean(this.undo.canRedo()));
	}

}
