package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public abstract class ITypeInstruction extends Instruction
{
	protected int rA;
	protected int rB;
	protected short imm;

	public ITypeInstruction(int opCode) {
		this.rA  = (opCode >>> 27) & 0x1F;
		this.rB  = (opCode >>> 22) & 0x1F;
		this.imm = (short)((opCode >>>  6) & 0xFFFF);
	}

	public abstract void run(Emulator em) throws EmulatorException;
}
