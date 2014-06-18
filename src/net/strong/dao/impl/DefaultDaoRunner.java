package net.strong.dao.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import net.strong.dao.ConnCallback;
import net.strong.dao.ConnectionHolder;
import net.strong.dao.DaoRunner;
import net.strong.dao.Daos;

public class DefaultDaoRunner implements DaoRunner {

	public void run(DataSource dataSource, ConnCallback callback) {
		ConnectionHolder ch = Daos.getConnection(dataSource);
		try {
			ch.invoke(callback);
		}
		catch (Throwable e) {
			try {
				ch.rollback();
			}
			catch (SQLException e1) {}
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			else
				throw new RuntimeException(e);
		}
		finally {
			Daos.releaseConnection(ch);
		}
	}

}
