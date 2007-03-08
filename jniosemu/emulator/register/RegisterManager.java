package jniosemu.emulator.register;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Manage all register
 */
public class RegisterManager
{
	/**
	 * contains the values of the registers
	 */
	private int[] register = new int[32];

	/**
	 * Init RegisterManager
	 */
	public RegisterManager() {}

	/**
	 * Check if you are allowed to access a specific register
	 *
	 * @calledby read(), write()
	 *
	 * @param index  Register you want to check
	 * @return true or false depending if you are allowed to access that index
	 */
	private boolean checkIndex(int index) {
		if (!(index == 31 || index == 27 || (index >= 0 && index <= 23)))
			return false;

		return true;
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
		if (!checkIndex(index))
			throw new RegisterException(index);

		return register[index];
	}

	/**
	 * Write the value to an index
	 *
	 * @calledby Emulator
	 *
	 * @param aIndex  Register you want to read
	 * @param aValue you want to set
	 * @throws RegisterException  If you don't have access to that register
	 */
	public void write(int aIndex, int aValue) throws RegisterException {
		if (!checkIndex(aIndex))
			throw new RegisterException(aIndex);

		if (aIndex > 0)
		register[aIndex] = aValue;
	}

	/**
	 * Reseting all registers
	 *
	 * @calledby EmulatorManager.reset()
	 */
	public void reset() {
		
	}

	public void dump() {
		for (int i = 0; i < 32; i++) {
			if (checkIndex(i))
				System.out.println(i +": "+ this.register[i] + " ("+ Integer.toBinaryString(this.register[i]) +")");
		}
	}
}
