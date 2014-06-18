package net.strong.ioc.java;

import net.strong.ioc.IocMaking;

public class IocSelfNode extends ChainNode {

	@Override
	protected Object getValue(IocMaking ing, Object obj) throws Exception {
		return ing.getIoc();
	}

	@Override
	protected String asString() {
		return "@Ioc";
	}

}
