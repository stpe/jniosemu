package jniosemu.emulator.memory;

public class MemoryInt {
	private final int address;
	private final byte[] memoryInt;

	public MemoryInt (int address, byte[] memoryInt) {
		this.address = address;
		this.memoryInt = memoryInt;
	}

	public int getAddress() {
		return this.address;
	}

	public byte[] getMemory() {
		return this.memoryInt;
	}
}
