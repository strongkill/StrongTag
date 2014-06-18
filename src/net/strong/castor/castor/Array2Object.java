package net.strong.castor.castor;

import java.lang.reflect.Array;

import net.strong.castor.Castor;
import net.strong.castor.Castors;
import net.strong.castor.FailToCastObjectException;

public class Array2Object extends Castor<Object, Object> {

	public Array2Object() {
		this.fromClass = Array.class;
		this.toClass = Object.class;
	}

	@Override
	public Object cast(Object src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		if (Array.getLength(src) == 0)
			return null;
		return Castors.me().castTo(Array.get(src, 0), toType);
	}

}
