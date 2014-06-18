package com.bumnetworks.daybreak.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.bumnetworks.daybreak.annotations.Getter;
import com.bumnetworks.daybreak.annotations.Index;
import com.bumnetworks.daybreak.annotations.Indexes;
import com.bumnetworks.daybreak.annotations.Key;
import com.bumnetworks.daybreak.annotations.Setter;

public abstract class AbstractService<P extends GenericDBObject> implements Service<P> {
    protected Class<P> klass;
    protected Map<String,Pair> pairs;

    @SuppressWarnings("unchecked")
	protected AbstractService() {
        Type tp = getClass().getGenericSuperclass();
        while (!(tp instanceof ParameterizedType)) {
            tp = ((Class<P>) tp).getGenericSuperclass();
        }
        this.klass = (Class<P>) ((ParameterizedType) tp)
            .getActualTypeArguments()[0];

        init();
    }

    protected AbstractService(Class<P> k) {
        klass = k;
        init();
    }

    private void init() {
        pairs = computePairs();
    }

    public Class<P> getKlass() { return klass; }

    /**
       Obviously, re-inventing wheel here. Don't want to pull in any
       other deps for such a simple thing, though.
    */
    protected String inferKey(Method m) {
        String op;
        if (m.isAnnotationPresent(Getter.class))
            op = "get";
        else if (m.isAnnotationPresent(Setter.class))
            op = "set";
        else
            throw new IllegalArgumentException("not a @Getter or @Setter: " + m);

        // shamelessly ripped out of JRuby
        return
            Pattern.compile("([a-z][0-9]*)([A-Z])")
            .matcher(m.getName().substring(m.getName().indexOf(op) + op.length()))
            .replaceAll("$1_$2").toLowerCase();
    }

    protected Map<String,Pair> computePairs() {
        Map<String,Pair> computePairs = new HashMap<String,Pair>();
        for (Method m : klass.getMethods()) {
            if (!m.isAnnotationPresent(Getter.class)
                && !m.isAnnotationPresent(Setter.class))
                continue;

            String key = null;
            if (m.isAnnotationPresent(Getter.class))
                key = m.getAnnotation(Getter.class).key();
            else if (m.isAnnotationPresent(Setter.class))
                key = m.getAnnotation(Setter.class).key();

            if (key == null || "".equals(key))
                key = inferKey(m);

            Pair pair = computePairs.get(key);
            if (pair == null)
                pair = new Pair(key); computePairs.put(key, pair);

            if (m.isAnnotationPresent(Key.class))
                pair.setIsKey(true);

            if (m.isAnnotationPresent(Index.class))
                pair.addIndex(m.getAnnotation(Index.class));

            if (m.isAnnotationPresent(Indexes.class))
                for (Index idx : m.getAnnotation(Indexes.class).value())
                    pair.addIndex(idx);

            pair.accept(m);
        }
        return computePairs;
    }

    public Map<String,Pair> getPairs() { return pairs; }

    public List<Pair> getKeys() {
        List<Pair> keys = new ArrayList<Pair>();
        for (Pair pair : pairs.values())
            if (pair.isKey())
                keys.add(pair);
        return keys;
    }

    public Pair getFirstKey() {
        return getKeys().get(0);
    }
}
