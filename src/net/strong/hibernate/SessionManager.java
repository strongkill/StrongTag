package net.strong.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * <p>Title: SessionManager 专们管理Hibernate Session</p>
 * <p>Description: Development Lib For CRM</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  Strong Software International CO,.LTD   </p>
 * @Date 2007-3-6
 * @author Strong Yuan
 * @version 1.0.1
 * @param <T>
 */
public abstract  class SessionManager<T> {
	/**
	 * 输入参数,如Form
	 */
	public T form = null;
	/**
	 * 返回参数,类型为定义时的T类型
	 */
	public T parameter = null;
	/**
	 * 通用返回参数,类型为Object
	 */
	public Object parameter1 = null;
	
	/**
	 * 列表返回参数
	 */
	public List<T> list = null;
	
	/**
	 * 返回参数true /false
	 */
	public boolean flag;
	/**
	 * 返回参数2 true /false
	 */	
	public boolean flag1;
	/**
	 * 核心处理逻辑方法
	 * @param session
	 * @throws Exception
	 */
	public  abstract void process(Session session) throws Exception;
	
	public SessionManager(){

	}
	
	public void release(){
		form=null;
		parameter=null;
		parameter1=null;
		if(list!=null){
			list.clear();
		}
		list = null;
			
	}
	/**
	 * 要保存或者处理的对象.必须是hibernate mapping Object
	 * @param form
	 */
	public SessionManager(T form){
		this.form = form;
	}
	/**
	 * 最后执行的方法,该方法先取得Hibernate Session,再调用process方法
	 * @throws Exception
	 */
	public /*synchronized*/ void execute() throws Exception   
	{
		Session session;
		Transaction trans=null;   
		session=HibernateSessionFactory.getCurrentSession();   
		try {      
			trans=session.beginTransaction();      
			process(session);      
			trans.commit();      
		} catch (Exception e) {    
			if (trans != null) {   
				trans.rollback();   
			}   
			e.printStackTrace();      
			throw e;      
		}finally{
			HibernateSessionFactory.closeCurrentSession();
		}
	}
}
