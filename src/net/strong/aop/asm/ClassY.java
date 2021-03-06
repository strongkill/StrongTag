package net.strong.aop.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.strong.aop.AopCallback;
import net.strong.repo.org.objectweb.asm.ClassWriter;
import net.strong.repo.org.objectweb.asm.MethodVisitor;
import net.strong.repo.org.objectweb.asm.Opcodes;
import net.strong.repo.org.objectweb.asm.Type;

/**
 * 
 * @author wendal(wendal1985@gmail.com)
 *
 */
class ClassY implements Opcodes {

	protected ClassWriter cw;

	protected String myName;

	protected String enhancedSuperName;

	protected Method[] methodArray;

	protected Constructor<?>[] constructors;

	public ClassY(Class<?> klass, String myName, Method[] methodArray, Constructor<?>[] constructors) {
		this.myName = myName.replace('.', '/');
		this.enhancedSuperName = klass.getName().replace('.', '/');
		this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(	AsmClassAgent.CLASS_LEVEL,
					ACC_PUBLIC,
					this.myName,
					null,
					enhancedSuperName,
					getParentInterfaces(klass));
		this.methodArray = methodArray;
		this.constructors = constructors;
	}

	protected String[] getParentInterfaces(Class<?> xClass) {
		Class<?> its[] = xClass.getInterfaces();
		if (its == null || its.length == 0)
			return new String[]{AopCallback.class.getName().replace('.', '/')};
		else {
			String[] iii = new String[its.length + 1];
			for (int i = 0; i < its.length; i++)
				iii[i] = its[i].getName().replace('.', '/');
			iii[its.length] = AopCallback.class.getName().replace('.', '/');
			return iii;
		}
	}

	protected String[] convertExp(Class<?>[] expClasses) {
		if (expClasses.length == 0)
			return null;
		String[] results = new String[expClasses.length];
		for (int i = 0; i < results.length; i++)
			results[i] = expClasses[i].getName().replace('.', '/');
		return results;
	}

	protected int getAccess(int modify) {
		if (Modifier.isProtected(modify))
			return ACC_PROTECTED;
		if (Modifier.isPublic(modify))
			return ACC_PUBLIC;
		return 0x00;
	}

	protected static int findMethodIndex(String name, String desc, Method[] methods) {
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (Type.getMethodDescriptor(method).equals(desc) && method.getName().equals(name))
				return i;
		}
		return -1;// 是否应该抛出异常呢?应该不可能发生的
	}

	protected void addConstructors() {
		for (Constructor<?> constructor : constructors) {
			String[] expClasses = convertExp(constructor.getExceptionTypes());
			String desc = Type.getConstructorDescriptor(constructor);
			int access = getAccess(constructor.getModifiers());
			MethodVisitor mv = cw.visitMethod(access, "<init>", desc, null, expClasses);
			new ChangeToChildConstructorMethodAdapter(mv, desc, access, enhancedSuperName).visitCode();
		}
	}

	protected byte[] toByteArray() {
		addField();
		addConstructors();
		addAopMethods();
		enhandMethod();
		return cw.toByteArray();
	}

	private void enhandMethod() {
		for (Method method : methodArray) {
			String methodName = method.getName();
			String methodDesc = Type.getMethodDescriptor(method);
			int methodAccess = getAccess(method.getModifiers());
			MethodVisitor mv = cw.visitMethod(	methodAccess,
												methodName,
												methodDesc,
												null,
												convertExp(method.getExceptionTypes()));
			int methodIndex = findMethodIndex(methodName, methodDesc, methodArray);
			new AopMethodAdapter(	mv,
									methodAccess,
									methodName,
									methodDesc,
									methodIndex,
									myName,
									enhancedSuperName).visitCode();
		}
	}

	private void addAopMethods() {
		new AopInvokeAdpter(methodArray,
							cw.visitMethod(	ACC_PUBLIC,
											"_aop_invoke",
											"(I[Ljava/lang/Object;)Ljava/lang/Object;",
											null,
											null),
							ACC_PUBLIC,
							"invoke",
							"(I[Ljava/lang/Object;)Ljava/lang/Object;",
							0,
							myName,
							enhancedSuperName).visitCode();
	}

	private void addField() {
		AopToolKit.addFields(cw);
	}

	public static <T> byte[] enhandClass(	Class<T> kclass,
											String myName,
											Method[] methodArray,
											Constructor<?>[] constructors) {
		return new ClassY(kclass, myName, methodArray, constructors).toByteArray();
	}
}
