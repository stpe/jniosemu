package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class RolInstruction extends RTypeInstruction {
  public RolInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    int vB = em.readRegister(this.rB);
    vB = vB & 0xF; // Lowest 4 bits.
    em.writeRegister(this.rC, Integer.rotateLeft(vA, vB));
  }
}
