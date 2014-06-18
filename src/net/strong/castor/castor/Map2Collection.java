package net.strong.castor.castor;

import java.util.Collection;
import java.util.Map;

import net.strong.castor.Castor;
import net.strong.castor.FailToCastObjectException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Map2Collection extends Castor<Map, Collection> {

	@Override
	public Collection cast(Map src, Class<?> toType, String... args)
			throws FailToCastObjectException {
		Collection coll = createCollection(src, toType);
		coll.add(src);
		return coll;
	}

}
