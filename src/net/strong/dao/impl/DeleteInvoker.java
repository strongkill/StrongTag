package net.strong.dao.impl;

public abstract class DeleteInvoker implements LinkInvoker {

	protected NutDao dao;

	DeleteInvoker(NutDao dao) {
		this.dao = dao;
	}

}
