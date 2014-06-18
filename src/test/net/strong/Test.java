package test.net.strong;

import net.strong.dao.Dao;
import net.strong.dao.impl.NutDao;


public class Test {

	public static void main(String[] args){
		String str_sql = "SELECT res.truename, res.member_id, res.article_id, res.article_subject  FROM (SELECT res.truename, res.member_id, res.article_id, res.article_subject  FROM (SELECT   m.truename, a.member_id, a.article_id, a.article_subject            FROM articles a, MEMBER m           WHERE a.member_id = m.member_id             AND a.checked = 1             AND m.member_type = 0             AND a.cr_date > SYSDATE - 7        ORDER BY a.article_id DESC) res WHERE ROWNUM <= ?)re where rownum>?";
		String sqlkey = null;
		long cur = System.currentTimeMillis();
		for (int i=0;i<10000;i++){
			if(str_sql!=null && str_sql.length()>0){}
		sqlkey = str_sql.replaceFirst("\\?",String.valueOf(25 * (2 + 1))).replaceFirst("\\?", String.valueOf(25 * 2));
		}
		System.out.println("neet time:"+(System.currentTimeMillis()-cur));
		/*
		Connection con = null;

		try {
			System.out.println("初始化连接池.....");
			String pro_home_path = System.getProperty("user.dir");
			JAXPConfigurator.configure(pro_home_path + "/proxool.xml", false);
			System.out.println("连接池初始化成功.");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("初始化连接池失败:" + e.getMessage());
			System.exit(1);
		}

		try{
			System.out.println("试启动数据库连接池，加快连接速度用......");
			con = DriverManager.getConnection("proxool.instant_alias");
			System.out.println("启动数据库连接池成功....");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("试启动数据库连接池错误:" + e.getMessage());
			System.exit(1);
		} finally {
			try {
				if (!con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		try {


			Dao dao = new NutDao();
			Person p = new Person();
			p.setAge(10);
			p.setName("Strong Yuan");
			dao.insert(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ICacheManager<IMemcachedCache> manager;
		manager = CacheUtil.getCacheManager(IMemcachedCache.class,
				MemcachedCacheManager.class.getName());
		manager.start();
		try
		{
			IMemcachedCache cache = manager.getCache("mclient0");
			cache.put("key", "value");
			System.out.println(cache.get("key"));
			IMemcachedCache cache1 = manager.getCache("mclient1");
			System.out.println(cache1.get("key"));
			IMemcachedCache cache2 = manager.getCache("mclient2");
			System.out.println(cache2.get("key"));
		}
		finally{ manager.stop();}
		*/
	}

}
