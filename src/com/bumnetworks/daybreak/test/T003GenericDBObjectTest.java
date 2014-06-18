package com.bumnetworks.daybreak.test;

import org.junit.*;
import static org.junit.Assert.*;
import com.mongodb.*;
import org.slf4j.*;
import java.util.*;

public class T003GenericDBObjectTest extends AbstractTest {
    private static Logger log = LoggerFactory.getLogger(T003GenericDBObjectTest.class);
    private DB piggyDB;
    private DBCollection piggiesCol;

    @Before
    public void setUp() {
        piggyDB = singleton.getMongo().getDB("happypiggyland");
        log.info("piggy DB: " + piggyDB);
        assertTrue(piggyDB != null);

        piggiesCol = piggyDB.getCollection("piggies");
        assertTrue(piggiesCol != null);

        log.info("piggies collection: " + piggiesCol);
    }

    @Test
    public void savePiggy() {
        Piggy piggy = new Piggy("spider pig @ " + (new Date()).toString(), System.currentTimeMillis());
        piggy.getChildren().add(new Piggy("child 1", 1));
        piggy.getChildren().add(new Piggy("child 2", 2));
        piggy.getChildren().add(new Piggy("child 3", 3));
        piggiesCol.insert(piggy);
    }

    @Test
    public void loadPiggies() {
        Iterator i = piggiesCol.find();
        while (i.hasNext()) {
            DBObject raw = (DBObject) i.next();
            log.info("loaded raw: " + raw);
            Piggy piggy = new Piggy();
            piggy.putAll(raw);
            log.info("loaded piggy: " + piggy);
        }
    }
}
