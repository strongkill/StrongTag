package net.strong.ioc.val;

import java.util.Properties;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;

public class SysPropValue implements ValueProxy{
	
	private String name;
	
	public SysPropValue(String name) {
		this.name = name;
	}

	public Object get(IocMaking ing) {
		Properties properties = System.getProperties();
		if (properties != null)
			return properties.get(name);
		return null;
	}

}
