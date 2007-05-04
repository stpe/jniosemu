package jniosemu.emulator.compiler;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.Utilities;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.instruction.InstructionException;

public class Variable
{
	static enum TYPE {BYTE, HWORD, WORD, ASCII, ASCIZ};
	private String name;
	private TYPE type;
	private int startAddr;
	private byte[] startValue;

	/**
	 * Init a Variable
	 *
	 * @param aName		Name of the variable
	 * @param aType		Type of variable
	 * @param aStartValue	Start value of the variable
	 */
	public Variable (String aName, TYPE aType, String aStartValue) throws InstructionException {
		this.type = aType;
		this.name = aName;
		switch(this.type) {
			case BYTE:
				this.startValue = new byte[1];
				this.startValue[0] = (byte)(Compiler.parseValue(aStartValue) & 0xFF);
				break;
			case HWORD:
				this.startValue = Utilities.shortToByteArray((short)Compiler.parseValue(aStartValue));
				break;
			case WORD:
				this.startValue = Utilities.intToByteArray((int)Compiler.parseValue(aStartValue));
				break;
			case ASCII:
			case ASCIZ:
				Pattern pString = Pattern.compile("\"(.*)\"");
				Matcher mString = pString.matcher(aStartValue);
				if (mString.matches()) {
					String valueStr = mString.group(1);
					int length = valueStr.length();
					if (type == TYPE.ASCIZ)
						length++;
					this.startValue = new byte[length];
					char[] chars = valueStr.toCharArray();
					for (int i = 0; i < chars.length; i++) {
						this.startValue[i] = (byte)(chars[i] & 0xFF);
					}
				} else {
					throw new InstructionException();
				}
				break;
		}
	}

	/**
	 * Return the name of the variable
	 *
	 * @return	name of the variable
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the value of the variable
	 *
	 * @return	value of the variable
	 */
	public byte[] getStartValue() {
		return this.startValue;
	}

	public int getLength() {
		return this.startValue.length;
	}

	public TYPE getType() {
		return this.type;
	}

	public void setStartAddr(int startAddr) {
		this.startAddr = startAddr;
	}

	public int getStartAddr() {
		return this.startAddr;
	}

	public byte[] getValue(MemoryBlock memoryBlock) {
		byte[] value = new byte[this.getLength()];
		for (int i = 0; i < this.getLength(); i++) {
			value[i] = memoryBlock.readByte(this.getStartAddr() + i);
		}

		return value;
	}
}
