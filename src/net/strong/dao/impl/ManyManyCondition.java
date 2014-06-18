package net.strong.dao.impl;

import net.strong.dao.Condition;
import net.strong.dao.Dao;
import net.strong.dao.Sqls;
import net.strong.dao.entity.Entity;
import net.strong.dao.entity.Link;

public class ManyManyCondition implements Condition {

	private Dao dao;
	private Link link;
	private Object obj;

	public ManyManyCondition(Dao dao, Link link, Object obj) {
		this.dao = dao;
		this.link = link;
		this.obj = obj;
	}

	public String toSql(Entity<?> me) {
		return String.format(	"%s IN (SELECT %s FROM %s WHERE %s=%s)",
								dao	.getEntity(link.getTargetClass())
									.getField(link.getTargetField().getName())
									.getColumnName(),
								link.getTo(),
								link.getRelation(),
								link.getFrom(),
								evalValue(me));
	}

	private Object evalValue(Entity<?> me) {
		return Sqls.formatFieldValue(me.getMirror().getValue(obj, link.getReferField()));
	}

}
