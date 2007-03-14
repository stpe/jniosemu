package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class NextpcInstruction extends RTypeInstruction
{
	public NextpcInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {

	}
}
