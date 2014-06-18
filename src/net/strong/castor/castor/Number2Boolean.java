package net.strong.castor.castor;

import net.strong.castor.Castor;

public class Number2Boolean extends Castor<Number, Boolean> {

	@Override
	public Boolean cast(Number src, Class<?> toType, String... args) {
		return src.toString().charAt(0) == '0' ? false : true;
	}

}
