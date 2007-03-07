package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CallInstruction extends JTypeInstruction
{
	public CallInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		em.writeRegister(31, em.readPC() + 4);
		em.writePC(this.imm * 4 - 4);
	}
}
