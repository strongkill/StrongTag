package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Object2String extends Castor<Object, String> {

	@Override
	public String cast(Object src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return src.toString();
	}

}
