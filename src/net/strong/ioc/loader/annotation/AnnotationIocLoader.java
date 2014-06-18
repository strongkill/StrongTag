package net.strong.ioc.loader.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.strong.castor.Castors;
import net.strong.ioc.IocLoader;
import net.strong.ioc.IocLoading;
import net.strong.ioc.ObjectLoadException;
import net.strong.ioc.meta.IocEventSet;
import net.strong.ioc.meta.IocField;
import net.strong.ioc.meta.IocObject;
import net.strong.ioc.meta.IocValue;
import net.strong.lang.Lang;
import net.strong.lang.Strings;
import net.strong.log.Log;
import net.strong.log.Logs;
import net.strong.resource.Scans;

/**
 * 
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class AnnotationIocLoader implements IocLoader {

	protected static final Log LOG = Logs.getLog(AnnotationIocLoader.class);

	private HashMap<String, IocObject> map = new HashMap<String, IocObject>();

	public AnnotationIocLoader(String... packages) {
		for (String packageZ : packages)
			for (Class<?> classZ : Scans.me().scanPackage(packageZ))
				addClass(classZ);
		if (LOG.isInfoEnabled())
			LOG.infof(	"Scan complete ! Found %s classes in %s base-packages!\nbeans = %s",
						map.size(),
						packages.length,
						Castors.me().castToString(map.keySet()));
	}

	private void addClass(Class<?> classZ) {
		if (classZ.isInterface()
			|| classZ.isMemberClass()
			|| classZ.isEnum()
			|| classZ.isAnnotation()
			|| classZ.isAnonymousClass())
			return;
		int modify = classZ.getModifiers();
		if (Modifier.isAbstract(modify) || (!Modifier.isPublic(modify)))
			return;
		IocBean iocBean = classZ.getAnnotation(IocBean.class);
		if (iocBean != null) {
			if (LOG.isDebugEnabled())
				LOG.debugf("Found a Class with Ioc-Annotation : %s", classZ);
			String beanName = iocBean.name();
			if (Strings.isBlank(beanName))
				beanName = Strings.lowerFirst(classZ.getSimpleName());
			IocObject iocObject = new IocObject();
			iocObject.setType(classZ);
			map.put(beanName, iocObject);

			iocObject.setSingleton(iocBean.singleton());
			if (!Strings.isBlank(iocBean.scope()))
				iocObject.setScope(iocBean.scope());

			// 配置构造方法
			if (iocBean.param().length > 0)
				for (String value : iocBean.param())
					iocObject.addArg(convert(value));

			// 设置Events
			IocEventSet eventSet = new IocEventSet();
			iocObject.setEvents(eventSet);
			if (!Strings.isBlank(iocBean.create()))
				eventSet.setCreate(iocBean.create().trim().intern());
			if (!Strings.isBlank(iocBean.depose()))
				eventSet.setCreate(iocBean.depose().trim().intern());
			if (!Strings.isBlank(iocBean.fetch()))
				eventSet.setCreate(iocBean.fetch().trim().intern());

			// 处理字段(以@Inject方式,位于字段)
			List<String> fieldList = new ArrayList<String>();
			Field[] fields = classZ.getDeclaredFields();
			for (Field field : fields) {
				Inject inject = field.getAnnotation(Inject.class);
				if (inject == null)
					continue;
				IocField iocField = new IocField();
				iocField.setName(field.getName());
				IocValue iocValue;
				if (Strings.isBlank(inject.value())) {
					iocValue = new IocValue();
					iocValue.setType("refer");
					iocValue.setValue(field.getName());
				} else
					iocValue = convert(inject.value());
				iocField.setValue(iocValue);
				iocObject.addField(iocField);
				fieldList.add(iocField.getName());
			}
			// 处理字段(以@Inject方式,位于set方法)
			Method[] methods = classZ.getMethods();
			for (Method method : methods) {
				Inject inject = method.getAnnotation(Inject.class);
				if (inject == null)
					continue;
				if (method.getName().startsWith("set")
					&& method.getName().length() > 3
					&& method.getParameterTypes().length == 1) {
					IocField iocField = new IocField();
					iocField.setName(Strings.lowerFirst(method.getName().substring(3)));
					IocValue iocValue;
					if (Strings.isBlank(inject.value())) {
						iocValue = new IocValue();
						iocValue.setType(IocValue.TYPE_REFER);
						iocValue.setValue(Strings.lowerFirst(method.getName().substring(3)));
					} else
						iocValue = convert(inject.value());
					iocField.setValue(iocValue);
					iocObject.addField(iocField);
					fieldList.add(iocField.getName());
				}
			}
			// 处理字段(以@IocBean.field方式),只允许引用同名的bean, 就映射为 refer:FieldName
			if (iocBean.field() != null && iocBean.field().length > 0) {
				for (String fieldInfo : iocBean.field()) {
					if (fieldList.contains(fieldInfo))
						throw Lang.makeThrow(	"Duplicate filed defined! Class=%s,FileName=%s",
												classZ,
												fieldInfo);
					IocField iocField = new IocField();
					iocField.setName(fieldInfo);
					IocValue iocValue = new IocValue();
					iocValue.setType(IocValue.TYPE_REFER);
					iocValue.setValue(fieldInfo);
					iocField.setValue(iocValue);
					iocObject.addField(iocField);
					fieldList.add(iocField.getName());
				}
			}
			if (LOG.isDebugEnabled())
				LOG.debugf("Processed Ioc Class : %s as [%s]", classZ, beanName);
		}
	}

	protected IocValue convert(String value) {
		IocValue iocValue = new IocValue();
		iocValue.setType(value.substring(0, value.indexOf(":")));
		iocValue.setValue(value.substring(value.indexOf(":") + 1));
		return iocValue;
	}

	public String[] getName() {
		return map.keySet().toArray(new String[map.size()]);
	}

	public boolean has(String name) {
		return map.containsKey(name);
	}

	public IocObject load(IocLoading loading, String name) throws ObjectLoadException {
		if (has(name))
			return map.get(name);
		throw new ObjectLoadException("Object '" + name + "' without define!");
	}
}
