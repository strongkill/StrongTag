package net.strong.bean;

import net.strong.exception.UnSupportDataBaseException;
import net.strong.lang.Strings;

/**
 * 查询sql对象
 * @author Strong Yuan
 * @Date 2010-5-17
 * @Version
 */
public class QueryObject {
	/**
	 * 表名
	 */
	private String tablename;
	/**
	 * 字段列表
	 */
	private String[] columns;

	/**
	 * where条件
	 */
	private String sql_where;
	/**
	 * Order By 字段
	 */
	private String sql_orderby;
	/**
	 * Group by 字段
	 */
	private String sql_groupby;
	/**
	 * 起始行,不包含本行
	 */
	private int startrow;
	/**
	 * 返回记录数
	 */
	private int maxRow;
	public QueryObject(){

	}

	/**
	 * 返回对像对应的SQL语句.
	 * @return
	 */
	public StringBuffer getQuerySql() throws Exception{
		return getQuerySql(Constants.database_type);
	}
	/**
	 * 返回对像对应的SQL语句.
	 * @param database_type 0-MS SQL,1 - MySQL , 2 - postgreSQL , 3 - Oracle
	 * @return
	 * @throws Exception 
	 */
	public StringBuffer getQuerySql(int database_type) throws Exception{

		StringBuffer sqlFields = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		for(int i=0;i<getColumns().length;i++){
			sqlFields.append(getColumns()[i]);
			if(getColumns().length>i+1)
				sqlFields.append(",");
		}
		switch(database_type){
		case 0:
			break;
		case 1:
			sql.append("select ").append(sqlFields.toString()).append(" from ").append(getTablename());;
			if(getSql_where() != null && getSql_where().length()>0)
				sql.append(" where ").append(getSql_where());
			if(getSql_orderby() != null && getSql_orderby().length()>0)
				sql.append(" order by ").append(getSql_orderby());
			if(getSql_groupby() != null && getSql_groupby().length()>0)
				sql.append(" group by ").append(getSql_groupby());
			if(getStartrow()>0){
				sql.append(" limit ").append(getStartrow()).append(",").append(getMaxRow());
			}else{
				sql.append(" limit 0,").append(getMaxRow()>0?getMaxRow():1);
			}
			break;
		case 2:
			break;
		case 3:
			if(getStartrow()>0){
				String columns_p = Strings.FixSqlFieldsStarChar("res.rn,", "res.",
						sqlFields.toString());
				String columns = Strings.FixSqlFieldsStarChar(null, "res_s.",
						sqlFields.toString());
				sql.append("select ").append(columns_p).append(" from (")
				.append("select ROWNUM RN,").append(columns).append(" from (")
				.append("select ").append(sqlFields.toString())
				.append(" from ").append(getTablename());
				if (getSql_where() != null && getSql_where().length() > 0)
					sql.append(" where ").append(getSql_where());
				if (getSql_orderby() != null && getSql_orderby().length() > 0)
					sql.append(" order by ").append(getSql_orderby());
				sql.append(")res_s where rownum<=").append(getStartrow() + getMaxRow())
				.append(") res where rn>")
				.append(getStartrow());
			}else{
				String columns = Strings.FixSqlFieldsStarChar(null, "res.",
						sqlFields.toString());
				sql.append("select ").append(columns).append(" from (")
				.append("select ").append(sqlFields.toString())
				.append(" from ").append(getTablename());
				if (getSql_where() != null && getSql_where().length() > 0)
					sql.append(" where ").append(getSql_where());
				if (getSql_orderby() != null && getSql_orderby().length() > 0)
					sql.append(" order by ").append(getSql_orderby());
				sql.append(")res where rownum<=").append(getMaxRow()>0?getMaxRow():1);
			}
			break;
		default:
			throw new UnSupportDataBaseException("不支持的数据库类型:" + database_type);
		}

		return sql;
	}


	/**
	 * 查询sql对象
	 * @param tablename 表名
	 * @param columns 字段列表
	 * @param sql_where where条件
	 * @param sql_orderby orderby条件
	 * @param sql_groupby groupby条件
	 * @param startrow 起始行
	 * @param maxRow 返回记录数
	 */
	public QueryObject(String tablename,String[] columns,String sql_where,String sql_orderby,String sql_groupby,int startrow,int maxRow){
		this.tablename= tablename;
		this.columns = columns;
		this.sql_where = sql_where;
		this.sql_orderby = sql_orderby;
		this.sql_groupby = sql_groupby;
		this.startrow = startrow;
		this.maxRow = maxRow;

	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public String getSql_where() {
		return sql_where;
	}
	public void setSql_where(String sql_where) {
		this.sql_where = sql_where;
	}

	public String getSql_orderby() {
		return sql_orderby;
	}
	public void setSql_orderby(String sql_orderby) {
		this.sql_orderby = sql_orderby;
	}
	public String getSql_groupby() {
		return sql_groupby;
	}
	public void setSql_groupby(String sql_groupby) {
		this.sql_groupby = sql_groupby;
	}
	public int getStartrow() {
		return startrow;
	}
	public void setStartrow(int startrow) {
		this.startrow = startrow;
	}
	public int getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	public static void main(String[] args) throws Exception{
		String columns[] ={"a.article_id","a.member_id","a.article_subject","a.tags","a.cr_date","ad.article_content"};
		int starwith =300;
		int maxrow = 200;

		QueryObject q = new QueryObject("articles a,articles_details ad", columns, "a.article_id=ad.article_id and a.checked=1 and a.article_id>=" + 100+" and a.article_id<="+10, "a.article_id", null, starwith, maxrow);
		System.out.println(q.getQuerySql(3));

	}
}
