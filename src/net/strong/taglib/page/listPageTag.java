package net.strong.taglib.page;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class listPageTag extends pageTag {
	private static final long serialVersionUID = 1L;

	protected String listCount = "5"; // 列表页数的个数

	protected int nlistCount = 5;

	public void release() {
		super.release();
		listCount = "5";
	}

	public void setListCount(String listCount) {
		this.listCount = listCount;
		try {
			nlistCount = Integer.valueOf(listCount).intValue();
		} catch (Exception e) {
			nlistCount = 5;
		}
	}

	public String getListCount() {
		return this.listCount;
	}

	public int doEndTag() throws JspException {

		return (EVAL_PAGE);

	}

	public int doStartTag() throws JspException {
		// Generate the URL to be encoded
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		StringBuffer this_url = new StringBuffer();
		// String comValue = null;

		if (url == null)
			this_url.append(request.getRequestURI());
		else
			this_url.append(url);
		String queryString = request.getQueryString();// 获取参数串
		StringBuffer results = new StringBuffer();
		Object maxp = pageContext.getAttribute("maxPage");
		if (maxp == null)
			maxp = pageContext.getRequest().getAttribute("maxPage");
		// System.out.println("maxPage at listPageTag:"+maxp);
		if (maxp != null)
			maxpage = getIntValue(String.valueOf(maxp), 0);
		// Integer.valueOf(String.valueOf(maxp)).intValue();
		
		maxp =null;
		if (queryString == null || queryString.indexOf("page") < 0) // 参数串中未含page
			cur_page = "1";
		else {
			int i_page = net.strong.util.MyUtil.getStringToInt(String
					.valueOf(request.getParameter("page")), 1);
			cur_page = String.valueOf(i_page + 1);
			// cur_page =
			// String.valueOf(Integer.valueOf(request.getParameter("page")).intValue()
			// + 1);
		}

		int n_cur_page = getIntValue(cur_page, 0);

		if (comName != null && comName.indexOf("::isDebug") > 0) {
			// 指定参数中含有"::isDebug",则打印运行信息
			String str_url = request.getRequestURI();
			String str_ser = request.getServerName();
			System.out.println("debug at " + str_ser + "/" + str_url);
		}

		if (comName != null) {
			queryString = dealQueryString();
			/*
			 * //
			 * 由于在实际使用中，不知为什么，用pageContext.getRequest().getParameter(res.trim());取不到 //
			 * 值，从而导致不能正能分页，所以不采用此方法来分页，改成下面的方法。 String res = null; while((res =
			 * getNextComName())!=null) { if(res == null) continue; comValue =
			 * pageContext.getRequest().getParameter(res.trim());//request.getParameter(res);
			 * if(comValue==null) comValue = (String )
			 * pageContext.getAttribute(res);
			 * 
			 * if (comValue != null) { queryString = delQueryStr(queryString,
			 * res); //删除指定参数 queryString = addQueryStr(queryString, res,
			 * comValue); //添加指定参数 } if(comName!=null &&
			 * comName.indexOf("::isDebug")>0) { //指定参数中含有"::isDebug",则打印运行信息
			 * System.out.println("comName:"+comName+"comValue:"+comValue+" ,
			 * res : " + res); } }
			 */
			/*
			 * java.util.Enumeration em =
			 * pageContext.getRequest().getParameterNames(); //获取所有request中的参数名
			 * 
			 * while(em.hasMoreElements()) { String t_str =
			 * (String)em.nextElement(); if(comName!=null &&
			 * comName.indexOf(t_str)<0) { ProDebug.addDebugLog(t_str + " is
			 * not exit"); ProDebug.saveToFile(); continue; } String p_value =
			 * pageContext.getRequest().getParameter(t_str); // p_value =
			 * java.net.URLEncoder.encode(p_value); // p_value =
			 * MultiLanguage.getUnicode(p_value,request);
			 * 
			 * if (p_value != null && p_value.trim().length() >0) { queryString =
			 * delQueryStr(queryString, t_str); //删除指定参数 queryString =
			 * addQueryStr(queryString, t_str, p_value); //添加指定参数 } //
			 * ProDebug.addDebugLog("t_str:"+t_str+" , p_value : " + p_value); //
			 * ProDebug.saveToFile();
			 * 
			 * if(comName!=null && comName.indexOf("::isDebug")>0) {
			 * //指定参数中含有"::isDebug",则打印运行信息
			 * System.out.println("comName:"+comName+"t_str:"+t_str+" , p_value : " +
			 * p_value); } }
			 */
		}

		int startPage = 0;
		int t_page = nlistCount; // 共列出的页数
		if (nlistCount < maxpage) {
			int mid_page = nlistCount / 2 + 1;
			if (n_cur_page > mid_page) {
				startPage = n_cur_page - mid_page;
			}
			if (mid_page > maxpage - n_cur_page) {
				startPage = maxpage - nlistCount;
			}
		} else {
			t_page = maxpage;
		}

		// 生成链接列表
		String result_url = null;
		String tempQuery = null;
		for (int i = startPage; i < startPage + t_page; i++) {
			result_url = null;
			if (i == 0 && "true".equals(createHtml)) {
				result_url = getRealHtmlFileName() + ".html";
			} else if ("true".equals(createHtml)
					&& i < getIntValue(maxHtmlPage, 5)) {
				result_url = getRealHtmlFileName() + "_p" + i + ".html";
			} else {
				tempQuery = null;
				StringBuffer temp_url = new StringBuffer();
				tempQuery = delQueryStr(queryString, "page");

				if (tempQuery != null && tempQuery.length() > 0)
					tempQuery += "&page=" + i;
				else
					tempQuery = "page=" + i;
				temp_url.append(this_url);

				temp_url.append("?");
				temp_url.append(tempQuery);
				result_url = temp_url.toString();
				temp_url = null;
			}

			// HttpServletResponse response = (HttpServletResponse)
			// pageContext.getResponse();

			if (i == n_cur_page - 1) { // 当前页
				results.append("<font color=\"#FF3333\">[");
				results.append(i + 1);
				results.append("]</font>&nbsp;");

			} else {
				results.append("<a href=\"");
				results.append(result_url);
				// results.append(response.encodeURL(result_url));
				results.append("\">[");
				results.append(i + 1);
				results.append("]</a>&nbsp;");
			}
		}
		result_url = null;
		tempQuery = null;
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		queryString = null;
		this_url = null;
		results = null;
		return (EVAL_BODY_INCLUDE);

	}

}