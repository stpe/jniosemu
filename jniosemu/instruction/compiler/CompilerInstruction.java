package jniosemu.instruction.compiler;

import java.util.Hashtable;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;

/**
 * Contains info about a instruction during compilation.
 */
public abstract class CompilerInstruction
{
	/**
	 * Info about the instruction.
	 */
	protected InstructionInfo instructionInfo;
	/**
	 * Which linenumber in the sourcecode this instruction comes from
	 */
	protected int lineNumber;

	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Returns the opcode of the instruction.
	 *
	 * @calledby Compiler.link()
	 *
	 * @return Opcode
	 */
	public abstract int getOpcode();

	/**
	 * Linking the instruction. Translate labels into memory addresses.
	 *
	 * @calledby Compiler.link()
	 *
	 * @param aLabels  Labels with there memory address
	 * @param aAddr  Memory address where this instruction is placed in memory
	 */
	public abstract void link(Hashtable<String, Integer> aLabels, int aAddr) throws InstructionException;
}
