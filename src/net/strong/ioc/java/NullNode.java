package net.strong.ioc.java;

import net.strong.ioc.IocMaking;

public class NullNode extends ChainNode {

	protected String asString() {
		return "null";
	}

	protected Object getValue(IocMaking ing, Object obj) throws Exception {
		return null;
	}

}
