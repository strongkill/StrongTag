package net.strong.taglib.db;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import net.strong.bean.Constants;
import net.strong.database.dbSpecifyValueBean;

import org.apache.struts.taglib.TagUtils;

/**
 *
 *
 * <p>
 * Title: dbSpecifyValueTag
 * </p>
 * <p>
 * Description:根据给定的参数，从数据库中取出所要的值，将其输出或存入pageContext对象中
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Strong Software International CO,.LTD qt
 * </p>
 *
 * @since 2007-6-15
 * @author Strong Yuan
 * @version 1.0
 */
public class dbSpecifyValueTag extends dbTag {

	/**
	 *
	 */
	private int version = 2;
	
	private static final long serialVersionUID = 2886449389943628644L;
	protected String isCount = "false";
	protected String isSum = "false";

	protected String iscached = "true";

	protected String defaultValue = null;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getIscached() {
		return iscached;
	}

	public void setIscached(String iscached) {
		this.iscached = iscached;
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
	/**
	 *
	 * @return
	 */
	protected String createMaxSQL() {
		String temp_sql = null;
		String sql_where = createWhereSQL();
		temp_sql = "select count(*) from " + sqlTablename;
		if (sql_where != null) {
			temp_sql += " where " + sql_where;
		}
		return temp_sql;
	}

	/**
	 *
	 * @return
	 * @deprecated
	 */
	protected String createWhereSQL() {

		String result = null;
		String temp_sql = null;
		int t_index = -1;

		if (bodySqlWhere != null && bodySqlWhere.length() > 0)
			temp_sql = bodySqlWhere;
		else if (sqlWhere != null && sqlWhere.length() > 0)
			temp_sql = sqlWhere;
		result = temp_sql;

		// ProDebug.addDebugLog("result at dbSpecifyValueTag:"+result);
		// ProDebug.saveToFile();

		// 例：new_id = id_new and bas_id = :id_bas
		ArrayList<String> paramPre = new ArrayList<String>(); // 用于记录where等号前的字符
		// bas_id
		ArrayList<String> paramNex = new ArrayList<String>(); // 用于记录where等号后的字符
		// id_bas
		ArrayList<String> paramTotal = new ArrayList<String>(); // 用于记录where完整子句
		// new_id =
		// id_new
		String t_sql = temp_sql;

		boolean is_int_value = false; // 判断此参数是否为int型

		StringBuffer resultBuf = new StringBuffer();
		if (t_sql != null) {
			t_index = t_sql.indexOf("::");
		}
		if (t_index > 0) // where子句中包含参数，以下进行处理
		{

			while (t_index > 0) {
				String sub_str1 = null;
				t_index = t_sql.indexOf("::");
				if (t_index > 0)
					sub_str1 = t_sql.substring(0, t_index);
				int r_index = -1;
				if (sub_str1 != null)
					r_index = sub_str1.indexOf("and ");
				if (r_index > 0) {
					paramTotal.add(sub_str1.substring(0, r_index));
					sub_str1 = sub_str1.substring(r_index + 3);
				}
				if (sub_str1 != null)
					paramPre.add(sub_str1);

				if (t_index > 0) {
					String sub_str2 = t_sql.substring(t_index + 2);
					int tt_index = sub_str2.indexOf("and ");

					if (tt_index > 0) {
						String sub_str3 = sub_str2.substring(0, tt_index);
						paramNex.add(sub_str3);
						t_sql = sub_str2.substring(tt_index + 3);

						t_index = 1;
					} else {
						paramNex.add(sub_str2);
						t_index = -1;
					}
				} else {
					if (t_sql != null)
						paramTotal.add(t_sql);
				}
				sub_str1 = null;
			}

			int pp_size = paramPre.size();
			for (int i = 0; i < pp_size; i++) {
				String str1 = paramPre.get(i).trim();
				String str2 = paramNex.get(i).trim();
				String value = null;
				String b_str = null; // 开始的字符，用于判断是否以%开头或结尾
				String e_str = null; // 结束的字符
				if (str2 != null) {
					if (str2.length() > 1) {
						b_str = str2.substring(0, 1);
						e_str = str2
						.substring(str2.length() - 1, str2.length());
					}
					if ("%".equals(b_str) && str2.length() >= 1)
						str2 = str2.substring(1, str2.length());
					if ("%".equals(e_str) && str2.length() >= 1)
						str2 = str2.substring(0, str2.length() - 1);

					str2 = str2.trim();

					if (str2.length() > 0 && str2.charAt(0) == ':') {//if (str2.startsWith(":")) {
						is_int_value = true;
						str2 = str2.substring(1);
					} else {
						is_int_value = false;
					}

					value = pageContext.getRequest().getParameter(str2);
					// 进行字符转换
					// value =
					// net.strong.util.MultiLanguage.getUnicode(value,(HttpServletRequest)pageContext.getRequest());
					if (value == null || value.equalsIgnoreCase("null"))
						value = (String) pageContext.getAttribute(str2);
					// 从session中取值
					if (value == null || value.equalsIgnoreCase("null")) {
						HttpSession session = pageContext.getSession();
						if (session != null)
							value = (String) session.getAttribute(str2);
						// value = (String)
						// pageContext.getSession().getAttribute(str2.trim());
					}

					if (log.isDebugEnabled())
						log.debug("start just propName");

					if (value == null || value.equalsIgnoreCase("null")
							|| value.trim().length() <= 0) {
						// 当参数propName不为空时，通过RequestUtils进行取值
						if (propName != null && propName.length() > 1) {
							try {
								value = (String) TagUtils.getInstance().lookup(
										pageContext, propName, str2, null);
							} catch (Exception e) {
								value = null;
								log.debug(e.getMessage());
							}
						}
					}

				}
				/*
				 * if(log.isDebugEnabled()) log.debug("value:"+value);
				 */
				if (value == null || value.equalsIgnoreCase("null")
						|| value.trim().length() <= 0) {
					if (log.isDebugEnabled())
						log.debug("value is null," + value);

					if (isIgnore.equalsIgnoreCase("true")) {
						is_int_value = false;
						continue;
					} else {
						String t_result = str1 + " 0";
						paramTotal.add(t_result);
					}
				} else {
					if (log.isDebugEnabled())
						log.debug("value is ok," + value);

					/**
					 * 参数值中不允许含有and , update , exists ,select,from
					 */
					pageContext.setAttribute(str2.trim(), value.trim());
					// save to pageContext for input text can get value from it
					String t_result = null;
					value = value.trim();

					if ("%".equals(b_str)) // 加上%,主要用于like查询
						value = "%" + value;
					if ("%".equals(e_str))
						value = value + "%";
					int e_len = 0;
					if ((e_len = str1.indexOf("==")) > 0) // 当参数用"=="表示时，说明是进行为数字的比较，不用加双引号
					{
						boolean is_num = net.strong.util.MyUtil.isNumber(value);
						if (is_num) {
							t_result = " " + str1.substring(0, e_len + 1)
							+ value;
						} else {
							is_value_ok = false;
							HttpServletRequest t_re = (HttpServletRequest) pageContext
							.getRequest();
							String strServerName = t_re.getServerName();
							String strQueryString = t_re.getQueryString();
							String requestUrl = t_re.getRequestURI();
							log.warn(strServerName + " , " + strQueryString
									+ " , " + requestUrl + " , " + value);
						}
					} else if (is_int_value) {
						t_result = " " + str1 + value;
					} else
						t_result = " " + str1 + " '" + value + "' ";
					paramTotal.add(t_result);
				}
				is_int_value = false;
			}

			int pt_size = paramTotal.size();
			for (int j = 0; j < pt_size; j++) {
				resultBuf.append(paramTotal.get(j));
				// resultBuf.append(" ");
				if (j < paramTotal.size() - 1)
					resultBuf.append(" and ");
			}
		} else {
			if (result != null)
				resultBuf.append(result);
		}
		if (resultBuf != null)
			result = resultBuf.toString();

		if (paramPre != null) {
			paramPre.clear();
			paramPre = null;
		}
		if (paramNex != null) {
			paramNex.clear();
			paramNex = null;
		}
		if (paramTotal != null) {
			paramTotal.clear();
			paramTotal = null;
		}

		resultBuf = null;

		// 判断是否正确
		result = getFilterString(result);
		if ("errorwhere".equals(result)) {
			is_value_ok = false;
			return null;
		}

		// 判断等号后是否有值
		if (!isValidWhere(result)) {
			is_value_ok = false;
			return null;
		}
		return result;
	}

	/**
	 *
	 * @return
	 */
	protected String createSql() {
		String temp_sql = null;
		if (sql != null){
			temp_sql = sql;
		}else if (sqlTablename != null) {
			int dbType = Constants.getDataType(pageContext);
			String sql_where = createWhereSQL(false);// createWhereSQL();
			String this_order_by = createOrderSQL();
			if (isCount != null && isCount.compareToIgnoreCase("true") == 0) {
				StringBuffer tmp_buf = new StringBuffer();
				if (dbType == 0) {// mssql

				} else if (dbType == 1) {// mysql
					tmp_buf.append("call prc_getTableCount('");
					tmp_buf.append(sqlTablename);
					tmp_buf.append("','");
					if (sql_where != null && sql_where.length() > 0)
						tmp_buf.append(sql_where);
					tmp_buf.append("');");
				} else if (dbType == 2) {// postgreSQL

				} else if (dbType == 3) { // oracle
					tmp_buf.append("select count(*) as count from ");
					tmp_buf.append(sqlTablename);
					if (sql_where != null && sql_where.length() > 0) {
						tmp_buf.append(" where ");
						tmp_buf.append(sql_where);
					}
				}
				temp_sql = tmp_buf.toString();
				tmp_buf = null;
			} else if (sqlFields != null) {
				if (isSum != null && isSum.compareToIgnoreCase("true") == 0) {
					StringBuffer tmp_buf = new StringBuffer();
					if (dbType == 0) {// mssql

					} else if (dbType == 1) {
						tmp_buf.append("call prc_getTableFieldSum('");
						tmp_buf.append(sqlTablename);
						tmp_buf.append("','");
						tmp_buf.append(sqlFields);
						tmp_buf.append("','");
						if (sql_where != null && sql_where.length() > 0)
							tmp_buf.append(sql_where);
						tmp_buf.append("');");
					} else if (dbType == 2) {// postgreSQL

					} else if (dbType == 3) { // oracle
						tmp_buf.append("select sum(");
						tmp_buf.append(sqlFields);
						tmp_buf.append(") as sum from ");
						tmp_buf.append(sqlTablename);
						if (sql_where != null && sql_where.length() > 0) {
							tmp_buf.append(" where ");
							tmp_buf.append(sql_where);
						}
					}
					temp_sql = tmp_buf.toString();
					tmp_buf = null;
				} else {
					StringBuffer tmp_buf = new StringBuffer();
					if (dbType == 0) { // ms sql 2000
						tmp_buf.append("select top 1 ").append(sqlFields).append(" from ").append(sqlTablename);

						if (sql_where != null && sql_where.length() > 0)
							tmp_buf.append(" where ").append(sql_where);

						if(this_order_by!=null && this_order_by.length()>0)
							tmp_buf.append(" order by ").append(this_order_by);

					} else if (dbType == 1) { // mysql 暂不支持order by
						tmp_buf.append("call prc_executeQuery(\"");
						tmp_buf.append(sqlTablename);
						tmp_buf.append("\",\"");
						tmp_buf.append(sqlFields);
						tmp_buf.append("\",\"");
						if (sql_where != null && sql_where.length() > 0)
							tmp_buf.append(sql_where);
						tmp_buf.append("\");");
					} else if (dbType == 2) { // postgreSQL
						tmp_buf.append("select ").append(sqlFields).append(" from ").append(sqlTablename);

						if (sql_where != null && sql_where.length() > 0)
							tmp_buf.append(" where ").append(sql_where);

						if(this_order_by!=null && this_order_by.length()>0)
							tmp_buf.append(" order by ").append(this_order_by);

						tmp_buf.append(" limit 1");
					} else if (dbType == 3) { // oracle
						/*
						tmp_buf.append("select ").append(sqlFields).append(" from ").append(sqlTablename);

						if (sql_where != null && sql_where.length() > 0){
							tmp_buf.append(" where ").append(sql_where).append(" and rownum <=1");
						}else{
							tmp_buf.append(" where rownum <=1");
						}
						*/
						tmp_buf.append("select ").append(
								FixSqlFieldsStarChar(null, "res.", sqlFields))
								.append(" from (");

						tmp_buf.append("select ").append(sqlFields).append(
								" from " + sqlTablename);
						if (sql_where != null) {
							tmp_buf.append(" where ").append(sql_where);
						}
						if (this_order_by != null)
							tmp_buf.append(" order by ").append(this_order_by);
						tmp_buf.append(") res where rownum <=1");
					}
					temp_sql = tmp_buf.toString();
					tmp_buf = null;
				}
			}
		}
		return temp_sql;
	}

	public int doStartTag() throws JspException {
		return (EVAL_BODY_BUFFERED);
	}

	public int doAfterBody() throws JspException {

		if (bodyContent != null) {
			bodySqlWhere = bodyContent.getString();
			if (bodySqlWhere != null) {
				bodySqlWhere = bodySqlWhere.trim();
			}
			if (bodySqlWhere.length() < 1) {
				bodySqlWhere = null;
			}
		}
		// ProDebug.addDebugLog("bodySqlWhere at
		// dbSpecifyValueTag:"+bodySqlWhere);
		// ProDebug.saveToFile();
		if (log.isDebugEnabled())
			log.debug("bodySqlWhere init:" + bodySqlWhere);

		bodySqlWhere = getFilterString(bodySqlWhere);

		if ("errorwhere".equals(bodySqlWhere))
			is_value_ok = false;

		return (SKIP_BODY);

	}

	public int doEndTag() throws JspException {
		if (!is_value_ok) // 条件不符合要求，直接返回
			return EVAL_PAGE;

		String temp_sql = createSql();
		// System.out.println("need time createSql: "+
		// (System.currentTimeMillis() - posstime));
		if (!is_value_ok) // 条件不符合要求，直接返回
			return EVAL_PAGE;

		if (log.isDebugEnabled())
			log.debug("temp_sql:" + temp_sql);

		if (temp_sql == null)
			throw new JspException("gived sql statement error");

		HashMap<String, Object> result = null;

		try {
			if ("true".equalsIgnoreCase(iscached)) {
				try {
					Object tmp = GetObject(temp_sql);// DBcached.getInstance().getServerCache().get(temp_sql);
					if (tmp != null) {
						result = (HashMap<String, Object>) tmp;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (result == null) {
				dbSpecifyValueBean dbsv = new dbSpecifyValueBean();
				dbsv.setSql(temp_sql);
				result = dbsv
				.getdbValue(getConnection()/* DriverManager.getConnection(Constants.getProxool_ReadOnly_Pools(pageContext)) */);
				dbsv = null;
				if ("true".equalsIgnoreCase(iscached))
					SetObject(temp_sql, result);// DBcached.getInstance().getServerCache().put(temp_sql,
				// result);
			}
			if (result == null)
				result = new HashMap<String, Object>();

		} catch (Exception e) {
			HttpServletRequest request = (HttpServletRequest) pageContext
			.getRequest();
			String strServerName = request.getServerName();
			String strRequestURI = request.getRequestURI();
			String strQueryString = request.getQueryString();
			String remoteAddr = request.getRemoteAddr();
			log.error(remoteAddr + " , " + strServerName + " , "
					+ strRequestURI + " , " + strQueryString + " , "
					+ e.getMessage());
			e.printStackTrace();
			return EVAL_PAGE;
		}
		if(bussinessClass!=null && bussinessClass.length()>0){
			HashMap<String, Object> tmphm = BussinessProcess(bussinessClass, result);
			if(tmphm!=null && tmphm.size()>0)
				result = tmphm;
		}
		try {
			if ("true".equalsIgnoreCase(isSave)) {
				if (name != null && "true".equalsIgnoreCase(isSave)) {
					pageContext.setAttribute(name, result);

					// HttpServletRequest request = (HttpServletRequest)
					// pageContext.getRequest();

				} else {
					result.clear();
					result = null;
				}
				result = null;
			} else if (result != null) {
				if (isSum.compareToIgnoreCase("true") == 0) {
					String str_sum = String.valueOf(result.get("sum"));
					if (defaultValue != null) {
						if (str_sum == null || "null".equalsIgnoreCase(str_sum)
								|| str_sum.length() == 0) {
							str_sum = defaultValue;
						}
					}
					TagUtils.getInstance().write(pageContext, str_sum);
				} else if (isCount.compareToIgnoreCase("true") == 0) {
					String str_count = String.valueOf(result.get("count"));
					if (defaultValue != null) {
						if (str_count == null
								|| "null".equalsIgnoreCase(str_count)
								|| str_count.length() == 0) {
							str_count = defaultValue;
						}
					}
					TagUtils.getInstance().write(pageContext, str_count);
				} else if (sqlFields != null && sqlFields.indexOf(",") == -1
						&& sqlFields.compareTo("*") != 0) {
					String str_value = String.valueOf(result.get(sqlFields));
					if (defaultValue != null) {
						if (str_value == null
								|| "null".equalsIgnoreCase(str_value)
								|| str_value.length() == 0) {
							str_value = defaultValue;
						}
					}
					TagUtils.getInstance().write(pageContext, str_value);
				}
				result.clear();
				result = null;
			}
		} catch (Exception e) {

			log.error(e.getMessage());
			HttpServletRequest request = (HttpServletRequest) pageContext
			.getRequest();
			String strServerName = request.getServerName();
			String strRequestURI = request.getRequestURI();
			String strQueryString = request.getQueryString();
			String context = request.getContextPath();
			// String referer = request.getHeader("referer");
			String remoteAddr = request.getRemoteAddr();
			log.error(remoteAddr + " , " + strServerName + " , "
					+ strRequestURI + " , " + strQueryString + " , " + context);

			return EVAL_PAGE;
			// net.strong.util.CloseCon.Close(con);
			// throw new JspException("IO Error: " + e.getMessage());
		}
		return EVAL_PAGE;
	}
	/**
	 *
	 * @param bdb_sql_where
	 * @return
	 */
	protected String createWhereSQL(boolean bdb_sql_where) {

		//		System.out.println("start createWhereSql at dbListArrayTag : "+ System.currentTimeMillis());
		String result = null;
		String temp_sql = null;
		int t_index = -1;

		boolean three_flag = false; // 是否为三冒号（:::）标志，如是，则直接替换所取到的值

		if (bodySqlWhere != null && bodySqlWhere.length() > 0) {
			temp_sql = bodySqlWhere;
		} else if (sqlWhere != null && sqlWhere.length() > 0) {
			temp_sql = sqlWhere;
		}
		if (bdb_sql_where && db_sql_where != null) {
			if (temp_sql != null)
				temp_sql += db_sql_where;
			else
				temp_sql = db_sql_where;
		}
		result = temp_sql;

		// System.out.println("result at createNewWhereSQL() : " + result);

		// bodySqlWhere = null;
		// sqlWhere = null;
		// 例：new_id = id_new and bas_id = :id_bas

		// System.out.println("sql where : " + result);
		ArrayList<String> paramPre = new ArrayList<String>(); // 用于记录where等号前的字符
		// bas_id
		ArrayList<String> paramNex = new ArrayList<String>(); // 用于记录where等号后的字符
		// id_bas
		ArrayList<String> paramTotal = new ArrayList<String>(); // 用于记录where完整子句
		// new_id =
		// id_new
		StringBuffer resultBuf = new StringBuffer(); // 用于存放where子句的临时字符串

		String t_sql = temp_sql;

		if (t_sql != null) {
			t_index = t_sql.indexOf("::");
		}
		if (t_index > 0) // where子句中包含参数，以下进行处理
		{
			//	if (log.isDebugEnabled())
			//		log.debug("t_index : " + t_index);

			while (t_index > 0) {
				t_index = t_sql.indexOf("::");
				String sub_str1 = null;
				if (t_index > 0)
					sub_str1 = t_sql.substring(0, t_index);
				int r_index = -1;
				if (sub_str1 != null)
					r_index = sub_str1.lastIndexOf("and ");// .indexOf("and");//sub_str1.indexOf("and");
				if (r_index > 0) {
					paramTotal.add(sub_str1.substring(0, r_index));
					sub_str1 = sub_str1.substring(r_index + 3);
				}
				paramPre.add(sub_str1);

				String sub_str2 = t_sql.substring(t_index + 2);
				int tt_index = sub_str2.indexOf("and ");// sub_str2.indexOf("and");

				if (tt_index > 0) {
					String sub_str3 = sub_str2.substring(0, tt_index);
					paramNex.add(sub_str3);
					t_sql = sub_str2.substring(tt_index + 3);

					t_index = 1;
				} else {
					paramNex.add(sub_str2);
					t_index = -1;
				}

			}

			int pp_size = paramPre.size();
			for (int i = 0; i < pp_size; i++) {
				String str1 = null;
				String str2 = null;

				str1 =  paramPre.get(i);
				if (str1 != null)
					str1 = str1.trim();

				if (paramNex.size() > i)
					str2 =  paramNex.get(i);
				if (str2 != null)
					str2 = str2.trim();

				String value = null;
				String b_str = null; // 开始的字符，用于判断是否以%开头或结尾
				String e_str = null; // 结束的字符

				if (str1 == null && str2 != null) {
					// str1为空，表示没有参数，不需要取数据，str2即为部分where子句
					paramTotal.add(str2);
					continue;
				}
				if (str2 != null) {
					if (str2.length() > 1) {
						b_str = str2.substring(0, 1); // 取第一个字符
						e_str = str2
						.substring(str2.length() - 1, str2.length()); // 取出最后一个字符
					}
					if ("%".equals(b_str) && str2.length() >= 1)
						str2 = str2.substring(1, str2.length());
					if ("%".equals(e_str) && str2.length() >= 1)
						str2 = str2.substring(0, str2.length() - 1);

					/*
					 * if("keyword".equals(str2.trim())) { count_hotkey = true;
					 * //查询条件中含有keyword,需要进行统计热门关键字 }
					 */
					if (str2.indexOf(":") == 0) {
						str2 = str2.substring(1);
						three_flag = true;
					}

					value = getParamValue(str2);

				}

				value = getFilterString(value); // 过滤一些不安全的语句
				if ("errorwhere".equals(value)) {
					is_value_ok = false;
					return null;
				}

				if (value == null || value.equalsIgnoreCase("null")
						|| value.trim().length() <= 0) {
					//	if (log.isDebugEnabled())
					//		log.debug("value is null," + value);

					if ("true".equals(isSubSQL)) {
						temp_sql = temp_sql.replaceFirst(str2, "0");
					} else {
						if (isIgnore.equalsIgnoreCase("true"))
							continue;
						else {
							String t_result = str1 + " 0";
							paramTotal.add(t_result);
						}
					}
				} else {

					/**
					 * 参数值中不允许含有and , update , exists ,select,from
					 */
					value = getFilterString(value); // 过滤一些不安全的语句
					if ("errorwhere".equals(value)) {
						is_value_ok = false;
						return null;
					}

					// save to pageContext for input text can get value from it
					value = value.trim();
					if (value.endsWith("`"))
						value = value.substring(0, value.length() - 1);
					if (value.endsWith("/"))
						value = value.substring(0, value.length() - 1);

					// pageContext.setAttribute(str2.trim(),value.trim());

					if ("true".equals(isSubSQL)) {
						temp_sql = temp_sql.replaceFirst(str2, value.trim());
					} else {
						String t_result = null;
						value = value.trim();
						value = value.replaceAll("'", "''"); // 将'换成''，为了可以对'进行查询

						if ("%".equals(b_str)) // 加上%,主要用于like查询
							value = "%" + value;
						if ("%".equals(e_str))
							value = value + "%";
						int str1_len = 0;
						if (three_flag) { // 参数含有三个冒号，直接替换参数
							t_result = str1 + " " + value;
						} else {

							if ((str1_len = str1.indexOf("==")) > 0) {
								// 当参数用"=="表示时，说明是进行为数字的比较，不用加双引号,并且不能含有字母
								if (isValidNumber(value)) {
									t_result = str1.substring(0, str1_len + 1)
									+ value;
								} else {
									is_value_ok = false;
									//log.warn("value is not number");
									return null;
								}
							} else
								t_result = str1 + " '" + value + "' ";
						}
						paramTotal.add(t_result);
					}
				}
			}
			if ("true".equals(isSubSQL)) {
				if (temp_sql != null)
					resultBuf.append(temp_sql);
			} else {
				int pt_size = paramTotal.size();
				for (int j = 0; j < pt_size; j++) {
					resultBuf.append(paramTotal.get(j));
					// resultBuf.append(" ");
					if (j < paramTotal.size() - 1)
						resultBuf.append(" and ");
				}
			}
		} else {
			if (t_sql != null)
				resultBuf.append(t_sql);
		}
		if (resultBuf != null)
			result = resultBuf.toString();

		if (result == null || result.length() == 0)
			result = null;

		paramPre.clear();
		paramNex.clear();
		paramTotal.clear();

		paramPre = null;
		paramNex = null;
		paramTotal = null;
		resultBuf = null;
		return result;
	}

	public void release() {
		super.release();
		sqlTablename = null;
		sqlFields = null;
		sqlWhere = null;
		isCount = "false";
		isSum = "false";
		sql = null;
		bodySqlWhere = null;
		isSave = "false";
		name = null;
	}

}
