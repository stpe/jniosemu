package jniosemu.emulator.compiler;

public class Constant
{
	private final String name;
	private final String value;

	/**
	 * Init a Constant from a codeline
	 *
	 * @param aLine	A line of code containing name and value
	 */
	public Constant(String aLine) {
		String[] value = aLine.split("\\s*,\\s*", 2);
		this.name = value[0];
		this.value = value[1];
	}

	/**
	 * Init a Constant from name and value
	 *
	 * @param aName		Name of the constant
	 * @param aValue	Value of the constant
	 */
	public Constant(String aName, String aValue) {
		this.name = aName;
		this.value = aValue;
	}

	/**
	 * Return the name of the constant
	 *
	 * @ret	The name of the contant
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the value of the constant
	 *
	 * @ret	The value of the contant
	 */
	public String getValue() {
		return this.value;
	}
}
