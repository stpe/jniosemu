package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class MuliInstruction extends ITypeInstruction {
  public MuliInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    em.writeRegister(this.rB, em.readRegister(this.rA) * this.imm);
  }
}
