package jniosemu.emulator.memory.io;

import java.util.Vector;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

/**
 * Handle the dipswitches
 */
public class LedDevice extends MemoryBlock
{
	/**
	 * Memory address which this uses
	 */
	private static final int MEMORYADDR = 0x810;
	/**
	 * Memory length it uses
	 */
	private static final int MEMORYLENGTH = 16;
	/**
	 * Name of memoryblock
	 */
	private static String MEMORYNAME = "Leds";
	/**
	 * Number of leds
	 */
	private static final int COUNT = 4;
	/**
	 * Containing the states of each dipswitch
	 */
	private Vector<Boolean> state;
	/**
	 * Used EventManager
	 */
	private EventManager eventManager;

	/**
	 * Init ButtonDevice
	 *
	 * @post Add events. Init states.
	 * @calledby IOManager()
	 *
	 * @param memory  current MemoryManager
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
	 * @calledby  IOManager.reset()
	 *
	 * @param memory current MemoryManager
	 */
	public void reset() {
		this.changed = true;
		this.state = new Vector<Boolean>(COUNT);
		for (int i = 0; i < COUNT; i++)
			this.state.add(i, false);

		this.sendEvent();
	}

	public boolean resetState() {
		this.changed = false;
		return false;
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		int mapAddr = this.mapAddr(addr);

		if (mapAddr == 0) {
			value &= (byte)0xf;
			memory[0] = value;

			this.state = Utilities.intToVector(Utilities.unsignedbyteToInt(value), COUNT);
			this.sendEvent();
		} else if (mapAddr < 0 || mapAddr > 3) {
			throw new MemoryException(addr);
		}

		this.changed = true;
	}

	public byte readByte(int addr) throws MemoryException {
		byte ret = 0;
		try {
			ret = memory[this.mapAddr(addr)];
		} catch (Exception e) {
			throw new MemoryException(addr);
		}

		this.changed = true;
		return ret;
	}

	/**
	 * Send states to eventManager
	 *
	 * @calledby ButtonDevice(), reset(), update()
	 */
	public void sendEvent() {
		this.eventManager.sendEvent(EventManager.EVENT.LED_UPDATE, this.state);
	}

}
