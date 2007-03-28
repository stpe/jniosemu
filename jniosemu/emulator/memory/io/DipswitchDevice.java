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
public class DipswitchDevice extends MemoryBlock implements EventObserver
{
	/**
	 * Address to memory where this is placed
	 */
	private static final int MEMORYADDR = 0x850;
	/**
	 * Length of memory that is used
	 */
	private static final int MEMORYLENGTH = 16;
	/**
	 * Name of memoryblock
	 */
	private static final String MEMORYNAME = "DIPSWITCHES";
	/**
	 * Number of buttons
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
	 * Used MemoryManger
	 */
	private MemoryManager memoryManager;
	/**
	 * Contains the memory data
	 */
	private byte[] memory;
	/**
	 * 
	 */
	private boolean changed = false;

	/**
	 * Init ButtonDevice
	 *
	 * @post Add events. Init states.
	 * @calledby IOManager()
	 *
	 * @param memory  current MemoryManager
	 * @param eventManager current EventManager
	 */
	public DipswitchDevice(EventManager eventManager, MemoryManager memoryManager) {
		this.name = MEMORYNAME;
		this.start = MEMORYADDR;
		this.length = MEMORYLENGTH;

		this.memory = new byte[this.length];

		this.eventManager = eventManager;
		this.memoryManager = memoryManager;

		this.eventManager.addEventObserver(EventManager.EVENT.DIPSWITCH_TOGGLE, this);

		this.state = new Vector<Boolean>(COUNT);
		for (int i = 0; i < COUNT; i++)
			this.state.add(i, false);

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
		this.changed = false;

		this.sendEvent();
	}

	public boolean resetState() {
		if (this.changed) {
			memory[0] = Utilities.vectorToByte(this.state);
			this.memoryManager.setState(this.start, MemoryManager.STATE.WRITE);
		}
		return false;
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		throw new MemoryException(addr);
	}

	public byte readByte(int addr) throws MemoryException {
		try {
			return memory[this.mapAddr(addr)];
		} catch (Exception e) {
			throw new MemoryException(addr);
		}
	}

	/**
	 * Send states to eventManager
	 *
	 * @calledby ButtonDevice(), reset(), update()
	 */
	private void sendEvent() {
		this.eventManager.sendEvent(EventManager.EVENT.DIPSWITCH_UPDATE, this.state);
	}

	/**
	 * Set state of a button
	 *
	 * @calledby update()
	 *
	 * @param index button index
	 * @param state new state
	 */
	private void setState(int index, boolean state) {
		this.state.set(index, state);

		this.sendEvent();
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch(eventIdentifier) {
			case DIPSWITCH_TOGGLE:
				this.changed = true;
				int index = ((Integer)obj).intValue();
				this.setState(index, !this.state.get(index));
				break;
		}
	}
}
