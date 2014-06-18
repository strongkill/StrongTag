package net.strong.taglib.db;

import java.security.Principal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.TagUtils;

public class dbPresentTag extends dbConditionalTagBase {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public static final String ROLE_DELIMITER = ",";

  // ------------------------------------------------------ Protected Methods


  /**
   * Evaluate the condition that is being tested by this particular tag,
   * and return <code>true</code> if the nested body content of this tag
   * should be evaluated, or <code>false</code> if it should be skipped.
   * This method must be implemented by concrete subclasses.
   *
   * @exception JspException if a JSP exception occurs
   */
  protected boolean condition() throws JspException {

      return (condition(true));

  }


  /**
   * Evaluate the condition that is being tested by this particular tag,
   * and return <code>true</code> if the nested body content of this tag
   * should be evaluated, or <code>false</code> if it should be skipped.
   * This method must be implemented by concrete subclasses.
   *
   * @param desired Desired outcome for a true result
   *
   * @exception JspException if a JSP exception occurs
   */
  protected boolean condition(boolean desired) throws JspException {

      // Evaluate the presence of the specified value
      boolean present = false;
      if (cookie != null) {
          Cookie cookies[] =
              ((HttpServletRequest) pageContext.getRequest()).
              getCookies();
          if (cookies == null)
              cookies = new Cookie[0];
          for (int i = 0; i < cookies.length; i++) {
              if (cookie.equals(cookies[i].getName())) {
                  present = true;
                  break;
              }
          }
      } else if (header != null) {
          String value =
              ((HttpServletRequest) pageContext.getRequest()).
              getHeader(header);
          present = (value != null);
      }
      else if (name != null) {

          Object bean = null;
          Object value = null;
          try {
              if (property != null) {
                  bean = lookup(pageContext,name,null);
                  if(value!=null)
                  {
                    if(bean instanceof HashMap)
                    {
                      value = (String)((HashMap)bean).get(property);
                    }
                    if(bean instanceof WeakHashMap)
                    {
                      value = (String)((WeakHashMap)bean).get(property);
                    }
                    if(bean instanceof ResultSet)
                    {
                      try
                      {
                        ResultSet rs = (ResultSet)bean;
                        value = rs.getString(property);
                      }
                      catch(Exception e)
                      {
                        value = null;
                      }
                    }
                  }
              } else {
                  value = lookup(pageContext,name,null);
              }
          } catch (JspException e) {
              value = null;
          }
          present = (value != null);
      }
      else if (parameter != null) {
          String value =
              pageContext.getRequest().getParameter(parameter);
          present = (value != null);
      } else if (role != null) {
          HttpServletRequest request = (HttpServletRequest)
                                                     pageContext.getRequest();
          StringTokenizer st = new StringTokenizer(role, ROLE_DELIMITER, false);
          while(!present && st.hasMoreTokens()){
              present = request.isUserInRole(st.nextToken());
          }
      } else if (user != null) {
          HttpServletRequest request =
              (HttpServletRequest) pageContext.getRequest();
          Principal principal = request.getUserPrincipal();
          present = (principal != null) &&
              user.equals(principal.getName());
      } else {
          JspException e = new JspException
              (messages.getMessage("logic.selector"));
          TagUtils.getInstance().saveException(pageContext, e);
          throw e;
      }

      return (present == desired);

  }


  protected Object lookup(PageContext pageContext,
      String name, String scope) throws JspTagException {

      Object bean = null;
      if (scope == null)
      {
        bean = pageContext.findAttribute(name);
        if(bean == null)
          bean = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
        if(bean == null)
          bean = pageContext.getAttribute(name, PageContext.SESSION_SCOPE);
        if(bean == null)
          bean = pageContext.getAttribute(name, PageContext.APPLICATION_SCOPE);
      }
      else if (scope.equalsIgnoreCase("page"))
          bean = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
      else if (scope.equalsIgnoreCase("request"))
          bean = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
      else if (scope.equalsIgnoreCase("session"))
          bean = pageContext.getAttribute(name, PageContext.SESSION_SCOPE);
      else if (scope.equalsIgnoreCase("application"))
          bean =
              pageContext.getAttribute(name, PageContext.APPLICATION_SCOPE);
      else {
          JspTagException e = new JspTagException("Invalid scope " + scope);
          throw e;
      }
      return (bean);

   }

}