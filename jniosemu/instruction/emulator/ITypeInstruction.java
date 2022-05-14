package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;
import jniosemu.instruction.InstructionInfo;
import jniosemu.instruction.InstructionManager;

/** Used for running instructions of type I */
public abstract class ITypeInstruction extends Instruction {
  /** rA register */
  protected int rA;
  /** rB register */
  protected int rB;
  /** imm part of the instruction */
  protected short imm;

  public ITypeInstruction(int opCode) {
    this.opCode = opCode;

    this.rA = (opCode >>> 27) & 0x1F;
    this.rB = (opCode >>> 22) & 0x1F;
    this.imm = (short) ((opCode >>> 6) & 0xFFFF);
  }

  public abstract void run(Emulator em) throws EmulatorException;

  public String toString() {
    InstructionInfo info = InstructionManager.getInfo(this.opCode);

    if (info != null) {
      switch (info.getSyntax()) {
        case DEFAULT:
          return info.getName()
              + " r"
              + Integer.toString(this.rB)
              + ", r"
              + Integer.toString(this.rA)
              + ", "
              + Integer.toString(this.imm);
        case BRANCH_COND:
          return info.getName()
              + " r"
              + Integer.toString(this.rA)
              + ", r"
              + Integer.toString(this.rB)
              + ", "
              + Integer.toString(this.imm);
        case BRANCH:
          return info.getName() + " " + Integer.toString(this.imm);
        case MEMORY:
          return info.getName()
              + " r"
              + Integer.toString(this.rB)
              + ", "
              + Integer.toString(this.imm)
              + "(r"
              + Integer.toString(this.rA)
              + ")";
      }
    }

    return "Invalid IType Instruction!";
  }
}
