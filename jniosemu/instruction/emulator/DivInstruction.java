package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class DivInstruction extends RTypeInstruction
{
	public DivInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if(em.readRegister(rB) == 0)
			throw new EmulatorException("Can't divide by zero");
		em.writeRegister(rC, em.readRegister(rA)/em.readRegister(rB));
	}
}
