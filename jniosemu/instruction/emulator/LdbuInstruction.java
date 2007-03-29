package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdbuInstruction extends ITypeInstruction
{
	public LdbuInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                em.writeRegister(rB, (int)signedToUnsigned(em.readByteMemory(em.readRegister(rA)+imm)));		
	}
}
