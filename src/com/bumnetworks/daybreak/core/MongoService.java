package com.bumnetworks.daybreak.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bumnetworks.daybreak.annotations.Index;
import com.bumnetworks.daybreak.annotations.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class MongoService<P extends GenericDBObject> extends AbstractService<P> implements Service<P> {
    private static Logger log = LoggerFactory.getLogger(MongoService.class);
    protected DB db;
    protected DBCollection coll;

    protected MongoService() {
        super();
        init();
    }

    protected MongoService(Class<P> k) {
        super(k);
        init();
    }

    private void init() {
        MongoDB mdb = klass
            .getAnnotation(MongoDB.class);

        db = Daybreak.getInstance().getDB(mdb.db());
        coll = db.getCollection(mdb.coll());

        ensureIndexes();
    }

    public DB getDB() { return db; }
    public DBCollection getColl() { return coll; }

    public P get(Object o) {
        try {
            BasicDBObject q = new BasicDBObject();
            if (o instanceof Number) {
                q.put(getFirstKey().getKey(), o);
            } else {
                q.put(getFirstKey().getKey(), o.toString());
            }
            List<P> list = find(q);
            if (list.isEmpty())
                return null;
            return list.get(0);
        }
        catch (Throwable t) {
            log.error("failed to find " + klass + " by " + o, t);
        }
        return null;
    }

    public P put(P p) {
        coll.save(p);
        return p;
    }

    public List<P> find() { return find(new BasicDBObject()); }
    public List<P> find(DBObject q) {
        DBCursor cur = coll.find(q);
        List<P> list = new ArrayList<P>();
        while (cur.hasNext())
            list.add(inflate(cur.next()));
        return list;
    }

    public List<P> findByMap(Map<String,Object> m) {
        DBObject q = new BasicDBObject();
        q.putAll(m);
        return find(q);
    }

    private P inflate(DBObject raw) {
        try {
            P result = klass.newInstance();
            result.putAll(raw);
            return result;
        }
        catch (Throwable t) {
            log.error("failed to inflate '" + raw + "'", t);
            throw new InflationFailed(t);
        }
    }

    public void ensureIndexes() {
        coll.resetIndexCache();

        for (Pair pair : pairs.values()) {
            for (Index idx : pair.getIndexes()) {
                int order = 1;
                if (Index.Order.DESC.equals(idx.order()))
                    order = -1;

                BasicDBObjectBuilder b = BasicDBObjectBuilder.start();

                if (!"".equals(idx.exp()))
                    b.add(idx.exp(), order);
                else
                    b.add(pair.getKey(), order);

                coll.ensureIndex(b.get(),
                                 pair.getKey() + "_idx",
                                 idx.unique());
            }
        }
    }
}
