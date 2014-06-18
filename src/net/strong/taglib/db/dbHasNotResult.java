package net.strong.taglib.db;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;

public class dbHasNotResult extends dbTag {

	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
		
		String name = getName();
		//if("rows".equals(name))name=null;
		if(name!=null && name.length()>0){
			ArrayList obj = (ArrayList) pageContext.getAttribute(name);
			if(obj!=null && obj.size()>0){
				obj = null;
				return SKIP_BODY;
			}
			return EVAL_BODY_INCLUDE;
		}
		
	    String str_max_record = (String) pageContext.getAttribute("maxRecord");
	    if (str_max_record == null)
	      str_max_record = (String) pageContext.getRequest().getAttribute("maxRecord");
	    if(str_max_record==null || "0".equals(str_max_record))
	    	return EVAL_BODY_INCLUDE;
	    
	    return SKIP_BODY;
	}


	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
}
