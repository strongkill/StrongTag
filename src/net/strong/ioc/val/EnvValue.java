package net.strong.ioc.val;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;

public class EnvValue implements ValueProxy {

	private String name;

	public EnvValue(String name) {
		this.name = name;
	}

	public Object get(IocMaking ing) {
		return System.getenv(name);
	}

}
