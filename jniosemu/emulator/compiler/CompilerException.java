package jniosemu.emulator.compiler;

public class CompilerException extends Exception {
  private int lineNumber = -1;
  private String message = "";

  public CompilerException() {
    super();
  }

  public CompilerException(String msg) {
    super("Line Unknown: " + msg);

    this.message = msg;
  }

  public CompilerException(int lineNumber, String msg) {
    super("Line " + lineNumber + ": " + msg);

    this.lineNumber = lineNumber;
    this.message = msg;
  }

  public String getMessagePart() {
    return this.message;
  }
}
