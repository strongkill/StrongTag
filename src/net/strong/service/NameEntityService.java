package net.strong.service;

import net.strong.dao.Cnd;
import net.strong.dao.Dao;
import net.strong.dao.entity.EntityField;

public abstract class NameEntityService<T> extends EntityService<T> {

	public NameEntityService() {
		super();
	}

	public NameEntityService(Dao dao) {
		super(dao);
	}

	public NameEntityService(Dao dao, Class<T> entityType) {
		super(dao, entityType);
	}

	public int delete(String name) {
		return dao().delete(getEntityClass(), name);
	}

	public T fetch(String name) {
		return dao().fetch(getEntityClass(), name);
	}

	public boolean exists(String name) {
		EntityField ef = getEntity().getNameField();
		if (null == ef)
			return false;
		return dao().count(getEntityClass(), Cnd.where(ef.getName(), "=", name)) > 0;
	}

}
