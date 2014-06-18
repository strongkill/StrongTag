package net.strong.exutil;


import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.strong.bean.Config;
import net.strong.bean.Constants;
import net.strong.bean.ProConstants;
import net.strong.hibernate.HibernateSessionFactory;
import net.strong.util.MyUtil;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
/**
 * 初始化WEB APPLICATION所使用的环境，包括连接池的ALIAS，数据库的类型
 *
 * <p>Title: InitProxoolConnectionProvider</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  Strong Software International CO,.LTD qt </p>
 * @since 2007-6-16
 * @author Strong Yuan
 * @version 1.0
 * 	web.xml配置
		<servlet>
		<servlet-name>InitProvider</servlet-name>
		<servlet-class>net.strong.exutil.InitProvider</servlet-class>
		<init-param>
			<param-name>initProxool</param-name>
			<param-value>true</param-value>
		</init-param>
		<init-param>
			<param-name>Proxool_config</param-name>
			<param-value>/WEB-INF/classes/proxool.xml</param-value>
		</init-param>
		<init-param>
			<param-name>proxool_alias</param-name>
			<param-value>pool_blog</param-value>
		</init-param>
		<init-param>
			<param-name>proxool_alias_pools</param-name>
			<param-value></param-value>
		</init-param>
		<init-param>
			<param-name>memcachedClient</param-name>
			<param-value>192.168.3.123:11211 192.168.3.123:11212 192.168.3.123:11213</param-value>
		</init-param>
		<init-param>
			<param-name>isCached</param-name>
			<param-value>true</param-value>
		</init-param>
		<init-param>
			<param-name>memcachedTimeOutSecond</param-name>
			<param-value>30</param-value>
		</init-param>
		<init-param>
			<param-name>initHibernate</param-name>
			<param-value>false</param-value><!-- 在Spring已经初始化，不需要重复初始化 -->
		</init-param>
		<init-param>
			<param-name>watermark_path</param-name>
			<param-value>D:/workspace/blog/web/images/classnotes_sy.png</param-value>
		</init-param>
		<init-param>
			<param-name>real_upload_path</param-name>
			<param-value>album</param-value>
		</init-param>
		<init-param>
			<param-name>DeniedExtensionsFile</param-name>
			<param-value>PHP|PHP3|PHP5|PHTML|ASP|ASPX|ASCX|JSP|CFM|CFC|PL|BAT|EXE|DLL|REG|CGI|php|php3|php5|phtml|asp|aspx|ascx|jsp|cfm|cfc|pl|bat|exe|dll|reg|cgi</param-value>
		</init-param>
		<init-param>
			<param-name>AllowedExtensionsFile</param-name>
			<param-value>JPG|GIF|JPEG|PNG|BMP|SWF|FLA|PPT|jpg|gif|jpeg|png|bmp|swf|fla|ppt</param-value>
		</init-param>
		<init-param>
			<param-name>AllowedExtensionsVideo</param-name>
			<param-value>asf|mp4|mov|mpg|3gp|mpeg|wmv|avi|rm|rmvb|flv|ppt|ASF|MP4|MOV|MPG|3GP|MPEG|WMV|AVI|RM|RMVB|FLV</param-value>
		</init-param>

		<init-param>
			<param-name>des_key</param-name>
			<param-value>!-@#$~%*^~?:</param-value>
		</init-param>
		<init-param>
			<param-name>maxsecurefilesize</param-name>
			<param-value>3101024</param-value>
		</init-param>
		<init-param>
			<param-name>DataType</param-name>
			<param-value>1</param-value><!-- 数据库类型，0-MS SQL,1 - MySQL , 2 - postgreSQL -->
		</init-param>
		<init-param>
			<param-name>smtpServer</param-name>
			<param-value>smtp.126.com</param-value>
		</init-param>
		<init-param>
			<param-name>smtpusername</param-name>
			<param-value>strongkill</param-value>
		</init-param>
		<init-param>
			<param-name>smtppassword</param-name>
			<param-value></param-value>
		</init-param>
		<init-param>
			<param-name>default_email</param-name>
			<param-value>strongkill@126.com</param-value>
		</init-param>
		<init-param>
			<param-name>DEFAULT_HOST</param-name>
			<param-value>1363.cn</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
 *
 *
 */
