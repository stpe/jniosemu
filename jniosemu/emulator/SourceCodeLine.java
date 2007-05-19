package jniosemu.emulator;

import jniosemu.instruction.InstructionException;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.compiler.CompilerInstruction;
import jniosemu.instruction.emulator.Instruction;
import jniosemu.Utilities;

/**
 * Contains info about one line in the program
 */
public class SourceCodeLine
{
	/**
	 * Status of a breakpoint
	 */
	public static enum BREAKPOINT {TRUE, FALSE, DISABLED};
	/**
	 * Sibling status. Is used for GUI.
	 */
	public static enum SIBLINGSTATUS {FIRST, LAST, ME, MIDDLE, NONE}

	/**
	 * Breakpoint status for the SourceCodeLine
	 */
	private BREAKPOINT breakpoint = BREAKPOINT.FALSE;
	/**
	 * Number of child lines
	 */
	private int childs = 1;
	/**
	 * Opcode of the SourceCodeLine.
	 */
	private int opCode = 0;
	/**
	 * Instruction of the line
	 */
	private Instruction instruction = null;
	/**
	 * Sourcecode line
	 */
	private String sourceCodeLine = null;
	/**
	 * Which number is this SourceCodeLine
	 */
	private int lineNumber;
	/**
	 * Parent of this SourceCodeLine
	 */
	private SourceCodeLine parent = null;

	public SourceCodeLine(int opCode, int lineNumber, SourceCodeLine parent) throws InstructionException {
		this.opCode = opCode;
		this.lineNumber = lineNumber;
		this.parent = parent;

		this.instruction = InstructionManager.get(opCode);

		if (this.opCode == 0) {
			this.breakpoint = BREAKPOINT.DISABLED;
			this.childs = 0;
		}
	}

	/**
	 * Init SourceCodeLine
	 *
	 * @checks If opcode == 0 then breakpoint = DISABLED
	 * @calledby Program()
	 * @calls replaceTabWithSpaces()
	 *
	 * @param opCode  Opcode of the instruction
	 * @param instruction  Info about the instruction
	 * @param sourceCodeLine  Sourcecode line
	 * @param lineNumber  Program line number
	 * @param parent  Parent SourceCodeLine
	 */
	public SourceCodeLine(CompilerInstruction compilerInstruction, String sourceCodeLine, int lineNumber, SourceCodeLine parent) throws InstructionException {
		this.sourceCodeLine = this.replaceTabWithSpaces(sourceCodeLine, 8);
		this.lineNumber = lineNumber;
		this.parent = parent;

		if (compilerInstruction != null) {
			this.opCode = compilerInstruction.getOpcode();
			this.instruction = InstructionManager.get(this.opCode);
		}

		if (this.opCode == 0) {
			this.breakpoint = BREAKPOINT.DISABLED;
			this.childs = 0;
	 	}
	}

	/**
	 * Replace tab with spaces
	 *
	 * @calledby SourceCodeLine()
	 *
	 * @param str Original string
	 * @param spacesPerTab spaces per tab
	 * @return New string
	 */
	public static String replaceTabWithSpaces(String str, int spacesPerTab) {
		if (str == null)
			return null;

		String ret = "";
		int addedChars = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\t') {
				int spaces = spacesPerTab - ((i + addedChars) % spacesPerTab);
				for (int j = 0; j < spaces; j++)
					ret = ret.concat(" ");
				addedChars += spaces - 1;
			} else {
				ret = ret.concat(str.substring(i, i+1));
			}
		}
		return ret;
	}

	/**
	 * Get line number
	 *
	 * @calledby Program()
	 *
	 * @return line number
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Get child count
	 *
	 * @calledby Program()
	 *
	 * @return child count
	 */
	public int getChildCount() {
		return this.childs;
	}

	/**
	 * Increase child count by one
	 *
	 * @post intrease child count by one
	 * @calledby Program()
	 */
	public void incrChildCount() {
		this.childs++;
	}

	/**
	 * Return sibling status. 
	 *
	 * @calledby GUIEmulator.EmulatorCellRenderer.paintComponent()
	 *
	 * @param lineNumber The line that is marked in Emulator
	 * @return Sibling status
	 */
	public SIBLINGSTATUS isSibling(int lineNumber) {
		if (this.lineNumber == lineNumber)
			return SIBLINGSTATUS.ME;

		if (this.parent == null) {
			if (this.lineNumber <= lineNumber && this.lineNumber + this.childs - 1 >= lineNumber)
				return SIBLINGSTATUS.FIRST;

			return SIBLINGSTATUS.NONE;
		}

		int parentStart = this.parent.getLineNumber();
		int parentEnd = parentStart + this.parent.getChildCount() - 1;
		if (parentStart <= lineNumber && parentEnd >= lineNumber) {
			if (parentStart == this.lineNumber)
				return SIBLINGSTATUS.FIRST;
			if (parentEnd == this.lineNumber)
				return SIBLINGSTATUS.LAST;
			return SIBLINGSTATUS.MIDDLE;
		}

		return SIBLINGSTATUS.NONE;
	}

	/**
	 * Get breakpoint status
	 *
	 * @calledby GUIEmulator.EmulatorCellRenderer.paintComponent()
	 *
	 * @return breakpoint status
	 */
	public BREAKPOINT getBreakPoint() {
		return this.breakpoint;
	}

	/**
	 * Get opcode
	 *
	 * @calledby GUIEmulator.EmulatorCellRenderer.paintComponent()
	 * @calls Utilities.intToHexString()
	 *
	 * @return opcode
	 */
	public String getOpCode() {
		if (this.opCode == 0)
			return null;

		return Utilities.intToHexString(this.opCode);
	}

	/**
	 * Get instruction string
	 *
	 * @calledby GUIEmulator.EmulatorCellRenderer.paintComponent()
	 * @calls Instruction.toString()
	 *
	 * @return instruction
	 */
	public String getInstruction() {
		if (this.instruction == null)
			return null;

		return this.instruction.toString();
	}

	/**
	 * Get sourcecode line
	 *
	 * @calledby GUIEmulator.EmulatorCellRenderer.paintComponent()
	 *
	 * @return sourcecode line
	 */
	public String getSourceCodeLine() {
		if (this.sourceCodeLine == null)
			return null;

		return this.sourceCodeLine;
	}

	/**
	 * Get a debug string
	 *
	 * @return debug string
	 */
	public String toString() {
		String ret = "";
		if (this.getSourceCodeLine() != null)
			ret += this.getSourceCodeLine() +"\n";
		if (this.getOpCode() != null)
			ret += this.getOpCode() +" "+ this.getInstruction();
		return ret;
	}

	/**
	 * Toggle breakpoint. If DISABLED nothing happens.
	 * Else TRUE -> FALSE and FALSE -> TRUE.
	 *
	 * @calledby Program.toggleBreakpoint()
	 *
	 * @return  True if the breakpoint is set
	 */
	public boolean toggleBreakpoint() {
		switch (this.breakpoint) {
			case TRUE:
				this.breakpoint = BREAKPOINT.FALSE;
				break;
			case FALSE:
				this.breakpoint = BREAKPOINT.TRUE;
				return true;
		}

		return false;
	}
}
