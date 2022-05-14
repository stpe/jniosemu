package jniosemu.emulator.memory.io;

import java.util.Vector;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryInt;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;

/** Handle the dipswitches */
public class LedDevice extends MemoryBlock {
  /** Memory address which this uses */
  private static final int MEMORYADDR = 0x810;
  /** Memory length it uses */
  private static final int MEMORYLENGTH = 16;
  /** Name of memoryblock */
  private static String MEMORYNAME = "Leds";
  /** Number of leds */
  private static final int COUNT = 4;
  /** Containing the states of each dipswitch */
  private Vector<Boolean> value;
  /** Used EventManager */
  private EventManager eventManager;

  /**
   * Init ButtonDevice
   *
   * @post Add events. Init states.
   * @calledby IOManager()
   * @param memory current MemoryManager
   * @param eventManager current EventManager
   */
  public LedDevice(EventManager eventManager, MemoryManager memoryManager) {
    this.name = MEMORYNAME;
    this.start = MEMORYADDR;
    this.length = MEMORYLENGTH;

    this.memory = new byte[this.length];

    this.eventManager = eventManager;

    this.reset();
  }

  /**
   * Reset
   *
   * @calledby IOManager.reset()
   * @param memory current MemoryManager
   */
  public void reset() {
    this.resetState();

    this.changed = 0;
    this.value = new Vector<Boolean>(COUNT);
    for (int i = 0; i < COUNT; i++) this.value.add(i, false);

    this.sendEvent();
  }

  public boolean resetState() {
    this.clearState();

    return false;
  }

  public void writeByte(int addr, byte value) throws MemoryException {
    int mapAddr = this.mapAddr(addr);

    if (mapAddr == 0) {
      value &= (byte) 0xf;
      memory[0] = value;

      this.value = Utilities.intToVector(Utilities.unsignedbyteToInt(value), COUNT);
      this.sendEvent();
    } else if (mapAddr < 0 || mapAddr > 3) {
      throw new MemoryException(addr);
    }

    this.sourceCode = null;
    this.setState(mapAddr, MemoryInt.STATE.WRITE);
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
  public void sendEvent() {
    this.eventManager.sendEvent(EventManager.EVENT.LED_UPDATE, this.value);
  }
}
