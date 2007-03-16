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
	 * @calledby EmulatorManager.load()
	 * @calls LedDevice(), ButtonDevice(), DipSwitchDevice()
	 *
	 * @param memory  MemoryManager currently used
	 * @param eventManager EventManager currently used
	 */
	public IOManager(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;
		this.eventManager = eventManager;

		this.ioDevices.add(new LedDevice(this.memory, this.eventManager));
		this.ioDevices.add(new ButtonDevice(this.memory, this.eventManager));
		this.ioDevices.add(new DipSwitchDevice(this.memory, this.eventManager));
	}

	/**
	 * Reset
	 *
	 * @calledby EmulatorManager.load();
	 * @calls IODevice.reset()
	 *
	 * @param memory Current MemoryManager
	 */
	public void reset(MemoryManager memory) {
		this.memory = memory;

		for (IODevice ioDevice: this.ioDevices)
			ioDevice.reset(this.memory);
	}
}
