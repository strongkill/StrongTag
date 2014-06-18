package com.bumnetworks.daybreak.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public abstract class GenericDBObject implements DBObject {

    public static class ValueTypeMismatch extends RuntimeException {
        public Class owner, expected, received;
        public String key;

        public ValueTypeMismatch(Class owner, String key, Class expected, Class received) {
            this.owner = owner; this.key = key;
            this.expected = expected; this.received = received;
        }

        @Override
        public String getMessage() {
            return owner + "." + key + " expected " + expected + ", but got " + received;
        }
    }

	@SuppressWarnings("unchecked")
	private <T extends GenericDBObject> Service<T> getService() {
        return (Service<T>) ServiceFactory.get(getClass());
    }

    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<String,Object>();
        for (String key : getService().getPairs().keySet())
            map.put(key, get(key));
        return map;
    }

    public boolean containsKey(String k) { return getService().getPairs().containsKey(k); }
    public boolean containsField(String k) { return containsKey(k); }

    public Object get(String key) {
        if (!containsKey(key))
            return null;

        Method m = getService().getPairs()
            .get(key).getGetter();
        if (m != null) {
            try {
                return m.invoke(this);
            }
            catch (Throwable t) {
                //log.error(this + ".get(" + key + ")", t);
            }
        }
        return null;
    }

    public boolean isPartialObject() { return false; }
    public void markAsPartialObject() {}

    @SuppressWarnings("unchecked")
	private Object putCollection(Pair pair, String k, Object v) throws Throwable {
        if (!(v instanceof BasicDBList))
            throw new ValueTypeMismatch(getClass(), k, BasicDBList.class, v.getClass());

        Method setter = pair.getSetter();

        Type collType = setter.getGenericParameterTypes()[0];
        Type argType = ((ParameterizedType) collType)
            .getActualTypeArguments()[0];

        //log.info("'" + k + "' takes a '" + collType
        //                 + "' of '" + ((Class) argType) + "'s");

        Collection coll = pair.makeCollection();
        //log.info("coll: " + coll.getClass() + ": " + coll);
        setter.invoke(this, coll);

        Iterator i = ((BasicDBList) v).iterator();
        while (i.hasNext()) {
            DBObject entry = (DBObject) i.next();

            if (GenericDBObject.class.isAssignableFrom((Class) argType)) {
                //log.info("'" + entry + "' is GenericDBObject!");

                GenericDBObject magic = (GenericDBObject) ((Class) argType).newInstance();
                magic.putAll(entry);
                coll.add(magic);
            } else {
                //log.info("'" + entry + "' is primitive...");
                coll.add(entry);
            }
        }

        return v;
    }

    private Object putMap(Pair pair, String k, Object v) throws Throwable {
        if (!v.getClass().equals(BasicDBObject.class))
            throw new ValueTypeMismatch(getClass(), k, BasicDBObject.class, v.getClass());

        Method setter = pair.getSetter();
        Map<String,Object> map = pair.makeMap();
        setter.invoke(this, map);
        for (String key : ((BasicDBObject) v).keySet()) {
            DBObject value = (DBObject) ((DBObject) v).get(key);
            if (GenericDBObject.class.isAssignableFrom((Class) pair.getMapValueType())) {
                //log.info("'" + value + "' is GenericDBObject!");
                GenericDBObject magic = (GenericDBObject) ((Class) pair.getMapValueType()).newInstance();
                magic.putAll(value);
                map.put(key, magic);
            } else {
                //log.info("'" + value + "' is primitive...");
                map.put(key, value);
            }
        }

        return v;
    }

    public Object put(String k, Object v) {
        if (v != null) {
            //log.info(this + ".put(" + k + ", " + v.getClass() + "@" + v.hashCode() + ")");
        } else {
            //log.info(this + ".put(" + k + ", null)");
        }

        if (!containsKey(k))
            return null;

        Pair methods = getService().getPairs().get(k);

        try {
            if (methods.isCollection()) {
                return putCollection(methods, k, v);
            } else if (methods.isMap()) {
                return putMap(methods, k, v);
            } else {
                methods.getSetter().invoke(this, v);
            }
            return v;
        }
        catch (Throwable t) {
            //log.error(this + ".put(" + k + ", " + v + ")", t);
        }

        return null;
    }

    public Set<String> keySet() {
        return new HashSet<String>(getService().getPairs().keySet());
    }

    public void putAll(DBObject dbo) { putAll(dbo.toMap()); }
    public void putAll(Map map) {
        for (Object o : map.keySet())
            if (map.get(o) != null)
                put(o.toString(), map.get(o));
    }

    public Object removeField(String k) {
        if (!containsField(k))
            return null;

        Object previous = get(k);
        put(k, null);
        return previous;
    }
}
