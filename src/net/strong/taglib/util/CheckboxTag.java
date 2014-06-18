package net.strong.taglib.util;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;

/**
 * <p>Title: checkbox标签</p>
 * <p>Description: 在html:checkbox标签的基础上加一propertyValue属性，
 * 用于从pageContext对象上取得propertyValue的值，并将值赋与checkbox的value</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 * @deprecated
 */

public class CheckboxTag extends BaseHandlerTag {


    // ----------------------------------------------------- Instance Variables

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String p_value = null;
    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
     MessageResources.getMessageResources(Constants.Package + ".LocalStrings");


    /**
     * The name of the bean containing our underlying property.
     */
    protected String name = Constants.BEAN_KEY;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * The property name for this field.
     */
    protected String property = null;

    /**
     * the property for checkbox value
     */
    protected String propertyValue = null;

    /**
     * The body content of this tag (if any).
     */
    protected String text = null;


    /**
     * The server value for this option.
     */
    protected String value = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the property name.
     */
    public String getProperty() {

        return (this.property);

    }


    /**
     * Set the property name.
     *
     * @param property The new property name
     */
    public void setProperty(String property) {

        this.property = property;

    }

    public String getPropertyValue(){
      return propertyValue;
    }

    public  void setPropertyValue(String propertyValue)
    {
      this.propertyValue = propertyValue;
    }

    /**
     * Return the server value.
     */
    public String getValue() {

        return (this.value);

    }


    /**
     * Set the server value.
     *
     * @param value The new server value
     */
    public void setValue(String value) {

        this.value = value;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Generate the required input tag.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Create an appropriate "input" element based on our parameters
        StringBuffer results = new StringBuffer("<input type=\"checkbox\"");
        results.append(" name=\"");
        // * @since Struts 1.1
        if( indexed )
                prepareIndex( results, name );
        results.append(this.property);
        results.append("\"");
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        results.append(" value=\"");
        p_value = null;
        if (value == null)
        {
         Object result = new Object();// RequestUtils.lookup(pageContext, name, propertyValue, null);
          if(result == null)
            results.append("on");
          else
          {
            if(!(result instanceof String))
              result = result.toString();
            p_value = (String)result;
            results.append(p_value);
          }
        }
        else
            results.append(value);
        results.append("\"");
        Object result = new Object();// RequestUtils.lookup(pageContext, name, property, null);
        if (result == null)
            result = "";
        if (!(result instanceof String))
            result = result.toString();
        String checked = (String) result;
        if (checked.equalsIgnoreCase(value)
            || checked.equalsIgnoreCase(p_value)
            || checked.equalsIgnoreCase("true")
            || checked.equalsIgnoreCase("yes")
            || checked.equalsIgnoreCase("on"))
            results.append(" checked=\"checked\"");
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(">");

        // Print this field to our output writer
        //ResponseUtils.write(pageContext, results.toString());

        // Continue processing this page
        this.text = null;
        return (EVAL_BODY_BUFFERED);

    }



    /**
     * Save the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0)
                text = value;
        }
        return (SKIP_BODY);

    }


    /**
     * Process the remainder of this page normally.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Render any description for this checkbox
        //if (text != null)
            //ResponseUtils.write(pageContext, text);

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }


    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        name = Constants.BEAN_KEY;
        property = null;
        text = null;
        value = null;

    }


}