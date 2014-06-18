package com.bumnetworks.daybreak.core;

import java.util.List;
import java.util.Map;

public interface Service<P extends GenericDBObject> {
    public Class<P> getKlass();

    public Map<String,Pair> getPairs();
    public List<Pair> getKeys();

    public P get(Object key);
    public P put(P p);

    public List<P> find();
    public List<P> findByMap(Map<String,Object> m);
}
