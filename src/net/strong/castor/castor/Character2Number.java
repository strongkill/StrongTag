package net.strong.castor.castor;

import net.strong.castor.Castor;

public class Character2Number extends Castor<Character, Number> {

	@Override
	public Number cast(Character src, Class<?> toType, String... args) {
		return (int) src.charValue();
	}

}
