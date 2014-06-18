package net.strong.exutil;

import java.util.regex.Pattern;


/**
 *
 * <p>Title:HTML格式代码过滤器 </p>
 * <p>Description: 本类是用于过滤HTML代码的，将指定的字符串的HTML代码全部去掉，只留下文本信息</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class htmlFilter {
	//单行过滤关键字，遇到以下关键字时，在100个字符内，只要再遇到“>”，则此行都乎略掉
	private static String filter_line_key =
		"<table </table <span </span <font </font  " +
		"<tr </tr <div </div <tbody </tbody <td </td <a </a <img </img ";
	//整段过滤关键字，遇到以下关键字时（<script），在300个字符内，只要再遇到结束关键字（</script ），则此段都乎略掉
	private static String filter_block_key = "<script <style";


	public htmlFilter() {
	}
	/**
	 * 截取字符,已过滤html
	 * @param str
	 * @param maxLength
	 * @return
	 */
	public static String SubString(String str,int maxLength){
		return SubString(str, 0, maxLength,"...");
	}
	public static String SubString(String str,int start,int maxLength,String more){
		if(str==null || maxLength<=0)return null;
		str = doFilterAllHtmlTag(str);
		str = subContentStringOrialBytes(str,start, maxLength,more);
		//str = subContent(str, maxLength);
		return str;
	}

	/**
	 * 过滤HTML代码,性能不及doFilterAllHtmlTag好.请使用doFilterAllHtmlTag
	 * @deprecated
	 * @param inputString
	 * @return
	 */
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; //含html标签的字符串
		String textStr ="";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
			String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); //过滤script标签

			p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); //过滤style标签

			p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); //过滤html标签

			textStr = htmlStr;

		}catch(Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;//返回文本字符串
	}
