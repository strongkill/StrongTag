package net.strong.taglib.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import javax.servlet.jsp.JspException;

import net.strong.bean.Constants;
import net.strong.database.weakDbSpecifyValueBean;
import net.strong.util.Helper;
import oracle.sql.CLOB;

import org.apache.commons.collections.OrderedMap;
import org.apache.struts.taglib.TagUtils;

//import net.strong.exutil.writeMessage;
/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description: 将所提供的SQL语句所得到的数据集存为ArrayList,
 * 并将其存入pageContext中，以便JSP能用kfdb:iterate用rows（属于ArrayList类） 进行遍历
 * 此标签一般与iterate,write，page及逻辑判断等标签一起使用。
 * </p>
 * 如下： <kfdb:dbListArray sqlTablename="bas_flowmeter" relateField="flowmeter_id"
 * rowMax="10" sqlOrderby="flowmeter_id"> </kfdb:dbListArray> 参数说明请查看tld文档
 * 此标签中，除了数据库中的数据外，程序自动加入了一个字段元素：1。index 当前页的序号，从0开始计数；2。index_1 当前页的
 * 序号，从1开始计数；3。t_index 所有记录的序号，即翻页后也不会从新计数，从0开始中；4。t_index_1 所有记录的序号，
 * 即翻页也不会从新计数，从1开始。
 *
 * 更新日志： 1.更改doEndTag()函数，此函数过于庞大，将里面的功能细分出来，这样对数据库连接也好控制。
 * 2.更改产生SQL语句的函数，使得所产生的SQL语句含有 with (nolock)子句 更新时间：2005-05-05 Strong Yuan
 * 3.由于通过设置with (nolock)会使用某些对多表进行查询的SQL语句出错，所以暂时将此功能屏避掉，可以通过
 * dbListArray标签的sqlTablename属性来实现，即在表名后面加上 with (nolock)语句。更新时间：2005-05-09
 * Strong Yuan
 * 4.当topNum主设置为-1时，不真正执行SQL语句，只是将sqlWhere,sql语句分别以sqlWhere,sql名称存入request对象中
 * 。更新时间：2005-11-22 Strong Yuan 5.增加group by支持.当有group by 传入时执行group by 操作,支持分页
 * 2009-09-23
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Strong Software International CO,.LTD Strong Software International
 * CO,.LTD
 * </p>
 *
 * @author Strong Yuan
 * @since 2007-06-18
 * @version 1.0
 */

public class dbListArrayTag extends dbTag {
	// public static int curr_list =0;
	// static Category cat =
	// Category.getInstance(dbListArrayTag.class.getName());

	//版本标记
	private int version = 1;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected String userPrc = "true";

	protected String iscached = "true";

	protected String rowStart = "0"; // 开始点

	protected String rowMax = null; // 最大数据量

	protected String relateField = null;

	protected String isMysql = "true";

	protected String maxSQL = null; // 当sql为用户设置时，maxSQL用于计算数据量

	protected int dbType = 1; // 数据库类型，0--MS SQL,1 -- MySQL , 2 -- postgreSQL ,3
	// -- oracle

	protected String pageControl = "false";

	protected int pageIndex = 0; // 当前页数

	protected int maxPageIndex = 0; // 最大页数

	protected int maxRecord = 0; // 最大记录数

	protected int pageMax = 100; // 一页最大显示数

	protected String comName = null; // 用于从此控件取值，并用于查询的where子句中。

	// protected String comNames = null; //用于从多个控件中取值，多个控件用：分割

	protected String classFieldName = "class_id";

	private String topNum = null; // 对于只需取出数据库中前几条数据的情况下，使用此参数指定需取出几条

	private String page_flag = "false";

	private boolean body_start = false; // 判断body有没有执行

	// 判断是否需要统计热门关键字，如果在查询条件中含有::keyword，并且参数中有keyword=xxx时
	// count_hotkey为true，即会进行统计。
	// private boolean count_hotkey = false;

	// 是否需要统计热门关键字，此参数是在需要统计时，但查询的条件不是通过::来设定，而是直接将条件
	// 加到body中，此时需要在dbListArray中的参数中设置此值为true.
	private String isKeyword = "false";

	// private long posstime = 0;

	// 以下为了控制时间，超过时间就不让用
	/*
	 * private int cur_year =0; private int cur_month = 0; private int max_year
	 * = 2005; private int max_month = 6;
	 */
	public void release() {
		rowStart = "0";
		rowMax = null;
		relateField = null;
		isMysql = "false";
		maxSQL = null;
		dbType = 0;
		pageControl = "true";
		comName = null;
		// comNames = null;
		classFieldName = "class_id";
		maxSQL = null;
		topNum = null;
		page_flag = "false";
		isKeyword = "false";
		super.release();
	}

	public void setIsKeyword(String isKeyword) {
		this.isKeyword = isKeyword;
	}

	public String getIsKeyword() {
		return this.isKeyword;
	}

	public void setPage_flag(String page_flag) {
		this.page_flag = page_flag;
	}

	public String getPage_flag() {
		return this.page_flag;
	}

	public void setPageControl(String pageControl) {
		this.pageControl = pageControl;
	}

	public String getPageControl() {
		return this.pageControl;
	}

	public void setMaxSQL(String maxSQL) {
		this.maxSQL = maxSQL;
	}

	public String getMaxSQL(String maxSQL) {
		return this.maxSQL;
	}

	public void setRowStart(String rowStart) {
		this.rowStart = rowStart;
	}

	public String getRowStart() {
		return rowStart;
	}

	public void setRowMax(String rowMax) {
		if (rowMax != null) {
			if (rowMax.indexOf("::") > -1) {
				rowMax = getParamValue("rowMax");
			}
			this.rowMax = rowMax;
			if (log.isDebugEnabled())
				log.debug("set rowMax : " + this.rowMax);
			this.pageMax = net.strong.util.MyUtil.getStringToInt(rowMax, 0);
			// Integer.valueOf(rowMax).intValue() ;

			if (this.pageMax <= 0)
				this.pageMax = 3;
		} else {
			this.pageMax = 0;
		}
	}

