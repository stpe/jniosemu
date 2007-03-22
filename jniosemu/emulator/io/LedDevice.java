package jniosemu.emulator.io;

import java.util.Vector;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.Events;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

/**
 * Handle all leds
 */
public class LedDevice extends IODevice
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
	 * Contains the state of all leds
	 */
	private Vector<Boolean> state;
	/**
	 * Current MemoryManager
	 */
	private MemoryManager memory;
	/**
	 * Current EventManager
	 */
	private EventManager eventManager;

	/**
	 * Init LedDevice
	 *
	 * @calledby IOManager.reset()
	 *
	 * @param memory Current MemoryManager
	 * @param eventManager Current EventManager
	 */
	public LedDevice(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;
		this.eventManager = eventManager;

		this.memory.register(MEMORYNAME, MEMORYADDR, MEMORYLENGTH, this);
		this.memoryChange();
	}

	/**
	 * Reset
	 *
	 * @calledby LedDevice()
	 *
	 * @param memory Current MemoryManager
	 */
	public void reset(MemoryManager memory) {
		this.memory = memory;

		this.memory.register(MEMORYNAME, MEMORYADDR, MEMORYLENGTH, this);
		this.memoryChange();
	}

	public void memoryChange() {
		int value = this.memory.readInt(MEMORYADDR, false);
		this.state = this.intToVector(value, COUNT);

		this.sendEvent();
	}

	public void sendEvent() {
		this.eventManager.sendEvent(Events.EVENTID_UPDATE_LEDS, this.state);
	}
}
