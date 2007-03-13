package jniosemu.emulator;

import jniosemu.instruction.emulator.Instruction;
import jniosemu.instruction.InstructionManager;

public class ProgramLine
{
	public static enum BREAKPOINT {TRUE, FALSE, DISABLED};
	public static enum SIBLINGSTATUS {FIRST, LAST, ME, MIDDLE, NONE}

	private BREAKPOINT breakpoint = BREAKPOINT.FALSE;
	private int childs = 1;
	private final int opCode;
	private final Instruction instruction;
	private final String sourceCodeLine;
	private final int lineNumber;
	private final ProgramLine parent;

	public ProgramLine(int opCode, Instruction instruction, String sourceCodeLine, int lineNumber, ProgramLine parent) {
		this.opCode = opCode;
		this.instruction = instruction;
		this.sourceCodeLine = sourceCodeLine;
		this.lineNumber = lineNumber;
		this.parent = parent;

		if (this.opCode == 0)
			this.breakpoint = BREAKPOINT.DISABLED;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}

	public int getChildCount() {
		return this.childs;
	}

	public void incrChildCount() {
		this.childs++;
	}

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

	public BREAKPOINT getBreakPoint() {
		return this.breakpoint;
	}

	public String getOpCode() {
		if (this.opCode == 0)
			return null;

		return InstructionManager.intToHexString(this.opCode);
	}

	public String getInstruction() {
		if (this.instruction == null)
			return null;

		return this.instruction.toString();
	}

	public String getSourceCodeLine() {
		if (this.sourceCodeLine == null)
			return null;

		return this.sourceCodeLine;
	}

	public String toString() {
		String ret = "";
		if (this.getSourceCodeLine() != null)
			ret += this.getSourceCodeLine() +"\n";
		if (this.getOpCode() != null)
			ret += this.getOpCode() +" "+ this.getInstruction();
		return ret;
	}
}
