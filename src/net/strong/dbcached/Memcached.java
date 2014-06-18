package net.strong.dbcached;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.strong.crypt.MD5;
import net.strong.crypt.MD5_deprecated;
/**
 * spy.memcached封装程序,初始一次后可以系统任何地方调用get和set访问.
 * @author Strong Yuan
 * @Date Jun 26, 2009
 * @Version
 */
public class Memcached {
	private static MemcachedClient c;
	private static Memcached instance = null;
	private static int DEFAULT_TIMEOUT_SECOND = 60;
	/**
	 * 初始化memcached
	 * @param servers
	 * @return
	 */
	public synchronized static Memcached getInstance(String servers) {
		if(instance == null){
			instance = new Memcached(servers);
		}
		return instance;
	}
	/**
	 * 构造函数
	 * @param servers
	 */
	private Memcached(String servers) {
		try {
			c=new MemcachedClient(AddrUtil.getAddresses(servers));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过key来获取对象
	 * @param key
	 * @return
	 */
	public Object get(String key){
		MD5 md5 = new MD5();
	    try {
			md5.Update(key, null);
		    key = md5.asHex();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return c.get(key);
	}
	/**
	 * 保存对象
	 * @param key 键值
	 * @param o 对象
	 */
	public void set(String key ,Object o){
		set(key,DEFAULT_TIMEOUT_SECOND,o);
	}
	/**
	 * 保存对象,并设置超时里长,主要针对分页.
	 * @param key
	 * @param timeoutsecond
	 * @param o
	 */
	public void set(String key,int timeoutsecond,Object o){
		if(o==null)return;
		MD5 md5 = new MD5();
	    try {
			md5.Update(key, null);
		    key = md5.asHex();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		c.set(key, timeoutsecond, o);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(MD5.getMD5ofStr("您好","utf-8"));
		System.out.println(MD5_deprecated.getInstance().getMD5ofStr("您好"));
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	/**
	public static void main(String[] args) throws Exception{
		final String st0 = "t0";
		final String st1 = "t1";
		final String st2 = "t2";
		final String ser = "192.168.1.104:11211 192.168.1.104:11212 192.168.1.104:11213";
		getInstance(ser).set(st0,3600,"t0");
		getInstance(ser).set(st1,3600,"t1");
		getInstance(ser).set(st2,3600,"t2");
		System.out.println("t0 : "+getInstance(ser).get(st0));
		System.out.println("t1 : "+getInstance(ser).get(st1));
		System.out.println("t2 : "+getInstance(ser).get(st2));

		Thread t0 = new Thread(){
			public void run() {
				while(true){
					System.out.println("t0 : "+getInstance(ser).get(st0));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}		
		};
		Thread t1 = new Thread(){
			public void run(){
				while(true){
					System.out.println("t1 : "+getInstance(ser).get(st1 ));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}		
		};
		Thread t2 = new Thread(){

			@Override
			public void run() {
				while(true){
					System.out.println("t2 : "+getInstance(ser).get(st2 ));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}		
		};
		t0.start();
		t1.start();
		t2.start();
	}**/
}