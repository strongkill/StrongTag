package net.strong.dao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.strong.dao.entity.Entity;
import net.strong.dao.entity.EntityField;
import net.strong.json.Json;
import net.strong.lang.Mirror;

/**
 * 名值链。
 * <p>
 * 通过 add 方法，建立一条名值对的链表
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author Wendal(wendal1985@gmail.com)
 */
public class Chain {

	/**
	 * 建立一条名值链开始的一环
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 * @return 链头
	 */
	public static Chain make(String name, Object value) {
		return new Chain(name, value, null, null);
	}

	private Chain(String name, Object value, Chain head, Chain next) {
		this.name = name;
		this.value = value;
		if (head == null)
			this.head = this;
		else
			this.head = head;
		this.next = next;
	}

	private Chain head;

	private String name;

	private Object value;

	private Chain next;

	/**
	 * 改变当前节点的名称
	 * 
	 * @param name
	 *            新名称
	 * @return 当前节点
	 */
	public Chain name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 改变当前节点的值
	 * 
	 * @param value
	 *            新值
	 * @return 当前节点
	 */
	public Chain value(Object value) {
		this.value = value;
		return this;
	}

	/**
	 * 将一个名值对，添加为本链节点的下一环
	 * 
	 * @param name
	 *            名
	 * @param value
	 *            值
	 * @return 新增加的节点
	 */
	public Chain add(String name, Object value) {
		Chain oldNext = next;
		next = new Chain(name, value, this.head, oldNext);
		return next;
	}

	/**
	 * @return 当前节点的名称
	 */
	public String name() {
		return name;
	}

	/**
	 * @return 当前节点的值
	 */
	public Object value() {
		return value;
	}

	/**
	 * @return 下一个节点
	 */
	public Chain next() {
		return next;
	}

	/**
	 * @return 整个链的第一环（头节点）
	 */
	public Chain head() {
		return head;
	}

	/**
	 * 根据 Entity 里的设定，更新整个链所有节点的名称。
	 * <p>
	 * 如果节点的名称是 Entity 的一个字段，则采用数据库字段的名称
	 * 
	 * @param entity
	 *            实体
	 * @return 链头节点
	 */
	public Chain updateBy(Entity<?> entity) {
		Chain c = head;
		while (c != null) {
			EntityField ef = entity.getField(c.name);
			if (null != ef) {
				c.name(ef.getColumnName());
			}
			c = c.next;
		}
		return head;
	}

	/**
	 * 由当前的名值链，生成一个对象
	 * 
	 * @param classOfT
	 *            对象类型
	 * @return 对象实例
	 */
	public <T> T toObject(Class<T> classOfT) {
		Mirror<T> mirror = Mirror.me(classOfT);
		T re = mirror.born();
		Chain c = head;
		while (c != null) {
			mirror.setValue(re, c.name(), c.value());
			c = c.next;
		}
		return re;
	}

	/**
	 * 由当前名值链，生成一个 Map
	 * 
	 * @return Map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Chain c = head;
		while (c != null) {
			map.put(c.name(), c.value());
			c = c.next;
		}
		return map;
	}

	/**
	 * 生成一个 JSON 字符串
	 */
	public String toString() {
		return Json.toJson(toMap());
	}

	/**
	 * 根据一个对象的字段 生成一个 Chain 对象
	 * <p>
	 * 这个对象可以是一个 POJO 或者是一个 Map。
	 * <p>
	 * 支持 FieldMatcher，即你可以通过 FieldMatcher 来指定你需要哪些字段加入 Chain
	 * 
	 * @param obj
	 *            对象，可以是一个 POJO 或者是一个 Map
	 * @param fm
	 *            指明可用字段，null 表示全部字段可用
	 * @return Chain 对象，null 表示对象中没有可用字段
	 * 
	 * @see net.strong.dao.FieldMatcher
	 */
	public static Chain from(Object obj, FieldMatcher fm) {
		if (null == obj)
			return null;
		Chain c = null;
		/*
		 * Is Map
		 */
		if (obj instanceof Map<?, ?>) {
			for (Entry<?, ?> en : ((Map<?, ?>) obj).entrySet()) {
				Object key = en.getKey();
				if (null == key)
					continue;
				String name = key.toString();
				if (null != fm && !fm.match(name))
					continue;
				Object v = en.getValue();
				if (null != fm && null == v && fm.isIgnoreNull())
					continue;
				if (c == null) {
					c = Chain.make(name, v);
				} else {
					c = c.add(name, v);
				}
			}
		}
		/*
		 * Is POJO
		 */
		else {
			Mirror<?> mirror = Mirror.me(obj.getClass());
			for (Field f : mirror.getFields()) {
				if (null != fm && !fm.match(f.getName()))
					continue;
				Object v = mirror.getValue(obj, f.getName());
				if (null != fm && null == v && fm.isIgnoreNull())
					continue;
				if (c == null) {
					c = Chain.make(f.getName(), v);
				} else {
					c = c.add(f.getName(), v);
				}
			}
		}
		return c;
	}

	/**
	 * 根据一个 POJO 对象的字段 生成一个 Chain 对象
	 * <p>
	 * 相当于 Chain.from(obj,null)
	 * 
	 * @param obj
	 *            POJO 对象
	 * @return Chain 对象
	 */
	public static Chain from(Object obj) {
		return from(obj, null);
	}

}
