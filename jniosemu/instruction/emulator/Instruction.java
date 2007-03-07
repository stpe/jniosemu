package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public abstract class Instruction
{
	/**
	 * Help method that the instructions could use to cast an int to a long if the int is unsigned
	 *
	 * @param aValue	An int
	 * @param					A long
	 */
	protected long signedToUnsigned(int aValue) {
		long ret = 0;
		return ret | aValue;
	}

	/**
	 * Help method that the instructions could use to cast a short to an int if the short is unsigned
	 *
	 * @param aValue	A short
	 * @param					An int
	 */
	protected int signedToUnsigned(short aValue) {
		int ret = 0;
		return ret | aValue;
	}

	/**
	 * Execute the instruction
	 *
	 * @param aEm	So the instruction can access memory, register and stuff like that
	 */
	public abstract void run(Emulator aEm) throws EmulatorException;
}
