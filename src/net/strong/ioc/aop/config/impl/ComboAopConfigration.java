package net.strong.ioc.aop.config.impl;

import java.util.ArrayList;
import java.util.List;

import net.strong.ioc.Ioc;
import net.strong.ioc.aop.config.AopConfigration;
import net.strong.ioc.aop.config.InterceptorPair;

/**
 * 整合多种AopConfigration
 * 
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class ComboAopConfigration implements AopConfigration {

	private List<AopConfigration> aopConfigrations;

	public List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz) {
		List<InterceptorPair> interceptorPairs = new ArrayList<InterceptorPair>();
		for (AopConfigration aopConfigration : aopConfigrations) {
			List<InterceptorPair> ipList = aopConfigration.getInterceptorPairList(ioc, clazz);
			if (ipList != null && ipList.size() > 0)
				interceptorPairs.addAll(ipList);
		}
		return interceptorPairs;
	}

	public void setAopConfigrations(List<AopConfigration> aopConfigrations) {
		this.aopConfigrations = aopConfigrations;
	}
}
