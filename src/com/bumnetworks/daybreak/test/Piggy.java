package com.bumnetworks.daybreak.test;

import com.bumnetworks.daybreak.core.*;
import com.bumnetworks.daybreak.annotations.*;
import java.util.*;

import org.bson.BSONObject;

@MongoDB(db = "happypiggyland", coll = "piggies")
public class Piggy extends GenericDBObject {
    private String name;
    private long age;
    private List<Piggy> children;
    private Barn location;

    public Piggy() { children = new ArrayList<Piggy>(); }
    public Piggy(String name, long age) { this(); this.name = name; this.age = age; }

    @Key
    @Getter
    public String getName() { return name; }

    @Setter
    public void setName(String name) { this.name = name; }

    @Getter
    public long getAge() { return age; }

    @Setter
    public void setAge(long age) { this.age = age; }

    @Getter
    public List<Piggy> getChildren() { return children; }

    @Setter
    public void setChildren(List<Piggy> children) { this.children = children; }

    @Getter
    public Barn getLocation() { return location; }

    @Setter
    public void setLocation(Barn location) { this.location = location; }

    @Override
	public String toString() {
        return "Piggy(name: '" + name
            + "', age: '" + age
            + "', children: " + children
            + ")";
    }
	public void putAll(BSONObject arg0) {
		// TODO Auto-generated method stub

	}
}
