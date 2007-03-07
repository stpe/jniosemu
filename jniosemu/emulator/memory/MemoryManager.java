package jniosemu.emulator.memory;

import java.util.ArrayList;

public class MemoryManager
{
	public static int PROGRAMSTART = 0x20000;

	private byte[] memory;
	private ArrayList<MemoryBlock> memoryBlocks = new ArrayList<MemoryBlock>();

	public MemoryManager() {
		this.memoryBlocks.add(new MemoryBlock("SRAM", PROGRAMSTART, 50));
		this.memoryBlocks.add(new MemoryBlock("LED_PIO", 0x810, 16));
		this.memoryBlocks.add(new MemoryBlock("BUTTON_PIO", 0x840, 16));
		this.memoryBlocks.add(new MemoryBlock("DIP_PIO", 0x850, 16));

		this.memory = new byte[100];
	}

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

	private int mapAddr(int aAddr) throws MemoryException {
		int start = 0;
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.getStart() <= aAddr && block.getEnd() >= aAddr)
				return start + (aAddr - block.getStart());
			start += block.getLength();
		}

		throw new MemoryException(aAddr);
	}

	public byte readByte(int aAddr) throws MemoryException {
		return memory[mapAddr(aAddr)];
	}

	public void writeByte(int aAddr, byte aValue) throws MemoryException {
		memory[mapAddr(aAddr)] = aValue;
	}

	public short readShort(int aAddr) throws MemoryException {

		return (short)((memory[mapAddr(aAddr+1)] & 0xFF) << 8 | (memory[mapAddr(aAddr)] & 0xFF));
	}

	public void writeShort(int aAddr, short aValue) throws MemoryException {
		memory[mapAddr(aAddr)  ] = (byte)(aValue        & 0xFF);
		memory[mapAddr(aAddr+1)] = (byte)(aValue >>> 8  & 0xFF);
	}

	public int readInt(int aAddr) throws MemoryException {
		return (memory[mapAddr(aAddr+3)] & 0xFF) << 24 | (memory[mapAddr(aAddr+2)] & 0xFF) << 16 | (memory[mapAddr(aAddr+1)] & 0xFF) << 8 | (memory[mapAddr(aAddr)] & 0xFF);
	}

	public void writeInt(int aAddr, int aValue) throws MemoryException {
		memory[mapAddr(aAddr)]   = (byte)(aValue        & 0xFF);
		memory[mapAddr(aAddr+1)] = (byte)(aValue >>> 8  & 0xFF);
		memory[mapAddr(aAddr+2)] = (byte)(aValue >>> 16 & 0xFF);
		memory[mapAddr(aAddr+3)] = (byte)(aValue >>> 24 & 0xFF);
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
