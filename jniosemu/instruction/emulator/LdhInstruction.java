package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdhInstruction extends ITypeInstruction {
  public LdhInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    em.writeRegister(this.rB, em.readShortMemory(em.readRegister(this.rA) + this.imm));
  }
}
