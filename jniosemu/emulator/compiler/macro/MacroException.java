package jniosemu.emulator.compiler.macro;

public class MacroException extends RuntimeException
{
	public MacroException() {
		super();
	}

	public MacroException(String name, String msg) {
		super("Macro "+ name +": "+ msg);
	}
}
