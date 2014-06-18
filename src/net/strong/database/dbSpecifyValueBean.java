package net.strong.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import oracle.sql.CLOB;

import net.strong.util.CloseCon;
import net.strong.util.Helper;

/**
 * <p>Title: 从数据库中取指定的值</p>
 * <p>Description: 根据给定的参数，从数据库中取出所要的值，标签的参数说明请查看tld文档</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class dbSpecifyValueBean {

	//  Log log = LogFactory.getLog(this.getClass().getName());

	private String tableName = null;

	private String valueField = null;

	private String sqlWhere = null;

	private String isCount = "false";

	private String isSum = "false";

	private String sql = null;

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getValueField() {
		return valueField;
	}

	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	public String getSqlWhere() {
		return sqlWhere;
	}

	public void setIsCount(String isCount) {
		this.isCount = isCount;
	}

	public String getIsCount() {
		return isCount;
	}

	public void setIsSum(String isSum) {
		this.isSum = isSum;
	}

	public String getIsSum() {
		return isSum;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public String createSql() {
		String temp_sql = null;
		if (sql != null)
			temp_sql = sql;
		else if (tableName != null) {
			if (isCount != null && isCount.compareToIgnoreCase("true") == 0) {
				if (sqlWhere != null)
					temp_sql = "select count(*) as COUNT from " + tableName
							+ " where " + sqlWhere;
				else
					temp_sql = "select count(*) as COUNT from " + tableName;
			}
			if (valueField != null) {
				if (isSum != null && isSum.compareToIgnoreCase("true") == 0) {
					if (sqlWhere != null)
						temp_sql = "select sum(" + valueField
								+ ") as SUM from " + tableName + " where "
								+ sqlWhere;
					else
						temp_sql = "select sum(" + valueField
								+ ") as SUM from " + tableName;
				} else {
					if (sqlWhere != null)
						temp_sql = "select " + valueField + " from "
								+ tableName + " where " + sqlWhere;
					else
						temp_sql = "select " + valueField + " from "
								+ tableName;
				}
			}
		}
		return temp_sql;
	}

	/**
	 * 获取数据库查询的内容，内容将存放在HashMap对象中。本方法将通过参数pool连接池中获取数据库连接
	 * 运行速度比较快。
	 * @param pool
	 * @return
	 * @throws java.lang.Exception
	 */
	public HashMap<String, Object> getdbValue(Connection con /*PoolBean pool*/)
			throws Exception {
		/*    if(pool.getLog().isDebugEnabled())
		 pool.getLog().debug("star getdbValue");

		 String system_name = pool.getDatabaseInfo().getSystemName();
		 */
		HashMap<String, Object> result = null;
		String temp_sql =(sql==null?createSql():sql);
		if (temp_sql == null)
			throw new SQLException("not sql statement");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//Connection con = null;
		try {
			//con = DriverManager.getConnection(pageConte);
			//pool.getNoInfoConnection();// .getConnection();
			con.setReadOnly(true);
			long cost_1 = System.currentTimeMillis();			
			pstmt = con.prepareStatement(temp_sql);
			rs = pstmt.executeQuery();
			long cost_2 = System.currentTimeMillis();
			if(cost_2-cost_1 >= 1000*3){
			System.out.println("*********************=== PAY ATTENTION:SLOW SQL(Cost:" +(cost_2-cost_1)+ " Millis) ===*********************");
				System.out.println(temp_sql);
				System.out.println("**********************************************END*************************************************");
			}			
			//      rs = stmt.executeQuery(sql);
			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				if (rs.next()) {
					result = new HashMap<String, Object>();
					for (int i = 1; i <= colCount; i++) {
						String name = rsmd.getColumnLabel(i).toLowerCase();
						if(name==null)
							name = rsmd.getColumnName(i);
						
						String type_name = rsmd.getColumnTypeName(i).toLowerCase();
						if ("text".equals(type_name)) {
							//当字段类型为text时，如果直接用rs.getObject(i)取数据，在jtds1.0版本中不能直接取出，改为以下方式即可  2005-07-22 Strong Yuan
							String tt_str = rs.getString(i);
							//            System.out.println("text -- "+tt_str);
							if (tt_str == null)
								tt_str = "";
							else
								tt_str = tt_str.trim();
							result.put(name.trim(), tt_str);
						}else if("clob".equalsIgnoreCase(type_name)){
							try {
								CLOB tm = (CLOB)rs.getObject(i);
								String tmp = null;
								if(tm!=null){
									tmp =tm.getSubString(1,(int)tm.length()); 
									tm = null;
								}
								result.put(name.trim(), tmp);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}else if("date".equalsIgnoreCase(type_name)){
							Object value = rs.getTimestamp(i);
							result.put(name.trim(), value);							
						} else {
							Object obj_value = rs.getObject(i);
							if (obj_value instanceof java.lang.String) {
								String t_str = null;
								if (obj_value == null)
									t_str = "";
								else
									t_str = String.valueOf(obj_value).trim();
								result.put(name.trim(), t_str);
							} else {
								result.put(name.trim(), obj_value);
							}
						}
					}
				}
				rsmd = null;
			}
			Helper.cleanup(pstmt, rs);
			con.setReadOnly(false);
			Helper.cleanup(con);
		} catch (SQLException e) {
			Helper.cleanup(con);
			//pool.getLog().error(system_name+","+e.getMessage()+",sql:"+temp_sql);
			throw new SQLException("database perform error : " + e.getMessage()+",Sql:" + temp_sql);
		} finally {
			Helper.cleanup(con);
			//pool.realsePool();
			//pool = null;
		}

		return result;

	}

	/**
	 * 获取某一记录的hashMap对象
	 * @param con
	 * @return
	 * @throws java.lang.Exception
	 * @deprecated 由于直接将connection作为参数，导致出现数据库连接问题的可能性较大，所以今后不再使用此函数。
	 */
	/*
	 private HashMap getdbValue(Connection con) throws Exception
	 {
	 HashMap result = null;
	 String temp_sql = createSql();
	 if(temp_sql == null)
	 throw new SQLException("not sql statement");

	 Statement stmt = null;
	 ResultSet rs = null;

	 try
	 {

	 stmt = con.createStatement();
	 rs = stmt.executeQuery(temp_sql);
	 //      rs = stmt.executeQuery(sql);
	 if (rs != null) {
	 ResultSetMetaData rsmd = rs.getMetaData();
	 int colCount = rsmd.getColumnCount();
	 if (rs.next()) {
	 result = new HashMap();
	 for (int i = 1; i <= colCount; i++) {
	 String name = rsmd.getColumnName(i);
	 name = name.toLowerCase();
	 String type_name = rsmd.getColumnTypeName(i).toLowerCase();
	 //          System.out.println("field name : " + name + " , type_anme : " + type_name);
	 if ("text".equals(type_name)) {
	 //当字段类型为text时，如果直接用rs.getObject(i)取数据，在jtds1.0版本中不能直接取出，改为以下方式即可  2005-07-22 Strong Yuan
	 String tt_str = rs.getString(i);
	 //            System.out.println("text -- "+tt_str);
	 if (tt_str == null)
	 tt_str = "";
	 else
	 tt_str = tt_str.trim();
	 result.put(name.trim(), tt_str);
	 }
	 else {
	 Object obj_value = rs.getObject(i);
	 if (obj_value instanceof java.lang.String) {
	 String t_str = null;
	 if (obj_value == null)
	 t_str = "";
	 else
	 t_str = String.valueOf(obj_value).trim();
	 result.put(name.trim(), t_str);
	 }
	 else {
	 result.put(name.trim(), obj_value);
	 }
	 }
	 }
	 }
	 rsmd = null;
	 }
	 rs.close();
	 stmt.close();
	 stmt =null;
	 }
	 catch(SQLException e)
	 {
	 //      log.error("database perform error : " + e.getMessage());
	 //      log.error("sql:"+temp_sql);
	 //      ProDebug.addDebugLog("database perform error : " + e.getMessage());
	 //      ProDebug.addDebugLog("sql:"+temp_sql);
	 //      ProDebug.saveToFile();
	 throw new SQLException("database perform error : " + e.getMessage());
	 }
	 finally
	 {
	 CloseCon.Close(con);
	 }

	 return result;

	 }
	 */
	/**
	 * 获取数据库查询的内容，内容将存放在HashMap对象中。本方法将通过参数path读取数据库配置信息
	 * 并通过配置信息产生数据库连接（即将进行I/O操作）。
	 *　注： 本方法尽量不用
	 * @param path　数据库配置文件的路径。
	 * @return　返回一个HashMap对象。
	 * @throws java.lang.Exception
	 */
	/*
	 private HashMap getdbValue(String path) throws Exception
	 {
	 System.out.println("此函数须要进行 IO操作，请尽量不用");

	 HashMap result = null;
	 String temp_sql = createSql();
	 if(temp_sql == null)
	 throw new SQLException("not sql statement");

	 //    PoolBean pool = new PoolBean();
	 Connection con = null;
	 Statement stmt = null;
	 ResultSet rs = null;
	 InitDatabase initDb = new InitDatabase(path);
	 DatabaseInfo dbInfo = initDb.getDatabaseInfo();

	 try
	 {
	 Class.forName(dbInfo.getDriver());
	 con = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUsername(),
	 dbInfo.getPassword());
	 stmt = con.createStatement();
	 rs = stmt.executeQuery(temp_sql);
	 //      rs = stmt.executeQuery(sql);
	 if (rs != null) {
	 ResultSetMetaData rsmd = rs.getMetaData();
	 int colCount = rsmd.getColumnCount();
	 if (rs.next()) {
	 result = new HashMap();
	 for (int i = 1; i <= colCount; i++) {
	 String name = rsmd.getColumnName(i);
	 name = name.toLowerCase();
	 String type_name = rsmd.getColumnTypeName(i).toLowerCase();
	 //          System.out.println("field name : " + name + " , type_anme : " + type_name);
	 if ("text".equals(type_name)) {
	 //当字段类型为text时，如果直接用rs.getObject(i)取数据，在jtds1.0版本中不能直接取出，改为以下方式即可  2005-07-22 Strong Yuan
	 String tt_str = rs.getString(i);
	 //            System.out.println("text -- "+tt_str);
	 if (tt_str == null)
	 tt_str = "";
	 else
	 tt_str = tt_str.trim();
	 result.put(name.trim(), tt_str);
	 }
	 else {
	 Object obj_value = rs.getObject(i);
	 if (obj_value instanceof java.lang.String) {
	 String t_str = null;
	 if (obj_value == null)
	 t_str = "";
	 else
	 t_str = String.valueOf(obj_value).trim();
	 result.put(name.trim(), t_str);
	 }
	 else {
	 result.put(name.trim(), obj_value);
	 }
	 }
	 }
	 }
	 rsmd = null;
	 rs.close();
	 }
	 stmt.close();
	 }
	 catch(SQLException e)
	 {
	 //      log.error(e.getMessage());
	 //      log.error("sql:"+temp_sql);
	 throw new SQLException("database perform error : " + e.getMessage());
	 }
	 finally
	 {
	 CloseCon.Close(con);
	 rs = null;
	 stmt = null;
	 con = null;
	 }

	 return result;
	 }
	 */
	/**
	 * @deprecated
	 *  获取数据库查询的内容，内容将存放在HashMap对象中。本方法将通过参数dbinfo的数据库配置信息
	 * 产生数据库连接（通过Class.forNmae方式），不用进行I/O操作，不过通过Class.forName操作
	 * 也速度较慢。
	 * @param dbInfo
	 * @return
	 * @throws java.lang.Exception
	 */
	/*
	public HashMap<String, Object> getdbValue(DatabaseInfo dbInfo) throws Exception {
		HashMap<String, Object> result = null;
		String temp_sql = createSql();
		if (temp_sql == null)
			throw new SQLException("not sql statement");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(dbInfo.getDriver());
			con = DriverManager.getConnection(dbInfo.getUrl(), dbInfo
					.getUsername(), dbInfo.getPassword());
			pstmt = con.prepareStatement(temp_sql);
			rs = pstmt.executeQuery();
			//      rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			if (rs.next()) {
				result = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; i++) {
					String name = rsmd.getColumnName(i);
					name = name.toLowerCase();
					String type_name = rsmd.getColumnTypeName(i).toLowerCase();
					//          System.out.println("field name : " + name + " , type_anme : " + type_name);
					if ("text".equals(type_name)) {
						//当字段类型为text时，如果直接用rs.getObject(i)取数据，在jtds1.0版本中不能直接取出，改为以下方式即可  2005-07-22 Strong Yuan
						String tt_str = rs.getString(i);
						//            System.out.println("text -- "+tt_str);
						if (tt_str == null)
							tt_str = "";
						else
							tt_str = tt_str.trim();
						result.put(name.trim(), tt_str);
					} else {
						Object obj_value = rs.getObject(i);
						if (obj_value instanceof java.lang.String) {
							String t_str = null;
							if (obj_value == null)
								t_str = "";
							else
								t_str = String.valueOf(obj_value).trim();
							result.put(name.trim(), t_str);
						} else {
							result.put(name.trim(), obj_value);
						}
					}
				}
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (SQLException e) {
			//      log.error(e.getMessage());
			//      log.error("sql:"+temp_sql);
			throw new SQLException("database perform error : " + e.getMessage());
		} finally {
			CloseCon.Close(con);
		}

		return result;
	}*/
}
