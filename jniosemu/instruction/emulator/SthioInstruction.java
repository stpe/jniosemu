package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SthioInstruction extends ITypeInstruction
{
	public SthioInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                int vA = em.readRegister(this.rA);
                short vB = (short)em.readRegister(this.rB);
                em.writeShortMemory(vA + this.imm, vB);		
	}
}
