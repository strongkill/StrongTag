package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.Lang;
import net.strong.lang.Mirror;

public class Boolean2Number extends Castor<Boolean, Number> {

	@Override
	public Number cast(Boolean src, Class<?> toType, String... args) {
		try {
			return (Number) Mirror	.me(toType)
									.getWrapperClass()
									.getConstructor(String.class)
									.newInstance(src ? "1" : "0");
		}
		catch (Exception e) {
			throw Lang.wrapThrow(e);
		}
	}

}
