package jniosemu.gui;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * File filter used by File Chooser dialogs.
 */
public class AsmFileFilter extends FileFilter
{

  public boolean accept(File f)
  {
		if (f.isDirectory())
		{
			return true;
		}

		String extension = getExtension(f);

		return (extension != null && ( extension.equals("s") ));
  }

  public String getDescription()
  {
      return "Assembler Files (*.s)";
  }

  /**
   * Returns extension of given file or null if
   * there is no extension.
   *
   * @calledby  accept()
   *
   * @param  f  file object
   * @return    file extension not including period
   */
  private String getExtension(File f)
  {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 &&  i < s.length() - 1) {
          ext = s.substring(i+1).toLowerCase();
      }
      return ext;
  }
}
