package net.strong.castor.castor;

import java.util.Map;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class Map2Object extends Castor<Map, Object> {

	@Override
	public Object cast(Map src, Class<?> toType, String... args) throws FailToCastObjectException {
		return Lang.map2Object(src, toType);
	}

}
