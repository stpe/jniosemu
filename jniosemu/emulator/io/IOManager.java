package jniosemu.emulator.io;

import java.util.ArrayList;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;

/**
 * Handle all IODevices
 */
public class IOManager
{
	/**
	 * Current MemoryManager
	 */
	private MemoryManager memory = null;
	/**
	 * Current EventManager
	 */
	private EventManager eventManager = null;
	/**
	 * Containing all IODevices
	 */
	private ArrayList<IODevice> ioDevices = new ArrayList<IODevice>();

	/**
	 * Init IOManager
	 *
	 * @post Populate ioDevices.
	 * @calledby EmulatorManager
	 *
	 * @param memory  The MemoryManager that is currently used
	 */
	public IOManager(MemoryManager memory, EventManager eventManager) {
		this.reset(memory, eventManager);
	}

	/**
	 * Reset
	 *
	 * @param memory Current MemoryManager
	 */
	public void reset(MemoryManager memory) {
		this.reset(memory, null);
	}

	/**
	 * Reset
	 *
	 * @param memory Current MemoryManager
	 * @param eventManager Current EventManager
	 */
	public void reset(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;

		if (eventManager != null)
			this.eventManager = eventManager;

		this.ioDevices.add(new LedDevice(this.memory, this.eventManager));
		// this.ioDevices.add(new ButtonDevice(this.memory, this.eventManager));
		this.ioDevices.add(new DipSwitchDevice(this.memory, this.eventManager));
	}
}
