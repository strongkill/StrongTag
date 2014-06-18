package net.strong.castor.castor;

import java.sql.Timestamp;
import java.text.ParseException;

import net.strong.lang.Lang;

public class String2Timestamp extends DateTimeCastor<String, Timestamp> {

	@Override
	public Timestamp cast(String src, Class<?> toType, String... args) {
		try {
			return new java.sql.Timestamp(dateTimeFormat.parse(src).getTime());
		}
		catch (ParseException e) {
			throw Lang.wrapThrow(e);
		}
	}

}
