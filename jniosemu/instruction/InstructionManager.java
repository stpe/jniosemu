package jniosemu.instruction;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.ArrayList;
import jniosemu.instruction.emulator.*;
import jniosemu.instruction.compiler.*;
import jniosemu.emulator.compiler.macro.MacroManager;

/**
 * Manage all instructions and provide related static utility methods.
 */
public class InstructionManager
{
	/**
	 * Used to track if instruction has been populated or not.
	 */
	private static boolean inited = false;
	
	/**
   * Used for fast getting an InstructionInfo from an opcode
   */
	private static HashMap<Integer, InstructionInfo> opCodeHash;
	
	/**
	 * Used for fast getting an InstructionInfo from a name
	 */
	private static HashMap<String, InstructionInfo> nameHash;
	
	/**
	 * Contains all InstructionInfo
	 */
	private static ArrayList<InstructionInfo> instructions;
	
	/**
	 * Categories for instructions used for instruction insert menu.
	 */
	public static enum INSTRUCTION_CATEGORY {
		ARITHMETIC_LOGICAL,
		MOVE,
		COMPARISON,
		SHIFT_ROTATE,
		PROGRAM_CONTROL,
		DATA_TRANSFER,
		OTHER
	}
	
	/**
	 * Used to keep track of which category each instruction belongs to.
	 */
	private static HashMap<String, INSTRUCTION_CATEGORY> categorizedInstructions = new HashMap<String, INSTRUCTION_CATEGORY>();

