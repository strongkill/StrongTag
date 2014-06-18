package net.strong.mongodb;

import com.google.code.morphia.DAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class MongoDAO<T> extends DAO<T, String> {
    public MongoDAO(Morphia morphia, Mongo mongo,String db ) {
        super(mongo, morphia, db);
    }
    public MongoDAO(Class<T> clazz,Mongo mongo,Morphia morphia,String dbName){
    	super(clazz,mongo,morphia,dbName);
    }
    public MongoDAO(Datastore ds){
    	super(ds);
    }
    public MongoDAO(Class<T> clazz,Datastore ds){
    	super(clazz,ds);
    }
}
