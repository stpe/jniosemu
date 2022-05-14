package jniosemu.gui;

import java.util.*;
import javax.swing.AbstractButton;
import jniosemu.events.*;

/** Manages enabled/disabled state of toolbar buttons and menu items. */
public class StateManager implements EventObserver {
  /** Manages which items are associated to which event. */
  Hashtable<EventManager.EVENT, ArrayList<AbstractButton>> stateTable =
      new Hashtable<EventManager.EVENT, ArrayList<AbstractButton>>();

  /** Reference to EventManager used to receive and send events. */
  private EventManager eventManager;

  /** Creates an instance of stateTable. */
  public StateManager(EventManager eventManager) {
    this.eventManager = eventManager;

    // add events to listen to
    EventManager.EVENT[] events = {
      EventManager.EVENT.APPLICATION_START,
      EventManager.EVENT.EMULATOR_END,
      EventManager.EVENT.EMULATOR_READY,
      EventManager.EVENT.EMULATOR_START,
      EventManager.EVENT.EMULATOR_STOP,
      EventManager.EVENT.EDITOR_UPDATE_UNDO_STATE,
      EventManager.EVENT.EDITOR_UPDATE_REDO_STATE
    };

    this.eventManager.addEventObserver(events, this);
  }

  /**
   * Add item that is associated with identifier.
   *
   * @pre stateTable instance must exist.
   * @post Item added to stateTable.
   * @calledby GUIToolBar.setup(), GUIMenuBar.setup(), GUIMenuBar.setupInstructions()
   * @param eventIdentifier String identifying the event
   * @param obj Object that is associated with the event
   */
  public void addItem(EventManager.EVENT eventIdentifier, AbstractButton obj) {
    // create list of items if it doesn't exist
    if (!stateTable.containsKey(eventIdentifier)) {
      stateTable.put(eventIdentifier, new ArrayList<AbstractButton>());
    }

    // get list of existing items
    ArrayList<AbstractButton> itemList = stateTable.get(eventIdentifier);

    // add item to list
    itemList.add(obj);
  }

  /**
   * Overloaded variant of addItem that takes multiple eventIdentifiers.
   *
   * @param eventIdentifiers Array of string identifying the events
   * @param obj Object that is associated with the event
   */
  public void addItem(EventManager.EVENT[] eventIdentifiers, AbstractButton obj) {
    for (EventManager.EVENT eventIdentifier : eventIdentifiers) {
      addItem(eventIdentifier, obj);
    }
  }

  /** */
  private void setEnabled(EventManager.EVENT eventIdentifier, boolean state) {
    // get list of items
    ArrayList<AbstractButton> itemList = stateTable.get(eventIdentifier);

    // if no items associated with event, do nothing
    if (itemList == null) return;

    // iterate over all items associated with event
    for (AbstractButton item : itemList) {
      item.setEnabled(state);
    }
  }

  public void update(EventManager.EVENT eventIdentifier, Object obj) {
    switch (eventIdentifier) {
      case APPLICATION_START:
        setEnabled(EventManager.EVENT.EMULATOR_RUN, false);
        setEnabled(EventManager.EVENT.EMULATOR_PAUSE, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP_OVER, false);
        setEnabled(EventManager.EVENT.EMULATOR_RESET, false);
        break;
      case EMULATOR_END:
        setEnabled(EventManager.EVENT.EMULATOR_RUN, false);
        setEnabled(EventManager.EVENT.EMULATOR_PAUSE, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP_OVER, false);
        setEnabled(EventManager.EVENT.EMULATOR_RESET, true);
        break;
      case EMULATOR_READY:
        setEnabled(EventManager.EVENT.EMULATOR_RUN, true);
        setEnabled(EventManager.EVENT.EMULATOR_PAUSE, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP, true);
        setEnabled(EventManager.EVENT.EMULATOR_STEP_OVER, true);
        setEnabled(EventManager.EVENT.EMULATOR_RESET, false);
        break;
      case EMULATOR_START:
        setEnabled(EventManager.EVENT.EMULATOR_RUN, false);
        setEnabled(EventManager.EVENT.EMULATOR_PAUSE, true);
        setEnabled(EventManager.EVENT.EMULATOR_STEP, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP_OVER, false);
        setEnabled(EventManager.EVENT.EMULATOR_RESET, true);
        break;
      case EMULATOR_STOP:
        setEnabled(EventManager.EVENT.EMULATOR_RUN, true);
        setEnabled(EventManager.EVENT.EMULATOR_PAUSE, false);
        setEnabled(EventManager.EVENT.EMULATOR_STEP, true);
        setEnabled(EventManager.EVENT.EMULATOR_STEP_OVER, true);
        setEnabled(EventManager.EVENT.EMULATOR_RESET, true);
        break;

      case EDITOR_UPDATE_UNDO_STATE:
        setEnabled(EventManager.EVENT.EDITOR_UNDO, ((Boolean) obj).booleanValue());
        break;
      case EDITOR_UPDATE_REDO_STATE:
        setEnabled(EventManager.EVENT.EDITOR_REDO, ((Boolean) obj).booleanValue());
        break;
    }
  }
}