	public String getRowMax() {
		return rowMax;
	}

	public void setRelateField(String relateField) {
		this.relateField = relateField;
	}

	public String getRelateField() {
		return relateField;
	}

	public void setIsMysql(String isMysql) {
		this.isMysql = isMysql;
		if ("true".compareToIgnoreCase(isMysql) == 0) {
			dbType = 1;
		}
	}

	public String getIsMysql() {
		return isMysql;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getComName() {
		return comName;
	}

	/*
	 * public void setComNames(String comNames) { this.comNames = comNames; }
	 * public String getComNames() { return this.comNames; }
	 */
	public void setClassFieldName(String classFieldName) {
		this.classFieldName = classFieldName;
	}

	public String getClassFieldName() {
		return classFieldName;
	}

	protected String createMaxSQL() {
		String temp_sql = null;
		if (sql != null) {
			temp_sql = "select count(*) from (" + sql + ")";
		} else {
			if (maxSQL != null) {
				temp_sql = maxSQL;
			} else if (sqlTablename == null) {
				temp_sql = null;
			} else {
				String sql_where = createWhereSQL();
				temp_sql = "select count(*) from " + sqlTablename;

				if (sql_where != null && sql_where.length() > 0) {
					temp_sql += " where " + sql_where;
				}

				if (sqlGroupBy != null) {
					temp_sql = "select count(*) from (" + temp_sql
					+ " group by " + sqlGroupBy + ") s ";
					if (log.isDebugEnabled())
						log.debug(temp_sql);
				}

				sql_where = null;
			}
		}
		log.debug(temp_sql);
		return temp_sql;
	}

	protected String createWhereSQL() {
		String result = null;
		if (new_flag == null || "false".equalsIgnoreCase(new_flag))
			result = createWhereSQL(true); // 使用原方法产生where子句
		else
			result = createNewWhereSQL();// 使用新方法产生where子句
		if (result != null)
			result = result.replaceAll("`", "");
		// result = filterChar(result, '`');

		return result;
	}

	/**
	 * 将某个字符过虑掉
	 *
	 * @param value
	 * @param bec
	 * @return
	 */
	protected String filterChar(String value, char bec) {
		if (value == null || value.length() < 1)
			return value;
		char[] v_char = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		int char_len = v_char.length;
		for (int i = 0; i < char_len; i++) {
			if (v_char[i] == bec)
				continue;
			sb.append(v_char[i]);
		}
		if (sb.length() > 0)
			value = sb.toString();
		sb = null;
		return value;
	}

	/**
	 * 新的产生WHERE子句的函数，与原来的主要区别是加入[]来区别各个条件， 这样就不用人工去切割
	 *
	 * @return
	 */
	protected String createNewWhereSQL() {
		String result = null;
		String temp_sql = null;
		int t_index = -1;
		int t_index2 = -1;
		// int p_index = -1;
		if (body_start && bodySqlWhere != null && bodySqlWhere.length() > 0) {
			temp_sql = bodySqlWhere;
		} else if (sqlWhere != null && sqlWhere.length() > 0) {
			temp_sql = sqlWhere;
		}
		result = temp_sql;

		// bodySqlWhere = null;
		// sqlWhere = null;
		// System.out.println("result at createNewWhereSQL : " + result);

		ArrayList<String> paramPre = new ArrayList<String>(); // 用于记录where等号前的字符
		// bas_id
		ArrayList<String> paramNex = new ArrayList<String>(); // 用于记录where等号后的字符
		// id_bas
		ArrayList<String> paramTotal = new ArrayList<String>(); // 用于记录where完整子句
		// new_id =
		// id_new
		StringBuffer resultBuf = new StringBuffer(); // 用于存放where子句的临时字符串

		boolean three_flag = false; // 是否为三冒号（:::）标志，如是，则直接替换所取到的值

		String t_sql = temp_sql;
		if (t_sql != null) {
			t_index = t_sql.indexOf("[");
		}
		if (t_index > 0) {
			while (t_index > 0) {
				t_index = t_sql.indexOf("[");
				t_index2 = t_sql.indexOf("]");
				if (t_index2 < 0) {
					// 条件不匹配
					return null;
				}
				String total_str = t_sql.substring(0, t_index);
				if (total_str != null && total_str.trim().length() > 0)
					paramTotal.add(total_str);
				String p_str = t_sql.substring(t_index + 1, t_index2);
				String deal_str = dealParam(p_str);
				if (deal_str != null && deal_str.length() > 0)
					paramTotal.add(deal_str);
				t_sql = t_sql.substring(t_index2 + 1); // 获取剩下的字符串
				/*
				 * p_index = p_str.indexOf("::");
				 * //需要获取参数的子句的前半部分字串,如bas_id==::bas_id
				 * ,则前半部分为bas_id==，后半部分为bas_id String p_str_b =
				 * p_str.substring(0, p_index);// - 1); //需要获取参数的子句的后半部分字串
				 * String p_str_a = p_str.substring(p_index + 2);
				 * paramPre.add(p_str_b); paramNex.add(p_str_a);
				 */
				t_index = t_sql.indexOf("[");
			} // end while
			int p_size = paramPre.size();
			for (int i = 0; i < p_size; i++) {
				// String p_str_b = (String)paramPre.get(i);
				// String p_str_a = (String)paramNex.get(i);
				String str1 = null;
				String str2 = null;

				three_flag = false;

				str1 = paramPre.get(i);
				if (str1 != null)
					str1 = str1.trim();

				if (paramNex.size() > i)
					str2 = paramNex.get(i);
				if (str2 != null)
					str2 = str2.trim();
				String value = null;
				String b_str = null; // 开始的字符，用于判断是否以%开头或结尾
				String e_str = null; // 结束的字符
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
					value = pageContext.getRequest().getParameter(str2.trim());
					// 进行字符转换

					if (value == null || value.equalsIgnoreCase("null"))
						value = (String) pageContext.getRequest().getAttribute(
								str2.trim());

					if (value == null || value.equalsIgnoreCase("null"))
						value = (String) pageContext.getAttribute(str2.trim());
					// 从session中取值
					if (value == null || value.equalsIgnoreCase("null"))
						value = (String) pageContext.getSession().getAttribute(
								str2.trim());

					if (value == null || value.equalsIgnoreCase("null")
							|| value.trim().length() <= 0) {
						// 当参数propName不为空时，通过RequestUtils进行取值
						if (propName != null && propName.length() > 1) {
							try {
								Object obj_value = TagUtils.getInstance()
								.lookup(pageContext, propName,
										str2.trim());
								if (obj_value != null)
									value = String.valueOf(obj_value);
							} catch (Exception e) {
								if(log.isErrorEnabled())
									log.error(e.getMessage());
								value = null;
							}
						}
					}

				}

				value = getFilterString(value); // 过滤一些不安全的语句
				if ("errorwhere".equals(value)) {
					is_value_ok = false;
					return null;
				}

				if (value == null || value.equalsIgnoreCase("null")
						|| value.trim().length() <= 0) {
					// if (log.isDebugEnabled())
					// log.debug("value is null," + str2);

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
					// if (log.isDebugEnabled())
					// log.debug("value is ok," + value);

					/**
					 * 参数值中不允许含有and , update , exists ,select,from
					 */

					// save to pageContext for input text can get value from it
					pageContext.setAttribute(str2.trim(), value.trim());

					if ("true".equals(isSubSQL)) {
						temp_sql = temp_sql.replaceFirst(str2, value.trim());
					} else {
						String t_result = null;
						value = value.trim();
						if ("%".equals(b_str)) // 加上%,主要用于like查询
							value = "%" + value;
						if ("%".equals(e_str))
							value = value + "%";
						int str1_len = 0;
						if (three_flag) {
							t_result = str1 + " " + value;
						} else {
							if ((str1_len = str1.indexOf("==")) > 0) { // 当参数用"=="表示时，说明是进行为数字的比较，不用加双引号
								long lng_value = net.strong.util.MyUtil
								.getStringToLong(value, 0);
								if (lng_value == 0) {
									System.out.println("value:" + value);
									// log.error("value:"+value);
								}
								t_result = str1.substring(0, str1_len + 1)
								+ lng_value;
							} else
								t_result = str1 + " '" + value + "' ";
						}
						paramTotal.add(t_result);
						t_result = null;
					}
				}

			}
			if ("true".equals(isSubSQL)) {
				if (temp_sql != null)
					resultBuf.append(temp_sql);
			} else {
				while (paramTotal.size() > 0 && paramTotal.get(0) != null) {
					String t_result = paramTotal.remove(0);
					if (t_result == null || t_result.trim().length() < 0)
						continue;
					resultBuf.append(t_result);
					t_result = null;
				}
			}
		} else {
			if (t_sql != null)
				resultBuf.append(t_sql);
		}
		if (resultBuf != null)
			result = resultBuf.toString();

		paramPre.clear();
		paramNex.clear();
		paramTotal.clear();
		paramPre = null;
		paramNex = null;
		paramTotal = null;
		resultBuf = null;

		return result;
	}

