package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class JmpInstruction extends RTypeInstruction
{
	public JmpInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writePC(em.readRegister(this.rA) - 4);
	}
}
