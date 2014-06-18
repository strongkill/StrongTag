package net.strong.struts;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.sql.DataSource;

import net.strong.bean.Constants;
import net.strong.bean.QueryObject;
import net.strong.dbcached.Memcached;
import net.strong.exutil.htmlFilter;
import net.strong.hibernate.BaseDAO;
import net.strong.mongodb.BaseMongodb;
import net.strong.util.Helper;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * <p>Title: BlogAction.java</p>
 * <p>Description:BlogAction</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  cn.qt </p>
 * @author Strong Yuan
 * @sina 2007-6-21
 * @version 1.0
 */
public class BaseAction extends DispatchAction {
	private static ApplicationContext ctx = null;
	protected static String Des_key = null;
	private static BaseDAO dao = new BaseDAO();

	protected Object GetObject(HttpServletRequest request,String name){
		return request.getSession().getServletContext().getAttribute(name);
	}
	protected void SaveObject(HttpServletRequest request,String name,Object obj){
		request.getSession().getServletContext().setAttribute(name, obj);
	}
	protected String getDES_KEY(){
		if(Des_key==null)
			Des_key = Constants.getDes_key(servlet.getServletContext());

		return Des_key;
	}

	protected ActionForward requestMasterLogin(HttpServletRequest request){
		return new ActionForward("/admin/index.jsp");
	}
	protected String filterbr(String txt){
		return htmlFilter.filterbr(txt);
	}

	protected String decfilterbr(String txt){
		return htmlFilter.decfilterbr(txt);
	}

	protected void OutPutCommonJSON(HttpServletResponse response, String operator){
		StringBuffer json = new StringBuffer(200);
		json.append("var result = {");
		json.append("\"result\":\""+operator+"\",");
		json.append("\"name\":\"\",");
		json.append("\"value\":\"\"}");
		OutPubJson(response, json);
	}

	protected ActionForward requestLoginJSON(HttpServletResponse response){
		StringBuffer json = new StringBuffer();
		json.append("var result = {");
		json.append("\"result\":\"login\",");
		json.append("\"name\":\"\",");
		json.append("\"value\":0}");
		OutPubJson(response, json.toString());
		json = null;
		return null;
	}

	/**
	 * 返回mongodb的Service
	 * @return
	 */
	protected BaseMongodb getMongodbService(){
		return (BaseMongodb)findBean("MongodbService");
	}
	/**
	 * 返回 DataSource
	 * @return dataSource
	 */
	protected DataSource getDataSource(){
		return (DataSource)findBean("dataSource");
	}
	/**
	 * 返回 HibernateTemplate
	 * @return HibernateTemplate
	 */
	protected HibernateTemplate getHibernateTemplate(){
		return (HibernateTemplate)findBean("hibernateTemplate");
	}
	/**
	 * 返回jdbcTemplate
	 * @return jdbcTemplate
	 */
	protected JdbcTemplate getJdbcTemplate(){
		return (JdbcTemplate)findBean("jdbcTemplate");
	}

	/**
	 * 查找对象
	 * @param beanName
	 * @return
	 */
	protected Object findBean(String beanName){
		if(ctx==null)
			ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet.getServletContext());
		return ctx.getBean(beanName);
	}
	/**
	 *
	 * @param request
	 * @param errMsg
	 */
	protected void saveError(HttpServletRequest request,String errMsg){
		ActionMessages error = new ActionMessages();
		error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errMsg));
		saveErrors(request,error);
		error = null;
	}

	protected PrintWriter getOut(HttpServletResponse response){
		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=UTF-8");
			out = response.getWriter();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return out;
	}
	protected void OutPubJson(HttpServletResponse response,StringBuffer buf){
		OutPubJson(getOut(response),buf.toString());
	}
	protected void OutPubJson(HttpServletResponse response,String xml){
		OutPubJson(getOut(response),xml);
	}
	protected void OutPubJson(PrintWriter out,String json){
		out.println(json);
	}
	protected void OutPutResult(HttpServletResponse response,String script){
		OutPutResult(getOut(response),script);
	}
	protected void OutPutResult(PrintWriter out,String script){
		out.println("<script type=\"text/javascript\">"+script+"</script>");
	}
	protected String getResource(HttpServletRequest request ,String resource_key){
		return this.getResources(request).getMessage(resource_key);
	}
	protected void go(HttpServletResponse response,String url){
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected ActionForward send404(HttpServletResponse response){
		go(response,"/404.html");
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
	 *
	 * @author Strong Yuan
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(String tablename,
			String[] columns, String sql_where, String sql_orderby,
			String sql_groupby, int startrow, int maxRow) {
		return dao.QueryForList(new QueryObject(tablename, columns, sql_where,
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
	 *
	 * @author Strong Yuan
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(String tablename,
			String[] columns, String sql_where, String sql_orderby,
			String sql_groupby, int startrow, int maxRow,Connection con) {
		return dao.QueryForList(new QueryObject(tablename, columns, sql_where,
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
	 *
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
			al = dao.QueryForList(q,con);
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
	 *
	 * @author String Yuan
	 * @see QueryObject
	 *
	 */
	public ArrayList<HashMap<String, Object>> QueryForList(QueryObject q,Connection con) {
		return dao.QueryForList(q, con);
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
		return dao.QueryForHashMap(new QueryObject(tablename, columns, sql_where,
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
		return dao.QueryForHashMap(new QueryObject(tablename, columns, sql_where,
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
			hm = dao.QueryForHashMap(q,con);
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
		return dao.QueryForHashMap(q, con);
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
	 * 保存对象到Memcached中，可以指定有效期
	 * @param key 保存的key(md5加密)
	 * @param timeout 有效期（以秒计算）
	 * @param o 保存的对象
	 * @param servletcontext
	 */
	public void SetObject(String key,int timeout,Object o, ServletContext servletcontext){
		String servers = Constants.getMemcachedPools(servletcontext);
		if(servers!=null)
			Memcached.getInstance(servers).set(key, timeout, o);
	}
	/**
	 * 保存对象到Memcached中，可以指定有效期
	 * @param key 保存的key(md5加密)
	 * @param timeout 有效期（以秒计算）
	 * @param o 保存的对象
	 * @param servlet
	 */
	public void SetObject(String key,int timeout,Object o, Servlet servlet){
		String servers = Constants.getMemcachedPools(servlet);
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
	 * 读取Memcached中的对象。
	 * @param key 保存对象的key(md5加密)
	 * @param servletcontext
	 * @return
	 */
	public Object GetObject(String key, ServletContext servletcontext){
		String servers = Constants.getMemcachedPools(servletcontext);
		if(servers!=null)
			return Memcached.getInstance(servers).get(key);
		return null;
	}
	/**
	 * 读取Memcached中的对象。
	 * @param key 保存对象的key(md5加密)
	 * @param servletcontext
	 * @return
	 */	
	public Object GetObject(String key, Servlet servlet){
		String servers = Constants.getMemcachedPools(servlet);
		if(servers!=null)
			return Memcached.getInstance(servers).get(key);
		return null;
	}
	
	
	public static void main(String[] args){
		QueryObject q = new QueryObject();
		String columns[] = {"a"};
		q.setColumns(columns);
		StringBuffer sqlFields = new StringBuffer();
		for(int i=0;i<q.getColumns().length;i++){
			sqlFields.append(q.getColumns()[i]);
			if(q.getColumns().length>i+1)
				sqlFields.append(",");
		}
		System.out.println(sqlFields.toString());
	}
}
