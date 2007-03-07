package jniosemu.emulator;

import jniosemu.emulator.memory.MemoryManager;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.emulator.Instruction;

public class EmulatorManager
{
	private int pc = MemoryManager.PROGRAMSTART;
	private MemoryManager memory;
	private RegisterManager register = new RegisterManager();
	private InstructionManager instructions = new InstructionManager();
	private Emulator emulator;

	public EmulatorManager(int aPc, byte[] aProgram) {
		this.emulator = new Emulator(this);
		this.memory = new MemoryManager(aProgram);
		if (aPc >= 0)
			this.pc = aPc;
	}

	public int readPC() {
		return this.pc;
	}

	public void writePC(int value) throws EmulatorException {
		if (value % 4 != 0)
			throw new EmulatorException();

		this.pc = value;
	}

	public void run() throws EmulatorException {
		while (this.step());
	}

	public boolean step() throws EmulatorException {
		int opCode = this.memory.readInt(this.pc);
		if (opCode == 0)
			return false;

		Instruction instruction = this.instructions.get(opCode);
		instruction.run(this.emulator);
		this.pc += 4;

		return true;
	}

	public MemoryManager getMemoryManager() {
		return this.memory;
	}

	public RegisterManager getRegisterManager() {
		return this.register;
	}

	public void dump() {
		this.register.dump();
		this.memory.dump();
	}
}
