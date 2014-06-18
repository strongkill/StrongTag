package net.strong.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class tagTest extends BodyTagSupport {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String body = null;
  private String name = null;
  public void setName(String name)
  {
    this.name = name;
  }
  public String getName()
  {
    return name;
  }

  public int doStartTag() throws javax.servlet.jsp.JspTagException
  {
     return (EVAL_BODY_BUFFERED);
  }

    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            body = bodyContent.getString();
            if (body != null) {
                body = body.trim();
            }
            if (body.length() < 1) {
                body = null;
            }
        }
        return (SKIP_BODY);

    }

  public int doEndTag() throws JspException
  {
    /*HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    ProDebug.addDebugLog("tagTest");
    ProDebug.saveToFile();
*/
    String result = null;
    if(result==null)
      result = "result is null";
    if(body!=null)
      result += body;
    JspWriter writer = pageContext.getOut();
    try {
      writer.print(result);
    } catch (IOException e) {
      throw new JspException(e.getMessage());
    }

    return EVAL_PAGE;
  }
}