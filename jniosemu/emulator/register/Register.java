package jniosemu.emulator.register;

import jniosemu.emulator.memory.MemoryManager;
import jniosemu.Utilities;

/**
 * Contains info about one register
 */
public class Register
{
	/**
	 * The different state a register could be
	 */
	public static enum STATE {NONE, READ, WRITE, DISABLED};
	/**
	 * Register number
	 */
	private int number;
	/**
	 * Value
	 */
	private int value = 0;
	/**
	 * State
	 */
	private STATE state = STATE.NONE;

	/**
	 * Init register
	 *
	 * @post Set number, state and value.
	 * @checks if register isn't implemented set state to DISABLED
	 * @calledby RegisterManager
	 *
	 * @param number  Number of the regiser
	 */
	public Register(int number) {
		this.number = number;

		if (!(this.number == 31 || this.number == 27 || this.number == 28 || (this.number >= 0 && this.number <= 23)))
			this.state = STATE.DISABLED;

		if (this.number == 27)
			this.value = MemoryManager.STACKSTARTADDR;
	}

	/**
	 * Reset state
	 *
	 * @checks Set state to NONE if state != DISABLED
	 * @calledby RegisterManager
	 */
	public void resetState() {
		if (this.state != STATE.DISABLED)
			this.state = STATE.NONE;
	}

	/**
	 * Get name
	 *
	 * @calledby GUIRegister
	 *
	 * @return name
	 */
	public String getName() {
		return "r"+ Integer.toString(this.number);
	}

	/**
	 * Get integer representation of value
	 *
	 * @calledby GUIRegister
	 *
	 * @return value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Get string representation of value
	 *
	 * @calledby GUIRegister
	 * @calls Utilities.intToHexString()
	 *
	 * @return value
	 */
	public String getValueAsString() {
		return Utilities.intToHexString(this.value);
	}

	/**
	 * Read value and update state
	 *
	 * @calledby RegisterManager
	 *
	 * @return value
	 */
	public int readValue() throws RegisterException {
		if (this.state == STATE.DISABLED)
			throw new RegisterException(this.number);

		this.state = STATE.READ;
		return this.value;
	}

	/**
	 * Write value and update state
	 *
	 * @checks If number = 0 don't change value
	 *
	 * @param value  new value
	 */
	public void writeValue(int value) throws RegisterException {
		if (this.state == STATE.DISABLED)
			throw new RegisterException(this.number);

		if (this.number == 0)
			value = 0;

		this.value = value;
		this.state = STATE.WRITE;
	}

	/**
	 * Get state
   *
	 * @return state
	 */
	public STATE getState() {
		return this.state;
	}
}
