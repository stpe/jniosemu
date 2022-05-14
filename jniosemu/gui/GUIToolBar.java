package jniosemu.gui;

import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;
import jniosemu.Utilities;
import jniosemu.events.*;

/** Toolbar in GUI. */
public class GUIToolBar extends JToolBar implements ActionListener {

  /** Reference to EventManager used to receive and send events. */
  private transient EventManager eventManager;

  /**
   * Initiates the creation of this GUI component.
   *
   * @post eventManager reference is set for this object.
   * @calledby GUIManager.setup()
   * @calls setup()
   * @param eventManager the Event Manager object
   */
  public GUIToolBar(EventManager eventManager, StateManager stateManager) {
    super();

    this.eventManager = eventManager;

    setup();

    registerWithStateManager(stateManager);
  }

  /**
   * Setup GUI components and attributes.
   *
   * @post Buttons created and added to toolbar.
   * @calledby GUIToolBar
   * @calls makeButton()
   */
  private void setup() {
    this.setFloatable(false);
    this.setRollover(true);

    JButton button = null;

    button =
        makeButton("new", EventManager.EVENT.DOCUMENT_NEW.toString(), "Create new document", "New");
    this.add(button);

    button =
        makeButton("open", EventManager.EVENT.DOCUMENT_OPEN.toString(), "Open document", "Open");
    this.add(button);

    button =
        makeButton("save", EventManager.EVENT.DOCUMENT_SAVE.toString(), "Save document", "Save");
    this.add(button);

    this.addSeparator();

    button =
        makeButton(
            "compile",
            EventManager.EVENT.COMPILER_COMPILE_INIT.toString(),
            "Assemble source code",
            "Assemble");
    this.add(button);

    button =
        makeButton(
            "run",
            EventManager.EVENT.EMULATOR_RUN.toString(),
            "Run assembled code in emulator",
            "Run");
    this.add(button);

    button =
        makeButton(
            "pause", EventManager.EVENT.EMULATOR_PAUSE.toString(), "Pause emulation", "Pause");
    this.add(button);

    button =
        makeButton(
            "step",
            EventManager.EVENT.EMULATOR_STEP.toString(),
            "Step. Execute one instruction at a time",
            "Step");
    this.add(button);

    button =
        makeButton(
            "step_over",
            EventManager.EVENT.EMULATOR_STEP_OVER.toString(),
            "Step Over. Execute one instruction or call at a time",
            "Step Over");
    this.add(button);

    button =
        makeButton(
            "reset", EventManager.EVENT.EMULATOR_RESET.toString(), "Reset emulator", "Reset");
    this.add(button);

    this.addSeparator();

    button =
        makeButton(
            "variable_view",
            EventManager.EVENT.VARIABLE_VIEW.toString(),
            "View variables",
            "Variable View");
    this.add(button);

    button =
        makeButton(
            "memory_view", EventManager.EVENT.MEMORY_VIEW.toString(), "View memory", "Memory View");
    this.add(button);
  }

  /**
   * Creates a toolbar button.
   *
   * @calledby GUIToolBar
   * @param imageName filename of image
   * @param actionCommand command for action
   * @param toolTipText text to display when hover mouse over button
   * @param altText a brief textual description of the image
   * @return instance of button
   */
  private JButton makeButton(
      String imageName, String actionCommand, String toolTipText, String altText) {
    // look for the image.
    String imgLocation = "graphics/toolbar/" + imageName + ".png";

    // create and initialize the button.
    JButton button = new JButton();
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.addActionListener(this);

    button.setIcon(new ImageIcon(Utilities.loadImage(imgLocation), altText));

    return button;
  }

  /**
   * Register toolbar buttons with state manager.
   *
   * @calledby GUIToolBar()
   * @calls StateManager.addItem()
   * @param stateManager reference to State Manager
   */
  private void registerWithStateManager(StateManager stateManager) {
    for (int i = 0; i < this.getComponentCount(); i++) {
      Component comp = this.getComponentAtIndex(i);

      // if it's a JButton (and not a separator), then update state
      if (comp.getClass().getName().equals("javax.swing.JButton")) {
        try {
          EventManager.EVENT event =
              this.eventManager.getEvent(((JButton) comp).getActionCommand());
          stateManager.addItem(event, (AbstractButton) comp);
        } catch (EventException ex) {
          System.out.println("Error: " + ex.getMessage());
        }
      }
    }
  }

  /**
   * Invoked when a GUI action occurs, forwards it as an event to the EventManager object.
   *
   * @calls EventManager.sendEvent()
   * @param e action event object
   */
  public void actionPerformed(ActionEvent e) {
    try {
      EventManager.EVENT event = this.eventManager.getEvent(e.getActionCommand());
      eventManager.sendEvent(event);
    } catch (EventException ex) {
      System.out.println("Error: " + ex.getMessage());
    }
  }
}
