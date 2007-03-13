package jniosemu.emulator.register;

import jniosemu.instruction.InstructionManager;

public class Register
{
	public static enum STATE {NONE, READ, WRITE, DISABLED};

	private int number;
	private int value = 0;
	private STATE state = STATE.NONE;

	public Register(int number) {
		this.number = number;
	}

	public void resetState() {
		if (this.state != STATE.DISABLED)
			this.state = STATE.NONE;
	}

	public String getName() {
		return "r"+ Integer.toString(this.number);
	}

	public String getValue() {
		return this.value +" ("+ InstructionManager.intToHexString(this.value) +")";
	}

	public int readValue() throws RegisterException {
		if (this.state == STATE.DISABLED)
			throw new RegisterException(this.number);

		this.state = STATE.READ;
		return this.value;
	}

	public void writeValue(int value) throws RegisterException {
		if (this.state == STATE.DISABLED)
			throw new RegisterException(this.number);

		if (this.number == 0)
			value = 0;

		this.value = value;
		this.state = STATE.WRITE;
	}

	public STATE getState() {
		return this.state;
	}
}
