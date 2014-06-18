package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Mirror;

public class Object2Object extends Castor<Object, Object> {

	@Override
	public Object cast(Object src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return Mirror.me(toType).born(src);
	}

}
