package net.strong.ioc.val;

import java.util.ArrayList;
import java.util.Collection;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;
import net.strong.ioc.meta.IocValue;
import net.strong.lang.Lang;

public class CollectionValue implements ValueProxy {

	private Class<? extends Collection<Object>> type;

	private ValueProxy[] values;

	@SuppressWarnings("unchecked")
	public CollectionValue(	IocMaking ing,
							Collection<IocValue> col,
							Class<? extends Collection<Object>> type) {
		this.type = (Class<? extends Collection<Object>>) (null == type ? ArrayList.class : type);
		values = new ValueProxy[col.size()];
		int i = 0;
		for (IocValue iv : col)
			values[i++] = ing.makeValue(iv);
	}

	public Object get(IocMaking ing) {
		try {
			Collection<Object> re = type.newInstance();
			for (ValueProxy vp : values)
				re.add(vp.get(ing));
			return re;
		}
		catch (Exception e) {
			throw Lang.wrapThrow(e);
		}
	}

}
