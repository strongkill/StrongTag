package net.strong.castor.castor;

import java.sql.Date;
import java.sql.Timestamp;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class SqlDate2Timestamp extends Castor<Date, Timestamp> {

	@Override
	public Timestamp cast(Date src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		return new Timestamp(src.getTime());
	}

}
