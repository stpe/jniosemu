package jniosemu.emulator.memory.io;

import java.util.LinkedList;
import java.util.Queue;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

/**
 * Handle the SerialPort
 */
public class SerialDevice extends MemoryBlock implements EventObserver
{
	/**
	 * Address to memory where this is placed
	 */
	private static final int MEMORYADDR = 0x860;
	/**
	 * Length of memory that is used
	 */
	private static final int MEMORYLENGTH = 12;
	/**
	 * Name of memoryblock
	 */
	private static final String MEMORYNAME = "Serial";

	private Queue<Byte> inputBuffer = new LinkedList<Byte>();
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
	public SerialDevice(EventManager eventManager, MemoryManager memoryManager) {
		this.name = MEMORYNAME;
		this.start = MEMORYADDR;
		this.length = MEMORYLENGTH;

		this.memory = new byte[this.length];

		this.eventManager = eventManager;
		this.memoryManager = memoryManager;

		this.eventManager.addEventObserver(EventManager.EVENT.SERIAL_INPUT, this);

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
		this.inputBuffer.clear();
	}

	public boolean resetState() {
		if (!this.inputBuffer.isEmpty()) {
			memory[0] = this.inputBuffer.poll();
			memory[8] |= 0x80;
		}

		return false;
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		int mapAddr = this.mapAddr(addr);

		if (mapAddr == 4) {
			memory[4] = value;
			this.eventManager.sendEvent(EventManager.EVENT.SERIAL_OUTPUT, Utilities.unsignedbyteToInt(value));
		} else if (mapAddr < 4 || mapAddr > 7) {
			throw new MemoryException(addr);
		}
	}

	public byte readByte(int addr) throws MemoryException {
		int mapAddr = this.mapAddr(addr);

		if (mapAddr == 0) {
			memory[8] &= 0x7F;
			return memory[0];
		} else {
			throw new MemoryException(addr);
		}
	}

	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch(eventIdentifier) {
			case SERIAL_INPUT:
				this.inputBuffer.offer(((Byte)obj).byteValue());
				break;
		}
	}
}