public class InitProvider extends HttpServlet {
	private static Logger log = Logger.getLogger("InitProvider");
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		getServletContext().removeAttribute(Constants.PROXOOL_ALIAS_NAME);
		super.destroy();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		String path = getServletConfig().getServletContext().getRealPath("/");
		/*File f = new File(path+"licenses");
		if(f.exists()){
			while(f)
		}
		f = null;*/
		String initproxool = getServletConfig().getInitParameter(Constants.SERVLET_INITPROXOOL)==null?Constants.DEFAULT_SERVLET_INITPROXOOL:getServletConfig().getInitParameter(Constants.SERVLET_INITPROXOOL);
		if("true".equalsIgnoreCase(initproxool)){

			try {
				//String path = getServletConfig().getServletContext().getRealPath("/");
				String server_proxool_config = getServletConfig().getInitParameter(Constants.SERVLET_PROXOOL_CONFIG);
				JAXPConfigurator.configure(path +( server_proxool_config==null?Constants.DEFAULT_SERVLET_PROXOOL_CONFIG:server_proxool_config),false);
			} catch (ProxoolException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}

		String initHibernate = getServletConfig().getInitParameter(Constants.SERVLET_INITHIBERNATE)==null?Constants.SERVLET_INITHIBERNATE:getServletConfig().getInitParameter(Constants.SERVLET_INITHIBERNATE);
		if("true".equalsIgnoreCase(initHibernate)){
			Session s = HibernateSessionFactory.getCurrentSession();
			s.close();
		}
		String des_key = getServletConfig().getInitParameter(Constants.SERVLET_DES_KEY_NAME)==null?Constants.DEFAULT_SERVLET_DES_KEY_VALUE:getServletConfig().getInitParameter(Constants.SERVLET_DES_KEY_NAME);

		String MAXSECUREFILESIZE = getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_MAXSECUREFILESIZE)==null?Constants.DEFAULT_SERVLET_MAXSECUREFILESIZE:getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_MAXSECUREFILESIZE);

		String real_path = getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_UPLOAD_REAL_PATH);
		ProConstants.setPHOTO_UPLOAD_DIR(real_path);
		real_path = getServletContext().getRealPath(real_path);
		if(real_path == null)
			real_path = Constants.DEFAULT_SERVLET_UPLOAD_REAL_PATH;
		ProConstants.setPHOTO_FULL_UPLOAD_DIR(real_path);

		String AllowedExtensionsFile = getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_ALLOWUPLOAD_FILE_EXT);
		if(AllowedExtensionsFile==null)
			AllowedExtensionsFile = Constants.DEFAULT_SERVLET_ALLOWUPLOAD_FILE_EXT;


		String DeniedExtensionsFile = getServletConfig().getInitParameter(Constants.SERVLET_PATAMETER_DENIEDUPLOAD_FILE_EXT);
		if(DeniedExtensionsFile==null)
			DeniedExtensionsFile = Constants.DEFAULT_SERVLET_DENIEDUPLOAD_FILE_EXT;

		String AllowedExtensionsVideo = getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_ALLOWVIDEOUPLOAD_FILE_EXT);
		if(AllowedExtensionsVideo==null)
			AllowedExtensionsVideo = Constants.DEFAULT_SERVLET_ALLOWVIDEOUPLOAD_FILE_EXT;

		String smtpServer = getServletConfig().getInitParameter(Constants.SMTPSERVER);
		String smtpusername = getServletConfig().getInitParameter(Constants.SMTPUSER);
		String smtppassword = getServletConfig().getInitParameter(Constants.SMTPPASSWORD);
		String default_email = getServletConfig().getInitParameter(Constants.DEFAULT_EMAIL);
		String default_host = getServletConfig().getInitParameter(Constants.DEFAULT_HOST);

		String proxool_alias_pools = getServletConfig().getInitParameter(Constants.DEFAULT_ALIAS_POOLS);

		String memcached_pools = getServletConfig().getInitParameter(Constants.DEFAULT_MEMCACHED_POOLS);
		String memcachedflag = getServletConfig().getInitParameter(Constants.DEFAULT_MEMCACHED_FLAG);
		String memcachedTimeOutSecond = getServletConfig().getInitParameter(Constants.DEFAULT_MEMCACHED_TIMEOUT);

		String watermark_path = getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_WATERMARK_PATH);

		String proxool_alias = getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER);


		//System.out.println("Get Proxool 1: " + proxool_alias);

		String DataType =  getServletConfig().getInitParameter(Constants.SERVLET_PARAMETER_DATATYPE);
		if(DataType ==  null || DataType.length()==0)
			DataType = Constants.DEFAULT_DATA_TYPE;
		if(proxool_alias==null || proxool_alias.length()==0)
			proxool_alias=Constants.DEFAULT_ALIAS_NAME;
		//System.out.println("Get Proxool 2: " + proxool_alias);
		Config config = new Config();

		if(proxool_alias_pools!=null && proxool_alias_pools.length()>0){
			ArrayList<String> tmp = MyUtil.stringToArrayList(proxool_alias_pools,",");
			config.setProxool_pool(tmp);
		}
		config.setisCached("true".equalsIgnoreCase(memcachedflag));

		if(memcached_pools!=null && memcached_pools.length()>10){
			memcached_pools= memcached_pools.replaceAll(",", " ");
			config.setMemcached_pools(memcached_pools);
		}

		if(memcachedTimeOutSecond!=null && memcachedTimeOutSecond.length()>0){
			config.setMemcachedTimeOutSecond(Integer.parseInt(memcachedTimeOutSecond));
		}

		config.setDataType(Integer.parseInt(DataType));
		config.setProxool_alias_name(Constants.PORXOOL_ALIAS_PERFIX+proxool_alias);
		config.setDES_KEY(des_key);
		config.setMaxsecurefilesize(MAXSECUREFILESIZE);
		config.setReal_path(real_path);
		config.setWatermark_path(watermark_path);

		if(smtppassword!=null && smtppassword.length()>0)
			config.setSmtppassword(smtppassword);
		if(smtpServer!=null && smtpServer.length()>0)
			config.setSmtpServer(smtpServer);
		if(smtpusername!=null && smtpusername.length()>0)
			config.setSmtpusername(smtpusername);
		if(default_email!=null && default_email.length()>0)
			config.setDefault_email(default_email);
		if(default_host!=null && default_host.length()>0)
			ProConstants.setDEFAULT_HOST(default_host);

		config.setDeniedExtensions(MyUtil.stringToArrayList(DeniedExtensionsFile));

		config.setAllowedExtensions(MyUtil.stringToArrayList(AllowedExtensionsFile));
		config.setAllowExtensionsVideo(MyUtil.stringToArrayList(AllowedExtensionsVideo));

		getServletContext().setAttribute(Constants.PROXOOL_ALIAS_NAME, config);
		//ProConstants.setCfg(config);
	}

}
