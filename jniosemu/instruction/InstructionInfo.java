package jniosemu.instruction;

public class InstructionInfo
{
	public static enum Type {ITYPE, RTYPE, JTYPE};
	public static enum Syntax {DEFAULT, BRANCH_COND, BRANCH, MEMORY, CALLJUMP, PC, SHIFT, CUSTOM, NONE};

	/*
	ITypeInstruction
	DEFAULT: addi rB, rA, imm
	BRANCH_COND: bne rA, rB, imm
	BRANCH: br label
	MEMORY: ldb rB, byte_offset(rA)

	RTypeInstruction
	DEFAULT: add rC, rA, rB
	CALLJUMP: jmp rA
	PC: nextpc rC
	SHIFT: srli rC, rA, IMM5
	CUSTOM: custom imm, rC, rA, rB
	NONE: ret

	JTypeInstruction
	DEFAULT: call imm
	*/

	private final String name;
	private final int    opCode;
	private final Type   type;
	private final Syntax syntax;

	/**
	 * Creating Instruction info
	 *
	 * @param name		Name of the instruction
	 * @param opCode	OpCode for the instruction.
	 * @param type		Type of instruction
	 * @param syntax	Syntax of the instruction
	 */	 
	public InstructionInfo(String name, int opCode, Type type, Syntax syntax) {
		this.name   = name.toLowerCase();
		this.opCode = opCode;
		this.type   = type;
		this.syntax = syntax;
	}

	/**
	 * Returns the classname of this instruction
	 *
	 * @ret The classname of the instruction
	 */
	public String getClassName() {
		return String.valueOf("jniosemu.instruction.emulator."+ Character.toUpperCase(this.name.charAt(0))) + this.name.substring(1) + "Instruction";
	}

	/**
	 * Returns the name of the instruction
	 *
	 * @ret The name of the instruction
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the hash of the instruction
	 *
	 * @ret The hash of the instruction
	 */
	public int getHash() {
		return this.opCode & 0x1F83F;
	}

	/**
	 * Returns the opCode of the instruction
	 *
	 * @ret The opCode of the instruction
	 */
	public int getOpCode() {
		return this.opCode;
	}

	/**
	 * Returns the type of the instruction
	 *
	 * @ret The type of the instruction
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns the syntax of the instruction
	 *
	 * @ret The syntax of the instruction
	 */
	public Syntax getSyntax() {
		return this.syntax;
	}
}
