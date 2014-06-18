package net.strong.dao.impl;

import net.strong.dao.Condition;
import net.strong.dao.Sqls;
import net.strong.dao.entity.Entity;
import net.strong.dao.entity.Link;

public class ManyCondition implements Condition {

	private Object value;
	private Link link;

	public ManyCondition(Link link, Object value) {
		this.link = link;
		this.value = value;
	}

	public String toSql(Entity<?> entity) {
		return String.format("%s=%s", entity.getField(link.getTargetField().getName())
											.getColumnName(), Sqls.formatFieldValue(value));
	}

}
