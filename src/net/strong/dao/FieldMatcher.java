package net.strong.dao;

import java.util.regex.Pattern;

import net.strong.lang.Strings;

/**
 * 字段匹配器
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public class FieldMatcher {

	public static FieldMatcher make(String actived, String locked, boolean ignoreNull) {
		FieldMatcher fm = new FieldMatcher();
		fm.ignoreNull = ignoreNull;
		if (!Strings.isBlank(actived))
			fm.actived = Pattern.compile(actived);
		if (!Strings.isBlank(locked))
			fm.locked = Pattern.compile(locked);
		return fm;
	}

	/**
	 * 哪些字段可用
	 */
	private Pattern actived;
	/**
	 * 哪些字段不可用
	 */
	private Pattern locked;
	/**
	 * 是否忽略空值
	 */
	private boolean ignoreNull;

	public boolean isIgnoreNull() {
		return ignoreNull;
	}

	public boolean match(String str) {
		if (null != locked)
			if (locked.matcher(str).find())
				return false;
		if (null != actived)
			if (!actived.matcher(str).find())
				return false;
		return true;
	}

}
