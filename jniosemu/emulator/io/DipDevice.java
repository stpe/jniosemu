package jniosemu.emulator.io;

import java.util.Vector;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.Events;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;
/**
 * Handle the dipswitches
 */
public class DipSwitchDevice extends IODevice implements EventObserver
{
	public static int MEMORYADDR = 0x850;
	public static int COUNT = 4;

	private Vector<Boolean> state;
	private MemoryManager memory;
	private EventManager eventManager;

	public DipDevice(MemoryManager memory, EventManager eventManager) {
		this.reset(memory, eventManager);
	}

	public void reset(MemoryManager memory, EventManager eventManager) {
		this.memory = memory;
		this.eventManager = eventManager;

		this.eventManager.addEventObserver(Events.EVENTID_GUI_DIPSWITCHES, this);
		this.memory.register("DipSwitches", MEMORYADDR, 16, this);

		this.state = new Vector<Boolean>(COUNT);
		for (int i = 0; i < COUNT; i++)
			this.state.add(i, false);

		this.eventManager.sendEvent(Events.EVENTID_UPDATE_DIPSWITCHES, this.state);
	}

	/**
	 * When memory change in in this region this method is called. And then we
	 * want to restore the memory.
	 *
	 * @calledby MemoryManager.memoryChange()
	 */
	public void memoryChange() {
		this.memory.writeInt(MEMORYADDR     , this.vectorToInt(this.state), false);
		this.memory.writeInt(MEMORYADDR +  4, 0, false);
		this.memory.writeInt(MEMORYADDR +  8, 0, false);
		this.memory.writeInt(MEMORYADDR + 12, 0, false);
	}

	public void update(String eventIdentifier, Object obj) {
		if (eventIdentifier.equals(Events.EVENTID_GUI_DIPSWITCHES)) {
			int index = ((Integer)obj).intValue();
			this.state.set(index, !this.state.get(index));
			this.memory.writeInt(MEMORYADDR, this.vectorToInt(this.state), false);
		}
	}
}
