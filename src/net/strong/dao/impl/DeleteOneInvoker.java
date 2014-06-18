package net.strong.dao.impl;

import net.strong.dao.entity.Link;

public class DeleteOneInvoker extends DeleteInvoker {

	DeleteOneInvoker(NutDao dao) {
		super(dao);
	}

	public void invoke(Link link, Object one) {
		dao.delete(one);
	}

}
