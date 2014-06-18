package net.strong.lang.util;

public class Nodes {

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> Node<T> create(T obj) {
		return new SimpleNode().set(obj);
	}

}
