package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SlliInstruction extends RTypeInstruction
{
	public SlliInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		int vI = (int)signedToUnsigned(this.imm) & 63;
		em.writeRegister(this.rC, em.readRegister(this.rA) << vI);
	}
}
