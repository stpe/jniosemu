package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdhioInstruction extends ITypeInstruction
{
	public LdhioInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(this.rB, em.readShortMemory(em.readRegister(this.rA) + this.imm));
	}
}
