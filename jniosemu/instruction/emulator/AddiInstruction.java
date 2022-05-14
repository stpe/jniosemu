package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class AddiInstruction extends ITypeInstruction {
  public AddiInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    em.writeRegister(this.rB, vA + this.imm);
  }
}
