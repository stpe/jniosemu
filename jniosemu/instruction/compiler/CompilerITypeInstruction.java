package jniosemu.instruction.compiler;

import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionException;


public class CompilerITypeInstruction extends CompilerInstruction
{
	private int rA     = 0;
	private int rB     = 0;
	private int imm     = 0;
	private String tImm = "";

	/**
	 * Create a CompilerJTypeInstruction
	 *
	 * @param aInstructionInfo	Info about the instruction
	 * @param aArgs							Arguments
	 * @param aLineNumber				LineNumber
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
	 * Returns the opcode of the instruction
	 *
	 * @ret The opcode
	 */
	public int getOpcode() {
		return (this.rA & 0x1F) << 27 | (this.rB & 0x1F) << 22 | (this.imm & 0xFFFF) << 6 | this.instructionInfo.getOpCode();
	}

	/**
	 * Linking the instruction. Translate labels into memory addresses
	 *
	 * @param aLabels	Labels with there memory address
	 * @param aAddr		The memory address of this instruction
	 */
	public void link(Hashtable<String, Integer> aLabels, int aAddr) throws InstructionException {
		long imm = Compiler.parseValue(this.tImm, aLabels) & 0xFFFFFFFF;

		if (this.instructionInfo.getSyntax() == InstructionInfo.Syntax.BRANCH || this.instructionInfo.getSyntax() == InstructionInfo.Syntax.BRANCH_COND)
			this.imm = (int)(imm - aAddr);
		else
			this.imm = (int)imm;
	}
}
