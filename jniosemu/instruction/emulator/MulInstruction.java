package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class MulInstruction extends RTypeInstruction
{
	public MulInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(rC, (em.readRegister(rA)*em.readRegister(rB)));
	}
}
