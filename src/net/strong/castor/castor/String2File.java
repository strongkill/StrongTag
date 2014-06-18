package net.strong.castor.castor;

import java.io.File;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;
import net.strong.lang.Files;

public class String2File extends Castor<String, File> {

	@Override
	public File cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
		return Files.findFile(src);
	}

}
