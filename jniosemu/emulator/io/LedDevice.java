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
	public static int MEMORYADDR = 0x810;
	/**
	 * Memory length it uses
	 */
	public static int MEMORYLENGTH = 16;
	/**
	 * Number of leds
	 */
	public static int COUNT = 4;

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
		this.reset(memory, eventManager);
	}

	/**
	 * Reset
	 *
	 * @calledby LedDevice()
	 *
	 * @param memory Current MemoryManager
	 * @param eventManager Current EventManager
	 */
	public void reset(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;
		this.eventManager = eventManager;

		this.memory.register("Leds", MEMORYADDR, MEMORYLENGTH, this);

		this.state = new Vector<Boolean>(COUNT);
		for (int i = 0; i < COUNT; i++)
			this.state.add(i, false);

		this.eventManager.sendEvent(Events.EVENTID_UPDATE_LEDS, this.state);
	}

	public void memoryChange() {
		int value = this.memory.readInt(MEMORYADDR, false);
		this.state = this.intToVector(value, COUNT);

		this.eventManager.sendEvent(Events.EVENTID_UPDATE_LEDS, this.state);
	}
}
