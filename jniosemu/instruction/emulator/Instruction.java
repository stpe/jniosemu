package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public abstract class Instruction
{
	protected long signedToUnsigned(int value) {
		long ret = 0;
		return ret | value;
	}

	protected int signedToUnsigned(short value) {
		int ret = 0;
		return ret | value;
	}

	public abstract void run(Emulator em) throws EmulatorException;
}
