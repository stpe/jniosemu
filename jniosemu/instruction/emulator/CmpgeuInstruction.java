package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpgeuInstruction extends RTypeInstruction
{
	public CmpgeuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                if(signedToUnsigned(em.readRegister(rA)) >= signedToUnsigned(em.readRegister(rB)))
                        em.writeRegister(rC, 1);
                else
                        em.writeRegister(rC, 0);


	}
}
