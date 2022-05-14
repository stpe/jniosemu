package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class XorhiInstruction extends ITypeInstruction {
  public XorhiInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    em.writeRegister(this.rB, vA ^ (this.imm << 16));
  }
}
