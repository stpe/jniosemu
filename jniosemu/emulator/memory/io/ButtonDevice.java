package jniosemu.emulator.memory.io;

import java.util.Vector;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryInt;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

/** Handle the dipswitches */
public class ButtonDevice extends MemoryBlock implements EventObserver {
  /** Address to memory where this is placed */
  private static final int MEMORYADDR = 0x840;
  /** Length of memory that is used */
  private static final int MEMORYLENGTH = 16;
  /** Name of memoryblock */
  private static final String MEMORYNAME = "Buttons";
  /** Number of buttons */
  private static final int COUNT = 4;
  /** Containing the states of each dipswitch */
  private Vector<Boolean> value;
  /** Used EventManager */
  private EventManager eventManager;
  /** Used MemoryManger */
  private MemoryManager memoryManager;
  /** */
  private boolean valueChanged = false;

  /**
   * Init ButtonDevice
   *
   * @post Add events. Init states.
   * @calledby IOManager()
   * @param memory current MemoryManager
   * @param eventManager current EventManager
   */
  public ButtonDevice(EventManager eventManager, MemoryManager memoryManager) {
    this.name = MEMORYNAME;
    this.start = MEMORYADDR;
    this.length = MEMORYLENGTH;

    this.memory = new byte[this.length];

    this.eventManager = eventManager;
    this.memoryManager = memoryManager;

    EventManager.EVENT[] events = {
      EventManager.EVENT.BUTTON_RELEASE,
      EventManager.EVENT.BUTTON_PRESS,
      EventManager.EVENT.BUTTON_TOGGLE
    };

    this.eventManager.addEventObserver(events, this);

    this.reset();
  }

  /**
   * Reset
   *
   * @calledby IOManager.reset()
   * @param memory current MemoryManager
   */
  public void reset() {
    this.clearState();

    this.changed = 0;
    this.valueChanged = false;
    this.value = new Vector<Boolean>(COUNT);
    for (int i = 0; i < COUNT; i++) this.value.add(i, false);

    this.sendEvent();
  }

  public boolean resetState() {
    this.clearState();

    if (this.valueChanged) {
      memory[0] = Utilities.vectorToByte(this.value);
      this.setState(0, MemoryInt.STATE.WRITE);

      this.valueChanged = false;
    }

    return false;
  }

  public void writeByte(int addr, byte value) throws MemoryException {
    throw new MemoryException(addr);
  }

  public byte readByte(int addr) throws MemoryException {
    byte ret = 0;
    int mapAddr = this.mapAddr(addr);
    try {
      ret = memory[mapAddr];
    } catch (Exception e) {
      throw new MemoryException(addr);
    }

    this.setState(mapAddr, MemoryInt.STATE.READ);
    return ret;
  }

  /**
   * Send states to eventManager
   *
   * @calledby ButtonDevice(), reset(), update()
   */
  private void sendEvent() {
    this.eventManager.sendEvent(EventManager.EVENT.BUTTON_UPDATE, this.value);
  }

  /**
   * Set state of a button
   *
   * @calledby update()
   * @param index button index
   * @param state new state
   */
  private void setValue(int index, boolean value) {
    this.valueChanged = true;
    this.value.set(index, value);

    this.sendEvent();
  }

  public void update(EventManager.EVENT eventIdentifier, Object obj) {
    switch (eventIdentifier) {
      case BUTTON_RELEASE:
        this.setValue(((Integer) obj).intValue(), false);
        break;
      case BUTTON_PRESS:
        this.setValue(((Integer) obj).intValue(), true);
        break;
      case BUTTON_TOGGLE:
        int index = ((Integer) obj).intValue();
        this.setValue(index, !this.value.get(index));
        break;
    }
  }
}
