package net.strong.resource.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.strong.lang.util.Disks;
import net.strong.resource.NutResource;

/**
 * 记录了一个磁盘文件资源
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public class FileResource extends NutResource {

	private File file;

	public FileResource(File base, File file) {
		this(base.getAbsolutePath(), file);
	}

	public FileResource(String base, File file) {
		base = Disks.normalize(Disks.getCanonicalPath(base)).replace('\\', '/');
		this.name = Disks.normalize(Disks.getCanonicalPath(file.getAbsolutePath())).replace('\\', '/');
		this.name = this.name.substring(this.name.indexOf(base));
		this.file = file;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
