package jniosemu.emulator.io;

import java.util.Vector;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.Events;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

public class LedDevice extends IODevice
{
	public static int MEMORYADDR = 0x810;
	public static int COUNT = 4;

	private Vector<Boolean> state;
	private MemoryManager memory;
	private EventManager eventManager;

	public LedDevice(MemoryManager memory, EventManager eventManager) {
		this.reset(memory, eventManager);
	}

	public void reset(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;
		this.eventManager = eventManager;

		this.memory.register("Leds", MEMORYADDR, 16, this);

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
