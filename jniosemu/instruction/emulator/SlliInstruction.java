package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SlliInstruction extends RTypeInstruction
{
	public SlliInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                em.writeRegister(rC, em.readRegister(rA) << imm);
		
	}
}
