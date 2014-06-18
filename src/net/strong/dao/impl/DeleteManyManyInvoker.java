package net.strong.dao.impl;

import net.strong.dao.entity.Entity;
import net.strong.dao.entity.Link;
import net.strong.dao.sql.Sql;
import net.strong.lang.Each;
import net.strong.lang.ExitLoop;
import net.strong.lang.Lang;
import net.strong.lang.LoopException;
import net.strong.lang.Mirror;

public class DeleteManyManyInvoker extends DeleteInvoker {

	DeleteManyManyInvoker(NutDao dao) {
		super(dao);
	}

	public void invoke(final Link link, Object mm) {
		Object first = Lang.first(mm);
		if (null != first) {
			final Entity<?> entity = dao.getEntity(first.getClass());
			Lang.each(mm, new Each<Object>() {
				public void invoke(int i, Object ta, int length) throws ExitLoop, LoopException {
					if (null != ta) {
						dao._deleteSelf(entity, ta);
						Object value = Mirror.me(ta.getClass()).getValue(ta, link.getTargetField());
						Sql sql = dao.getSqlMaker().clear_links(link.getRelation(),
																link.getTo(),
																link.getTo());
						sql.params().set(link.getTo(), value);
						dao.execute(sql);
					}
				}
			});
		}
	}

}
