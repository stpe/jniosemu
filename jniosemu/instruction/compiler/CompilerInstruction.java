package jniosemu.instruction.compiler;

import java.util.Hashtable;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;

public abstract class CompilerInstruction
{
	protected InstructionInfo instructionInfo;
	protected int lineNumber;

	/**
	 * Returns the opcode of the instruction
	 *
	 * @ret The opcode
	 */
	public abstract int getOpcode();

	/**
	 * Linking the instruction. Translate labels into memory addresses
	 *
	 * @param aLabels	Labels with there memory address
	 * @param aAddr		The memory address of this instruction
	 */
	public abstract void link(Hashtable<String, Integer> aLabels, int aAddr) throws InstructionException;
}
