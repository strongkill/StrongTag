package net.strong.mongodb;

import java.util.List;

import com.google.code.morphia.DAO;
import com.google.code.morphia.Key;
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

public interface IBaseMongodb {
	public <T> DAO<T,String> getDAO(Class<T> entityClazz);

	public <T,K> MongoDAO<T> getMongoDAO(Class<K> entityClazz);

	public <T> void delete(T entity, WriteConcern wc) ;
	public <T,K> List<T> find(Class<T> c,Key<K> condition);
	public <T, V> Query<T> find(Class<T> clazz, String property, V value);
	public <T, V> Query<T> find(Class<T> clazz, String property, V value, int offset, int size);
		public <T, V> Query<T> get(Class<T> clazz, Iterable<V> ids);
	public <T, V> T get(Class<T> clazz, V id);
		public <T, V> void delete(Class<T> clazz, Iterable<V> ids) ;
	public <T, V> void delete(Class<T> clazz, V id) ;
		public <T> Iterable<Key<T>> save(Iterable<T> entities);
	public <T> Iterable<Key<T>> save(Iterable<T> entities, WriteConcern wc) ;
	public <T> Iterable<Key<T>> save(T[] entities);
	public <T> Key<T> getKey(T entity);
		public <T> Key<T> save(T entity) ;
	public <T> Key<T> save(T entity, WriteConcern wc) ;
		public <T> List<T> getByKeys(Class<T> clazz, Iterable<Key<T>> keys);
	public <T> List<T> getByKeys(Iterable<Key<T>> keys) ;
	public <T> Query<T> createQuery(Class<T> clazz);
	public <T> Query<T> find(Class<T> clazz);
	public <T> T findAndDelete(Query<T> query) ;
	public <T> T findAndModify(Query<T> q, UpdateOperations<T> ops) ;
	public <T> T findAndModify(Query<T> query, UpdateOperations<T> ops, boolean oldVersion) ;
	public <T> T get(Class<T> clazz, DBRef ref);
	public <T> T get(T entity) ;
	public <T> T getByKey(Class<T> clazz, Key<T> key);
	public <T> UpdateResults<T> update(Query<T> query, UpdateOperations<T> ops)   ;
	public <T> UpdateResults<T> update(Query<T> query, UpdateOperations<T> ops, boolean createIfMissing) ;
	public <T> UpdateResults<T> update(Query<T> query, UpdateOperations<T> ops, boolean createIfMissing, WriteConcern wc) ;
	public <T> UpdateResults<T> updateFirst(Query<T> query, T entity, boolean createIfMissing)   ;
	public <T> UpdateResults<T> updateFirst(Query<T> query, UpdateOperations<T> ops)   ;
	public <T> UpdateResults<T> updateFirst(Query<T> query, UpdateOperations<T> ops, boolean createIfMissing) ;
	public <T> UpdateResults<T> updateFirst(Query<T> query, UpdateOperations<T> ops, boolean createIfMissing, WriteConcern wc);
	public <T> long getCount(Class<T> clazz);
	public <T> long getCount(Query<T> query);
	public <T> long getCount(T entity);
	public <T> void delete(Query<T> query) ;
	public <T> void delete(Query<T> query, WriteConcern wc) ;
	public <T> void delete(T entity) ;
		public <T> void ensureIndex(Class<T> clazz, String name, IndexFieldDef[] defs, boolean unique, boolean dropDupsOnCreate);
	public <T> void ensureIndex(Class<T> type, IndexFieldDef[] fields) ;
	public <T> void ensureIndex(Class<T> type, String name, IndexDirection dir) ;
	public <T> void ensureIndexes(Class<T> clazz);
	public DB getDB();
	public <T> DBCollection getCollection(Class<T> clazz);
	public DBCollection getCollection(Object obj) ;
	public Mongo getMongo();
	public WriteConcern getDefaultWriteConcern() ;
	public long getCount(String kind);
	public void ensureCaps();
	public void ensureIndexes();
}
