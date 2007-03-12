package jniosemu.editor;

import java.io.*;

/**
 * Utility class for editor. Read and write files.
 */
public class Editor
{

	/**
	 * Return content from file as string.
	 *
	 * @calledby GUIEditor.openDocument()
	 *
	 * @param  filename  Name and path of file to read
	 * @return           Content of file as string
	 */
  public String read(String filename) throws IOException
	{
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();

    try
    {
      in = new BufferedReader(new FileReader(filename));

			String line;
      while ((line = in.readLine()) != null) {
            sb.append(line + "\n");
      }
    }
    finally {
      in.close();
    }

    return sb.toString();
  }

	/**
	 * Write string to file.
	 *
	 * @calledby GUIEditor.saveDocument()
	 *
	 * @param  filename  Name and path of file to write
	 * @param  content   Data to write to file
	 */
	public void write(String filename, String content) throws IOException
	{
		PrintWriter out = null;

    try
    {
			out =
				new PrintWriter(
					new BufferedWriter(
						new FileWriter(filename)
					)
				);

       out.print(content);
       out.flush();
    }
    finally
    {
			out.close();
    }
  }
}
