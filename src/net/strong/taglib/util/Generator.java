package net.strong.taglib.util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;

import net.strong.taglib.db.dbTag;

/**
 * <stUtil:generator value="{aaa,bbb,ccc,ddd,eee}{aaa,bbb,ccc,ddd,eee}{aaa,bbb,ccc,ddd,eee}" name="row">
 * @author Strong Yuan
 * @Date 2010-3-24
 * @Version
 */
public class Generator extends dbTag {

	/**
	 *
	 */
	private static final long serialVersionUID = -2162911680318508907L;

	protected String value=null;
	protected String count = null;
	protected String separator = null;
	protected String name = null;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int doEndTag() throws JspException {
		if(value!=null && value.length()>0){
			/*if(value.indexOf("::")>-1){
				String[] tmp = value.split("::");
				value = (String)getValue(tmp[0],tmp[1]);
			}*/
			ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
			while(value.indexOf("{")>-1){
				int spos = value.indexOf("{");
				int epos = value.indexOf("}");
				String tmp = value.substring(spos+1, epos);
				String[] values = tmp.split(separator);
				HashMap<String,String> m = new HashMap<String,String>();
				for(int i=0;i<values.length;i++){
					m.put("value" + i, values[i]);
				}
				al.add(m);
				value = value.substring(epos+1, value.length());
			}
			pageContext.setAttribute(name, al);
		}
		return EVAL_PAGE;
	}

	public static void main(String[] args){

	}

/*
	protected Object getValue(String object ,String property) throws JspException {
		return invokObjectValue(lookup(pageContext, object, null), property);
	}

	protected Object invokObjectValue(Object bean,String property) throws JspException {
		if(bean == null)
			return null;
		Object value = null;
		if (property != null) {
			if (bean instanceof ResultSet) {
				value = getResultValue(bean,property);
			} else if (bean instanceof Map<?,?>) {
				Map<?,?> hm = (Map<?,?>) bean;
				if("empty".equalsIgnoreCase(property)){
					value = hm.isEmpty();
				}else if("size".equalsIgnoreCase(property)){
					value = hm.size();
				}else{
					value = hm.get(property);
				}
				hm = null;
			}else if(bean instanceof SimpleHash){
				SimpleHash hm = (SimpleHash)bean;
				try {
					value = hm.get(property);
				} catch (TemplateModelException e) {
					e.printStackTrace();
				}
				hm = null;
			} else if(bean instanceof HttpServletRequest){
				value = ((HttpServletRequest) bean).getParameter(property);
			} else if(bean instanceof HttpSession){
				value = ((HttpSession)bean).getAttribute(property);
			} else if(bean instanceof OpCookies){
				value = OpCookies.getCookies(property, getRequest());
			} else if(bean instanceof List<?>){
				Class<?> c = bean.getClass();
				try{
					Method m = c.getDeclaredMethod(property);
					value = m.invoke(bean);
				}catch(Exception e){
					e.printStackTrace();
				}
			} else {
				try {
					value = PropertyUtils.getProperty(bean, property);
				} catch (Exception t) {
					throw new JspException("error at TagIf :"
							+ t.getMessage());
				}

			}

		}
		return value;
	}

	protected Object getResultValue(Object bean,String property) throws JspException {
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


	protected Object lookup(PageContext pageContext, String name, String scope) throws JspTagException {
		Object bean = null;
		if("cookie".equalsIgnoreCase(name)){
			bean = new OpCookies();
		}else if("request".equalsIgnoreCase(name)){
			bean =  getRequest();
		}else if("session".equalsIgnoreCase(name)){
			bean =  getRequest().getSession();
		}else{
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
		}
		name = null;
		scope = null;
		return (bean);

	}
	*/
}
