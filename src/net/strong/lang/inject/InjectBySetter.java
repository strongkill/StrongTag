package net.strong.lang.inject;

import java.lang.reflect.Method;

import net.strong.castor.Castors;
import net.strong.lang.Lang;

public class InjectBySetter implements Injecting {

	private Method setter;
	private Class<?> valueType;

	public InjectBySetter(Method setter) {
		this.setter = setter;
		valueType = setter.getParameterTypes()[0];
	}

	public void inject(Object obj, Object value) {
		Object v = null;
		try {
			v = Castors.me().castTo(value, valueType);
			setter.invoke(obj, v);
		}
		catch (Exception e) {
			throw Lang.makeThrow(	"Fail to set '%s'[ %s ] by setter %s.'%s()' because: %s",
									value,
									v,
									setter.getDeclaringClass().getName(),
									setter.getName(),
									e.getMessage());
		}
	}

}