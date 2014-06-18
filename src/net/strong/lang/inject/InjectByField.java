package net.strong.lang.inject;

import java.lang.reflect.Field;

import net.strong.castor.Castors;
import net.strong.lang.Lang;

public class InjectByField implements Injecting {

	private Field field;

	public InjectByField(Field field) {
		this.field = field;
		this.field.setAccessible(true);
	}

	public void inject(Object obj, Object value) {
		Object v = null;
		try {
			v = Castors.me().castTo(value, field.getType());
			field.set(obj, v);
		}
		catch (Exception e) {
			throw Lang.makeThrow(	"Fail to set '%s'[ %s ] to field %s.'%s' because: %s",
									value,
			                     	v,
									field.getDeclaringClass().getName(),
									field.getName(),
									e.getMessage());
		}
	}
}
