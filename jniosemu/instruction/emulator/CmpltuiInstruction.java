package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpltuiInstruction extends ITypeInstruction
{
	public CmpltuiInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		int vI = this.imm & 0xFFFF;
		if (signedToUnsigned(em.readRegister(this.rA)) < signedToUnsigned(vI)) {
			em.writeRegister(this.rB, 1);
		} else {
			em.writeRegister(this.rB, 0);
		}
	}
}
