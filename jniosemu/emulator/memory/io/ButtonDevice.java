package jniosemu.emulator.io;

import java.util.Vector;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;
/**
 * Handle the dipswitches
 */
public class ButtonDevice extends MemoryBlock implements EventObserver
{
	/**
	 * Address to memory where this is placed
	 */
	private static final int MEMORYADDR = 0x840;
	/**
	 * Length of memory that is used
	 */
	private static final int MEMORYLENGTH = 16;
	/**
	 * Name of memoryblock
	 */
	private static final String MEMORYNAME = "Buttons";
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
	 * Contains the memory data
	 */
	private byte[] memory;

	/**
	 * Init ButtonDevice
	 *
	 * @post Add events. Init states.
	 * @calledby IOManager()
	 *
	 * @param memory  current MemoryManager
	 * @param eventManager current EventManager
	 */
	public ButtonDevice(EventManager eventManager) {
		this.name = MEMORYNAME;
		this.start = MEMORYADDR;
		this.length = MEMORYLENGTH;

		this.memory = new byte[this.length];

		this.eventManager = eventManager;

		EventManager.EVENT[] events = {
			EventManager.EVENT.BUTTON_RELEASE,
			EventManager.EVENT.BUTTON_PRESS,
			EventManager.EVENT.BUTTON_TOGGLE
		};

		this.eventManager.addEventObserver(events, this);
	}

	/**
	 * Reset
	 *
	 * @calledby  IOManager.reset()
	 *
	 * @param memory current MemoryManager
	 */
	public void reset() {
		this.state = new Vector<Boolean>(COUNT);
		for (int i = 0; i < COUNT; i++)
			this.state.add(i, false);

		this.sendEvent();
	}

	public boolean resetState() {
		
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		if (!this.inRange(addr))
			throw new MemoryException(addr);
	}

	public byte readByte(int addr) throws MemoryException {
		if (!this.inRange(addr))
			throw new MemoryException(addr);

		if (addr == MEMORYADDR + 3) {
			return (byte)vectorToInt(this.state);
		}

		return 0;
	}

	/**
	 * Send states to eventManager
	 *
	 * @calledby ButtonDevice(), reset(), update()
	 */
	private void sendEvent() {
		this.eventManager.sendEvent(EventManager.EVENT.BUTTON_UPDATE, this.state);
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
		this.memory.writeInt(MEMORYADDR, this.vectorToInt(this.state), false);

		this.sendEvent();
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch(eventIdentifier) {
			case BUTTON_RELEASE:
				this.setState(((Integer)obj).intValue(), false);
				break;
			case BUTTON_PRESS:
				this.setState(((Integer)obj).intValue(), true);
				break;
			case BUTTON_TOGGLE:
				int index = ((Integer)obj).intValue();
				this.setState(index, !this.state.get(index));
				break;
		}
	}
}
