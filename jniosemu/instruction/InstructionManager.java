package jniosemu.instruction;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.ArrayList;
import jniosemu.instruction.emulator.*;
import jniosemu.instruction.compiler.*;

/**
 * Manage all instructions.
 */
public class InstructionManager
{
	private static boolean inited = false;
	/**
   * Used for fast getting an InstructionInfo from an opcode
   */
	private static Hashtable<Integer, InstructionInfo> opCodeHash;
	/**
	 * Used for fast getting an InstructionInfo from a name
	 */
	private static Hashtable<String, InstructionInfo> nameHash;
	/**
	 * Contains all InstructionInfo
	 */
	private static ArrayList<InstructionInfo> instructions;

	/**
	 * Init the instructionManager.
	 *
	 * @post Populate instructions, opCodeHash and nameHash
	 * @calledby EmulatorManager()
	 * @calls InstructionInfo()
	 */
	private static void init() {
		if (inited)
			return;

		opCodeHash = new Hashtable<Integer, InstructionInfo>(50);
		nameHash = new Hashtable<String, InstructionInfo>(50);
		instructions = new ArrayList<InstructionInfo>(50);

		instructions.add(new InstructionInfo("ADD",     0x1883A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ADDI",    0x04,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("AND",     0x703A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ANDHI",   0x2C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ANDI",    0x0C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("BEQ",     0x26,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BGE",     0x0e,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BGEU",    0x2e,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BLT",     0x16,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BLTU",    0x36,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BNE",     0x1e,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BR",      0x06,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH));
		instructions.add(new InstructionInfo("CALL",    0x0,        InstructionInfo.Type.JTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CALLR",   0x3EE83A,   InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.CALLJUMP));
		instructions.add(new InstructionInfo("CMPEQ",   0x1003A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPEQI",  0x20,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPGE",   0x403A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPGEI",  0x08,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPGEU",  0x1403A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPGEUI", 0x28,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPLT",   0x803A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPLTI",  0x10,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPLTU",  0x1803A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPLTUI", 0x30,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPNE",   0xC03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CMPNEI",  0x18,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("DIV",     0x1283A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("DIVU",    0x1203A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("JMP",     0x683A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.CALLJUMP));
		instructions.add(new InstructionInfo("LDB",     0x07,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("LDBU",    0x03,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("LDH",     0x0F,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("LDHU",    0x0B,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("LDW",     0x17,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("MUL",     0x1383A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("MULI",    0x24,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("MULXSS",  0xF83A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("MULXSU",  0xB83A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("MULXUU",  0x383A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("NEXTPC",  0xE03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.PC));
		instructions.add(new InstructionInfo("NOR",     0x303A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("OR",      0xB03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ORHI",    0x34,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ORI",     0x14,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("RET",     0xF800283A, InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.NONE));
		instructions.add(new InstructionInfo("ROL",     0x183A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ROLI",    0x83A,      InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT));
		instructions.add(new InstructionInfo("ROR",     0x583A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("SLL",     0x983A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("SLLI",    0x903A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT));
		instructions.add(new InstructionInfo("SRA",     0x1D83A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("SRAI",    0x1D03A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT));
		instructions.add(new InstructionInfo("SRL",     0xD83A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("SRLI",    0xD03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.SHIFT));
		instructions.add(new InstructionInfo("STB",     0x05,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("STH",     0x0D,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("STW",     0x15,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("SUB",     0x1C83A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("XOR",     0xF03A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("XORHI",   0x3C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("XORI",    0x1C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));

		for (InstructionInfo instruction: instructions) {
			opCodeHash.put(instruction.getHash(), instruction);
			nameHash.put(instruction.getName(), instruction);
		}

		inited = true;
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
		} catch (Exception e) {
			throw new InstructionException(opCode, "Class missing: "+ instruction.getClassName());
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
			throw new InstructionException(aName);

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

	public static String intToHexString(int value) {
		String hex = "00000000"+ Integer.toHexString(value);
		return "0x"+ hex.substring(hex.length()-8, hex.length());
	}
}
