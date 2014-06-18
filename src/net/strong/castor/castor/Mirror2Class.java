package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Mirror2Class extends Castor<Mirror, Class> {

	@Override
	public Class cast(Mirror src, Class toType, String... args) {
		return src.getType();
	}

}
