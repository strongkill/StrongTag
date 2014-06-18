package net.strong.castor.castor;

import net.strong.castor.Castor;
import net.strong.lang.meta.Email;

public class String2Email extends Castor<String, Email> {

	@Override
	public Email cast(String src, Class<?> toType, String... args) {
		return new Email(src);
	}

}