	protected String dealParam(String str) {
		String result = "";
		int p_index = str.indexOf("::");
		if (p_index < 0) {
			// 字符串中不含有::，表明没有参数，直接反回
			return str;
		}

		while (p_index > 0) {
			String sub_str1 = null;
			if (p_index > 0) {
				sub_str1 = str.substring(0, p_index);
			}
			String str_left = str.substring(p_index + 2);
			int pe_index = str_left.indexOf(" "); // 判断余下的字符串是否含有空格
			String str_p = null;
			if (pe_index > 0) {
				str_p = str_left.substring(0, pe_index); // 参数字符串
				str = str_left.substring(pe_index); // 余下的字符串
			} else {
				str_p = str_left;
				str = "";
			}

			// String value = null;
			String b_str = null; // 开始的字符，用于判断是否以%开头或结尾
			String e_str = null; // 结束的字符
			if (str_p.length() > 1) {
				b_str = str_p.substring(0, 1); // 取第一个字符
				e_str = str_p.substring(str_p.length() - 1, str_p.length()); // 取出最后一个字符
			}
			if ("%".equals(b_str) && str_p.length() >= 1)
				str_p = str_p.substring(1, str_p.length());
			if ("%".equals(e_str) && str_p.length() >= 1)
				str_p = str_p.substring(0, str_p.length() - 1);
			if (":".equals(b_str) && str_p.length() >= 1)
				str_p = str_p.substring(1, str_p.length());

			String p_value = getParamValue(str_p); // 获取参数的值
			if (p_value != null && !p_value.equalsIgnoreCase("null")
					&& p_value.length() > 0) {
				if ("%".equals(b_str)) { // 含有％，用like
					result = result + sub_str1;
					result = result + " '%" + p_value + "%' ";
				} else if (":".equals(b_str)) {
					result = result + sub_str1;
					result = result + p_value + " ";
				} else if (sub_str1 != null) {
					if (sub_str1.indexOf("==") > 0) {
						sub_str1 = sub_str1.replaceFirst("==", "=");
						result = result + sub_str1;
						result = result + p_value + " ";
					} else {
						result = result + sub_str1;
						result = result + " '" + p_value + "' ";
					}
				}
			}
			p_index = str.indexOf("::");
			if (p_index < 0 && result != null && result.length() > 0)
				result = result + str + " ";
		}
		return result;
	}

	/**
	 * 分别从request,pagecontext,session,comName中取出对象
	 *
	 * @param str2
	 * @return
	 */
	protected String getParamValue(String str2) {
		String value = null;

		// 优先从地址栏取参数
		value = pageContext.getRequest().getParameter(str2.trim());
		// 然后从request容器里取参数
		if (value == null || value.equalsIgnoreCase("null"))
			value = (String) pageContext.getRequest().getAttribute(str2.trim());
		// 再到pageContext中取参数
		if (value == null || value.equalsIgnoreCase("null"))
			value = (String) pageContext.getAttribute(str2.trim());

		// 从session获取对象
		if (value == null || value.equalsIgnoreCase("null"))
			value = (String) pageContext.getSession().getAttribute(str2.trim());

		if (value == null || value.equalsIgnoreCase("null")
				|| value.trim().length() <= 0) {
			if (propName != null && propName.length() > 1) {
				try {
					Object obj_value = TagUtils.getInstance().lookup(
							pageContext, propName, str2.trim(), null);
					if (obj_value != null)
						value = String.valueOf(obj_value);
				} catch (Exception e) {
					if(log.isErrorEnabled())
						log.error(e.getMessage());
					value = null;
				}
			}
		}

		return value;
	}

