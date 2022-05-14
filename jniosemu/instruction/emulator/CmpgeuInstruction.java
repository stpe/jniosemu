package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpgeuInstruction extends RTypeInstruction {
  public CmpgeuInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    if (this.signedToUnsigned(em.readRegister(this.rA))
        >= this.signedToUnsigned(em.readRegister(this.rB))) {
      em.writeRegister(this.rC, 1);
    } else {
      em.writeRegister(this.rC, 0);
    }
  }
}
