package net.strong.castor.castor;

import java.util.Date;

import net.strong.castor.Castor;

public class Datetime2Long extends Castor<java.util.Date, Long> {

	@Override
	public Long cast(Date src, Class<?> toType, String... args) {
		return src.getTime();
	}

}
