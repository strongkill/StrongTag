package net.strong.taglib.db;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.WeakHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import net.strong.User;
import net.strong.cookie.OpCookies;
import net.strong.exutil.htmlFilter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;

public class dbWriteTag extends dbTag {

	/**
	 *
	 */
	private static final long serialVersionUID = 2818429201656006803L;

	/**
	 * The key to search default format string for java.sql.Timestamp in
	 * resources.
	 */
	public static final String SQL_TIMESTAMP_FORMAT_KEY = "org.apache.struts.taglib.bean.format.sql.timestamp";

	/**
	 * The key to search default format string for java.sql.Date in resources.
	 */
	public static final String SQL_DATE_FORMAT_KEY = "org.apache.struts.taglib.bean.format.sql.date";

	/**
	 * The key to search default format string for java.sql.Time in resources.
	 */
	public static final String SQL_TIME_FORMAT_KEY = "org.apache.struts.taglib.bean.format.sql.time";

	/**
	 * The key to search default format string for java.util.Date in resources.
	 */
	public static final String DATE_FORMAT_KEY = "org.apache.struts.taglib.bean.format.date";

	/**
	 * The key to search default format string for int (byte, short, etc.) in
	 * resources.
	 */
	public static final String INT_FORMAT_KEY = "org.apache.struts.taglib.bean.format.int";

	/**
	 * The key to search default format string for float (double, BigDecimal) in
	 * resources.
	 */
	public static final String FLOAT_FORMAT_KEY = "org.apache.struts.taglib.bean.format.float";

	/**
	 * The message resources for this package.
	 */
	protected static MessageResources messages = MessageResources
	.getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

	/**
	 * the rendered output for characters that are sensitive in HTML?
	 */
	protected boolean filter = true;

