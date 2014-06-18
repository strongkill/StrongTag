package net.strong.ioc.java;

import net.strong.ioc.IocMaking;

public class IocObjectNameNode extends ChainNode {

	@Override
	protected Object getValue(IocMaking ing, Object obj) throws Exception {
		return ing.getObjectName();
	}

	@Override
	protected String asString() {
		return "@Name";
	}

}
