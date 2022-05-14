package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class CallrInstruction extends RTypeInstruction {
  public CallrInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    em.writeRegister(31, em.readPC() + 4);
    int vA = em.readRegister(this.rA);
    em.writePC(vA - 4);
  }
}
