package net.strong.castor.castor;

import java.util.Calendar;
import java.sql.Timestamp;

import net.strong.castor.Castor;

public class Calendar2Timestamp extends Castor<Calendar, Timestamp> {

	@Override
	public Timestamp cast(Calendar src, Class<?> toType, String... args) {
		long ms = src.getTimeInMillis();
		return new Timestamp(ms);
	}
}
