package net.strong.dao.impl;

import net.strong.dao.Dao;
import net.strong.dao.entity.Link;
import net.strong.lang.Each;
import net.strong.lang.Lang;
import net.strong.lang.Mirror;

public class InsertManyManyRelationInvoker extends InsertInvoker {

	public InsertManyManyRelationInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		super(dao, mainObj, mirror);
	}

	public void invoke(final Link link, Object mm) {
		Object first = Lang.first(mm);
		if (null != first) {
			final Object fromValue = mirror.getValue(mainObj, link.getReferField());
			final Mirror<?> mta = Mirror.me(first.getClass());
			Lang.each(mm, new Each<Object>() {
				public void invoke(int i, Object ta, int length) {
					insertManyManyRelation(link, fromValue, mta, ta);
				}
			});
		}
	}

}
