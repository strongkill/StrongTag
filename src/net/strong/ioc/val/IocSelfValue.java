package net.strong.ioc.val;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;

public class IocSelfValue implements ValueProxy {

	public Object get(IocMaking ing) {
		return ing.getIoc();
	}

}
