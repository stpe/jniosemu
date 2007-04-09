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
		int vI = this.imm & 0x1F;
		em.writeRegister(this.rC, Integer.rotateLeft(vA, vI));
	}
}
