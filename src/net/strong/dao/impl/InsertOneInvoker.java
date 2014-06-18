package net.strong.dao.impl;

import net.strong.dao.Dao;
import net.strong.dao.entity.Link;
import net.strong.lang.Mirror;

public class InsertOneInvoker extends InsertInvoker {

	public InsertOneInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		super(dao, mainObj, mirror);
	}

	public void invoke(Link link, Object one) {
		one = this.dao.insert(one);
		Mirror<?> ta = Mirror.me(one.getClass());
		Object value = ta.getValue(one, link.getTargetField());
		mirror.setValue(mainObj, link.getReferField(), value);
	}
}
