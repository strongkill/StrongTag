package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class pageTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Log log = LogFactory.getLog(this.getClass().getName());

	protected String url = null;
	protected String comName = null;
	protected String cur_page = null;
	protected int maxpage = 0;
	protected String tempComName = null; //记录临时的comName的值，用于分解comName
	protected boolean bFirst = true;//是否为第一次分解。
	protected String createHtml; //是否需要产生静态页面
	protected String maxHtmlPage="5";//产生静态页页最大数，默认为5页　（原为默认１０页，由于产生页面过多，致使WEB服务器负载过重，现改为５页）
	//对于列表页面，产生静态文件的文件名通过htmlFileName指定，由于动态列表页面可根据参数产生
	//很多不同的列表，所以相应的静态文件名也必须不同，如不同类别的产品列表，用同一列表页，但数据不同
	//为了处理这类问题，htmlFileName参数可以指定参数，参数用中括号括起来，系统将从request、
	//等对象中取出参数对应的值来替代参数。格式为：/html/product/pro_list_[pro_type_id] ,
	//注：1.文件名不须要后缀固定为html，2.须加上相对根路径的URL路径
	protected String htmlFileName;
	public void release() {
		super.release();
		url = null;
		comName = null;
		cur_page = null;
		tempComName = null;
		bFirst = true;
		maxHtmlPage="5";
	}

	protected int getIntValue(String str_int_value,int default_value)
	{
		int result = 0;
		try
		{
			result = Integer.valueOf(str_int_value).intValue();
		}
		catch(Exception e)
		{
			result = default_value;
		}
		return result;
	}
	/**
	 * 获取真实的静态文件名（替换参数）
	 * @return
	 */
	protected String getRealHtmlFileName()
	{
		if(htmlFileName == null)
			return null;
		String result = "";
		String left_result = htmlFileName;
		String p_str = null;
		int pos = -1;
		int pos2 = -1;

		while((pos=left_result.indexOf("["))>=0)
		{
			String t_result = left_result.substring(0,pos);
			String t_left_result = left_result.substring(pos+1);
			pos2 = t_left_result.indexOf("]");
			if(pos2>0)
			{
				p_str = t_left_result.substring(0, pos2);
				left_result = t_left_result.substring(pos2 + 1);
				String p_value = pageContext.getRequest().getParameter(p_str);
				if(p_value==null)
					p_value = (String)pageContext.getRequest().getAttribute(p_str);
				if(p_value==null)
					p_value = (String)pageContext.getSession().getAttribute(p_str);

				if (p_value != null) {
					result = result + t_result + strTrim(p_value);
				}
				else {
					result = result + t_result;
				}
			}
		}
		if(left_result!=null && left_result.length() >0)
		{
			result += left_result;
		}
		return result;

	}
	/**
	 * 将字符串的左右空格去掉，如字符串为空，则返回空
	 * @param str
	 * @return
	 */
	protected   String strTrim(String str)
	{
		if(str!=null)
			str = str.trim();
		return str;
	}


	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUrl()
	{
		return url;
	}
	public void setComName(String comName)
	{
		this.comName = comName;
	}
	public String getComName()
	{
		return comName;
	}

	public int doStartTag() throws JspException {

		return (EVAL_BODY_INCLUDE);

	}

	public int doEndTag() throws JspException {
//		Print the ending element to our output writer
		if(getIntValue(cur_page,0) >= 0)
		{
			JspWriter writer = pageContext.getOut();
			try {
				writer.print("</a>");
			} catch (IOException e) {
				throw new JspException(e.getMessage());
			}
		}
		return (EVAL_PAGE);

	}
	/**
	 * 处理查询参数
	 * @return
	 */
	protected String dealQueryString()
	{
		String res = comName;
		String queryString = null;
		String[] comNameList = null;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		if(res!=null)
		{
			comNameList = res.split("::");
		}
		String t_comName = null;
		String comValue = null;
		for(int i =0;i<comNameList.length;i++)
		{
			t_comName = comNameList[i];
			if(t_comName == null || t_comName.length() <1)
				continue;
			comValue =request.getParameter(t_comName);

			if(comValue==null)
				comValue = String.valueOf(pageContext.getAttribute(t_comName));
			if(comValue !=null && !"null".equalsIgnoreCase(comValue) && comValue.length() >0)
			{
//				if("keyword".equals(t_comName) && comName!=null)
				{
					try
					{
						comValue = java.net.URLEncoder.encode(comValue,
								request.getCharacterEncoding());
					}
					catch(Exception e)
					{
						log.error(e.getMessage());
					}
				}

//				if("keyword".equals(t_comName) && comName!=null && comName.indexOf("::addChar")>=0)
//				comValue = addUnderLine(comValue);

//				comValue  = java.net.URLEncoder.encode(comValue);
				queryString = delQueryStr(queryString, t_comName); //删除指定参数
				queryString = addQueryStr(queryString, t_comName, comValue); //添加指定参数
			}
			if(comName!=null && comName.indexOf("::isDebug")>=0)
			{
				//指定参数中含有"::isDebug",则打印运行信息
				log.debug(t_comName+"="+comValue);
//				System.out.println("pageTage -- "+t_comName+"="+comValue);
			}
		}
		if(comName!=null && comName.indexOf("::isDebug")>=0)
		{
			//指定参数中含有"::isDebug",则打印运行信息
			log.debug("queryString="+queryString);

//			System.out.println("pageTage -- queryString="+queryString);
		}

		return queryString;

	}

	/**
	 * 将查询的字符串后面加一"_"，用于暂时解决中文问题
	 * @param value
	 * @return
	 */
	protected String addUnderLine(String value)
	{
		String result = value;
		if(!result.endsWith("`"))
		{
			result = result+"`";
		}
		return result;
	}

	protected String addQueryStr(String querystr,String addName,String addValue)
	{
		String result = querystr;
		if(addName==null || addValue==null)
			return result;
		if(addValue.trim().length() < 1 || "null".equals(addValue.trim()))
			return result;

		if(result==null || result.length() <1)
			result = addName+"="+addValue;
		else
			result = result + "&" + addName+"="+addValue;
		return result;
	}


	//从输入参数中去掉包含exceptStr的参数
	protected String delQueryStr(String querystr,String exceptStr)
	{
		if(querystr==null || querystr.trim().length() == 0)
			return null;
		String t_str = querystr;
		String result = querystr;
		int p_index = t_str.indexOf(exceptStr);
		if(p_index>0)
		{
			result = t_str.substring(0,p_index-1);
			t_str = t_str.substring(p_index);
			int s_index = -1;
			if((s_index=t_str.indexOf("&"))>=0)
			{
				result += t_str.substring(s_index);
				t_str = t_str.substring(0,s_index);
			}
		}
		else if(p_index==0)
		{
			int t_index = t_str.indexOf("&");
			if(t_index>0)
			{
				result=t_str.substring(t_index+1);
			}
			else
			{
				result = null;
			}
		}
		querystr = null;
		exceptStr = null;
		t_str = null;
		
		return result;
	}

	//分解comName的值,如果存在的话，返回值，不存在的话，返回null
	protected  String getNextComName()
	{
		if(bFirst)
		{
			bFirst = false;
			tempComName = comName;
		}
		if(tempComName == null || tempComName.length() <1)
			return null;
		String result = null;
		int pos = tempComName.indexOf("::");
		if(pos<0)
		{
			result = tempComName;
			tempComName = null;
			return result;
		}
		else
		{
			result = tempComName.substring(0,pos);
			tempComName = tempComName.substring(pos+2);
		}
		return result;
	}
	public String getCreateHtml() {
		return createHtml;
	}
	public void setCreateHtml(String createHtml) {
		this.createHtml = createHtml;
	}
	public String getMaxHtmlPage() {
		return maxHtmlPage;
	}
	/**
	 * 设置列表页面最大产生静态文件数，默认为５
	 * @param maxHtmlPage
	 */
	public void setMaxHtmlPage(String maxHtmlPage) {
		this.maxHtmlPage = maxHtmlPage;
	}
	public String getHtmlFileName() {
		return htmlFileName;
	}
	public void setHtmlFileName(String htmlFileName) {
		this.htmlFileName = htmlFileName;
	}

}