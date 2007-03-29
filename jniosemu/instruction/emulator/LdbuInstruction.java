package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdbuInstruction extends ITypeInstruction
{
	public LdbuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(this.rB, (int)this.signedToUnsigned(em.readByteMemory(em.readRegister(this.rA) + this.imm)));
	}
}
