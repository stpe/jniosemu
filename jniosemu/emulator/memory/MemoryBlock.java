package jniosemu.emulator.memory;

import jniosemu.emulator.io.IODevice;

/**
 * Contains info about a part of the memory.
 */
public class MemoryBlock
{
	/**
	 * Name of the part.
	 */
	private final String name;
	/**
	 * External start address.
	 */
	private final int start;
	/**
	 * Length of the memory part.
	 */
	private final int length;

	private byte[] memory;

	private final IODevice device;

	/**
	 * Init MemoryBlock.
	 *
	 * @calledby MemoryManager
	 *
	 * @param name  Name of the part
	 * @param start  External start address
	 * @param length  Length of the memory part
	 */
	public MemoryBlock(String name, int start, int length, IODevice device, byte[] memory) {
		this.name = name;
		this.start = start;
		this.length = length;
		this.device = device;

		this.memory = new byte[this.length];

		if (memory != null)
			System.arraycopy(memory, 0, this.memory, 0, this.length);
	}

	/**
	 * Get the name of the part.
	 *
	 * @calledby MemoryManager.dump()
	 *
	 * @return Name of the part
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the external start address.
	 *
	 * @calledby MemoryManager.mapAddr()
	 *
	 * @return External start address
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * Get the external start address.
	 *
	 * @calledby MemoryManager.mapAddr()
	 *
	 * @return External end address
	 */
	public int getEnd() {
		return this.start + this.length - 1;
	}

	/**
	 * Get the length of the memory part.
	 *
	 * @return Length of the memory part
	 */
	public int getLength() {
		return this.length;
	}

	public boolean inRange(int addr) {
		if (this.start <= addr && this.start + this.length > addr)
			return true;

		return false;
	}

	public int mapAddr(int addr) {
		return addr - this.start;
	}

	public byte readByte(int addr, boolean notify) throws MemoryException {
		byte value = memory[mapAddr(addr)];

		if (notify)
			this.notifyDevice();
		return value;
	}

	public void writeByte(int addr, byte value, boolean notify) throws MemoryException {
		memory[mapAddr(addr)] = value;

		if (notify)
			this.notifyDevice();
	}

	public void notifyDevice() {
		if (this.device != null)
			this.device.memoryChange();
	}

}
