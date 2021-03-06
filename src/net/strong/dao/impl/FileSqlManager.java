package net.strong.dao.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.strong.lang.Files;
import net.strong.lang.Strings;
import net.strong.resource.NutResource;
import net.strong.resource.Scans;
import net.strong.resource.impl.FileResource;

public class FileSqlManager extends AbstractSqlManager {

	private String[] paths;

	private String regex;

	private boolean autoscan;

	public FileSqlManager(String... paths) {
		this.paths = paths;
		this.autoscan = true;
	}

	public String getRegex() {
		return regex;
	}

	public FileSqlManager setRegex(String regex) {
		this.regex = regex;
		return this;
	}

	public boolean isAutoscan() {
		return autoscan;
	}

	public FileSqlManager setAutoscan(boolean autoscan) {
		this.autoscan = autoscan;
		return this;
	}

	public void refresh() {
		// 准备对象
		List<NutResource> nrs = new LinkedList<NutResource>();

		// 按照路径解析
		for (String path : paths) {
			File f = Files.findFile(path);
			if (null == f)
				continue;
			if (f.isFile()) {
				nrs.add(new FileResource(f.getParentFile(), f));
				if (autoscan == false)
					continue;
			}
			List<NutResource> list = Scans.me().scan(path, regex);
			String parent = path;
			int pos = path.lastIndexOf('/');
			if (pos == -1)
				pos = path.lastIndexOf('\\');
			if (pos > 0)
				parent = path.substring(0, pos);

			if (Strings.isBlank(parent)) {
				nrs.addAll(list);
			} else {
				for (NutResource nr : list)
					if (autoscan) {
						if (nr.getName().startsWith(parent)) {
							nrs.add(nr);
						}
					} else {
						if (nr.getName().equals(path)) {
							nrs.add(nr);
						}
					}
			}
		}
		// 父类提供解析方法
		buildSQLMaps(nrs);
	}

}
