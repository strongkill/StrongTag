package net.strong.taglib.util;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.strong.util.ProDebug;

/**
 * <p>Title:JSP页面初始化 </p>
 * <p>Description: 初始化JSP页面的字符编码，根据'Accept-Language'设置
 * request及response的解码方式</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 * @deprecated
 */

public class jspInitTag extends TagSupport {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String judgeLocale = null;
  protected String isDebug = "false";

  public void setIsDebug(String isDebug)
  {
    this.isDebug = isDebug;
  }
  public String getIsDebug()
  {
    return this.isDebug;
  }
  public void setJudgeLocale(String judgeLocale)
  {
    this.judgeLocale = judgeLocale;
  }
  public String getJudgeLocale()
  {
    return this.judgeLocale;
  }

  public void release()
  {
    judgeLocale = null;
  }

  public int doStartTag() throws JspException {

    return (SKIP_BODY);

  }

  public int doEndTag() throws JspException {

    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
    String clientLanguage = request.getHeader("Accept-Language");
    if(isDebug.equalsIgnoreCase("true"))
    {
      ProDebug.addDebugLog("jspInitTag --  clientLanguage:"+clientLanguage);
      ProDebug.saveToFile();
    }
    if(clientLanguage != null)
    {
      try {
        if ("zh-cn".equals( clientLanguage )) {
          //for Simplied Chinese
          request.setCharacterEncoding("GBK");
          response.setContentType("text/html; charset=GBK");
        }
        else if (clientLanguage.equals("zh-tw")) {
          //for Traditional Chinese
          request.setCharacterEncoding("BIG5");
          response.setContentType("text/html; charset=BIG5");
        }
        else {
          //default encoding
          request.setCharacterEncoding("ISO-8859-1");
          response.setContentType("text/html; charset=ISO-8859-1");
        }
      }
      catch (java.io.UnsupportedEncodingException e) {
        throw new JspException("request setCharacterEncoding exception:" +
                               e.getMessage());
      }
    }
    if(judgeLocale != null && judgeLocale.equalsIgnoreCase("true"))
    {
     Cookie cookies[] = request.getCookies();
      String str_locale = null;
      if(cookies != null)
      {
        for (int i = 0; i < cookies.length; i++) {
          Cookie cookie = cookies[i];
          if (cookie.getName().equalsIgnoreCase("locale_prefer")) {
            str_locale = cookie.getValue();
            break;
          }
        }
      }
      if(isDebug.equalsIgnoreCase("true"))
      {
        ProDebug.addDebugLog("jspInitTag --  str_locale:"+str_locale);
        ProDebug.saveToFile();
      }
      if(str_locale!=null)
      {
        if(str_locale.equalsIgnoreCase("en"))
        {
          request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY,Locale.ENGLISH);
        }
        if(str_locale.equalsIgnoreCase("en_US"))
        {
          request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY,Locale.US);
        }
        if(str_locale.equalsIgnoreCase("zh_CN"))
        {
          request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY,
              Locale.SIMPLIFIED_CHINESE);
        }
        if(str_locale.equalsIgnoreCase("zh_TW") ||str_locale.equalsIgnoreCase("zh_HK")  )
        {
          request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY,
              Locale.SIMPLIFIED_CHINESE);
        }
        if(str_locale.equalsIgnoreCase("ja_JP"))
        {
          request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY,
              Locale.JAPANESE);
        }

      }
    }
      return EVAL_PAGE;

  }
}