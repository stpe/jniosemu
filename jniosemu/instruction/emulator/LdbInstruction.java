package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdbInstruction extends ITypeInstruction {
  public LdbInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    em.writeRegister(this.rB, em.readByteMemory(em.readRegister(this.rA) + this.imm));
  }
}
