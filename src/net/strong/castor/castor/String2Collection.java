package net.strong.castor.castor;

import java.util.Collection;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;
import net.strong.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class String2Collection extends Castor<String, Collection> {

	@Override
	public Collection cast(String src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return (Collection) Json.fromJson(toType, Lang.inr(src));
	}

}
