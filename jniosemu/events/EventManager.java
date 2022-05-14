package jniosemu.events;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * EventManager manages events that may have several senders and/or receivers. Events are identified
 * by event identifier strings to distinguish between different types of events.
 *
 * <p>Classes that want to listen to events must implement the EventObserver interface.
 *
 * <p>To listen to an event the object adds itself as an observer and states which event identifier
 * to listen to. It's possible to listen to more than one event by adding the same object several
 * times using different event identifiers.
 *
 * <p>Any object may send an event by simply calling the sendEvent method.
 */
public final class EventManager {
  public static enum EVENT {
    ABOUT_VIEW, // Show About window of application
    APPLICATION_EXIT, // Exit application
    APPLICATION_START, // Application is started
    APPLICATION_TAB_CHANGE, // Change tab in GUI
    APPLICATION_TAB_TOGGLE, // Toggles between view the emulator or editor
    APPLICATION_TITLE_CHANGE, // Trigger change of window title (used when text in editor is
                              // modified)
    BUTTON_PRESS, // IO button in GUI was pressed
    BUTTON_RELEASE, // IO button in GUI was released
    BUTTON_TOGGLE, // IO button in GUI was toggled
    BUTTON_UPDATE, // Update state of buttons
    COMPILER_COMPILE, // Start compiling
    COMPILER_COMPILE_INIT, // Start compile requested from GUI
    COMPILER_ERROR, // Error occured during compilation
    CURRENT_DIRECTORY, // Current working directory has changed
    DIPSWITCH_TOGGLE, // IO dipswitches in GUI has changed
    DIPSWITCH_UPDATE, // Update state of dipswitches
    DOCUMENT_NEW, // New document
    DOCUMENT_NEW_DONE, // New document created
    DOCUMENT_OPEN, // Open document
    DOCUMENT_OPEN_DONE, // A document has been opened
    DOCUMENT_SAVE, // Save document
    DOCUMENT_SAVE_AS, // Save document as different file name
    DOCUMENT_SAVE_DONE, // Document has been saved
    EDITOR_CURSOR_CHANGE, // Cursor changed in editor
    EDITOR_INSERT_INSTRUCTION, // Insert instruction using insert menu
    EDITOR_UNDO, // Perform undo in editor
    EDITOR_REDO, // Perform redo in editor
    EDITOR_UPDATE_UNDO_STATE, // Update state of undo menu item
    EDITOR_UPDATE_REDO_STATE, // Update state of redo menu item
    EDITOR_MOVE_TO_LINE, // Move cursor (caret) to line
    EMULATOR_CLEAR,
    EMULATOR_END, // Emulation ended
    EMULATOR_ERROR, // Error occured during emulation
    EMULATOR_PAUSE, // Pause emulation
    EMULATOR_READY, // Compilation of current source code successfully done
    EMULATOR_RESET, // Reset emulator
    EMULATOR_RUN, // Start emulation
    EMULATOR_SPEED, // Change the emulator speed
    EMULATOR_START, // Emulation started
    EMULATOR_STEP, // Step emulation one instruction
    EMULATOR_STEP_OVER, // Step over call
    EMULATOR_STOP, // Emulation stopped
    EMULATOR_TOGGLE_STEP_OVER, // Toggle the step over setting that is used in run
    EMULATOR_BREAKPOINT_UPDATE, // Breakpoint has been changed
    EMULATOR_BREAKPOINT_TOGGLE, // Breakpoint has been toggled in GUI
    EXCEPTION, // Exception has occured
    LED_UPDATE, // Update state of leds
    MEMORY_CHANGE, // If the memory changed
    MEMORY_REQUEST_UPDATE, // Trigger a MEMORY_CHANGE event
    MEMORY_VIEW, // Show Memory View window
    PROGRAMCOUNTER_CHANGE, // Program counter in emulated program has changed
    PROGRAM_REQUEST_UPDATE, // Request program
    PROGRAM_CHANGE, // Program has changed
    REGISTER_CHANGE, // Register in emulated program has changed
    REGISTER_VIEW_SELECT, // A register is selected in the registers list
    STATE_CHANGE, // Update state (disabled/enabled) of elements in menu and toolbar
    UART0_INPUT,
    UART0_OUTPUT, // Send a character to console
    UART0_VIEW, // Show Console window
    UART1_INPUT, // Send a character to emulated serial port
    UART1_OUTPUT, // Send a character out of emulated serial port
    UART1_VIEW, // Show Serial Console window
    VARIABLE_CHANGE, // If variables have changed (send memory block)
    VARIABLE_REQUEST_UPDATE, // Trigger a VARIABLE_CHANGE and VARIABLE_VECTOR event
    VARIABLE_VECTOR, // Vector of Variable for Variable View
    VARIABLE_VIEW, // Show Variable View window
  };

  /** Manages which observers are listening to which event. */
  private HashMap<EVENT, ArrayList<EventObserver>> eventTable =
      new HashMap<EVENT, ArrayList<EventObserver>>();

  /** Used for string to enum conversion. */
  private HashMap<String, EVENT> stringLookup = new HashMap<String, EVENT>();

