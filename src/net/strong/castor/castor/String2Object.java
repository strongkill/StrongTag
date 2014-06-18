package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;
import net.strong.lang.Mirror;
import net.strong.lang.Strings;

public class String2Object extends Castor<String, Object> {

	@Override
	public Object cast(String src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		if (Strings.isQuoteByIgnoreBlank(src, '{', '}'))
			return Json.fromJson(toType, src);
		return Mirror.me(toType).born(src);
	}

}
