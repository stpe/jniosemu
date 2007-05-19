package jniosemu.emulator;

import java.util.Vector;
import java.util.ArrayList;

import jniosemu.emulator.memory.MemoryManager;
import jniosemu.emulator.compiler.Variable;
import jniosemu.instruction.InstructionException;
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
	private final byte[] variabledata;

	private Vector<Variable> variables = new Vector<Variable>();

	/**
	 * Start address
	 */
	private final int startAddr;

	private final SourceCode sourceCode;

	/**
	 * Init Program.
	 *
	 * @calledby Compiler
	 *
	 * @param lines  Sourcecode lines
	 * @param instructions  Program in the form of binary data
	 * @param variables  Vector containing all variables
	 * @param program  Binary program
	 * @param variabledata  Variables as binary
	 * @param startAddr  Start position in the memory (what to set pc before starting to emulate)
	 */
	public Program(String[] lines, ArrayList<CompilerInstruction> instructions, Vector<Variable> variables, byte[] program, byte[] variabledata, int startAddr) throws InstructionException {
		this.startAddr = startAddr;
		this.program = program;
		this.variables = variables;
		this.variabledata = variabledata;

		this.sourceCode = new SourceCode(lines, instructions, MemoryManager.PROGRAMSTARTADDR);
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
		return this.variabledata;
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

	public Vector<Variable> getVariables() {
		return this.variables;
	}

	public SourceCode getSourceCode() {
		return this.sourceCode;
	}
}
