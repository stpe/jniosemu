package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class AndiInstruction extends ITypeInstruction {
  public AndiInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    int vI = this.imm & 0xFFFF; // Lower 16 bits.
    em.writeRegister(this.rB, vA & this.imm);
  }
}
