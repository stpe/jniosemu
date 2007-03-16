package jniosemu.emulator.io;

import java.util.Vector;

import jniosemu.emulator.memory.MemoryManager;
import jniosemu.events.EventManager;

public abstract class IODevice
{
	protected int vectorToInt(Vector<Boolean> vector) {
		int ret = 0;
		int i = 0;
		for (Boolean value: vector) {
			if (value)
				ret |= (int)Math.pow(2, i);
			i++;
		}
		return ret;
	}

	protected Vector<Boolean> intToVector(int value, int size) {
		Vector<Boolean> ret = new Vector<Boolean>(size);

		for (int i = 0; i < size; i++)
			ret.add(i, (value & (int)Math.pow(2, i)) > 0);

		return ret;
	}

	public abstract void memoryChange();

	public abstract void reset(MemoryManager memory);
}
