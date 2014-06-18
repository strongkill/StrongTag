package net.strong.castor.castor;

import java.text.DateFormat;

import net.strong.castor.Castor;

public abstract class DateTimeCastor<FROM, TO> extends Castor<FROM, TO> {

	public DateFormat dateTimeFormat;
	public DateFormat dateFormat;
	public DateFormat timeFormat;

	public void setDateTimeFormat(DateFormat format) {
		this.dateTimeFormat = format;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setTimeFormat(DateFormat timeFormat) {
		this.timeFormat = timeFormat;
	}

}
