package net.strong.castor.castor;

import java.text.ParseException;
import java.util.Calendar;

import net.strong.lang.Lang;

public class String2Calendar extends DateTimeCastor<String, Calendar> {

	@Override
	public Calendar cast(String src, Class<?> toType, String... args) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateTimeFormat.parse(src));
		}
		catch (ParseException e) {
			throw Lang.wrapThrow(e);
		}
		return c;
	}

}
