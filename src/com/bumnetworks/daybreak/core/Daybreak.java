package com.bumnetworks.daybreak.core;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class Daybreak {
    private static Daybreak instance = null;
    private Conf conf;
    private Mongo mongo;

    public static class NotConfiguredException extends RuntimeException {}

    private Daybreak(Conf conf) throws UnknownHostException {
        this.conf = conf;
        mongo = new Mongo(conf.getMongoHost(), conf.getMongoPort());
        for (Class sc : conf.getServiceClasses())
            try { sc.newInstance(); } catch(Throwable t) {}
    }

    public static void configure(Conf conf) {
        synchronized (Daybreak.class) {
            if (instance != null)
                return;

            try {
                instance = new Daybreak(conf);
            }
            catch (UnknownHostException uhe)
            {/* do nothing and cause NotConfiguredException to be thrown*/}
        }
    }

    public static Daybreak getInstance() throws NotConfiguredException {
        if (instance == null)
            throw new NotConfiguredException();
        return instance;
    }

    public Mongo getMongo() {
        return mongo;
    }

    public DB getDB(String name) {
        DB db = mongo.getDB(name);
        if (conf.isMongoSecure())
            db.authenticate(conf.getMongoUsername(), conf.getMongoPassword().toCharArray());
        return db;
    }
}
