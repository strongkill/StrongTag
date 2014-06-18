package com.bumnetworks.daybreak.test;

import com.bumnetworks.daybreak.core.*;

public abstract class AbstractTest {
    protected Daybreak singleton;

    public AbstractTest() {
        Daybreak.configure(new BasicConf());
        singleton = Daybreak.getInstance();
    }
}
