package net.strong.mongodb;


public interface Conf {
    public String getMongoHost();
    public void setMongoHost(String host);

    public int getMongoPort();
    public void setMongoPort(int port);
    public boolean isMongoSecure();
    public String getMongoUsername();
    public void setMongoUsername(String username);
    public String getMongoPassword();
    public void setMongoPassword(String password);
    public String getDatabase();
    public void setDatabase(String database);
  //  public Collection<Class> getServiceClasses();
}
