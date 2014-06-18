package com.bumnetworks.daybreak.test;

import org.bson.BSONObject;

import com.bumnetworks.daybreak.core.*;
import com.bumnetworks.daybreak.annotations.*;

@MongoDB(db = "happypiggyland", coll = "barns")
public class Barn extends GenericDBObject {
    private String name;

    public Barn() {}
    public Barn(String name) { this(); this.name = name; }

    @Getter
    public String getName() { return name; }

    @Setter
    public void setName(String name) { this.name = name; }

    @Override
	public String toString() {
        return "Barn(name: '" + name + "')";
    }
	public void putAll(BSONObject arg0) {
		// TODO Auto-generated method stub

	}
}
