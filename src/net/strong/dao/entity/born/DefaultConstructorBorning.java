package net.strong.dao.entity.born;

import java.lang.reflect.Constructor;

import net.strong.dao.entity.Entity;

class DefaultConstructorBorning extends ReflectBorning {

	Constructor<?> c;

	DefaultConstructorBorning(Entity<?> entity, Constructor<?> c) {
		super(entity);
		this.c = c;
	}

	public Object create() throws Exception {
		return c.newInstance();
	}
}
