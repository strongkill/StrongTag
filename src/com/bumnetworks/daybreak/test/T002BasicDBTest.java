package com.bumnetworks.daybreak.test;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import com.mongodb.DBCollection;
import com.mongodb.DB;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.WriteResult;

public class T002BasicDBTest extends AbstractTest {
    private String dbName;

    public T002BasicDBTest() {
        dbName = "daybreak.test." + System.currentTimeMillis();
    }

    //@Test
    public void countDatabases() {
        List<String> names = singleton
            .getMongo().getDatabaseNames();
        assertTrue(names.size() > 0);
    }

    //@Test
    public void printDBNames() {
        for (String name : singleton.getMongo().getDatabaseNames())
            System.err.println("found DB: " + name);
    }

    @Test
    public void dropDatabase(){
    	 DB test = singleton.getMongo().getDB("daybreak");
    	 assertTrue(test != null);
    	test.dropDatabase();

    }

    //@Test
    public void setUpCollections() {
        DB test = singleton.getMongo().getDB(dbName);
        System.out.println("test DB: " + test);
        assertTrue(test != null);

        DBCollection foo = test.getCollection("foo");
        assertTrue(foo != null);

        System.out.println("foo: " + foo);

        BasicDBObject one = new BasicDBObject();
        one.put("time-right-now", System.currentTimeMillis());
        BasicDBList list = new BasicDBList();
        one.put("some-crap", list);
        int i = 1;
        for (; i <= 10; i++)
            list.add(System.currentTimeMillis());

       // WriteResult aa = foo.insert(one);

        System.out.println(foo.findOne());
    }
}
