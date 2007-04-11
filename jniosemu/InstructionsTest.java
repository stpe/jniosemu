package jniosemu;

import java.util.Vector;
import java.io.*;
import jniosemu.events.EventManager;
import jniosemu.emulator.EmulatorManager;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.emulator.register.Register;
import jniosemu.editor.Editor;

public class InstructionsTest {
		
	public void readRegValues() {

		// Open file and read what the 
		// registers should have as value.

	}

	public static void main(String [] args) {
		
		String path = "asm_test/instruction";

		if(args.length > 0) {
			path = args[0];
		}

		File dir = new File(path);

		String [] s = dir.list();
		if(s==null) {
			System.out.println("Error: No such directory");
			return;
		}
		for(int i=0;i<s.length;i++) {
			File f = new File(path+"/"+s[i]);
			if(f.isFile())
				processFile(path+"/"+s[i]);
		}

	}

	public static boolean processFile(String filename) {

		String fileContent;

		try {
			fileContent = Editor.read(filename);
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			return false;
		}

		// Parsa ut registervärdena från fileContent.

		EventManager eventManager = new EventManager();

		// Add listener for COMPILE_ERROR, EMULATOR_ERROR

		EmulatorManager emulatorManager = new EmulatorManager(eventManager);
		emulatorManager.compile(fileContent);

		for (int i = 0; i < 2; i++) {
			emulatorManager.runAll();

			// Wait until EMULATOR_DONE

			RegisterManager registerManager = emulatorManager.getRegisterManager();

			// Check register if right value
			Vector<Register> registers = registerManager.get();
			for (Register register : registers) {
				System.out.println(register.getName() +": "+ register.getValue());
			}

			emulatorManager.reset();
		}
	
		return true;

	}

}

