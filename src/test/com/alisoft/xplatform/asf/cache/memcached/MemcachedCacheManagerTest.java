package test.com.alisoft.xplatform.asf.cache.memcached;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

public class MemcachedCacheManagerTest
{
	ICacheManager<IMemcachedCache> manager;

	@Before
	public void setUp() throws Exception
	{
		manager = CacheUtil.getCacheManager(IMemcachedCache.class,
				MemcachedCacheManager.class.getName());
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testStartAndStop()
	{
		try
		{
			manager.start();
			manager.stop();
		}
		catch(Exception ex)
		{
			org.junit.Assert.assertFalse(true);
			ex.printStackTrace();
		}
	}

}
