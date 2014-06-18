package net.strong.castor.castor;

import java.util.regex.Pattern;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

public class String2Pattern extends Castor<String, Pattern> {

	@Override
	public Pattern cast(String src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		try {
			return Pattern.compile(src);
		}
		catch (Exception e) {
			throw new FailToCastObjectException("Error regex: " + src, e);
		}
	}

}
