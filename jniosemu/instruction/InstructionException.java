package jniosemu.instruction;

import jniosemu.emulator.EmulatorException;

public class InstructionException extends EmulatorException {
  public InstructionException() {
    super();
  }

  public InstructionException(int opCode) {
    super(Integer.toBinaryString(opCode) + ": Couldn't find any class for it");
  }

  public InstructionException(int opCode, String msg) {
    super(Integer.toBinaryString(opCode) + ": " + msg);
  }

  public InstructionException(String instructionName) {
    super(instructionName + ": Couldn't find any class for it");
  }

  public InstructionException(String aInstructionName, String aMsg) {
    super(aInstructionName + ": " + aMsg);
  }
}
