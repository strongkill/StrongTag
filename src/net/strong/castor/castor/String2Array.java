package net.strong.castor.castor;

import java.lang.reflect.Array;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;
import net.strong.lang.Lang;
import net.strong.lang.Strings;

public class String2Array extends Castor<String, Object> {

	public String2Array() {
		this.fromClass = String.class;
		this.toClass = Array.class;
	}

	@Override
	public Object cast(String src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		if (Strings.isQuoteByIgnoreBlank(src, '[', ']')) {
			return Json.fromJson(toType, src);
		}
		String[] ss = Strings.splitIgnoreBlank(src);
		return Lang.array2array(ss, toType.getComponentType());
	}

}
