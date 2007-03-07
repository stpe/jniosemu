package jniosemu.emulator.memory;

public class MemoryException extends RuntimeException
{
	public MemoryException() {
		super();
	}

	public MemoryException(int aAddr) {
		super("Can't read addr: "+ aAddr);
	}
}
