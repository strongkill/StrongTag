package net.strong.castor.castor;

import java.sql.Time;
import java.sql.Timestamp;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Timestamp2SqlTime extends Castor<Timestamp, Time> {

	@Override
	public Time cast(Timestamp src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return new Time(src.getTime());
	}

}
