package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class BgeuInstruction extends ITypeInstruction
{
	public BgeuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if(this.signedToUnsigned(em.readRegister(this.rA)) >= this.signedToUnsigned(em.readRegister(this.rB)))
			em.writePC(em.readPC() + this.imm);
	}
}
