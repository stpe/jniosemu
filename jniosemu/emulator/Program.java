package jniosemu.emulator;

/**
 * Contains the compiled program.
 */
public class Program
{
	private final byte[] data;
	private final int startAddr;

	/**
	 * Init Program.
	 *
	 * @calledby Compiler
	 *
	 * @param data Program in the form of binary data
	 * @param startAddr Start position in the memory (what to set pc before starting to emulate)
	 */
	public Program(byte[] data, int startAddr) {
		this.data = data;
		this.startAddr = startAddr;
	}

	/**
	 * Get the executable data
	 *
	 * @calledby EmulatorManager
	 *
	 * @return Program in the form of binary data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * Get start address
	 *
	 * @calledby EmulatorManager
	 *
	 * @return Start address
	 */
	public int getStartAddr() {
		return this.startAddr;
	}
}