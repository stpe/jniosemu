package jniosemu.emulator.compiler;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import jniosemu.emulator.Program;
import jniosemu.emulator.compiler.macro.MacroManager;
import jniosemu.emulator.compiler.macro.MacroException;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.InstructionException;
import jniosemu.instruction.compiler.*;


/**
 * Takes sourcecode and turn it into a Program.
 */
public class Compiler
{
	/**
	 * Lines of the sourcecode.
	 */
	private String[] lines;
	/**
	 * Contains all Label.
	 */
	private Hashtable<String, Integer> labels = new Hashtable<String, Integer>();
	/**
	 * Contains all CompilerInstruction.
	 */
	private ArrayList<CompilerInstruction> instructions = new ArrayList<CompilerInstruction>();
	/**
	 * Contains all Constant.
	 */
	private ArrayList<Constant> constants = new ArrayList<Constant>();
	/**
	 * Contains all Variable.
	 */
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	/**
	 * For managing macros.
	 */
	private MacroManager macros = new MacroManager();
	/**
	 * For managing instructions.
	 */
	private InstructionManager instructionManager = new InstructionManager();
	/**
	 * True if we are in the codepart of the program.
	 */
	private boolean codePart = true;
	/**
	 * Contains the name of all global Label.
	 */
	private ArrayList<String> globals = new ArrayList<String>();
	/**
	 * Contains the name of the last label if we not in codepart.
	 */
	private String lastLabel = null;

	/**
	 * Init Compiler.
	 *
	 * @post lines is set
	 * @calledby EmulatorManager
	 *
	 * @param aLines  Sourcecode
	 */
	public Compiler(String aLines) {
		this.lines = aLines.split("\n");
	}

	/**
	 * Replaces all instanses of aReplace with aNew in aValue.
	 *
	 * @calledby parseValue(), parseLine()
	 *
	 * @param aValue The string where the replacment will happen
	 * @param aReplace The old value
	 * @param aNew The new value
	 * @return The new string
	 */
	public static String stringReplace(String aValue, String aReplace, String aNew) {
		int start = aValue.indexOf(aReplace);
		while (start >= 0) {
			StringBuffer replace = new StringBuffer(aValue);
			replace.replace(start, start+aReplace.length(), aNew);
			aValue = replace.toString();
			start = aValue.indexOf(aReplace, start);
		}
		return aValue;
	}

