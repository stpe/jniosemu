package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class BltuInstruction extends ITypeInstruction
{
	public BltuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
  		if (signedToUnsigned(em.readRegister(this.rA)) < signedToUnsigned(em.readRegister(this.rB)))
               		em.writePC(em.readPC() + this.imm);
	}
}
