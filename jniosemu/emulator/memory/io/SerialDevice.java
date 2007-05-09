package jniosemu.emulator.memory.io;

import java.util.LinkedList;
import java.util.Queue;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryInt;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

/**
 * Handle the SerialPort
 */
public class SerialDevice extends MemoryBlock implements EventObserver
{
	/**
	 * Length of memory that is used
	 */
	private static final int MEMORYLENGTH = 16;

	private Queue<Character> inputBuffer = new LinkedList<Character>();
	/**
	 * Used EventManager
	 */
	private EventManager eventManager;

	/**
	 * Used MemoryManger
	 */
	private MemoryManager memoryManager;

	private EventManager.EVENT inEvent = null;
	private EventManager.EVENT outEvent = null;
	
	/**
	 * Init ButtonDevice
	 *
	 * @post Add events. Init states.
	 * @calledby IOManager()
	 *
	 * @param memory  current MemoryManager
	 * @param eventManager current EventManager
	 */
	public SerialDevice(EventManager eventManager, MemoryManager memoryManager, String name, int startAddr, EventManager.EVENT inEvent, EventManager.EVENT outEvent) {
		this.name = name;
		this.start = startAddr;
		this.length = MEMORYLENGTH;
		this.inEvent = inEvent;
		this.outEvent = outEvent;

		this.memory = new byte[this.length];

		this.eventManager = eventManager;
		this.memoryManager = memoryManager;

		if (this.inEvent != null)
			this.eventManager.addEventObserver(this.inEvent, this);

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
		this.resetState();
		this.clearState();
		this.changed = true;
		this.inputBuffer.clear();
	}

	public boolean resetState() {
		this.clearState();

		this.changed = false;

		if (!this.inputBuffer.isEmpty() && (memory[8] & 0x80) == 0) {
			memory[0] = (byte)(this.inputBuffer.poll() & 0xFF);
			this.setState(0, MemoryInt.STATE.WRITE);
			memory[8] |= 0x80;
			this.setState(8, MemoryInt.STATE.WRITE);
			this.changed = true;
		}
		if ((memory[8] & 0x40) == 0) {
			memory[8] |= 0x40;
			this.setState(8, MemoryInt.STATE.WRITE);
			this.changed = true;
		}

		return false;
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		int mapAddr = this.mapAddr(addr);

		if (mapAddr == 4) {
			memory[4] = value;
			this.eventManager.sendEvent(this.outEvent, (char)(value & 0xFF));
		} else if (mapAddr == 12) {
			memory[12] = (byte)(value & 0xC0);
		} else if (mapAddr < 4 || mapAddr >= 8 && mapAddr < 12 || mapAddr >= 16) {
			throw new MemoryException(addr);
		}

		this.setState(mapAddr, MemoryInt.STATE.WRITE);
		this.changed = true;
	}

	public byte readByte(int addr) throws MemoryException {
		int mapAddr = this.mapAddr(addr);

		byte ret = 0;
		if (mapAddr == 0) {
			memory[8] &= 0x7F;
			this.setState(8, MemoryInt.STATE.WRITE);
			ret = memory[0];
		} else if (mapAddr >= 1 && mapAddr < 4 || mapAddr >= 8 && mapAddr < 16) {
			ret = memory[mapAddr];
		} else {
			throw new MemoryException(addr);
		}

		this.setState(mapAddr, MemoryInt.STATE.READ);
		this.changed = true;
		return ret;
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		if (eventIdentifier == this.inEvent) {
			this.inputBuffer.offer(((Character)obj).charValue());
		}
	}
}
