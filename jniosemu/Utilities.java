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

		ret[0] = (byte)(value       & 0xFF);
		ret[1] = (byte)(value >>> 8 & 0xFF);

		return ret;
	}

	public static byte[] intToByteArray(int value) {
		byte[] ret = new byte[4];

		ret[0] = (byte)(value        & 0xFF);
		ret[1] = (byte)(value >>> 8  & 0xFF);
		ret[2] = (byte)(value >>> 16 & 0xFF);
		ret[3] = (byte)(value >>> 24 & 0xFF);

		return ret;
	}

	public static byte[] longToByteArray(long value) {
		byte[] ret = new byte[8];

		ret[0] = (byte)(value        & 0xFF);
		ret[1] = (byte)(value >>> 8  & 0xFF);
		ret[2] = (byte)(value >>> 16 & 0xFF);
		ret[3] = (byte)(value >>> 24 & 0xFF);
		ret[4] = (byte)(value >>> 32 & 0xFF);
		ret[5] = (byte)(value >>> 40 & 0xFF);
		ret[6] = (byte)(value >>> 48 & 0xFF);
		ret[7] = (byte)(value >>> 56 & 0xFF);

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
