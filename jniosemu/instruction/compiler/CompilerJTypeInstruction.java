package jniosemu.instruction.compiler;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.instruction.InstructionException;
import jniosemu.instruction.InstructionInfo;

/** Contains info about a instruction of type J during compilation. */
public class CompilerJTypeInstruction extends CompilerInstruction {
  /** Contains the immediate value which is calculated from tImm during linking */
  private int imm = 0;
  /** Contains the temporary immediate value which is used during linking */
  private String tImm = "";

  /**
   * Create a CompilerJTypeInstruction by parsing the arguments
   *
   * @post tImm is set
   * @param aInstructionInfo Info about the instruction
   * @param aArgs Arguments
   * @param aLineNumber LineNumber in the sourcecode
   * @throws InstructionException If the argument syntax is wrong
   */
  public CompilerJTypeInstruction(InstructionInfo aInstructionInfo, String aArgs, int aLineNumber)
      throws InstructionException {
    this.instructionInfo = aInstructionInfo;
    this.lineNumber = aLineNumber;

    String[] args;

    Pattern pArgs;
    Matcher mArgs;
    switch (this.instructionInfo.getSyntax()) {
      case DEFAULT:
        this.tImm = aArgs;
        break;
      default:
        throw new InstructionException();
    }
  }

  /**
   * Returns the opcode of the instruction.
   *
   * @pre imm must get its value which it get during linking
   * @calledby Compiler.link()
   * @calls InstructionInfo.getOpCode()
   * @return Opcode
   */
  public int getOpcode() {
    return (this.imm & 0x3FFFFFF) << 6 | this.instructionInfo.getOpCode();
  }

  /**
   * Linking the instruction. Translate labels into memory addresses.
   *
   * @post Set imm
   * @calledby Compiler.link()
   * @calls Compiler.parseValue()
   * @param aLabels Labels with there memory address
   * @param aAddr Memory address where this instruction is placed in memory
   * @throws InstructionException If the immediate value can't be parsed
   */
  public void link(Hashtable<String, Integer> aLabels, int aAddr) throws InstructionException {
    long imm;
    try {
      imm = Compiler.parseValue(this.tImm, aLabels, 0, 4);
    } catch (InstructionException e) {
      throw new InstructionException(
          this.instructionInfo.getName(), "Error parsing immediate value (" + this.tImm + ")");
    }

    this.imm = (int) (imm & 0x3FFFFFF);

    if ((imm >> 26) != 0 && (imm >> 26) != -1)
      throw new InstructionException(
          this.instructionInfo.getName(), "Immediate value is out of range");
  }
}
