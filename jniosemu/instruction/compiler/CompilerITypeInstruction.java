package jniosemu.instruction.compiler;

import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;

/**
 * Contains info about a instruction of type I during compilation.
 */
public class CompilerITypeInstruction extends CompilerInstruction
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
	 * Contains the immediate value which is calculated from tImm during linking
	 */
	private int imm = 0;
	/**
	 * Contains the temporary immediate value which is used during linking
	 */
	private String tImm = "";

	/**
	 * Create a CompilerITypeInstruction by parsing the arguments
	 *
	 * @post rA, rB and tImm is set
	 *
	 * @param aInstructionInfo  Info about the instruction
	 * @param aArgs  Arguments
	 * @param aLineNumber  LineNumber in the sourcecode
	 * @throws InstructionException  If the argument syntax is wrong
	 */
	public CompilerITypeInstruction (InstructionInfo aInstructionInfo, String aArgs, int aLineNumber) throws InstructionException {
		this.instructionInfo = aInstructionInfo;
		this.lineNumber = aLineNumber;

		String[] args;

		Pattern pArgs;
		Matcher mArgs;
		switch (this.instructionInfo.getSyntax()) {
			case DEFAULT:
				pArgs = Pattern.compile("([a-z0-9]+)\\s*,\\s*([a-z0-9]+)\\s*,\\s*(.*)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					try {
						this.rB   = RegisterManager.parseRegister(mArgs.group(1));
						this.rA   = RegisterManager.parseRegister(mArgs.group(2));
					} catch (Exception e) {
						throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
					}
					this.tImm = mArgs.group(3);
				} else {
					throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
				}
				break;
			case BRANCH_COND:
				pArgs = Pattern.compile("([a-z0-9]+)\\s*,\\s*([a-z0-9]+)\\s*,\\s*(.*)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					try {
						this.rB   = RegisterManager.parseRegister(mArgs.group(2));
						this.rA   = RegisterManager.parseRegister(mArgs.group(1));
					} catch (Exception e) {
						throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
					}
					this.tImm = mArgs.group(3);
				} else {
					throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
				}
				break;
			case BRANCH:
				this.tImm = aArgs;
				break;
			case MEMORY:
				pArgs = Pattern.compile("([a-z0-9]+)\\s*,\\s*(.*)\\s*\\(([a-z0-9]+)\\)");
				mArgs = pArgs.matcher(aArgs);
				if (mArgs.matches()) {
					try {
						this.rB = RegisterManager.parseRegister(mArgs.group(1));
						this.rA = RegisterManager.parseRegister(mArgs.group(3));
					} catch (Exception e) {
						throw new InstructionException(aInstructionInfo.getName(), "Wrong argument syntax: "+ aArgs);
					}
					this.tImm = mArgs.group(2);
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
	 * @pre link() must be called first so imm gets it value
	 * @calledby Compiler.link()
	 * @calls InstructionInfo.getOpCode()
	 *
	 * @return Opcode
	 */
	public int getOpcode() {
		return (this.rA & 0x1F) << 27 | (this.rB & 0x1F) << 22 | (this.imm & 0xFFFF) << 6 | this.instructionInfo.getOpCode();
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
		long imm;
		try {
			if (this.instructionInfo.getSyntax() == InstructionInfo.Syntax.BRANCH || this.instructionInfo.getSyntax() == InstructionInfo.Syntax.BRANCH_COND) {
				imm = Compiler.parseValue(this.tImm, aLabels, aAddr + 4, 1);
			} else {
				imm = Compiler.parseValue(this.tImm, aLabels, 0, 1);
			}
		} catch (InstructionException e) {
			throw new InstructionException(this.instructionInfo.getName(), "Error parsing immediate value ("+ this.tImm +")");
		}

		this.imm = (int)(imm & 0xFFFF);

		if ((imm >> 16) != 0 && (imm >> 16) != -1)
			throw new InstructionException(this.instructionInfo.getName(), "Immediate value is out of range");
	}
}
