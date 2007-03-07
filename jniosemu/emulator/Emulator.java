package jniosemu.emulator;

import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.register.RegisterException;

public class Emulator
{
	private EmulatorManager emulator;

	public Emulator(EmulatorManager em) {
		this.emulator = em;
	};

	public int readRegister(int index) throws RegisterException {
		return this.emulator.getRegisterManager().read(index);
	}

	public void writeRegister(int index, int value) throws RegisterException {
		this.emulator.getRegisterManager().write(index, value);
	}

	public byte readByteMemory(int addr) throws MemoryException {
		return this.emulator.getMemoryManager().readByte(addr);
	}

	public void writeByteMemory(int addr, byte value) throws MemoryException {
		this.emulator.getMemoryManager().writeByte(addr, value);
	}

	public short readShortMemory(int addr) throws MemoryException {
		return this.emulator.getMemoryManager().readShort(addr);
	}

	public void writeShortMemory(int addr, short value) throws MemoryException {
		this.emulator.getMemoryManager().writeShort(addr, value);
	}

	public int readIntMemory(int addr) throws MemoryException {
		return this.emulator.getMemoryManager().readInt(addr);
	}

	public void writeIntMemory(int addr, int value) throws MemoryException {
		this.emulator.getMemoryManager().writeInt(addr, value);
	}

	public int readPC() {
		return this.emulator.readPC();
	}

	public void writePC(int addr) throws EmulatorException {
		this.emulator.writePC(addr);
	}
}
