package jniosemu.emulator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.compiler.CompilerInstruction;
import jniosemu.instruction.emulator.Instruction;

/**
 * Contains the compiled program.
 */
public class Program
{
	private final byte[] data;
	private final int startAddr;
	// private final String[] lines;
	// private final ArrayList<CompilerInstruction> instructions;
	private ArrayList<ProgramLine> programLines = new ArrayList<ProgramLine>();
	private Hashtable<Integer, Integer> addressLookup = new Hashtable<Integer, Integer>();

	/**
	 * Init Program.
	 *
	 * @calledby Compiler
	 * @todo Doc
	 *
	 * @param data Program in the form of binary data
	 * @param startAddr Start position in the memory (what to set pc before starting to emulate)
	 */
	public Program(InstructionManager instructionManager, String[] lines, ArrayList<CompilerInstruction> instructions, byte[] data, int startAddr) {
		this.startAddr = startAddr;
		this.data = data;

		int lineNumber = 0;
		CompilerInstruction compilerInstruction = null;
		Iterator instructionIterator = instructions.iterator();
		if (instructionIterator.hasNext())
			compilerInstruction = (CompilerInstruction)instructionIterator.next();

		for (String line: lines) {
			if (compilerInstruction != null && compilerInstruction.getLineNumber() == lineNumber) {
				do {
					int opCode = compilerInstruction.getOpcode();
					Instruction instruction = null;
					try {
						instruction = instructionManager.get(opCode);
					} catch (Exception e) {}

					this.programLines.add(new ProgramLine(opCode, instruction, line));

					compilerInstruction = null;
					line = null;
					if (instructionIterator.hasNext())
						compilerInstruction = (CompilerInstruction)instructionIterator.next();
				} while (compilerInstruction != null && compilerInstruction.getLineNumber() == lineNumber);
			} else {
				this.programLines.add(new ProgramLine(0, null, line));
			}

			lineNumber++;
		}

		for (ProgramLine pl: this.programLines)
			System.out.println(pl.toString());
	}

	/**
	 * Return an arraylist of programlines
	 *
	 * @todo Doc
	 *
	 * @return Arraylist of ProgramLine:s
	 */
	public ArrayList<ProgramLine> getProgramLines() {
		return null;
	}

	/**
	 * Return the linenumber of an address
	 *
	 * @todo Doc
	 *
	 * @param address  Memory address
	 * @return Linenumber
	 */
	public int getLineNumber(int address) {
		return 0;
	}

	/**
	 * Get the executable data
	 *
	 * @calledby EmulatorManager
	 *
	 * @return Program in the form of binary data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * Get start address
	 *
	 * @calledby EmulatorManager
	 *
	 * @return Start address
	 */
	public int getStartAddr() {
		return this.startAddr;
	}
}
