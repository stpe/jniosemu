package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpltiInstruction extends ITypeInstruction
{
	public CmpltiInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if (em.readRegister(this.rA) < this.imm) {
			em.writeRegister(this.rB, 1);
		} else {
			em.writeRegister(this.rB, 0);
		}		
	}
}
