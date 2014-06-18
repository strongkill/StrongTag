package net.strong.dao.entity.born;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;

import net.strong.dao.FieldMatcher;
import net.strong.dao.entity.Borning;

class ResultSetConstructorBorning implements Borning {

	Constructor<?> c;

	ResultSetConstructorBorning(Constructor<?> c) {
		this.c = c;
	}

	public Object born(ResultSet rs, FieldMatcher fm) throws Exception {
		return c.newInstance(rs);
	}

}