	/**
	 * Init the InstructionManager by populating the instructions.
	 *
	 * @post     Populate instructions, opCodeHash and nameHash.
	 * @calledby EmulatorManager()
	 * @calls    InstructionInfo()
	 */
	private static void init() {
		if (inited)
			return;

		opCodeHash = new HashMap<Integer, InstructionInfo>(50);
		nameHash = new HashMap<String, InstructionInfo>(50);
		instructions = new ArrayList<InstructionInfo>(50);

		addInstruction("ADD",     0x1883A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("ADDI",    0x04,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("AND",     0x703A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("ANDHI",   0x2C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("ANDI",    0x0C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("BEQ",     0x26,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND,  INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("BGE",     0x0e,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND,  INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("BGEU",    0x2e,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND,  INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("BLT",     0x16,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND,  INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("BLTU",    0x36,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND,  INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("BNE",     0x1e,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND,  INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("BR",      0x06,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH,       INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("CALL",    0x0,        InstructionInfo.Type.JTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("CALLR",   0x3EE83A,   InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.CALLJUMP,     INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("CMPEQ",   0x1003A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPEQI",  0x20,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPGE",   0x403A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPGEI",  0x08,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPGEU",  0x1403A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPGEUI", 0x28,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPLT",   0x803A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPLTI",  0x10,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPLTU",  0x1803A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPLTUI", 0x30,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPNE",   0xC03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("CMPNEI",  0x18,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.COMPARISON);
    addInstruction("DIV",     0x1283A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("DIVU",    0x1203A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("JMP",     0x683A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.CALLJUMP,     INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("LDB",     0x07,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("LDBU",    0x03,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("LDH",     0x0F,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("LDHU",    0x0B,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("LDW",     0x17,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("MUL",     0x1383A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("MULI",    0x24,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("MULXSS",  0xF83A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("MULXSU",  0xB83A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("MULXUU",  0x383A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("NEXTPC",  0xE03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.PC,           INSTRUCTION_CATEGORY.OTHER);
    addInstruction("NOR",     0x303A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("OR",      0xB03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("ORHI",    0x34,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("ORI",     0x14,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("RET",     0xF800283A, InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.NONE,         INSTRUCTION_CATEGORY.PROGRAM_CONTROL);
    addInstruction("ROL",     0x183A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("ROLI",    0x83A,      InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT,        INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("ROR",     0x583A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("SLL",     0x983A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("SLLI",    0x903A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT,        INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("SRA",     0x1D83A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("SRAI",    0x1D03A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT,        INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("SRL",     0xD83A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("SRLI",    0xD03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT,        INSTRUCTION_CATEGORY.SHIFT_ROTATE);
    addInstruction("STB",     0x05,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("STH",     0x0D,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("STW",     0x15,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY,       INSTRUCTION_CATEGORY.DATA_TRANSFER);
    addInstruction("SUB",     0x1C83A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("XOR",     0xF03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("XORHI",   0x3C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);
    addInstruction("XORI",    0x1C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT,      INSTRUCTION_CATEGORY.ARITHMETIC_LOGICAL);

		for (InstructionInfo instruction: instructions) {
			opCodeHash.put(instruction.getHash(), instruction);
			nameHash.put(instruction.getName(), instruction);
		}

		inited = true;
	}

	/**
	 * Add instruction to instructions list and category.
	 *
	 * @calledby        init()
	 *
	 * @param name      name of the instruction
	 * @param opCode    op-code for the instruction
	 * @param type      sype of instruction
	 * @param syntax    syntax of the instruction
	 * @param category  instruction category
	 */
	private static void addInstruction(String name, int opCode, InstructionInfo.Type type, InstructionInfo.Syntax syntax, INSTRUCTION_CATEGORY category)
	{
		instructions.add(new InstructionInfo(name, opCode, type, syntax));
		addInstructionToCategory(name, category);
	}

	/**
	 * Add instruction to category list.
	 *
	 * @calledby        addInstruction(), addMacro()
	 *
	 * @param name      name of the instruction
	 * @param category  instruction category
	 */
	public static void addInstructionToCategory(String name, INSTRUCTION_CATEGORY category)
	{
		categorizedInstructions.put(name, category);
	}

	/**
	 * Add macro to category list.
	 *
	 * @calledby        MacroManager.addMacro()
	 *
	 * @param name      name of the instruction
	 * @param args      arguments of macro
	 * @param category  instruction category
	 */
	public static void addMacro(String name, String args, INSTRUCTION_CATEGORY category)
	{
		// Note: take String args as parameter since it will be used for future getArgument()
		
		addInstructionToCategory(name, category);
	}

	public static int getHash(int opCode) {
		int op = opCode & 0x3F;
		if (op == 0x3A)
			op = op | (opCode & 0x1F800);

		return op;
	}

	public static InstructionInfo getInfo(int opCode) {
		init();

		return opCodeHash.get(getHash(opCode));
	}

	/**
	 * Translate an opcode to an instruction.
	 *
	 * @pre opCodeHash must be populated.
	 * @calledby EmulatorManager
	 * @calls Instruction()
	 *
	 * @param opCode  Opcode of the instruction
	 * @return Instruction  The instruction that one requested
	 * @throws InstructionException  If the instruction don't exists or the instruction class is missing
	 */
	public static Instruction get(int opCode) throws InstructionException {
		init();

		// Check the last 6 bits if it is an opx instruction
		int op = opCode & 0x3F;
		if (op == 0x3A)
			op = op | (opCode & 0x1F800);

		InstructionInfo instruction = opCodeHash.get(op);
		if (instruction == null)
			throw new InstructionException(opCode);

		try {
			Constructor c = Class.forName(instruction.getClassName()).getConstructors()[0];
			Object[] args = new Object[]{opCode};
			return (Instruction)c.newInstance(args);
		} 
		// catch are done per exception instead of one Exception in order to
		// not by mistake catch RuntimeException that may hide potential bugs
		catch (ClassNotFoundException e) {
			throw new InstructionException(opCode, "Class is missing: "+ instruction.getClassName());
		} catch (InstantiationException e) {
			// does only happen if trying to make instance of interface or abstract class
			throw new InstructionException(opCode, "Cannot make instance of class: "+ instruction.getClassName());
		} catch (IllegalAccessException e) {
			throw new InstructionException(opCode, "No access to class: "+ instruction.getClassName());
		} catch (java.lang.reflect.InvocationTargetException e) {
			throw new InstructionException(opCode, "Invocation target exception: "+ instruction.getClassName());
		}
	}

	/**
	 * Translate an instruction name and arguments to a CompilerInstruction.
	 *
	 * @pre nameHash must be populated.
	 * @calledby Compiler
	 * @calls CompilerITypeInstruction(), CompilerRTypeInstruction(), CompilerJTypeInstructio()
	 *
	 * @param aName  Name of the instruction
	 * @param aArgs  Arguments for the instruction
	 * @param aLineNumber  Line number in the sourcecode from where the instruction comes from
	 * @return CompilerInstruction of requested type
	 * @throws InstructionException  If the instruction don't exists
	 */
	public static CompilerInstruction get(String aName, String aArgs, int aLineNumber) throws InstructionException {
		init();

		InstructionInfo instructionInfo = nameHash.get(aName.toLowerCase());
		if (instructionInfo == null)
			throw new InstructionException(aName, "Unknown instruction");

		switch (instructionInfo.getType()) {
			case ITYPE:
				return new CompilerITypeInstruction(instructionInfo, aArgs, aLineNumber);
			case RTYPE:
				return new CompilerRTypeInstruction(instructionInfo, aArgs, aLineNumber);
			case JTYPE:
				return new CompilerJTypeInstruction(instructionInfo, aArgs, aLineNumber);
		}

		throw new InstructionException();
	}

	/**
	 * Get instruction arguments as a string.
	 *
	 * @calledby GUIEditor.insertInstruction()
	 *
	 * @param    instruction  instruction name
	 * @return   comma separated arguments
	 */
	public static String getArgument(String instruction) {
		return " rX, rY, rZ";
	}
	
	/**
	 * Get available instructions in sorted order.
	 *
	 * @pre       The HashMap categorizedInstructions must be populated.
	 * @calledby  GUIMenuBar.setupInstructions()
	 *
	 * @return  sorted list of instructions
	 */
	public static ArrayList<String> getSortedInstructionList()
	{
		init();
		
		// Note: This method is only called once when the menu is populated,
		//       therefore there is no need to cache the sorted arraylist.
		
		// temporarily create instance of MacroManager to populate 
		// macros in categorized instruction list
		MacroManager macroMan = new MacroManager();
		
		ArrayList<String> list = new ArrayList<String>( categorizedInstructions.keySet() );
		java.util.Collections.sort(list);
		
		return list;
	}
	
	/**
	 * Get instruction category for given instruction.
	 *
	 * @pre       The HashMap categorizedInstructions must be populated.
	 * @calledby  GUIMenuBar.setupInstructions()
	 *
	 * @param     instruction  instruction to get category for
	 * @return    instruction  category
	 */
	public static INSTRUCTION_CATEGORY getInstructionCategory(String instruction)
	{
		return categorizedInstructions.get(instruction);
	}

}
