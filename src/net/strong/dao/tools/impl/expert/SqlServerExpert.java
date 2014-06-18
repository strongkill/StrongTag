package net.strong.dao.tools.impl.expert;

import net.strong.dao.Sqls;
import net.strong.dao.sql.ComboSql;
import net.strong.dao.sql.Sql;
import net.strong.dao.tools.DField;
import net.strong.dao.tools.DTable;
import net.strong.dao.tools.impl.SqlExpert;
import net.strong.lang.Strings;

public class SqlServerExpert implements SqlExpert {

	private static final String ALTER_PK = "ALTER TABLE ${T} WITH NOCHECK ADD CONSTRAINT PK_${T} PRIMARY KEY  NONCLUSTERED (${F})";

	public Sql evalCreateSql(DTable dt, Sql createTable) {
		if (dt.getPks().isEmpty())
			return createTable;

		// 有一个或多个 PK
		Sql alter = Sqls.create(Experts.gSQL(ALTER_PK, dt.getName(), dt.getPkNames()));
		ComboSql sql = new ComboSql();
		sql.add(createTable);
		sql.add(alter);
		return sql;
	}

	public Sql evalDropSql(DTable dt, Sql dropTable) {
		return dropTable;
	}

	public String tellCreateSqlPattern() {
		return Experts.CREATE_SQLSERVER;
	}

	private void appendFieldType(StringBuilder sb, DField df) {
		sb.append(' ');
		// BOOLEAN
		if ("boolean".equalsIgnoreCase(df.getType())) {
			sb.append("BIT");
		}
		// Date
		else if ("date".equalsIgnoreCase(df.getType())) {
			sb.append("DATETIME");
		}
		// Time
		else if ("time".equalsIgnoreCase(df.getType())) {
			sb.append("DATETIME");
		}
		// Timestamp
		else if ("timestamp".equalsIgnoreCase(df.getType())) {
			sb.append("DATETIME");
		}
		// Others
		else {
			sb.append(df.getType());
		}
	}

	public String tellField(int pkNum, DField df) {
		StringBuilder sb = new StringBuilder();
		// Name
		sb.append('"').append(df.getName()).append('"').append(' ');
		// Type
		appendFieldType(sb, df);
		// Decorator
		if (Experts.isInteger(df.getType()) && df.isUnsign())
			sb.append(" UNSIGNED");
		if (!df.isPrimaryKey() && df.isUnique())
			sb.append(" UNIQUE");
		if (df.isNotNull())
			sb.append(" NOT NULL");
		if (df.isAutoIncreament()) {
			sb.append(" IDENTITY");
		}
		// Default Value
		else if (!Strings.isBlank(df.getDefaultValue()))
			sb.append(" DEFAULT ").append(df.getDefaultValue());
		return sb.toString();
	}

	public String tellPKs(DTable dt) {
		return null;
	}

}
