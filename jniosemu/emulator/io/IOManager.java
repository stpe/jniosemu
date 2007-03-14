package jniosemu.emulator.io;

import java.util.ArrayList;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;

public class IOManager
{
	private MemoryManager memory = null;
	private EventManager eventManager = null;
	private ArrayList<IODevice> ioDevices = new ArrayList<IODevice>();

	/**
	 * Init IOManager
	 *
	 * @post Populate ioDevices.
	 *
	 * @param memory  The MemoryManager that is currently used
	 */
	public IOManager(MemoryManager memory, EventManager eventManager) {
		this.reset(memory, eventManager);
	}

	public void reset(MemoryManager memory) {
		this.reset(memory, null);
	}

	public void reset(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;

		if (eventManager != null)
			this.eventManager = eventManager;

		this.ioDevices.add(new LedDevice(this.memory, this.eventManager));
		// this.ioDevices.add(new ButtonDevice(this.memory, this.eventManager));
		this.ioDevices.add(new DipDevice(this.memory, this.eventManager));
	}
}
