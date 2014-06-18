package net.strong.bean;

import java.util.ArrayList;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import net.strong.util.MyUtil;
/**
 * 
 * 
 * <p>Title: Constants</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  Strong Software International CO,.LTD qt </p>
 * @since 2007-6-16
 * @author Strong Yuan
 * @version 1.0
 */
public class Constants {
	private Constants(){ //禁止实例化

	}
	/**
	 * 数据库类型,只适用于QueryObject函数
	 */
	public static int database_type = -1;
	
	public static final String SERVLET_INITHIBERNATE = "initHibernate";
	public static final String DEFAULT_SERVLET_INITHIBERNATE = "false";	
	public static final String SERVLET_HIBERNATE_CONFIG = "Hibernate_config";
	public static final String DEFAULT_SERVLET_HIBERNAET_CONFIG = "/WEB-INF/classes/hibernate.cfg.xml";
	
	public static final String SERVLET_INITPROXOOL = "initProxool";
	public static final String DEFAULT_SERVLET_INITPROXOOL = "false";
	public static final String SERVLET_PROXOOL_CONFIG = "Proxool_config";
	public static final String DEFAULT_SERVLET_PROXOOL_CONFIG ="/WEB-INF/classes/proxool.xml";
	
	public static final String SERVLET_DES_KEY_NAME ="des_key";
	public static final String DEFAULT_SERVLET_DES_KEY_VALUE = "123456";
	
	public static final String SERVLET_PARAMETER = "proxool_alias";
	
	public static final String SERVLET_PARAMETER_DATATYPE = "DataType";
	
	
	
	public static final String PORXOOL_ALIAS_PERFIX = "proxool.";
	
	public static final String PROXOOL_ALIAS_NAME = "taglib_proxool_alias";
	
	public static final String DEFAULT_ALIAS_NAME = "proxool_alias";
	
	public static final String DEFAULT_ALIAS_POOLS = "proxool_alias_pools";
	
	public static final String DEFAULT_MEMCACHED_POOLS = "memcachedClient";
	public static final String DEFAULT_MEMCACHED_FLAG ="isCached";
	public static final String DEFAULT_MEMCACHED_TIMEOUT ="memcachedTimeOutSecond";
	
	
	public static final String DEFAULT_DATA_TYPE = "1";
	
	public static final String SMTPSERVER ="smtpServer";
	public static final String SMTPPASSWORD ="smtppassword";
	public static final String SMTPUSER = "smtpusername";
	public static final String DEFAULT_EMAIL ="default_email";
	
	public static final String DEFAULT_HOST="DEFAULT_HOST";
	
	public static final String SERVLET_PARAMETER_MAXSECUREFILESIZE = "MAXSECUREFILESIZE";
	public static final String DEFAULT_SERVLET_MAXSECUREFILESIZE = "3101024";
	
	public static final String SERVLET_PARAMETER_WATERMARK_PATH = "watermark_path";
	
	
	public static final String SERVLET_PARAMETER_UPLOAD_REAL_PATH = "real_upload_path";
	public static final String DEFAULT_SERVLET_UPLOAD_REAL_PATH = "/home1/web/blog/album/";
	
	public static final String SERVLET_PARAMETER_ALLOWUPLOAD_FILE_EXT ="AllowedExtensionsFile";
	public static final String DEFAULT_SERVLET_ALLOWUPLOAD_FILE_EXT = "JPG|GIF|JPEG|PNG|BMP|SWF|FLA|jpg|gif|jpeg|png|bmp|swf|fla";
	
	public static final String SERVLET_PATAMETER_DENIEDUPLOAD_FILE_EXT="DeniedExtensionsFile";
	public static final String DEFAULT_SERVLET_DENIEDUPLOAD_FILE_EXT = "PHP|PHP3|PHP5|PHTML|ASP|ASPX|ASCX|JSP|CFM|CFC|PL|BAT|EXE|DLL|REG|CGI|php|php3|php5|phtml|asp|aspx|ascx|jsp|cfm|cfc|pl|bat|exe|dll|reg|cgi";

	public static final String SERVLET_PARAMETER_ALLOWVIDEOUPLOAD_FILE_EXT = "AllowedExtensionsVideo";
	public static final String DEFAULT_SERVLET_ALLOWVIDEOUPLOAD_FILE_EXT = "mp4|mov|mpg|3gp|mpeg|wmv|avi|rm|rmvb|flv|MP4|MOV|MPG|3GP|MPEG|WMV|AVI|RM|RMVB|FLV";
	
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String LOGIN = "login";
	public static final String USER_KEY ="user";
	public static final String DATABASE_KEY = "database";
	
