package jniosemu.editor;

import java.io.*;

/**
 * This is a utility class for the GUIEditor where the user is editing the source code. This class
 * provides I/O access through static read and write methods for opening and saving source
 * documents.
 */
public class Editor {

  /**
   * Return content from file as string.
   *
   * @pre Given filename must be valid.
   * @calledby GUIEditor.openDocument()
   * @param filename name and path of file to read
   * @return content of file as string
   * @throws IOException when given file is invalid
   */
  public static String read(String filename) throws IOException {
    BufferedReader in = null;
    StringBuffer sb = new StringBuffer();

    try {
      in = new BufferedReader(new FileReader(filename));

      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line + "\n");
      }
    } finally {
      if (in != null) in.close();
    }

    return sb.toString();
  }

  /**
   * Write string to file.
   *
   * @pre Given filename must be valid.
   * @post Content is written to file.
   * @calledby GUIEditor.saveDocument()
   * @param filename name and path of file to write
   * @param content data to write to file
   * @throws IOException when given file is invalid or permission is denied to write to disk
   */
  public static void write(String filename, String content) throws IOException {
    PrintWriter out = null;

    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

      out.print(content);
      out.flush();
    } finally {
      if (out != null) out.close();
    }
  }
}
