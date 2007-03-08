package jniosemu.emulator.compiler.macro;

public class Macro
{
	private String name;
	private String[] args;
	private String[] lines;

	/**
	 * Init a Macro
	 *
	 * @param aName 	Name of the macro
	 * @param aArgs 	Arguments of the macro
	 * @param aLines	Lines of the macro
	 */
	public Macro(String aName, String[] aArgs, String[] aLines) {
		this.name = aName.toLowerCase();
		this.args = aArgs;
		this.lines = aLines;
	}

	/**
	 * Gets the value of the arguments and replace the arguments with its value
	 *
	 * @param aArgs The arguments value
	 * @return			The lines
	 */
	public String[] get(String[] aArgs) throws MacroException {
		if (aArgs == null ^ this.args == null)
			throw new MacroException();

		if (aArgs != null && this.args != null && aArgs.length != this.args.length)
			throw new MacroException();

		String[] lines = this.lines.clone();

		if (this.args != null && this.args.length > 0) {
			for (int i = 0; i < lines.length; i++) {
				for (int j = 0; j < aArgs.length; j++)
					lines[i] = lines[i].replaceAll("\\\\"+ this.args[j], aArgs[j]);
			}
		}

		return lines;
	}

	/**
	 * Return the name of the macro
	 *
	 * @return	name of the macro
	 */
	public String getName() {
		return this.name;
	}
}
