package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
/**
 * <p>Title: 分页选择标签</p>
 * <p>Description:列出当前所有页，可随意跳转到某页 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */

public class selectPageTag extends pageTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String onchange = null; //当选择某页时触发相应的JS事件

	public void setOnchange(String onchange)
	{
		this.onchange = onchange;
	}
	public String getOnchange()
	{
		return this.onchange;
	}

	public int doStartTag() throws JspException {

		return (SKIP_BODY);

	}

	public int doEndTag() throws JspException {
//		Generate the URL to be encoded
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
		StringBuffer this_url = new StringBuffer();
		//String comValue = null;

		int maxpage = 0;
		String queryString = request.getQueryString();//获取参数串
		StringBuffer results = new StringBuffer();
		Object maxp = pageContext.getAttribute("maxPage");
		if(maxp==null)
			maxp = pageContext.getRequest().getAttribute("maxPage");
		if(maxp!=null)
			maxpage = getIntValue(String.valueOf(maxp),0);
		maxp = null;

		String t_url = request.getRequestURI();
		if(url==null)
			this_url.append(t_url);
		else
			this_url.append(url);

		if(queryString==null || queryString.indexOf("page")<0) //参数串中未含page
			cur_page = "0";
		else
		{
			int i_page = net.strong.util.MyUtil.getStringToInt(String.valueOf(request.getParameter("page")),1);
			cur_page = String.valueOf(i_page );
//			cur_page = String.valueOf(Integer.valueOf(request.getParameter("page")).intValue());
		}

		if(comName!=null)
		{
			queryString = dealQueryString();
		}

		queryString = delQueryStr(queryString,"page");
		if(queryString == null)
		{
			queryString = "pSel=true";
		}
		this_url.append("?");
		this_url.append(queryString);

		//HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		int n_cur_page = getIntValue(cur_page,1); //当前页数
		//int n_max_html_page = getIntValue(maxHtmlPage,5); //最大静态页数

		if("true".equals(createHtml))
		{
			String result_file_name = getRealHtmlFileName();
			String t_str = "<script>function changeUrl(JsName){ if(JsName==0) location.replace(\""+
			result_file_name+".html\");  if(JsName>0 && JsName<"+maxHtmlPage+") location.replace(\""+
			result_file_name+"_p\"+JsName+\".html\"); if(JsName>="+maxHtmlPage+") location.replace(\""+
			this_url.toString() +"&page=\"+JsName);}</script>";
			results.append(t_str);

		}
		else
		{
			results.append("<script>function changeUrl(JsName){ location.replace(\"" +
					this_url.toString() + "&page=\"+JsName);}</script>");
		}

		results.append("<select name=\"pageSelected\"  onChange=\"changeUrl(this.value)\">");
		int start_pos= (n_cur_page-100)<0?0:(n_cur_page-100);
		int end_pos = (n_cur_page+100)>maxpage?maxpage:(n_cur_page+100);
		for(int i =start_pos;i<end_pos;i++)
		{
			results.append("<option value=\"" + String.valueOf(i) + "\"");
			if (i == getIntValue(cur_page,0))
//				Integer.valueOf(cur_page).intValue())
				results.append("selected");
			results.append(" >  " + String.valueOf(i + 1) + "  </option>");
		}
		results.append("</select>");

		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		}
		catch (IOException e)
		{
			throw new JspException(e.getMessage());
		}
		queryString = null;
		this_url = null;
		results = null;
		return (EVAL_PAGE);

	}

}