package jniosemu.emulator.memory;

/**
 * Contains a part of the memory.
 */
public class Memory extends MemoryBlock
{
	/**
	 * Contains the memory data
	 */
	private byte[] memory;

	/**
	 * Contains how the memory looked when init
	 */
	private byte[] originalMemory;

	/**
	 * Init Memory.
	 *
	 * @calledby MemoryManager
	 *
	 * @param name  Name of the part
	 * @param start  External start address
	 * @param length  Length of the memory part
	 */
	public Memory(String name, int start, int length, byte[] memory) {
		this.name = name;
		this.start = start;
		this.length = length;

		this.originalMemory = memory;
		this.reset();
	}

	public byte readByte(int addr) throws MemoryException {
		byte value = 0;
		try {
			value = memory[mapAddr(addr)];
		} catch (Exception e) {
			throw new MemoryException(addr);
		}

		return value;
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		try {
			memory[mapAddr(addr)] = value;
		} catch (Exception e) {
			throw new MemoryException(addr);
		}
	}

	public void reset() {
		this.memory = new byte[this.length];

		if (memory != null)
			System.arraycopy(memory, 0, this.memory, 0, memory.length);
	}

	public boolean resetState() {}
}
