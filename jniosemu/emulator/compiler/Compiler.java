package jniosemu.emulator.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import jniosemu.Utilities;
import jniosemu.editor.Editor;
import jniosemu.emulator.Program;
import jniosemu.emulator.compiler.macro.Macro;
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
	private Vector<Variable> variables = new Vector<Variable>();
	/**
	 * For managing macros.
	 */
	private MacroManager macros = new MacroManager();
	/**
	 * True if we are in the codepart of the program.
	 */
	private boolean codePart = true;
	/**
	 * Contains the macro if we are in one.
	 */
	private Macro lastMacro = null;
	/**
	 * Contains the name of all global Label.
	 */
	private ArrayList<String> globals = new ArrayList<String>();
	/**
	 * Contains the name of the last label if we not in codepart.
	 */
	private String lastLabel = null;

	private String currentDir = null;

	/**
	 * Init Compiler.
	 *
	 * @post lines is set
	 * @calledby EmulatorManager
	 *
	 * @param aLines  Sourcecode
	 */
	public Compiler(String aLines, String currentDir) {
		this.lines = removeMultilineComments(aLines).split("\r\n|\n|\r");
		this.currentDir = currentDir;

		// Add libfunctions
		this.labels.put("nr_uart_rxchar", MemoryManager.LIBSTARTADDR);
		this.labels.put("nr_uart_txchar", MemoryManager.LIBSTARTADDR + 100);
		this.labels.put("nr_uart_txcr", MemoryManager.LIBSTARTADDR + 196);
		this.labels.put("nr_uart_txhex16", MemoryManager.LIBSTARTADDR + 252);
		this.labels.put("nr_uart_txhex32", MemoryManager.LIBSTARTADDR + 400);
		this.labels.put("nr_uart_txhex", MemoryManager.LIBSTARTADDR + 496);
		this.labels.put("nr_uart_txstring", MemoryManager.LIBSTARTADDR + 540);
	}

	public static long parseValue(String aValue) throws InstructionException {
		return parseValue(aValue, null, 0, 1);
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
	public static long parseValue(String aValue, Hashtable<String, Integer> aLabels, int aAddr, int aDivider) throws InstructionException {
		if (aValue.length() == 0)
			return 0;

		// Remove all whitespaces
		aValue = aValue.replaceAll("\\s", "");

		// Translate all labels into memory addresses
		if (aLabels != null) {
			Pattern pLabels = Pattern.compile("[\\w\\_]+");
			Matcher mLabels = pLabels.matcher(aValue);
			while (mLabels.find()) {
				String match = mLabels.group(0);
				if (aLabels.containsKey(match)) {
					long newValue = (long)(aLabels.get(match) - aAddr) / aDivider;
					aValue = Utilities.stringReplace(aValue, match, Long.toString(newValue));
				}
			}
		}

		// Find all parentheses and macron
		StringBuffer sb = new StringBuffer(128);	// used to avoid string concatination
		boolean found;
		do {
			found = false;
			Pattern pParenthesis = Pattern.compile("(%(lo|hi|hiadj|\\-))?\\(([^\\(\\)]+)\\)");
			Matcher mParenthesis = pParenthesis.matcher(aValue);
			while (mParenthesis.find()) {
				found = true;
				long inner = parseValue(mParenthesis.group(3), aLabels, aAddr, aDivider);

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
				aValue = Utilities.stringReplace(aValue, mParenthesis.group(0), Long.toString(inner));
			}
		} while (found);

		// Translate all binary and hexdecimal values into decimal values
		Pattern pHexBin = Pattern.compile("(-)?0((x)([0-9A-Fa-f]+)|(b)([0-1]+))");
		Matcher mHexBin = pHexBin.matcher(aValue);

		while (mHexBin.find()) {
			sb.setLength(0);
			sb.append( (mHexBin.group(1) != null) ? "-" : "" );

			if (mHexBin.group(3) != null) {
				sb.append( Long.toString(parseLong(mHexBin.group(4), 16)) );
			} else {
				sb.append( Long.toString(parseLong(mHexBin.group(6), 2)) );
			}
			aValue = Utilities.stringReplaceOne(aValue, mHexBin.group(0), sb.toString());
		}

		// Take care of the "not"-operation
		Pattern pNotOperator = Pattern.compile("~(-?[\\d]+)");
		Matcher mNotOperator = pNotOperator.matcher(aValue);
		while (mNotOperator.find()) {
			long newValue = ~parseLong(mNotOperator.group(1));
			aValue = Utilities.stringReplace(aValue, mNotOperator.group(0), Long.toString(newValue));
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
					long valueA = parseLong(mCalculate.group(1));
					long valueB = parseLong(mCalculate.group(3));
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
						// Can't happen
						throw new InstructionException("Not a valid operator", mCalculate.group(2));
					}
					aValue = Utilities.stringReplace(aValue, mCalculate.group(0), Long.toString(newValue));
				}
			} while (found);
		}

		// Now it should just be a numeric value
		return parseLong(aValue);
	}

	private static long parseLong(String value) throws InstructionException {
		return parseLong(value, 10);
	}

	private static long parseLong(String value, int base) throws InstructionException {
		try {
			return Long.parseLong(value, base);
		} catch (NumberFormatException e) {
			throw new InstructionException("Not a valid value", value);
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
		return MemoryManager.PROGRAMSTARTADDR + (this.instructions.size()*4);
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
				aLine = Utilities.stringReplace(aLine, constant.getName(), constant.getValue());
			}
		}

		// Find the important part of the line. That will say remove commants and whitespace in the beginning and end
		Pattern pRemoveWhitespace = Pattern.compile("\\s*([^#]*?)\\s*(#.*)*");
		Matcher mRemoveWhitespace = pRemoveWhitespace.matcher(aLine);
		if (mRemoveWhitespace.matches()) {
			String match = mRemoveWhitespace.group(1);
			if (match.length() > 0) {
				// Find labels
				Pattern pLabel = Pattern.compile("([\\w\\_]+):\\s*(.*?)");
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
					Pattern pInstruction = Pattern.compile("(\\.)?([A-Za-z]+)(|\\s+(.*?))");
					Matcher mInstruction = pInstruction.matcher(match);
					if (mInstruction.matches()) {

						// Check if the first character is a ".".
						if (mInstruction.group(1) != null) {
							String name = mInstruction.group(2);

							// Handle all parts with a "."-character in the begining
							if (name.equals("equ")) {
								this.constants.add(new Constant(mInstruction.group(4)));
							} else if (name.equals("data")) {
								this.codePart = false;
							} else if (name.equals("text")) {
								this.codePart = true;
							} else if (name.equals("global") || name.equals("globl")) {
								this.globals.add(mInstruction.group(4));
							} else if (name.equals("macro")) {
								Pattern pMacro = Pattern.compile("([A-Za-z]+)\\s*(.*?)");
								Matcher mMacro = pMacro.matcher(mInstruction.group(4));
								if (mMacro.matches()) {
									this.lastMacro = this.macros.put(mMacro.group(1), mMacro.group(2), null, aLineNumber, null);
								} else {
									throw new CompilerException(aLineNumber, "Wrong syntax for a macro");
								}
							} else if (name.equals("endm")) {
								this.lastMacro = null;
							} else if (name.equals("word") || name.equals("hword") || name.equals("byte") || name.equals("ascii") || name.equals("asciz") || name.equals("string")) {
								try {
									String[] variables = mInstruction.group(4).split(",");
									Variable.TYPE type;
									if (name.equals("word"))
										type = Variable.TYPE.WORD;
									else if (name.equals("hword"))
										type = Variable.TYPE.HWORD;
									else if (name.equals("byte"))
										type = Variable.TYPE.BYTE;
									else if (name.equals("ascii"))
										type = Variable.TYPE.ASCII;
									else
										type = Variable.TYPE.ASCIZ;

									for (String variable : variables) {
										this.variables.add(new Variable(this.lastLabel, type, variable));
										this.lastLabel = null;
									}
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else if (name.equals("skip")) {
								try {
									long count = this.parseValue(mInstruction.group(4));
									for (int i = 0; i < count; i++) {
										this.variables.add(new Variable(this.lastLabel, (byte)0));
										this.lastLabel = null;
									}
								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else if (name.equals("fill")) {
								String[] variables = mInstruction.group(4).split(",");
								if (variables.length != 3)
									throw new CompilerException(aLineNumber, "Not enough arguments");

								try {
									long count = this.parseValue(variables[0]);
									long size = this.parseValue(variables[1]);
									byte[] value = Utilities.longToByteArray(this.parseValue(variables[2]));

									for (int i = 0; i < count; i++) {
										for (int j = 0; j < size; j++) {
											this.variables.add(new Variable(this.lastLabel, value[(int)j]));
											this.lastLabel = null;
										}
									}

								} catch (InstructionException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else if (name.equals("end")) {
								// Not sure if we have to do anything but we have it here so we don't get an error
							} else if (name.equals("include")) {
								Pattern pFile = Pattern.compile("\"([^\"]+)\"");
								Matcher mFile = pFile.matcher(mInstruction.group(4));
								if (mFile.matches()) {
									if (this.currentDir == null)
										throw new CompilerException(aLineNumber, "Include path unknown (save file)");

									String path = this.currentDir +"/"+ mFile.group(1);
									try {
										String content;
										if ((content = Editor.read(path)) != null) {
											String[] lines = content.split("\r\n|\n|\r");
											String[] tmpLines = new String[this.lines.length + lines.length + 2];
											for (int i = 0; i < aLineNumber; i++)
												tmpLines[i] = new String(this.lines[i]);
											tmpLines[aLineNumber] = "# START INCLUDE";
											for (int i = 0; i < lines.length; i++)
												tmpLines[aLineNumber + 1 + i] = new String(lines[i]);
											tmpLines[aLineNumber + lines.length + 1] = "# END INCLUDE";
											for (int i = 0; i < this.lines.length - aLineNumber; i++)
												tmpLines[aLineNumber + 2 + lines.length + i] = new String(this.lines[aLineNumber + i]);
											this.lines = tmpLines;
										}
									} catch (IOException e) {
										throw new CompilerException(aLineNumber, "Can't open include file: "+ path);
									}
								}
							} else {
								throw new CompilerException(aLineNumber, "Unknown: "+ name);
							}
						} else {
							// Check if the instruction is a macro
							String ins = mInstruction.group(2);
							if (this.macros.exists(ins)) {
								try {
									// Parse the macro
									Macro macro = this.macros.get(ins);
									ArrayList<String> lines = macro.parse(mInstruction.group(4));

									if (this.lastMacro != null) {
										this.lastMacro.addLine(lines);
									} else {
										int i = 0;
										try {
											for (String line: lines) {
												i++;
												this.parseLine(line, false, aLineNumber);
											}
										} catch (CompilerException e) {
											throw new CompilerException(aLineNumber, "Macro "+ ins +": Assembler error:\n\t"+ macro.getLineNumberAsString(i) +": "+ e.getMessagePart());
										}
									}
								} catch (MacroException e) {
									throw new CompilerException(aLineNumber, e.getMessage());
								}
							} else {
								if (this.lastMacro != null) {
									this.lastMacro.addLine(mInstruction.group(0));
								} else {
									// get the CompilerInstruction for the instruction
									try {
										CompilerInstruction cins = InstructionManager.get(ins, mInstruction.group(4), aLineNumber);
										this.instructions.add(cins);
									} catch (InstructionException e) {
										throw new CompilerException(aLineNumber, e.getMessage());
									}
								}
							}
						}
					} else {
						throw new CompilerException(aLineNumber, "Wrong syntax");
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
		for (int i = 0; i < this.lines.length; i++) {
			this.parseLine(this.lines[i], true, i+1);
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
	public Program link() throws CompilerException {
		int size = 0;
		for (Variable variable: this.variables)
			size += variable.getLength();

		int diff = size % 4;
		if (diff > 0)
			size += 4 - diff;

		byte[] binaryVariables = new byte[size];

		size = this.instructions.size()*4+4;
		byte[] binaryProgram = new byte[size];

		// Add variables in the memory
		int addr = 0;
		for (Variable variable: this.variables) {
			if (variable.getName() != null)
				this.labels.put(variable.getName(), MemoryManager.VARIABLESTARTADDR + addr);

			variable.setStartAddr(MemoryManager.VARIABLESTARTADDR + addr);
			byte[] value = variable.getStartValue();
			System.arraycopy(value, 0, binaryVariables, addr, value.length);
			addr += value.length;
		}

		// Add program to the memory
		addr = 0;
		for (CompilerInstruction instruction: this.instructions) {
			try {
				instruction.link(this.labels, MemoryManager.PROGRAMSTARTADDR + addr);
			} catch (InstructionException e) {
				throw new CompilerException(instruction.getLineNumber(), e.getMessage());
			}

			byte[] value = Utilities.intToByteArray(instruction.getOpcode());
			System.arraycopy(value, 0, binaryProgram, addr, value.length);
			addr += 4;
		}

		int pc = MemoryManager.PROGRAMSTARTADDR;
		try {
			pc = this.getGlobal("main");
		} catch (CompilerException e) {}

		try {
			return new Program(this.lines, this.instructions, this.variables, binaryProgram, binaryVariables, pc);
		} catch (InstructionException e) {
			// Could not happen
			throw new CompilerException();
		}
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

	/**
	 * Returns input with all multiline comments removed.
	 * Uses regex from http://stackoverflow.com/a/241506
	 * @param input Input string
	 * @return Returns input with comments replaced by spaces
	 */

	private static String removeMultilineComments(String input) {
		Pattern myPattern = Pattern.compile("//.*?$|/\\*.*?\\*/|\\'(?:\\\\.|[^\\\\\\'])*\\'|\"(?:\\\\.|[^\\\\\"])*\"",
			Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = myPattern.matcher(input);
		StringBuffer b = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(b, "");
			String group = matcher.group();
			if (group.charAt(0) != '/') {
				// not a comment, but a string or something else
				b.append(group);
				continue;
			}
			b.append(' ');
			for (int i = 0; i < group.length(); i++) {
				if (group.charAt(i) == '\n') {
					b.append('\n');
				}
			}
		}
		matcher.appendTail(b);
		return b.toString();
	}
}