/**
 * 过滤HTML代码.有着良好的性能,推荐使用.
 * @param str
 * @return
 */
	public static String doFilterAllHtmlTag(String str){
		if(str==null)return null;
		//str = str.replaceAll("\n","");
		//str = str.replaceAll("\\<style(.*)\\>(.*)\\</style\\>", "");
		str = str.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>","");
		//str = str.replaceAll("\\<script(.*)\\>(.*)\\</script\\>", "");
		str = str.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>","");
		//str = str.replaceAll("<[^<>]+>", "");
		str = str.replaceAll("<[^>]+>", "");
		return str;
	}

	/**
	 * 截取字符串的前targetCount个字符
	 * @param str 被处理字符串
	 * @param targetCount 截取长度
	 * @version 1.1
	 * @author Strong Yuan
	 * @return String
	 */
	public static String subContentStringOrialBytes(String str,int targetCount){
		return subContentStringOrialBytes(str,targetCount,"...");
	}
	/**
	 * 截取字符串的前targetCount个字符
	 * @param str 被处理字符串
	 * @param targetCount 截取长度
	 * @version 1.1
	 * @author Strong Yuan
	 * @return String
	 */
	public static String subContentStringOrialBytes(String str,int start,int targetCount){
		return subContentStringOrialBytes(str,start,targetCount,"...");
	}
	/**
	 * 获取指定长度字符串的字节长
	 * @param str 被处理字符串
	 * @param maxlength 截取长度
	 * @author Strong Yuan
	 * @version 1.1
	 * @return String
	 */
	private static long getStringByteLength(String str,int maxlength){
		if(str==null)
			return 0;
		int tmp_len = maxlength;

		if(str.length()<maxlength)
			tmp_len = str.length();
		else if(str.length()>maxlength*2)
			tmp_len = maxlength*2;



		char[] tempchar = str.substring(0, tmp_len).toCharArray();
		int intVariable = 0;
		String s1 = null;
		for(int i=0;i<tempchar.length && intVariable<=maxlength;i++){
			s1 = String.valueOf(tempchar[i]);
			intVariable += s1.getBytes().length;
		}
		s1= null;
		tempchar = null;
		return intVariable;
	}
	/**
	 * 截取指定长度的字符串,基于bytes,即是中文的长度为2,英文为1
	 * @param str 被处理字符串
	 * @param targetCount 截取长度
	 * @param more 后缀字符串
	 * @author Strong Yuan
	 * @version 1.1
	 * @return
	 */
	public static String subContentStringOrialBytes(String str, int targetCount,String more)
	{
		if (str == null)
			return "";
		int initVariable = 0;
		StringBuffer restr = new StringBuffer();
		if (getStringByteLength(str,targetCount) <= targetCount)
			return str;

		String s1=null;
		byte[] b;
		char[] tempchar = str.toCharArray();
		for (int i = 0; (i < tempchar.length && targetCount > initVariable); i++) {
			s1 = String.valueOf(tempchar[i]);
			b = s1.getBytes();
			initVariable += b.length;
			restr.append(tempchar[i]);
		}

		if (targetCount == initVariable || (targetCount == initVariable - 1)) {
			restr.append(more);
		}
		b = null;
		tempchar = null;
		s1=null;
		return restr.toString();
	}
	public static String subContentStringOrialBytes(String str,int start_point, int targetCount,String more)
	{
		if (str == null)
			return "";
		int initVariable = 0;
		StringBuffer restr = new StringBuffer();
		if (getStringByteLength(str,targetCount) <= targetCount)
			return str;

		String s1=null;
		byte[] b;
		char[] tempchar = str.toCharArray();
		for (int i = start_point; (i < tempchar.length && targetCount > initVariable); i++) {
			s1 = String.valueOf(tempchar[i]);
			b = s1.getBytes();
			initVariable += b.length;
			restr.append(tempchar[i]);
		}

		if (targetCount == initVariable || (targetCount == initVariable - 1)) {
			restr.append(more);
		}

		b = null;
		tempchar = null;
		s1=null;
		return restr.toString();
	}
	/**
	 * 截取指定长度的字符串,存在问题,但效率会高一点点.just a little
	 * @param str 被处理字符串
	 * @param targetCount 截取长度
	 * @param more 后缀字符串
	 * @version 1.1
	 * @author Strong Yuan
	 * @return String
	 */
	public static String subContentStringOrial(String str,int targetCount){
		return subContentStringOrial(str,targetCount,"...");
	}
	/**
	 * 截取指定长度的字符串,存在问题,但效率会高一点点.just a little
	 * @param str 被处理字符串
	 * @param targetCount 截取长度
	 * @param more 后缀字符串
	 * @author Strong Yuan
	 * @return String
	 */
	public static String subContentStringOrial(String str, int targetCount,String more)
	{
		if (str == null)
			return "";
		int initVariable = 0;
		StringBuffer restr = new StringBuffer();
		if (str.length() <= targetCount)
			return str;

		String s1=null;
		byte[] b;
		char[] tempchar = str.toCharArray();
		for (int i = 0; (i < tempchar.length && targetCount > initVariable); i++) {
			s1 = String.valueOf(tempchar[i]);
			b = s1.getBytes();
			initVariable += b.length;
			restr.append(tempchar[i]);
		}

		if (targetCount == initVariable || (targetCount == initVariable - 1)) {
			restr.append(more);
		}
		b = null;
		tempchar = null;
		s1=null;
		return restr.toString();
	}

	/***
	 * 格式化字符串长度，超出部分显示省略号,区分汉字跟字母。汉字2个字节，字母数字一个字节
	 * 截取指定文章内容
	 * @param str
	 * @param n
	 * @author Strong Yuan
	 * @return String
	 */
	public static String subContentChineseOrial(String str,int n){
		if(str.length()<n)
			return str;
		String temp= "";

		int t=0;
		char[] tempChar=str.toCharArray();
		for(int i=0;i<tempChar.length&&t<n;i++){

			if((int)tempChar[i]>=0x4E00 && (int)tempChar[i]<=0x9FA5){//是否汉字
				temp+=tempChar[i];
				t+=2;
			}else{
				temp+=tempChar[i];
				t++;
			}

		}
		tempChar = null;
		return (temp+"...");
	}
	/**
	 * @param txt
	 * @return
	 */
	public static String filterbr(String txt){
		return txt==null?null:txt.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
	}
	/**
	 * @param txt
	 * @return
	 */
	public static String decfilterbr(String txt){
		return txt==null?null:txt.replaceAll("<br>|<br/>|<BR>|<BR/>", "\n");
	}
	/**
	 * 转换><\"等符号
	 * @deprecated
	 * @param message
	 * @return
	 */
	public static String filter(String message) {

		if (message == null)
			return (null);

		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());

	}
	/**
	 * @deprecated
	 * 过滤掉HTML,性能问题.被取消使用
	 * @param initString
	 * @param length
	 * @param skidbr
	 * @return
	 */
	public static String filter(String initString,int length,boolean skidbr)
	{
		if(initString==null)
			return null;
		String resultStr = "";
		char[] str_chars = initString.toCharArray();
		String temp_str = "";
		int cur_pos = 0;
		int temp_len = 0;//记录从<开始的字符长度
		boolean is_start_map = false; //是否开始匹配
		//boolean is_start_filter = false;//是否开始过滤
		int blank_len = 0; //记录空格的长度
		int i = 0;
		for( i=0;i<str_chars.length&&resultStr.length() <length + blank_len ;i++)
		{
			if(str_chars[i]=='\n' || str_chars[i]=='\t' || str_chars[i] == '\r')
				continue;
			if(str_chars[i]!='<' && str_chars[i]!='&' && !is_start_map )
			{
				if(str_chars[i]==' ' )
				{
					blank_len++;
				}

				resultStr += str_chars[i];
				cur_pos ++;
				//is_start_filter = false;
				is_start_map = false;
				temp_len = 0;
			}
			else
			{
//				is_start_map = true;
//				is_start_filter = true;
				if((str_chars[i]!='>' && str_chars[i]!=' ' )&&temp_len < 20)
				{
					temp_str +=str_chars[i];
					temp_len ++;
					is_start_map = true;
				}
				if(str_chars[i]=='>')
				{
					//整句已完成，直接进行下一步循环
					if(skidbr)
					{
						//跳过br语句
						if("<br".equalsIgnoreCase(temp_str))
							resultStr += "<br>";
						if("<p".equalsIgnoreCase(temp_str))
							resultStr += "<p>";
						if("</p".equalsIgnoreCase(temp_str))
							resultStr += "</p>";
					}
					temp_str="";
					temp_len = 0;
					is_start_map = false;
				}
				if(str_chars[i]==';')
				{
					if(filter_line_key.indexOf(temp_str.toLowerCase())>0)
					{
						temp_str = "";
						temp_len = 0;
						is_start_map = false;
						continue;
					}
					if("&nbsp;".equals(temp_str))
					{
						resultStr +=" ";
					}
					temp_str="";
					is_start_map = false;
					temp_len = 0;
					continue;
				}
				int l_pos = -1;
				int b_pos = -1;
				if(str_chars[i]==' ' && temp_len<20)
				{
					blank_len++;
					l_pos = filter_line_key.indexOf(temp_str.toLowerCase());
					b_pos = filter_block_key.indexOf(temp_str.toLowerCase() );
					if(l_pos==-1 && b_pos==-1)
					{
						//未找到匹配的，接下来再找
						resultStr += temp_str;
						temp_str="";
						temp_len = 0;
						is_start_map = false;
						continue;
					}
					is_start_map = false;
				}
				if(l_pos>=0)
				{
					//已找到
					int t_pos = initString.indexOf(">",i);
					if(t_pos>=0)
					{
						i=t_pos;
						//is_start_filter = false;
						is_start_map = false;
						temp_str="";
						temp_len  =0;
						continue;
					}
				}
				else if(b_pos>=0)
				{
					String t_str = temp_str.replaceAll("<","</");
					int t_pos = initString.indexOf(t_str.trim());
					if(t_pos>=0)
					{
						i=t_pos+t_str.length();
						//is_start_filter = true;
						is_start_map = false;
						temp_str="";
						temp_len = 0;
						continue;
					}
				}
			}
		}
		if(length!=-1 && resultStr!=null && i<str_chars.length)//resultStr.length() > length + blank_len)
			resultStr+="...";

		return resultStr;

	}
	/**
	 * @deprecated
	 * 过滤html代码，默认不过滤BR语句,性能问题.被取消使用
	 * @param initString 需要过滤的字符串
	 * @param length 过滤后返回的字符串长度，－1则将所得的字符串全部返回
	 * @return
	 */
	public static String filter(String initString,int length)
	{
		return filter(initString,length,true);
	}

	/**
	 * 将给定的字符串中的换行符过滤掉
	 * @param initString　原始字符串
	 * @return
	 */
	public static String filterNewLine(String initString)
	{
		char[] str_chars = initString.toCharArray();
		StringBuffer str_buf = new StringBuffer();
		for(int i=0;i<str_chars.length;i++)
		{
			if (str_chars[i] == '\n' || str_chars[i] == '\t' || str_chars[i] == '\r')
				continue;
			if(str_chars[i]=='\'')
			{
				str_buf.append("`");
				continue;
			}

			str_buf.append(str_chars[i]);
		}
		str_chars = null;
		return str_buf.toString();
	}

	public static void main(String[] args){
		String str =null;
			String t = null;
		long now = System.currentTimeMillis();
		for(int i=0;i<10000;i++)
			t = Html2Text(str);
		System.out.println("cost:" + (System.currentTimeMillis()-now));
		System.out.println(t);
		now = System.currentTimeMillis();
		for(int i=0;i<10000;i++)
			t = doFilterAllHtmlTag(str);
		System.out.println("cost:" + (System.currentTimeMillis()-now));
		System.out.println(t);
	}
}
