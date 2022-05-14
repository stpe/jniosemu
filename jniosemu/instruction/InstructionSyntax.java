package jniosemu.instruction;

public abstract class InstructionSyntax implements Comparable {
  /** Categories for instructions used for instruction insert menu. */
  public static enum CATEGORY {
    ARITHMETIC_LOGICAL,
    MOVE,
    COMPARISON,
    SHIFT_ROTATE,
    PROGRAM_CONTROL,
    DATA_TRANSFER,
    OTHER
  }

  protected CATEGORY category = null;

  /** Name of instruction. */
  protected String name;

  public abstract String getName();

  public CATEGORY getCategory() {
    return this.category;
  }

  public abstract String getArguments() throws InstructionException;

  public int compareTo(Object obj) {
    return this.name.compareTo(((InstructionSyntax) obj).getName());
  }
}
