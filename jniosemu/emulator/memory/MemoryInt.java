package jniosemu.emulator.memory;

public class MemoryInt
{
	public static enum STATE {UNTOUCHED, READ, WRITE};
	private final int address;
	private final byte[] memoryInt;
	private final MemoryBlock memoryBlock;

	public MemoryInt (int address, byte[] memoryInt, MemoryBlock memoryBlock) {
		this.address = address;
		this.memoryInt = memoryInt;
		this.memoryBlock = memoryBlock;
	}

	public int getAddress() {
		return this.address;
	}

	public byte[] getMemory() {
		return this.memoryInt;
	}

	public STATE getState(int index) {
		return this.memoryBlock.getState(this.address + index);
	}
}
