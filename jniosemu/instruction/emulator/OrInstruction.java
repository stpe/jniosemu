package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class OrInstruction extends RTypeInstruction
{
	public OrInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(rC, em.readRegister(rA) | em.readRegister(rB));
	}
}
