package net.strong.castor.castor;

import java.sql.Timestamp;
import java.util.Date;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Timestamp2Datetime extends Castor<Timestamp, java.util.Date> {

	@Override
	public Date cast(Timestamp src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return new java.util.Date(src.getTime());
	}

}
