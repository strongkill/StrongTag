package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Mirror2String extends Castor<Mirror, String> {

	@Override
	public String cast(Mirror src, Class<?> toType, String... args) {
		return src.getType().getName();
	}

}
