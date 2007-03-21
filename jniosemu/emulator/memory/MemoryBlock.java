package jniosemu.emulator.memory;

import jniosemu.emulator.io.IODevice;

/**
 * Contains a part of the memory.
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
	/**
	 * Contains the memory data
	 */
	private byte[] memory;
	/**
	 * If there is a IODevice attached to this memory
	 */
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

	/**
	 * Checks if a memory address exists in this memoryBlock
	 *
	 * @calledby MemoryManager.readByte(), MemoryManager.writeByte()
	 *
	 * @param addr Memory address
	 * @return If the memory address exists in this memoryBlock
	 */
	public boolean inRange(int addr) {
		if (this.start <= addr && this.start + this.length > addr)
			return true;

		return false;
	}

	/**
	 * Map an address to an internal index
	 *
	 * @calledby readByte(), writeByte()
	 *
	 * @param addr Memory address
	 * @return internal index
	 */
	private int mapAddr(int addr) {
		return addr - this.start;
	}

	/**
	 * Return a byte from a specific memory address
	 *
	 * @calledby MemoryManager.readByte()
	 * @calls mapAddr(), notifyDevice()
	 *
	 * @param addr Memory address
	 * @param notify True if the IO device that is connected to this memoryblock should be notified
	 * @return Requested byte
	 * @throws MemoryException  If the address don't exits in this memoryBlock
	 */
	public byte readByte(int addr, boolean notify) throws MemoryException {
		byte value = 0;
		try {
			value = memory[mapAddr(addr)];
		} catch (Exception e) {
			throw new MemoryException(addr);
		}

		if (notify)
			this.notifyDevice();
		return value;
	}

	/**
	 * Write a byte to memory
	 *
	 * @calledby MemoryManager.writeByte()
	 * @calls mapAddr(), notifyDevice()
	 *
	 * @param addr  Memory address where to place the byte
	 * @param value Byte that should be placed in the memory
	 * @param notify True if the IO device that is connected to this memoryblock should be notified
	 * @throws MemoryException  If the address don't exits in this memoryBlock
	 */
	public void writeByte(int addr, byte value, boolean notify) throws MemoryException {
		try {
			memory[mapAddr(addr)] = value;
		} catch (Exception e) {
			throw new MemoryException(addr);
		}

		if (notify)
			this.notifyDevice();
	}

	/**
	 * Notify the IO Device that the memory has been read of written to
	 *
	 * @checks If there isn't any connected IO Device 
	 * @calledby readByte(), writeByte()
	 * @calls IODevice.memoryChange()
	 */
	public void notifyDevice() {
		if (this.device != null)
			this.device.memoryChange();
	}

}
