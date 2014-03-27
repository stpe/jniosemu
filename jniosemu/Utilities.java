package jniosemu;

import java.util.Vector;
import java.net.URL;

public class Utilities
{
	public static int vectorToInt(Vector<Boolean> vector) {
		int ret = 0;
		int i = 0;
		for (Boolean value: vector) {
			if (value)
				ret |= (int)Math.pow(2, i);
			i++;
		}
		return ret;
	}

	public static byte vectorToByte(Vector<Boolean> vector) {
		byte ret = 0;
		int i = 0;
		for (Boolean value: vector) {
			if (value)
				ret |= (byte)Math.pow(2, i);
			i++;
		}
		return ret;
	}

	public static byte[] shortToByteArray(short value) {
		byte[] ret = new byte[2];

		for (int i = 0; i < 2; i++)
			ret[i] = (byte)(value >>> (8 * i) & 0xFF);

		return ret;
	}

	public static byte[] intToByteArray(int value) {
		byte[] ret = new byte[4];

		for (int i = 0; i < 4; i++)
			ret[i] = (byte)(value >>> (8 * i) & 0xFF);

		return ret;
	}

	public static byte[] longToByteArray(long value) {
		byte[] ret = new byte[8];

		for (int i = 0; i < 8; i++)
			ret[i] = (byte)(value >>> (8 * i) & 0xFF);

		return ret;
	}

	public static int byteArrayToInt(byte[] value, int offset) {
		int ret = 0;

		for (int i = 0; i < 4; i++)
			ret |= (((long)value[i+offset] & 0xFF) << (8 * i));

		return ret;
	}

	public static int byteArrayToInt(byte[] value) {
		return byteArrayToInt(value, 0);
	}

	public static long byteArrayToLong(byte[] value) {
		long ret = 0;

		for (int i = 0; i < 8; i++)
			ret |= ((value[i] & 0xFFL) << (8 * i));

		return ret;
	}

	public static Vector<Boolean> intToVector(int value, int size) {
		Vector<Boolean> ret = new Vector<Boolean>(size);

		for (int i = 0; i < size; i++)
			ret.add(i, (value & (int)Math.pow(2, i)) > 0);

		return ret;
	}

	public static String byteArrayToBitString(byte[] value) {
		String ret = "";

		for (int i = value.length - 1; i >= 0; i--) {
			for (int j = 7; j >= 0; j--) {
				if ((value[i] & (int)Math.pow(2, j)) > 0)
					ret = ret.concat("1");
				else
					ret = ret.concat("0");
			}

			if (i > 0)
				ret = ret.concat(" ");
		}

		return ret;
	}

	public static int unsignedbyteToInt(byte value) {
		int ret = 0;
		return (int)(ret | value);
	}

	/**
	 * Converts byte array to string where non-printable
	 * characters are displayed as a period (".").
	 *
	 * @param   value  byte array value
	 * @return         string
	 */
	public static String byteArrayToString(byte[] value) {
		String ret = "";

		for (int i = 0; i < value.length; i++)
		{
			if (value[i] >= 32 && value[i] <= 126)
				ret = ret + ((char) value[i]);
			else
				ret = ret.concat(".");
		}
		
		return ret;
	}

	/**
	 * Converts byte array to hex string.
	 *
	 * @param   value  byte array value
	 * @return         string
	 */
	public static String byteArrayToHexString(byte[] value) {
		String ret = "";
		
		for (int i = value.length - 1; i >= 0; i--)
		{
			ret = ret.concat( Integer.toHexString( (value[i] & 0xFF) | 0x100 ).substring(1, 3) );
		}

		return ret;		
	}

	/**
	 * Converts integer to a formatted string of the hexadecimal value.
	 *
	 * @param   value  decimal value to convert
	 * @return         hexadecimal value as string
	 */
	public static String intToHexString(int value) {
		String hex = "00000000" + Integer.toHexString(value);
		return "0x" + hex.substring(hex.length()-8, hex.length());
	}

	public static String byteToHexString(byte value) {
		String hex = "00" + Integer.toHexString(value);
		return "0x" + hex.substring(hex.length()-2, hex.length());
	}

	/**
	 * Converts integer to a formatted string of the binary value.
	 *
	 * @param   value  decimal value to convert
	 * @return         binary value as string
	 */
	public static String intToBinaryString(int value) {
		String bin = "00000000000000000000000000000000" + Integer.toBinaryString(value);
		
		return
			bin.substring(bin.length() - 32, bin.length() - 24) + " " +
			bin.substring(bin.length() - 24, bin.length() - 16) + " " +
			bin.substring(bin.length() - 16, bin.length() - 8) + " " +
			bin.substring(bin.length() - 8, bin.length());
	}

	/**
	 * Uses classLoader to load an image. Needed for placing images in jar-files
	 *
	 * @param path  Relative path to the image
	 * @return  URL to the image
	 */
	public static URL loadImage(String path) {
		Utilities tmp = new Utilities();
		ClassLoader classLoader = tmp.getClass().getClassLoader();
		return classLoader.getResource(path);
	}

	/**
	 * Replaces all instanses of aReplace with aNew in aValue.
	 *
	 * @calledby parseValue(), parseLine()
	 *
	 * @param aValue The string where the replacment will happen
	 * @param aReplace The old value
	 * @param aNew The new value
	 * @return The new string
	 */
	public static String stringReplace(String aValue, String aReplace, String aNew) {
		int start = aValue.indexOf(aReplace);
		StringBuffer replace;
		while (start >= 0) {
			replace = new StringBuffer(aValue);
			replace.replace(start, start+aReplace.length(), aNew);
			aValue = replace.toString();
			start = aValue.indexOf(aReplace, start);
		}
		return aValue;
	}

	public static String stringReplaceOne(String value, String oldstring, String newstring) {
		int start = value.indexOf(oldstring);

		if (start >= 0) {
			StringBuffer replace = new StringBuffer(value);
			replace.replace(start, start+oldstring.length(), newstring);
			return replace.toString();
		}

		return value;
	}
}
