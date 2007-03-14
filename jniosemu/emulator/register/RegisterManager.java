package jniosemu.emulator.register;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Vector;

/**
 * Manage all register
 */
public class RegisterManager
{
	/**
	 * contains the values of the registers
	 */
	private Vector<Register> registers;
	// private int[] register = new int[32];

	/**
	 * Init RegisterManager
	 *
	 * @post Populate this.register
	 */
	public RegisterManager() {
		this.reset();
	}

	/**
	 * Parse a register name
	 *
	 * @param aRegister  Name of the register
	 * @return index of the register
	 * @throws RegisterException  If the index is not an integer
	 */
	public static int parseRegister(String aRegister) throws RegisterException {
		if (aRegister.equals("zero")) {
			return 0;
		} else if (aRegister.equals("at")) {
			return 1;
		} else if (aRegister.equals("sp")) {
			return 27;
		} else if (aRegister.equals("ra")) {
			return 31;
		}

		Pattern pRegister = Pattern.compile("r([\\d]+)");
		Matcher mRegister = pRegister.matcher(aRegister);
		if (mRegister.matches()) {
			try {
				return Integer.parseInt(mRegister.group(1));
			} catch (Exception e) {
				throw new RegisterException(aRegister);
			}
		}

		throw new RegisterException(aRegister);
	}

	/**
	 * Read the value of an index
	 *
	 * @calledby Emulator
	 *
	 * @param index  Register you want to read
	 * @return Value of the register
	 * @throws RegisterException  If you don't have access to that register
	 */
	public int read(int index) throws RegisterException {
		return this.registers.get(index).readValue();
	}

	/**
	 * Write the value to an index
	 *
	 * @calledby Emulator
	 *
	 * @param index  Register you want to read
	 * @param value you want to set
	 * @throws RegisterException  If you don't have access to that register
	 */
	public void write(int index, int value) throws RegisterException {
		this.registers.get(index).writeValue(value);
	}

	/**
	 * Reseting all registers
	 *
	 * @post Populate this.register
	 * @calledby EmulatorManager.reset()
	 */
	public void reset() {
		this.registers = new Vector<Register>(32);
		for (int i = 0; i < 32; i++)
			this.registers.add(new Register(i));
	}

	/**
	 * Reset state of all registers
	 */
	public void resetState() {
		for (Register reg: this.registers)
			reg.resetState();
	}

	/**
	 * Get all registers
	 *
	 * @calledby EmulatorManager
	 */
	public Vector<Register> get() {
		return this.registers;
	}
}
