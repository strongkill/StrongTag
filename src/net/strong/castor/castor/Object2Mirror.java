package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Object2Mirror extends Castor<Object, Mirror> {

	@Override
	public Mirror cast(Object src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return Mirror.me(src.getClass());
	}

}
