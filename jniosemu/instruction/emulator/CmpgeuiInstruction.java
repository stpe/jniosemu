package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CmpgeuiInstruction extends ITypeInstruction {
  public CmpgeuiInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    if (this.signedToUnsigned(em.readRegister(this.rA)) >= this.signedToUnsigned(this.imm)) {
      em.writeRegister(this.rB, 1);
    } else {
      em.writeRegister(this.rB, 0);
    }
  }
}
