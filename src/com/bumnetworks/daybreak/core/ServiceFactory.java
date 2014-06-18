package com.bumnetworks.daybreak.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceFactory {
    private static Logger log = LoggerFactory.getLogger(ServiceFactory.class);
    private static Map<Class,Class> klasses;
    private static Map<Class,Service> instances;

    static {
        synchronized (ServiceFactory.class) {
            klasses = new ConcurrentHashMap<Class,Class>();
            instances = new ConcurrentHashMap<Class,Service>();
        }
    }

    public static class NotRegistered extends RuntimeException {
        private Class klass;
        public NotRegistered(Class klass) { this.klass = klass; }
        public Class getKlass() { return klass; }
        @Override
		public String getMessage() {
            return klass + " has no registered service";
        }
    }

    public static class NotInstantiated extends RuntimeException {
        private Class klass;
        public NotInstantiated(Throwable t, Class klass) {
            super(t);
            this.klass = klass;
        }
        public Class getKlass() { return klass; }
    }

    public static <M extends GenericDBObject> Service<M> get(Class<M> klass) throws NotRegistered {
        return get(klass, false);
    }

    @SuppressWarnings("unchecked")
	public static <M extends GenericDBObject> Service<M> get(Class<M> klass, boolean wantProxy) throws NotRegistered {
        synchronized (klass) {
            if (!klasses.containsKey(klass)) {
                if (wantProxy) {
                    Service<M> proxy = ProxyService.getProxy(klass);
                    register(klass, proxy);
                    return proxy;
                } else {
                    throw new NotRegistered(klass);
                }
            }

            Class<Service<M>> svcKlass = klasses.get(klass);
            if (instances.containsKey(svcKlass))
                return instances.get(svcKlass);

            try {
                Service<M> svc = svcKlass.newInstance();
                instances.put(svcKlass, svc);
                log.info(klass + " -> " + svc);
                return svc;
            }
            catch (Throwable t) {
                throw new NotInstantiated(t, klass);
            }
        }
    }

    public static <M extends GenericDBObject, S extends Service<M>> void register(Class<M> klass, Class<S> svcKlass) {
        synchronized (klass) {
            if (!klasses.containsKey(klass)) {
                klasses.put(klass, svcKlass);
                log.info(klass + " -> " + svcKlass);
            }
        }
    }

    public static <M extends GenericDBObject> void register(Class<M> klass, Service<M> svc) {
        synchronized (klass) {
            if (!klasses.containsKey(klass)) {
                klasses.put(klass, svc.getClass());
                log.info(klass + " -> " + svc.getClass());
            }
            if (!instances.containsKey(klass)) {
                instances.put(svc.getClass(), svc);
                log.info(klass + " -> " + svc);
            }
        }
    }
}
