package net.strong.dao.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.strong.lang.util.LinkedIntArray;

class VarIndexImpl implements VarIndex {

	VarIndexImpl() {
		map = new HashMap<String, LinkedIntArray>();
	}

	private Map<String, LinkedIntArray> map;

	void add(String name, int index) {
		LinkedIntArray lia = map.get(name);
		if (null == lia) {
			lia = new LinkedIntArray();
			map.put(name, lia);
		}
		lia.push(index);
	}

	int[] get(String name) {
		LinkedIntArray lia = map.get(name);
		if (null == lia)
			return null;
		return lia.toArray();
	}

	public Set<String> names() {
		return map.keySet();
	}

	Collection<LinkedIntArray> values() {
		return map.values();
	}

	public int size() {
		return map.size();
	}
}
