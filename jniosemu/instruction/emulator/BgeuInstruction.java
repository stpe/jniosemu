package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class BgeuInstruction extends ITypeInstruction
{
	public BgeuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if(signedToUnsigned(em.readRegister(rA)) >= signedToUnsigned(em.readRegister(rB)))
			em.writePC(em.readPC()+imm);		
		
	}
}
