package jniosemu.emulator.memory;

import java.util.ArrayList;
import jniosemu.emulator.io.IODevice;

/**
 * Manage the memory that the emulated program can access.
 */
public class MemoryManager
{
	/**
	 * Address in the memory where the program is placed.
	 */
	public static int PROGRAMSTARTADDR  = 0x20000;
	/**
	 * Address in the memory where the variables is placed.
	 */
	public static int VARIABLESTARTADDR = 0x30000;
	/**
	 * Address in the memory where the stack is placed.
	 */
	public static int STACKSTARTADDR    = 0x40000;
	/**
	 * Size of the stack
	 */
	public static int STACKSIZE         = 0x08000;
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
	 * @param memory Program
	 */
	public MemoryManager(byte[] program, byte[] variables)
	{
		this.memoryBlocks.add(new MemoryBlock("PROGRAM", PROGRAMSTARTADDR, program.length, null, program));
		this.memoryBlocks.add(new MemoryBlock("VARIABLES", VARIABLESTARTADDR, variables.length, null, variables));
		this.memoryBlocks.add(new MemoryBlock("STACK", (STACKSTARTADDR - STACKSIZE), STACKSIZE, null, null));
	}

	/**
	 * Register a part of memory
	 *
	 * @calledby IODevice.reset()
	 *
	 * @param name Name of the MemoryBlock
	 * @param startAddr Memory start address
	 * @param length Length of the MemoryBlock
	 * @param device Device
	 */
	public void register(String name, int startAddr, int length, IODevice device) {
		this.memoryBlocks.add(new MemoryBlock(name, startAddr, length, device, null));
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
	public byte readByte(int addr, boolean notify) throws MemoryException {
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.inRange(addr)) {
				// System.out.println("read: "+ block.getName() +" ("+ addr +")");
				return block.readByte(addr, notify);
			}
		}

		throw new MemoryException(addr);		
	}

	/**
	 * Read one byte from memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @return One byte from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public byte readByte(int addr) throws MemoryException {
		return readByte(addr, true);
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
	public void writeByte(int addr, byte value, boolean notify) throws MemoryException {
		for (MemoryBlock block: this.memoryBlocks) {
			if (block.inRange(addr)) {
				block.writeByte(addr, value, notify);
				return;
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
	 * @param value  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeByte(int addr, byte value) throws MemoryException {
		writeByte(addr, value, true);
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
	public short readShort(int addr, boolean notify) throws MemoryException {
		return (short)((this.readByte(addr+1, notify) & 0xFF) << 8 | (this.readByte(addr, notify) & 0xFF));
	}

	/**
	 * Read one short from memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @return One short from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public short readShort(int addr) throws MemoryException {
		return readShort(addr, true);
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
	public void writeShort(int addr, short value, boolean notify) throws MemoryException {
		this.writeByte(addr    , (byte)(value       & 0xFF), notify);
		this.writeByte(addr + 1, (byte)(value >>> 8 & 0xFF), notify);
	}

	/**
	 * Write one short to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param value  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeShort(int addr, short value) throws MemoryException {
		writeShort(addr, value, true);
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
	public int readInt(int addr, boolean notify) throws MemoryException {
		return (this.readByte(addr+3, notify) & 0xFF) << 24 | (this.readByte(addr+2, notify) & 0xFF) << 16 | (this.readByte(addr+1, notify) & 0xFF) << 8 | (this.readByte(addr, notify) & 0xFF);
	}

	/**
	 * Read one int from memory.
	 *
	 * @calledby Instruction, EmulatorManager
	 *
	 * @param addr  External address
	 * @return One int from the memory
	 * @throws MemoryException  If the address is wrong
	 */
	public int readInt(int addr) throws MemoryException {
		return readInt(addr, true);
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
	public void writeInt(int addr, int value, boolean notify) throws MemoryException {
		this.writeByte(addr    , (byte)(value        & 0xFF), notify);
		this.writeByte(addr + 1, (byte)(value >>> 8  & 0xFF), notify);
		this.writeByte(addr + 2, (byte)(value >>> 16 & 0xFF), notify);
		this.writeByte(addr + 3, (byte)(value >>> 24 & 0xFF), notify);
	}

	/**
	 * Write one int to memory.
	 *
	 * @calledby Instruction
	 *
	 * @param addr  External address
	 * @param value  Value
	 * @throws MemoryException  If the address is wrong
	 */
	public void writeInt(int addr, int value) throws MemoryException {
		writeInt(addr, value, true);
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
