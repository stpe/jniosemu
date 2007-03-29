package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class BneInstruction extends ITypeInstruction
{
	public BneInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		if(em.readRegister(this.rA) != em.readRegister(this.rB))
			em.writePC(em.readPC() + this.imm);
	}
}
