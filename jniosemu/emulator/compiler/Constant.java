package jniosemu.emulator.compiler;

public class Constant
{
	private final String name;
	private final String value;

	/**
	 * Init a Constant from a codeline
	 *
	 * @param aLine  line of code containing name and value
	 */
	public Constant(String aLine) {
		String[] value = aLine.split("\\s*,\\s*", 2);
		this.name = value[0];
		this.value = value[1];
	}

	/**
	 * Init a Constant from name and value
	 *
	 * @param aName		name of the constant
	 * @param aValue	value of the constant
	 */
	public Constant(String aName, String aValue) {
		this.name = aName;
		this.value = aValue;
	}

	/**
	 * Return the name of the constant
	 *
	 * @return	name of the constant
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the value of the constant
	 *
	 * @return	value of the constant
	 */
	public String getValue() {
		return this.value;
	}
}
