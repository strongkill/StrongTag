package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
/**
 * <p>Title: 第一页</p>
 * <p>Description:第一页页标签，主要是与dbListArray标签一起使用，
 * 取出第一页页的数据记录。 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class firstPageTag extends pageTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
//		Generate the URL to be encoded
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
		StringBuffer this_url = new StringBuffer();
		//String comValue = null;
		String queryString = request.getQueryString();//获取参数串
		StringBuffer results = new StringBuffer();
		Object maxp = pageContext.getAttribute("maxPage");
		
		if(maxp==null)
			maxp = pageContext.getRequest().getAttribute("maxPage");
		if(maxp!=null)
			maxpage = getIntValue(String.valueOf(maxp),0);
		maxp = null;
//		Integer.valueOf(String.valueOf(maxp)).intValue();
		cur_page = "0";
		if(comName!=null && !"true".equals(createHtml))
		{
			queryString = dealQueryString();

		}
		if(getIntValue(cur_page,0) < maxpage)
		{
			if("true".equals(createHtml))
			{
				this_url.append(getRealHtmlFileName()+".html");
			}
			else
			{
				if(url==null)
					this_url.append(request.getRequestURI());
				else
					this_url.append(url);

				queryString = delQueryStr(queryString, "page");

				if (queryString != null && queryString.length() > 0)
					queryString += "&page=" + cur_page;
				else
					queryString = "page=" + cur_page;
				this_url.append("?");
				this_url.append(queryString);
			}

			//HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			results.append("<a href=\"");
			results.append(this_url.toString());
			
//			results.append(response.encodeURL(this_url.toString()));
			results.append("\">");

			JspWriter writer = pageContext.getOut();
			try {
				writer.print(results.toString());
			}
			catch (IOException e)
			{
				throw new JspException(e.getMessage());
			}
		}
		queryString = null;
		this_url = null;
		results = null;
		return (EVAL_BODY_INCLUDE);

	}

}