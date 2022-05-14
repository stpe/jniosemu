package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class AddInstruction extends RTypeInstruction {
  public AddInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    int vB = em.readRegister(this.rB);
    em.writeRegister(this.rC, vA + vB);
  }
}
