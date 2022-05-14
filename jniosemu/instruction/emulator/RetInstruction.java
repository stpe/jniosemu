package jniosemu.instruction.emulator;

import jniosemu.emulator.Emulator;
import jniosemu.emulator.EmulatorException;

public class RetInstruction extends RTypeInstruction {
  public RetInstruction(int opCode) {
    super(opCode);
  }

  public void run(Emulator em) throws EmulatorException {
    int ra = em.readRegister(31);
    em.writePC(ra - 4);
  }
}
