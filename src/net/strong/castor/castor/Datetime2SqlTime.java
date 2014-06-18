package net.strong.castor.castor;

import java.sql.Time;
import java.util.Date;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Datetime2SqlTime extends Castor<Date, Time> {

	@Override
	public Time cast(Date src, Class<?> toType, String... args) throws FailToCastObjectException {
		return new Time(src.getTime());
	}

}
