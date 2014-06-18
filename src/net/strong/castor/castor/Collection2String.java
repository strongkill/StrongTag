package net.strong.castor.castor;

import java.util.Collection;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;
import net.strong.json.JsonFormat;

@SuppressWarnings({"rawtypes"})
public class Collection2String extends Castor<Collection, String> {

	@Override
	public String cast(Collection src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return Json.toJson(src, JsonFormat.compact());
	}

}
