package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class StbInstruction extends ITypeInstruction {
  public StbInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    byte vB = (byte) em.readRegister(this.rB);
    em.writeByteMemory(vA + this.imm, vB);
  }
}
