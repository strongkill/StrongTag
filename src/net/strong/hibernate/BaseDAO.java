package net.strong.hibernate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.jsp.PageContext;
import javax.sql.DataSource;

import net.strong.bean.Constants;
import net.strong.bean.QueryObject;
import net.strong.dbcached.Memcached;
import net.strong.lang.StMap;
import net.strong.mongodb.BaseMongodb;
import net.strong.util.Helper;

import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class BaseDAO {
	private static DataSource dataSource;
	private static HibernateTemplate hibernateTemplate;
	private static JdbcTemplate jdbcTemplate;
	private static BaseMongodb mongodbService;

	/**
	 * 返回当前Session对象
	 * @return Session对象
	 */
	protected Session getSession(){
		return hibernateTemplate.getSessionFactory().getCurrentSession();
	}
	/**
	 * 返回 DataSource
	 * @return dataSource
	 */
	protected DataSource getDataSource(){
		return dataSource;
	}
	/**
	 * 返回 HibernateTemplate
	 * @return HibernateTemplate
	 */
	protected HibernateTemplate getHibernateTemplate(){
		return hibernateTemplate;
	}
	/**
	 * 返回jdbcTemplate
	 * @return jdbcTemplate
	 */
	protected JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	public void setDataSource(DataSource dataSource) {
		BaseDAO.dataSource = dataSource;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		BaseDAO.hibernateTemplate = hibernateTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		BaseDAO.jdbcTemplate = jdbcTemplate;
	}
	public void setMongodbService(BaseMongodb mongodbService) {
		BaseDAO.mongodbService = mongodbService;
	}
	protected BaseMongodb getMongodbService() {
		return mongodbService;
	}
	/**
	 * 保存对象到Memcached中，有效期是系统默认。
	 * @param key 保存的key(md5加密)
	 * @param o 保存的对象
	 */
	public void SetObject(String key,Object o, PageContext pageContext){
		SetObject(key, Constants.getMemcachedTimeOutSecond(pageContext), o,pageContext);
	}
	/**
	 *  保存对象到Memcached中，可以指定有效期
	 * @param key 保存的key(md5加密)
	 * @param timeout 有效期（以秒计算）
	 * @param o 保存的对象
	 */
	public void SetObject(String key,int timeout,Object o, PageContext pageContext){
		String servers = Constants.getMemcachedPools(pageContext);
		if(servers!=null)
			Memcached.getInstance(servers).set(key, timeout, o);
	}
	/**
	 * 读取Memcached中的对象。
	 * @param key 保存对象的key(md5加密)
	 * @return
	 */
	public Object GetObject(String key, PageContext pageContext){
		String servers = Constants.getMemcachedPools(pageContext);
		if(servers!=null)
			return Memcached.getInstance(servers).get(key);
		return null;
	}




	/**
	 * 根据传入的QueryObject对象,将数据集转换成ArrayList<HashMap<String,Object>>;
	 * @param tablename 表名
	 * @param columns 字段列表
	 * @param sql_where where条件
	 * @param sql_orderby orderby条件
	 * @param sql_groupby groupby条件
	 * @param startrow 起始行
	 * @param maxRow 返回记录数
	 * @return ArrayList<HashMap<String, Object>>
	 *
	 * 使用方法:<br>
	 * String columns[] = {"article_subject","article_id"};<br>
	 * 	ArrayList<HashMap<String,Object>> al = QueryForList("articles",columns,"member_id=17879","article_id desc",null,10,20);<br>
	 *  while(al.size()>0){<br>
	 *  	HashMap<String,Object>> o = al.remove(0);<br>
	 *  	String article_subject = (String)o.get("article_subject");<br>
	 *  	int article_id = Integer.pastInt(o.get("article_id").toString());<br>
	 *  }<br>
	 * @author Strong Yuan
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(String tablename,
			String[] columns, String sql_where, String sql_orderby,
			String sql_groupby, int startrow, int maxRow) {
		return QueryForList(new QueryObject(tablename, columns, sql_where,
				sql_orderby, sql_groupby, startrow, maxRow));
	}

	/**
	 * 根据传入的QueryObject对象,将数据集转换成ArrayList<HashMap<String,Object>>,支持事务;
	 * @param tablename 表名
	 * @param columns 字段列表
	 * @param sql_where where条件
	 * @param sql_orderby orderby条件
	 * @param sql_groupby groupby条件
	 * @param startrow 起始行
	 * @param maxRow 返回记录数
	 * @param con Connection 数据库连接
	 * @return ArrayList<HashMap<String, Object>>
	 *
	 * 使用方法:<br>
	 * String columns[] = {"article_subject","article_id"};<br>
	 * 	ArrayList<HashMap<String,Object>> al = QueryForList("articles",columns,"member_id=17879","article_id desc",null,10,20);<br>
	 *  while(al.size()>0){<br>
	 *  	HashMap<String,Object>> o = al.remove(0);<br>
	 *  	String article_subject = (String)o.get("article_subject");<br>
	 *  	int article_id = Integer.pastInt(o.get("article_id").toString());<br>
	 *  }<br>
	 * @author Strong Yuan
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(String tablename,
			String[] columns, String sql_where, String sql_orderby,
			String sql_groupby, int startrow, int maxRow,Connection con) {
		return QueryForList(new QueryObject(tablename, columns, sql_where,
				sql_orderby, sql_groupby, startrow, maxRow),con);
	}
	/**
	 * 根据指定条件,将数据集转换成ArrayList<HashMap<String,Object>>
	 * @param q QueryObject
	 * @return ArrayList<HashMap<String, Object>>
	 *
	 * 使用方法:<br>
	 * 	ArrayList<HashMap<String,Object>> al = new ArrayList<HashMap<String,Object>>();<br>
	 * 	String columns[] = {"article_subject","article_id"};<br>
	 * 	QueryObject q = new QueryObject("articles",columns,"member_id=17879","article_id desc",null,10,20);<br>
	 * 	al = QueryForList(q);<br>
	 	 *  while(al.size()>0){<br>
	 *  	HashMap<String,Object>> o = al.remove(0);<br>
	 *  	String article_subject = (String)o.get("article_subject");<br>
	 *  	int article_id = Integer.pastInt(o.get("article_id").toString());<br>
	 *  }<br>
	 * @author String Yuan
	 * @see QueryObject
	 *
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(QueryObject q) {
		if (q.getTablename() == null)
			return null;
		if (q.getColumns() == null || q.getColumns().length <= 0)
			return null;
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		Connection con = null;
		try{
			con = getDataSource().getConnection();
			al = QueryForList(q,con);
			Helper.cleanup(con);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Helper.cleanup(con);
		}
		return al;
	}
	/**
	 * 根据指定条件,将数据集转换成ArrayList<HashMap<String,Object>>,支持事务处理
	 * @param q QueryObject
	 * @param con Connection
	 * @return ArrayList<HashMap<String, Object>>
	 *
	 * 使用方法:<br>
	 * 	ArrayList<HashMap<String,Object>> al = new ArrayList<HashMap<String,Object>>();<br>
	 * 	String columns[] = {"article_subject","article_id"};<br>
	 * 	QueryObject q = new QueryObject("articles",columns,"member_id=17879","article_id desc",null,10,20);<br>
	 * 	al = QueryForList(q);<br>
	 	 *  while(al.size()>0){<br>
	 *  	HashMap<String,Object>> o = al.remove(0);<br>
	 *  	String article_subject = (String)o.get("article_subject");<br>
	 *  	int article_id = Integer.pastInt(o.get("article_id").toString());<br>
	 *  }<br>
	 * @author String Yuan
	 * @see QueryObject
	 *
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(QueryObject q,Connection con) {

		if (q.getTablename() == null)
			return null;
		if (q.getColumns() == null || q.getColumns().length <= 0)
			return null;
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();

		//		Connection con = null;
		try {
			if(Constants.database_type<0)
				checkDatabase(con);
			/*
			StringBuffer sqlFields = new StringBuffer();
			for(int i=0;i<q.getColumns().length;i++){
				sqlFields.append(q.getColumns()[i]);
				if(q.getColumns().length>i+1)
					sqlFields.append(",");
			}

			dbTag t = new dbTag();
			String columns_p = t.FixSqlFieldsStarChar("res.rn,", "res.",
					sqlFields.toString());
			String columns = t.FixSqlFieldsStarChar(null, "res_s.",
					sqlFields.toString());

			StringBuffer sql = new StringBuffer();
			sql.append("select ").append(columns_p).append(" from (")
					.append("select ROWNUM RN,").append(columns).append(" from (")
					.append("select ").append(sqlFields.toString())
					.append(" from ").append(q.getTablename());
			if (q.getSql_where() != null && q.getSql_where().length() > 0)
				sql.append(" where ").append(q.getSql_where());
			if (q.getSql_orderby() != null && q.getSql_orderby().length() > 0)
				sql.append(" order by ").append(q.getSql_orderby());
			sql.append(")res_s where rownum<=").append(q.getStartrow() + q.getMaxRow())
					.append(") res where rn>")
					.append(q.getStartrow());
			 */
			StringBuffer sql = q.getQuerySql();
			//con = getDataSource().getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			al = StMap.ResultSetToList(rs);
			/*
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rs.getMetaData().getColumnCount();
			HashMap<String, Object> hm = null;
			while (rs.next()) {
				hm = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; i++) {
					String name = rsmd.getColumnLabel(i).toLowerCase();
					if (name == null || name.length() == 0)
						name = rsmd.getColumnName(i).toLowerCase();
					String type_name = rsmd.getColumnTypeName(i).toLowerCase();
					if ("text".equals(type_name)) {
						String tt_str = rs.getString(i);
						if (tt_str == null)
							tt_str = "";
						else
							tt_str = tt_str.trim();
						hm.put(name.trim(), tt_str);
					} else if ("clob".equalsIgnoreCase(type_name)) {
						try {
							CLOB tm = (CLOB) rs.getObject(i);
							String tmp = null;
							if (tm != null) {
								tmp = tm.getSubString(1, (int) tm.length());
								tm = null;
							}
							hm.put(name.trim(), tmp);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						Object obj = rs.getObject(i);
						if (obj instanceof java.lang.String) {
							String t_str = null;
							t_str = String.valueOf(obj).trim();
							hm.put(name.trim(), t_str);
						} else {
							hm.put(name.trim(), obj);
						}
					}
				}
				al.add(hm);
			}*/
			Helper.cleanup(stmt, rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//Helper.cleanup(con);
		}
		return al;
	}



	/**
	 * 根据条件,将数据库对像转换了HashMap<String,Object>对象.
	 *
	 * @param table_name
	 *            表名
	 * @param columns
	 *            字段列表
	 * @param sql_where
	 *            where 条件
	 * @return HashMap<String,Object>
	 *
	 *
	 * 使用方法:<br>
	 * String columns[] = {"absname","truename"};<br>
	 * HashMap<String,Object> hm = QueryForHashMap("member",columns,"member_id=17879")<br>
	 * String absname = (String)hm.get("absname");<br>
	 * String truename = (String)hm.get("truename");<br>
	 *
	 *@author Strong Yuan
	 */
	public HashMap<String, Object> QueryForHashMap(String tablename,
			String[] columns, String sql_where) {
		return QueryForHashMap(new QueryObject(tablename, columns, sql_where,
				null, null, 0, 0));
	}

	/**
	 * 根据条件,将数据库对像转换了HashMap<String,Object>对象.支持事务
	 *
	 * @param table_name
	 *            表名
	 * @param columns
	 *            字段列表
	 * @param sql_where
	 *            where 条件
	 * @param con Connection数据库连接
	 * @return HashMap<String,Object>
	 *
	 *
	 * 使用方法:<br>
	 * String columns[] = {"absname","truename"};<br>
	 * HashMap<String,Object> hm = QueryForHashMap("member",columns,"member_id=17879")<br>
	 * String absname = (String)hm.get("absname");<br>
	 * String truename = (String)hm.get("truename");<br>
	 *
	 *@author Strong Yuan
	 */
	public HashMap<String, Object> QueryForHashMap(String tablename,
			String[] columns, String sql_where,Connection con) {
		return QueryForHashMap(new QueryObject(tablename, columns, sql_where,
				null, null, 0, 0),con);
	}

	/**
	 * 根据传入的QueryObject对象,将数据库对像转换了HashMap<String,Object>对象.
	 *
	 * @param q
	 *            QueryObject对象
	 * @return
	 *
	 * 使用方法:<br>
	 * String columns[] = {"absname","truename"};<br>
	 * QueryObject q = new QueryObject("member",columns,"member_id=17879",null,null,0,0);<br>
	 * HashMap<String,Object> hm = QueryForHashMap(q)<br>
	 * String absname = (String)hm.get("absname");<br>
	 * String truename = (String)hm.get("truename");<br>
	 *@author Strong Yuan
	 * @see QueryObject
	 */
	public HashMap<String, Object> QueryForHashMap(QueryObject q) {
		if (q.getTablename() == null)
			return null;
		if (q.getColumns() == null || q.getColumns().length <= 0)
			return null;

		HashMap<String, Object> hm = new HashMap<String, Object>();
		Connection con = null;
		try{
			con = getDataSource().getConnection();
			hm = QueryForHashMap(q,con);
			Helper.cleanup(con);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Helper.cleanup(con);
		}
		return hm;
	}

	/**
	 * 根据传入的QueryObject对象,将数据库对像转换了HashMap<String,Object>对象.支持事务
	 *
	 * @param q
	 *            QueryObject对象
	 * @param con Connection 数据库连接
	 * @return
	 *
	 * 使用方法:<br>
	 * String columns[] = {"absname","truename"};<br>
	 * QueryObject q = new QueryObject("member",columns,"member_id=17879",null,null,0,0);<br>
	 * HashMap<String,Object> hm = QueryForHashMap(q)<br>
	 * String absname = (String)hm.get("absname");<br>
	 * String truename = (String)hm.get("truename");<br>
	 *@author Strong Yuan
	 * @see QueryObject
	 */
	public HashMap<String, Object> QueryForHashMap(QueryObject q,Connection con) {
		if (q.getTablename() == null)
			return null;
		if (q.getColumns() == null || q.getColumns().length <= 0)
			return null;

		HashMap<String, Object> hm = new HashMap<String, Object>();
		//Connection con = null;
		try {
			if(Constants.database_type<0)
				checkDatabase(con);
			/*
			StringBuffer sqlFields = new StringBuffer();
			for(int i=0;i<q.getColumns().length;i++){
				sqlFields.append(q.getColumns()[i]);
				if(q.getColumns().length>i+1)
					sqlFields.append(",");
			}

			dbTag t = new dbTag();
			String columns = t.FixSqlFieldsStarChar(null, "res.",
					sqlFields.toString());

			StringBuffer sql = new StringBuffer();
			sql.append("select ").append(columns).append(" from (")
					.append("select ").append(sqlFields.toString())
					.append(" from ").append(q.getTablename());
			if (q.getSql_where() != null && q.getSql_where().length() > 0)
				sql.append(" where ").append(q.getSql_where());
			if (q.getSql_orderby() != null && q.getSql_orderby().length() > 0)
				sql.append(" order by ").append(q.getSql_orderby());
			sql.append(")res where rownum<=1");
			 */
			StringBuffer sql = q.getQuerySql();
			//con = getDataSource().getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			hm = StMap.ResultSetToHashMap(rs);
			/*
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rs.getMetaData().getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					String name = rsmd.getColumnLabel(i).toLowerCase();
					if (name == null || name.length() == 0)
						name = rsmd.getColumnName(i).toLowerCase();
					String type_name = rsmd.getColumnTypeName(i).toLowerCase();
					if ("text".equals(type_name)) {
						String tt_str = rs.getString(i);
						if (tt_str == null)
							tt_str = "";
						else
							tt_str = tt_str.trim();
						hm.put(name.trim(), tt_str);
					} else if ("clob".equalsIgnoreCase(type_name)) {
						try {
							CLOB tm = (CLOB) rs.getObject(i);
							String tmp = null;
							if (tm != null) {
								tmp = tm.getSubString(1, (int) tm.length());
								tm = null;
							}
							hm.put(name.trim(), tmp);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						Object obj = rs.getObject(i);
						if (obj instanceof java.lang.String) {
							String t_str = null;
							t_str = String.valueOf(obj).trim();
							hm.put(name.trim(), t_str);
						} else {
							hm.put(name.trim(), obj);
						}
					}
				}
			}
			 */
			Helper.cleanup(stmt, rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//Helper.cleanup(con);
		}
		return hm;
	}


	public int Update(String tablename,List<HashMap<String,Object>> column_values,List<HashMap<String,Object>> where_values,Connection con) throws Exception{
		/*if(column_values==null || column_values.size()<=0)return 0;
		PreparedStatement pstmt = con.prepareStatement("");
*/
		return 0;
	}
	public int Update(String tablename,HashMap<String,Object> column_value,HashMap<String,Object> where_value,Connection con){
		return 0;
	}

	private synchronized void checkDatabase(Connection con) throws Exception {
		DatabaseMetaData dmd = con.getMetaData();

		String proName = dmd.getDatabaseProductName().toLowerCase();
		if (proName.startsWith("microsoft sql")) {
			Constants.database_type = 0;
		} else if (proName.startsWith("postgresql")) {
			Constants.database_type = 2;
		} else if (proName.startsWith("mysql")) {
			Constants.database_type = 1;
		} else if (proName.startsWith("oracle")) {
			Constants.database_type = 3;
		} else if (proName.startsWith("db2")) {
			Constants.database_type = 4;
		} else if (proName.equals("h2")) {
			Constants.database_type = 5;
		} else {
			Constants.database_type = 6;
		}
	}


}
