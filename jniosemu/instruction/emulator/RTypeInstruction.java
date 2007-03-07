package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public abstract class RTypeInstruction extends Instruction
{
	protected int rA;
	protected int rB;
	protected int rC;
	protected int imm;

	public RTypeInstruction(int opCode) {
		this.rA  = (opCode >>> 27) & 0x1F;
		this.rB  = (opCode >>> 22) & 0x1F;
		this.rC  = (opCode >>> 17) & 0x1F;
		this.imm = (opCode >>>  6) & 0x1F;
	}

	public abstract void run(Emulator em) throws EmulatorException;
}
