package net.strong.taglib.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;

import net.strong.bean.Constants;
import net.strong.util.Helper;

import org.apache.struts.taglib.TagUtils;

/**
 * Title:自定义选择标签
 * Description: 从数据库中取数据，并将数据作为选择框的数据， 并可实现分级效果
 * Copyright: Copyright (c) 2003
 * Company: Strong Software International CO,.LTD
 * 
 * @author unascribed
 * @version 1.0
 */

public class dbSelectTag extends dbTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4165091937129437644L;

	private String hasParent = "true";

	private String parentIdName = "PARENT_ID";

	private String tableName = "PRD_CLS";

	private String disFieldName = "NAME";

	private String valueFieldName = "BAS_ID";

	private String name = "BAS_ID";

	private String id = null;

	private String className = null;

	private String hasBlank = "false";

	private String blankMsg = null;

	private String blankValue = "";

	private String onChange = null;

	private String title = null;

	private String size = null;

	private String hasMultiple = "false";

	private String selectedName = null;

	private String selectedProperty = null;

	/**
	 * 设置选上的值，如果当前列表中有一值与指定的selectedValue值相同，则设置为选上
	 */
	private String selectedValue = null;

	private String onlyShowLeaf = "false";

	// private boolean bonlySL = false;
	private String onlyLeaf = "false";

	// private boolean bonlyL = false; //只显示叶子结点
	private String isFieldMsg = "false"; // 设置是否为显示disFieldName对应的资源信息

	// private boolean bFieldMsg = false;
	private String msgResources = "net.strong.resources.ApplicationResources";

//	protected String isDebug = "false";

	// 是否为调试状态，如为true，则记录调试信息（主要是数据库的查询语句）

	protected String scopeType = "4";

