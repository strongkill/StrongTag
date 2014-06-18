package net.strong.castor.castor;

import java.util.Collection;

import net.strong.castor.Castor;
import net.strong.castor.Castors;
import net.strong.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Collection2Object extends Castor<Collection, Object> {

	@Override
	public Object cast(Collection src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		if (src.size() == 0)
			return null;
		return Castors.me().castTo(src.iterator().next(), toType);
	}

}
