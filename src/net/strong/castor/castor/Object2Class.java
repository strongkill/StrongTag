package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Object2Class extends Castor<Object, Class> {

	@Override
	public Class cast(Object src, Class<?> toType, String... args) throws FailToCastObjectException {
		return src.getClass();
	}

}
