package net.strong.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import net.strong.bean.Constants;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class MyUtil {
	public static int getRandomNumber(int length){
		Random rnd = new Random();
		return rnd.nextInt(length);		
	}
	
	public static boolean checkemail(String email){
		boolean valid= false;
		PatternCompiler compiler = new Perl5Compiler();
		PatternMatcher matcher = new Perl5Matcher();
		String email_regexp = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
		try {
			Pattern p = compiler.compile(email_regexp);
			valid = matcher.contains(email,p);
				 
		} catch (MalformedPatternException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		compiler=null;
		matcher = null;
		return valid;
	}
	
	public static String getfilenameFromUrl(String url){
		if(url==null)return null;
		if(url.length()==0)return url;
		int pos = url.lastIndexOf("/");
		if(pos==-1)return url;
		
		if(url.indexOf(".do")>-1)
			return "/index.jsp";
		
		return url.substring(pos, url.length());
	}
	public static ArrayList<String> stringToArrayList(String str,String regexp) {

		String[] strArr=str.split(regexp);

		ArrayList<String> tmp=new ArrayList<String>();
		if(str.length()>0) {
			for(int i=0;i<strArr.length;++i) {
				tmp.add(strArr[i].toLowerCase());
			}
		}
		return tmp;
	}
	
	public static ArrayList<String> stringToArrayList(String str) {

		String[] strArr=str.split("\\|");

		ArrayList<String> tmp=new ArrayList<String>();
		if(str.length()>0) {
			for(int i=0;i<strArr.length;++i) {
				tmp.add(strArr[i].toLowerCase());
			}
		}
		return tmp;
	}
	
	public static void main(String[] args)
	{
		System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));	
	}
	public static String getFullDateTime(){
		String datestr =  "" ;
		try {
			java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;			
			datestr = df.format(new java.util.Date()) ;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return datestr ;		
	}
	
  public static String gettime() {
     String datestr =  "" ;
     try {
     java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH") ;
     datestr = df.format(new java.util.Date()) ;
     }
     catch (Exception ex) {

     }

     return datestr ;
   }

 public static String getHour() {
    String datestr =  "" ;
    try {
    java.text.DateFormat df = new java.text.SimpleDateFormat("H") ;
    datestr = df.format(new java.util.Date()) ;
    }
    catch (Exception ex) {

    }

    return datestr ;
  }

 public static String getMonth() {
    String datestr =  "" ;
    try {
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-M") ;
    datestr = df.format(new java.util.Date()) ;
    }
    catch (Exception ex) {

    }

    return datestr ;
  }

 public static String getStrMonth() {
    String datestr =  "" ;
    try {
    java.text.DateFormat df = new java.text.SimpleDateFormat("M") ;
    datestr = df.format(new java.util.Date()) ;
    }
    catch (Exception ex) {

    }

    return datestr ;
  }

 public static String getStrDay() {
    String datestr =  "" ;
    try {
    java.text.DateFormat df = new java.text.SimpleDateFormat("d") ;
    datestr = df.format(new java.util.Date()) ;
    }
    catch (Exception ex) {

    }

    return datestr ;
  }


 public static String getDay() {
    String datestr =  "" ;
    try {
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-M-d") ;
    datestr = df.format(new java.util.Date()) ;
    }
    catch (Exception ex) {

    }

    return datestr ;
  }

 public static String getWeek() {
    String datestr =  "" ;
    try {
    java.text.DateFormat df = new java.text.SimpleDateFormat("E") ;
    datestr = df.format(new java.util.Date()) ;
     }
    catch (Exception ex) {

    }

    return datestr ;
  }
  public static int getStringToInt(String str,int defalut_value){
    int result = defalut_value;
    try
    {
      result = Integer.parseInt(str.trim());
    }
    catch(Exception e)
    {
      result = defalut_value;
    }
    return result;
  }
  public static long getStringToLong(String str,long default_value)
  {
    if(!isNumber(str))
    {
      //当给定的字符串不是数字时，返回缺省值
      return default_value;
    }

    long result = default_value;
    try
    {
      result = Long.valueOf(str.trim()).longValue();
    }
    catch(Exception e)
    {
      result = default_value;
    }
    return result;
  }

  /**
   * 判断给定的一个字符串是否为数字（含小数点）
   * @param str
   * @return
   */
  public static boolean isNumber(String str)
  {
    if(str==null)
      return false;

    boolean result = true;
    char [] t_char = str.toCharArray();
    for(int i=0;i<t_char.length ;i++)
    {
      char t = t_char[i];
      if((t>='0' && t<='9') || t=='.')
      {
        continue;
      }
      else
      {
        result = false;
      }
    }
    return result;
  }
	public static String md5(String str){
		MD5 md =new MD5();
		str = md.getMD5ofStr(str);
		md = null;
		return str;
	}
	public static boolean videoExtIsAllowed(ServletContext context,String filename){
		ArrayList<String> l = Constants.getConfig(context).getAllowExtensionsVideo();
		return l.contains(getExtension(filename));
	}
	public static boolean videoExtIsAllowed(Servlet servlet,String filename){
		return getAllowVideoExtList(servlet).contains(getExtension(filename));
	}

	public static ArrayList<String> getAllowVideoExtList(Servlet servlet){
		return Constants.getConfig(servlet).getAllowExtensionsVideo();
	}
	public static String getExtension(String fileName) {
		String tmp =fileName.substring(fileName.lastIndexOf(".")+1); 
		return tmp.toLowerCase();
	}
	public static String getExtensionincludedot(String fileName){
		String tmp =fileName.substring(fileName.lastIndexOf(".")); 
		return tmp.toLowerCase();
	}
	public static ArrayList<String> getAllowList(Servlet servlet){
		return Constants.getConfig(servlet).getAllowedExtensions();
	}
	public static ArrayList<String> getDenyList(Servlet servlet){
		return Constants.getConfig(servlet).getDeniedExtensions();
	}
	public static boolean extIsAllowed(String filename,Servlet servlet){
		return extIsAllowed(getAllowList(servlet),filename);
	}
	public static boolean extIsAllowed(ArrayList<String> allowlist,String filename){
		String ext = getExtension(filename);
		ext = ext.toLowerCase();
		return allowlist.contains(ext);
	}
	public static boolean extIsAllowed(Servlet servlet,String ext) {

		ext=ext.toLowerCase();

		ArrayList<String> allowList=getAllowList(servlet);//Constants.getConfig(servlet).getAllowedExtensions();
		ArrayList<String> denyList=getDenyList(servlet);//Constants.getConfig(servlet).getDeniedExtensions();
		if(allowList.size()==0)
			return false;
		if(denyList.size()==0)
			return true;

		if(allowList.contains(ext)){
			return true;
		}

		if(denyList.contains(ext)){
			return false;
		}

		return false;
	}		
}