package net.strong.aop.asm;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import net.strong.aop.AbstractClassAgent;
import net.strong.aop.ClassDefiner;
import net.strong.aop.MethodInterceptor;
import net.strong.repo.org.objectweb.asm.Opcodes;
import net.strong.lang.Files;
import net.strong.lang.Streams;
import net.strong.log.Logs;

/**
 * 
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class AsmClassAgent extends AbstractClassAgent {

	static int CLASS_LEVEL = Opcodes.V1_5;
	
	private static final boolean debug = false;

	static {
		// 判断编译等级
		InputStream is = null;
		try {
			String classFileName = AsmClassAgent.class.getName().replace('.', '/') + ".class";
			is = AsmClassAgent.class.getResourceAsStream(classFileName);
			if (is == null)
				is = AsmClassAgent.class.getResourceAsStream("/"+classFileName);
			if (is != null && is.available() > 8) {
				is.skip(7);
				switch (is.read()) {
				case 50: // Java 1.6
					CLASS_LEVEL = Opcodes.V1_6;
				}
			}
		}
		catch (Throwable e) {}
		finally {
			Streams.safeClose(is);
			Logs.getLog(AsmClassAgent.class).debugf("AsmClassAgent will define class in Version %s",CLASS_LEVEL);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> Class<T> generate(ClassDefiner cd,
									Pair2[] pair2s,
									String newName,
									Class<T> klass,
									Constructor<T>[] constructors) {
		try {
			return (Class<T>) cd.load(newName);
		}
		catch (ClassNotFoundException e) {}
		Method[] methodArray = new Method[pair2s.length];
		List<MethodInterceptor>[] methodInterceptorList = new List[pair2s.length];
		for (int i = 0; i < pair2s.length; i++) {
			Pair2 pair2 = pair2s[i];
			methodArray[i] = pair2.method;
			methodInterceptorList[i] = pair2.listeners;
		}
		byte[] bytes = ClassY.enhandClass(klass, newName, methodArray, constructors);
		if (debug)
			Files.write(new File(newName), bytes);
		Class<T> newClass = (Class<T>) cd.define(newName, bytes);
		AopToolKit.injectFieldValue(newClass, methodArray, methodInterceptorList);
		return newClass;
	}

}
