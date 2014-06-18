package net.strong.dao.tools;

import net.strong.dao.sql.Sql;

public interface TableDefinition {

	public abstract Sql makeCreateSql(DTable dt);

	public abstract Sql makeDropSql(DTable dt);

}