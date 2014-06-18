package net.strong.dao.impl;

import java.sql.Connection;

import javax.sql.DataSource;

import net.strong.dao.ConnCallback;
import net.strong.dao.DaoExecutor;
import net.strong.dao.DaoRunner;
import net.strong.dao.sql.Sql;
import net.strong.log.Log;
import net.strong.log.Logs;

public class DefaultDaoExecutor implements DaoExecutor {

	private static Log log = Logs.getLog(DefaultDaoExecutor.class);

	public void execute(DataSource dataSource, DaoRunner runner, final Sql... sqls) {
		if (null != sqls) {
			runner.run(dataSource, new ConnCallback() {
				public void invoke(Connection conn) throws Exception {
					// Store the old auto commit setting
					boolean isAuto = conn.getAutoCommit();
					// If multiple SQL, change the auto commit
					if (isAuto && sqls.length > 1)
						conn.setAutoCommit(false);

					// 打印 LOG
					if (log.isDebugEnabled()) {
						for (int i = 0; i < sqls.length; i++) {
							if (null != sqls[i]) {
								log.debug(sqls[i].toString());
								sqls[i].execute(conn);
							}
						}
					}
					// 不打印
					else {
						for (int i = 0; i < sqls.length; i++)
							if (null != sqls[i])
								sqls[i].execute(conn);
					}
				}
			});
		}
	}

}
