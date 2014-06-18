package net.strong.ioc.val;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;

public class StaticValue implements ValueProxy {

	private Object obj;

	public StaticValue(Object obj) {
		this.obj = obj;
	}

	public Object get(IocMaking ing) {
		return obj;
	}

}
