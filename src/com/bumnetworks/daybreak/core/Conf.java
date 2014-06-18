package com.bumnetworks.daybreak.core;

import java.util.Collection;

public interface Conf {
    public String getMongoHost();
    public int getMongoPort();
    public boolean isMongoSecure();
    public String getMongoUsername();
    public String getMongoPassword();
    public Collection<Class> getServiceClasses();
}
