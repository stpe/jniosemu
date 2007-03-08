package jniosemu.emulator.memory;

import java.util.ArrayList;

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
		this.memoryBlocks.add(new MemoryBlock("SRAM", PROGRAMSTART, 50));
		this.memoryBlocks.add(new MemoryBlock("LED_PIO", 0x810, 16));
		this.memoryBlocks.add(new MemoryBlock("BUTTON_PIO", 0x840, 16));
		this.memoryBlocks.add(new MemoryBlock("DIP_PIO", 0x850, 16));

		this.memory = new byte[100];
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
		this.memoryBlocks.add(new MemoryBlock("SRAM", PROGRAMSTART, memory.length));
		this.memoryBlocks.add(new MemoryBlock("LED_PIO", 0x810, 16));
		this.memoryBlocks.add(new MemoryBlock("BUTTON_PIO", 0x840, 16));
		this.memoryBlocks.add(new MemoryBlock("DIP_PIO", 0x850, 16));

		int length = 0;
		for (MemoryBlock block: this.memoryBlocks)
			length += block.getLength();

		this.memory = new byte[length];
		for (int i = 0; i < memory.length; i++)
			this.memory[i] = memory[i];
	}

	/**
	 * Get the internal address for an external address.
	 *
	 * @calledby readByte(), readShort(), readInt(), writeByte(), writeShort(), writeInt()
	 *
	 * @param aAddr  External address
	 * @return Internal address
	 */
	private int mapAddr(int aAddr) throws MemoryException {
		int start = 0;
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.getStart() <= aAddr && block.getEnd() >= aAddr)
				return start + (aAddr - block.getStart());
			start += block.getLength();
		}

		throw new MemoryException(aAddr);
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
	public byte readByte(int aAddr) throws MemoryException {
		return memory[mapAddr(aAddr)];
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
	public void writeByte(int aAddr, byte aValue) throws MemoryException {
		memory[mapAddr(aAddr)] = aValue;
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
	public short readShort(int aAddr) throws MemoryException {
		return (short)((memory[mapAddr(aAddr+1)] & 0xFF) << 8 | (memory[mapAddr(aAddr)] & 0xFF));
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
	public void writeShort(int aAddr, short aValue) throws MemoryException {
		memory[mapAddr(aAddr)  ] = (byte)(aValue        & 0xFF);
		memory[mapAddr(aAddr+1)] = (byte)(aValue >>> 8  & 0xFF);
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
	public int readInt(int aAddr) throws MemoryException {
		return (memory[mapAddr(aAddr+3)] & 0xFF) << 24 | (memory[mapAddr(aAddr+2)] & 0xFF) << 16 | (memory[mapAddr(aAddr+1)] & 0xFF) << 8 | (memory[mapAddr(aAddr)] & 0xFF);
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
	public void writeInt(int aAddr, int aValue) throws MemoryException {
		memory[mapAddr(aAddr)]   = (byte)(aValue        & 0xFF);
		memory[mapAddr(aAddr+1)] = (byte)(aValue >>> 8  & 0xFF);
		memory[mapAddr(aAddr+2)] = (byte)(aValue >>> 16 & 0xFF);
		memory[mapAddr(aAddr+3)] = (byte)(aValue >>> 24 & 0xFF);
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
