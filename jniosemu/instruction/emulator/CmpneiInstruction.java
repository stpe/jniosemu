package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpneiInstruction extends ITypeInstruction
{
	public CmpneiInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if(em.readRegister(this.rA) != this.imm) {
			em.writeRegister(rB, 1);
		} else {
			em.writeRegister(rB, 0);
		}
	}
}
