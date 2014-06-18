package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Enum2String extends Castor<Enum, String> {

	@Override
	public String cast(Enum src, Class<?> toType, String... args) throws FailToCastObjectException {
		return src.name();
	}
}
