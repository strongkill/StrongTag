package net.strong.aop.interceptor;

import java.sql.Connection;

import net.strong.aop.InterceptorChain;
import net.strong.aop.MethodInterceptor;
import net.strong.lang.Lang;
import net.strong.trans.Atom;
import net.strong.trans.Trans;

/**
 * 可以插入事务的拦截器
 * <p/>
 * 默认事务登记为 Connection.TRANSACTION_READ_COMMITTED
 * <p/>
 * 可以在构建拦截器时设置
 * 
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class TransactionInterceptor implements MethodInterceptor {

	private int level;

	public TransactionInterceptor() {
		this.level = Connection.TRANSACTION_READ_COMMITTED;
	}

	public TransactionInterceptor(int level) {
		this.level = level;
	}

	public void filter(final InterceptorChain chain) {
		Trans.exec(level, new Atom() {
			public void run() {
				try {
					chain.doChain();
				}
				catch (Throwable e) {
					throw Lang.wrapThrow(e);
				}
			}
		});
	}

}
