package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class MulxsuInstruction extends RTypeInstruction {
  public MulxsuInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    long vA = (long) em.readRegister(this.rA);
    long vB = this.signedToUnsigned(em.readRegister(this.rB));
    long vC = (vA * vB) >>> 32;
    em.writeRegister(this.rC, (int) vC);
  }
}
