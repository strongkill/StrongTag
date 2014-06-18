package net.strong.castor.castor;

import java.text.ParseException;

import net.strong.lang.Lang;

public class String2Datetime extends DateTimeCastor<String, java.util.Date> {

	@Override
	public java.util.Date cast(String src, Class<?> toType, String... args) {
		try {
			return dateTimeFormat.parse(src);
		}
		catch (ParseException e) {
			throw Lang.wrapThrow(e);
		}
	}

}
