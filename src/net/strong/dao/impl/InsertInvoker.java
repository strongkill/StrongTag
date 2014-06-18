package net.strong.dao.impl;

import net.strong.dao.Dao;
import net.strong.dao.entity.Link;
import net.strong.dao.sql.Sql;
import net.strong.dao.sql.SqlMaker;
import net.strong.lang.Mirror;

public abstract class InsertInvoker implements LinkInvoker {

	Dao dao;
	Object mainObj;
	Mirror<?> mirror;

	public InsertInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		this.dao = dao;
		this.mainObj = mainObj;
		this.mirror = mirror;
	}

	protected void insertManyManyRelation(	final Link link,
											final Object fromValue,
											final Mirror<?> mta,
											Object ta) {
		Object toValue = mta.getValue(ta, link.getTargetField());
		SqlMaker maker = ((NutDao) dao).getSqlMaker();
		Sql sql = maker.insert_manymany(link);
		sql.params().set(link.getFrom(), fromValue).set(link.getTo(), toValue);
		dao.execute(sql);
	}

}
