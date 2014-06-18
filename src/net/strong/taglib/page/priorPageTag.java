package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class priorPageTag extends pageTag {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public int doStartTag() throws JspException {
// Generate the URL to be encoded
    HttpServletRequest request =
        (HttpServletRequest) pageContext.getRequest();
    StringBuffer this_url = new StringBuffer();
    //String comValue = null;
    String t_url = request.getRequestURI();
    if(url==null)
      this_url.append(t_url);
    else
      this_url.append(url);
    
    t_url = null;
    String queryString = request.getQueryString();//获取参数串
    if(queryString==null || queryString.indexOf("page")<0) //参数串中未含page
      cur_page = "-1";
    else
    {
      int i_page = net.strong.util.MyUtil.getStringToInt(String.valueOf(request.getParameter("page")),1);
      cur_page = String.valueOf(i_page - 1);
    }

    int n_cur_page = getIntValue(cur_page,1); //当前页数
    int n_max_html_page = getIntValue(maxHtmlPage,5); //最大静态页数

    if(comName!=null &&( !"true".equals(createHtml) ||("true".equals(createHtml) && n_cur_page>n_max_html_page-1 )))
    {
      queryString = dealQueryString();
    }
    if(n_cur_page >= 0)
    {
      String result_url = null;
      if(n_cur_page==0&&"true".equals(createHtml))
      {
        result_url = getRealHtmlFileName()+".html";
      }
      else if("true".equals(createHtml) && n_cur_page < n_max_html_page)
      {
        //需要创建静态页面，并且当前页还未到最大页，下一页的链接用静态的。
        result_url = getRealHtmlFileName()+"_p"+n_cur_page+".html";
      }
      else
      {
        queryString = delQueryStr(queryString, "page");
        if (queryString != null && queryString.length() > 0)
          queryString += "&page=" + cur_page;
        else
          queryString = "page=" + cur_page;
        this_url.append("?");
        this_url.append(queryString);
        result_url = this_url.toString();
      }
      StringBuffer results = new StringBuffer();      
      //HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
      results.append("<a href=\"");
      results.append(result_url);
//      results.append(response.encodeURL(result_url));
      results.append("\">");

      JspWriter writer = pageContext.getOut();
      try {
        writer.print(results.toString());
      }
      catch (IOException e)
      {
        throw new JspException(e.getMessage());
      }
      results = null;
    }
    this_url = null;
    queryString = null;
    
    return (EVAL_BODY_INCLUDE);

  }
}