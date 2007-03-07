package jniosemu.instruction.compiler;

import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;

public class CompilerJTypeInstruction extends CompilerInstruction
{
	private int imm    = 0;
	private String tImm = "";

	/**
	 * Create a CompilerJTypeInstruction
	 *
	 * @param aInstructionInfo	Info about the instruction
	 * @param aArgs							Arguments
	 * @param aLineNumber				LineNumber
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
	 * Returns the opcode of the instruction
	 *
	 * @ret The opcode
	 */
	public int getOpcode() {
		return (this.imm & 0x3FFFFFF) << 6 | this.instructionInfo.getOpCode();
	}

	/**
	 * Linking the instruction. Translate labels into memory addresses
	 *
	 * @param aLabels	Labels with there memory address
	 * @param aAddr		The memory address of this instruction
	 */
	public void link(Hashtable<String, Integer> aLabels, int aAddr) throws InstructionException {
		long imm = Compiler.parseValue(this.tImm, aLabels) & 0xFFFFFFFF;
		this.imm = (int)(imm / 4);
	}
}

