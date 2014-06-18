package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * <p>Title: 下一页标签</p>
 * <p>Description:
 * 下一页标签，主要是与dbListArryTag标签一下起使用，
 * 用dbListArray分页取得数据记录 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author jony
 * @version 1.0
 */

public class nextPageTag extends pageTag {
	private static final long serialVersionUID = 1L;
  public int doStartTag() throws JspException {
// Generate the URL to be encoded
    HttpServletRequest request =
        (HttpServletRequest) pageContext.getRequest();
    StringBuffer this_url = new StringBuffer();
   // String comValue = null;

    if(url==null)
      this_url.append(request.getRequestURI());
    else
      this_url.append(url);
    String queryString = request.getQueryString();//获取参数串
    StringBuffer results = new StringBuffer();
    Object maxp = pageContext.getAttribute("maxPage");
    if(maxp==null)
      maxp = pageContext.getRequest().getAttribute("maxPage");
    if(maxp!=null)
      maxpage = getIntValue(String.valueOf(maxp),0);
    maxp = null;
    
    if(queryString==null || queryString.indexOf("page")<0) //参数串中未含page
      cur_page = "1";
    else
    {
      int i_page = net.strong.util.MyUtil.getStringToInt(String.valueOf(request.getParameter("page")),1);
      cur_page = String.valueOf(i_page + 1);
//      cur_page = String.valueOf(Integer.valueOf(request.getParameter("page")).intValue() + 1);
    }

    int n_cur_page = getIntValue(cur_page,1); //当前页数
    int n_max_html_page = getIntValue(maxHtmlPage,5); //最大静态页数

    if(comName!=null &&( !"true".equals(createHtml) ||("true".equals(createHtml) && n_cur_page>n_max_html_page-1 )))
    {
      queryString = dealQueryString();
      /*
      java.util.Enumeration em = pageContext.getRequest().getParameterNames(); //获取所有request中的参数名
      if(em!=null)
      {
        while (em.hasMoreElements()) {
          String t_str = (String) em.nextElement();
          if (comName != null && comName.indexOf(t_str) < 0) {
            ProDebug.addDebugLog(t_str + " is not exit");
            ProDebug.saveToFile();
            continue;
          }
          String p_value = pageContext.getRequest().getParameter(t_str);

          if (p_value != null && p_value.trim().length() > 0) {
            queryString = delQueryStr(queryString, t_str); //删除指定参数
            queryString = addQueryStr(queryString, t_str, p_value); //添加指定参数
          }

        }
      }
      em = null;
          */
    }


    if(getIntValue(cur_page,0)<maxpage)
//        Integer.valueOf(cur_page).intValue() < maxpage)
    {
      String result_url = null;
      if("true".equals(createHtml) && n_cur_page < n_max_html_page)
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

      HttpServletResponse response =
        (HttpServletResponse) pageContext.getResponse();
      results.append("<a href=\"");
      results.append(response.encodeURL(result_url));
      results.append("\">");

      JspWriter writer = pageContext.getOut();
      try {
          writer.print(results.toString());
      }
      catch (IOException e)
      {
          throw new JspException(e.getMessage());
      }
      result_url = null;
    }
    queryString = null;
    this_url = null;
    results = null;
    return (EVAL_BODY_INCLUDE);

  }
}