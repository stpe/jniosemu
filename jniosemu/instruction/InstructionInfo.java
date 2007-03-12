package jniosemu.instruction;

/**
 * Have info about one instruction
 */
public class InstructionInfo
{
	/**
	 * The different type of instructions.
	 */
	public static enum Type {ITYPE, RTYPE, JTYPE};
	/**
	 * The different syntax of instructions.
	 */
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

	/**
	 * Name of instruction.
	 */
	private final String name;
	/**
	 * OpCode of the instruction
	 */
	private final int    opCode;
	/**
	 * Type of the instruction
	 */
	private final Type   type;
	/**
	 * Syntax of the instruction
	 */
	private final Syntax syntax;

	/**
	 * Creating Instruction info
	 *
	 * @calledby InstructionManager.
	 *
	 * @param name  Name of the instruction
	 * @param opCode  OpCode for the instruction.
	 * @param type  Type of instruction
	 * @param syntax  Syntax of the instruction
	 */
	public InstructionInfo(String name, int opCode, Type type, Syntax syntax) {
		this.name   = name.toLowerCase();
		this.opCode = opCode;
		this.type   = type;
		this.syntax = syntax;
	}

	/**
	 * Returns the classname of this instruction.
	 *
	 * @calledby InstructionManager.get()
	 *
	 * @return Classname of the instruction
	 */
	public String getClassName() {
		return String.valueOf("jniosemu.instruction.emulator."+ Character.toUpperCase(this.name.charAt(0))) + this.name.substring(1) + "Instruction";
	}

	/**
	 * Returns the name of the instruction.
	 *
	 * @calledby InstructionManager.get(), InstructionManager(), CompilerInstruction().
	 *
	 * @return Name of the instruction
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the hash of the instruction.
	 *
	 * @calledby InstructionManager.get(), InstructionManager().
	 *
	 * @return Hash of the instruction.
	 */
	public int getHash() {
		return InstructionManager.getHash(this.opCode);
	}

	/**
	 * Returns the opCode of the instruction.
	 *
   * @calledby InstructionManager.get().
	 *
	 * @return OpCode of the instruction
	 */
	public int getOpCode() {
		return this.opCode;
	}

	/**
	 * Returns the type of the instruction.
	 *
	 * @calledby InstructionManager.get().
	 *
	 * @return Type of the instruction
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns the syntax of the instruction.
	 *
	 * @calledby CompilerInstruction(), CompilerInstruction.link()
	 *
	 * @return Syntax of the instruction
	 */
	public Syntax getSyntax() {
		return this.syntax;
	}
}
