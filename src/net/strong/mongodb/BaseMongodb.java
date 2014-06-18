package net.strong.mongodb;

import java.util.List;

import com.google.code.morphia.DAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.google.code.morphia.utils.IndexDirection;
import com.google.code.morphia.utils.IndexFieldDef;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBRef;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

public class BaseMongodb implements IBaseMongodb {
	protected Mongo mongo;
	protected DB db;
	protected Datastore ds;
	protected Morphia morphia = new Morphia();

	private static Conf conf;
	private static BaseMongodb instance = null;

	public BaseMongodb getInstance(String host,int port,String database) {
		synchronized (BaseMongodb.class) {
			if(instance==null){
				instance = new BaseMongodb(host,port,database);//
			}
		}
		return instance;
	}

	public BaseMongodb getInstance() {
		return  getInstance(conf.getMongoHost(),conf.getMongoPort(),conf.getDatabase());//
	}

	public void setConf(Conf conf) {
		BaseMongodb.conf = conf;
	}
	protected Conf getConf() {
		return conf;
	}

	protected BaseMongodb() {/*
		try {
			System.out.println("init the mongodb instance no parameter");
			this.mongo = new Mongo(conf.getMongoHost(),conf.getMongoPort());
			this.ds = this.morphia.createDatastore(this.mongo,conf.getDatabase());
			this.db = this.ds.getDB();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/
	}


	protected BaseMongodb(String host,int port,String database) {
		try {
			if(conf==null){
				conf = new BasicConf();
				conf.setMongoHost(host);
				conf.setMongoPort(port);
				conf.setDatabase(database);
			}
			System.out.println("init the mongodb instance");
			this.mongo = new Mongo(host,port);
			this.ds = this.morphia.createDatastore(this.mongo,database);
			this.db = this.ds.getDB();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}/*
	public <T> Object get(Object h){
		return ds.get(h);
	}*/

	public <T,K> List<T> find(Class<T> c,Key<K> condition){
		List<T> l =ds.find(c, condition.getKind(), condition.getId()).asList();
		//or
		//l = ds.find(c).field("stars").greaterThanOrEq(4).asList();
		return l;
	}
	public <T> Key<T> save(T h){
		return (Key<T>)ds.save(h);
	}

	public <T> Key<T> saveOrUpdate(T h){
		return (Key<T>)ds.save(h);
	}	
	
	public <T> Iterable<Key<T>> saveOrUpdate(Iterable<T> entities){
		return ds.save(entities);
	}	
	public <T> Iterable<Key<T>> save(Iterable<T> entities){
		return ds.save(entities);
	}


	/////////////////Autn_generated DataStore/////////////////////////

	public <T, V> Query<T> find(Class<T> clazz, String property, V value) {
		// TODO Auto-generated method stub
		return ds.find(clazz, property, value);
	}

	public <T, V> Query<T> find(Class<T> clazz, String property, V value,
			int offset, int size) {
		// TODO Auto-generated method stub
		return ds.find(clazz, property, value, offset, size);
	}


	public <T, V> Query<T> get(Class<T> clazz, Iterable<V> ids) {
		// TODO Auto-generated method stub
		return ds.get(clazz,ids);
	}

	public <T, V> T get(Class<T> clazz, V id) {
		// TODO Auto-generated method stub
		return ds.get(clazz,id);
	}


	public <T, V> void delete(Class<T> clazz, Iterable<V> ids) {
		ds.delete(clazz, ids);
	}

	public <T, V> void delete(Class<T> clazz, V id) {
		ds.delete(clazz, id);
	}

	public <T> Iterable<Key<T>> saveOrUpdate(Iterable<T> entities, WriteConcern wc) {
		// TODO Auto-generated method stub
		return ds.save(entities,wc);
	}
	public <T> Iterable<Key<T>> save(Iterable<T> entities, WriteConcern wc) {
		// TODO Auto-generated method stub
		return ds.save(entities,wc);
	}

	public <T> Iterable<Key<T>> saveOrUpdate(T[] entities){
		return ds.save(entities);
	}
	public <T> Iterable<Key<T>> save(T[] entities) {
		// TODO Auto-generated method stub
		return ds.save(entities);
	}

	public <T> Key<T> saveOrUpdate(T entity, WriteConcern wc) {
		// TODO Auto-generated method stub
		return ds.save(entity, wc);
	}
	
	public <T> Key<T> save(T entity, WriteConcern wc) {
		// TODO Auto-generated method stub
		return ds.save(entity, wc);
	}

	
	public <T> Key<T> getKey(T entity) {
		// TODO Auto-generated method stub
		return ds.getKey(entity);
	}


	public <T> List<T> getByKeys(Class<T> clazz, Iterable<Key<T>> keys) {
		// TODO Auto-generated method stub
		return ds.getByKeys(clazz, keys);
	}

	public <T> List<T> getByKeys(Iterable<Key<T>> keys) {
		// TODO Auto-generated method stub
		return ds.getByKeys(keys);
	}

	public <T> Query<T> createQuery(Class<T> clazz) {
		// TODO Auto-generated method stub
		return ds.createQuery(clazz);
	}


	public <T> Query<T> find(Class<T> clazz) {
		// TODO Auto-generated method stub
		return ds.find(clazz);
	}



	public <T> T findAndDelete(Query<T> query) {
		// TODO Auto-generated method stub
		return ds.findAndDelete(query);
	}

	public <T> T findAndModify(Query<T> q, UpdateOperations<T> ops) {
		// TODO Auto-generated method stub
		return ds.findAndModify(q, ops);
	}

	public <T> T findAndModify(Query<T> query, UpdateOperations<T> ops,
			boolean oldVersion) {
		// TODO Auto-generated method stub
		return ds.findAndModify(query, ops, oldVersion);
	}

	public <T> T get(Class<T> clazz, DBRef ref) {
		// TODO Auto-generated method stub
		return ds.get(clazz, ref);
	}

	public <T> T getByKey(Class<T> clazz, Key<T> key) {
		// TODO Auto-generated method stub
		return ds.getByKey(clazz, key);
	}

	public <T> UpdateResults<T> update(Query<T> query, UpdateOperations<T> ops) {
		// TODO Auto-generated method stub
		return ds.update(query, ops);
	}

	public <T> UpdateResults<T> update(Query<T> query, UpdateOperations<T> ops,
			boolean createIfMissing) {
		// TODO Auto-generated method stub
		return ds.update(query, ops, createIfMissing);
	}

	public <T> UpdateResults<T> update(Query<T> query, UpdateOperations<T> ops,
			boolean createIfMissing, WriteConcern wc) {
		// TODO Auto-generated method stub
		return ds.update(query, ops, createIfMissing, wc);
	}

	public <T> UpdateResults<T> updateFirst(Query<T> query, T entity,
			boolean createIfMissing) {
		// TODO Auto-generated method stub
		return ds.updateFirst(query, entity, createIfMissing);
	}

	public <T> UpdateResults<T> updateFirst(Query<T> query,
			UpdateOperations<T> ops) {
		// TODO Auto-generated method stub
		return ds.updateFirst(query, ops);
	}

	public <T> UpdateResults<T> updateFirst(Query<T> query,
			UpdateOperations<T> ops, boolean createIfMissing) {
		// TODO Auto-generated method stub
		return ds.updateFirst(query, ops, createIfMissing);
	}

	public <T> UpdateResults<T> updateFirst(Query<T> query,
			UpdateOperations<T> ops, boolean createIfMissing, WriteConcern wc) {
		// TODO Auto-generated method stub
		return ds.updateFirst(query, ops, createIfMissing, wc);
	}

	public <T> long getCount(Class<T> clazz) {
		// TODO Auto-generated method stub
		return ds.getCount(clazz);
	}

	public <T> long getCount(Query<T> query) {
		return ds.getCount(query);
	}

	public <T> long getCount(T entity) {
		return ds.getCount(entity);
	}

	public <T> void delete(Query<T> query) {
		ds.delete(query);
	}

	public <T> void delete(Query<T> query, WriteConcern wc) {
		ds.delete(query, wc);
	}


	public <T> void delete(T entity) {
		ds.delete(entity);
	}

	public <T> void delete(T entity, WriteConcern wc) {
		ds.delete(entity, wc);
	}

	public <T> void ensureIndex(Class<T> clazz, String name,
			IndexFieldDef[] defs, boolean unique, boolean dropDupsOnCreate) {
		ds.ensureIndex(clazz, name,defs,unique,dropDupsOnCreate);
	}

	public <T> void ensureIndex(Class<T> type, IndexFieldDef[] fields) {
		ds.ensureIndex(type,fields);
	}

	public <T> void ensureIndex(Class<T> type, String name, IndexDirection dir) {
		ds.ensureIndex(type, name,dir);
	}

	public <T> void ensureIndexes(Class<T> clazz) {
		ds.ensureIndexes(clazz);
	}

	public DB getDB() {
		return this.db;
	}

	public <T> DBCollection getCollection(Class<T> clazz) {
		return ds.getCollection(clazz);
	}

	public DBCollection getCollection(Object obj) {
		if(obj==null) return null;
		return ds.getCollection(obj.getClass());
	}


	public Mongo getMongo() {
		// TODO Auto-generated method stub
		return this.mongo;
	}

	public WriteConcern getDefaultWriteConcern() {
		return ds.getDefaultWriteConcern();
	}

	public long getCount(String kind) {
		return ds.getCount(kind);
	}


	public void ensureCaps() {
		ds.ensureCaps();
	}

	public void ensureIndexes() {
		ds.ensureIndexes();
	}

	public <T> T get(T entity) {
		return ds.get(entity);
	}


	public <T> DAO<T, String> getDAO(Class<T> entityClazz) {
		if(entityClazz==null)return null;
		return new DAO<T, String>(entityClazz,mongo,morphia,conf.getDatabase());
	}

	public <T, K> MongoDAO<T> getMongoDAO(Class<K> entityClazz) {
		if(entityClazz==null)return null;
		return new MongoDAO(entityClazz,mongo,morphia,conf.getDatabase());
	}

}
