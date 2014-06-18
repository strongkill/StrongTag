package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class String2Enum extends Castor<String, Enum> {

	@SuppressWarnings("unchecked")
	@Override
	public Enum cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
		return Enum.valueOf((Class<Enum>) toType, src);
	}

}
