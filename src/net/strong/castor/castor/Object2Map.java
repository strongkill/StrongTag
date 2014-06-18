package net.strong.castor.castor;

import java.util.Map;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class Object2Map extends Castor<Object, Map> {

	@SuppressWarnings("unchecked")
	@Override
	public Map cast(Object src, Class<?> toType, String... args) throws FailToCastObjectException {
		return Lang.obj2map(src, (Class<? extends Map>) ((Class<? extends Map>) toType));
	}

}