	public boolean getFilter() {
		return (this.filter);
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	/**
	 * Should we ignore missing beans and simply output nothing?
	 */
	protected boolean ignore = false;

	public boolean getIgnore() {
		return (this.ignore);
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	protected String regexp = null;

	public String getRegexp(){
		return this.regexp;
	}
	public void setRegexp(String regexp){
		this.regexp = regexp;
	}

	protected String defaultvalue = null;

	public String getDefaultvalue(){
		return this.defaultvalue;
	}
	public void setDefaultvalue(String defaultvalue){
		this.defaultvalue = defaultvalue;
	}
	/**
	 * Name of the bean that contains the data we will be rendering.
	 */
	protected String name = null;

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Name of the property to be accessed on the specified bean.
	 */
	protected String property = null;

	public String getProperty() {
		return (this.property);
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * The scope to be searched to retrieve the specified bean.
	 */
	protected String scope = null;

	public String getScope() {
		return (this.scope);
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * The format string to be used as format to convert value to String.
	 */
	protected String formatStr = null;

	public String getFormat() {
		return (this.formatStr);
	}

	public void setFormat(String formatStr) {
		this.formatStr = formatStr;
	}

	/**
	 * The key to search format string in applciation resources
	 */
	protected String formatKey = null;

	public String getFormatKey() {
		return (this.formatKey);
	}

	public void setFormatKey(String formatKey) {
		this.formatKey = formatKey;
	}

	/**
	 * The session scope key under which our Locale is stored.
	 */
	protected String localeKey = null;

	public String getLocale() {
		return (this.localeKey);
	}

	public void setLocale(String localeKey) {
		this.localeKey = localeKey;
	}

	/**
	 * The servlet context attribute key for our resources.
	 */
	protected String bundle = null;

	private String highlightKey;

	// 需打印出字符是最大长度，如果设置了此值，则当长度超过此值时，切断字符。
	private String maxLength = null;// "20";

	public String getBundle() {
		return (this.bundle);
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Process the start tag.
	 *
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
		String func = null;
		if(property==null){
			if(name.indexOf("::")>-1){ //struts 2.0的输入方式
				String[] tmp = name.split("::");
				name = tmp[0];
				property=tmp[1];
				if(tmp.length>2)
					func = tmp[2];
			}else if(name.indexOf(".")>-1){//ognl写法。
				String[] tmp = name.split("\\.");
				name = tmp[0];
				property=tmp[1];
				if(tmp.length>2)
					func = tmp[2];
			}
		}
		Object bean = (Object) lookup(pageContext, name, null);
		if (bean == null) {
			if(defaultvalue!=null)
				TagUtils.getInstance().write(pageContext, defaultvalue);
			return (SKIP_BODY);
		}
		Object value = null;
		if (property != null) {
			if (bean instanceof ResultSet) {
				if (property.indexOf("index") >= 0) {
					if ("index".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_index");
					} else if ("index_1".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_index_1");
					} else if ("t_index".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "_t_index");
					} else if ("t_index_1".equalsIgnoreCase(property)) {
						value = pageContext.getAttribute(name + "t_index_1");
					} else {
						value = getValue(bean);
					}
				} else {
					value = getValue(bean);
				}
			} else if (bean instanceof HashMap<?,?>) {
				HashMap<?,?> hm = (HashMap<?,?>) bean;
				value = hm.get(property);
				hm = null;
			} else if (bean instanceof WeakHashMap<?,?>) {
				WeakHashMap<?,?> hm = (WeakHashMap<?,?>) bean;
				value = hm.get(property);
				hm = null;
			}else if(bean instanceof SimpleHash){
				SimpleHash hm = (SimpleHash)bean;
				try {
					value = hm.get(property);
				} catch (TemplateModelException e) {
					e.printStackTrace();
				}
				hm = null;
			}else if(bean instanceof ServletRequest){
				value = pageContext.getRequest().getAttribute(property);
				if(value==null)
					value = pageContext.getRequest().getParameter(property);
			}else if(bean instanceof HttpSession){
				value = pageContext.getSession().getAttribute(property);
			}else if(bean instanceof OpCookies){
				value = OpCookies.getCookies(property, (HttpServletRequest) pageContext.getRequest());
			} else {
				try {
					value = PropertyUtils.getProperty(bean, property);
				} catch (Exception t) {
					throw new JspException("error at dbWrite :"
							+ t.getMessage());
				}

			}

		}
		bean = null;
		if (value == null) {
			if(defaultvalue!=null)
				TagUtils.getInstance().write(pageContext, defaultvalue);
			return (SKIP_BODY);
		}

		String output = formatValue(value);
		value = null;
		if (filter) {
			output = net.strong.exutil.htmlFilter.filterNewLine(output); // 过滤掉换行符
		}
		int start_point =0;
		int max_length = 0;
		String more = "...";
		if(regexp!=null && regexp.indexOf("substr")>-1){
			int s_pos = regexp.indexOf("(");
			int e_pos = regexp.indexOf(")");
			String tmp = regexp.substring(s_pos+1, e_pos);
			start_point = Integer.parseInt(tmp.split(",")[0]);
			maxLength = tmp.split(",")[1];
			more="";
		}

		if (maxLength != null) {
			String temp_out = output;
			try {
				max_length = Integer.valueOf(maxLength).intValue();

				if (max_length > 0) {
					// 在切割之前，先必须过滤掉HTML代码
					output = htmlFilter.SubString(output,start_point,max_length,more);
				}
			} catch (Exception e) {
				max_length = 0;
				output = temp_out; // 恢复原来的值
			}
			temp_out = null;
		}

		String is_nokey = (String) pageContext.getAttribute("nokey"); // 是否topNum不为空，不为空则不需要进行加亮显示
		if (!"true".equals(is_nokey)) {
			if (highlightKey != null) {
				if (!"nokey".equals(highlightKey)) { // 当highlightKey为nokey时，不需要进行highlight
					output = highlightKey(highlightKey, output);
				}
			} else
				output = highlightKey("keyword", output);
		}
		is_nokey = null;

		if ("null".equalsIgnoreCase(output)){
			output = "";
			if(defaultvalue!=null)
				output = defaultvalue;
		}
		if(regexp!=null && regexp.indexOf("replace")>-1){
			int s_pos = regexp.indexOf("(");
			int e_pos = regexp.indexOf(")");
			String tmp = regexp.substring(s_pos+1, e_pos);
			String tmps[] = tmp.split(",");
			if(tmps.length==1)
				output=output.replaceAll(tmps[0], "");
			else
				output=output.replaceAll(tmps[0], tmps[1]);
		}
		if(bussinessClass!=null && bussinessClass.length()>0){
			String tmpoutput = (String)BussinessProcess(bussinessClass, output);
			if(tmpoutput!=null && tmpoutput.length()>0){
				output = tmpoutput;
			}
		}
		if(func!=null){
			try {
				output = (String) MethodUtils.invokeMethod(output, func, null);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		TagUtils.getInstance().write(pageContext, output);
		output = null;

		return (SKIP_BODY);

	}

	protected String trimString(String value) {
		if (value != null) {
			value = value.trim();
		}
		return value;
	}

	/**
	 * @deprecated
	 * @param value
	 * @return
	 * @throws JspException
	 *
	 */

	protected String doWithValue(Object value) throws JspException {
		String output = formatValue(value);
		value = null;
		if (filter) {
			output = net.strong.exutil.htmlFilter.filterNewLine(output); // 过滤掉换行符
		}

		int max_length = 0;
		if (maxLength != null) {
			String temp_out = output;
			try {
				max_length = Integer.valueOf(maxLength).intValue();
				if (max_length > 0) {
					// 在切割之前，先必须过滤掉HTML代码
					if (filter)
						output =htmlFilter.SubString(output, max_length);
					else {
						if (output != null && output.length() > max_length) {
							output = output.substring(0, max_length) + "...";
						}
					}
				}
			} catch (Exception e) {
				max_length = 0;
				output = temp_out; // 恢复原来的值
				output = null;
			}
		}

		String is_nokey = (String) pageContext.getAttribute("nokey"); // 是否highlight不为空，不为空则不需要进行加亮显示
		if (!"true".equals(is_nokey)) {
			if (highlightKey != null) {
				if (!"nokey".equals(highlightKey)) { // 当highlightKey为nokey时，不需要进行highlight
					output = highlightKey(highlightKey, output);
				}
			} else
				output = highlightKey("keyword", output);
		}

		if ("null".equalsIgnoreCase(output))
			output = "";
		return output;
	}

	// --------------------------------------------------------- Public Methods
	protected Object getValue(Object bean, String prop) throws JspException {
		Object value = null;
		if (prop != null) {
			if (bean instanceof ResultSet) {
				if (prop.indexOf("index") >= 0) {
					if ("index".equalsIgnoreCase(prop)) {
						value = pageContext.getAttribute(name + "_index");
					} else if ("index_1".equalsIgnoreCase(prop)) {
						value = pageContext.getAttribute(name + "_index_1");
					} else if ("t_index".equalsIgnoreCase(prop)) {
						value = pageContext.getAttribute(name + "_t_index");
					} else {
						value = getRSValue(bean);
					}
				} else {
					value = getRSValue(bean);
				}
			} else if (bean instanceof HashMap<?,?>) {
				HashMap<?,?> hm = (HashMap<?,?>) bean;
				value = hm.get(prop);
				hm = null;
			} else if (bean instanceof WeakHashMap<?,?>) {
				WeakHashMap<?,?> hm = (WeakHashMap<?,?>) bean;
				value = hm.get(prop);
				hm = null;
			} else {
				try {
					value = PropertyUtils.getProperty(bean, prop);
				} catch (Exception t) {
					throw new JspException("error at dbWrite :"
							+ t.getMessage());
				}

			}

		}
		bean = null;
		prop = null;
		return value;
	}

	protected Object getRSValue(Object bean) throws JspException {
		Object value = null;
		try {
			ResultSet rs = (ResultSet) bean;
			value = rs.getObject(property);
			rs = null;
		} catch (SQLException e) {
			throw new JspException("error at dbWriteTag SQLException: "
					+ e.getMessage());
		} catch (Exception e) {
			throw new JspException("error at dbWriteTag Exception:"
					+ e.getMessage());
		}
		bean = null;
		return value;
	}

	protected Object getValue(Object bean) throws JspException {
		Object value = null;
		try {
			ResultSet rs = (ResultSet) bean;
			value = rs.getObject(property);
			rs.close();

			rs = null;
		} catch (SQLException e) {
			throw new JspException("error at dbWriteTag SQLException: "
					+ e.getMessage());
		} catch (Exception e) {
			throw new JspException("error at dbWriteTag Exception:"
					+ e.getMessage());
		}
		bean = null;
		return value;
	}

	// 从需打印的内空容中判断是否还有指定需加亮显示的字符，如存在，则设置加亮。
	protected String highlightKey(String key, String content)
	throws JspException {
		String key_value = pageContext.getRequest().getParameter(key);
		if (key_value == null)
			key_value = (String) pageContext.getRequest().getAttribute(key);
		if (key_value == null || "null".equalsIgnoreCase(key_value)
				|| key_value.length() < 1) {
			return content;
		}

		String[] key_t = null;
		if (key_value != null) {
			key_t = key_value.split(" ");
		}
		for (int i = 0; i < key_t.length; i++) {
			String str_key = key_t[i];
			if (str_key != null && str_key.trim().length() > 1)
				str_key = str_key.trim();
			else
				continue;
			String high_value = "<font color=\"cc0033\" >" + str_key
			+ "</font>";
			content = content.replaceAll(str_key, high_value);
		}
		key_value = null;
		return content;
	}

	/**
	 * Retrieve format string from message bundle and return null if message not
	 * found or message string.
	 *
	 * @param formatKey
	 *            value to use as key to search message in bundle
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	protected String retrieveFormatString(String formatKey) throws JspException {
		String result = null;
		try{
			result = TagUtils.getInstance().message(pageContext,this.bundle, this.localeKey, formatKey);
		}catch(Exception e){
			//
		}
		if ((result != null)
				&& !(result.startsWith("???") && result.endsWith("???")))
			return result;
		else
			return null;
	}

	/**
	 * Format value according to specified format string (as tag attribute or as
	 * string from message resources) or to current user locale.
	 *
	 * @param valueToFormat
	 *            value to process and convert to String
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	protected String formatValue(Object value) throws JspException {
		Format format = null;
		Locale locale = TagUtils.getInstance().getUserLocale(pageContext,
				this.localeKey);
		// RequestUtils.retrieveUserLocale( pageContext, this.localeKey );
		boolean formatStrFromResources = false;
		String formatString = formatStr;
		// Return String object as is.
		if (value instanceof java.lang.String) {
			return (String) value;
			/*}else if(value instanceof oracle.sql.CLOB){
			try {
				long t = System.currentTimeMillis();
				CLOB tm= (CLOB)value;
				String tmp =tm.getSubString(1,(int)tm.length());
				System.out.println("neet time : " + (System.currentTimeMillis()-t));
				return tmp;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			 */
		} else {
			// Try to retrieve format string from resources by the key from
			// formatKey.
			if ((formatString == null) && (formatKey != null)) {
				formatString = retrieveFormatString(this.formatKey);
				if (formatString != null)
					formatStrFromResources = true;
			}
			// Prepare format object for numeric values.
			if (value instanceof Number) {
				if (formatString == null) {
					if ((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer) || (value instanceof Long) || (value instanceof BigInteger))
						formatString = retrieveFormatString(INT_FORMAT_KEY);
					else if ((value instanceof Float) || (value instanceof Double) || (value instanceof BigDecimal))
						formatString = retrieveFormatString(FLOAT_FORMAT_KEY);
					if (formatString != null)
						formatStrFromResources = true;
				}
				if (formatString != null) {
					try {
						format = NumberFormat.getNumberInstance(locale);
						if (formatStrFromResources)
							((DecimalFormat) format)
							.applyLocalizedPattern(formatString);
						else
							((DecimalFormat) format).applyPattern(formatString);
					} catch (IllegalArgumentException _e) {
						JspException e = new JspException(messages.getMessage(
								"write.format", formatString));
						TagUtils.getInstance().saveException(pageContext, e);
						throw e;
					}
				}
			}else if( value instanceof oracle.sql.TIMESTAMP ){

				try {
					value = ((oracle.sql.TIMESTAMP)value).timestampValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (formatString != null) {
					if (formatStrFromResources) {
						format = new SimpleDateFormat(formatString, locale);
					} else {
						format = new SimpleDateFormat(formatString);
					}
				}

			}else if( value instanceof oracle.sql.DATE){
				try {
					value = ((oracle.sql.DATE)value).dateValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (formatString != null) {
					if (formatStrFromResources) {
						format = new SimpleDateFormat(formatString, locale);
					} else {
						format = new SimpleDateFormat(formatString);
					}
				}
			} else if (value instanceof java.util.Date) {
				if (formatString == null) {
					if (value instanceof java.sql.Timestamp) {
						formatString = retrieveFormatString(SQL_TIMESTAMP_FORMAT_KEY);
					} else if (value instanceof java.sql.Date) {
						formatString = retrieveFormatString(SQL_DATE_FORMAT_KEY);
					} else if (value instanceof java.sql.Time) {
						formatString = retrieveFormatString(SQL_TIME_FORMAT_KEY);
					} else if (value instanceof java.util.Date) {
						formatString = retrieveFormatString(DATE_FORMAT_KEY);
					}
					if (formatString != null)
						formatStrFromResources = true;

				}
				if (formatString != null) {
					if (formatStrFromResources) {
						format = new SimpleDateFormat(formatString, locale);
					} else {
						format = new SimpleDateFormat(formatString);
					}
				}

			}
		}

		if (format != null)
			return format.format(value);
		else
			return value.toString();

	}

	/**
	 * Release all allocated resources.
	 */
	public void release() {

		super.release();
		filter = true;
		ignore = false;
		name = null;
		property = null;
		scope = null;
		formatStr = null;
		formatKey = null;
		localeKey = null;
		bundle = null;

	}

	protected Object lookup(PageContext pageContext, String name, String scope)
	throws JspTagException {
		if("request".equalsIgnoreCase(name))
			return pageContext.getRequest();
		if("session".equalsIgnoreCase(name))
			return pageContext.getSession();
		if("cookie".equalsIgnoreCase(name))
			return new OpCookies();
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
		} else if ("page".equalsIgnoreCase(scope))
			bean = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
		else if ("request".equalsIgnoreCase(scope))
			bean = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
		else if ("session".equalsIgnoreCase(scope))
			bean = pageContext.getAttribute(name, PageContext.SESSION_SCOPE);
		else if ("application".equalsIgnoreCase(scope))
			bean = pageContext
			.getAttribute(name, PageContext.APPLICATION_SCOPE);
		else {
			JspTagException e = new JspTagException("Invalid scope " + scope);
			throw e;
		}
		name = null;
		scope = null;

		return (bean);

	}

	public String getHighlightKey() {
		return highlightKey;
	}

	public void setHighlightKey(String highlightKey) {
		this.highlightKey = highlightKey;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public  static void main(String[] args){
		String name ="row::name::length";//"row::name::length";
		String property = "";
		String func = null;
			if(name.indexOf("::")>-1){ //struts 2.0的输入方式
				String[] tmp = name.split("::");
				name = tmp[0];
				property=tmp[1];
				if(tmp.length>2)
					func = tmp[2];
			}else if(name.indexOf(".")>-1){//ognl写法。
				String[] tmp = name.split("\\.");
				name = tmp[0];
				property=tmp[1];
				if(tmp.length>2)
					func = tmp[2];
			}
			User user = new User();
			user.setBas_no("strong");
		 try {
			 System.out.println("invoke : "+MethodUtils.invokeMethod("String", "length", null));
			 System.out.println("Name : "+PropertyUtils.getProperty(user, "bas_no"));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Name : " + name);
		System.out.println("Property : " + property);
		System.out.println("Property : " + func);
	}

}