package net.strong.dao;

import net.strong.dao.entity.Entity;

public interface Expression {

	void render(StringBuilder sb, Entity<?> en);

}
