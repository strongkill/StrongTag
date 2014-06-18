package net.strong.castor;

import java.text.SimpleDateFormat;

import net.strong.castor.castor.DateTimeCastor;

class DefaultCastorSetting {

	public void setup(DateTimeCastor<?, ?> c) {
		c.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		c.setTimeFormat(new SimpleDateFormat("HH:mm:ss"));
		c.setDateTimeFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

}
