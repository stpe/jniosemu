package jniosemu.emulator.register;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegisterManager
{
	private int[] register = new int[32];

	public RegisterManager() {}

	private boolean checkIndex(int index) {
		if (!(index == 31 || index == 27 || (index >= 0 && index <= 23)))
			return false;

		return true;
	}

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

	public int read(int index) throws RegisterException {
		if (!checkIndex(index))
			throw new RegisterException(index);

		return register[index];
	}

	public void write(int aIndex, int aValue) throws RegisterException {
		if (!checkIndex(aIndex))
			throw new RegisterException(aIndex);

		if (aIndex > 0)
		register[aIndex] = aValue;
	}

	public void dump() {
		for (int i = 0; i < 32; i++) {
			if (checkIndex(i))
				System.out.println(i +": "+ this.register[i] + " ("+ Integer.toBinaryString(this.register[i]) +")");
		}
	}
}
