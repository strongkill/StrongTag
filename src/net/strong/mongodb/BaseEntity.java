package net.strong.mongodb;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
/**
 * MongoDB对象的父类,所有保存于MongoDB中的对像都必需继承本类.
 * 并以id作为主键.
 * @author Strong Yuan
 * @Date 2010-9-3
 * @Version
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id protected String id = new ObjectId().toString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
