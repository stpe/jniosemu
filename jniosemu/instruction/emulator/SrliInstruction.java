package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SrliInstruction extends RTypeInstruction
{
	public SrliInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(this.rC, signedToUnsigned(em.readRegister(this.rA)) >>> signedToUnsigned(this.imm));
	}
}
