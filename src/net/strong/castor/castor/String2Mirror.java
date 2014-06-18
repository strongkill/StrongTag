package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class String2Mirror extends Castor<String, Mirror> {

	private static final String2Class castor = new String2Class();

	@Override
	public Mirror<?> cast(String src, Class<?> toType, String... args) {
		return Mirror.me(castor.cast(src, toType));
	}

}
