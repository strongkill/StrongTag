package net.strong.castor.castor;

import net.strong.castor.Castor;

@SuppressWarnings({"rawtypes"})
public class Class2String extends Castor<Class, String> {

	@Override
	public String cast(Class src, Class<?> toType, String... args) {
		return src.getName();
	}

}
