package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

/**
 * Used for running an instruction
 */
public abstract class Instruction
{
	/**
	 * Help method that the instructions could use to cast an int to a long if the int is unsigned
	 *
	 * @param aValue  Int value
	 * @return Long value
	 */
	protected long signedToUnsigned(int aValue) {
		long ret = 0;
		return ret | aValue;
	}

	/**
	 * Help method that the instructions could use to cast a short to an int if the short is unsigned
	 *
	 * @param aValue Short value
	 * @return Int value
	 */
	protected int signedToUnsigned(short aValue) {
		int ret = 0;
		return ret | aValue;
	}

	/**
	 * Execute the instruction
	 *
	 * @calledby EmulatorManager
	 * @calls Emulator.writeRegister(), Emulator.readRegister(), Emulator.writeByte(), Emulator.readByte(), Emulator.writeShort(), Emulator.readShort(), Emulator.writeInt(), Emulator.readInt(), Emulator.writePC(), Emulator.readPC()
	 *
	 * @param aEm	So the instruction can access memory, register and stuff like that
	 * @throws EmulatorException  If something goes wrong trying to execute the instruction
	 */
	public abstract void run(Emulator aEm) throws EmulatorException;
}
