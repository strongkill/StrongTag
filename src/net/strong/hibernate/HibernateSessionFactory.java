package net.strong.hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 初始化hibernateSessionFactory 
 * <p>Title: HibernateSessionFactory.java</p>
 * <p>Description: Development Lib For CRM</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  Strong Software International CO,.LTD   </p>
 * @Date 2007-3-21
 * @author Strong Yuan
 * @version 1.0
 */
public class HibernateSessionFactory {
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private  static Configuration configuration = new Configuration();
	private static SessionFactory sessionFactory;
	private static String configFile = CONFIG_FILE_LOCATION;
/*	static {   
		try {   
			sessionFactory = new Configuration().configure(configFile).buildSessionFactory();   
		} catch (HibernateException ex) {   
			throw new RuntimeException("Exception building SessionFactory: " + ex.getMessage(), ex);   
		}   
	}*/ 
	private HibernateSessionFactory() {
	}

	/**
	 * Returns the ThreadLocal Session instance.  Lazy initialize
	 * the <code>SessionFactory</code> if needed.
	 *
	 *  @return Session
	 *  @throws HibernateException
	 */
	public static Session getCurrentSession() throws HibernateException {
		Session session = (Session) threadLocal.get();

		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
		}

		return session;
	}
	public static Session getCurrentSession(String config_file) throws HibernateException{
		Session session = (Session) threadLocal.get();

		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				CONFIG_FILE_LOCATION = config_file;
				rebuildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
		}

		return session;
	}
	/**
	 *  Rebuild hibernate session factory
	 *
	 */
	public static void rebuildSessionFactory() {
		try {
//			System.out.println("RebuildSessionFactory : "+System.currentTimeMillis());
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
			//sessionFactory = new Configuration().configure().buildSessionFactory();   

		} catch (Exception e) {
			System.err
			.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	/**
	 *  Close the single hibernate session instance.
	 *
	 *  @throws HibernateException
	 */
	public static void closeCurrentSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);

		if (session != null) {
			session.close();
		}
	}

	/**
	 *  return session factory
	 *
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessiionFactory(SessionFactory springsessionfactory){
		sessionFactory = springsessionfactory;
	}
	/**
	 *  return session factory
	 *
	 *	session factory will be rebuilded in the next call
	 */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
	 *  return hibernate configuration
	 *
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}


}