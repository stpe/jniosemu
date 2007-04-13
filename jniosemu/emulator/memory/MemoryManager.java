package jniosemu.emulator.memory;

import java.util.ArrayList;
import java.util.HashMap;
import jniosemu.emulator.memory.io.*;
import jniosemu.events.EventManager;

/**
 * Manage the memory that the emulated program can access.
 */
public class MemoryManager
{
	/**
	 * Address in the memory where the program is placed.
	 */
	public static final int PROGRAMSTARTADDR  = 0x20000;
	/**
	 * Address in the memory where the variables is placed.
	 */
	public static final int VARIABLESTARTADDR = 0x30000;
	/**
	 * Address in the memory where the stack is placed.
	 */
	public static final int STACKSTARTADDR    = 0x40000;
	/**
	 * Size of the stack
	 */
	public static final int STACKSIZE         = 0x00020;
	/**
	 * Contains the different MemoryBlocks
	 */
	private ArrayList<MemoryBlock> memoryBlocks = new ArrayList<MemoryBlock>();

	/**
	 * Init MemoryManager with program.
	 *
	 * @post add program, variables and stack MemoryBlock
	 * @calledby EmulatorManager()
	 * @calls MemoryBlocks()
	 *
	 * @param program Program
	 * @param variables Variables
	 */
	public MemoryManager(EventManager eventManager, byte[] program, byte[] variables)
	{
		this.memoryBlocks.add(new Memory("PROGRAM", PROGRAMSTARTADDR, program.length, program));
		this.memoryBlocks.add(new Memory("VARIABLES", VARIABLESTARTADDR, variables.length, variables));
		this.memoryBlocks.add(new Memory("STACK", (STACKSTARTADDR - STACKSIZE), STACKSIZE, null));

		this.memoryBlocks.add(new ButtonDevice(eventManager, this));
		this.memoryBlocks.add(new LedDevice(eventManager, this));
		this.memoryBlocks.add(new DipswitchDevice(eventManager, this));
		this.memoryBlocks.add(new SerialDevice(eventManager, this));
		this.memoryBlocks.add(new TimerDevice(eventManager, this));
	}

	public void reset(byte[] program, byte[] variables) {
		for (MemoryBlock memoryBlock : this.memoryBlocks)
			memoryBlock.reset();

		this.memoryBlocks.set(0, new Memory("PROGRAM", PROGRAMSTARTADDR, program.length, program));
		this.memoryBlocks.set(1, new Memory("VARIABLES", VARIABLESTARTADDR, variables.length, variables));
		this.memoryBlocks.set(2, new Memory("STACK", (STACKSTARTADDR - STACKSIZE), STACKSIZE, null));
	}

	public void resetState() {
		for (MemoryBlock memoryBlock : this.memoryBlocks)
			memoryBlock.resetState();
	}

	/**
	 * Read one byte from memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param notify True if the device should be notified
	 * @return One byte from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public byte readByte(int addr) throws MemoryException {
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.inRange(addr)) {
				return block.readByte(addr);
			}
		}

		throw new MemoryException(addr);
	}

	/**
	 * Write one byte to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param notify True if the device should be notified
	 * @param value  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeByte(int addr, byte value) throws MemoryException {
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.inRange(addr)) {
				block.writeByte(addr, value);
				return;
			}
		}

		throw new MemoryException(addr);		
	}

	/**
	 * Read one short from memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param notify True if the device should be notified
	 * @return One short from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public short readShort(int addr) throws MemoryException {
		return (short)((this.readByte(addr+1) & 0xFF) << 8 | (this.readByte(addr) & 0xFF));
	}

	/**
	 * Write one short to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param value  Value
	 * @param notify True if the device should be notified
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeShort(int addr, short value) throws MemoryException {
		this.writeByte(addr    , (byte)(value       & 0xFF));
		this.writeByte(addr + 1, (byte)(value >>> 8 & 0xFF));
	}

	/**
	 * Read one int from memory.
	 *
	 * @calledby Instruction, EmulatorManager
	 *
	 * @param addr  External address
	 * @param notify True if the device should be notified
	 * @return One int from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public int readInt(int addr) throws MemoryException {
		return (this.readByte(addr+3) & 0xFF) << 24 | (this.readByte(addr+2) & 0xFF) << 16 | (this.readByte(addr+1) & 0xFF) << 8 | (this.readByte(addr) & 0xFF);
	}

	/**
	 * Write one int to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param notify True if the device should be notified
	 * @param value  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeInt(int addr, int value) throws MemoryException {
		this.writeByte(addr    , (byte)(value        & 0xFF));
		this.writeByte(addr + 1, (byte)(value >>> 8  & 0xFF));
		this.writeByte(addr + 2, (byte)(value >>> 16 & 0xFF));
		this.writeByte(addr + 3, (byte)(value >>> 24 & 0xFF));
	}

	public ArrayList<MemoryBlock> getMemoryBlocks() {
		return this.memoryBlocks;
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
