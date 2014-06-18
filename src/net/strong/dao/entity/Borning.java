package net.strong.dao.entity;

import java.sql.ResultSet;

import net.strong.dao.FieldMatcher;

public interface Borning {

	Object born(ResultSet rs, FieldMatcher fm) throws Exception;

}
