package net.strong.taglib.util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.PageContext;

public interface ItagProcess {
	/**
	 * 初始化类
	 * @param pageMax
	 * @param MaxPage
	 * @param maxRecord
	 */
	void init(long pageMax,long MaxPage,long maxRecord);
	/**
	 * for dbListArray
	 * @param pageContext
	 * @param list
	 * @return
	 */
	ArrayList<HashMap<String,Object>> process(PageContext pageContext,ArrayList<HashMap<String,Object>> list); 
	
	/**
	 * for dbSpecifyValue
	 * @param pageContext
	 * @param hm
	 * @return
	 */
	HashMap<String,Object> process(PageContext pageContext,HashMap<String,Object> hm);
	/**
	 * for db:write
	 * @param pageContext
	 * @param o
	 * @return
	 */
	Object process(PageContext pageContext,Object o);

}
