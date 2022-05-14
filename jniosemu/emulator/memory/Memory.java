package jniosemu.emulator.memory;

import jniosemu.emulator.SourceCode;

/** Contains a part of the memory. */
public class Memory extends MemoryBlock {
  /** Contains how the memory looked when init */
  private byte[] originalMemory;

  /**
   * Init Memory.
   *
   * @calledby MemoryManager
   * @param name Name of the part
   * @param start External start address
   * @param length Length of the memory part
   */
  public Memory(String name, int start, int length, byte[] memory, SourceCode sourceCode) {
    this.name = name;
    this.start = start;
    this.length = length;

    this.originalMemory = memory;
    this.sourceCode = sourceCode;
    this.reset();
  }

  public byte readByte(int addr) throws MemoryException {
    byte value = 0;
    int mapAddr = this.mapAddr(addr);
    try {
      value = memory[mapAddr];
    } catch (Exception e) {
      throw new MemoryException(addr);
    }

    this.setState(mapAddr, MemoryInt.STATE.READ);
    return value;
  }

  public void writeByte(int addr, byte value) throws MemoryException {
    int mapAddr = this.mapAddr(addr);
    try {
      memory[mapAddr] = value;
    } catch (Exception e) {
      throw new MemoryException(addr);
    }

    this.sourceCode = null;
    this.setState(mapAddr, MemoryInt.STATE.WRITE);
  }

  public void reset() {
    this.resetState();

    this.changed = 0;
    this.memory = new byte[this.length];

    if (this.originalMemory != null)
      System.arraycopy(this.originalMemory, 0, this.memory, 0, this.originalMemory.length);
  }

  public boolean resetState() {
    this.clearState();
    return false;
  }
}
