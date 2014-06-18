package net.strong.castor.castor;

import java.util.Calendar;

import net.strong.castor.Castor;

public class Number2Calendar extends Castor<Number, Calendar> {

	@Override
	public Calendar cast(Number src, Class<?> toType, String... args) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(src.longValue());
		return c;
	}

}
