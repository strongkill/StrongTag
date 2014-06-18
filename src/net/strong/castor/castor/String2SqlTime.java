package net.strong.castor.castor;

import java.text.ParseException;

import net.strong.lang.Lang;

public class String2SqlTime extends DateTimeCastor<String, java.sql.Time> {

	@Override
	public java.sql.Time cast(String src, Class<?> toType, String... args) {
		try {
			return new java.sql.Time(timeFormat.parse(src).getTime());
		}
		catch (ParseException e) {
			throw Lang.wrapThrow(e);
		}
	}

}
