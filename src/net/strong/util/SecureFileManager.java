package net.strong.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 受保护文件管理类
 * <p>
 * Title: SecureFileUploader.java
 * </p>
 * <p>
 * Description: Development Lib For
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @Date 2007-3-29
 * @author Strong Yuan
 * @version 1.0
 */
public class SecureFileManager {
	private static Logger log = Logger.getLogger("SecureFileUploader");

	// protected int diskBufferSize = 10 * 1024;
	/**
	 * 不允许client生成新的对象，可以调用静态方法就可以�?
	 */
	private SecureFileManager() {

	}



	/**
	 * 删除受保护的文件
	 *
	 * @param filename
	 *            直接输入文件路径
	 */
	private static void doSecureFileDelete(String filename) {
		String fullpath = null;
		String temp = filename;
		/*
		 * String file_sep = System.getProperty("file.separator");
		 *
		 * if("\\".equals(file_sep)) { char [] temps = temp.toCharArray();
		 * for(int i=0 ;i<temps.length;i++) { if(temps[i]=='/') temps[i] =
		 * '\\'; } temp = String.valueOf(temps); } temp = MyUtil.strTrim(temp);
		 * if(!Constants.SECUREFILEPATH.endsWith(file_sep)) fullpath =
		 * Constants.SECUREFILEPATH + file_sep;
		 */
		fullpath = /*BlogConstants.PHOTO_FULL_UPLOAD_DIR + */temp;

		if (log.isDebugEnabled())
			log.debug("file_name:" + fullpath);
		File file = new File(fullpath);
		if (file.exists()) {
			file.delete();
		} else {
			log.warn("file is not exist,file_name:" + fullpath);
		}
		file = null;
	}

	/**
	 * 取出拓展�?
	 *
	 * @param fileName
	 *            原始文件
	 * @return extName 拓展�?
	 */
	public static String getExt(String fileName) {
		int pos = fileName.lastIndexOf(".");
		String extName = ".txt";
		if (pos > -1)
			extName = fileName.substring(pos, fileName.length()); // 获到文件的扩展名
		if(".bmp".equalsIgnoreCase(extName))
			extName=".jpg";
		return extName;
	}
	public static String createAutoDir(String basePath){
		String path = createAutoDir();
		String full_path = basePath + path;
		File f = new File(full_path);
		if (!f.exists())
			f.mkdirs();
		return path;
	}
	/**
	 * 生成日期目录
	 *
	 * @return str_file生成的目�?
	 */
	public static String createAutoDir() {
		Date now_date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str_date = sdf.format(now_date);
		int len = str_date.length();

		String str_dd = "01";
		if (len == 8)
			str_dd = str_date.substring(6, 8);
		if (len > 6)
			str_date = str_date.substring(0, 6);

		// String sys_separator = System.getProperty("file.separator");

		//String str_file = File.separator + str_date + File.separator + str_dd + File.separator;

		String str_file = "/" + str_date + "/" + str_dd + "/";

		return str_file;
	}

	/**
	 * 生成随机文件�?
	 *
	 * @param fileName
	 *            原始文件�?
	 * @return 文件�?
	 */
	public static String getRamdomFileName(String fileName) {
		String extName = getExt(fileName);
		long time = System.currentTimeMillis();
		int i = (int) (Math.random() * 1000 + 1);
		long result = time * 1000 + i;
		return String.valueOf(result) + extName;
	}

	/**
	 * 通过图片流产生图片文件
	 *
	 * @param realPath
	 *            图片文件路径
	 * @param image
	 *            图片流
	 * @throws IOException
	 */
	public static void jpegEncode(String realPath, BufferedImage image)
			throws IOException {
		FileOutputStream ThF = null;
		try {
			ThF = new FileOutputStream(realPath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ThF);
			JPEGEncodeParam param = encoder
					.getDefaultJPEGEncodeParam((BufferedImage) image);
			param.setQuality(0.95f, true);
			encoder.encode((BufferedImage) image, param);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new IOException("写文件时出错 ");
		} finally {
			ThF.close();
			ThF = null;
			image = null;
		}

	}
	public static void gifEncode(String realPath,BufferedImage image) throws IOException{


	}

	public static void reduceImg(String imgsrc, String imgdist, float widthdist,
	        float heightdist) {
	    try {
	        File srcfile = new File(imgsrc);
	        if (!srcfile.exists()) {
	            return;
	        }
	        BufferedImage src = javax.imageio.ImageIO.read(srcfile);
	        int orig_w =src.getWidth();
	        int orig_h = src.getHeight();

	        if(orig_w>widthdist){

		        float r= orig_w/widthdist;
		        heightdist =orig_h / r;

				if(r>0){
					float tmp_height = orig_h / r;
					heightdist =(int) tmp_height;
				}

	        }else{
	        	widthdist = orig_w;
	        	heightdist = orig_h;
	        }


	        BufferedImage tag= new BufferedImage((int) widthdist, (int) heightdist,
	                BufferedImage.TYPE_INT_RGB);
	        Graphics g = tag.getGraphics();
	        Image tmp = src.getScaledInstance((int)widthdist, (int)heightdist,  Image.SCALE_SMOOTH);
	        g.drawImage(tmp, 0, 0,  null);
	///         tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,  Image.SCALE_AREA_AVERAGING), 0, 0,  null);

	        FileOutputStream out = new FileOutputStream(imgdist);
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam((BufferedImage) tag);
			param.setQuality(0.95f, true);
	        encoder.encode(tag,param);
	        out.close();
	        g = null;
	        tmp = null;
	        tag = null;
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
public static void main(String[] args){
	long current_time = System.currentTimeMillis();
	reduceImg("c:\\EE11.jpg", "c:\\EE11_s.jpg", 650, 480);
	System.out.println(System.currentTimeMillis()-current_time);
}
}
