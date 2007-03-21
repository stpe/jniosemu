package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionManager;

/**
 * Used for running an instruction of type J
 */
public abstract class JTypeInstruction extends Instruction
{
	/**
	 * imm part of the instruction
	 */
	protected int imm;

	public JTypeInstruction(int opCode) {
		this.opCode = opCode;

		this.imm = (opCode >>>  6) & 0x3FFFFFF;
	}

	public abstract void run(Emulator em) throws EmulatorException;

	public String toString() {
		InstructionInfo info = InstructionManager.getInfo(this.opCode);

		if (info != null) {
			switch (info.getSyntax()) {
				case DEFAULT:
					return info.getName() +" "+ Integer.toString(this.imm);
			}
		}

		return null;
	}
}
