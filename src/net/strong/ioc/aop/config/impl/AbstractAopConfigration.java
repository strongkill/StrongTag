package net.strong.ioc.aop.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.strong.aop.MethodInterceptor;
import net.strong.aop.matcher.MethodMatcherFactory;
import net.strong.ioc.Ioc;
import net.strong.ioc.aop.config.AopConfigration;
import net.strong.ioc.aop.config.InterceptorPair;
import net.strong.lang.Lang;

/**
 * 
 * 
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public abstract class AbstractAopConfigration implements AopConfigration {

	private List<AopConfigrationItem> aopItemList;

	public List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz) {
		List<InterceptorPair> ipList = new ArrayList<InterceptorPair>();
		for (AopConfigrationItem aopItem : aopItemList) {
			if (aopItem.matchClassName(clazz.getName()))
				ipList.add(new InterceptorPair(	getMethodInterceptor(	ioc,
																		aopItem.getInterceptor(),
																		aopItem.isSingleton()),
												MethodMatcherFactory.matcher(aopItem.getMethodName())));
		}
		return ipList;
	}

	public void setAopItemList(List<AopConfigrationItem> aopItemList) {
		this.aopItemList = aopItemList;
	}

	protected MethodInterceptor getMethodInterceptor(	Ioc ioc,
														String interceptorName,
														boolean singleton) {
		if (interceptorName.startsWith("ioc:"))
			return ioc.get(MethodInterceptor.class, interceptorName.substring(4));
		try {
			if (singleton == false)
				return (MethodInterceptor) Lang.loadClass(interceptorName).newInstance();
			MethodInterceptor methodInterceptor = cachedMethodInterceptor.get(interceptorName);
			if (methodInterceptor == null) {
				methodInterceptor = (MethodInterceptor) Class	.forName(interceptorName)
																.newInstance();
				cachedMethodInterceptor.put(interceptorName, methodInterceptor);
			}
			return methodInterceptor;
		}
		catch (Throwable e) {
			throw Lang.wrapThrow(e);
		}
	}

	private HashMap<String, MethodInterceptor> cachedMethodInterceptor = new HashMap<String, MethodInterceptor>();

}
