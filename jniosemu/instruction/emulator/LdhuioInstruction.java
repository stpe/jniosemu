package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class LdhuioInstruction extends ITypeInstruction {
  public LdhuioInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int vA = em.readRegister(this.rA);
    int vR = signedToUnsigned(em.readShortMemory(vA + this.imm));

    em.writeRegister(this.rB, vR);
  }
}
