package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class RorInstruction extends RTypeInstruction {
  public RorInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    int vB = em.readRegister(this.rB);
    vB = vB & 0xF; // Lowest 4 bits.
    em.writeRegister(this.rC, Integer.rotateRight(vA, vB));
  }
}
