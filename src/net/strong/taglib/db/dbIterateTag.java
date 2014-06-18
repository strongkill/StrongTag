package net.strong.taglib.db;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;

import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModelException;

public class dbIterateTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Log log = LogFactory.getLog(this.getClass().getName());

	private String id = "row";

	private String name = "rows";

	private ResultSet _rset = null;

	private int index = 0;

	private int index_1 = 1;

	/*
	 public final class IterateTime
	 {
	 long start_time = 0;
	 long end_time = 0;
	 }
	 */
	/**
	 * Iterator of the elements of this collection, while we are actually
	 * running.
	 */
	protected Iterator iterator = null;

	public void removeAttri(PageContext pageContext, String name, String scope)
			throws JspTagException {
		if (scope == null) {
			pageContext.removeAttribute(name);
			pageContext.removeAttribute(name, PageContext.REQUEST_SCOPE);
			pageContext.removeAttribute(name, PageContext.SESSION_SCOPE);
			pageContext.removeAttribute(name, PageContext.APPLICATION_SCOPE);
		} else if (scope.equalsIgnoreCase("page")) {
			pageContext.removeAttribute(name);
		} else if (scope.equalsIgnoreCase("request")) {
			pageContext.removeAttribute(name, PageContext.REQUEST_SCOPE);
		} else if (scope.equalsIgnoreCase("session")) {
			pageContext.removeAttribute(name, PageContext.SESSION_SCOPE);
		} else if (scope.equalsIgnoreCase("application")) {
			pageContext.removeAttribute(name, PageContext.APPLICATION_SCOPE);
		}
	}

	protected Object lookup(PageContext pageContext, String name, String scope)
			throws JspTagException {

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
			JspTagException e = new JspTagException("Invalid scope " + scope);
			throw e;
		}
		return (bean);

	}

	public int doStartTag() throws JspException {
		//System.out.println("StartTag_Iterate");
		if(name.indexOf("::")>-1){//struts 2.0的输入方式
			String[] tmp = name.split("::");
			name = tmp[0];
			id = tmp[1];
		}else if(name.indexOf(".")>-1){//ognl写法。
			String[] tmp = name.split("\\.");
			name = tmp[0];
			id=tmp[1];
		}
		index = 0;
		index_1 = 1;

		super.bodyContent = null;
		int maxPageIndex = 0;
		//int maxRecord = 0;
		int pageIndex = 0;
		Object pageObj = pageContext.getRequest().getParameter("page");//获取当前所在页
		Object maxPageObj = pageContext.getAttribute("maxPage");//获取最大页数
		if (pageObj != null)
			pageIndex = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(pageObj), 0);
		pageObj = null;
		if (maxPageObj != null)
			maxPageIndex = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(maxPageObj), 0);
		maxPageObj = null;

		Object collection = null;
		collection = lookup(pageContext, name, null);
		if (collection == null) {
			log.info("数据集为空，可能是查询出了错误");
			return SKIP_BODY;
		}

		if (collection instanceof ArrayList) {
			iterator = ((ArrayList) collection).iterator();
		} else if (collection instanceof ResultSet) {
			_rset = (ResultSet) collection;

			pageContext.setAttribute(getId(), _rset);

			try {
				if (_rset.next() == false) {
					return SKIP_BODY;
				}
			} catch (SQLException e) {
				throw new JspTagException("same error at dbIterateTag :"
						+ e.toString());
			}

		} else if (collection.getClass().isArray()) {
			try {
				iterator = Arrays.asList((Object[]) collection).iterator();
			} catch (ClassCastException e) {
				// Rats -- it is an array of primitives
				int length = Array.getLength(collection);
				ArrayList<Object> c = new ArrayList<Object>(length);
				for (int i = 0; i < length; i++) {
					c.add(Array.get(collection, i));
				}
				iterator = c.iterator();
			}
		} else if (collection instanceof Collection) {
			iterator = ((Collection) collection).iterator();
		} else if (collection instanceof Iterator) {
			iterator = (Iterator) collection;
		} else if (collection instanceof Enumeration) {
			iterator = IteratorUtils.asIterator((Enumeration) collection);
		} else if (collection instanceof SimpleSequence) {
			try {
				iterator = ((SimpleSequence) collection).toList().iterator();
			} catch (TemplateModelException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		} else {
			JspException e = new JspException(
					"can not get collection at dbIterateTag ");
			TagUtils.getInstance().saveException(pageContext, e);
			throw e;
		}
		collection = null;

		// Store the first value and evaluate, or skip the body if none
		if (_rset == null && iterator != null) {
			if (iterator.hasNext()) {
				Object element = iterator.next();
				if (element == null) {
					pageContext.removeAttribute(id);
				} else {
					pageContext.setAttribute(id, element);
				}
				iterator.remove();//移除列表中当前对象
			} else {
				//iterator = null;
				return (SKIP_BODY);
			}
		}
		//iterator = null;
		if (_rset != null) {
			int t_index = pageIndex * maxPageIndex + index_1;
			setRowCount(id + "_index", index);
			setRowCount(id + "_index_1", index_1);
			setRowCount(id + "_t_index", t_index);
			++index;
			++index_1;
		}
		//_rset = null;

		return EVAL_BODY_AGAIN;//EVAL_BODY_TAG
	}

	public int doEndTag() throws JspTagException {
		
		//System.out.println("EndTag_Iterate");
		pageContext.removeAttribute(id);
		pageContext.removeAttribute(name);
		try {
			if (getBodyContent() != null && getPreviousOut() != null) {
				getPreviousOut().write(getBodyContent().getString());
				bodyContent.clearBody();
				getBodyContent().clearBuffer();
			}
		} catch (IOException e) {
			throw new JspTagException(e.toString());
		}

		if (_rset != null) {
			try {
				_rset.close();
				Statement stmt = (Statement) pageContext.getAttribute(name
						+ "_stmt");
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				Connection con = (Connection) pageContext.getAttribute(name
						+ "_con");
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e) {
				// it's not fatal if the result set cannot be closed
				//e.printStackTrace();
			}
		}
		if (iterator != null) {
			try {
				iterator.remove();
			} catch (Exception e) {
				//
			}
			iterator = null;
		}
		_rset = null;
		return EVAL_PAGE;
	}

	public int doAfterBody() throws JspException {
		//System.out.println("AfterBody_Iterate");
		/*
		if (bodyContent != null) {
			TagUtils.getInstance().writePrevious(pageContext,
					bodyContent.getString());
			bodyContent.clearBody();
		}*/

		int maxPageIndex = 0;
		//int maxRecord = 0;
		int pageIndex = 0;
		Object pageObj = pageContext.getRequest().getParameter("page");//获取当前所在页
		Object maxPageObj = pageContext.getAttribute("maxPage");//获取最大页数
		if (pageObj != null)
			pageIndex = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(pageObj), 0);
		if (maxPageObj != null)
			maxPageIndex = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(maxPageObj), 0);
		if (_rset != null) {
			try {
				if (_rset.next() == true) {
					int t_index = pageIndex * maxPageIndex + index_1;
					setRowCount(id + "_index", index);
					setRowCount(id + "_index_1", index_1);
					setRowCount(id + "_t_index", t_index);
					++index;
					++index_1;
					return EVAL_BODY_AGAIN;//EVAL_BODY_TAG
				}
			} catch (SQLException e) {
				throw new JspTagException(e.toString());
			}
		} else if (iterator != null) {
			if (iterator.hasNext()) {
				Object element = iterator.next();
				if (element == null) {
					pageContext.removeAttribute(id);
				} else {
					pageContext.setAttribute(id, element);
				}
				iterator.remove();//移除列表中的当前对象
				return (EVAL_BODY_AGAIN);//EVAL_BODY_TAG
			} else {
				return (SKIP_BODY);
			}

		}

		return EVAL_PAGE;
	}

	public void release() {
		super.release();
		_rset = null;
		name = "rows";
		id = "row";
		index = 0;
		index_1 = 1;
	}

	protected void setRowCount(String name, int rowCount) {
		pageContext.setAttribute(name, String.valueOf(rowCount));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
