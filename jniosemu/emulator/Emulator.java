package jniosemu.emulator;

import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.register.RegisterException;

/** Used by the Instructions so they only get access to what they need. */
public class Emulator {
  /** EmulatorManager which is used to access all methods */
  private EmulatorManager emulator;

  /**
   * Init Emulator
   *
   * @post emulator is set
   * @calledby EmulatorManager()
   * @param em EmulatorManager which this is using
   */
  public Emulator(EmulatorManager em) {
    this.emulator = em;
  }
  ;

  /**
   * Read the value from a register
   *
   * @calledby Instruction.run()
   * @calls RegisterManager.read()
   * @param index Index of the register
   * @return Value of the register
   * @throws RegisterException If register index don't exists
   */
  public int readRegister(int index) throws RegisterException {
    return this.emulator.getRegisterManager().read(index);
  }

  /**
   * Write a value to a register
   *
   * @calledby Instruction.run()
   * @calls RegisterManager.write()
   * @param index Index of the register
   * @param value Value which is written to the register
   * @throws RegisterException If the register index don't exists
   */
  public void writeRegister(int index, int value) throws RegisterException {
    this.emulator.getRegisterManager().write(index, value);
  }

  /**
   * Read a byte from the memory
   *
   * @calledby Instruction.run()
   * @calls MemoryManager.readByte()
   * @param addr Memory address
   * @return Byte that was read
   * @throws MemoryException If the memory address isn't accessible
   */
  public byte readByteMemory(int addr) throws MemoryException {
    return this.emulator.getMemoryManager().readByte(addr);
  }

  /**
   * Write a byte to the memory
   *
   * @calledby Instruction.run()
   * @calls MemoryManager.writeByte()
   * @param addr Memory address
   * @param value Value which is written to memory
   * @throws MemoryException If the memory address isn't accessible
   */
  public void writeByteMemory(int addr, byte value) throws MemoryException {
    this.emulator.getMemoryManager().writeByte(addr, value);
  }

  /**
   * Read a short from the memory
   *
   * @calledby Instruction.run()
   * @calls MemoryManager.readShort()
   * @param addr Memory address
   * @return Short that was read
   * @throws MemoryException If the memory address isn't accessible
   */
  public short readShortMemory(int addr) throws MemoryException {
    return this.emulator.getMemoryManager().readShort(addr);
  }

  /**
   * Write a short to the memory
   *
   * @calledby Instruction.run()
   * @calls MemoryManager.writeShort()
   * @param addr Memory address
   * @param value Value which is written to memory
   * @throws MemoryException If the memory address isn't accessible
   */
  public void writeShortMemory(int addr, short value) throws MemoryException {
    this.emulator.getMemoryManager().writeShort(addr, value);
  }

  /**
   * Read a int from the memory
   *
   * @calledby Instruction.run()
   * @calls MemoryManager.readInt()
   * @param addr Memory address
   * @return Int that was read
   * @throws MemoryException If the memory address isn't accessible
   */
  public int readIntMemory(int addr) throws MemoryException {
    return this.emulator.getMemoryManager().readInt(addr);
  }

  /**
   * Write a int to the memory
   *
   * @calledby Instruction.run()
   * @calls MemoryManager.writeInt()
   * @param addr Memory address
   * @param value Value which is written to memory
   * @throws MemoryException If the memory address isn't accessible
   */
  public void writeIntMemory(int addr, int value) throws MemoryException {
    this.emulator.getMemoryManager().writeInt(addr, value);
  }

  /**
   * Get the PC address
   *
   * @calledby Instruction.run()
   * @calls EmulatorManager.readPC()
   * @return PC address
   */
  public int readPC() {
    return this.emulator.readPC();
  }

  /**
   * Set PC address
   *
   * @calledby Instruction.run()
   * @calls EmulatorManager.writePC()
   * @param addr PC address
   * @throws EmulatorException If the memory address isn't correct
   */
  public void writePC(int addr) throws EmulatorException {
    this.emulator.writePC(addr);
  }
}
