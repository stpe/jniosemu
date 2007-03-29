package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpltiInstruction extends ITypeInstruction
{
	public CmpltiInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                if (em.readRegister(rA) < imm) {
                        em.writeRegister(rB, 1);
                } else {
                        em.writeRegister(rB, 0);
                }		
	}
}
