package jniosemu.emulator.memory;

/**
 * Contains a part of the memory.
 */
public abstract class MemoryBlock
{
	/**
	 * Name of the part.
	 */
	protected String name;
	/**
	 * External start address.
	 */
	protected int start;
	/**
	 * Length of the memory part.
	 */
	protected int length;

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
	protected int mapAddr(int addr) {
		return addr - this.start;
	}

	/**
	 * Return a byte from a specific memory address
	 *
	 * @calledby MemoryManager.readByte()
	 * @calls mapAddr(), notifyDevice()
	 *
	 * @param addr Memory address
	 * @return Requested byte
	 * @throws MemoryException  If the address don't exits in this memoryBlock
	 */
	public abstract byte readByte(int addr) throws MemoryException;

	/**
	 * Write a byte to memory
	 *
	 * @calledby MemoryManager.writeByte()
	 * @calls mapAddr(), notifyDevice()
	 *
	 * @param addr  Memory address where to place the byte
	 * @param value Byte that should be placed in the memory
	 * @throws MemoryException  If the address don't exits in this memoryBlock
	 */
	public abstract void writeByte(int addr, byte value) throws MemoryException;

	public abstract void reset();

	public abstract boolean resetState();
}
