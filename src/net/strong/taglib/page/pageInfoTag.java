package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class pageInfoTag extends TagSupport {
	private static final long serialVersionUID = 1L;
  String type="0";//0--当前页面，1--页数，2--总数，3--当前页开始数量，4--当前页结束数量,5--打印产生静态页面所用信息（当前第几页，共几页）
  int nType = 0;
  private String maxHtmlPage;//产生最大的静态页数,默认为10页
  private String createHtml; //是否产生静态页面

  public void setType(String type)
  {
    this.type = type;
    if(type!=null && type.length() > 0)
      nType = net.strong.util.MyUtil.getStringToInt(type,0);
//          Integer.valueOf(type).intValue();
  }
  public String getType()
  {
    return this.type;
  }

  public int doStartTag() throws JspException {

    return (SKIP_BODY);

  }

  public int doEndTag() throws JspException {

    String str_cur_page = "0";
    String str_max_record = "0";
    String str_max_page = "0";
    String pageMaxObj = "0";
    int cur_page = 1;
    int page_max = 10;
    int cur_count = 0;
    int max_html_page = 0;
    int start_num = 0;
    int end_num = 0;
    int i_max_record = 0;
    int i_max_page = 0;

    str_cur_page = pageContext.getRequest().getParameter("page");
    str_max_record = (String) pageContext.getAttribute("maxRecord");
    if (str_max_record == null)
      str_max_record = (String) pageContext.getRequest().getAttribute("maxRecord");
    str_max_page = (String) pageContext.getAttribute("maxPage");
    if (str_max_page == null)
      str_max_page = (String) pageContext.getRequest().getAttribute("maxPage");
    if (str_cur_page != null && str_cur_page.length() > 0)
      cur_page = net.strong.util.MyUtil.getStringToInt(str_cur_page, 0) + 1;
  //          Integer.valueOf(str_cur_page).intValue()+1;
    str_cur_page = String.valueOf(cur_page);
    if (str_max_record == null)
      str_max_record = "0";
    if (str_max_page == null)
      str_max_page = "0";

    pageMaxObj = (String) pageContext.getRequest().getAttribute("pageMax"); //一页显示最大记录数
    page_max = net.strong.util.MyUtil.getStringToInt(String.valueOf(pageMaxObj),
        0);

    pageMaxObj = null;
    String str_cur_count = (String) pageContext.getRequest().getAttribute(
        "rowCount");
    cur_count = page_max; //当前记录数
    cur_count = net.strong.util.MyUtil.getStringToInt(str_cur_count, 0);
    str_cur_count= null;
    
    if (maxHtmlPage == null)
      maxHtmlPage = "5";
    max_html_page = net.strong.util.MyUtil.getStringToInt(maxHtmlPage, 10);

   start_num =  (cur_page-1)*page_max + 1;//当前页开始数
    end_num = cur_page * page_max;
    if(cur_count!=page_max)
    {
      end_num = (cur_page-1)*page_max + 1 + cur_count;//当前页不满最大显示数
    }
      i_max_record = net.strong.util.MyUtil.getStringToInt(str_max_page,0);
//          Integer.valueOf(str_max_page).intValue();
      i_max_page = net.strong.util.MyUtil.getStringToInt(str_max_page,0);
//          Integer.valueOf(str_max_page).intValue();
      if(i_max_page ==0 && i_max_record>0) //如果记录数大于零而页数为零，则设页数为1
        str_max_page = "1";

    String result = null;
    if(nType==0)
    {
      result = str_cur_page;
    }
    else if(nType==1)
    {
      result = str_max_page;
    }
    else if(nType==2)
    {
      result = str_max_record;
    }
    else if(nType==3)
    {
      result = String.valueOf(start_num);
    }
    else if(nType==4)
    {
      result = String.valueOf(end_num);
    }
    else if(nType==5)
    {
      if("true".equals(createHtml) && cur_page < max_html_page)
        result = "<!--page ["+str_cur_page+","+str_max_page+"]  page--> ";
    }
    else
    {
      result = "0";
    }

    if(result == null)
      result = "";

//    result  = null;
// Print the ending element to our output writer
//    JspWriter writer = pageContext.getOut();
    try {
      JspWriter writer = pageContext.getOut();
      writer.write(result);
//      pageContext.getOut().write(result);
//      pageContext.getOut().flush();
//      writer.print(result);
    } catch (IOException e) {
      throw new JspException(e.getMessage());
    }

    str_cur_page = null;
    str_max_record = null;
    str_max_page = null;
    pageMaxObj = null;
    result = null;
    return (EVAL_PAGE);

  }
  public String getMaxHtmlPage() {
    return maxHtmlPage;
  }
  public void setMaxHtmlPage(String maxHtmlPage) {
    this.maxHtmlPage = maxHtmlPage;
  }
  public String getCreateHtml() {
    return createHtml;
  }
  public void setCreateHtml(String createHtml) {
    this.createHtml = createHtml;
  }
}
