package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class MulxssInstruction extends RTypeInstruction
{
	public MulxssInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
		long vA = (long)em.readRegister(rA);
		long vB = (long)em.readRegister(rB);
		long vC = vA*vB;
		System.out.println("vA*vB: "+ Long.toString(vC));
		vC = vC >>> 32;
		System.out.println("vC: "+ Long.toString(vC));
		System.out.println("vC: "+ Integer.toString((int)vC));
		em.writeRegister(rC, (int)vC);
	}
}
