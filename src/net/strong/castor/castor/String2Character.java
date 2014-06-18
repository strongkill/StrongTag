package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class String2Character extends Castor<String, Character> {

	@Override
	public Character cast(String src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return src.charAt(0);
	}

}
