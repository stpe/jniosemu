package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class OriInstruction extends ITypeInstruction
{
	public OriInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(rC, em.readRegister(rA) | imm);		
	}
}
