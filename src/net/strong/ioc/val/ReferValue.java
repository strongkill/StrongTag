package net.strong.ioc.val;

import net.strong.ioc.Ioc;
import net.strong.ioc.Ioc2;
import net.strong.ioc.IocMaking;
import net.strong.ioc.Iocs;
import net.strong.ioc.ValueProxy;
import net.strong.lang.meta.Pair;

public class ReferValue implements ValueProxy {

	private String name;
	private Class<?> type;

	public ReferValue(String name) {
		Pair<Class<?>> p = Iocs.parseName(name);
		this.name = p.getName();
		this.type = p.getValue();
	}

	public Object get(IocMaking ing) {
		Ioc ioc = ing.getIoc();
		if (ioc instanceof Ioc2)
			return ((Ioc2)ioc).get(type, name,ing.getContext());
		return ioc.get(type, name);
	}

}
