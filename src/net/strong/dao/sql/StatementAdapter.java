package net.strong.dao.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.strong.dao.entity.Entity;

public interface StatementAdapter {

	void process(PreparedStatement stat, SqlLiteral sql, Entity<?> entity) throws SQLException;

}
