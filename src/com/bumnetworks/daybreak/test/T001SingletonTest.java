package com.bumnetworks.daybreak.test;

import org.junit.*;
import static org.junit.Assert.*;
import com.bumnetworks.daybreak.core.Daybreak;

public class T001SingletonTest extends AbstractTest {
    @Test
    public void init() {
        assertTrue(Daybreak.getInstance() != null);
    }

    @Test
    public void compareHashes() {
        assertEquals(Daybreak.getInstance().hashCode(),
                     Daybreak.getInstance().hashCode());
        assertEquals(singleton.hashCode(),
                     Daybreak.getInstance().hashCode());
    }

    @Test
    public void checkMongo() {
        assertTrue(singleton.getMongo() != null);
    }
}
