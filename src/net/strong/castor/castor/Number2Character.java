package net.strong.castor.castor;

import net.strong.castor.Castor;

public class Number2Character extends Castor<Number, Character> {

	@Override
	public Character cast(Number src, Class<?> toType, String... args) {
		return (char) src.intValue();
	}

}
