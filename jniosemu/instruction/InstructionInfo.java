package jniosemu.instruction;

/** Have info about one instruction */
public class InstructionInfo extends InstructionSyntax {
  /** The different type of instructions. */
  public static enum Type {
    ITYPE,
    RTYPE,
    JTYPE
  };
  /** The different syntax of instructions. */
  public static enum Syntax {
    DEFAULT,
    BRANCH_COND,
    BRANCH,
    MEMORY,
    CALLJUMP,
    PC,
    SHIFT,
    CUSTOM,
    NONE
  };

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

  /** OpCode of the instruction */
  private final int opCode;
  /** Type of the instruction */
  private final Type type;
  /** Syntax of the instruction */
  private final Syntax syntax;

  /**
   * Creating Instruction info
   *
   * @calledby InstructionManager.
   * @param name name of the instruction
   * @param opCode op-code for the instruction
   * @param type sype of instruction
   * @param syntax syntax of the instruction
   */
  public InstructionInfo(String name, int opCode, Type type, Syntax syntax, CATEGORY category) {
    this.name = name.toLowerCase();
    this.opCode = opCode;
    this.type = type;
    this.syntax = syntax;
    this.category = category;
  }

  /**
   * Returns the classname of this instruction.
   *
   * @calledby InstructionManager.get()
   * @return Classname of the instruction
   */
  public String getClassName() {
    return String.valueOf(
            "jniosemu.instruction.emulator." + Character.toUpperCase(this.name.charAt(0)))
        + this.name.substring(1)
        + "Instruction";
  }

  /**
   * Returns the name of the instruction.
   *
   * @calledby InstructionManager.get(), InstructionManager(), CompilerInstruction().
   * @return Name of the instruction
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the hash of the instruction.
   *
   * @calledby InstructionManager.get(), InstructionManager().
   * @return Hash of the instruction.
   */
  public int getHash() {
    return InstructionManager.getHash(this.opCode);
  }

  /**
   * Returns the opCode of the instruction.
   *
   * @calledby InstructionManager.get().
   * @return OpCode of the instruction
   */
  public int getOpCode() {
    return this.opCode;
  }

  /**
   * Returns the type of the instruction.
   *
   * @calledby InstructionManager.get().
   * @return Type of the instruction
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Returns the syntax of the instruction.
   *
   * @calledby CompilerInstruction(), CompilerInstruction.link()
   * @return Syntax of the instruction
   */
  public Syntax getSyntax() {
    return this.syntax;
  }

  public String getArguments() throws InstructionException {
    switch (this.type) {
      case ITYPE:
        switch (this.syntax) {
          case DEFAULT:
            return "rB, rA, imm";
          case BRANCH_COND:
            return "rA, rB, imm";
          case BRANCH:
            return "label";
          case MEMORY:
            return "rB, imm(rA)";
        }
        break;
      case RTYPE:
        switch (this.syntax) {
          case DEFAULT:
            return "rC, rA, rB";
          case CALLJUMP:
            return "rA";
          case PC:
            return "rC";
          case SHIFT:
            return "rC, rA, imm";
          case CUSTOM:
            return "imm, rC, rA, rB";
          case NONE:
            return "";
        }
        break;
      case JTYPE:
        switch (this.syntax) {
          case DEFAULT:
            return "imm";
        }
        break;
    }

    throw new InstructionException();
  }
}
