package net.strong.castor.castor;

import java.util.Map;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;
import net.strong.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class String2Map extends Castor<String, Map> {

	@Override
	public Map cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
		return (Map) Json.fromJson(Lang.inr(src));
	}

}
