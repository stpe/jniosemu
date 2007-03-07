package jniosemu.emulator.compiler;

public class CompilerException extends Exception
{
	public CompilerException() {
		super();
	}

	public CompilerException(String aMsg) {
		super(aMsg);
	}

	public CompilerException(int aLineNumber, String aMsg) {
		super("Line "+ aLineNumber +": "+ aMsg);
	}
}
