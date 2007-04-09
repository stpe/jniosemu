package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class XoriInstruction extends ITypeInstruction
{
	public XoriInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                int vA = em.readRegister(this.rA);
		int vI = this.imm & 0xFFFF;
                em.writeRegister(this.rB, vA ^ vI);
		
	}
}
