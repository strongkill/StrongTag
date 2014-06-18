package net.strong.ioc.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.strong.ioc.Ioc2;
import net.strong.ioc.IocContext;
import net.strong.ioc.IocException;
import net.strong.ioc.IocLoader;
import net.strong.ioc.IocLoading;
import net.strong.ioc.IocMaking;
import net.strong.ioc.ObjectLoadException;
import net.strong.ioc.ObjectMaker;
import net.strong.ioc.ObjectProxy;
import net.strong.ioc.ValueProxyMaker;
import net.strong.ioc.annotation.InjectName;
import net.strong.ioc.aop.MirrorFactory;
import net.strong.ioc.aop.impl.DefaultMirrorFactory;
import net.strong.ioc.loader.cached.CachedIocLoader;
import net.strong.ioc.loader.cached.CachedIocLoaderImpl;
import net.strong.ioc.meta.IocObject;
import net.strong.lang.Lang;
import net.strong.lang.Strings;
import net.strong.log.Log;
import net.strong.log.Logs;

/**
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author wendal(wendal1985@gmail.com)
 */
public class NutIoc implements Ioc2 {

	private static Log log = Logs.getLog(NutIoc.class);

	private static final String DEF_SCOPE = "app";

	/**
	 * 读取配置文件的 Loader
	 */
	private IocLoader loader;
	/**
	 * 缓存对象上下文环境
	 */
	private IocContext context;
	/**
	 * 装配对象的逻辑
	 */
	private ObjectMaker maker;
	/**
	 * 可扩展的"字段值"生成器
	 */
	private List<ValueProxyMaker> vpms;
	/**
	 * 反射工厂，封装 AOP 的逻辑
	 */
	private MirrorFactory mirrors;
	/**
	 * 对象默认生命周期范围名
	 */
	private String defaultScope;
	/**
	 * <ul>
	 * <li>缓存支持的对象值类型
	 * <li>如果 addValueProxyMaker() 被调用，这个缓存会被清空
	 * <li>createLoading() 将会检查这个缓存
	 * </ul>
	 */
	private Set<String> supportedTypes;

	public NutIoc(IocLoader loader) {
		this(loader, new ScopeContext(DEF_SCOPE), DEF_SCOPE);
	}

	public NutIoc(IocLoader loader, IocContext context, String defaultScope) {
		this(new ObjectMakerImpl(), loader, context, defaultScope);
	}

	protected NutIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope) {
		this(maker, loader, context, defaultScope, null);
	}

	protected NutIoc(	ObjectMaker maker,
						IocLoader loader,
						IocContext context,
						String defaultScope,
						MirrorFactory mirrors) {
		this.maker = maker;
		this.defaultScope = defaultScope;
		this.context = context;
		if (loader instanceof CachedIocLoader)
			this.loader = loader;
		else
			this.loader = CachedIocLoaderImpl.create(loader);
		vpms = new ArrayList<ValueProxyMaker>(5); // 预留五个位置，足够了吧
		addValueProxyMaker(new DefaultValueProxyMaker());

		// 初始化类工厂， 这是同 AOP 的连接点
		if (mirrors == null)
			this.mirrors = new DefaultMirrorFactory(this);
		else
			this.mirrors = mirrors;
	}

	/**
	 * @return 一个新创建的 IocLoading 对象
	 */
	private IocLoading createLoading() {
		if (null == supportedTypes) {
			synchronized (this) {
				if (null == supportedTypes) {
					// TODO 看看可不可以改成更快的 Set
					supportedTypes = new HashSet<String>();
					for (ValueProxyMaker maker : vpms) {
						String[] ss = maker.supportedTypes();
						if (ss != null)
							for (String s : ss)
								supportedTypes.add(s);
					}
				}
			}
		}
		return new IocLoading(supportedTypes);
	}

	public <T> T get(Class<T> type) throws IocException {
		InjectName inm = type.getAnnotation(InjectName.class);
		if (null != inm && (!Strings.isBlank(inm.value())))
			return get(type, inm.value());
		return get(type, Strings.lowerFirst(type.getSimpleName()));
	}

	public <T> T get(Class<T> type, String name, IocContext context) throws IocException {
		if (log.isDebugEnabled())
			log.debugf("Get '%s'<%s>", name, type);

		// 连接上下文
		IocContext cntx;
		if (null == context || context == this.context)
			cntx = this.context;
		else {
			if (log.isTraceEnabled())
				log.trace("Link contexts");
			cntx = new ComboContext(context, this.context);
		}

		// 创建对象创建时
		IocMaking ing = new IocMaking(this, mirrors, cntx, maker, vpms, name);

		// 从上下文缓存中获取对象代理
		ObjectProxy op = cntx.fetch(name);

		// 如果未发现对象
		if (null == op) {
			// 线程同步
			synchronized (this) {
				// 再次读取
				op = cntx.fetch(name);

				// 如果未发现对象
				if (null == op) {
					try {
						if (log.isDebugEnabled())
							log.debug("\t >> Load definition");

						// 读取对象定义
						IocObject iobj = loader.load(createLoading(), name);
						if (null == iobj)
							throw Lang.makeThrow("Undefined object '%s'", name);

						// 修正对象类型
						if (null == iobj.getType())
							if (null == type)
								throw Lang.makeThrow("NULL TYPE object '%s'", name);
							else
								iobj.setType(type);

						// 检查对象级别
						if (Strings.isBlank(iobj.getScope()))
							iobj.setScope(defaultScope);

						// 根据对象定义，创建对象，maker 会自动的缓存对象到 context 中
						if (log.isDebugEnabled())
							log.debugf("\t >> Make...'%s'<%s>", name, type);
						op = maker.make(ing, iobj);
					}
					// 处理异常
					catch (ObjectLoadException e) {
						throw new IocException(e, "For object [%s] - type:[%s]", name, type);
					}
				}
			}
		}
		return (T) op.get(type, ing);
	}

	public <T> T get(Class<T> type, String name) {
		return this.get(type, name, null);
	}

	public boolean has(String name) {
		return loader.has(name);
	}

	public void depose() {
		context.depose();
		if (loader instanceof CachedIocLoader)
			((CachedIocLoader) loader).clear();
		if (log.isDebugEnabled())
			log.debug("!!!Ioc is deposed, you can't use it anymore");
	}

	public void reset() {
		context.clear();
		if (loader instanceof CachedIocLoader)
			((CachedIocLoader) loader).clear();
	}

	public String[] getNames() {
		return loader.getName();
	}

	public void addValueProxyMaker(ValueProxyMaker vpm) {
		vpms.add(0, vpm);// 优先使用最后加入的ValueProxyMaker
	}

	public void setMaker(ObjectMaker maker) {
		this.maker = maker;
	}

	public void setMirrorFactory(MirrorFactory mirrors) {
		this.mirrors = mirrors;
	}

	public void setDefaultScope(String defaultScope) {
		this.defaultScope = defaultScope;
	}

}
