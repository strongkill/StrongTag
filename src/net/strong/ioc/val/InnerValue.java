package net.strong.ioc.val;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ObjectProxy;
import net.strong.ioc.ValueProxy;
import net.strong.ioc.meta.IocObject;

public class InnerValue implements ValueProxy {

	private IocObject iobj;

	public InnerValue(IocObject iobj) {
		this.iobj = iobj;
	}

	public Object get(IocMaking ing) {
		IocMaking innering = ing.clone(null);
		ObjectProxy op = ing.getObjectMaker().make(innering, iobj);
		return op.get(iobj.getType(), innering);
	}

}
