package test;
import java.util.HashMap;

import net.strong.dbcached.DBcached;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

public class TestMemcached {
	private static String key_name ="select name,adbaf,aasdf,asd,fas,dfas,dfasdf,asdf from member where aandfadasdf'asdf'asdf;";

	public static void main(String[] args) {

		ICacheManager<IMemcachedCache> manager;
		manager = CacheUtil.getCacheManager(IMemcachedCache.class,
				MemcachedCacheManager.class.getName());
		manager.start();
		try
		{	
			//manager.start();
			IMemcachedCache cache = manager.getCache("mclient0");
			//cache.put("key", "value");
			cache.remove("key");
			System.out.println(cache.get("key"));
			
			IMemcachedCache cache1 = manager.getCache("mclient0");
			System.out.println(cache1.get("key"));
			IMemcachedCache cache2 = manager.getCache("mclient0");
			System.out.println(cache2.get("key"));
			
		}
		finally{ manager.stop();}
	}

	/**
	 * @param args
	 */
	public static void main2(String[] args) {
		
		//DBcached.getInstance("mclient1").addCache(key_name,"String Yuan",3600);
		//DBcached.getInstance("mclient1").delCache(key_name);
		//System.out.println(DBcached.getInstance("mclient0").getCache(key_name));
		//System.out.println(DBcached.getInstance("mclient1").getCache(key_name));
		//System.out.println(DBcached.getInstance("mclient2").getCache(key_name));
		
		System.exit(0);
	}

	public static void main1(String[] args) {
		ICacheManager<IMemcachedCache> manager;
		manager = CacheUtil.getCacheManager(IMemcachedCache.class,
				MemcachedCacheManager.class.getName());

		try
		{
			manager.start();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		try

		{
			IMemcachedCache cache = manager.getCache("mclient0");

			/*IMemcachedCache cache1 = manager.getCache("mclient1");
			IMemcachedCache cache2 = manager.getCache("mclient2");*/

			//cache.remove("key1");
			HashMap<String,String> a = new HashMap<String,String>();
			a.put("value","Strong Yuan");
			a.put("value1","Strong Yuan");
			a.put("value2","Strong Yuan");
			cache.put(key_name, a);

			//cache1.remove("key2");
			//cache1.put("key2","value2");

			//System.out.println(cache.get(key_name));

			/*
			System.out.println(cache1.get("key2"));
			System.out.println(cache2.get("key2"));	*/
			//cache.remove(key_name);
			/*
			cache1.remove("key2");*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}		
		manager.stop();
	}

}
