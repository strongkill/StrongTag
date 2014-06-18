package net.strong.taglib.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
/**
 *
 * <p>Title: </p>
 * <p>Description: 由于dbListArray类已改为不使用HashMap进行暂存数据，所以原dbRelease
 * 所起的作用已没有用处，现改用dbSpReleaseTag来释放dbSpecifyValueTag中的数据库连接</p>
 *
 * 本标签已没有用，资源释放直接在其它标签中实现
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 * @deprecated
 */

public class dbReleaseTag extends TagSupport {

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
	public int doEndTag() throws JspException {
		pageContext.removeAttribute(name);
		return EVAL_PAGE;
	}
}