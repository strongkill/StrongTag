package net.strong.castor.castor;

import java.lang.reflect.Array;
import java.util.Map;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class Map2Array extends Castor<Map, Object> {

	public Map2Array() {
		this.fromClass = Map.class;
		this.toClass = Array.class;
	}

	@Override
	public Object cast(Map src, Class<?> toType, String... args) throws FailToCastObjectException {
		return Lang.collection2array(src.values(), toType.getComponentType());
	}

}
