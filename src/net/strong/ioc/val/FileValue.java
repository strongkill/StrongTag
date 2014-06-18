package net.strong.ioc.val;

import net.strong.ioc.IocMaking;
import net.strong.ioc.ValueProxy;
import net.strong.lang.Files;

public class FileValue implements ValueProxy {

	private String path;

	public FileValue(String path) {
		this.path = path;
	}

	public Object get(IocMaking ing) {
		return Files.findFile(path);
	}

}
