package jniosemu.emulator.memory;

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

	/**
	 * Init MemoryBlock.
	 *
	 * @calledby MemoryManager
	 *
	 * @param name  Name of the part
	 * @param start  External start address
	 * @param length  Length of the memory part
	 */
	public MemoryBlock(String name, int start, int length) {
		this.name = name;
		this.start = start;
		this.length = length;
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
}