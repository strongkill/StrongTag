package net.strong.ioc.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.strong.ioc.IocContext;
import net.strong.ioc.ObjectProxy;
import net.strong.lang.Lang;
import net.strong.log.Log;
import net.strong.log.Logs;

/**
 * 自定义级别上下文对象
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public class ScopeContext implements IocContext {

	private static final Log log = Logs.getLog(ScopeContext.class);

	private String scope;
	private Map<String, ObjectProxy> objs;

	public ScopeContext(String scope) {
		this.scope = scope;
		objs = new HashMap<String, ObjectProxy>();
	}

	private void checkBuffer() {
		if (null == objs)
			throw Lang.makeThrow("Context '%s' had been deposed!", scope);
	}

	public Map<String, ObjectProxy> getObjs() {
		return objs;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ObjectProxy fetch(String name) {
		checkBuffer();
		return objs.get(name);
	}

	public boolean save(String scope, String name, ObjectProxy obj) {
		if (accept(scope)) {
			checkBuffer();
			synchronized (this) {
				if (!objs.containsKey(name)) {
					if (log.isDebugEnabled())
						log.debugf("Save object '%s' to [%s] ", name, scope);
					return null != objs.put(name, obj);
				}
			}
		}
		return false;
	}

	protected boolean accept(String scope) {
		return null != scope && this.scope.equals(scope);
	}

	public boolean remove(String scope, String name) {
		if (accept(scope)) {
			checkBuffer();

			synchronized (this) {
				if (!objs.containsKey(name)) {
					if (log.isDebugEnabled())
						log.debugf("Remove object '%s' from [%s] ", name, scope);
					return null != objs.remove(name);
				}
			}
		}
		return false;
	}

	public void clear() {
		checkBuffer();
		for (Entry<String, ObjectProxy> en : objs.entrySet()) {
			if (log.isDebugEnabled())
				log.debugf("Depose object '%s' ...", en.getKey());

			en.getValue().depose();
		}
		objs.clear();
	}

	public void depose() {
		clear();
		objs = null;
	}

}
