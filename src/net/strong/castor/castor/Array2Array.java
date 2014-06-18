package net.strong.castor.castor;

import java.lang.reflect.Array;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Lang;

public class Array2Array extends Castor<Object, Object> {

	public Array2Array() {
		this.fromClass = Array.class;
		this.toClass = Array.class;
	}

	@Override
	public Object cast(Object src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return Lang.array2array(src, toType.getComponentType());
	}

}
