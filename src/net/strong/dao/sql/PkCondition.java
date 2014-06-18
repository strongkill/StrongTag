package net.strong.dao.sql;

import net.strong.dao.Condition;
import net.strong.dao.Sqls;
import net.strong.dao.entity.Entity;
import net.strong.dao.entity.EntityField;

class PkCondition implements Condition {

	private Object[] args;

	PkCondition(Object[] args) {
		this.args = args;
	}

	public String toSql(Entity<?> entity) {
		StringBuilder sb = new StringBuilder();
		EntityField[] pks = entity.getPkFields();
		sb.append(pks[0].getColumnName()).append('=');
		sb.append(Sqls.formatFieldValue(args[0]));
		for (int i = 1; i < pks.length; i++) {
			sb.append(" AND ");
			sb.append(pks[i].getColumnName()).append('=');
			sb.append(Sqls.formatFieldValue(args[i]));
		}
		return sb.toString();
	}

}
