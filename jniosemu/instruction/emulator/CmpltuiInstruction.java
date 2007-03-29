package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpltuiInstruction extends ITypeInstruction
{
	public CmpltuiInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                if (signedToUnsigned(em.readRegister(rA)) < signedToUnsigned(imm)) {
                        em.writeRegister(rB, 1);
                } else {
                        em.writeRegister(rB, 0);
                }		
	}
}
