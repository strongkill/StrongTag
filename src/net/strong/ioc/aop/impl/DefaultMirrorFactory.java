package net.strong.ioc.aop.impl;

import java.util.List;

import net.strong.aop.ClassAgent;
import net.strong.aop.ClassDefiner;
import net.strong.aop.DefaultClassDefiner;
import net.strong.aop.MethodInterceptor;
import net.strong.aop.asm.AsmClassAgent;
import net.strong.ioc.Ioc;
import net.strong.ioc.aop.MirrorFactory;
import net.strong.ioc.aop.config.AopConfigration;
import net.strong.ioc.aop.config.InterceptorPair;
import net.strong.ioc.aop.config.impl.AnnotationAopConfigration;
import net.strong.lang.Mirror;

/**
 * 通过AopConfigration来识别需要拦截的方法,并根据需要生成新的类
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author Wendal(wendal1985@gmail.com)
 */
public class DefaultMirrorFactory implements MirrorFactory {

	private Ioc ioc;

	private ClassDefiner cd;

	private AopConfigration aopConfigration;

	public DefaultMirrorFactory(Ioc ioc) {
		this.ioc = ioc;
		this.cd = new DefaultClassDefiner(getClass().getClassLoader());
	}

	@SuppressWarnings("unchecked")
	public <T> Mirror<T> getMirror(Class<T> type, String name) {
		if (MethodInterceptor.class.isAssignableFrom(type)
			|| type.getName().endsWith(ClassAgent.CLASSNAME_SUFFIX)
			|| AopConfigration.IOCNAME.equals(name)
			|| AopConfigration.class.isAssignableFrom(type))
			return Mirror.me(type);
		try {
			return (Mirror<T>) Mirror.me(cd.load(type.getName() + ClassAgent.CLASSNAME_SUFFIX));
		}
		catch (ClassNotFoundException e) {}
		if (aopConfigration == null)
			if (ioc.has(AopConfigration.IOCNAME))
				aopConfigration = ioc.get(AopConfigration.class, AopConfigration.IOCNAME);
			else
				aopConfigration = new AnnotationAopConfigration();
		List<InterceptorPair> interceptorPairs = aopConfigration.getInterceptorPairList(ioc, type);
		if (interceptorPairs == null || interceptorPairs.size() < 1)
			return Mirror.me(type);
		ClassAgent agent = new AsmClassAgent();
		for (InterceptorPair interceptorPair : interceptorPairs)
			agent.addInterceptor(	interceptorPair.getMethodMatcher(),
									interceptorPair.getMethodInterceptor());
		return Mirror.me(agent.define(cd, type));
	}

	public void setAopConfigration(AopConfigration aopConfigration) {
		this.aopConfigration = aopConfigration;
	}
}
