package net.strong.ioc.val;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;

public class ObjectNameValue implements ValueProxy {

	public Object get(IocMaking ing) {
		return ing.getObjectName();
	}

}
