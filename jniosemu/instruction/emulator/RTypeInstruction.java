package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionManager;

/**
 * Used for running an instruction of type R
 */
public abstract class RTypeInstruction extends Instruction
{
	/**
	 * rA register
	 */
	protected int rA;
	/**
	 * rB register
	 */
	protected int rB;
	/**
	 * rC register
	 */
	protected int rC;
	/**
	 * imm part of the instruction
	 */
	protected int imm;

	public RTypeInstruction(int opCode) {
		this.opCode = opCode;

		this.rA  = (opCode >>> 27) & 0x1F;
		this.rB  = (opCode >>> 22) & 0x1F;
		this.rC  = (opCode >>> 17) & 0x1F;
		this.imm = (opCode >>>  6) & 0x1F;
	}

	public abstract void run(Emulator em) throws EmulatorException;

	public String toString() {
		InstructionInfo info = InstructionManager.getInfo(this.opCode);

		if (info != null) {
			switch (info.getSyntax()) {
				case DEFAULT:
					return info.getName() +" r"+ Integer.toString(this.rC) +", r"+ Integer.toString(this.rA) +", "+ Integer.toString(this.rB);
				case CALLJUMP:
					return info.getName() +" r"+ Integer.toString(this.rA);
				case PC:
					return info.getName() +" r"+ Integer.toString(this.rA);
				case SHIFT:
					return info.getName() +" r"+ Integer.toString(this.rC) +", r"+ Integer.toString(this.rA) +", "+ Integer.toString(this.imm);
				case CUSTOM:
					return info.getName() +" "+ Integer.toString(this.imm) +", r"+ Integer.toString(this.rC) +", r"+ Integer.toString(this.rA) +", r"+ Integer.toString(this.rB);
				case NONE:
					return info.getName();
			}
		}

		return "Invalid RType Instruction!";
	}
}
