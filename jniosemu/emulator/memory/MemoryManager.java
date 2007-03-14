package jniosemu.emulator.memory;

import java.util.ArrayList;
import jniosemu.emulator.io.IODevice;

/**
 * Manage the memory that the emulated program can access.
 */
public class MemoryManager
{
	/**
	 * The default program start memory address.
	 */
	public static int PROGRAMSTART = 0x20000;

	/**
	 * The memory
	 */
	private byte[] memory;
	/**
	 * Info about the different parts of memory.
	 */
	private ArrayList<MemoryBlock> memoryBlocks = new ArrayList<MemoryBlock>();

	/**
	 * Init MemoryManager without any program.
	 *
	 * @post memory and memoryBlocks is set
	 * @calls MemoryBlocks()
	 */
	public MemoryManager() {
		this.memoryBlocks.add(new MemoryBlock("SRAM", PROGRAMSTART, 50, null, null));
	}

	/**
	 * Init MemoryManager with program.
	 *
	 * @post memory and memoryBlocks is set
	 * @calledby EmulatorManager
	 * @calls MemoryBlocks()
	 *
	 * @param memory Program
	 */
	public MemoryManager(byte[] memory)
	{
		this.memoryBlocks.add(new MemoryBlock("SRAM", PROGRAMSTART, memory.length, null, memory));
	}

	/**
	 *
	 */
	public void register(String name, int startAddr, int length, IODevice device) {
		this.memoryBlocks.add(new MemoryBlock(name, startAddr, length, device, null));
	}

	/**
	 * Read one byte from memory.
	 *
	 * @calledby Instruction
	 *
	 * @param aAddr  External address
	 * @return One byte from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public byte readByte(int addr, boolean notify) throws MemoryException {
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.inRange(addr)) {
				// System.out.println("read: "+ block.getName() +" ("+ addr +")");
				return block.readByte(addr, notify);
			}
		}

		throw new MemoryException(addr);		
	}

	public byte readByte(int addr) throws MemoryException {
		return readByte(addr, true);
	}

	/**
	 * Write one byte to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param aAddr  External address
	 * @param aValue  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeByte(int addr, byte value, boolean notify) throws MemoryException {
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.inRange(addr)) {
				block.writeByte(addr, value, notify);
				return;
			}
		}

		throw new MemoryException(addr);		
	}

	public void writeByte(int addr, byte value) throws MemoryException {
		writeByte(addr, value, true);
	}

	/**
	 * Read one short from memory.
	 *
	 * @calledby Instruction
	 *
	 * @param aAddr  External address
	 * @return One short from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public short readShort(int aAddr, boolean notify) throws MemoryException {
		return (short)((this.readByte(aAddr+1, notify) & 0xFF) << 8 | (this.readByte(aAddr, notify) & 0xFF));
	}

	public short readShort(int addr) throws MemoryException {
		return readShort(addr, true);
	}

	/**
	 * Write one short to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param aAddr  External address
	 * @param aValue  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeShort(int aAddr, short aValue, boolean notify) throws MemoryException {
		this.writeByte(aAddr    , (byte)(aValue       & 0xFF), notify);
		this.writeByte(aAddr + 1, (byte)(aValue >>> 8 & 0xFF), notify);
	}

	public void writeShort(int addr, short value) throws MemoryException {
		writeShort(addr, value, true);
	}

	/**
	 * Read one int from memory.
	 *
	 * @calledby Instruction, EmulatorManager
	 *
	 * @param aAddr  External address
	 * @return One int from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public int readInt(int aAddr, boolean notify) throws MemoryException {
		return (this.readByte(aAddr+3, notify) & 0xFF) << 24 | (this.readByte(aAddr+2, notify) & 0xFF) << 16 | (this.readByte(aAddr+1, notify) & 0xFF) << 8 | (this.readByte(aAddr, notify) & 0xFF);
	}

	public int readInt(int addr) throws MemoryException {
		return readInt(addr, true);
	}

	/**
	 * Write one int to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param aAddr  External address
	 * @param aValue  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeInt(int aAddr, int aValue, boolean notify) throws MemoryException {
		this.writeByte(aAddr    , (byte)(aValue        & 0xFF), notify);
		this.writeByte(aAddr + 1, (byte)(aValue >>> 8  & 0xFF), notify);
		this.writeByte(aAddr + 2, (byte)(aValue >>> 16 & 0xFF), notify);
		this.writeByte(aAddr + 3, (byte)(aValue >>> 24 & 0xFF), notify);
	}

	public void writeInt(int addr, int value) throws MemoryException {
		writeInt(addr, value, true);
	}

	/**
	 * Reseting the part of the memory that is changed during emulation
	 *
	 * @calledby EmulatorManager.reset()
	 */
	public void reset() {
		
	}

	public void dump() {
		int start = 0;
		for (MemoryBlock block: this.memoryBlocks) {
			System.out.println(block.getName());
			for (int addr = block.getStart(); addr <= block.getEnd(); addr += 4) {
				System.out.println(Integer.toBinaryString(this.readInt(addr)));
			}
		}
	}
}
