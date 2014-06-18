package net.strong.ioc.java;

import net.strong.ioc.IocMaking;

public class IocContextNode extends ChainNode {

	@Override
	protected Object getValue(IocMaking ing, Object obj) throws Exception {
		return ing.getContext();
	}

	@Override
	protected String asString() {
		return "@Context";
	}

}
