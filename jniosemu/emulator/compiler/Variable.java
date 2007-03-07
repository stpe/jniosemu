package jniosemu.emulator.compiler;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jniosemu.instruction.InstructionException;

public class Variable
{
	static enum Type {BYTE, WORD, ASCII, ASCIZ};
	private String name;
	private Type type;
	private byte[] value;

	/**
	 * Init a Variable
	 *
	 * @param aName		Name of the variable
	 * @param aType		Type of variable
	 * @param aValue	Value of the variable
	 */
	public Variable (String aName, Type aType, String aValue) throws InstructionException {
		this.type = aType;
		this.name = aName;
		switch(type) {
			case BYTE:
				this.value = new byte[1];
				this.value[0] = (byte)(Compiler.parseValue(aValue, null) & 0xFF);
				break;
			case WORD:
				this.value = new byte[4];
				int value = (int)(Compiler.parseValue(aValue, null) & 0xFFFFFFFF);
				this.value[0] = (byte)(value        & 0xFF);
				this.value[1] = (byte)(value >>> 8  & 0xFF);
				this.value[2] = (byte)(value >>> 16 & 0xFF);
				this.value[3] = (byte)(value >>> 24 & 0xFF);
				break;
			case ASCII:
			case ASCIZ:
				Pattern pString = Pattern.compile("\"(.*)\"");
				Matcher mString = pString.matcher(aValue);
				if (mString.matches()) {
					String valueStr = mString.group(1);
					int length = valueStr.length();
					if (type == Type.ASCIZ)
						length++;
					this.value = new byte[length];
					char[] chars = valueStr.toCharArray();
					for (int i = 0; i < chars.length; i++) {
						this.value[i] = (byte)(chars[i] & 0xFF);
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
	 * @ret	The name of the variable
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the value of the variable
	 *
	 * @ret	The value of the variable
	 */
	public byte[] getValue() {
		return this.value;
	}
}
