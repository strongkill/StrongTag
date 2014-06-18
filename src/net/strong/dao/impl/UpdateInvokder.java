package net.strong.dao.impl;

import net.strong.dao.Dao;
import net.strong.dao.entity.Link;
import net.strong.lang.Each;
import net.strong.lang.ExitLoop;
import net.strong.lang.Lang;
import net.strong.lang.LoopException;

public class UpdateInvokder implements LinkInvoker {

	private Dao dao;

	UpdateInvokder(Dao dao) {
		this.dao = dao;
	}

	public void invoke(Link link, Object objSet) {
		Lang.each(objSet, new Each<Object>() {
			public void invoke(int i, Object ele, int length) throws ExitLoop, LoopException {
				dao.update(ele);
			}
		});
	}
}
