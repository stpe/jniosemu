package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SrliInstruction extends RTypeInstruction
{
	public SrliInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		int vI = this.imm & 63;	// Lowest 6 bits.
		em.writeRegister(this.rC, (int)(signedToUnsigned(em.readRegister(this.rA)) >> signedToUnsigned(vI)));
	}
}
