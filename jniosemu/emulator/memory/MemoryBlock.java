package jniosemu.emulator.memory;

public class MemoryBlock
{
	private final String name;
	private final int start;
	private final int length;

	public MemoryBlock(String name, int start, int length) {
		this.name = name;
		this.start = start;
		this.length = length;
	}

	public String getName() {
		return this.name;
	}

	public int getStart() {
		return this.start;
	}

	public int getEnd() {
		return this.start + this.length - 1;
	}

	public int getLength() {
		return this.length;
	}
}