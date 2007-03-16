package jniosemu.emulator;

import jniosemu.instruction.emulator.Instruction;
import jniosemu.instruction.InstructionManager;

/**
 * Contains info about one line in the program
 */
public class ProgramLine
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
	 * Breakpoint status for the Programline
	 */
	private BREAKPOINT breakpoint = BREAKPOINT.FALSE;
	/**
	 * Number of child lines
	 */
	private int childs;
	/**
	 * Opcode of the Programline.
	 */
	private final int opCode;
	/**
	 * Instruction of the line
	 */
	private final Instruction instruction;
	/**
	 * Sourcecode line
	 */
	private final String sourceCodeLine;
	/**
	 * Which number is this ProgramLine
	 */
	private final int lineNumber;
	/**
	 * Parent of this ProgramLine
	 */
	private final ProgramLine parent;

	/**
	 * Init ProgramLine
	 *
	 * @checks If opcode == 0 then breakpoint = DISABLED
	 */
	public ProgramLine(int opCode, Instruction instruction, String sourceCodeLine, int lineNumber, ProgramLine parent) {
		this.opCode = opCode;
		this.instruction = instruction;
		this.sourceCodeLine = this.replaceTabWithSpaces(sourceCodeLine, 8);
		this.lineNumber = lineNumber;
		this.parent = parent;

		if (this.opCode == 0) {
			this.breakpoint = BREAKPOINT.DISABLED;
			this.childs = 0;
	 	} else {
			this.childs = 1;
		}
	}

	/**
	 * Replace tab with spaces
	 *
	 * @calledby ProgramLine()
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
	 * @calls InstructionManager.intToHexString()
	 *
	 * @return opcode
	 */
	public String getOpCode() {
		if (this.opCode == 0)
			return null;

		return InstructionManager.intToHexString(this.opCode);
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
