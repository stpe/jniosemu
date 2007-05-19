package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdbuioInstruction extends ITypeInstruction
{
	public LdbuioInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		int vA = em.readRegister(this.rA);
		int vR = signedToUnsigned(em.readByteMemory(vA + this.imm)) & 0xFF;
		
		em.writeRegister(this.rB, vR);
	}
}
