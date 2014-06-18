package net.strong.service;

import java.util.List;

import net.strong.dao.Chain;
import net.strong.dao.Condition;
import net.strong.dao.Dao;
import net.strong.dao.entity.Entity;
import net.strong.dao.pager.Pager;
import net.strong.lang.Mirror;
import net.strong.log.Log;
import net.strong.log.Logs;

public abstract class EntityService<T> extends Service {

	private Mirror<T> mirror;

	private Log log = Logs.getLog(getClass());

	@SuppressWarnings("unchecked")
	public EntityService() {
		try {
			Class<T> entryClass = (Class<T>) Mirror.getTypeParam(getClass(),0);
			mirror = Mirror.me(entryClass);
			if (log.isDebugEnabled())
				log.debugf("Get TypeParams for self : %s", entryClass.getName());
		}
		catch (Throwable e) {
			if (log.isWarnEnabled())
				log.warn("!!!Fail to get TypeParams for self!", e);
		}
	}

	public EntityService(Dao dao) {
		this();
		this.setDao(dao);
	}

	public EntityService(Dao dao, Class<T> entityType) {
		setEntityType(entityType);
		setDao(dao);
	}

	public Mirror<T> mirror() {
		return mirror;
	}

	@SuppressWarnings("unchecked")
	public <C extends T> void setEntityType(Class<C> classOfT) {
		mirror = (Mirror<T>) Mirror.me(classOfT);
	}

	public Entity<T> getEntity() {
		return dao().getEntity(mirror.getType());
	}

	public Class<T> getEntityClass() {
		return mirror.getType();
	}

	public int clear(Condition condition) {
		return dao().clear(getEntityClass(), condition);
	}

	public int clear() {
		return dao().clear(getEntityClass(), null);
	}

	public List<T> query(Condition condition, Pager pager) {
		return (List<T>) dao().query(getEntityClass(), condition, pager);
	}

	public int count(Condition condition) {
		return dao().count(getEntityClass(), condition);
	}

	public int count() {
		return dao().count(getEntityClass());
	}

	public T fetch(Condition condition) {
		return dao().fetch(getEntityClass(), condition);
	}

	/**
	 * 复合主键专用
	 * 
	 * @param pks
	 *            键值
	 * @return 对象 T
	 */
	public T fetchx(Object... pks) {
		return dao().fetchx(getEntityClass(), pks);
	}

	/**
	 * 复合主键专用
	 * 
	 * @param pks
	 *            键值
	 * @return 对象 T
	 */
	public boolean exists(Object... pks) {
		return null != fetchx(pks);
	}

	public void update(Chain chain, Condition condition) {
		dao().update(getEntityClass(), chain, condition);
	}

	public void updateRelation(String regex, Chain chain, Condition condition) {
		dao().updateRelation(getEntityClass(), regex, chain, condition);
	}

	public int deletex(Object... pks) {
		return dao().deletex(getEntityClass(), pks);
	}
}
