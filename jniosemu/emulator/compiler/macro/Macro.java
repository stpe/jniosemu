package jniosemu.emulator.compiler.macro;

import java.util.ArrayList;
import java.util.Arrays;

public class Macro
{
	private String name;
	private String[] args;
	private ArrayList<String> lines = new ArrayList<String>();

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

		if (aLines != null) {
			for (String line: aLines)
				this.lines.add(line);
		}
	}

	public Macro(String aName, String[] aArgs, ArrayList<String> aLines) {
		this.name = aName.toLowerCase();
		this.args = aArgs;

		this.lines = aLines;
	}

	public void addLine(ArrayList<String> lines) {
		for (String line: lines)
			this.lines.add(line);
	}

	public void addLine(String line) {
		this.lines.add(line);
	}

	/**
	 * Gets the value of the arguments and replace the arguments with its value
	 *
	 * @param aArgs The arguments value
	 * @return			The lines
	 */
	public ArrayList<String> get(String[] aArgs) throws MacroException {
		if (aArgs == null ^ this.args == null)
			throw new MacroException();

		if (aArgs != null && this.args != null && aArgs.length != this.args.length)
			throw new MacroException();

		ArrayList<String> lines = new ArrayList<String>(this.lines.size());

		if (this.args != null && this.args.length > 0) {
			for(String line: this.lines) {
				String outLine = line;
				for (int j = 0; j < aArgs.length; j++)
					outLine = outLine.replaceAll("\\\\"+ this.args[j], aArgs[j]);
				lines.add(outLine);
			}
		} else {
			lines = (ArrayList<String>)this.lines.clone();
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
