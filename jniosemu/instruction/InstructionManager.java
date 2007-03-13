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

		instructions.add(new InstructionInfo("ADDI",  0x04,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ANDHI", 0x2C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("ANDI",  0x0C,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("BLT",   0x16,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH_COND));
		instructions.add(new InstructionInfo("BR",    0x06,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.BRANCH));
		instructions.add(new InstructionInfo("LDW",   0x17,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("ORHI",  0x34,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("STW",   0x15,       InstructionInfo.Type.ITYPE, InstructionInfo.Syntax.MEMORY));
		instructions.add(new InstructionInfo("ADD",   0x1883A,    InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("AND",   0x703A,     InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.DEFAULT));
		instructions.add(new InstructionInfo("CALLR", 0x3EE83A,   InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.CALLJUMP));
		instructions.add(new InstructionInfo("RET",   0xF800283A, InstructionInfo.Type.RTYPE, InstructionInfo.Syntax.NONE));
		instructions.add(new InstructionInfo("CALL",  0x0,        InstructionInfo.Type.JTYPE, InstructionInfo.Syntax.DEFAULT));

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