	protected String createWhereSQL(boolean bdb_sql_where) {

		// System.out.println("start createWhereSql at dbListArrayTag : "+
		// System.currentTimeMillis());
		String result = null;
		String temp_sql = null;
		int t_index = -1;

		boolean three_flag = false; // 是否为三冒号（:::）标志，如是，则直接替换所取到的值

		if (body_start && bodySqlWhere != null && bodySqlWhere.length() > 0) {
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
			// if (log.isDebugEnabled())
			// log.debug("t_index : " + t_index);

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

				str1 = paramPre.get(i);
				if (str1 != null)
					str1 = str1.trim();

				if (paramNex.size() > i)
					str2 = paramNex.get(i);
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
					// if (log.isDebugEnabled())
					// log.debug("value is null," + value);

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
									// log.warn("value is not number");
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

		if (comName != null) {
			String comValue = pageContext.getRequest().getParameter(comName);
			if (comValue == null || comValue.equalsIgnoreCase("null")) {

				StringBuffer tmp_buf = new StringBuffer();
				if (dbType == 0) // ms sql
					tmp_buf.append("select top 1 ").append(classFieldName)
					.append(" from ").append(sqlTablename);
				else if (dbType == 1) // mysql
					tmp_buf.append("select ").append(classFieldName).append(
					" from ").append(sqlTablename).append(" limit 0,1");
				else if (dbType == 2) // postgreSQL
					tmp_buf.append("select ").append(classFieldName).append(
					" from ").append(sqlTablename).append(" limit 1");
				else if (dbType == 3) // oracle res.*
					tmp_buf.append("select ").append(
							FixSqlFieldsStarChar(null, "res.", classFieldName))
							.append(" from (select ").append(classFieldName)
							.append(" from ").append(sqlTablename).append(
							") res where rownum<=1");

				WeakHashMap<String, Object> result_map = null;
				Connection con = null;
				try {
					String sqlkey = tmp_buf.toString();
					if ("true".equalsIgnoreCase(iscached)) {
						try {
							Object tmp = GetObject(sqlkey);// Memcached.getInstance(Constants.getMemcachedPools(pageContext)).get(sql);
							if (tmp != null) {
								result_map = (WeakHashMap<String, Object>) tmp;
							}
							tmp = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (result_map == null) {
						weakDbSpecifyValueBean db = new weakDbSpecifyValueBean();

						db.setSql(sqlkey);
						tmp_buf = null;
						con = getConnection();// DriverManager.getConnection(Constants.getProxool_ReadOnly_Pools(pageContext));
						result_map = db.getdbValue(con);
						Helper.cleanup(con);
						db = null;
						if ("true".equalsIgnoreCase(iscached))
							SetObject(sqlkey, result_map);// Memcached.getInstance(Constants.getMemcachedPools(pageContext)).set(sql,
						// 30, result_map);
					}
					if (result_map != null) {
						comValue = String.valueOf(result_map.get(classFieldName
								.toLowerCase()));
						pageContext.setAttribute(comName, comValue);
						result_map.clear();
					}
					result_map = null;

				} catch (Exception e) {
					if(log.isErrorEnabled())
						log.error(e.getMessage());
					result_map = null;
					Helper.cleanup(con);
				} finally {
					result_map = null;
					Helper.cleanup(con);
				}
			}
			if (comValue != null && comValue.length() > 0) {
				if (result != null && result.length() > 0)
					result = result + " and " + comName + " = " + comValue;
				else
					result = comName + "=" + comValue;
			}
		}
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

	protected String createSQL() {
		return createSQL(null, null);
	}

	protected String createSQL(String sql_where, String this_orderby) {
		String temp_sql = null;
		StringBuffer temp_sql_buf = new StringBuffer();

		// String sql_where = null;
		if (sql == null) {

			// sql_where = createWhereSQL();
			if (sql_where != null) {
				if (sql_where.length() == 0)
					sql_where = null;
			} else {
				sql_where = createWhereSQL();
			}
		}
		if (sql != null) {
			if(version==2 && dbType==3){//v 2.0
				temp_sql = "select re.* from(select rownum rn,res.* from(" + sql
				+ ") res where rownum <=" + "?"
				+ " ) re where re.rn>" + "?";
			}else{	//v 1.0
				temp_sql = "select re.* from(select rownum rn,res.* from(" + sql
				+ ") res where rownum <=" + pageMax * (pageIndex + 1)
				+ " ) re where re.rn>" + pageMax * pageIndex;
			}
			System.out.println("SQL:" + temp_sql);
		} else {
			if (this_orderby == null)
				this_orderby = this.createOrderSQL();

			if (dbType == 0) { // MS SQL
				if (rowMax != null && relateField != null && pageMax >= 0) {
					temp_sql_buf.append("select top " + rowMax + " "
							+ sqlFields + " from " + sqlTablename);
					temp_sql_buf.append(" with (nolock) "); // 设置为此查询不加锁
					// 于2005-05-05添加
					if (sql_where != null) {
						if (this_orderby != null) {
							if (pageIndex == 0) // 在第一页，不用子查询，以便加快查询速度
								temp_sql_buf.append(" where " + sql_where
										+ " order by " + this_orderby);
							else
								temp_sql_buf.append(" where " + sql_where
										+ " and " + relateField
										+ " not in ( select top "
										+ String.valueOf(pageMax * pageIndex)
										+ " " + relateField + " from "
										+ sqlTablename + " where " + sql_where
										+ " order by " + this_orderby
										+ " ) order by " + this_orderby);
						} else {
							if (pageIndex == 0)// 在第一页，不用子查询，以便加快查询速度
								temp_sql_buf.append(" where " + sql_where);
							else
								temp_sql_buf.append(" where " + sql_where
										+ " and " + relateField
										+ " not in ( select top "
										+ String.valueOf(pageMax * pageIndex)
										+ " " + relateField + " from "
										+ sqlTablename + " where " + sql_where
										+ " ) ");
						}
					}
					if (sql_where == null) {
						if (this_orderby != null) {
							if (pageIndex == 0)
								temp_sql_buf
								.append(" order by " + this_orderby);
							else
								temp_sql_buf.append(" where " + relateField
										+ " not in ( select top "
										+ String.valueOf(pageMax * pageIndex)
										+ " " + relateField + " from "
										+ sqlTablename + " order by "
										+ this_orderby + " ) order by "
										+ this_orderby);
						} else {
							if (pageIndex > 0)
								temp_sql_buf.append(" where " + relateField
										+ " not in (select top "
										+ String.valueOf(pageMax * pageIndex)
										+ " " + relateField + " from "
										+ sqlTablename + ")");
						}
					}
				} else if (topNum != null) {
					temp_sql_buf.append("select top " + topNum + " "
							+ sqlFields + " from " + sqlTablename);
					temp_sql_buf.append(" with (nolock) "); // 设置为此查询不加锁
					// 于2005-05-05添加
					if (sql_where != null) {
						temp_sql_buf.append(" where " + sql_where);
					}
					if (this_orderby != null) {
						temp_sql_buf.append(" order by " + this_orderby);
					}

				} else {
					temp_sql_buf.append("select top 200 " + sqlFields
							+ " from " + sqlTablename);
					temp_sql_buf.append(" with (nolock) "); // 设置为此查询不加锁
					// 于2005-05-05添加
					if (sql_where != null) {
						temp_sql_buf.append(" where " + sql_where);
					}
					if (this_orderby != null) {
						temp_sql_buf.append(" order by " + this_orderby);
					}
				}
			} else if (dbType == 1) {// MySQL
				temp_sql_buf.append("select " + sqlFields + " from "
						+ sqlTablename);
				if (sql_where != null) {
					temp_sql_buf.append(" where " + sql_where);
				}
				if (this_orderby != null)
					temp_sql_buf.append(" order by " + this_orderby);
				if (topNum != null) {
					temp_sql_buf.append(" limit 0," + topNum);
				} else if (pageMax > 0) {
					if ("true".equalsIgnoreCase(userPrc)) {
						int orderby = 1;
						if (this_orderby.indexOf(" desc") == -1)
							orderby = 0;

						temp_sql_buf = null;
						temp_sql_buf = new StringBuffer();

						temp_sql_buf.append("CALL prc_page_result(");
						temp_sql_buf.append(pageIndex);
						temp_sql_buf.append(",\"");
						temp_sql_buf.append(sqlFields);
						temp_sql_buf.append("\",\"");
						temp_sql_buf.append(sqlTablename);
						temp_sql_buf.append("\",\"");
						if (sql_where == null || sql_where.length() == 0)
							sql_where = "";
						temp_sql_buf.append(sql_where);
						temp_sql_buf.append("\",\"");
						temp_sql_buf.append(this_orderby);
						temp_sql_buf.append("\",");
						temp_sql_buf.append(orderby);
						temp_sql_buf.append(",\"");
						temp_sql_buf.append(relateField);
						temp_sql_buf.append("\",");
						temp_sql_buf.append(pageMax);
						temp_sql_buf.append(')');
					} else {
						temp_sql_buf.append(" limit "
								+ String.valueOf(pageMax * pageIndex) + ","
								+ String.valueOf(pageMax));
					}
				}

			} else if (dbType == 2) { // postgreSQL
				temp_sql_buf.append("select " + sqlFields + " from "
						+ sqlTablename);
				if (sql_where != null) {
					temp_sql_buf.append(" where " + sql_where);
				}
				if (this_orderby != null)
					temp_sql_buf.append(" order by " + this_orderby);
				if (pageMax > 0)
					temp_sql_buf.append(" limit " + String.valueOf(pageMax)
							+ " offset " + String.valueOf(pageMax * pageIndex));

			} else if (dbType == 3) { // oracle
				if (topNum != null) {
					temp_sql_buf.append("select ").append(
							FixSqlFieldsStarChar(null, "res.", sqlFields))
							.append(" from (");

					temp_sql_buf.append("select ").append(sqlFields).append(
							" from " + sqlTablename);
					if (sql_where != null) {
						temp_sql_buf.append(" where ").append(sql_where);

					}
					if (sqlGroupBy != null) // 增加Group by 功能 20090923
						temp_sql_buf.append(" group by ").append(sqlGroupBy);

					if (this_orderby != null)
						temp_sql_buf.append(" order by ").append(this_orderby);

					if(version==2 && dbType==3){//V 2.0
						temp_sql_buf.append(") res where rownum <=?");
					}else{//V 1.0
						temp_sql_buf.append(") res where rownum <=").append(topNum);
					}
				} else if (pageMax > 0) {
					temp_sql_buf = new StringBuffer();
					temp_sql_buf.append("select ").append(
							FixSqlFieldsStarChar("res.rn,", "res.", sqlFields))
							.append(" from (");
					temp_sql_buf.append("select ROWNUM RN,").append(
							FixSqlFieldsStarChar(null, "res_s.", sqlFields))
							.append(" from (");
					temp_sql_buf.append("select ").append(sqlFields).append(
					" from ").append(sqlTablename);
					if (sql_where != null) {
						temp_sql_buf.append(" where ").append(sql_where);
					}

					if (sqlGroupBy != null) // 增加Group by 功能 20090923
						temp_sql_buf.append(" group by ").append(sqlGroupBy);

					if (this_orderby != null)
						temp_sql_buf.append(" order by ").append(this_orderby);

					if(version==2 && dbType==3){//V 2.0
						temp_sql_buf.append(") res_s where rownum <=?");
						temp_sql_buf.append(") res where rn>?");
					}else{//V 1.0
						temp_sql_buf.append(") res_s where rownum <=").append(pageMax * (pageIndex + 1));
						temp_sql_buf.append(") res where rn>").append(pageMax * pageIndex);
					}
				}
			}
			temp_sql = temp_sql_buf.toString();
			temp_sql_buf = null;
		}

		return temp_sql;
	}

	public int doStartTag() throws JspException {
		dbType = Constants.getDataType(pageContext);
		return (EVAL_BODY_BUFFERED);
	}

	public int doAfterBody() throws JspException {

		if (bodyContent != null) {
			bodySqlWhere = bodyContent.getString();
			if (bodySqlWhere != null) {
				bodySqlWhere = bodySqlWhere.trim();
			}
			if (bodySqlWhere == null || bodySqlWhere.length() < 1) {
				bodySqlWhere = null;
			}
		} else {
			bodySqlWhere = null;
		}

		body_start = true;

		bodySqlWhere = getFilterString(bodySqlWhere, false);

		if ("errorwhere".equals(bodySqlWhere))
			is_value_ok = false;
		// System.out.println("finish doAfterBody :" +
		// (System.currentTimeMillis()-posstime));
		return (SKIP_BODY);

	}

	/**
	 * 获取所指查询的最大记录数
	 *
	 * @param sqlTablename
	 *            查询的表名
	 * @param tt_where
	 *            查询的WHERE子句
	 * @param pool
	 *            连接池
	 * @param max_map
	 *            记录最大页数的OrderdMap对象
	 * @param system_name
	 *            系统名
	 * @return
	 */
	private long getPageMax(String sqlTablename, String tt_where,
			OrderedMap max_map, String system_name) {
		long p_max = 0;
		int md5_p_max = 0;
		if (md5_p_max == 0) {
			Connection con = null;
			try { // 获取最大记录数
				StringBuffer max_sql = new StringBuffer();
				if (dbType == 0) {// ms sql

				} else if (dbType == 1) { // mysql
					if("true".equalsIgnoreCase(userPrc)){
						max_sql.append("call prc_getTableCount(\"");
						max_sql.append(sqlTablename);
						max_sql.append("\",\"");
						if (tt_where != null && tt_where.length() > 0)
							max_sql.append(tt_where);
						max_sql.append("\");");
					}else{
						max_sql.append("select count(*) as count from " );
						max_sql.append(sqlTablename);
						if (tt_where != null && tt_where.length() > 0)
							max_sql.append(" where " +  tt_where);	
					}
				} else if (dbType == 2) { // postgreSQL

				} else if (dbType == 3) { // oracle
					if (sql != null) {
						max_sql.append("select count(*) as count from (")
						.append(sql).append(')');
						System.out.println("MaxSQL:" + max_sql.toString());
					} else {
						max_sql.append("select count(*) as count from ");
						max_sql.append(sqlTablename);
						if (tt_where != null && tt_where.length() > 0) {
							max_sql.append(" where ");
							max_sql.append(tt_where);
						}
						if (sqlGroupBy != null && sqlGroupBy.length() > 0) {
							max_sql.append(" group by ").append(sqlGroupBy);
							StringBuffer groupbystr = new StringBuffer();
							groupbystr
							.append("select count(*) as count from (")
							.append(max_sql).append(") s");
							max_sql = new StringBuffer();
							max_sql.append(groupbystr);
							groupbystr = null;
						}
					}
				}

				maxRecord = 0;// getJdbcTemplate().queryForInt(max_sql.toString());

				// if("true".equalsIgnoreCase(iscached)){
				try {
					Object tmp = GetObject(max_sql.toString());// Memcached.getInstance(Constants.getMemcachedPools(pageContext)).get(max_sql.toString());
					if (tmp != null) {
						try {
							maxRecord = (Integer) tmp;
						} catch (Exception e) {
							System.out.println("tmp:" + tmp + ";Key:"
									+ max_sql.toString());
							e.printStackTrace();
						}
					}
					tmp = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				// }
				if (maxRecord == 0) {
					con = getConnection();// DriverManager.getConnection(Constants.getProxool_ReadOnly_Pools(pageContext));
					long cost_1 = System.currentTimeMillis();
					PreparedStatement pstmt = con.prepareStatement(max_sql
							.toString());
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						maxRecord = rs.getInt("count");
					}
					long cost_2 = System.currentTimeMillis();
					if (cost_2 - cost_1 > 1000 * 4) {
						try {
							String page = getRequest().getRequestURL().append(
									getRequest().getQueryString() == null ? ""
											: "?"
												+ getRequest()
												.getQueryString())
												.toString();
							System.out
							.println("*********************=== PAY ATTENTION:SLOW SQL(Cost:"
									+ (cost_2 - cost_1)
									+ " Millis) in "
									+ page
									+ " ===*********************");
							System.out.println(max_sql.toString());
							System.out
							.println("**********************************************END*************************************************");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Helper.cleanup(pstmt, con, rs);
					if ("true".equalsIgnoreCase(iscached))
						SetObject(max_sql.toString(), Constants
								.getMemcachedTimeOutSecond(pageContext)
								* (1 + pageIndex), maxRecord);// Memcached.getInstance(Constants.getMemcachedPools(pageContext)).set(max_sql.toString(),
					// 30,
					// maxRecord);
				}
				p_max = Math.round(Math.ceil(maxRecord * 1.0 / pageMax));

				max_sql = null;

			} catch (Exception e2) {
				Helper.cleanup(con);
				System.out.println(" -- " + system_name
						+ ",getPageMax Exception : " + e2.getMessage());

				System.out.println("tt_where at dbListArrayTag:" + tt_where);
				System.out.println("sqlTablename at dbListArrayTag:"
						+ sqlTablename);

				ArrayList<String> rows = new ArrayList<String>();
				pageContext.setAttribute(this.name, rows);// 保存数据
				return (EVAL_PAGE);
			} finally {
				Helper.cleanup(con);
			}
		} else {
			// p_max = md5_p_max;
			maxRecord = md5_p_max;
			p_max = Math.round(Math.ceil(maxRecord * 1.0 / pageMax)); // 最大的页数
			// p_max = maxPageIndex;
		}
		// max_sql = null;

		return p_max; // 返回最大页数
	}

	public int doEndTag() throws JspException {

		if (!is_value_ok) // 条件不符合要求，直接返回
			return EVAL_PAGE;

		// Connection con = null;

		pageIndex = 0; // 当前页数
		maxPageIndex = 0; // 最大页数
		maxRecord = 0; // 最大记录数
		pageMax = 10; // 一页最大显示数

		// 获取当前所在页
		Object pageObj = pageContext.getRequest().getParameter("page");
		// 获取最大页数
		Object maxPageObj = pageContext.getRequest().getAttribute("maxPage");
		// 获取最大记录数
		Object maxRecordObj = pageContext.getRequest()
		.getAttribute("maxRecord");

		if (pageObj != null)
			pageIndex = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(pageObj), 0);
		if (maxPageObj != null)
			maxPageIndex = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(maxPageObj), 0);
		if (maxRecordObj != null)
			maxRecord = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(maxRecordObj), 0);

		if (rowMax != null && rowMax.length() > 0 && relateField != null) {
			pageMax = net.strong.util.MyUtil.getStringToInt(rowMax, 0);
		} else {
			pageMax = 10;
		}

		OrderedMap max_map = null;
		// ResultSet rs = null;

		String tt_where = createWhereSQL();

		String tt_order = createOrderSQL();

		if (tt_where == null)
			tt_where = "";
		if (tt_order == null)
			tt_order = "";

		if (!is_value_ok) // 条件不符合要求，直接返回
			return EVAL_PAGE;

		ArrayList<Object> curFieldList = new ArrayList<Object>(); // 初始化字段列表

		long p_max = 0;

		if (rowMax != null && rowMax.length() > 0 && relateField != null) {
			p_max = getPageMax(sqlTablename, tt_where, max_map, null);
		}

		String this_sql = createSQL(tt_where, tt_order); // 花时间在这里
		if (log.isDebugEnabled())
			log.debug("This SQL : " + this_sql);

		if (!is_value_ok) // 条件不符合要求，直接返回
			return EVAL_PAGE;

		if ("-1".equals(topNum)) {
			// 当设置topNum为－１时，不真正执行SQL语句，只是将sqlWhere,sql,orderby语句分别以sqlWhere,sql,orderby名称存入request对象中。更新时间：2005-11-22
			// Strong Yuan
			pageContext.getRequest().setAttribute("sqlWhere", tt_where);
			pageContext.getRequest().setAttribute("sql", this_sql);
			pageContext.getRequest().setAttribute("orderby", tt_order);
			pageContext.getRequest().setAttribute("groupby", sqlGroupBy);
			return (EVAL_PAGE);
		}
		// Statement stmt = null;
		// Statement stmt2 = null;

		// boolean con_is_close = false;
		// System.out.println("finish CreateSQlANDPrepareExecute : "+(System.currentTimeMillis()
		// - posstime));
		Connection con = null;
		ArrayList<HashMap<String, Object>> rows = new ArrayList<HashMap<String, Object>>();
		try {// 取得记录列表
			//
			// con.setReadOnly(true);
			if (relateField == null) {
				relateField = "";
			}
			String str_sql = this_sql;// createSQL(tt_where,tt_order);
			if (sql == null) {
				str_sql = this_sql;
			}

			//保存数据到memcached
			String sqlkey = null;

			if ("true".equalsIgnoreCase(iscached)) {
				try {
					if (version==2 && dbType==3){ //v 2.0
						if(sql!=null){
							sqlkey = str_sql.replaceFirst("\\?",String.valueOf(pageMax * (pageIndex + 1))).replaceFirst("\\?", String.valueOf(pageMax * pageIndex));
						}else if(topNum!=null){
							sqlkey = str_sql.replaceFirst("\\?", topNum);
						}else if(pageMax>0){
							sqlkey = str_sql.replaceFirst("\\?",String.valueOf(pageMax * (pageIndex + 1))).replaceFirst("\\?", String.valueOf(pageMax * pageIndex));
						}
					}else { //v 1.0
						sqlkey = str_sql;
					}
					Object tmp = GetObject(sqlkey);// Memcached.getInstance(Constants.getMemcachedPools(pageContext)).get(str_sql);
					if (tmp != null) {
						rows = (ArrayList<HashMap<String, Object>>) tmp;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rows.size() == 0) {
				con = getConnection();
				long cost_1 = System.currentTimeMillis();
				PreparedStatement pstmt = con.prepareStatement(str_sql);

				if(version==2 && dbType==3){ //v 2.0
					//解決數據庫服務器因编译SQL而产生商负载问题。
					if (sql != null) {
						pstmt.setInt(1, pageMax * (pageIndex + 1));
						pstmt.setInt(2, pageMax * pageIndex);
					}else if (topNum != null) {
						pstmt.setInt(1, Integer.parseInt(topNum));
					} else if (pageMax > 0) {
						pstmt.setInt(1, pageMax * (pageIndex + 1));
						pstmt.setInt(2, pageMax * pageIndex);
					}
				}else{ //v 1.0
					//不需要处理
				} 

				ResultSet rs = pstmt.executeQuery();
				long cost_2 = System.currentTimeMillis();
				if (cost_2 - cost_1 > 1000 * 4) {
					try {
						String page = getRequest().getRequestURL().append(
								getRequest().getQueryString() == null ? ""
										: "?" + getRequest().getQueryString())
										.toString();

						System.out
						.println("*********************=== PAY ATTENTION:SLOW SQL(Cost:"
								+ (cost_2 - cost_1)
								+ " Millis) in "
								+ page + " ===*********************");
						System.out.println(str_sql);
						System.out
						.println("**********************************************END*************************************************");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rs.getMetaData().getColumnCount();
				pageContext.getRequest().setAttribute("colCount",
						String.valueOf(colCount));// 将当前字段数保存起来
				int index = 0;

				int index_1 = 1;

				while (rs.next()) {
					HashMap<String, Object> row = new HashMap<String, Object>();
					row.put("index", String.valueOf(index)); // 当前页的序号,从0开始的序号
					row.put("index_1", String.valueOf(index_1)); // 当前页的序号,从1开始的序号
					long t_index = pageIndex * pageMax + index;
					row.put("t_index", String.valueOf(t_index)); // 所有记录的序号，从0开始
					row.put("t_index_1", String.valueOf(t_index + 1)); // 所有记录的序号,从1开始
					for (int i = 1; i <= colCount; i++) {
						// 所有名称转换为小写方式
						String name = rsmd.getColumnLabel(i).toLowerCase();
						if (name == null || name.length() == 0)
							name = rsmd.getColumnName(i).toLowerCase();

						String type_name = rsmd.getColumnTypeName(i)
						.toLowerCase();
						// System.out.println("field name : " + name + " ,
						// type_anme
						// : " + type_name);
						if ("text".equals(type_name)) {
							// 当字段类型为text时，如果直接用rs.getObject(i)取数据，在jtds1.0版本中不能直接取出，改为以下方式即可
							// 2005-07-22 Strong Yuan
							String tt_str = rs.getString(i);
							if (tt_str == null)
								tt_str = "";
							else
								tt_str = tt_str.trim();
							row.put(name.trim(), tt_str);
						} else if ("clob".equalsIgnoreCase(type_name)) {
							try {
								CLOB tm = (CLOB) rs.getObject(i);
								String tmp = null;
								if (tm != null) {
									tmp = tm.getSubString(1, (int) tm.length());
									tm = null;
								}
								row.put(name.trim(), tmp);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}else if("date".equalsIgnoreCase(type_name)){
							Object value = rs.getTimestamp(i);
							row.put(name.trim(), value);
						} else {
							Object obj = rs.getObject(i);
							if (obj instanceof java.lang.String) {
								String t_str = null;
								t_str = String.valueOf(obj).trim();
								row.put(name.trim(), t_str);
							} else {
								row.put(name.trim(), obj);
							}
							if (rec_field_name != null) { // 需要保存字段的值列表
								if (rec_field_name.equalsIgnoreCase(name)) {
									curFieldList.add(obj);
								}
							}
						}
					}
					index++;
					index_1++;
					rows.add(row);
					row = null;
				}
				Helper.cleanup(pstmt, con, rs);
				if ("true".equalsIgnoreCase(iscached))
					SetObject(sqlkey, Constants
							.getMemcachedTimeOutSecond(pageContext)
							* (1 + pageIndex), rows);// Memcached.getInstance(Constants.getMemcachedPools(pageContext)).set(str_sql,
				// 30, rows);
				// DBcached.getInstance().getServerCache().put(str_sql, rows);
			}
			pageContext.getRequest().setAttribute("rowCount",
					String.valueOf(rows.size()));// 将当前记录数保存起来，在分页时将用到。

		} catch (Exception ee) {
			Helper.cleanup(con);
			log.error(this_sql);
			ee.printStackTrace();
			return (EVAL_PAGE);
		} finally {
			Helper.cleanup(con);
		}
		if(bussinessClass!=null && bussinessClass.length()>0){
			long[] pageinfo = {p_max,pageMax,maxRecord};
			ArrayList<HashMap<String, Object>> tmprows = BussinessProcess(bussinessClass, rows, pageinfo);
			if(tmprows!=null && tmprows.size()>0)
				rows = tmprows;
		}
		pageContext.setAttribute(this.name, rows);

		rows = null;
		if (rowMax != null && rowMax.length() > 0) {
			pageContext.getRequest().setAttribute("maxPage",
					String.valueOf(p_max));
			// if (log.isDebugEnabled())
			// log.debug("pageMax is disable");
			pageContext.getRequest().setAttribute("pageMax",
					String.valueOf(pageMax));
			pageContext.getRequest().setAttribute("maxRecord",
					String.valueOf(maxRecord));
		}
		if (curFieldList != null && curFieldList.size() > 0) {
			pageContext.setAttribute("curFieldList", curFieldList);
		}

		pageContext.removeAttribute("nokey");
		if (topNum != null && topNum.length() > 0) {
			pageContext.setAttribute("nokey", "true"); // 不需要进行加亮显示
		}

		curFieldList = null;

		// con = null;
		body_start = false;
		// if (log.isDebugEnabled())
		// log.debug("need time : " + (System.currentTimeMillis() - posstime));
		// System.out.println("finish allStep :" +
		// (System.currentTimeMillis()-posstime));
		return (EVAL_PAGE);
	}

	public String getTopNum() {
		return topNum;
	}

	public void setTopNum(String topNum) {
		if (topNum.indexOf("::") > -1) {
			topNum = getParamValue("topNum");
		}
		this.topNum = topNum;
	}

	/*
	 * private void setCurRecordNum(int cur_rec) { int rec_num =
	 * getCurRecordNum(); //
	 * System.setProperty("cur_rec_num",String.valueOf(rec_num + cur_rec)); }
	 * private int getCurRecordNum() { String str_rec =
	 * System.getProperty("cur_rec_num"); int rec_num = 0; if(str_rec != null) {
	 * try { rec_num = Integer.getInteger(str_rec).intValue(); } catch(Exception
	 * e) { rec_num = 0; } } return rec_num; }
	 *
	 * class maxRecordObject{ private int record_value = 0; private long
	 * start_time = 0; private int run_times = 0; int getRecord_value() { return
	 * this.record_value; } void setRecord_value(int record_value) {
	 * this.record_value = record_value; } long getStart_time() { return
	 * this.start_time; } void setStart_time(long start_time) { this.start_time
	 * = start_time; } int getRun_times() { return this.run_times; } void
	 * setRun_times(int run_times) { this.run_times = run_times; } }
	 */

	public String getUserPrc() {
		return userPrc;
	}

	public void setUserPrc(String userPrc) {
		this.userPrc = userPrc;
	}

	public String getIscached() {
		return iscached;
	}

	public void setIscached(String iscached) {
		this.iscached = iscached;
	}
}
