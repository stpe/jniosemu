package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdwioInstruction extends ITypeInstruction {
  public LdwioInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    em.writeRegister(this.rB, em.readIntMemory(vA + this.imm));
  }
}
