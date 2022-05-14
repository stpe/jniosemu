package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class SrlInstruction extends RTypeInstruction {
  public SrlInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vB = em.readRegister(this.rB) & 0xF; // we only want the lower-4-bits.
    em.writeRegister(this.rC, em.readRegister(this.rA) >>> vB);
  }
}
