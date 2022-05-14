package jniosemu.emulator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import jniosemu.Utilities;
import jniosemu.instruction.compiler.CompilerInstruction;

public class SourceCode {
  /** Start address */
  private final int startAddr;

  private Vector<SourceCodeLine> sourceCodeLines = new Vector<SourceCodeLine>();
  /** Used for fast get linenumber from address */
  private Hashtable<Integer, Integer> addrLookup = new Hashtable<Integer, Integer>();
  /** Used for fast get address from linenumber */
  private Hashtable<Integer, Integer> lineNumberLookup = new Hashtable<Integer, Integer>();

  public SourceCode(byte[] binaryCode, int startAddr) {
    this.startAddr = startAddr;
    SourceCodeLine sourceCodeLine = null;

    for (int i = 0; i < binaryCode.length / 4; i++) {
      int opCode = Utilities.byteArrayToInt(binaryCode, i * 4);

      sourceCodeLine = new SourceCodeLine(opCode, i, null);
      this.sourceCodeLines.add(sourceCodeLine);

      this.addrLookup.put(i * 4, i);
      this.lineNumberLookup.put(i, i * 4);
    }
  }

  public SourceCode(
      String[] sourceCode, ArrayList<CompilerInstruction> instructions, int startAddr) {
    this.startAddr = startAddr;

    int sourceCodeLineNumber = 1;
    int addr = 0;
    int sourceCodeLineCount = 0;
    SourceCodeLine sourceCodeLineParent = null;
    SourceCodeLine sourceCodeLine = null;

    CompilerInstruction compilerInstruction = null;
    Iterator instructionIterator = instructions.iterator();
    if (instructionIterator.hasNext())
      compilerInstruction = (CompilerInstruction) instructionIterator.next();

    for (String line : sourceCode) {
      if (compilerInstruction != null
          && compilerInstruction.getLineNumber() == sourceCodeLineNumber) {
        do {
          sourceCodeLine =
              new SourceCodeLine(
                  compilerInstruction, line, sourceCodeLineCount, sourceCodeLineParent);
          this.sourceCodeLines.add(sourceCodeLine);

          if (sourceCodeLineParent == null) sourceCodeLineParent = sourceCodeLine;
          else sourceCodeLineParent.incrChildCount();

          this.addrLookup.put(addr, sourceCodeLineCount);
          this.lineNumberLookup.put(sourceCodeLineCount, addr);

          addr += 4;
          sourceCodeLineCount++;
          compilerInstruction = null;
          line = null;
          if (instructionIterator.hasNext())
            compilerInstruction = (CompilerInstruction) instructionIterator.next();
        } while (compilerInstruction != null
            && compilerInstruction.getLineNumber() == sourceCodeLineNumber);
      } else {
        this.sourceCodeLines.add(new SourceCodeLine(null, line, sourceCodeLineCount, null));
        sourceCodeLineCount++;
      }

      sourceCodeLineParent = null;
      sourceCodeLineNumber++;
    }
  }

  /**
   * Return an arraylist of programlines
   *
   * @return Arraylist of ProgramLine:s
   */
  public Vector<SourceCodeLine> getSourceCodeLines() {
    return this.sourceCodeLines;
  }

  /**
   * Return the linenumber of an address
   *
   * @calledby Program()
   * @param address Memory address
   * @return Linenumber
   */
  public int getLineNumber(int address) {
    address = address - this.startAddr;
    if (this.addrLookup.containsKey(address)) return this.addrLookup.get(address);

    return -1;
  }

  /**
   * Return the memory address where a line is placed
   *
   * @calledby EmulatorManager.toggleBreakpoint()
   * @param lineNumber line number
   * @return memory address
   */
  public int getAddress(int lineNumber) {
    if (this.lineNumberLookup.containsKey(lineNumber))
      return this.lineNumberLookup.get(lineNumber) + this.startAddr;

    return -1;
  }

  /**
   * Toggle breakpoint
   *
   * @checks Just don't do anything if the line don't exists
   * @calledby EmulatorManager.toggleBreakpoint()
   * @calls ProgramLine.toggleBreakpoint()
   * @param lineNumber lineNumber to toggle breakpoint
   * @return true if the breakpoint is set
   */
  public boolean toggleBreakpoint(int lineNumber) {
    try {
      return this.sourceCodeLines.get(lineNumber).toggleBreakpoint();
    } catch (Exception e) {
    }

    return false;
  }
}
