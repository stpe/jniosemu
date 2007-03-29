package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class MulxuuInstruction extends RTypeInstruction
{
	public MulxuuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		long vA = this.signedToUnsigned(em.readRegister(this.rA));
		long vB = this.signedToUnsigned(em.readRegister(this.rB));
		long vC = (vA * vB) >>> 32;
		em.writeRegister(this.rC, (int)vC);
	}
}
