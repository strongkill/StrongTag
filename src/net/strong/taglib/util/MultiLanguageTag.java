package net.strong.taglib.util;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.strong.util.ProDebug;


/**
 * <p>Title:多国语言标签 </p>
 * <p>Description: 多国语言标签，实现与bean:message一样的功能。可以动态改变当前语言
 * ，而不用管当前的语言环境是什么。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 * @deprecated
 */

public class MultiLanguageTag extends TagSupport {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String key = null;
  private String resourceName = null;

  public void setKey(String key)
  {
    this.key = key;
  }
  public String getKey()
  {
    return this.key;
  }
  public void setResourceName(String resourceName)
  {
    this.resourceName = resourceName;
  }
  public String getResourceName()
  {
    return this.resourceName;
  }

  public int doStartTag() throws JspException {

    return (SKIP_BODY);

  }

  public int doEndTag() throws JspException {

    if(key==null)
      throw new JspException("key can not be null");
    if(resourceName==null)
      throw new JspException("sourceName can not be null");

    Locale cur_locale = null;
    HttpSession session = pageContext.getSession();
    if (session != null)
      cur_locale = (Locale)session.getAttribute("userLocale");
    if(cur_locale==null)
      cur_locale = Locale.getDefault();

    String source_nm = pageContext.getServletConfig().getInitParameter("config");

    ProDebug.addDebugLog("source_name:"+source_nm);
    ProDebug.saveToFile();

    ResourceBundle rb = ResourceBundle.getBundle(
            resourceName,
            cur_locale);
    String result = rb.getString(key);

    JspWriter writer = pageContext.getOut();
    try {
      writer.print(result);
    } catch (IOException e) {
      throw new JspException(e.getMessage());
    }
      return EVAL_PAGE;

  }


  public void release() {
    key = null;
    resourceName=null;
    super.release();

  }

}