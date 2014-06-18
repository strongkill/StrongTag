package net.strong.castor.castor;

import java.sql.Timestamp;
import java.util.Date;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class Datetime2Timpestamp extends Castor<java.util.Date, Timestamp> {

	@Override
	public Timestamp cast(Date src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return new Timestamp(src.getTime());
	}

}
