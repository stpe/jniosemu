package jniosemu.instruction.compiler;

import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;

/**
 * Contains info about a instruction of type J during compilation.
 */
public class CompilerJTypeInstruction extends CompilerInstruction
{
	/**
	 * Contains the immediate value which is calculated from tImm during linking
	 */
	private int imm = 0;
	/**
	 * Contains the temporary immediate value which is used during linking
	 */
	private String tImm = "";

	/**
	 * Create a CompilerJTypeInstruction by parsing the arguments
	 *
	 * @post tImm is set
	 *
	 * @param aInstructionInfo  Info about the instruction
	 * @param aArgs  Arguments
	 * @param aLineNumber  LineNumber in the sourcecode
	 * @throws InstructionException  If the argument syntax is wrong
	 */
	public CompilerJTypeInstruction (InstructionInfo aInstructionInfo, String aArgs, int aLineNumber) throws InstructionException {
		this.instructionInfo = aInstructionInfo;
		this.lineNumber = aLineNumber;

		String[] args;

		Pattern pArgs;
		Matcher mArgs;
		switch (this.instructionInfo.getSyntax()) {
			case DEFAULT:
				pArgs = Pattern.compile("([a-z0-9]+)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					this.tImm = mArgs.group(1);
				} else {
					throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
				}
				break;
			default:
				throw new InstructionException();
		}
	}

	/**
	 * Returns the opcode of the instruction.
	 *
	 * @pre imm must get its value which it get during linking
	 * @calledby Compiler.link()
	 * @calls InstructionInfo.getOpCode()
	 *
	 * @return Opcode
	 */
	public int getOpcode() {
		return (this.imm & 0x3FFFFFF) << 6 | this.instructionInfo.getOpCode();
	}

	/**
	 * Linking the instruction. Translate labels into memory addresses.
	 *
	 * @post Set imm
	 * @calledby Compiler.link()
	 * @calls Compiler.parseValue()
	 *
	 * @param aLabels  Labels with there memory address
	 * @param aAddr  Memory address where this instruction is placed in memory
	 * @throws InstructionException  If the immediate value can't be parsed
	 */
	public void link(Hashtable<String, Integer> aLabels, int aAddr) throws InstructionException {
		try {
			this.imm = (int)((Compiler.parseValue(this.tImm, aLabels) & 0xFFFFFFFF) / 4);
		} catch (InstructionException e) {
			throw new InstructionException(this.instructionInfo.getName(), "Error parsing immediate value ("+ this.tImm +")");
		}
	}
}