	/**
	 * Translate a value into a number. Handle +, -, *, / and many other. Also handle labels.
	 *
	 * @calledby Instruction.link()
	 * @calls stringReplace(), parseValue()
	 *
	 * @param aValue A string containing numbers, labels and other stuff
	 * @param aLabels Contains all labels with there respective memory address
	 * @return The value
	 * @throws InstructionException  If we can't parse the value
	 */
	public static long parseValue(String aValue, Hashtable<String, Integer> aLabels) throws InstructionException {
		if (aValue.length() == 0)
			return 0;

		// Remove all whitespaces
		aValue = aValue.replaceAll("\\s", "");

		// Translate all labels into memory addresses
		if (aLabels != null) {
			Pattern pLabels = Pattern.compile("[A-Za-z]+");
			Matcher mLabels = pLabels.matcher(aValue);
			while (mLabels.find()) {
				String match = mLabels.group(0);
				if (aLabels.containsKey(match)) {
					long newValue = (long)aLabels.get(match);
					aValue = stringReplace(aValue, match, Long.toString(newValue));
				}
			}
		}

		// Find all parentheses and macron 
		boolean found;
		do {
			found = false;
			Pattern pParenthesis = Pattern.compile("(%(lo|hi|hiadj|\\-))?\\(([^\\(\\)]+)\\)");
			Matcher mParenthesis = pParenthesis.matcher(aValue);
			while (mParenthesis.find()) {
				found = true;
				long inner = parseValue(mParenthesis.group(3), aLabels);

				// If a macro is found take care of it
				if (mParenthesis.group(2) != null) {
					if (mParenthesis.group(2).equals("-")) {
						inner = -inner;
					} else if (mParenthesis.group(2).equals("lo")) {
						inner = inner & 0xFFFF;
					} else if (mParenthesis.group(2).equals("hi")) {
						inner = (inner >>> 16) & 0xFFFF;
					} else if (mParenthesis.group(2).equals("hiadj")) {
						inner = ((inner >>> 16) & 0xFFFF) + ((inner >>> 15) & 0x1);
					}
				}

				// Replace the parentheses with the value
				aValue = stringReplace(aValue, mParenthesis.group(0), Long.toString(inner));
			}
		} while (found);

		// Translate all binary and hexdecimal values into decimal values
		Pattern pHexBin = Pattern.compile("(-)?0((x)([0-9A-Fa-f]+)|(b)([0-1]+))");
		Matcher mHexBin = pHexBin.matcher(aValue);
		while (mHexBin.find()) {
			String newValue = (mHexBin.group(1) != null) ? "-" : "";

			if (mHexBin.group(3) != null) {
				newValue += Long.toString(Long.parseLong(mHexBin.group(4), 16));
			} else {
				newValue += Long.toString(Long.parseLong(mHexBin.group(6), 2));
			}
			aValue = stringReplace(aValue, mHexBin.group(0), newValue);
		}

		// Take care of the "not"-operation
		Pattern pNotOperator = Pattern.compile("~(-?[\\d]+)");
		Matcher mNotOperator = pNotOperator.matcher(aValue);
		while (mNotOperator.find()) {
			long newValue = ~Long.parseLong(mNotOperator.group(1));
			aValue = stringReplace(aValue, mNotOperator.group(0), Long.toString(newValue));
		}

		// Take care of all the other operations
		String[] operations = {"<<|>>>|>>", "&", "\\||\\^", "\\*|/|%", "\\+|\\-"};
		for (String operation: operations) {
			do {
				found = false;
				Pattern pCalculate = Pattern.compile("(-?[\\d]+)("+ operation +")(-?[\\d]+)");
				Matcher mCalculate = pCalculate.matcher(aValue);
				if (mCalculate.find()) {
					found = true;
					long newValue;
					long valueA = Long.parseLong(mCalculate.group(1));
					long valueB = Long.parseLong(mCalculate.group(3));
					if (mCalculate.group(2).equals("<<")) {
						newValue = valueA << valueB;
					} else if (mCalculate.group(2).equals(">>>")) {
						newValue = valueA >>> valueB;
					} else if (mCalculate.group(2).equals(">>")) {
						newValue = valueA >> valueB;
					} else if (mCalculate.group(2).equals("&")) {
						newValue = valueA & valueB;
					} else if (mCalculate.group(2).equals("|")) {
						newValue = valueA | valueB;
					} else if (mCalculate.group(2).equals("^")) {
						newValue = valueA ^ valueB;
					} else if (mCalculate.group(2).equals("*")) {
						newValue = valueA * valueB;
					} else if (mCalculate.group(2).equals("/")) {
						newValue = valueA / valueB;
					} else if (mCalculate.group(2).equals("%")) {
						newValue = valueA % valueB;
					} else if (mCalculate.group(2).equals("+")) {
						newValue = valueA + valueB;
					} else if (mCalculate.group(2).equals("-")) {
						newValue = valueA - valueB;
					} else {
						throw new InstructionException();
					}
					aValue = stringReplace(aValue, mCalculate.group(0), Long.toString(newValue));
				}
			} while (found);
		}

		// Now it should just be a numeric value
		try {
			return Long.parseLong(aValue);
		} catch (Exception e) {
			throw new InstructionException();
		}
	}

	/**
	 * Returns the current memory address.
	 *
	 * @calledby parseLine()
	 *
	 * @return Current memory address
	 */
	private int getCurrentAddr() {
		return MemoryManager.PROGRAMSTART + (this.instructions.size()*4);
	}

