package net.strong.taglib.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
/**
 *
 * <p>Title: </p>
 * <p>Description: 用于释放dbSpecifyValueTag中的数据库连接</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class dbSpReleaseTag  extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name=null;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int doEndTag() throws JspException {
		pageContext.removeAttribute(name);
		return EVAL_PAGE;
	}
}