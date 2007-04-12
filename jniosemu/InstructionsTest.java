package jniosemu;

import java.util.Vector;
import java.util.Arrays;
import java.io.*;
import jniosemu.events.EventManager;
import jniosemu.emulator.EmulatorManager;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.emulator.register.Register;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.editor.Editor;
import java.util.regex.*;
import java.util.LinkedList;

public class InstructionsTest {

	public static final int TEST_FAILED = -1;
		
	static int numSucceded = 0;
	static String error_msg;

	public static void main(String [] args) {
		
		int numFiles = 0;
		String path = "../JNiosEmu/trunk/asm_test/instruction/";
		error_msg = new String("");

		if(args.length > 0) {
			path = args[0];
		}

		File dir = new File(path);

		String [] s = dir.list();
		if(s==null) {
			System.out.println("Error: No such directory");
			return;
		}
		
		// Sort files in directory.
		Arrays.sort(s);
		
		for(int i=0;i<s.length;i++) {
			File f = new File(path+"/"+s[i]);
			if(f.isFile()) {
				numFiles++;
				
				System.out.print("Test [" + i + "] (" + s[i] + ")");
				
				int result = processFile(path+"/"+s[i]);
				
				if(result == TEST_FAILED) {
					System.out.println("\t\t\t failed: ("+error_msg+")");
				} else {
					numSucceded++;
					System.out.println("\t" + result + " checks \t successful");
				}
			}
		}
	
		System.out.println("Tests done. ("+numFiles+" total: " + numSucceded + " successful, "+(numFiles - numSucceded)+" failed.)");

		System.exit(0);
	}

	public static int processFile(String filename) {

		String fileContent;

		try {
			fileContent = Editor.read(filename);
		}
		catch(IOException e) {
			error_msg = e.getMessage();
			return TEST_FAILED;
		}

		LinkedList<String> regNum = new LinkedList<String>();
		LinkedList<String> regValue = new LinkedList<String>();

		int regCount = 0;

		Pattern pLabels = Pattern.compile("# r(\\d) = (.*)\n");
		Matcher mLabels = pLabels.matcher(fileContent);
		while (mLabels.find()) {
			regNum.add(mLabels.group(1));
			regValue.add(mLabels.group(2));
			regCount++;
		}

		EventManager eventManager = new EventManager();

		// Add listener for COMPILE_ERROR, EMULATOR_ERROR

		EmulatorManager emulatorManager = new EmulatorManager(eventManager);
		emulatorManager.compile(fileContent);

		emulatorManager.runAll();

		// Wait until EMULATOR_DONE

		RegisterManager registerManager = emulatorManager.getRegisterManager();

		int registerNum, registerValue = 0;
		for(int j=0;j<regNum.size();j++) {
			registerNum = java.lang.Integer.parseInt(regNum.get(j));
			try {
				registerValue = (int)Compiler.parseValue(regValue.get(j), null);
			}
			catch(Exception e) {
				error_msg = e.getMessage();
				return TEST_FAILED;
			}
			if(registerManager.read(registerNum) != registerValue) {
				error_msg = new String("Register r"+registerNum+"="+registerValue+" failed");
				return TEST_FAILED;
			}
				
		}

		emulatorManager.reset();
	
		return regCount;
	}

}

