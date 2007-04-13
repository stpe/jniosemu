package jniosemu.emulator.memory;

public class MemoryInt
{
	public static enum STATE {UNTOUCHED, READ, WRITE};
	private final int address;
	private final byte[] memoryInt;
	private final STATE[] states;

	public MemoryInt (int address, byte[] memoryInt, STATE[] states) {
		this.address = address;
		this.memoryInt = memoryInt;
		this.states = states;
	}

	public int getAddress() {
		return this.address;
	}

	public byte[] getMemory() {
		return this.memoryInt;
	}

	public STATE getState(int index) {
		return states[index];
	}
}
