package net.strong.castor.castor;

import java.io.File;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class File2String extends Castor<File, String> {

	@Override
	public String cast(File src, Class<?> toType, String... args) throws FailToCastObjectException {
		return src.getAbsolutePath();
	}

}
