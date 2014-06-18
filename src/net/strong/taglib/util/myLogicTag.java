package net.strong.taglib.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * <p>Title:求余是否为０ </p>
 * <p>Description: 此标签类似Logic的present标签，对某一对象的某一属性的
 * 值进行某一数字的求余，如为０，则输入出body中的数据，否则乎略。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author jonyzhang
 * @version 1.0
 * 添加 isDivNotEqual --  当此参数为true时，表示被除的值与设置的值不相等时，输出body的值 2006-07-08
 */

public class myLogicTag extends BodyTagSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String bodyStr = null;
	private String name = "row";
	private String property = "INDEX";
	private String divValue = "4";
	private String scope = null;
	private String divResult = "0";
	//判断根据name,property所取得的值是否与此参数指定的值相等，如相等，则打印出内容
	private String isValueEqual = null;

	private int div_value = 4;
	private int div_result = 0;
	private String isDivNotEqual;

	public void release() {
		name = "row";
		property = "INDEX";
		divValue = "4";
		divResult = "0";
		isValueEqual = null;
	}

	public void setDivResult(String divResult) {
		this.divResult = divResult;
		if (divResult != null)
			div_result = Integer.valueOf(divResult).intValue();
	}

	public String getDivResult() {
		return this.divResult;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getProperty() {
		return this.property;
	}

	public void setDivValue(String divValue) {
		this.divValue = divValue;
		this.div_value = Integer.valueOf(divValue).intValue();
	}

	public String getDivValue() {
		return this.divValue;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return this.scope;
	}

	public void setIsValueEqual(String isValueEqual) {
		this.isValueEqual = isValueEqual;
	}

	public String getIsValueEqual() {
		return this.isValueEqual;
	}

	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return (EVAL_BODY_BUFFERED);
	}

	public int doAfterBody() throws JspException {

		if (bodyContent != null) {
			bodyStr = bodyContent.getString();
			if (bodyStr != null) {
				bodyStr = bodyStr.trim();
			}
			if (bodyStr.length() < 1) {
				bodyStr = null;
			}
		}
		return (SKIP_BODY);

	}

	public int doEndTag() throws JspException {
		//HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		boolean show = false;
		Object value = null;
		Object hm_row = lookup(pageContext, name, null);//(Object)pageContext.getAttribute(name);
		//		if(hm_row == null)
		//		hm_row = (Object)pageContext.getServletContext().getAttribute(name);

		if (hm_row != null) {
			if (property != null) {
				if (hm_row instanceof ResultSet) {
					if ("index".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_index");
					} else if ("index_1".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_index_1");
					} else if ("t_index".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_t_index");
					} else if ("t_index_1".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_t_index_1");
					} else {
						try {
							value = ((ResultSet) hm_row)
									.getString(this.property.trim()
											.toLowerCase());
						} catch (Exception e) {
							value = null;
						}
					}
				}
				if (hm_row instanceof HashMap<?,?>) {
					value = ((HashMap<?,?>) hm_row).get(this.property.trim());
				}

			} else {
				value = String.valueOf(hm_row);
			}

			if (hm_row instanceof String) {
				value = (String) hm_row;
			}
		}

		show = isOk(value);
		if (show) {
			JspWriter writer = pageContext.getOut();
			try {
				if(bodyStr!=null && bodyStr.length()>0)
					writer.print(bodyStr);

				writer.close();
				writer = null;
			} catch (IOException e) {
				throw new JspException(e.getMessage());
			}

		}
		return EVAL_PAGE;
	}

	private boolean isOk(Object valueToFormat) {
		boolean result = false;
		int temp_result = 0;
		Object value = valueToFormat;
		int nvalue = -1;
		String str_value = null;

		if (value instanceof java.lang.String) {
			str_value = (String) value;
		}
		if (value instanceof Number) {
			str_value = String.valueOf(value);
		}
		if (value instanceof Boolean) {
			boolean bb = Boolean.valueOf(String.valueOf(value)).booleanValue();
			if (bb)
				str_value = "1";
			else
				str_value = "0";
		} else
			str_value = String.valueOf(value);

		//		ProDebug.addDebugLog("str_value:"+str_value);
		//		ProDebug.saveToFile();

		if (isValueEqual != null) {
			if (isValueEqual.equals(str_value))
				return true;
			else
				return false;
		}

		try {
			if (str_value == null || "null".equalsIgnoreCase(str_value))
				str_value = "0";
			nvalue = Integer.valueOf(str_value).intValue();
			temp_result = nvalue % div_value;

			//			ProDebug.addDebugLog("temp_result:"+temp_result+",n_value:"+nvalue+",div_result:"+div_result);
			//			ProDebug.saveToFile();
			if ("true".equals(isDivNotEqual)) {
				if (temp_result != div_result)
					result = true;
				else
					result = false;
			} else {
				if (temp_result == div_result)
					result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	protected Object lookup(PageContext pageContext, String name, String scope)
			throws JspException {

		Object bean = null;
		if (scope == null) {
			bean = pageContext.findAttribute(name);
			if (bean == null)
				bean = pageContext
						.getAttribute(name, PageContext.REQUEST_SCOPE);
			if (bean == null)
				bean = pageContext
						.getAttribute(name, PageContext.SESSION_SCOPE);
			if (bean == null)
				bean = pageContext.getAttribute(name,
						PageContext.APPLICATION_SCOPE);
		} else if (scope.equalsIgnoreCase("page"))
			bean = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
		else if (scope.equalsIgnoreCase("request"))
			bean = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
		else if (scope.equalsIgnoreCase("session"))
			bean = pageContext.getAttribute(name, PageContext.SESSION_SCOPE);
		else if (scope.equalsIgnoreCase("application"))
			bean = pageContext
					.getAttribute(name, PageContext.APPLICATION_SCOPE);
		else {
			JspException e = new JspException("Invalid scope " + scope);
			throw e;
		}
		return (bean);

	}

	public String getIsDivNotEqual() {
		return isDivNotEqual;
	}

	public void setIsDivNotEqual(String isDivNotEqual) {
		this.isDivNotEqual = isDivNotEqual;
	}

}