//	protected int scope_type = 4;

	// private Locale locale = null;

	// private PoolBean pool = null;
	// private Connection con = null;
	// private Statement stmt = null;
	// private int layer = 0;
	//private StringBuffer strBuf = null;

	// private String selectedValue = null;

	//private boolean bFirst = true; // 菜单的第一次循环

	// 此参数用于通过RequestUtil对象进行取值，即，设置此参数后，WHERE子句的参数值就可以从HashMap对象或JavaBean对象中取值

	protected String id_field_name = "bas_id"; // 表中记录ID的字段名

	private String bodySqlWhere = null;

	private String new_flag = "false";

	private long posstime = 0;

	public void setId_field_name(String id_field_name) {
		this.id_field_name = id_field_name;
	}

	public String getId_field_name() {
		return this.id_field_name;
	}

	public void setIsDebug(String isDebug) {
		this.isDebug = isDebug;
	}

	public String getIsDebug() {
		return this.isDebug;
	}

	public void setMsgResources(String msgResources) {
		this.msgResources = msgResources;
	}

	public String getMsgResources() {
		return this.msgResources;
	}

	public void setIsFieldMsg(String isFieldMsg) {
		this.isFieldMsg = isFieldMsg;
		/*
		 * if(isFieldMsg.equalsIgnoreCase("true")) bFieldMsg = true; else
		 * bFieldMsg = false;
		 */
	}

	public String getIsFieldMsg() {
		return this.isFieldMsg;
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

	public void setOnlyLeaf(String onlyLeaf) {
		this.onlyLeaf = onlyLeaf;
		/*
		 * if(this.onlyLeaf.compareToIgnoreCase("true")==0) this.bonlyL = true;
		 */
	}

	public String getOnlyLeaf() {
		return this.onlyLeaf;
	}

	public void setHasParent(String hasParent) {
		this.hasParent = hasParent;
	}

	public String getHasParent() {
		return hasParent;
	}

	public void setParentIdName(String parentIdName) {
		this.parentIdName = parentIdName;
	}

	public String getParentIdName() {
		return parentIdName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.sqlTablename = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setDisFieldName(String disFieldName) {
		this.disFieldName = disFieldName;
	}

	public String getDisFieldName() {
		return disFieldName;
	}

	public void setValueFieldName(String valueFieldName) {
		this.valueFieldName = valueFieldName;
	}

	public String getValueFieldName() {
		return valueFieldName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setHasBlank(String hasBlank) {
		this.hasBlank = hasBlank;
	}

	public String getHasBlank() {
		return hasBlank;
	}

	public void setBlankMsg(String blankMsg) {
		this.blankMsg = blankMsg;
	}

	public String getBlankMsg() {
		return blankMsg;
	}

	public void setBlankValue(String blankValue) {
		this.blankValue = blankValue;
	}

	public String getBlankValue() {
		return blankValue;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	public void setHasMultiple(String hasMultiple) {
		this.hasMultiple = hasMultiple;
	}

	public String getHasMultiple() {
		return hasMultiple;
	}

	public void setSelectedName(String selectedName) {
		this.selectedName = selectedName;
	}

	public String getSelectName() {
		return selectedName;
	}

	public void setSelectedProperty(String selectedProperty) {
		this.selectedProperty = selectedProperty;
	}

	public String getSelectedProperty() {
		return selectedProperty;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getSelectedValue() {
		return this.selectedValue;
	}

	public void setOnlyShowLeaf(String onlyShowLeaf) {
		this.onlyShowLeaf = onlyShowLeaf;
		/*
		 * if(this.onlyShowLeaf.compareToIgnoreCase("true")==0) bonlySL = true;
		 */
	}

	public String getOnlyShowLeaf() {
		return this.onlyShowLeaf;
	}

	public int doStartTag() throws JspException {
		posstime = System.currentTimeMillis();
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
		return (SKIP_BODY);

	}

	public int doEndTag() throws JspException {
		// long start_time = System.currentTimeMillis();
		int dbType = Constants.getDataType(pageContext);
		String tt_sqlWhere = null;
		String tt_sqlOrder = null;
		// if(bodySqlWhere!=null)
		// tt_sqlWhere = bodySqlWhere;
		tt_sqlWhere = createWhereSQL();
		tt_sqlOrder = createOrderSQL();


		StringBuffer tmp_buf = new StringBuffer();
		if(dbType==0){//mssql
			tmp_buf.append("select ").append(sqlFields).append(" from " ).append(sqlTablename);
			if (tt_sqlWhere != null)tmp_buf.append(" where ").append(tt_sqlWhere);			
			tmp_buf.append(" order by ").append(tt_sqlOrder);
		}else if(dbType==1){//mysql
			tmp_buf.append("call prc_execute('");
			tmp_buf.append(sqlTablename);
			tmp_buf.append("','");
			tmp_buf.append(sqlFields);
			tmp_buf.append("','");
			if (tt_sqlWhere != null)
				tmp_buf.append(tt_sqlWhere);
			tmp_buf.append("','");
			if (tt_sqlOrder != null)
				tmp_buf.append(tt_sqlOrder);
			tmp_buf.append("');");
		}else if(dbType==2){//postgreSQL
			tmp_buf.append("select ").append(sqlFields).append(" from " ).append(sqlTablename);
			if (tt_sqlWhere != null)tmp_buf.append(" where ").append(tt_sqlWhere);			
			tmp_buf.append(" order by ").append(tt_sqlOrder);			
		}else if(dbType==3){//oracle
			tmp_buf.append("select ").append(sqlFields).append(" from " ).append(sqlTablename);
			if (tt_sqlWhere != null)tmp_buf.append(" where ").append(tt_sqlWhere);			
			tmp_buf.append(" order by ").append(tt_sqlOrder);
		}
		String tt_sql = tmp_buf.toString();
		if(log.isDebugEnabled())
			log.debug("temp_sql:"+tt_sql);
		tmp_buf = null;
		//strBuf = new StringBuffer();
		// locale = pageContext.getRequest().getLocale();
		//System.out.println(tt_sql);
		// String path =
		// pageContext.getServletContext().getRealPath("/WEB-INF/classes/");
		Object selObj = null;
		if (selectedName != null) {
			Object value = null;
			if (selectedProperty != null) {
				try {
					value = TagUtils.getInstance().lookup(pageContext,
							selectedName, selectedProperty, null);
				} catch (Exception e) {
					// 通过RequestUtils.lookup找不到值，进行下一步操作
					value = null;
				}
				if (value != null)
					selectedValue = String.valueOf(value);
				// System.out.println("selectedValue:"+selectedValue + " at
				// dbSelectedTag");
			}
			if (selectedProperty == null)// && selectedValue!=null) //未找到数据
			{
				selObj = pageContext.getRequest().getParameter(selectedName);
				if (selObj == null)
					selObj = pageContext.getAttribute(selectedName);
				if (selObj != null) {
					selectedValue = String.valueOf(selObj);
				}

			}
		}

		selObj = null;

		//bFirst = true;


		StringBuffer out_str_buf = new StringBuffer();
		out_str_buf.append("<select name=\"").append(name).append("\"");
		if (id != null)
			out_str_buf.append(" id = \"").append(id).append("\"");
		if (className != null)
			out_str_buf.append(" class = \"" ).append( className ).append( "\"");
		if (onChange != null)
			out_str_buf.append(" onchange = \"" ).append( onChange ).append( "\"");
		if (title!=null)
			out_str_buf.append(" title=\"").append(title).append("\"");
		if (size != null)
			out_str_buf.append(" size = \"" ).append( size ).append( "\"");
		if (hasMultiple.compareToIgnoreCase("true") == 0)
			out_str_buf.append(" multiple ");
		out_str_buf.append(" >");
		if (hasBlank.compareToIgnoreCase("true") == 0) {
			if (blankMsg != null)
				out_str_buf.append("<option value=\"" ).append( blankValue + "\">").append( blankMsg ).append( "</option>");
			else
				out_str_buf.append("<option value=\"" ).append( blankValue).append( "\"></option>");
		}
		try {
			out_str_buf.append(initMenu(tt_sql));
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("tt_sql:" + tt_sql);
			return (EVAL_PAGE);
		}
		//strBuf);
		out_str_buf.append("</select>");

		try {
			pageContext.getOut().write(out_str_buf.toString());
			pageContext.getOut().flush();
		} catch (IOException e) {
			throw new JspException("IO Error: " + e.getMessage());
		}
		// long end_time = System.currentTimeMillis();
		out_str_buf = null;
		// System.out.println("spend time:"+(end_time-start_time));
		if(log.isDebugEnabled())
			log.debug("Select Tag finished :"+ (System.currentTimeMillis() - posstime));
		return (EVAL_PAGE);
	}

	public String initMenu(String sql) // throws Exception
	{

		net.strong.taglib.util.ClassItems c_item = new net.strong.taglib.util.ClassItems();
		c_item.setIdFieldName(id_field_name);
		c_item.setParentFieldName(parentIdName);
		Connection con = null;
		try {
			//con = getDataSource().getConnection();
			con =getConnection();// DriverManager.getConnection(Constants.getProxool_ReadOnly_Pools(pageContext));
			c_item.initData(con, sql);
		} catch (SQLException e1) {
			Helper.cleanup(con);
			e1.printStackTrace();
		} finally {
			Helper.cleanup(con);
		}
		// c_item.initData(con,sql);

		HashMap<String, Object> t_map = null;
		StringBuffer strBuf = new StringBuffer();
		String id = "0";
		String p_id = "0";
		while ((t_map = c_item.getDataItem(id, p_id, hasParent)) != null) {
			Object disObj_sub = t_map.get(disFieldName); // 显示的数据
			Object valueObj_sub = t_map.get(valueFieldName); // select的值
			String str_value = String.valueOf(disObj_sub);
			String level = String.valueOf(t_map.get("level"));
			// String isLeaf = String.valueOf(t_map.get("isLeaf"));

			int i_level = 1;
			try {
				i_level = Integer.valueOf(level).intValue();
			} catch (Exception e) {
				i_level = 1;
			}

			String beforeStr = "";
			StringBuffer str = new StringBuffer();
			for (int m = 1; m < i_level; m++) {
				beforeStr += "--";
			}
			String str_valueObj = String.valueOf(valueObj_sub);
			if (str_valueObj != null)
				str_valueObj = str_valueObj.trim();

			if (selectedValue != null
					&& selectedValue.compareToIgnoreCase(String
							.valueOf(valueObj_sub)) == 0) {
				str.append("<option value=\"" ).append( str_valueObj ).append( "\" selected>"
				).append( beforeStr ).append( str_value ).append( "</option>");

			} else {
				/*
				 * 2009年8月3日取消，当selectedName为空是，会替换原ActionForm的对象，导致页面出错。
				if (selectedName != null
						&& (selectedValue == null || "null"
								.equalsIgnoreCase(selectedValue))) {
					if (bFirst) {
						pageContext.setAttribute(selectedName, String
								.valueOf(valueObj_sub));
						bFirst = false;
					}
				}*/
				str.append("<option value=\"" ).append( str_valueObj ).append( "\">" + beforeStr
				).append( str_value ).append( "</option>");

			}
			strBuf.append(str);
			t_map = null;
		}
		c_item.clearData();
		c_item = null;
		return strBuf.toString();
	}

	public void release() {
		hasParent = "true";
		parentIdName = "PARENT_ID";
		tableName = "PRD_CLS";
		disFieldName = "NAME";
		valueFieldName = "BAS_ID";
		name = "BAS_ID";
		id = null;
		className = null;
		hasBlank = "false";
		blankMsg = null;
		blankValue = "0";
		onChange = null;
		size = null;
		hasMultiple = "false";
		selectedName = null;
		selectedProperty = null;
		sqlWhere = null;
		isDebug = "false";
		scopeType = "4";
		scope_type = 4;
		propName = null;
		id_field_name = "bas_id";
	}

	public String getNew_flag() {
		return new_flag;
	}

	public void setNew_flag(String new_flag) {
		this.new_flag = new_flag;
	}

	protected String createWhereSQL() {
		String result = null;
		if (new_flag == null || new_flag.equalsIgnoreCase("false"))
			result = createWhereSQL(true); // 使用原方法产生where子句
		else
			result = createNewWhereSQL();// 使用新方法产生where子句
		return result;
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
		int p_index = -1;
		if (bodySqlWhere != null && bodySqlWhere.length() > 0) {
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
				t_sql = t_sql.substring(t_index2 + 1); // 获取剩下的字符串

				p_index = p_str.indexOf("::");
				// 需要获取参数的子句的前半部分字串,如bas_id==::bas_id,则前半部分为bas_id==，后半部分为bas_id
				String p_str_b = p_str.substring(0, p_index - 1);
				// 需要获取参数的子句的后半部分字串
				String p_str_a = p_str.substring(p_index + 2);
				paramPre.add(p_str_b);
				paramNex.add(p_str_a);
				t_index = t_sql.indexOf("[");
			} // end while
			int pp_size = paramPre.size();
			for (int i = 0; i < pp_size; i++) {
				// String p_str_b = (String)paramPre.get(i);
				// String p_str_a = (String)paramNex.get(i);
				String str1 = null;
				String str2 = null;

				str1 = ((String) paramPre.get(i));
				if (str1 != null)
					str1 = str1.trim();

				if (paramNex.size() > i)
					str2 = ((String) paramNex.get(i));
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

					value = pageContext.getRequest().getParameter(str2.trim());
					// 进行字符转换
					// value = net.strong.util.MultiLanguage.getUnicode(value,
					// (HttpServletRequest) pageContext.getRequest());

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
										str2.trim(), null);
								// System.out.println("obj_value at
								// dbListArray:"+String.valueOf(obj_value));
								if (obj_value != null)
									value = String.valueOf(obj_value);
								/*
								 * System.out.println("test at dbListArray --
								 * value:" + value + ",propName:" + propName +
								 * ",str2.trim():" + str2.trim());
								 */
							} catch (Exception e) {
								value = null;
							}
						}
					}

				}

				if (value == null || value.equalsIgnoreCase("null")
						|| value.trim().length() <= 0) {
					if ("true".equals( isSubSQL )) {
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
					// save to pageContext for input text can get value from it
					pageContext.setAttribute(str2.trim(), value.trim());

					if ("true".equals( isSubSQL )) {
						temp_sql = temp_sql.replaceFirst(str2, value.trim());
					} else {
						String t_result = null;
						value = value.trim();
						if ("%".equals(b_str)) // 加上%,主要用于like查询
							value = "%" + value;
						if ("%".equals(e_str))
							value = value + "%";
						int str1_len = 0;
						if ((str1_len = str1.indexOf("==")) > 0) // 当参数用"=="表示时，说明是进行为数字的比较，不用加双引号
							t_result = str1.substring(0, str1_len + 1) + value;
						else
							t_result = str1 + " '" + value + "' ";
						paramTotal.add(t_result);
						t_result = null;
					}
				}

			}
			if ("true".equals( isSubSQL )) {
				if (temp_sql != null)
					resultBuf.append(temp_sql);
			} else {
				int pt_size = paramTotal.size();
				for (int j = 0; j < pt_size; j++) {
					String t_result = (String) paramTotal.get(j);
					if (t_result == null || t_result.trim().length() < 1)
						continue;
					resultBuf.append(t_result);
					// resultBuf.append(" ");
					// if (j < paramTotal.size() - 1)
					// resultBuf.append(" and ");
				}
			}
		} else {
			if (t_sql != null)
				resultBuf.append(t_sql);
		}
		if (resultBuf == null)
			result = null;
		else
			result = resultBuf.toString();

		paramPre.clear();
		paramNex.clear();
		paramTotal.clear();
		paramPre = null;
		paramNex = null;
		paramTotal = null;
		resultBuf = null;
		//System.out.println(result);
		return result;
	}

	protected String createWhereSQL(boolean bdb_sql_where) {
		// System.out.println("start createWhereSql at dbListArrayTag");
		String result = null;
		String temp_sql = null;
		int t_index = -1;

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

			while (t_index > 0) {
				t_index = t_sql.indexOf("::");
				String sub_str1 = null;
				if (t_index > 0)
					sub_str1 = t_sql.substring(0, t_index);
				int r_index = -1;
				if (sub_str1 != null)
					r_index = sub_str1.lastIndexOf("and");// .indexOf("and");//sub_str1.indexOf("and");
				if (r_index > 0) {
					paramTotal.add(sub_str1.substring(0, r_index));
					sub_str1 = sub_str1.substring(r_index + 3);
				}
				paramPre.add(sub_str1);

				String sub_str2 = t_sql.substring(t_index + 2);
				int tt_index = sub_str2.indexOf("and");// sub_str2.indexOf("and");

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

				str1 = ((String) paramPre.get(i));
				if (str1 != null)
					str1 = str1.trim();

				if (paramNex.size() > i)
					str2 = ((String) paramNex.get(i));
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

					value = pageContext.getRequest().getParameter(str2.trim());
					// 进行字符转换
					// value = net.strong.util.MultiLanguage.getUnicode(value,
					// (HttpServletRequest) pageContext.getRequest());

					if (value == null || value.equalsIgnoreCase("null"))
						value = (String) pageContext.getRequest().getAttribute(
								str2.trim());

					if (value == null || value.equalsIgnoreCase("null"))
						value = (String) pageContext.getAttribute(str2.trim());

					if (value == null || value.equalsIgnoreCase("null")
							|| value.trim().length() <= 0) {
						// 当参数propName不为空时，通过RequestUtils进行取值
						/*
						 * System.out.println("test at dbListArray 0 --
						 * propName:" + propName + ",str2.trim():" +
						 * str2.trim());
						 */
						if (propName != null && propName.length() > 1) {
							try {
								Object obj_value = TagUtils.getInstance()
								.lookup(pageContext, propName,
										str2.trim(), null);
								// System.out.println("obj_value at
								// dbListArray:"+String.valueOf(obj_value));
								if (obj_value != null)
									value = String.valueOf(obj_value);
								/*
								 * value = (String)
								 * RequestUtils.lookup(pageContext, propName,
								 * str2.trim(), null);
								 * 
								 * System.out.println("test at dbListArray --
								 * value:" + value + ",propName:" + propName +
								 * ",str2.trim():" + str2.trim());
								 */
							} catch (Exception e) {
								System.out
								.println("get value by RequestUtils exceptione:"
										+ e.getMessage());
								value = null;
							}
						}
					}

				}

				if (value == null || value.equalsIgnoreCase("null")
						|| value.trim().length() <= 0) {
					if ("true".equals( isSubSQL )) {
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
					// save to pageContext for input text can get value from it
					pageContext.setAttribute(str2.trim(), value.trim());

					if ("true".equals( isSubSQL )) {
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
						if ((str1_len = str1.indexOf("==")) > 0) // 当参数用"=="表示时，说明是进行为数字的比较，不用加双引号
							t_result = str1.substring(0, str1_len + 1) + value;
						else
							t_result = str1 + " '" + value + "' ";
						paramTotal.add(t_result);
					}
				}
			}
			if ("true".equals( isSubSQL )) {
				if (temp_sql != null)
					resultBuf.append(temp_sql);
			} else {
				int pt_size = paramTotal.size();
				for (int j = 0; j < pt_size; j++) {
					resultBuf.append((String) paramTotal.get(j));
					// resultBuf.append(" ");
					if (j < paramTotal.size() - 1)
						resultBuf.append(" and ");
				}
			}
		} else {
			if (t_sql != null)
				resultBuf.append(t_sql);
		}
		if (resultBuf == null)
			result = null;
		else
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

		return temp_orderby;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
