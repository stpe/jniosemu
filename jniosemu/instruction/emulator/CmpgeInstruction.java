package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpgeInstruction extends RTypeInstruction
{
	public CmpgeInstruction(int opCode) {
		super(opCode);
	}

	public void run(Emulator em) throws EmulatorException {
                if(em.readRegister(rA) >= em.readRegister(rB))
                        em.writeRegister(rC, 1);
                else
                        em.writeRegister(rC, 0);

	}
}
