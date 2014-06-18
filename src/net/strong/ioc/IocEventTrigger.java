package net.strong.ioc;

/**
 * 容器事件触发器
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public interface IocEventTrigger<T> {

	void trigger(T obj);

}
