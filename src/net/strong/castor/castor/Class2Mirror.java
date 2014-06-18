package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.Mirror;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Class2Mirror extends Castor<Class, Mirror> {

	@Override
	public Mirror<?> cast(Class src, Class toType, String... args) {
		return Mirror.me(src);
	}

}
