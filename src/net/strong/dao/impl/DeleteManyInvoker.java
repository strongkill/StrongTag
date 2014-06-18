package net.strong.dao.impl;

import net.strong.dao.entity.Entity;
import net.strong.dao.entity.Link;
import net.strong.lang.Each;
import net.strong.lang.ExitLoop;
import net.strong.lang.Lang;
import net.strong.lang.LoopException;

public class DeleteManyInvoker extends DeleteInvoker {

	DeleteManyInvoker(NutDao dao) {
		super(dao);
	}

	public void invoke(Link link, Object many) {
		Object first = Lang.first(many);
		if (null != first) {
			final Entity<?> entity = dao.getEntity(first.getClass());
			Lang.each(many, new Each<Object>() {
				public void invoke(int i, Object ele, int length) throws ExitLoop, LoopException {
					dao._deleteSelf(entity, ele);
				}
			});
		}
	}
}
