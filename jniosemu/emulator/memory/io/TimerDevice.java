package jniosemu.emulator.memory.io;

import java.util.Queue;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;

/**
 * Handle the Timer
 */
public class TimerDevice extends MemoryBlock
{
	/**
	 * Address to memory where this is placed
	 */
	private static final int MEMORYADDR = 0x820;
	/**
	 * Length of memory that is used
	 */
	private static final int MEMORYLENGTH = 24;
	/**
	 * Name of memoryblock
	 */
	private static final String MEMORYNAME = "Timer";
	/**
	 * Used MemoryManger
	 */
	private MemoryManager memoryManager;
	/**
	 * Contains the memory data
	 */
	private byte[] memory;

	private long counter = 0;
	private long period = 0;
	private boolean counting = false;

	/**
	 * Init the Timer
	 *
	 * @post Add events. Init states.
	 * @calledby IOManager()
	 *
	 * @param memory  current MemoryManager
	 * @param eventManager current EventManager
	 */
	public TimerDevice(EventManager eventManager, MemoryManager memoryManager) {
		this.name = MEMORYNAME;
		this.start = MEMORYADDR;
		this.length = MEMORYLENGTH;

		this.memoryManager = memoryManager;

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
		this.memory = new byte[this.length];
		this.counter = 0;
		this.period = 0;
		this.counting = false;
	}

	public boolean resetState() {
		if (this.counting) {
			if (this.counter > 0) {
				this.counter--;
			} else if ((this.memory[4] & 0x2) > 0) {
				this.counter = this.period;
			} else {
				this.counting = false;
				this.memory[0] &= 0xFD;
				this.memoryManager.setState(this.start, MemoryManager.STATE.WRITE);
			}
		}

		System.out.println(this.counter);

		return false;
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		int mapAddr = this.mapAddr(addr);

		if (mapAddr == 0) {
			memory[0] &= 0xFE;
		} else if (mapAddr == 4) {
			value &= 0xf;
			memory[4] = value;
			if ((value & 0x8) > 0) {
				this.counting = false;
			}
			if ((value & 0x4) > 0) {
				this.counting = true;
				this.counter = this.period;
			}
		} else if (mapAddr >= 16 && mapAddr < 24) {
			byte[] snapshot = Utilities.longToByteArray(this.counter);
			for (int i = 0; i < snapshot.length; i++) {
				memory[16 + i] = snapshot[i];
				this.memoryManager.setState(this.start + i, MemoryManager.STATE.WRITE);
			}
		} else if (mapAddr >= 8 && mapAddr < 16) {
			this.memory[mapAddr] = value;
		}
	}

	public byte readByte(int addr) throws MemoryException {
		try {
			return this.memory[this.mapAddr(addr)];
		} catch (Exception e) {
			throw new MemoryException(addr);
		}
	}

}
