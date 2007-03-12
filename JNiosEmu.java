import jniosemu.gui.*;
import jniosemu.events.*;
import javax.swing.UIManager;
import jniosemu.emulator.EmulatorManager;

public class JNiosEmu
{
	public static void main(String args[]) {
		try {
      UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());
 	  } 
    catch (Exception e) {
      System.out.println("JNiosEmu.main(): " + e.getMessage());
    }

		EventManager eventManager = new EventManager();

		// start
    new GUIManager(eventManager);
    new EmulatorManager(eventManager);
	}
}
