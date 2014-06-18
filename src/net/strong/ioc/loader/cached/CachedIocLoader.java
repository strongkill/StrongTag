package net.strong.ioc.loader.cached;

import net.strong.ioc.IocLoader;

/**
 * 带缓存的IocLoader，不用考虑线程安全性
 * 
 */
public interface CachedIocLoader extends IocLoader {

	void clear();

}
