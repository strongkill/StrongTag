package com.bumnetworks.daybreak.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.bumnetworks.daybreak.annotations.Getter;
import com.bumnetworks.daybreak.annotations.Index;
import com.bumnetworks.daybreak.annotations.Setter;

public class Pair {
    private String key;
    private boolean isKey;
    private Method getter;
    private Method setter;
    private List<Index> indexes;

    public static class NotCollection extends RuntimeException {}
    public static class NotMap extends RuntimeException {}

    public Pair() { indexes = new ArrayList<Index>(); }
    public Pair(String key) { this(); this.key = key; }

    public boolean isKey() { return isKey; }
    public void setIsKey(boolean isKey) { this.isKey = isKey; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public Method getGetter() { return getter; }
    public void setGetter(Method m) { getter = m; }

    public Method getSetter() { return setter; }
    public void setSetter(Method m) { setter = m; }

    public void accept(Method m) {
        if (m.isAnnotationPresent(Getter.class))
            setGetter(m);
        else if (m.isAnnotationPresent(Setter.class))
            setSetter(m);
        else
            return;
    }

    @Override
	public String toString() {
        return "Pair(" + key + ": " + getter + ", " + setter + ", " + isCollection() + ", " + isMap() + ")";
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(getter.getReturnType());
    }

    public Type getCollectionType() {
        if (!isCollection())
            throw new NotCollection();
        return setter.getGenericParameterTypes()[0];
    }

    public Type getCollectionArgType() {
        if (!isCollection())
            throw new NotCollection();
        return ((ParameterizedType) getCollectionType()).getActualTypeArguments()[0];
    }

    public Collection<?> makeCollection() {
        if (!isCollection())
            throw new NotCollection();
        return Pair.makeCollection((Class) ((ParameterizedType) getCollectionType()).getRawType());
    }

    private static Collection<?> makeCollection(Class iface) {
        if (List.class.isAssignableFrom(iface)) {
            return new ArrayList<Object>();
        } else if (Set.class.isAssignableFrom(iface)) {
            return new HashSet<Object>();
        }
        throw new IllegalArgumentException("expect " + List.class + " or " + Set.class
                                           + ", but got " + iface);
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(getter.getReturnType());
    }

    public Type getMapType() {
        if (!isMap())
            throw new NotMap();
        return setter.getGenericParameterTypes()[0];
    }

    public Type getMapKeyType() {
        if (!isMap())
            throw new NotMap();
        return ((ParameterizedType) getMapType()).getActualTypeArguments()[0];
    }

    public Type getMapValueType() {
        if (!isMap())
            throw new NotMap();
        return ((ParameterizedType) getMapType()).getActualTypeArguments()[1];
    }

    public Map<String,Object> makeMap() {
        return new HashMap<String,Object>();
    }

    public List<Index> getIndexes() { return indexes; }
    public void addIndex(Index idx) { indexes.add(idx); }
}
