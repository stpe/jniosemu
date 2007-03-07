import jniosemu.emulator.EmulatorManager;
import jniosemu.emulator.EmulatorException;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.emulator.compiler.CompilerException;
import jniosemu.editor.Editor;

public class JNiosEmu
{
	public static void main(String args[]) {
		Editor editor = new Editor();

		Compiler compiler = new Compiler(editor.read("test.s"));

		byte[] program = null;
		try {
			compiler.compile();
			program = compiler.link();
		} catch (CompilerException e) {
			System.out.println("CompilerException: "+ e.getMessage());
		}

		int pc = -1;
		try {
			pc = compiler.getGlobal("main");
		} catch (CompilerException e) {
			System.out.println("getGlobal ERROR");
		}

		EmulatorManager em = new EmulatorManager(pc, program);
		try {
			em.run();
		} catch (EmulatorException ex) {
			System.out.println("EmulatorException: "+ ex.getMessage());
		}
		em.dump();
	}
}
