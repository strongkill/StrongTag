package net.strong.hibernate;

import java.sql.Connection;
import net.strong.bean.*;
import java.sql.DriverManager;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import org.apache.struts.action.ActionServlet;

import net.strong.util.Helper;
/**
 * <p>Title: ConnectionManager.java</p>
 * <p>Description: Development Lib For CRM</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  Strong Software International CO,.LTD   </p>
 * @Date 2007-3-19
 * @author Strong Yuan
 * @version 1.0
 */
public abstract class ConnectionManager {
	public boolean flag;
	public Object parameter;
	public Object parameter1;
	/*	public ActionServlet servlet;
		public PageContext pageContext;
	public Servlet servlet1;*/
	public ServletContext servletContext; 

	public ConnectionManager(){

	}
	public ConnectionManager(ServletContext servletContext){
		this.servletContext = servletContext;
	}
	public ConnectionManager(PageContext pageContext){
		this.servletContext = pageContext.getServletContext();
	}
	public ConnectionManager(ActionServlet servlet){
		this.servletContext = servlet.getServletContext();
	}
	public ConnectionManager(Servlet servlet1){
		this.servletContext = servlet1.getServletConfig().getServletContext();
	}
	public void release(){
		parameter = null;
		parameter1=null;
		//servlet =null;
		servletContext = null;
	}
	public  abstract void process(Connection con) throws Exception;

	public void execute() throws Exception   
	{
		Connection con = null;
		String proxool_name = Constants.getConfig(servletContext).getProxool_alias_name();
		
		con = DriverManager.getConnection(proxool_name);
		try {      
			con.setAutoCommit(false);
			process(con);
			con.commit();      
		} catch (Exception e) {    
			if (con != null) {    // 要判断   
				con.rollback();   
			}   
			e.printStackTrace();      
			throw e;      
		}finally{
			Helper.cleanup(con);
		}
	}
}
