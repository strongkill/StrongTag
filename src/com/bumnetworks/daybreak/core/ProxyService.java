package com.bumnetworks.daybreak.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyService<P extends GenericDBObject> extends MongoService<P> implements Service<P> {
    protected ProxyService() { super(); }
    protected ProxyService(Class<P> p) { super(p); }

    @SuppressWarnings("unchecked")
	public static Service getProxy(final Class p) {
        final InvocationHandler handler = new InvocationHandler() {
                protected Service impl = new ProxyService(p);
                public Object invoke(Object proxy,
                                     Method method,
                                     Object[] args)
                    throws Throwable {
                    return method.invoke(impl, args);
                }
            };
        return (Service) Proxy
            .newProxyInstance(p.getClassLoader(),
                              new Class[] { Service.class },
                              handler);
    }
}
