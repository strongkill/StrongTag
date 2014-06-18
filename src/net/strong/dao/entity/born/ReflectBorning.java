package net.strong.dao.entity.born;

import java.sql.ResultSet;
import java.util.Iterator;

import net.strong.dao.FieldMatcher;
import net.strong.dao.entity.Borning;
import net.strong.dao.entity.Entity;
import net.strong.dao.entity.EntityField;

abstract class ReflectBorning implements Borning {
	
	Entity<?> entity;

	ReflectBorning(Entity<?> entity) {
		this.entity = entity;
	}

	abstract Object create() throws Exception;

	public Object born(ResultSet rs, FieldMatcher fm) throws Exception {
		Object obj = create();
		Iterator<EntityField> it = entity.fields().iterator();
		while (it.hasNext()) {
			EntityField ef = it.next();
			if (null == fm || fm.match(ef.getField().getName()))
				ef.fillValue(obj, rs);
		}
		return obj;
	}
	
}
