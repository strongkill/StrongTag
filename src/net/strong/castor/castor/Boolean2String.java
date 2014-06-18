package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Boolean2String extends Castor<Boolean, String> {

	@Override
	public String cast(Boolean src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return String.valueOf(src);
	}

}
