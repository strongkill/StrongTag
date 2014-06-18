package test.net.strong;

import net.strong.dao.entity.annotation.Column;
import net.strong.dao.entity.annotation.Id;
import net.strong.dao.entity.annotation.Name;
import net.strong.dao.entity.annotation.Table;




@Table("t_person")   // 声明了Person对象的数据表
public class Person {

	@Column	  // 表示该对象属性可以映射到数据库里作为一个字段
	@Id       // 表示该字段为一个自增长的Id
	private int id;

	@Column
	@Name    // 表示该字段可以用来标识此对象，或者是字符型主键，或者是唯一性约束
	private String name;

	@Column
	private int age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}