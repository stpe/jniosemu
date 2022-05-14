package jniosemu.emulator.compiler.macro;

import java.util.ArrayList;
import java.util.HashMap;
import jniosemu.instruction.InstructionSyntax;

public class MacroManager {
  private HashMap<String, Macro> nameHash = new HashMap<String, Macro>();
  private ArrayList<Macro> macros = new ArrayList<Macro>();

  /** Init the MacroManager */
  public MacroManager() {
    addMacro(
        "BGT",
        "rA, rB, label",
        "BLT \\rB, \\rA, \\label",
        -1,
        InstructionSyntax.CATEGORY.PROGRAM_CONTROL);
    addMacro(
        "BGTU",
        "rA, rB, label",
        "BLTU \\rB, \\rA, \\label",
        -1,
        InstructionSyntax.CATEGORY.PROGRAM_CONTROL);
    addMacro(
        "BLE",
        "rA, rB, label",
        "BGE \\rB, \\rA, \\label",
        -1,
        InstructionSyntax.CATEGORY.PROGRAM_CONTROL);
    addMacro(
        "CMPGT", "rC, rA, rB", "CMPLT \\rC, \\rB, \\rA", -1, InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPGTI",
        "rB, rA, imm",
        "CMPGEI \\rB, \\rA, (\\imm + 1)",
        -1,
        InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPGTU",
        "rC, rA, rB",
        "CMPLTU \\rC, \\rB, \\rA",
        -1,
        InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPGTUI",
        "rB, rA, imm",
        "CMPGEUI \\rB, \\rA, (\\imm + 1)",
        -1,
        InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPLE", "rC, rA, rB", "CMPGE \\rC, \\rB, \\rA", -1, InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPLEI",
        "rB, rA, imm",
        "CMPLTI \\rB, \\rA, (\\imm + 1)",
        -1,
        InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPLEU",
        "rC, rA, rB",
        "CMPGEU \\rC, \\rB, \\rA",
        -1,
        InstructionSyntax.CATEGORY.COMPARISON);
    addMacro(
        "CMPLEUI",
        "rB, rA, imm",
        "CMPLTUI \\rB, \\rA, (\\imm + 1)",
        -1,
        InstructionSyntax.CATEGORY.COMPARISON);
    addMacro("MOV", "rC, rA", "ADD \\rC, \\rA, r0", -1, InstructionSyntax.CATEGORY.MOVE);
    addMacro("MOVHI", "rB, imm", "ORHI \\rB, r0, \\imm", -1, InstructionSyntax.CATEGORY.MOVE);
    addMacro("MOVI", "rB, imm", "ADDI \\rB, r0, \\imm", -1, InstructionSyntax.CATEGORY.MOVE);
    // The last & 0xFFFF is there because else the value could be 0x10000 which means that
    // it would be out of range
    addMacro(
        "MOVIA",
        "rB, imm",
        "ORHI \\rB, r0, %hiadj(\\imm) & 0xFFFF\nADDI \\rB, \\rB, %lo(\\imm)",
        -1,
        InstructionSyntax.CATEGORY.MOVE);
    addMacro("MOVUI", "rB, imm", "ORI \\rB, r0, \\imm", -1, InstructionSyntax.CATEGORY.MOVE);
    addMacro("NOP", null, "ADD r0, r0, r0", -1, InstructionSyntax.CATEGORY.OTHER);
    addMacro(
        "SUBI",
        "rB, rA, imm",
        "ADDI \\rB, \\rA, -(\\imm)",
        -1,
        InstructionSyntax.CATEGORY.ARITHMETIC_LOGICAL);
  }

  /**
   * Add macro and put it in category list in InstructionManager.
   *
   * @calledby MacroManager()
   * @calls InstructionManager.addMacro()
   * @param name name of the macro
   * @param args arguments separated with ","-character
   * @param lines lines separated with ","-character
   * @param lineNumber line number where the macro is defined
   * @param category instruction category
   */
  private void addMacro(
      String name, String args, String lines, int lineNumber, InstructionSyntax.CATEGORY category) {
    this.put(name, args, lines, lineNumber, category);
  }

  /**
   * Checks if a macro exists
   *
   * @param aName Name of the macro that we check for
   * @return True or false depending of the macro exists or not
   */
  public boolean exists(String aName) {
    return this.nameHash.containsKey(aName.toLowerCase());
  }

  /**
   * Add a macro
   *
   * @calledby MacroManager()
   * @calls put()
   * @param name name of the macro
   * @param args arguments separated with ","-character
   * @param lines lines separated with ","-character
   * @param lineNumber line number where the macro is defined
   */
  public Macro put(
      String name, String args, String lines, int lineNumber, InstructionSyntax.CATEGORY category) {
    String[] argsArray = null;
    if (args != null && args.length() > 0) {
      argsArray = args.split("\\s*,\\s*");
    }

    String[] linesArray = null;
    if (lines != null && lines.length() > 0) linesArray = lines.split("\n");

    return this.put(name, argsArray, linesArray, lineNumber, category);
  }

  /**
   * Add a macro
   *
   * @param name Name of the macro
   * @param args Arguments
   * @param lines Lines
   * @param lineNumber Line number where the macro is defined
   */
  public Macro put(
      String name,
      String[] args,
      String[] lines,
      int lineNumber,
      InstructionSyntax.CATEGORY category) {
    Macro macro = new Macro(name, args, lines, lineNumber, category);
    this.macros.add(macro);
    this.nameHash.put(macro.getName(), macro);
    return macro;
  }

  /**
   * Get macro
   *
   * @param name Name of the macro
   * @return Macro
   */
  public Macro get(String name) throws MacroException {
    return this.nameHash.get(name.toLowerCase());
  }

  public ArrayList<Macro> getAll() {
    return this.macros;
  }
}
