package net.strong.castor.castor;

import java.lang.reflect.Array;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.json.Json;
import net.strong.json.JsonFormat;
import net.strong.lang.Lang;

public class Array2String extends Castor<Object, String> {

	public Array2String() {
		this.fromClass = Array.class;
		this.toClass = String.class;
	}

	@Override
	public String cast(Object src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		if (null != src && CharSequence.class.isAssignableFrom(src.getClass().getComponentType())) {
			return Lang.concat(",", (CharSequence[]) src).toString();
		}
		return Json.toJson(src, JsonFormat.compact());
	}

}
