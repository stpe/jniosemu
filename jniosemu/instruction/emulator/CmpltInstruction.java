package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpltInstruction extends RTypeInstruction {
  public CmpltInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    if (em.readRegister(this.rA) < em.readRegister(this.rB)) {
      em.writeRegister(this.rC, 1);
    } else {
      em.writeRegister(this.rC, 0);
    }
  }
}
