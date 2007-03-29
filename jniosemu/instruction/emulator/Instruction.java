package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

/**
 * Used for running an instruction
 */
public abstract class Instruction
{
	/**
	 * Opcode of the instruction
	 */
	protected int opCode;

	/**
	 * Help method that the instructions could use to cast an int to a long if the int is unsigned
	 *
	 * @param value  Int value
	 * @return Long value
	 */
	protected long signedToUnsigned(int value) {
		// This is a very stupid way to do it but it seams to be the only way Java can do it
		return (long)value & (new Long("4294967295")).longValue();
	}

	/**
	 * Help method that the instructions could use to cast a short to an int if the short is unsigned
	 *
	 * @param value Short value
	 * @return Int value
	 */
	protected int signedToUnsigned(short value) {
		return (int)value & 0xFFFF;
	}

	/**
	 * Execute the instruction
	 *
	 * @calledby EmulatorManager
	 * @calls Emulator.writeRegister(), Emulator.readRegister(), Emulator.writeByte(), Emulator.readByte(), Emulator.writeShort(), Emulator.readShort(), Emulator.writeInt(), Emulator.readInt(), Emulator.writePC(), Emulator.readPC()
	 *
	 * @param em	So the instruction can access memory, register and stuff like that
	 * @throws EmulatorException  If something goes wrong trying to execute the instruction
	 */
	public abstract void run(Emulator em) throws EmulatorException;

	/**
	 * Output the instruction as a String
	 *
	 * @return String representation of the instruction
	 */
	public abstract String toString();
}
