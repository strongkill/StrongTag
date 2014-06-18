package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Lang;

public class String2Boolean extends Castor<String, Boolean> {

	@Override
	public Boolean cast(String src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		if (src.length() == 0)
			return false;
		return Lang.parseBoolean(src);
	}

}
