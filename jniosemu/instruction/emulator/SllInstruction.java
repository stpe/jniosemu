package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SllInstruction extends RTypeInstruction
{
	public SllInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		int vB = em.readRegister(rB);
		vB = vB & 0xF;	// we only want the lower-4-bits.
		em.writeRegister(rC, em.readRegister(rA) << vB);
	}
}