  /** Event queue. */
  private ConcurrentLinkedQueue<QueueObject> queue = new ConcurrentLinkedQueue<QueueObject>();

  /** Separate thread used to send events. */
  private EventSender sendEventThread;

  /** Starts the event sender thread and populates the hashmap for string to enum conversions. */
  public EventManager() {
    sendEventThread = new EventSender();
    sendEventThread.start();

    for (EVENT event : EVENT.values()) stringLookup.put(event.toString(), event);
  }

  /**
   * Add event observer that listens to given event identifier.
   *
   * @pre eventTable instance must exist.
   * @post Observer added to eventTable.
   * @calledby <i>EventObserver</i>
   * @param eventIdentifier string identifying the event
   * @param obj object that listens to the event
   */
  public void addEventObserver(EVENT eventIdentifier, EventObserver obj) {
    // create list of observers if it doesn't exist
    if (!eventTable.containsKey(eventIdentifier)) {
      eventTable.put(eventIdentifier, new ArrayList<EventObserver>());
    }

    // get list of existing observers
    ArrayList<EventObserver> eventObservers = eventTable.get(eventIdentifier);

    // add observer to list
    eventObservers.add(obj);
  }

  /**
   * Overloaded variant of addEventObserver that takes multiple eventIdentifiers.
   *
   * @param eventIdentifiers array of string identifying the events
   * @param obj object that listens to the event
   */
  public void addEventObserver(EVENT[] eventIdentifiers, EventObserver obj) {
    for (EVENT eventIdentifier : eventIdentifiers) {
      addEventObserver(eventIdentifier, obj);
    }
  }

  /**
   * Add event to event sender queue.
   *
   * @pre Event sender queue must be created.
   * @calledby <i>All objects that sends an event</i>
   * @calls EventSender.notify()
   * @param eventIdentifier string identifying the event
   * @param obj object to pass along to the observer
   */
  public void sendEvent(EVENT eventIdentifier, Object obj) {
    // put event in queue
    queue.add(new QueueObject(eventIdentifier, obj));

    synchronized (this.sendEventThread) {
      this.sendEventThread.notify();
    }
  }

  /**
   * Add event to event sender queue without passing along any object.
   *
   * @calls sendEvent() (overloaded method)
   * @param eventIdentifier event identifier
   */
  public void sendEvent(EVENT eventIdentifier) {
    sendEvent(eventIdentifier, null);
  }

  /**
   * Translate a string into its enum value
   *
   * @param eventIdentifier event identifier in the form of a string
   * @return enum value of the string
   * @throws EventException if eventIdentifier is unknown
   */
  public EVENT getEvent(String eventIdentifier) throws EventException {
    if (!stringLookup.containsKey(eventIdentifier))
      throw new EventException("Unknown event '" + eventIdentifier + "'.");

    return stringLookup.get(eventIdentifier);
  }

  /**
   * Event queue object. Used as container for event identifier and event object when places in send
   * queue.
   */
  private class QueueObject {
    public QueueObject(EVENT eventIdentifier, Object obj) {
      this.eventIdentifier = eventIdentifier;
      this.obj = obj;
    }

    public EVENT eventIdentifier;
    public Object obj;
  }

  /**
   * The event sender class runs in it's own thread and sends the events in the queue when it
   * receives a notify.
   */
  private class EventSender extends Thread {
    public void run() {
      while (true) {
        try {
          synchronized (this) {
            wait();
          }
        } catch (InterruptedException e) {
        }

        // send events in queue
        while (!queue.isEmpty()) {
          sendEvent(queue.poll());
        }
      }
    }

    /**
     * Notifies all observers that listens to given event identifier.
     *
     * @pre Observers must be added eventTable.
     * @checks That there are <i>EventObservers</i> listening to to the particular event. If none,
     *     the event is not sent.
     * @calledby <i>All objects that sends an event</i>
     * @calls update() method of all <i>EventObservers</i>
     * @param eventIdentifier string identifying the event
     * @param obj object to pass along to the observer
     */
    private void sendEvent(QueueObject queueObj) {
      // get list of observers
      ArrayList<EventObserver> eventObservers = eventTable.get(queueObj.eventIdentifier);

      if (eventObservers == null) {
        // debug
        // String objValue = queueObj.obj != null ? ": " + queueObj.obj : "<null>";
        // System.out.println("EventManger.sendEvent(): No observers listening to '" +
        // queueObj.eventIdentifier + "': " + objValue);
        return;
      }

      // debug
      //			String objValue = queueObj.obj != null ? ": " + queueObj.obj : "<null>";
      //			System.out.println("EventManger.sendEvent(): Event: " + queueObj.eventIdentifier + ",
      // Value: " + objValue);

      // iterate over all listening observers
      for (EventObserver eventObserver : eventObservers) {
        // have a try-catch so the EventManager don't die if something goes wrong
        //				try {
        eventObserver.update(queueObj.eventIdentifier, queueObj.obj);
        //				} catch (Exception e) {
        //					System.out.println("EventObserver.update() error: "+ e.getMessage());
        //				}
      }
    }
  }
}
