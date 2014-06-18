package net.strong.dao.impl;

import java.lang.reflect.Field;

import net.strong.dao.Dao;
import net.strong.dao.entity.Link;
import net.strong.lang.Each;
import net.strong.lang.ExitLoop;
import net.strong.lang.Lang;
import net.strong.lang.Mirror;

public class InsertManyInvoker extends InsertInvoker {

	public InsertManyInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		super(dao, mainObj, mirror);
	}

	public void invoke(final Link link, Object many) {
		Object first = Lang.first(many);
		if (null != first) {
			Field refer = link.getReferField();
			if (null == refer) {
				Lang.each(many, new Each<Object>() {
					public void invoke(int index, Object ta, int size) throws ExitLoop {
						dao.insert(ta);
					}
				});
			} else {
				final Object value = mirror.getValue(mainObj, refer);
				final Mirror<?> mta = Mirror.me(first.getClass());
				Lang.each(many, new Each<Object>() {
					public void invoke(int index, Object ta, int size) throws ExitLoop {
						mta.setValue(ta, link.getTargetField(), value);
						dao.insert(ta);
					}
				});
			}
		}
	}
}
