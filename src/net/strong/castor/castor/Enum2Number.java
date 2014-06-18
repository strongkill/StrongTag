package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Enum2Number extends Castor<Enum, Number> {

	@Override
	public Number cast(Enum src, Class<?> toType, String... args) throws FailToCastObjectException {
		Mirror<?> mirror = Mirror.me(Integer.class);
		Integer re = src.ordinal();
		if (mirror.canCastToDirectly(toType))
			return re;
		return (Number) Mirror.me(toType).born(re.toString());
	}

}
