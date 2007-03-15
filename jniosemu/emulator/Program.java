package jniosemu.emulator;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import jniosemu.emulator.memory.MemoryManager;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.compiler.CompilerInstruction;
import jniosemu.instruction.emulator.Instruction;

/**
 * Contains the compiled program.
 */
public class Program
{
	/**
	 * Binary representation of the program
	 */
	private final byte[] program;
	/**
	 * Binary representation of the variables
	 */
	private final byte[] variables;
	/**
	 * Start address
	 */
	private final int startAddr;
	/**
	 * Contains all ProgramLine:s
	 */
	private Vector<ProgramLine> programLines = new Vector<ProgramLine>();
	/**
	 * Used for fast get linenumber from address
	 */
	private Hashtable<Integer, Integer> addrLookup = new Hashtable<Integer, Integer>();
	/**
	 * Used for fast get address from linenumber
	 */
	private Hashtable<Integer, Integer> lineNumberLookup = new Hashtable<Integer, Integer>();

	/**
	 * Init Program.
	 *
	 * @calledby Compiler
	 *
	 * @param lines  Sourcecode lines
	 * @param instructions  Program in the form of binary data
	 * @param data  The binary program
	 * @param startAddr  Start position in the memory (what to set pc before starting to emulate)
	 */
	public Program(String[] lines, ArrayList<CompilerInstruction> instructions, byte[] program, byte[] variables, int startAddr) {
		this.startAddr = startAddr;
		this.program = program;
		this.variables = variables;

		int sourceCodeLineNumber = 1;
		int addr = MemoryManager.PROGRAMSTARTADDR;
		int programLineCount = 0;
		ProgramLine programLineParent = null;
		ProgramLine programLine = null;

		CompilerInstruction compilerInstruction = null;
		Iterator instructionIterator = instructions.iterator();
		if (instructionIterator.hasNext())
			compilerInstruction = (CompilerInstruction)instructionIterator.next();

		for (String line: lines) {
			if (compilerInstruction != null && compilerInstruction.getLineNumber() == sourceCodeLineNumber) {
				do {
					int opCode = compilerInstruction.getOpcode();
					Instruction instruction = null;
					try {
						instruction = InstructionManager.get(opCode);
					} catch (Exception e) {}

					programLine = new ProgramLine(opCode, instruction, line, programLineCount, programLineParent);
					this.programLines.add(programLine);

					if (programLineParent == null)
						programLineParent = programLine;
					else
						programLineParent.incrChildCount();

					this.addrLookup.put(addr, programLineCount);
					this.lineNumberLookup.put(programLineCount, addr);

					addr += 4;
					programLineCount++;
					compilerInstruction = null;
					line = null;
					if (instructionIterator.hasNext())
						compilerInstruction = (CompilerInstruction)instructionIterator.next();
				} while (compilerInstruction != null && compilerInstruction.getLineNumber() == sourceCodeLineNumber);
			} else {
				this.programLines.add(new ProgramLine(0, null, line, programLineCount, null));
				programLineCount++;
			}

			programLineParent = null;
			sourceCodeLineNumber++;
		}
	}

	/**
	 * Return an arraylist of programlines
	 *
	 * @return Arraylist of ProgramLine:s
	 */
	public Vector<ProgramLine> getProgramLines() {
		return this.programLines;
	}

	/**
	 * Return the linenumber of an address
	 *
	 * @param address  Memory address
	 * @return Linenumber
	 */
	public int getLineNumber(int address) {
		if (this.addrLookup.containsKey(address))
			return this.addrLookup.get(address);

		return -1;
	}

	/**
	 * Return the memory address where a line is placed
	 *
	 * @calledby EmulatorManager
	 *
	 * @param lineNumber  line number
	 * @return memory address
	 */
	public int getAddress(int lineNumber) {
		if (this.lineNumberLookup.containsKey(lineNumber))
			return this.lineNumberLookup.get(lineNumber);

		return -1;
	}

	/**
	 * Get the executable data
	 *
	 * @calledby EmulatorManager.load()
	 *
	 * @return Program in the form of binary data
	 */
	public byte[] getBinaryProgram() {
		return this.program;
	}

	/**
	 * Get the variable memory
	 *
	 * @calledby EmulatorManager.load()
	 *
	 * @return Variables in the form of binary data
	 */
	public byte[] getBinaryVariables() {
		return this.variables;
	}

	/**
	 * Get start address
	 *
	 * @calledby EmulatorManager.load()
	 *
	 * @return Start address
	 */
	public int getStartAddr() {
		return this.startAddr;
	}

	/**
	 * Toggle breakpoint
	 *
	 * @checks Just don't do anything if the line don't exists
	 * @calledby EmulatorManager.toggleBreakpoint()
	 * @calls ProgramLine.toggleBreakpoint()
	 *
	 * @param lineNumber  lineNumber to toggle breakpoint
	 * @return  true if the breakpoint is set
	 */
	public boolean toggleBreakpoint(int lineNumber) {
		try {
			return this.programLines.get(lineNumber).toggleBreakpoint();
		} catch (Exception e) {}

		return false;
	}
}
