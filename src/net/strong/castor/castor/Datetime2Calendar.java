package net.strong.castor.castor;

import java.util.Calendar;
import java.util.Date;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Datetime2Calendar extends Castor<java.util.Date, java.util.Calendar> {

	@Override
	public Calendar cast(Date src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		Calendar c = Calendar.getInstance();
		c.setTime(src);
		return c;
	}

}
