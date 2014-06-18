package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.Lang;
import net.strong.lang.Mirror;

public class Number2Number extends Castor<Number, Number> {

	@Override
	public Number cast(Number src, Class<?> toType, String... args) {
		try {
			return (Number) Mirror	.me(toType)
									.getWrapperClass()
									.getConstructor(String.class)
									.newInstance(src.toString());
		}
		catch (Exception e) {
			throw Lang.wrapThrow(e);
		}
	}

}
