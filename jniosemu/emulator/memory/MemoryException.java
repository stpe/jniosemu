package jniosemu.emulator.memory;

import jniosemu.Utilities;

public class MemoryException extends RuntimeException {
  public MemoryException() {
    super();
  }

  public MemoryException(int addr) {
    super("Memory Error: Invalid address: " + Utilities.intToHexString(addr));
  }
}