	public static Config getConfig(ServletContext servletcontext){
		
		Config config = (Config)servletcontext.getAttribute(Constants.PROXOOL_ALIAS_NAME);
		return config==null?new Config():config;
	}
	
	public static Config getConfig(Servlet servlet){
/*		
		Config config = (Config)servlet.getServletConfig().getServletContext().getAttribute(Constants.PROXOOL_ALIAS_NAME);
		return config==null?new Config():config;
*/
		Config config = getConfig(servlet.getServletConfig().getServletContext());
		return config==null?new Config():config;
	}
	public static Config getConfig(PageContext pageContext){
		Config config = getConfig(pageContext.getServletContext());//(Config)pageContext.getServletContext().getAttribute(Constants.PROXOOL_ALIAS_NAME);
		return config==null?new Config():config;
	}

	public static String getProxool_alias_name(Servlet servlet){
		return getConfig(servlet).getProxool_alias_name();
	}

	public static String getProxool_alias_name(PageContext pageContext){
		return getConfig(pageContext).getProxool_alias_name();
	}

	public static String getProxool_alias_name(ServletContext servletcontext){
		return getConfig(servletcontext).getProxool_alias_name();
	}
	
	public static int getDataType(Servlet servlet){
		return getConfig(servlet).getDataType();
	}
	public static int getDataType(ServletContext servletcontext){
		return getConfig(servletcontext).getDataType();
	}
	
	public static int getDataType(PageContext pageContext){
		return getConfig(pageContext).getDataType();
	}

	public static String getMAXSECUREFILESIZE(PageContext pageContext){
		return getConfig(pageContext).getMaxsecurefilesize();
	}
	public static String getMAXSECUREFILESIZE(Servlet servlet){
		return getConfig(servlet).getMaxsecurefilesize();
	}
	public static String getMAXSECUREFILESIZE(ServletContext servletcontext){
		return getConfig(servletcontext).getMaxsecurefilesize();
	}	
	
	public static String getDes_key(PageContext pageContext){
		return getConfig(pageContext).getDES_KEY();
	}
	public static String getDes_key(Servlet servlet){
		return getConfig(servlet).getDES_KEY();
	}
	public static String getDes_key(ServletContext servletcontext){
		return getConfig(servletcontext).getDES_KEY();
	}
	public static ArrayList<String> getProxool_Pools(ServletContext servletcontext){
		return getConfig(servletcontext).getProxool_pool();
	}		
	public static ArrayList<String> getProxool_Pools(PageContext pageContext){
		return getConfig(pageContext).getProxool_pool();
	}	
	public static ArrayList<String> getProxool_Pools(Servlet servlet){
		return getConfig(servlet).getProxool_pool();
	}
	
	public static String getProxool_ReadOnly_Pools(ServletContext servletcontext){
		ArrayList<String> pools = getConfig(servletcontext).getProxool_pool();
		if(pools.size()==0)
			return getProxool_alias_name(servletcontext);
		return Constants.PORXOOL_ALIAS_PERFIX+pools.get(MyUtil.getRandomNumber(pools.size()));
	}
	public static String getProxool_ReadOnly_Pools(PageContext pageContext){
		ArrayList<String> pools = getConfig(pageContext).getProxool_pool();
		if(pools.size()==0)
			return getProxool_alias_name(pageContext);
		String retval = Constants.PORXOOL_ALIAS_PERFIX + pools.get(MyUtil.getRandomNumber(pools.size()));
//		System.out.println("当前连接池是：" +retval);
		return retval;
	}
	public static String getProxool_ReadOnly_Pools(Servlet servlet){
		ArrayList<String> pools = getConfig(servlet).getProxool_pool();
		if(pools.size()==0)
			return getProxool_alias_name(servlet);
		return Constants.PORXOOL_ALIAS_PERFIX+ pools.get(MyUtil.getRandomNumber(pools.size()));
	}
	public static String getMemcachedPools(ServletContext servletcontext){
		return getConfig(servletcontext).getMemcached_pools();
	}
	public static String getMemcachedPools(PageContext pageContext){
		return getConfig(pageContext).getMemcached_pools();
	}
	public static String getMemcachedPools(Servlet servlet){
		return getConfig(servlet).getMemcached_pools();
	}
	
	public static int getMemcachedTimeOutSecond(ServletContext servletcontext){
		return getConfig(servletcontext).getMemcachedTimeOutSecond();
	}
	public static int getMemcachedTimeOutSecond(PageContext pageContext){
		return getConfig(pageContext).getMemcachedTimeOutSecond();
	}
	public static int getMemcachedTimeOutSecond(Servlet servlet){
		return getConfig(servlet).getMemcachedTimeOutSecond();
	}
}
