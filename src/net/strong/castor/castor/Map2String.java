package net.strong.castor.castor;

import java.util.Map;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;

@SuppressWarnings({"rawtypes"})
public class Map2String extends Castor<Map, String> {

	@Override
	public String cast(Map src, Class<?> toType, String... args) throws FailToCastObjectException {
		return Json.toJson(src);
	}

}
