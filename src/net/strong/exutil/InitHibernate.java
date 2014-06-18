package net.strong.exutil;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class InitHibernate extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html; charset=GBK";
  private Context ctx;
  private SessionFactory sessionFactory;

  //Initialize global variables
  public void InitHibernate(){
    //
  }
  public void init() throws ServletException {

    try {
            // 获取SessionFactory的实例
            String path = getServletContext().getRealPath("/WEB-INF/classes/");
            xmlConstant xml = new xmlConstant(path,"pro-config.xml",false);
            String hi_name = xml.getString("hibernateName");
            sessionFactory =
                    new Configuration().configure(hi_name).buildSessionFactory();

    } catch (HibernateException ex) {
            throw new RuntimeException(
                    "Exception building SessionFactory: " + ex.getMessage(),
                    ex);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }

    try {
            // 取得容器上下文
            ctx = new InitialContext();
            // 将sessionFactory bind到JND树中
            ctx.bind("HibernateSessionFactory", sessionFactory);
    } catch (NamingException ex) {
            throw new RuntimeException(
                    "Exception binding SessionFactory to JNDI: " + ex.getMessage(),
                    ex);
    }

  }

  //Clean up resources
  public void destroy() {
    if (ctx != null) {
            try {
                    // unbind JNDI 节点
                    ctx.unbind("HibernateSessionFactory");
            } catch (NamingException e) {
                    e.printStackTrace();
            }
    }
    if (sessionFactory != null) {
            try {
                    // 关闭sessionFactory
                    sessionFactory.close();
            } catch (HibernateException e) {
                    e.printStackTrace();
            }
            sessionFactory = null;
    }
  }
}
