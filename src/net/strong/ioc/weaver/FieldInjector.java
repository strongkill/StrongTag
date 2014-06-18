package net.strong.ioc.weaver;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;
import net.strong.lang.Mirror;
import net.strong.lang.inject.Injecting;

public class FieldInjector {

	public static FieldInjector create(Mirror<?> mirror, String fieldName, ValueProxy vp) {
		FieldInjector fi = new FieldInjector();
		fi.valueProxy = vp;
		fi.inj = mirror.getInjecting(fieldName);
		return fi;
	}

	private ValueProxy valueProxy;
	private Injecting inj;

	private FieldInjector() {}

	void inject(IocMaking ing, Object obj) {
		Object value = valueProxy.get(ing);
		inj.inject(obj, value);
	}
}
