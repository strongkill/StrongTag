package net.strong.taglib.db;


import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.sql.DataSource;

import net.strong.bean.Constants;
import net.strong.cookie.OpCookies;
import net.strong.dbcached.Memcached;
import net.strong.lang.Strings;
import net.strong.taglib.util.ItagProcess;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;

/**
 * Title:
 * Description:
 * Copyright: Copyright (c) 2003
 * Company: Strong Software International CO,.LTD
 *
 * @author unascribed
 * @version 1.0
 */

public class dbTag extends BodyTagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static ApplicationContext ctx = null;

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) pageContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) pageContext.getResponse();
	}

	protected DataSource getDataSource() {
		return (DataSource) findBean("dataSource");
	}

	protected HibernateTemplate getHibernateTemplate() {
		return (HibernateTemplate) findBean("hibernateTemplate");
	}

	protected JdbcTemplate getJdbcTemplate() {
		return (JdbcTemplate) findBean("jdbcTemplate");
	}

	protected Object findBean(String beanName) {
		if (ctx == null)
			ctx = WebApplicationContextUtils
			.getRequiredWebApplicationContext(pageContext
					.getServletContext());
		return ctx.getBean(beanName);
	}

	protected void OutPrintString(String str) {
		JspWriter out = pageContext.getOut();
		try {
			out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = null;
	}

	protected void go(String url) {
		try {
			getResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Log log = LogFactory.getLog(this.getClass().getName());

	protected String sqlTablename = null;

	protected String sqlFields = "*";

	protected String sqlWhere = null;

	protected String sqlOrderby = null;

	/**
	 * 针对某些统计进行分页查询
	 */
	protected String sqlGroupBy = null;

	protected String sql = null;

	protected String isSave = "false"; // 是否存入pageContext对象中

	protected String name = "rows";

	protected String bodySqlWhere = null;

	protected String isDebug = "false"; // 是否为调试状态，如为true，则记录调试信息（主要是数据库的查询语句）

	protected String isIgnore = "true"; // 是否忽略，当参数对应的值找不到时，是否忽略此参数的where子句

	protected String isSubSQL = "false"; // 含有子查询,含有子查询时，如参数找不到，则设为0。

	protected boolean is_value_ok = true; // body值，参数值是否符合要求

	protected int dbType = 1; // 数据库类型，0--MS SQL,1 -- MySQL , 2 -- postgreSQL ,3 -- oracle

	protected String bussinessClass=null; //注入自定义业务处理类。
	public String getBussinessClass() {
		return bussinessClass;
	}

	public void setBussinessClass(String bussinessClass) {
		this.bussinessClass = bussinessClass;
	}

	/**
	 * //此参数用于通过RequestUtil对象进行取值，即，设置此参数后，WHERE子句的参数值就可以从HashMap对象或JavaBean对象中取值
	 */
	protected String propName = null;

	/**
	 * 字段名，如此值不为空，则从数据列表中取出此字段对应的值存入curFieldList对象中,并将对象curFieldList存入pageContext对象中
	 */
	protected String rec_field_name = null;

	protected String scopeType = "4";

	protected int scope_type = 4;

	protected String db_sql_where = null;

	protected String new_flag = "false";

	public void release() {
		sqlTablename = null;
		sqlFields = "*";
		sqlWhere = null;
		sqlOrderby = null;
		sql = null;
		bodySqlWhere = null;
		isSave = "false";
		isDebug = "false";
		name = "rows";
		scopeType = "4";
		scope_type = 4;
		isIgnore = "true";
		isSubSQL = "false";
		rec_field_name = null;
		propName = null;
		log = null;
		super.release();
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropName() {
		return this.propName;
	}

	public void setRec_field_name(String rec_field_name) {
		this.rec_field_name = rec_field_name;
	}

	public String getRec_field_name() {
		return this.rec_field_name;
	}

	public void setIsSubSQL(String isSubSQL) {
		this.isSubSQL = isSubSQL;
	}

	public String getIsSubSQL() {
		return this.isSubSQL;
	}

	public void setIsIgnore(String isIgnore) {
		this.isIgnore = isIgnore;
	}

	public String getIsIgnore() {
		return this.isIgnore;
	}

	public void setIsDebug(String isDebug) {
		this.isDebug = isDebug;
	}

	public String getIsDebug() {
		return this.isDebug;
	}

	/**
	 * 获取数字字符串对应的数值，如不能转换，用默认值
	 *
	 * @param str_int_value
	 * @param default_value
	 *            默认值
	 * @return
	 */
	protected int getIntValue(String str_int_value, int default_value) {
		int result = 0;
		try {
			result = Integer.valueOf(str_int_value).intValue();
		} catch (Exception e) {
			result = default_value;
		}
		return result;
	}

	/**
	 * 判断是否为数字
	 *
	 * @param value
	 * @return
	 */
	protected boolean isValidNumber(String value) {
		if (value == null || value.length() < 1)
			return false;
		char[] tt = value.toCharArray();
		boolean result = true;
		for (int i = 0; i < tt.length; i++) {
			char t = tt[i];
			// boolean bb = t<'0';
			// boolean cc = t>'9';
			if ((t < '0' || t > '9') && t != '.') {
				result = false;
				break;
			}
		}
		tt = null;
		return result;
	}

	protected boolean isValidWhere(String value) {
		if (value == null || value.trim().length() < 1)
			return true;

		boolean result = true;
		String[] tt = value.split("and"); // 先对where子句按and进行分割
		for (int i = 0; i < tt.length; i++) {
			String t_str = tt[i];
			if (t_str != null && t_str.trim().length() > 0
					&& t_str.indexOf("=") > 0) {
				String[] t2 = t_str.split("=");
				if (t2.length < 2) {
					result = false;
					break;
				} else {
					for (int j = 0; j < t2.length; j++) {
						String t_t = t2[j];
						if (t_t == null || t_t.trim().length() < 1) {
							result = false;
							break;
						}
						t_t = null;
					}
				}
				t2 = null;
			}
			t_str = null;
		}
		tt = null;

		if (!result) {
			if (log.isDebugEnabled()) {
				log.debug("where is invalid,where:" + value);
			}
		}
		return result;
	}

	/**
	 * 过滤引号,将单引号改为双单引号
	 *
	 * @param value
	 * @return
	 */
	protected String filterQuotation(String value) {
		if (value != null && value.trim().length() > 0) {
			value = value.replaceAll("'", "''");
		}
		return value;
	}

	/**
	 * 获取过滤某些字符后的字符串
	 *
	 * @param value
	 * @param q_ok
	 *            是否需要过滤中括号（true－需要，false－不需要）
	 * @return
	 */
	protected String getFilterString(String value, boolean q_ok) {
		if (value != null) {
			value = value.trim();
			String value_tmp = value.toLowerCase();
			//value = value.toLowerCase();
			if (value_tmp.indexOf(" select ") > -1
					|| value_tmp.indexOf("(select ") > -1
					|| value_tmp.indexOf(" declare ") > -1
					|| value_tmp.indexOf("(declare ") > -1
					|| value_tmp.indexOf("(update ") > -1
					|| value_tmp.indexOf(" update ") > -1
					|| value_tmp.indexOf(" exists ") > -1
					|| value_tmp.indexOf("(exists ") > -1
					|| value_tmp.indexOf(" from ") > -1
					|| value_tmp.indexOf("(from ") > -1
					|| value_tmp.indexOf(" delete ") > -1
					|| value_tmp.indexOf("(delete ") > -1
					|| (q_ok && (value_tmp.indexOf("[") > -1 || value_tmp.indexOf("]") > -1)) // 将q_ok为false时，不过滤中括号
			) {
				HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
				String url = request.getRequestURL() + (request.getQueryString()==null?"":"?"+request.getQueryString());
				log.warn("value is error:" + value +",URL:"+url);
				request = null;
				url = null;
				value = "errorwhere";
			}
			if (value_tmp != null
					&& (value_tmp.indexOf(" user ") > -1
							|| value_tmp.indexOf("(user ") > -1
							|| value_tmp.indexOf("user)") > -1 || value_tmp
							.indexOf("user+") > -1)) {
				HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
				String url = request.getRequestURL() + (request.getQueryString()==null?"":"?"+request.getQueryString());
				log.warn("can not use user for select,value:" + value+",URL:" + url);
				request = null;
				url = null;
				value = "errorwhere";
			}
		}

		filterQuotation(value); // 将单引号改为双个单引号

		return value;

	}

	/**
	 * 获取过滤某些字符后的字符串
	 *
	 * @param value
	 * @return
	 */
	protected String getFilterString(String value) {
		return getFilterString(value, true);
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
		if (scopeType != null) {
			scope_type = Integer.valueOf(scopeType).intValue();
			if (scope_type > 4)
				scope_type = 4;
			if (scope_type < 1)
				scope_type = 1;
		}
	}

	public String getScopeType() {
		return this.scopeType;
	}

	public void setSqlOrderby(String sqlOrderby) {
		this.sqlOrderby = sqlOrderby;
	}

	public String getSqlOrderby() {
		return this.sqlOrderby;
	}

	public void setSqlTablename(String sqlTablename) {
		this.sqlTablename = sqlTablename;
	}

	public String getSqlTablename() {
		return sqlTablename;
	}

	public void setSqlFields(String sqlFields) {
		this.sqlFields = sqlFields;
	}

	public String getSqlFields() {
		return sqlFields;
	}

	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	public String getSqlWhere() {
		return sqlWhere;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getNew_flag() {
		return new_flag;
	}

	public void setNew_flag(String new_flag) {
		this.new_flag = new_flag;
	}
	/**
	 * 保存对象到Memcached中，有效期是系统默认。
	 * @param key 保存的key(md5加密)
	 * @param o 保存的对象
	 */
	public void SetObject(String key,Object o){
		SetObject(key, Constants.getMemcachedTimeOutSecond(pageContext), o);
	}
	/**
	 *  保存对象到Memcached中，可以指定有效期
	 * @param key 保存的key(md5加密)
	 * @param timeout 有效期（以秒计算）
	 * @param o 保存的对象
	 */
	public void SetObject(String key,int timeout,Object o){
		String servers = Constants.getMemcachedPools(pageContext);
		if(servers!=null)
			Memcached.getInstance(servers).set(key, timeout, o);
	}
	/**
	 * 读取Memcached中的对象。
	 * @param key 保存对象的key(md5加密)
	 * @return
	 */
	public Object GetObject(String key){
		String servers = Constants.getMemcachedPools(pageContext);
		if(servers!=null)
			return Memcached.getInstance(servers).get(key);
		return null;
	}

	public Connection getConnection(){
		Connection con = null;
		try {
			con =DriverManager.getConnection(Constants.getProxool_ReadOnly_Pools(pageContext));
		} catch (SQLException e) {
			try {
				con =DriverManager.getConnection(Constants.getConfig(pageContext).getProxool_alias_name());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Can't get Connection from the readonly Connection pool,Use the main Connection pool.");
		}
		return con;
	}
	/**
	 * 解决在Oracle数据库中存在*的问题.
	 * 2009-06-25解決存在多個括弧的問題.
	 * @param perfix
	 * @param name
	 * @param sqlFields
	 * @return
	 */
	public String FixSqlFieldsStarChar(String perfix,String name,String sqlFields){
		return Strings.FixSqlFieldsStarChar(perfix, name, sqlFields);
	}

	public String getSqlGroupBy() {
		return sqlGroupBy;
	}

	public void setSqlGroupBy(String sqlGroupBy) {
		this.sqlGroupBy = sqlGroupBy;
	}
	/**
	 * 分别从request,pagecontext,session,comName中取出对象
	 * @param str2
	 * @return
	 */
	protected String getParamValue(String str2) {
		String value = null;

		//优先从地址栏取参数
		value = pageContext.getRequest().getParameter(str2.trim());
		//然后从request容器里取参数
		if (value == null || value.equalsIgnoreCase("null"))
			value = (String) pageContext.getRequest().getAttribute(str2.trim());
		//再到pageContext中取参数
		if (value == null || value.equalsIgnoreCase("null"))
			value = (String) pageContext.getAttribute(str2.trim());

		//从session获取对象
		if (value == null || value.equalsIgnoreCase("null"))
			value = (String) pageContext.getSession().getAttribute(str2.trim());

		if (value == null || value.equalsIgnoreCase("null") || value.trim().length() <= 0) {
			if (propName != null && propName.length() > 1) {
				try {
					Object obj_value = TagUtils.getInstance().lookup(
							pageContext, propName, str2.trim(), null);
					if (obj_value != null)
						value = String.valueOf(obj_value);
				} catch (Exception e) {
					value = null;
				}
			}
		}

		return value;
	}
	/**
	 *
	 * @return
	 */
	protected String createOrderSQL() {
		String temp_orderby = sqlOrderby;

		if (sqlOrderby != null && sqlOrderby.length() > 0
				&& sqlOrderby.indexOf("::") >= 0) {
			// 如果存在参数（即含有::），则表示只有一个参数（参数一般通过下拉选择列表来获取
			// ，多个参数不允许出现。（由于一个参数已能处理大部分的情况）。
			// 缺省排序的处理：中括号[]中的数据为缺省排序内容
			// sqlOrderby参数的设置示例：sqlOrderby="::order_by[logtime desc]" 其中logtime
			// desc为缺省排序

			int t_index = -1;
			t_index = temp_orderby.indexOf("::");
			String temp = temp_orderby.substring(t_index + 2);
			temp = temp.trim(); // 参数
			int t2_index = temp.indexOf("["); // 左中括号出现的位置
			int t3_index = temp.indexOf("]"); // 右中括号出现的位置
			String default_order = null;
			if (t2_index > 0) {
				// 含有缺省排序
				default_order = temp.substring(t2_index + 1, t3_index);
				temp = temp.substring(0, t2_index); // 去掉缺省后的排序参数
			}
			String value = null;
			if (temp != null) {
				// 获取参数值
				value = pageContext.getRequest().getParameter(temp.trim());
				if (value == null || value.equalsIgnoreCase("null"))
					value = (String) pageContext.getAttribute(temp.trim());
			}
			if (value != null) {
				temp_orderby = value;
			} else if (default_order != null) {
				temp_orderby = default_order;
			} else
				temp_orderby = "";
		}
		// System.out.println(temp_orderby);
		return temp_orderby;

	}
	//======================================================================================================================

	protected Object getValue(String object, String property)
	throws JspException {
		return  invokObjectValue(lookup(pageContext, object, null), property);
	}

	protected Object invokObjectValue(Object bean, String property)
	throws JspException {
		if (bean == null)
			return null;
		Object value = null;
		if (property != null) {
			if (bean instanceof ResultSet) {
				value = getResultValue(bean, property);
			} else if (bean instanceof Map<?, ?>) {
				Map<?, ?> hm = (Map<?, ?>) bean;
				if ("empty".equalsIgnoreCase(property)) {
					value = hm.isEmpty();
				} else if ("size".equalsIgnoreCase(property)) {
					value = hm.size();
				} else {
					value = hm.get(property);
				}
				hm = null;
				/*
				 * } else if (bean instanceof WeakHashMap<?,?>) {
				 * WeakHashMap<?,?> hm = (WeakHashMap<?,?>) bean; value =
				 * hm.get(property); hm = null;
				 */
			} else if (bean instanceof SimpleHash) {
				SimpleHash hm = (SimpleHash) bean;
				try {
					value = hm.get(property);
				} catch (TemplateModelException e) {
					e.printStackTrace();
				}
				hm = null;
			} else if (bean instanceof HttpServletRequest) {
				value = ((HttpServletRequest) bean).getParameter(property);
				if(value==null)
					value = ((HttpServletRequest) bean).getAttribute(property);
			} else if (bean instanceof HttpSession) {
				value = ((HttpSession) bean).getAttribute(property);
			} else if (bean instanceof OpCookies) {
				value = OpCookies.getCookies(property, getRequest());
			} else if (bean instanceof List<?>) {
				Class<?> c = bean.getClass();
				try {
					Method m = c.getDeclaredMethod(property);
					value = m.invoke(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					value = PropertyUtils.getProperty(bean, property);
				} catch (Exception t) {
					throw new JspException("error at TagIf :" + t.getMessage());
				}

			}

		}
		return value;
	}

	protected Object getResultValue(Object bean, String property)
	throws JspException {
		Object value = null;
		try {
			ResultSet rs = (ResultSet) bean;
			value = rs.getObject(property);
			rs.close();
			rs = null;
		} catch (SQLException e) {
			throw new JspException("error at TagIf SQLException: "
					+ e.getMessage());
		} catch (Exception e) {
			throw new JspException("error at TagIf Exception:"
					+ e.getMessage());
		}
		bean = null;
		return value;
	}

	protected Class<?> getClass(String className) throws ClassNotFoundException{
		return Class.forName(bussinessClass);
	}
	protected ItagProcess getProcess(String ClassName) throws Exception{
		return (ItagProcess)getClass(bussinessClass).newInstance();
	}
	protected ArrayList<HashMap<String,Object>> BussinessProcess(String bussinessClass,ArrayList<HashMap<String,Object>> al,long[] pageinfo){
		ArrayList<HashMap<String,Object>> retal = null;
		try{
			ItagProcess itag = getProcess(bussinessClass);
			itag.init(pageinfo[0], pageinfo[1], pageinfo[2]);
			retal = itag.process(pageContext, al);
		}catch(Exception e){
			e.printStackTrace();
		}
		return retal;
	}
	protected HashMap<String,Object> BussinessProcess(String bussinessClass,HashMap<String,Object> hm){
		HashMap<String,Object> rethm = null;
		try{
			ItagProcess itag = getProcess(bussinessClass);
			rethm = itag.process(pageContext, rethm);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rethm;
	}

	protected Object BussinessProcess(String bussinessClass,Object o){
		Object retobject = null;
		try{
			ItagProcess itag = getProcess(bussinessClass);
			retobject = itag.process(pageContext,o);		
		}catch(Exception e){
			e.printStackTrace();
		}

		return retobject;
	}

	protected Object lookup(PageContext pageContext, String name, String scope)
	throws JspTagException {
		Object bean = null;
		if ("cookie".equalsIgnoreCase(name)) {
			bean = new OpCookies();
		} else if ("request".equalsIgnoreCase(name)) {
			bean = getRequest();
			/*
			 * }else if("response".equalsIgnoreCase(name)){ bean =
			 * getResponse();
			 */
		} else if ("session".equalsIgnoreCase(name)) {
			bean = getRequest().getSession();
		} else {
			if (scope == null) {
				bean = pageContext.findAttribute(name);
				if (bean == null)
					bean = pageContext.getAttribute(name,
							PageContext.REQUEST_SCOPE);
				if (bean == null)
					bean = pageContext.getAttribute(name,
							PageContext.SESSION_SCOPE);
				if (bean == null)
					bean = pageContext.getAttribute(name,
							PageContext.APPLICATION_SCOPE);
			} else if ("page".equalsIgnoreCase(scope))
				bean = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
			else if ("request".equalsIgnoreCase(scope))
				bean = pageContext
				.getAttribute(name, PageContext.REQUEST_SCOPE);
			else if ("session".equalsIgnoreCase(scope))
				bean = pageContext
				.getAttribute(name, PageContext.SESSION_SCOPE);
			else if ("application".equalsIgnoreCase(scope))
				bean = pageContext.getAttribute(name,
						PageContext.APPLICATION_SCOPE);
			else {
				JspTagException e = new JspTagException("Invalid scope "
						+ scope);
				throw e;
			}
		}
		name = null;
		scope = null;
		return (bean);

	}
	public static void main(String[] args){
		dbTag dbt = new dbTag();
		System.out.println(System.currentTimeMillis());
		String bodySqlWhere = dbt.getFilterString("b.is_reply=2 and b.parent_id=0 and b.blog_id=1213 and b.checked=1 and b.blog_reply_type_id =0 and b.relate_id=1231 and b.member_id = m.member_id ", false);
		System.out.println(bodySqlWhere+""+System.currentTimeMillis());
	}
}
