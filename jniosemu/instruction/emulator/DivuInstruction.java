package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class DivuInstruction extends RTypeInstruction {
  public DivuInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    if (em.readRegister(this.rB) == 0) throw new EmulatorException("Can't divide by zero");

    em.writeRegister(
        this.rC,
        (int)
            (this.signedToUnsigned(em.readRegister(this.rA))
                / this.signedToUnsigned(em.readRegister(this.rB))));
  }
}
