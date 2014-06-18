package net.strong.mongodb;


public class BasicConf implements Conf {
    private String mongoHost;
    private int mongoPort;
    private String mongoUsername;
    private String mongoPassword;
    private String database;

    public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	//private Collection<Class> serviceClasses;

    public BasicConf() {
        //mongoHost = "192.168.1.104";
        //mongoPort = 27017;
        //mongoUsername = null;
        //mongoPassword = null;
        //serviceClasses = new ArrayList<Class>();
    }

    public String getMongoHost() {
        return mongoHost;
    }
    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }
    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoUsername() {
        return mongoUsername;
    }
    public void setMongoUsername(String mongoUsername) {
        this.mongoUsername = mongoUsername;
    }

    public String getMongoPassword() {
        return mongoPassword;
    }
    public void setMongoPassword(String mongoPassword) {
        this.mongoPassword = mongoPassword;
    }

    public boolean isMongoSecure() {
        if (mongoUsername == null
            || "".equals(mongoUsername)
            || mongoPassword == null
            || "".equals(mongoPassword))
            return false;
        return true;
    }
/*
    public Collection<Class> getServiceClasses() {
        return serviceClasses;
    }
    public void addServiceClass(Class klass) {
        serviceClasses.add(klass);
    }*/
}
