package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public abstract class JTypeInstruction extends Instruction
{
	protected int imm;

	public JTypeInstruction(int opCode) {
		this.imm = (opCode >>>  6) & 0x3FFFFFF;
	}

	public abstract void run(Emulator em) throws EmulatorException;
}
