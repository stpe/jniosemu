package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpgeiInstruction extends ITypeInstruction
{
	public CmpgeiInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if(em.readRegister(this.rA) >= this.imm) {
			em.writeRegister(this.rB, 1);
		} else {
			em.writeRegister(this.rB, 0);
		}
	}
}
