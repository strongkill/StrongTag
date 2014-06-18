package net.strong.dbcached;

import com.alisoft.xplatform.asf.cache.ICache;
import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
/**
 * memcached介面類
 * @author Strong Yuan
 * @Date Mar 14, 2009
 * @Version
 * @deprecated
 */
public class DBcached {
	ICacheManager<IMemcachedCache> manager= CacheUtil.getCacheManager(IMemcachedCache.class,MemcachedCacheManager.class.getName());
	private static String cacheServerName = "mclient0";
	private static DBcached instance = null;

	protected void finalize() throws Throwable {
		manager.stop();
		super.finalize();
	}
	/**
	 * 返回DBcached
	 * @return
	 */
	public static DBcached getInstance() {
		if (instance == null) {
			instance = new DBcached();
		}
		return instance;
	}
	/**
	 * 返回指定的DBCACHED
	 * @param currentCachedServerName
	 * @return
	 */
	public static DBcached getInstance(String currentCachedServerName) {
		cacheServerName = currentCachedServerName;
		return getInstance();
	}	
	/**
	 * 返回Memcached
	 * @return
	 */
	public ICache getServerCache(){
		return manager.getCache(cacheServerName);
	}
	/**
	 * 初始化時獲取一個manager單例
	 */
	private DBcached(){
		//manager = CacheUtil.getCacheManager(IMemcachedCache.class,MemcachedCacheManager.class.getName());
		manager.start();
	}
	/**
	 * 設置緩存物件,默認保存60秒
	 * @param key Key
	 * @param obj 物件
	 */
	public void addObject(String key,Object obj){
		addObject(key, obj,60);
	}
	/**
	 * 設置緩存物件
	 * @param key KEY
	 * @param obj 物件
	 * @param timeoutsecond 超時時間(秒)
	 */
	public void addObject(String key,Object obj,int timeoutsecond){
		try{
			//manager.start();
			getServerCache().put(key, obj,timeoutsecond);
		}finally{
			//manager.stop();
		}
	}
	/**
	 * 獲取緩存物件
	 * @param key
	 * @return
	 */
	public Object getObject(String key){	
		Object tmp = null;
		try{
			//manager.start();
			tmp = getServerCache().get(key);
		}finally{
			//manager.stop();
		}
		return tmp;
	}
	public void RemoveObject(String key){
		try{
			//manager.start();
			getServerCache().remove(key);
		}finally{
			//manager.stop();
		}
	}
	
	
	public static void main(String[] args) {
		String aa = "CALL prc_page_result(0,\"announcement_id,announcement_subject,cr_date\",\"announcement\",\"1=1 \",\"announcement_id\",0,\"announcement_id\",10)";
		getInstance().getServerCache().put(aa, "Strong",10000);
		//for(int i=0;i<10;i++)
			System.out.println(getInstance().getServerCache().get(aa));
		/*
		ICacheManager<IMemcachedCache> manager;
		manager = CacheUtil.getCacheManager(IMemcachedCache.class,
				MemcachedCacheManager.class.getName());
		manager.start();
		try
		{	
			//manager.start();
			IMemcachedCache cache = manager.getCache("mclient0");
			//cache.put("key", "value");
			//cache.remove("key");
			System.out.println(cache.get("key"));
			
			IMemcachedCache cache1 = manager.getCache("mclient0");
			System.out.println(cache1.get("key"));
			IMemcachedCache cache2 = manager.getCache("mclient0");
			System.out.println(cache2.get("key"));
			
		}
		finally{ manager.stop();}*/
	}

}
