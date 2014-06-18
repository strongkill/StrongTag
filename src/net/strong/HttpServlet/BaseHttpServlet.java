package net.strong.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.strong.bean.Constants;
import net.strong.mongodb.BaseMongodb;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BaseHttpServlet extends HttpServlet{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static ApplicationContext ctx = null;
	/**
	 * 查找对象
	 * @param beanName
	 * @return
	 */
	protected Object findBean(String beanName){
		if(ctx==null)
			ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return ctx.getBean(beanName);
	}
	/**
	 * 返回 DataSource
	 * @return dataSource
	 */
	protected DataSource getDataSource(){
		return (DataSource)findBean("dataSource");
	}
	/**
	 * 返回 HibernateTemplate
	 * @return HibernateTemplate
	 */
	protected HibernateTemplate getHibernateTemplate(){
		return (HibernateTemplate)findBean("hibernateTemplate");
	}
	/**
	 * 返回jdbcTemplate
	 * @return jdbcTemplate
	 */
	protected JdbcTemplate getJdbcTemplate(){
		return (JdbcTemplate)findBean("jdbcTemplate");
	}

	/**
	 * 返回mongodb的Service
	 * @return
	 */
	protected BaseMongodb getMongodbService(){
		return (BaseMongodb)findBean("MongodbService");
	}

	protected String getDES_KEY(){
		return Constants.getDes_key(getServletContext());
	}

	protected void OutPutJson(HttpServletResponse response,StringBuffer buf){
		OutPutJson(getOut(response),buf.toString());
	}
	protected void OutPutXml(HttpServletResponse response ,String xml){
		OutPutJson(getOutXml(response),xml);
	}
	protected void OutPutJson(HttpServletResponse response,String str){
		OutPutJson(getOut(response),str);
	}
	protected void OutPutJson(PrintWriter out,String json){
		out.println(json);
	}
	protected PrintWriter getOutXml(HttpServletResponse response){
		PrintWriter out = null;
		try {
			response.setContentType("text/xml; charset=UTF-8");
			out = response.getWriter();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return out;
	}
	protected PrintWriter getOut(HttpServletResponse response){
		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=UTF-8");
			out = response.getWriter();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return out;
	}
}
