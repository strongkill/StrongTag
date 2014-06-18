package com.bumnetworks.daybreak.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.slf4j.*;
import com.bumnetworks.daybreak.core.*;
import java.util.*;

public class T004ServiceTest extends AbstractTest {
    private static Logger log = LoggerFactory.getLogger(T004ServiceTest.class);
    private static Service<Piggy> svc;
    private static String name;

    @BeforeClass
    public static void makeName() {
        Conf conf = new BasicConf();
        Daybreak.configure(conf);
        ServiceFactory.register(Piggy.class, new MongoService<Piggy>() {});
        svc = ServiceFactory.get(Piggy.class);
        name = "test_piggy_" + System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testGetKeys() {
        List<Pair> keys = ((MongoService) svc).getKeys();
        log.info("keys: {}", keys);
        assertTrue(keys.size() > 0);
    }

    @Test
    public void savePiggy() {
        Piggy piggy = new Piggy();
        piggy.setName(name);
        assertTrue(svc.put(piggy) != null);
    }

    @Test
    public void retrievePiggy() {
        Piggy piggy = svc.get(name);
        assertTrue(piggy != null);
        assertTrue(name.equals(piggy.getName()));
    }
}
