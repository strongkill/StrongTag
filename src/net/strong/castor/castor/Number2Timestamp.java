package net.strong.castor.castor;

import java.sql.Timestamp;

import net.strong.castor.Castor;

public class Number2Timestamp extends Castor<Number, Timestamp> {

	@Override
	public Timestamp cast(Number src, Class<?> toType, String... args) {
		return new Timestamp(src.longValue());
	}

}
