package net.strong.util;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import org.apache.struts.upload.FormFile;

/**
 * 自定义的mime文件信息类
 * <p>Title: MimetypesFileTypeMap.java</p>
 * <p>Description: Development Lib For CRM</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:   </p>
 * @since 2007-4-2
 * @author Strong Yuan
 * @version 1.0
 */
public class MyMimetypesFileTypeMap extends MimetypesFileTypeMap{

	public MyMimetypesFileTypeMap(){
		addMimeTypes("application/vnd.ms-excel	xls XLS");
		addMimeTypes("application/x-zip-compressed	rar RAR");
		addMimeTypes("application/msword	doc DOC");
	}

	public synchronized void addMimeTypes(String arg0) {
		super.addMimeTypes(arg0);
	}

	public String getContentType(File arg0) {
		return super.getContentType(arg0);
	}
	public String getContentType(FormFile arg0){
		return getContentType(arg0.getFileName());
	}
	public synchronized String getContentType(String arg0) {
		return super.getContentType(arg0);
	}

}
