package jniosemu.emulator.compiler.macro;

import java.util.ArrayList;
import jniosemu.Utilities;
import jniosemu.instruction.InstructionException;
import jniosemu.instruction.InstructionSyntax;

/** Contains information about a Macro */
public class Macro extends InstructionSyntax {
  /** Arguments that the macro takes */
  private String[] args;
  /** Lines that the macro exists of */
  private ArrayList<String> lines = new ArrayList<String>();
  /** Line number */
  private int lineNumber = -1;

  /**
   * Init Macro
   *
   * @calledby MacroManager()
   * @param name Name of the macro
   * @param args Arguments of the macro
   * @param lines Lines of the macro
   */
  public Macro(String name, String[] args, String[] lines, int lineNumber, CATEGORY category) {
    this.name = name.toLowerCase();
    this.args = args;
    this.lineNumber = lineNumber;
    this.category = category;

    if (lines != null) {
      for (String line : lines) this.lines.add(line);
    }
  }

  /**
   * Init Macro
   *
   * @param name Name of the macro
   * @param args Arguments of the macro
   * @param lines Lines of the macro
   */
  public Macro(String name, String[] args, ArrayList<String> lines, int lineNumber) {
    this.name = name.toLowerCase();
    this.args = args;
    this.lineNumber = lineNumber;

    this.lines = lines;
  }

  /**
   * Add several lines to the Macro
   *
   * @calledby Compiler.parseLine();
   * @param lines Lines that will be added
   */
  public void addLine(ArrayList<String> lines) {
    for (String line : lines) this.lines.add(line);
  }

  /**
   * Add line to the Macro
   *
   * @calledby Compiler.parseLine();
   * @param line Line that will be added
   */
  public void addLine(String line) {
    this.lines.add(line);
  }

  /**
   * Gets the value of the arguments and replace the arguments with its value
   *
   * @calls parse()
   * @param args Arguments separated with ","-character
   */
  public ArrayList<String> parse(String args) throws MacroException {
    String[] argsArray = null;
    if (args != null && args.length() > 0) argsArray = args.split("\\s*,\\s*");

    return this.parse(argsArray);
  }

  /**
   * Gets the value of the arguments and replace the arguments with its value
   *
   * @param args The arguments value
   * @return The lines
   */
  public ArrayList<String> parse(String[] args) throws MacroException {
    if (args == null ^ this.args == null)
      throw new MacroException(this.name, "Number of arguments is wrong");

    if (args != null && this.args != null && args.length != this.args.length)
      throw new MacroException(this.name, "Number of arguments is wrong");

    ArrayList<String> lines = new ArrayList<String>(this.lines.size());

    if (this.args != null && this.args.length > 0) {
      for (String line : this.lines) {
        String outLine = line;
        for (int j = 0; j < args.length; j++)
          outLine = Utilities.stringReplace(outLine, "\\" + this.args[j], args[j]);
        lines.add(outLine);
      }
    } else {
      lines = new ArrayList<String>(this.lines);
    }

    return lines;
  }

  /**
   * Return the name of the macro
   *
   * @calledby MacroManager(), Compiler.parseLine()
   * @return name of the macro
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return line number where the macro is defined.
   *
   * @return Line number
   */
  public int getLineNumber() {
    return this.lineNumber;
  }

  /**
   * Return line string
   *
   * @checks If lineNumber < 0 return "Psuedo instruction"
   * @calledby Compiler.parseLine()
   * @param macroLine Line number in the macro
   * @return line number as string
   */
  public String getLineNumberAsString(int macroLine) {
    if (this.lineNumber <= 0) return "Psuedo instruction";

    return "Line " + (this.lineNumber + macroLine);
  }

  public String getArguments() throws InstructionException {
    StringBuffer buffer = new StringBuffer();

    int i = 0;
    for (String arg : this.args) {
      buffer.append(arg);
      if (++i < this.args.length) buffer.append(", ");
    }
    return buffer.toString();
  }
}
