package jniosemu.emulator.compiler.macro;

import java.util.Hashtable;
import java.util.ArrayList;

public class MacroManager
{
	private Hashtable<String, Macro>nameHash = new Hashtable<String, Macro>();
	private ArrayList<Macro> macros = new ArrayList<Macro>();

	/**
	 * Init the MacroManager
	 */
	public MacroManager() {
		this.put("BGT",     "rA, rB, label", "BLT \\rB, \\rA, \\label", -1);
		this.put("BGTU",    "rA, rB, label", "BLTU \\rB, \\rA, \\label", -1);
		this.put("BLE",     "rA, rB, label", "BGE \\rB, \\rA, \\label", -1);
		this.put("CMPGT",   "rC, rA, rB",    "CMPLT \\rC, \\rB, \\rA", -1);
		this.put("CMPGTI",  "rB, rA, imm",   "CMPGEI \\rB, \\rA, (\\imm + 1)", -1);
		this.put("CMPGTU",  "rC, rA, rB",    "CMPLTU \\rC, \\rB, \\rA", -1);
		this.put("CMPGTUI", "rB, rA, imm",   "CMPGEUI \\rB, \\rA, (\\imm + 1)", -1);
		this.put("CMPLE",   "rC, rA, rB",    "CMPGE \\rC, \\rB, \\rA", -1);
		this.put("CMPLEI",  "rB, rA, imm",   "CMPLTI \\rB, \\rA, (\\imm + 1)", -1);
		this.put("CMPLEU",  "rC, rA, rB",    "CMPGEU \\rC, \\rB, \\rA", -1);
		this.put("CMPLEUI", "rB, rA, imm",   "CMPLTUI \\rB, \\rA, (\\imm + 1)", -1);
		this.put("MOV",     "rC, rA",        "ADD \\rC, \\rA, r0", -1);
		this.put("MOVHI",   "rB, imm",       "ORHI \\rB, r0, \\imm", -1);
		this.put("MOVI",    "rB, imm",       "ADDI \\rB, r0, \\imm", -1);
		this.put("MOVIA",   "rB, imm",       "ORHI \\rB, r0, %hiadj(\\imm)\nADDI \\rB, \\rB, %lo(\\imm)", -1);
		this.put("MOVUI",   "rB, imm",       "ORI \\rB, r0, \\imm", -1);
		this.put("NOP",     null,            "ADD r0, r0, r0", -1);
		this.put("SUBI",    "rB, rA, imm",   "ADDI \\rB, \\rA, -(\\imm)", -1);
	}

	/**
	 * Checks if a macro exists
	 *
	 * @param aName Name of the macro that we check for
	 * @return			True or false depending of the macro exists or not
	 */
	public boolean exists(String aName) {
		return this.nameHash.containsKey(aName.toLowerCase());
	}

	/**
	 * Add a macro
	 *
	 * @calledby MacroManager()
	 * @calls put()
	 *
	 * @param aName		Name of the macro
	 * @param aArgs		Arguments separated with ","-character
	 * @param aLines  Lines separated with ","-character
	 * @param lineNumber  Line number where the macro is defined
	 */
	public Macro put(String name, String args, String lines, int lineNumber) {
		String[] argsArray = null;
		if (args != null && args.length() > 0) {
			argsArray = args.split("\\s*,\\s*");
		}

		String[] linesArray = null;
		if (lines != null && lines.length() > 0)
			linesArray = lines.split("\n");

		return this.put(name, argsArray, linesArray, lineNumber);
	}

	/**
	 * Add a macro
	 *
	 * @param name 	Name of the macro
	 * @param args 	Arguments
	 * @param lines	Lines
	 * @param lineNumber  Line number where the macro is defined
	 */
	public Macro put(String name, String[] args, String[] lines, int lineNumber) {
		Macro macro = new Macro(name, args, lines, lineNumber);
		this.macros.add(macro);
		this.nameHash.put(macro.getName(), macro);
		return macro;
	}

	/**
	 * Get macro
	 *
	 * @param name	Name of the macro
	 * @return			Macro
	 */
	public Macro get(String name) throws MacroException {
		return this.nameHash.get(name.toLowerCase());
	}
}
