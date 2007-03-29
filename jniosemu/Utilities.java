package jniosemu;

import java.util.Vector;

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

	public static int byteArrayToInt(byte[] value) {
		int ret = 0;

		for (int i = 0; i < 4; i++)
			ret |= ((long)value[i] << (8 * i));

		return ret;
	}

	public static long byteArrayToLong(byte[] value) {
		long ret = 0;

		for (int i = 0; i < 8; i++)
			ret |= ((long)value[i] << (8 * i));

		return ret;
	}

	public static Vector<Boolean> intToVector(int value, int size) {
		Vector<Boolean> ret = new Vector<Boolean>(size);

		for (int i = 0; i < size; i++)
			ret.add(i, (value & (int)Math.pow(2, i)) > 0);

		return ret;
	}

	public static int unsignedbyteToInt(byte value) {
		int ret = 0;
		return (int)(ret | value);
	}
}
