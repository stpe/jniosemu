package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class RoliInstruction extends RTypeInstruction
{
	public RoliInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		int vA = em.readRegister(this.rA);
		em.writeRegister(this.rC, Integer.rotateLeft(vA, this.imm));
	}
}
