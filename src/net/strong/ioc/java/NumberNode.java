package net.strong.ioc.java;

import net.strong.ioc.IocMaking;
import net.strong.json.Json;

public class NumberNode extends ChainNode {

	private Object v;

	public NumberNode(String num) {
		v = Json.fromJson(num);
	}

	protected String asString() {
		return v.toString();
	}

	protected Object getValue(IocMaking ing, Object obj) throws Exception {
		return v;
	}

}
