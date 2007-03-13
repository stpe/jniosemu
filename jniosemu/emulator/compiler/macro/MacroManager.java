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
		this.put("BGT",     "rA, rB, label", "BLT \\rB, \\rA, \\label");
		this.put("BGTU",    "rA, rB, label", "BLTU \\rB, \\rA, \\label");
		this.put("BLE",     "rA, rB, label", "BGE \\rB, \\rA, \\label");
		this.put("CMPGT",   "rC, rA, rB",    "CMPLT \\rC, \\rB, \\rA");
		this.put("CMPGTI",  "rB, rA, imm",   "CMPGEI \\rB, \\rA, (\\imm + 1)");
		this.put("CMPGTU",  "rC, rA, rB",    "CMPLTU \\rC, \\rB, \\rA");
		this.put("CMPGTUI", "rB, rA, imm",   "CMPGEUI \\rB, \\rA, (\\imm + 1)");
		this.put("CMPLE",   "rC, rA, rB",    "CMPGE \\rC, \\rB, \\rA");
		this.put("CMPLEI",  "rB, rA, imm",   "CMPLTI \\rB, \\rA, (\\imm + 1)");
		this.put("CMPLEU",  "rC, rA, rB",    "CMPGEU \\rC, \\rB, \\rA");
		this.put("CMPLEUI", "rB, rA, imm",   "CMPLTUI \\rB, \\rA, (\\imm + 1)");
		this.put("MOV",     "rC, rA",        "ADD \\rC, \\rA, r0");
		this.put("MOVHI",   "rB, imm",       "ORHI \\rB, r0, \\imm");
		this.put("MOVI",    "rB, imm",       "ADDI \\rB, r0, \\imm");
		this.put("MOVIA",   "rB, imm",       "ORHI \\rB, r0, %hiadj(\\imm)\nADDI \\rB, \\rB, %lo(\\imm)");
		this.put("MOVUI",   "rB, imm",       "ORI \\rB, r0, \\imm");
		this.put("NOP",     null,            "ADD r0, r0, r0");
		this.put("SUBI",    "rB, rA, imm",   "ADDI \\rB, \\rA, -(\\imm)");
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
	 * @param aName		Name of the macro
	 * @param aArgs		Arguments separated with ","-character
	 * @param aLines  Lines separated with ","-character
	 */
	public Macro put(String aName, String aArgs, String aLines) {
		String[] args = null;
		if (aArgs != null && aArgs.length() > 0) {
			args = aArgs.split("\\s*,\\s*");
		}

		String[] lines = null;
		if (aLines != null && aLines.length() > 0)
			lines = aLines.split("\n");

		return this.put(aName, args, lines);
	}

	/**
	 * Add a macro
	 *
	 * @param aName 	Name of the macro
	 * @param aArgs 	Arguments
	 * @param aLines	Lines
	 */
	public Macro put(String aName, String[] aArgs, String[] aLines) {
		Macro macro = new Macro(aName, aArgs, aLines);
		this.macros.add(macro);
		this.nameHash.put(macro.getName(), macro);
		return macro;
	}

	/**
	 * Get lines of a macro
	 *
	 * @param aName	Name of the macro
	 * @param aArgs Arguments value
	 * @return			Lines
	 */
	public ArrayList<String> get(String aName, String aArgs) throws MacroException {
		String[] args = null;
		if (aArgs != null && aArgs.length() > 0)
			args = aArgs.split("\\s*,\\s*");

		Macro macro = this.nameHash.get(aName.toLowerCase());
		return macro.get(args);
	}
}
