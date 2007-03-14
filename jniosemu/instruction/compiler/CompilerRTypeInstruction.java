package jniosemu.instruction.compiler;

import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;

/**
 * Contains info about a instruction of type R during compilation.
 */
public class CompilerRTypeInstruction extends CompilerInstruction
{
	/**
	 * Contains which register rA is
	 */
	private int rA = 0;
	/**
	 * Contains which register rB is
	 */
	private int rB = 0;
	/**
	 * Contains which register rC is
	 */
	private int rC = 0;
	/**
	 * Contains the immediate value which is calculated from tImm during linking
	 */
	private int imm = 0;
	/**
	 * Contains the temporary immediate value which is used during linking
	 */
	private String tImm = "";

	/**
	 * Create a CompilerRTypeInstruction by parsing the arguments
	 *
	 * @post rA, rB, rC and tImm is set
	 *
	 * @param aInstructionInfo  Info about the instruction
	 * @param aArgs  Arguments
	 * @param aLineNumber  LineNumber in the sourcecode
	 * @throws InstructionException  If the argument syntax is wrong
	 */
	public CompilerRTypeInstruction (InstructionInfo aInstructionInfo, String aArgs, int aLineNumber) throws InstructionException {
		this.instructionInfo = aInstructionInfo;
		this.lineNumber = aLineNumber;

		String[] args;

		Pattern pArgs;
		Matcher mArgs;
		switch (this.instructionInfo.getSyntax()) {
			case DEFAULT:
				pArgs = Pattern.compile("([a-z0-9]+)\\s*,\\s*([a-z0-9]+)\\s*,\\s*([a-z0-9]+)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					try {
						this.rC = RegisterManager.parseRegister(mArgs.group(1));
						this.rA = RegisterManager.parseRegister(mArgs.group(2));
						this.rB = RegisterManager.parseRegister(mArgs.group(3));
					} catch (Exception e) {
						throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
					}
				} else {
					throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
				}
				break;
			case CALLJUMP:
				pArgs = Pattern.compile("([a-z0-9]+)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					try {
						this.rA = RegisterManager.parseRegister(mArgs.group(1));
					} catch (Exception e) {
						throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
					}
				} else {
					throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
				}
				break;
			case SHIFT:
				pArgs = Pattern.compile("([a-z0-9]+)\\s*,\\s*([a-z0-9]+)\\s*,\\s*(.*)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					try {
						this.rC = RegisterManager.parseRegister(mArgs.group(1));
						this.rA = RegisterManager.parseRegister(mArgs.group(2));
						this.tImm = mArgs.group(3);
					} catch (Exception e) {
						throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
					}
				} else {
					throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
				}
				break;
			case NONE:
				break;
			default:
				throw new InstructionException();
		}
	}

	/**
	 * Returns the opcode of the instruction.
	 *
	 * @pre link() must be called first so imm gets it value
	 * @calledby Compiler.link()
	 * @calls InstructionInfo.getOpCode()
	 *
	 * @return Opcode
	 */
	public int getOpcode() {
		return (this.rA & 0x1F) << 27 | (this.rB & 0x1F) << 22 | (this.rC & 0x1F) << 17 | (this.imm & 0x1F) << 6 | this.instructionInfo.getOpCode();
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
		this.imm = (int)(Compiler.parseValue(this.tImm, aLabels) & 0xFFFFFFFF);
	}
}
