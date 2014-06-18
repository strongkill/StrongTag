package net.strong.castor.castor;

import java.util.Calendar;

import net.strong.castor.Castor;

public class Calendar2Long extends Castor<Calendar, Long> {
	@Override
	public Long cast(Calendar src, Class<?> toType, String... args) {
		return src.getTimeInMillis();
	}
}
