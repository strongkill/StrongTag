package net.strong.ioc.java;

import net.strong.ioc.IocMaking;

public class BooleanNode extends ChainNode {

	private boolean b;

	public BooleanNode(String s) {
		b = Boolean.parseBoolean(s);
	}

	protected String asString() {
		return String.valueOf(b);
	}

	protected Object getValue(IocMaking ing, Object obj) throws Exception {
		return b;
	}

}