	/**
	 * Parses a sourcecode line 
	 *
	 * @post If instruction is found they are added to instructions
	 * @calledby compile()
	 *
	 * @param aLine		A sourcecode line
	 * @param aFirst	True if this is the first time this sourcecode line is parsed.
	 * @throws CompilerException  If something goes wrong
	 */
	private void parseLine(String aLine, boolean aFirst, int aLineNumber) throws CompilerException {
		// If it is the first pass replace constants with there value
		if (aFirst) {
			for(Constant constant: this.constants) {
				aLine = stringReplace(aLine, constant.getName(), constant.getValue());
			}
		}

		// Find the important part of the line. That will say remove commants and whitespace in the beginning and end
		Pattern pRemoveWhitespace = Pattern.compile("\\s*([^#]*?)\\s*(#.*)*");
		Matcher mRemoveWhitespace = pRemoveWhitespace.matcher(aLine);
		if (mRemoveWhitespace.matches()) {
			String match = mRemoveWhitespace.group(1);
			if (match.length() > 0) {
				
				// Find labels
				Pattern pLabel = Pattern.compile("([A-Za-z]+):\\s*(.*?)");
				Matcher mLabel = pLabel.matcher(match);
				if (mLabel.matches()) {
					// Depending if we are in the codepart or not we handle labels different
					if (this.codePart) {
						this.labels.put(mLabel.group(1), this.getCurrentAddr());
					} else {
						this.lastLabel = mLabel.group(1);
					}

					// Now parse the remaing part. Now when the label is removed
					this.parseLine(mLabel.group(2), false, aLineNumber);
				} else {
					Pattern pInstruction = Pattern.compile("(\\.)?([A-Za-z]+)\\s*(.*?)");
					Matcher mInstruction = pInstruction.matcher(match);
					if (mInstruction.matches()) {

						// Check if the first character is a ".".
						if (mInstruction.group(1) != null) {
							String name = mInstruction.group(2);

							// Handle all parts with a "."-character in the begining
							if (name.equals("equ")) {
								this.constants.add(new Constant(mInstruction.group(3)));
							} else if (name.equals("data")) {
								this.codePart = false;
							} else if (name.equals("text")) {
								this.codePart = true;
							} else if (name.equals("global")) {
								this.globals.add(mInstruction.group(3));
							} else if (name.equals("word")) {
								try {
									this.variables.add(new Variable(lastLabel, Variable.Type.WORD, mInstruction.group(3)));
									this.lastLabel = null;
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else if (name.equals("byte")) {
								try {
									this.variables.add(new Variable(lastLabel, Variable.Type.BYTE, mInstruction.group(3)));
									this.lastLabel = null;
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else if (name.equals("ascii")) {
								try {
									this.variables.add(new Variable(lastLabel, Variable.Type.ASCII, mInstruction.group(3)));
									this.lastLabel = null;
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else if (name.equals("asciz") || name.equals("string")) {
								try {
									this.variables.add(new Variable(lastLabel, Variable.Type.ASCIZ, mInstruction.group(3)));
									this.lastLabel = null;
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else {
								throw new CompilerException(aLineNumber, "Unknown part: "+ name);
							}
						} else {
							// Check if the instruction is a macro
							String ins = mInstruction.group(2);
							if (this.macros.exists(ins)) {
								try {
									// Parse the macro
									String[] lines = this.macros.get(ins, mInstruction.group(3));
									for (String line: lines)
										this.parseLine(line, true, aLineNumber);
								} catch (MacroException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else {
								// get the CompilerInstruction for the instruction
								try {
									CompilerInstruction cins = this.instructionManager.get(ins, mInstruction.group(3), aLineNumber);
									this.instructions.add(cins);
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Compile the program.
	 *
	 * @calledby EmulatorManager.compile()
	 * @calls parseLine()
	 *
	 * @throws CompilerException  If parseLine casts CompilerException
	 */
	public void compile() throws CompilerException {
		int i = 0;
		for (String line: this.lines) {
			this.parseLine(line, true, i++);
		}
	}

	/**
	 * Link the program and returns it.
	 *
	 * @pre compile() must have run so instructions contains CompilerInstruction
	 * @calledby EmulatorManager.compile()
	 *
	 * @return Program
	 * @throws CompilerException  If an instruction can't link
	 */
	public byte[] link() throws CompilerException {
		int size = this.instructions.size()*4+8;
		for (Variable variable: this.variables)
			size += variable.getValue().length;

		byte[] program = new byte[size];

		// Add variables in the memory
		int addr = this.instructions.size()*4+4;
		for (Variable variable: this.variables) {
			if (variable.getName() != null) {
				this.labels.put(variable.getName(), MemoryManager.PROGRAMSTART + addr);
			}

			byte[] value = variable.getValue();
			for (int i = 0; i < value.length; i++)
				program[addr+i] = value[i];

			addr += value.length;
		}

		// Add program to the memory
		addr = 0;
		for (CompilerInstruction instruction: this.instructions) {
			try {
				instruction.link(this.labels, MemoryManager.PROGRAMSTART + addr);
			} catch (InstructionException e) {
				throw new CompilerException(e.getMessage());
			}

			int opcode = instruction.getOpcode();
			program[addr]     = (byte)(opcode        & 0xFF);
			program[addr + 1] = (byte)(opcode >>> 8  & 0xFF);
			program[addr + 2] = (byte)(opcode >>> 16 & 0xFF);
			program[addr + 3] = (byte)(opcode >>> 24 & 0xFF);

			addr += 4;
		}

		new Program(this.instructionManager, this.lines, this.instructions, program, 0);

		return program;
	}

	/**
	 * Returns the memory address of the label if the label exists and the label is global
	 *
	 * @param aName  Name of the label
	 * @return Returns the memory address
	 */
	public int getGlobal(String aName) throws CompilerException {
		if (this.globals.contains(aName) && this.labels.containsKey(aName)) {
			return this.labels.get(aName);
		}

		throw new CompilerException();
	}
}
