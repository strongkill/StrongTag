package net.strong.taglib.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.jsp.JspException;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModelException;

public class dbEmptyTag extends dbConditionalTagBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Evaluate the condition that is being tested by this particular tag,
	 * and return <code>true</code> if the nested body content of this tag
	 * should be evaluated, or <code>false</code> if it should be skipped.
	 * This method must be implemented by concrete subclasses.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	protected boolean condition() throws JspException {

		return (condition(true));

	}

	/**
	 * Evaluate the condition that is being tested by this particular tag,
	 * and return <code>true</code> if the nested body content of this tag
	 * should be evaluated, or <code>false</code> if it should be skipped.
	 * This method must be implemented by concrete subclasses.
	 *
	 * @param desired Desired outcome for a true result
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	protected boolean condition(boolean desired) throws JspException {

		// Evaluate the presence of the specified value
		boolean empty = true;
		if (name != null) {
			Object bean = (Object) pageContext.getAttribute(name);
			String value = null;
			if (bean instanceof ResultSet) {
				ResultSet rs = (ResultSet) bean;
				if (property != null) {
					try {
						value = rs.getString(property);
					} catch (Exception e) {
						value = null;
					}
					if (value != null) {
						empty = (value.length() < 1);
					}
				} else {
					try {
						empty = rs.next();
					} catch (Exception e) {
						empty = true;
					}
				}
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				rs = null;
				
			}else if (bean instanceof ArrayList) {
				ArrayList al = (ArrayList) bean;
				empty = al.isEmpty();
				al = null;
			}else if (bean instanceof HashMap) {
				HashMap hm = (HashMap) bean;
				if (property != null) {
					if (hm != null) {
						value = String.valueOf(hm.get(property));
					}
					if (value != null) {
						empty = (value.trim().length() < 1);
						if (!empty) {
							if ("null".equalsIgnoreCase(value.trim()))
								empty = true;
						}
					}
				} else {
					empty = hm.isEmpty();
				}
				hm = null;
			}else if (bean instanceof WeakHashMap) {
				WeakHashMap hm = (WeakHashMap) bean;
				if (property != null) {
					if (hm != null) {
						value = String.valueOf(hm.get(property));
					}
					if (value != null) {
						empty = (value.trim().length() < 1);
						if (!empty) {
							if ("null".equalsIgnoreCase(value.trim()))
								empty = true;
						}
					}
				} else {
					empty = hm.isEmpty();
				}
				hm = null;
			}else if(bean instanceof SimpleSequence){
				try {
					List al = ((SimpleSequence)bean).toList();
					empty = al.isEmpty();
					al = null;
				} catch (TemplateModelException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
			}else if(bean instanceof SimpleHash){
				try {
					Map hm = ((SimpleHash)bean).toMap();
					empty = hm.isEmpty();
					hm = null;
				} catch (TemplateModelException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
			} else {
				if (bean == null)
					empty = true;
			}
			bean = null;
		}
		return (empty == desired);
	}

}