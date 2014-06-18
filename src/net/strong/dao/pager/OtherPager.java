package net.strong.dao.pager;

import static java.lang.String.format;

import java.sql.ResultSet;

import net.strong.dao.entity.Entity;

public class OtherPager extends AbstractPager {

	@Override
	public int getResultSetType() {
		return ResultSet.TYPE_SCROLL_INSENSITIVE;
	}

	public String toSql(Entity<?> entity, String fields, String cnd) {
		return format("SELECT %s FROM %s %s", fields, entity.getViewName(), cnd);
	}

}
