package jniosemu.editor;

import java.io.*;

public class Editor
{
	public Editor() {
	}

	public String read(String aFilename) {
		byte[] content = null;

		File file = new File(aFilename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);

			// Here BufferedInputStream is added for fast reading.
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			content = new byte[dis.available()];
			dis.readFully(content);

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (Exception e) {
			System.out.println("error: "+ e.getMessage());
			e.printStackTrace();
		}

		return new String(content);
	}
}
