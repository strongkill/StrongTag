package net.strong.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import net.strong.bean.Constants;
import net.strong.util.imageMark;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;


import net.strong.exception.FileFormatUnSupportException;
import net.strong.exception.FileTooBigException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片上传类,有加水印功能
 *
 * <p>
 * Title: WaterMarkAndUpload.java
 * </p>
 * <p>
 * Description:WaterMarkAndUpload
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: cn.qt
 * </p>
 *
 * @author Strong Yuan
 * @sina 2007-7-9
 * @version 1.0
 */
public class WaterMarkAndUpload {
	private static Logger log = Logger
	.getLogger("UploadArticleFileFlashAction");

	/**
	 * 刪除文件(知道全路徑)
	 *
	 * @param basepath
	 * @param filename
	 */
	public static void doFileDelete(String basepath, String filename) {
		File f = new File(basepath + filename);
		if (f.exists())
			f.delete();
		f = null;
	}

	/**
	 * 刪除文件(通过servletContext获取全路径)
	 *
	 * @param servletContext
	 * @param filename
	 */
	public static void doFileDelete(ServletContext servletContext,
			String filename) {
		String basepath = Constants.getConfig(servletContext).getReal_path();
		doFileDelete(basepath, filename);
	}

	/**
	 * 刪除文件(通过servlet获取全路径)
	 *
	 * @param servlet
	 * @param filename
	 */
	public static void doFileDelete(Servlet servlet, String filename) {
		String basepath = Constants.getConfig(servlet).getReal_path();
		doFileDelete(basepath, filename);
	}


	/**
	 * 小峰
	 * @deprecated
	 * @param servlet
	 * @param file
	 * @param autowater
	 * @param filesize
	 * @param auto_small_pic
	 * @param small_pic_width
	 * @param small_pic_height
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> doWaterMarkAndUploadByRatio(Servlet servlet,
			FormFile file, boolean autowater, int filesize,
			boolean auto_small_pic, int small_pic_width, int small_pic_height)
			throws Exception {
		ArrayList<String> list = new ArrayList<String>(2);

		if(filesize>0 && file.getFileSize() > filesize)
			throw new IOException("strong.member.upload.photo.tobig");
		String ext = SecureFileManager.getExt(file.getFileName());
		if(!MyUtil.extIsAllowed(file.getFileName(), servlet)){
			throw new Exception("file Format not Support.");
		}
		String watermark_path = Constants.getConfig(servlet)
		.getWatermark_path();

		int real_height_big;
		int real_width_big;
		String basepath = Constants.getConfig(servlet).getReal_path();
		String autodir = SecureFileManager.createAutoDir();
		String filefullpath = autodir + SecureFileManager.getRamdomFileName(file.getFileName());
		String full_path = basepath + filefullpath;
		String tmppath = basepath + autodir;
		File f = new File(tmppath);
		if (!f.exists())
			f.mkdirs();
		f = null;
		if (auto_small_pic
				&& (".bmp".equalsIgnoreCase(ext)
						|| ".gif".equalsIgnoreCase(ext)
						|| ".jpg".equalsIgnoreCase(ext)
						|| ".jpeg".equalsIgnoreCase(ext) || ".png"
						.equalsIgnoreCase(ext))) { // jpg图片或jpeg图片可以进行缩小处理

			InputStream in = file.getInputStream();
			BufferedImage Bi = ImageIO.read(in);
			int in_h = Bi.getHeight();
			int in_w = Bi.getWidth();
			real_height_big = in_h;
			real_width_big = in_w;
			FileOutputStream ThR = new FileOutputStream(full_path);// realPath
			if(in_w>1024){
				try {
					BufferedImage bimage = new BufferedImage(1024,
							800, BufferedImage.TYPE_INT_RGB);
					Graphics g = bimage.getGraphics();
					Image tmp = Bi.getScaledInstance(1024, 800,  Image.SCALE_SMOOTH);
					g.drawImage(tmp, 0, 0,  null);
					File wm = null;

					if (autowater && watermark_path != null) {
						wm = new File(watermark_path);
					}

					// File wm = new File(watermark_path);
					if (wm != null && wm.exists()) {
						if (log.isDebugEnabled())
							log.debug("给图片加水印");
						wm = null;
						imageMark.createMark((BufferedImage) bimage, full_path,
								watermark_path, "down");// realPath => full_path
					} else {
						if (log.isDebugEnabled())
							log.debug("开始上传，不能进行给图片加水印处理");
						JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ThR);
						JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam((BufferedImage) bimage);
						param.setQuality(0.95f, true);
						encoder.encode(bimage,param);
						/*
						BufferedOutputStream fos = null;
						try {
							in = file.getInputStream();

							fos = new BufferedOutputStream(new FileOutputStream(
									new File(full_path)), 10 * 1024);
							int read = 0;
							byte buffer[] = new byte[10 * 1024];
							while ((read = in.read(buffer, 0, 10 * 1024)) > 0) {
								fos.write(buffer, 0, read);
							}
							buffer = null;
							fos.flush();
						} finally {
							in.close();
							in = null;
							fos.close();
							fos = null;
						}*/
					}
					g = null;
					bimage = null;

					if (log.isInfoEnabled())
						log.info("原始图片处理完成");

				} catch (Exception ex) {
					log.error(ex.getMessage());
					throw new IOException("写文件时出错 ");
				} finally {
					ThR.close();
					if (in != null)
						in.close();
					// Bi = null;
				}

				ThR = null;

				if (log.isInfoEnabled())
					log.info("完成原始图片上传，开始处理缩略图");
			}

			String smallpic = autodir
			+ SecureFileManager.getRamdomFileName(file.getFileName());
			// String smallFileName = path +smallpic;
			String smallpicfullpath = /*BlogConstants.PHOTO_FULL_UPLOAD_DIR
			+ */smallpic;
			double Ratio = 1.0;
			if (auto_small_pic && small_pic_height > 0 && small_pic_width > 0) {
				if ((in_h > small_pic_height) || (in_w > small_pic_width)) {
					if (in_h < in_w) // 产生缩放比率，以数值大的为基准
						Ratio = small_pic_width * 1.0 / in_w;
					else
						Ratio = small_pic_height * 1.0 / in_h;
				}

				Image Itemp = Bi.getScaledInstance(small_pic_width,
						small_pic_height, Image.SCALE_SMOOTH);

				AffineTransformOp op = new AffineTransformOp(AffineTransform
						.getScaleInstance(Ratio, Ratio), null);
				Itemp = op.filter(Bi, null);

				if (Ratio < 1.0)// && ("jpg".equalsIgnoreCase(ext) ||
					// "jpeg".equalsIgnoreCase(ext)))
				{

					BufferedImage bimage = new BufferedImage(small_pic_width,
							small_pic_height, BufferedImage.TYPE_INT_RGB);
					// bimage = (BufferedImage) Itemp;

					int t_width = ((BufferedImage) Itemp).getWidth();
					int t_height = ((BufferedImage) Itemp).getHeight();

					FileOutputStream out = new FileOutputStream(
							smallpicfullpath);// realpath = smallpicfullpath

					Graphics2D g = bimage.createGraphics();
					g.fillRect(0, 0, small_pic_width, small_pic_height);
					g.drawImage(Itemp, (small_pic_width - t_width) / 2,
							(small_pic_height - t_height) / 2, null); // 首先加入源图片

					SecureFileManager.jpegEncode(smallpicfullpath, bimage);// realpath
					// =
					// smallpicfullpath

					out.close();
					Itemp = null;
					bimage = null;
					g = null;
					if (log.isInfoEnabled())
						log.info("缩略图处理完成");
				} else {
					SecureFileManager.jpegEncode(smallpicfullpath,
							(BufferedImage) Itemp);// realpath
					// =
					// smallpicfullpath
				}
				// securefile.setPhoto_url_s(smallpic);
				// ThF = null;
				Itemp = null;
				op = null;

			}
			Bi = null;
			if (in != null) {
				in.close();
				in = null;
			}
			//System.out.println(filefullpath);
			list.add(filefullpath);
			list.add(smallpic);
		} else if (!auto_small_pic
				&& autowater
				&& (".bmp".equalsIgnoreCase(ext)
						|| ".jpg".equalsIgnoreCase(ext)
						|| ".jpeg".equalsIgnoreCase(ext) || ".png"
						.equalsIgnoreCase(ext)) && watermark_path != null) {
			if (log.isDebugEnabled())
				log.debug("开始上传须加水印但不须要缩略的图片");
			InputStream in = file.getInputStream();

			BufferedImage Bi = ImageIO.read(in);

			int in_h = Bi.getHeight();
			int in_w = Bi.getWidth();
			real_height_big = in_h;
			real_width_big = in_w;

			BufferedImage bimage = new BufferedImage(real_width_big,
					real_height_big, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bimage.createGraphics();
			g.drawImage(Bi, 0, 0, null); // 首先加入源图片

			File wm = new File(watermark_path);
			if (wm.exists()) {
				imageMark.createMark((BufferedImage) bimage, full_path,
						watermark_path, "down");// realPath => full_path
			} else {
				SecureFileManager.jpegEncode(full_path, bimage);
			}
			wm = null;
			g = null;
			bimage = null;
			Bi = null;
			list.add(filefullpath);
		} else if (!auto_small_pic && small_pic_height > 0
				&& small_pic_width > 0 && (".bmp".equalsIgnoreCase(ext)
						|| ".jpg".equalsIgnoreCase(ext)
						|| ".jpeg".equalsIgnoreCase(ext) || ".png"
						.equalsIgnoreCase(ext))) { // 不生成缩略,但改变原图片的大小.

			InputStream in = file.getInputStream();
			BufferedImage Bi = ImageIO.read(in);
			String smallpic = autodir
			+ SecureFileManager.getRamdomFileName(file.getFileName());

			String smallpicfullpath = /*BlogConstants.PHOTO_FULL_UPLOAD_DIR
			+ */smallpic;

			int orig_w =Bi.getWidth();
			int orig_h = Bi.getHeight();
			if(orig_w>small_pic_width){
				double r= (orig_w*1.0)/small_pic_width;
				if(r>0){
					float tmp_height = (float) (orig_h / r);
					small_pic_height =(int) tmp_height;
				}


				BufferedImage tag= new BufferedImage((int) small_pic_width, (int) small_pic_height,BufferedImage.TYPE_INT_RGB);

				Graphics g = tag.getGraphics();
				Image tmp = Bi.getScaledInstance(small_pic_width, small_pic_height,  Image.SCALE_SMOOTH);
				g.drawImage(tmp, 0, 0,  null);

				//tag.getGraphics().drawImage(Bi.getScaledInstance(small_pic_width, small_pic_height,  Image.SCALE_SMOOTH), 0, 0,  null);

				FileOutputStream out = new FileOutputStream(smallpicfullpath);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam((BufferedImage) tag);
				param.setQuality(0.95f, true);
				encoder.encode(tag,param);
				tag = null;
				out.close();
				encoder = null;
				param = null;
			}else{
				SecureFileManager.jpegEncode(smallpicfullpath,Bi);
			}
			/*

				Itemp = null;
				op = null;
			 */
			Bi = null;
			if (in != null) {
				in.close();
				in = null;
			}
			list.add(smallpic);
		} else { // 它文件，不能进行缩小等处理
			if (log.isDebugEnabled())
				log.debug("开始上传BMP图片，或其它文件，不能进行缩小等处理");
			InputStream in = null;
			BufferedOutputStream fos = null;
			try {
				in = file.getInputStream();

				fos = new BufferedOutputStream(new FileOutputStream(new File(
						full_path)),// realPath => full_path
						10 * 1024);
				int read = 0;
				byte buffer[] = new byte[10 * 1024];
				while ((read = in.read(buffer, 0, 10 * 1024)) > 0) {
					fos.write(buffer, 0, read);
				}
				buffer = null;
				fos.flush();
			} finally {
				in.close();
				in = null;
				fos.close();
				fos = null;
			}
			list.add(filefullpath);
		}
		file = null;
		if(list.size()<=1)
			list.add(list.get(0));
		return list;// filefullpath;
	}

	/**
	 * 上传图片,不作任何处理
	 *
	 * @param servlet
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String doWaterMarkAndUpload(Servlet servlet, FormFile file)
	throws Exception {
		return doWaterMarkAndUpload(servlet, file, false);
	}

	/**
	 * 上传图片,可对图片进行加水印处理.
	 *
	 * @param servlet
	 * @param file
	 * @param autowater
	 * @return
	 * @throws Exception
	 */
	public static String doWaterMarkAndUpload(Servlet servlet, FormFile file,
			boolean autowater) throws Exception {
		String str_filesize = Constants.getConfig(servlet)
		.getMaxsecurefilesize();
		int filesize = 3101024;
		try {
			filesize = Integer.parseInt(str_filesize);
		} catch (Exception e) {
			filesize = 3101024;
		}

		return doWaterMarkAndUpload(servlet, file, autowater, filesize);
	}

	/**
	 * 上传图片,可对图片进行加水印处理.可以限制图片大小
	 *
	 * @param servlet
	 * @param file
	 * @param autowater
	 * @param filesize
	 * @return
	 * @throws Exception
	 */
	public static String doWaterMarkAndUpload(Servlet servlet, FormFile file,
			boolean autowater, int filesize) throws Exception {
		return doWaterMarkAndUpload(servlet, file, autowater, filesize, false,
				0, 0).get(0);
	}

	/**
	 * 上传图片,可对图片进行加水印处理.生成小图片
	 *
	 * @param servlet
	 * @param file
	 * @param autowater
	 * @param filesize
	 * @param auto_small_pic
	 * @param small_pic_width
	 * @param small_pic_height
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> doWaterMarkAndUpload(Servlet servlet,
			FormFile file, boolean autowater, int filesize,
			boolean auto_small_pic, int small_pic_width, int small_pic_height)
			throws Exception {
		return doWaterMarkAndUpload(servlet, file, autowater, filesize,
				auto_small_pic, small_pic_width, small_pic_height, false, 0, 0);
	}

	/**
	 * 上传图片,可对图片进行加水印处理.生成小图片,可对原图片直行压缩
	 * @deprecated
	 * @param servlet
	 * @param file 要上传的文件
	 * @param autowater 是否需要加水印
	 * @param filesize 限制的文件大小
	 * @param auto_small_pic 是否生成缩略图的
	 * @param small_pic_width 缩略图的宽
	 * @param small_pic_height 缩略图的高
	 * @param fix_pic 是否需要进行原始图片压缩
	 * @param pic_width 原图压缩后的宽
	 * @param pic_height 原图压缩后的高
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> doWaterMarkAndUploadold(Servlet servlet,
			FormFile file, boolean autowater, int filesize,
			boolean auto_small_pic, int small_pic_width, int small_pic_height,
			boolean fix_pic, int pic_width, int pic_height) throws Exception {

		ArrayList<String> list = new ArrayList<String>(2);
		if (filesize > 0 && file.getFileSize() > filesize)
			throw new FileTooBigException("strong.member.upload.photo.tobig");
		String ext = SecureFileManager.getExt(file.getFileName());
		if (!MyUtil.extIsAllowed(file.getFileName(), servlet)) {
			throw new FileFormatUnSupportException("file Format not Support." + file.getFileName());
		}
		String watermark_path = Constants.getConfig(servlet)
		.getWatermark_path();

		int real_height_big;
		int real_width_big;
		String basepath = Constants.getConfig(servlet).getReal_path();
		String autodir = SecureFileManager.createAutoDir();
		String filefullpath = autodir
		+ SecureFileManager.getRamdomFileName(file.getFileName());
		String full_path = basepath + filefullpath;
		String tmppath = basepath + autodir;
		File f = new File(tmppath);
		if (!f.exists())
			f.mkdirs();
		f = null;

		/**
		 * 处理原图
		 */

		if (auto_small_pic
				&& (".bmp".equalsIgnoreCase(ext)
						|| ".gif".equalsIgnoreCase(ext)
						|| ".jpg".equalsIgnoreCase(ext)
						|| ".jpeg".equalsIgnoreCase(ext) || ".png"
						.equalsIgnoreCase(ext))) { // jpg图片或jpeg图片可以进行缩小处理
			InputStream in = file.getInputStream();
			BufferedImage Bi = ImageIO.read(in);
			int in_h = Bi.getHeight();
			int in_w = Bi.getWidth();
			real_height_big = in_h;
			real_width_big = in_w;
			FileOutputStream ThR = new FileOutputStream(full_path);// realPath

			try {
				BufferedImage bimage = new BufferedImage(real_width_big,
						real_height_big, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = bimage.createGraphics();
				g.drawImage(Bi, 0, 0, null); // 首先加入源图片
				File wm = null;

				if (autowater && watermark_path != null) {
					wm = new File(watermark_path);
				}
				if (wm != null && wm.exists()) {
					if (log.isDebugEnabled())
						log.debug("给图片加水印");
					wm = null;
					imageMark.createMark((BufferedImage) bimage, full_path,
							watermark_path, "down");
				} else {
					if (log.isDebugEnabled())
						log.debug("开始上传，不能进行给图片加水印处理");
					SecureFileManager.jpegEncode(full_path, bimage);
				}
				g = null;
				bimage = null;

				if (log.isInfoEnabled())
					log.info("原始图片处理完成");

			} catch (Exception ex) {
				log.error(ex.getMessage());
				throw new IOException("写文件时出错 ");
			} finally {
				ThR.close();
				if (in != null)
					in.close();
			}

			ThR = null;

			if (log.isInfoEnabled())
				log.info("完成原始图片上传，开始处理缩略图");

			// 生成缩略图
			String smallpic = autodir
			+ SecureFileManager.getRamdomFileName(file.getFileName());
			String smallpicfullpath = /*BlogConstants.PHOTO_FULL_UPLOAD_DIR
			+ */smallpic;
			double Ratio = 1.0;
			if (auto_small_pic && small_pic_height > 0 && small_pic_width > 0) {
				if ((in_h > small_pic_height) || (in_w > small_pic_width)) {
					if (in_h < in_w) // 产生缩放比率，以数值大的为基准
						Ratio = small_pic_width * 1.0 / in_w;
					else
						Ratio = small_pic_height * 1.0 / in_h;
				}

				Image Itemp = Bi.getScaledInstance(small_pic_width,
						small_pic_height, Image.SCALE_SMOOTH);

				AffineTransformOp op = new AffineTransformOp(AffineTransform
						.getScaleInstance(Ratio, Ratio), null);
				Itemp = op.filter(Bi, null);

				if (Ratio < 1.0) {
					BufferedImage bimage = new BufferedImage(small_pic_width,
							small_pic_height, BufferedImage.TYPE_INT_RGB);

					int t_width = ((BufferedImage) Itemp).getWidth();
					int t_height = ((BufferedImage) Itemp).getHeight();

					FileOutputStream out = new FileOutputStream(
							smallpicfullpath);

					Graphics2D g = bimage.createGraphics();
					g.fillRect(0, 0, small_pic_width, small_pic_height);
					g.drawImage(Itemp, (small_pic_width - t_width) / 2,
							(small_pic_height - t_height) / 2, null); // 首先加入源图片

					SecureFileManager.jpegEncode(smallpicfullpath, bimage);
					out.close();
					Itemp = null;
					bimage = null;
					g = null;
					if (log.isInfoEnabled())
						log.info("缩略图处理完成");
				} else {
					SecureFileManager.jpegEncode(smallpicfullpath,
							(BufferedImage) Itemp);
				}
				Itemp = null;
				op = null;

			}
			Bi = null;
			if (in != null) {
				in.close();
				in = null;
			}
			list.add(filefullpath);
			list.add(smallpic);
		} else if (!auto_small_pic
				&& autowater
				&& (".bmp".equalsIgnoreCase(ext)
						|| ".jpg".equalsIgnoreCase(ext)
						|| ".jpeg".equalsIgnoreCase(ext) || ".png"
						.equalsIgnoreCase(ext)) && watermark_path != null) {
			if (log.isDebugEnabled())
				log.debug("开始上传须加水印但不须要缩略的图片");
			InputStream in = file.getInputStream();

			BufferedImage Bi = ImageIO.read(in);

			int in_h = Bi.getHeight();
			int in_w = Bi.getWidth();
			real_height_big = in_h;
			real_width_big = in_w;

			BufferedImage bimage = new BufferedImage(real_width_big,
					real_height_big, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bimage.createGraphics();
			g.drawImage(Bi, 0, 0, null); // 首先加入源图片

			File wm = new File(watermark_path);
			if (wm.exists()) {
				imageMark.createMark((BufferedImage) bimage, full_path,
						watermark_path, "down");// realPath => full_path
			} else {
				SecureFileManager.jpegEncode(full_path, bimage);
			}
			wm = null;
			g = null;
			bimage = null;
			Bi = null;
			list.add(filefullpath);
		} else if (!auto_small_pic
				&& small_pic_height > 0
				&& small_pic_width > 0
				&& (".bmp".equalsIgnoreCase(ext)
						|| ".jpg".equalsIgnoreCase(ext)
						|| ".jpeg".equalsIgnoreCase(ext) || ".png"
						.equalsIgnoreCase(ext))) { // 不生成缩略,但改变原图片的大小.

			InputStream in = file.getInputStream();
			BufferedImage Bi = ImageIO.read(in);
			String smallpic = autodir
			+ SecureFileManager.getRamdomFileName(file.getFileName());

			String smallpicfullpath =/* BlogConstants.PHOTO_FULL_UPLOAD_DIR
			+ */smallpic;

			int orig_w = Bi.getWidth();
			int orig_h = Bi.getHeight();
			if (orig_w > small_pic_width) {
				double r = (orig_w * 1.0) / small_pic_width;
				if (r > 0) {
					float tmp_height = (float) (orig_h / r);
					small_pic_height = (int) tmp_height;
				}
				BufferedImage tag = new BufferedImage((int) small_pic_width,
						(int) small_pic_height, BufferedImage.TYPE_INT_RGB);

				Graphics g = tag.getGraphics();
				Image tmp = Bi.getScaledInstance(small_pic_width,
						small_pic_height, Image.SCALE_SMOOTH);
				g.drawImage(tmp, 0, 0, null);
				FileOutputStream out = new FileOutputStream(smallpicfullpath);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder
				.getDefaultJPEGEncodeParam((BufferedImage) tag);
				param.setQuality(0.95f, true);
				encoder.encode(tag, param);
				tag = null;
				out.close();
				encoder = null;
				param = null;
			} else {
				SecureFileManager.jpegEncode(smallpicfullpath, Bi);
			}
			Bi = null;
			if (in != null) {
				in.close();
				in = null;
			}
			list.add(smallpic);
		} else { // 它文件，不能进行缩小等处理
			if (log.isDebugEnabled())
				log.debug("开始上传其它文件，不能进行缩小等处理");
			InputStream in = null;
			BufferedOutputStream fos = null;
			try {
				in = file.getInputStream();

				fos = new BufferedOutputStream(new FileOutputStream(new File(
						full_path)),// realPath => full_path
						10 * 1024);
				int read = 0;
				byte buffer[] = new byte[10 * 1024];
				while ((read = in.read(buffer, 0, 10 * 1024)) > 0) {
					fos.write(buffer, 0, read);
				}
				buffer = null;
				fos.flush();
			} finally {
				in.close();
				in = null;
				fos.close();
				fos = null;
			}
			list.add(filefullpath);
		}
		file = null;
		if (list.size() <= 1)
			list.add(list.get(0));
		return list;// filefullpath;
	}

	public static void main(String[] args) throws Exception {
		File file = new File("C://Bluehills.bmp");
		FileInputStream in = new FileInputStream(file);// file.
		// .getInputStream();
		BufferedImage Bi = ImageIO.read(in);
		int in_h = Bi.getHeight();
		int in_w = Bi.getWidth();
		int real_height_big = in_h;
		int real_width_big = in_w;
		String full_path = "C://Bluehills.jpg";
		boolean autowater = false;
		String watermark_path = "C://Bluehills_s.jpg";
		FileOutputStream ThR = new FileOutputStream(full_path);// realPath

		try {
			BufferedImage bimage = new BufferedImage(real_width_big,
					real_height_big, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bimage.createGraphics();
			g.drawImage(Bi, 0, 0, null); // 首先加入源图片
			File wm = null;

			if (autowater && watermark_path != null) {
				wm = new File(watermark_path);
			}

			// File wm = new File(watermark_path);
			if (wm != null && wm.exists()) {
				if (log.isDebugEnabled())
					log.debug("给图片加水印");
				wm = null;
				imageMark.createMark((BufferedImage) bimage, full_path,
						watermark_path, "down");// realPath => full_path
			} else {
				if (log.isDebugEnabled())
					log.debug("开始上传，不能进行给图片加水印处理");
				SecureFileManager.jpegEncode(full_path, bimage);
			}
			g = null;
			bimage = null;

			if (log.isInfoEnabled())
				log.info("原始图片处理完成");

		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new IOException("写文件时出错 ");
		} finally {
			ThR.close();
			if (in != null)
				in.close();
			// Bi = null;
		}

		ThR = null;

		if (log.isInfoEnabled())
			log.info("完成原始图片上传，开始处理缩略图");

		// 生成缩略图
		String smallpic = SecureFileManager.getRamdomFileName(file.getName());
		// String smallFileName = path +smallpic;
		String smallpicfullpath = "C://" + smallpic;
		double Ratio = 1.0;
		boolean auto_small_pic = true;
		int small_pic_height = 124;
		int small_pic_width = 88;
		if (auto_small_pic && small_pic_height > 0 && small_pic_width > 0) {
			if ((in_h > small_pic_height) || (in_w > small_pic_width)) {
				if (in_h < in_w) // 产生缩放比率，以数值大的为基准
					Ratio = small_pic_width * 1.0 / in_w;
				else
					Ratio = small_pic_height * 1.0 / in_h;
			}

			Image Itemp = Bi.getScaledInstance(small_pic_width,
					small_pic_height, Image.SCALE_SMOOTH);

			AffineTransformOp op = new AffineTransformOp(AffineTransform
					.getScaleInstance(Ratio, Ratio), null);
			Itemp = op.filter(Bi, null);

			if (Ratio < 1.0)// && ("jpg".equalsIgnoreCase(ext) ||
				// "jpeg".equalsIgnoreCase(ext)))
			{

				BufferedImage bimage = new BufferedImage(small_pic_width,
						small_pic_height, BufferedImage.TYPE_INT_RGB);
				// bimage = (BufferedImage) Itemp;

				int t_width = ((BufferedImage) Itemp).getWidth();
				int t_height = ((BufferedImage) Itemp).getHeight();

				FileOutputStream out = new FileOutputStream(smallpicfullpath);// realpath
				// =
				// smallpicfullpath

				Graphics2D g = bimage.createGraphics();
				g.fillRect(0, 0, small_pic_width, small_pic_height);
				g.drawImage(Itemp, (small_pic_width - t_width) / 2,
						(small_pic_height - t_height) / 2, null); // 首先加入源图片

				SecureFileManager.jpegEncode(smallpicfullpath, bimage);// realpath
				// =
				// smallpicfullpath

				out.close();
				Itemp = null;
				bimage = null;
				g = null;
				if (log.isInfoEnabled())
					log.info("缩略图处理完成");
			} else {
				SecureFileManager.jpegEncode(smallpicfullpath,
						(BufferedImage) Itemp);// realpath
				// =
				// smallpicfullpath
			}
			// securefile.setPhoto_url_s(smallpic);
			// ThF = null;
			Itemp = null;
			op = null;

		}
		Bi = null;
		if (in != null) {
			in.close();
			in = null;
		}
		//list.add(filefullpath);
		//list.add(smallpic);
	}

	/**
	 * 上传图片,可对图片进行加水印处理.生成小图片,可对原图片直行压缩
	 * @param servlet
	 * @param file 要上传的文件
	 * @param autowater 是否需要加水印
	 * @param filesize 限制的文件大小
	 * @param auto_small_pic 是否生成缩略图的
	 * @param small_pic_width 缩略图的宽
	 * @param small_pic_height 缩略图的高
	 * @param fix_pic 是否需要进行原始图片压缩
	 * @param pic_width 原图压缩后的宽
	 * @param pic_height 原图压缩后的高
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> doWaterMarkAndUpload(Servlet servlet,
			FormFile file, boolean autowater, int filesize,
			boolean auto_small_pic, int small_pic_width, int small_pic_height,
			boolean fix_pic, int pic_width, int pic_height) throws Exception {

		ArrayList<String> list = new ArrayList<String>(2);
		if (filesize > 0 && file.getFileSize() > filesize)
			throw new FileTooBigException("strong.member.upload.photo.tobig");
		String ext = SecureFileManager.getExt(file.getFileName());
		if (!MyUtil.extIsAllowed(file.getFileName(), servlet)) {
			throw new FileFormatUnSupportException("file Format not Support." + file.getFileName());
		}
		String watermark_path = Constants.getConfig(servlet) .getWatermark_path();

		int real_height_big;
		int real_width_big;
		String basepath ="";//BlogConstants.PHOTO_FULL_UPLOAD_DIR; // Constants.getConfig(servlet).getReal_path();
		String autodir = SecureFileManager.createAutoDir();
		//大图片
		String filefullpath = autodir + SecureFileManager.getRamdomFileName(file.getFileName());
		String full_path = basepath + filefullpath;
		//小图片
		String smallpicfullpath = autodir + SecureFileManager.getRamdomFileName(file.getFileName());
		String small_path = basepath + smallpicfullpath;

		//创建目录
		String tmppath = basepath + autodir;
		File f = new File(tmppath);
		if (!f.exists())
			f.mkdirs();
		f = null;


		if((".bmp".equalsIgnoreCase(ext) ||
				/*".gif".equalsIgnoreCase(ext) ||*/
				".jpg".equalsIgnoreCase(ext) ||
				".jpeg".equalsIgnoreCase(ext) ||
				".png".equalsIgnoreCase(ext))){


			/**
			 * 首先加入源图片
			 */
			InputStream in = file.getInputStream();
			BufferedImage Bi = ImageIO.read(in);
			int in_h = Bi.getHeight();
			int in_w = Bi.getWidth();
			real_height_big = in_h;
			real_width_big = in_w;
			//FileOutputStream ThR = new FileOutputStream(full_path);// realPath

			//Graphics2D g;


			//ThR = null;

			if (log.isInfoEnabled())
				log.info("完成原始图处理!");


			if(fix_pic && pic_height>0 && pic_width>0){//压缩原图片
				int orig_w = Bi.getWidth();
				int orig_h = Bi.getHeight();
				if (orig_w > pic_width) {
					double r = (orig_w * 1.0) / pic_width;
					if (r > 0) {
						float tmp_height = (float) (orig_h / r);
						pic_height = (int) tmp_height;
					}
					BufferedImage tag = new BufferedImage((int) pic_width, (int) pic_height, BufferedImage.TYPE_INT_RGB);

					Graphics g =  tag.getGraphics();
					Image tmp = Bi.getScaledInstance(pic_width, pic_height, Image.SCALE_SMOOTH);
					g.drawImage(tmp, 0, 0, null);
					FileOutputStream out = new FileOutputStream(full_path);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam((BufferedImage) tag);
					param.setQuality(0.95f, true);
					encoder.encode(tag, param);
					tmp = null;
					g = null;
					tag = null;
					out.close();
					encoder = null;
					param = null;
				} else {
					SecureFileManager.jpegEncode(full_path, Bi);
				}
				if (in != null) {
					in.close();
					in = null;
				}
				//list.add(smallpic);
			}else{//不压缩原图
				try {
					BufferedImage bimage = new BufferedImage(real_width_big, real_height_big, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = bimage.createGraphics();
					g.drawImage(Bi, 0, 0, null);

					/*
					File wm = null;
					if (autowater && watermark_path != null) {
						wm = new File(watermark_path);
					}
					if (wm != null && wm.exists()) {//给图片加水印
						wm = null;
						imageMark.createMark((BufferedImage) bimage, full_path, watermark_path, "down");
					} else {//开始上传，不给图片加水印处理*/
					SecureFileManager.jpegEncode(full_path, bimage);
					//}
					bimage = null;
					g  = null;
					if (log.isInfoEnabled())log.info("原始图片处理完成");
				} catch (Exception ex) {
					log.error(ex.getMessage());
					throw new IOException("写文件时出错 ");
				} finally {
					//ThR.close();
					if (in != null)
						in.close();
				}
			}
			list.add(filefullpath);
			//生成缩略图
			if (auto_small_pic) { // jpg图片或jpeg图片可以进行缩小处理
				double Ratio = 1.0;
				if (auto_small_pic && small_pic_height > 0 && small_pic_width > 0) {
					if ((in_h > small_pic_height) || (in_w > small_pic_width)) {
						if (in_h < in_w) // 产生缩放比率，以数值大的为基准
							Ratio = small_pic_width * 1.0 / in_w;
						else
							Ratio = small_pic_height * 1.0 / in_h;
					}

					Image Itemp = Bi.getScaledInstance(small_pic_width, small_pic_height, Image.SCALE_SMOOTH);

					AffineTransformOp op = new AffineTransformOp(AffineTransform .getScaleInstance(Ratio, Ratio), null);
					Itemp = op.filter(Bi, null);

					if (Ratio < 1.0) {
						BufferedImage bimage = new BufferedImage(small_pic_width, small_pic_height, BufferedImage.TYPE_INT_RGB);

						int t_width = ((BufferedImage) Itemp).getWidth();
						int t_height = ((BufferedImage) Itemp).getHeight();

						FileOutputStream out = new FileOutputStream(small_path);

						Graphics2D g = bimage.createGraphics();
						g.fillRect(0, 0, small_pic_width, small_pic_height);
						g.drawImage(Itemp, (small_pic_width - t_width) / 2, (small_pic_height - t_height) / 2, null); // 首先加入源图片

						SecureFileManager.jpegEncode(small_path, bimage);
						out.close();
						Itemp = null;
						bimage = null;
						g = null;
						if (log.isInfoEnabled())
							log.info("缩略图处理完成");
					} else {
						SecureFileManager.jpegEncode(small_path, (BufferedImage) Itemp);
					}
					Itemp = null;
					op = null;

				}
				Bi = null;
				if (in != null) {
					in.close();
					in = null;
				}
				list.add(smallpicfullpath);
			} else if (!auto_small_pic && autowater && (!".gif".equalsIgnoreCase(ext)) && watermark_path != null) {
				//开始上传须加水印但不须要缩略的图片,不改变原图片大小
				/*
				if (log.isDebugEnabled())
					log.debug("开始上传须加水印但不须要缩略的图片");

				BufferedImage bimage = new BufferedImage(real_width_big,
						real_height_big, BufferedImage.TYPE_INT_RGB);

				Graphics2D g = bimage.createGraphics();
				g.drawImage(Bi, 0, 0, null); // 首先加入源图片

				File wm = new File(watermark_path);
				if (wm.exists()) {
					imageMark.createMark((BufferedImage) bimage, full_path,
							watermark_path, "down");// realPath => full_path
				} else {
					SecureFileManager.jpegEncode(full_path, bimage);
				}
				wm = null;
				g = null;
				bimage = null;
				Bi = null;
				list.add(filefullpath);*/
			} else if (!auto_small_pic && small_pic_height > 0 && small_pic_width > 0) { // 不生成缩略,但改变原图片的大小. && (!".gif".equalsIgnoreCase(ext))
				/*InputStream in = file.getInputStream();
				BufferedImage Bi = ImageIO.read(in);
				String smallpic = autodir
				+ SecureFileManager.getRamdomFileName(file.getFileName());

				String smallpicfullpath = BlogConstants.PHOTO_FULL_UPLOAD_DIR
				+ smallpic;

				int orig_w = Bi.getWidth();
				int orig_h = Bi.getHeight();
				if (orig_w > small_pic_width) {
					double r = (orig_w * 1.0) / small_pic_width;
					if (r > 0) {
						float tmp_height = (float) (orig_h / r);
						small_pic_height = (int) tmp_height;
					}
					BufferedImage tag = new BufferedImage((int) small_pic_width,
							(int) small_pic_height, BufferedImage.TYPE_INT_RGB);

					Graphics g = tag.getGraphics();
					Image tmp = Bi.getScaledInstance(small_pic_width,
							small_pic_height, Image.SCALE_SMOOTH);
					g.drawImage(tmp, 0, 0, null);
					FileOutputStream out = new FileOutputStream(smallpicfullpath);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					JPEGEncodeParam param = encoder
					.getDefaultJPEGEncodeParam((BufferedImage) tag);
					param.setQuality(0.95f, true);
					encoder.encode(tag, param);
					tag = null;
					out.close();
					encoder = null;
					param = null;
				} else {
					SecureFileManager.jpegEncode(smallpicfullpath, Bi);
				}
				Bi = null;
				if (in != null) {
					in.close();
					in = null;
				}
				list.add(smallpic);*/
			}
			Bi=null;
		} else { // 它文件，不能进行缩小等处理
			if (log.isDebugEnabled())
				log.debug("开始上传其它文件，不能进行缩小等处理");
			InputStream in = null;
			BufferedOutputStream fos = null;
			try {
				in = file.getInputStream();

				fos = new BufferedOutputStream(new FileOutputStream(new File(
						full_path)),// realPath => full_path
						10 * 1024);
				int read = 0;
				byte buffer[] = new byte[10 * 1024];
				while ((read = in.read(buffer, 0, 10 * 1024)) > 0) {
					fos.write(buffer, 0, read);
				}
				buffer = null;
				fos.flush();
			} finally {
				in.close();
				in = null;
				fos.close();
				fos = null;
			}
			list.add(filefullpath);
		}
		file = null;
		if (list.size() <= 1)
			list.add(list.get(0));
		return list;// filefullpath;
	}

	/**
	 * 上传图片,可对图片进行加水印处理.生成小图片,可对原图片直行压缩
	 * @param servlet
	 * @param file 要上传的文件
	 * @param autowater 是否需要加水印
	 * @param filesize 限制的文件大小
	 * @param auto_small_pic 是否生成缩略图的
	 * @param small_pic_width 缩略图的宽
	 * @param small_pic_height 缩略图的高
	 * @param fix_pic 是否需要进行原始图片压缩
	 * @param pic_width 原图压缩后的宽
	 * @param pic_height 原图压缩后的高
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> doWaterMarkAndUploadSmallPic(Servlet servlet,
			FormFile file, boolean autowater, int filesize,
			boolean auto_small_pic, int small_pic_width, int small_pic_height,
			boolean fix_pic, int pic_width, int pic_height) throws Exception {

		ArrayList<String> list = new ArrayList<String>(2);
		if (filesize > 0 && file.getFileSize() > filesize)
			throw new IOException("strong.member.upload.photo.tobig");
		String ext = SecureFileManager.getExt(file.getFileName());
		if (!MyUtil.extIsAllowed(file.getFileName(), servlet)) {
			throw new Exception("file Format not Support.");
		}
		String watermark_path = Constants.getConfig(servlet) .getWatermark_path();

		//int real_height_big;
		//int real_width_big;
		String basepath ="";//BlogConstants.PHOTO_FULL_UPLOAD_DIR; // Constants.getConfig(servlet).getReal_path();
		String autodir = SecureFileManager.createAutoDir();
		//大图片
		//String filefullpath = autodir + SecureFileManager.getRamdomFileName(file.getFileName());
		//String full_path = basepath + filefullpath;
		//小图片
		String smallpicfullpath = autodir + SecureFileManager.getRamdomFileName(file.getFileName());
		String small_path = basepath + smallpicfullpath;

		//创建目录
		String tmppath = basepath + autodir;
		File f = new File(tmppath);
		if (!f.exists())
			f.mkdirs();
		f = null;


		if((".bmp".equalsIgnoreCase(ext) ||
				/*".gif".equalsIgnoreCase(ext) ||*/
				".jpg".equalsIgnoreCase(ext) ||
				".jpeg".equalsIgnoreCase(ext) ||
				".png".equalsIgnoreCase(ext))){


			/**
			 * 首先加入源图片
			 */
			InputStream in = file.getInputStream();
			BufferedImage Bi = ImageIO.read(in);
			int in_h = Bi.getHeight();
			int in_w = Bi.getWidth();
			//real_height_big = in_h;
			//real_width_big = in_w;
			//FileOutputStream ThR = new FileOutputStream(full_path);// realPath

			//Graphics2D g;


			//ThR = null;

			if (log.isInfoEnabled())
				log.info("完成原始图处理!");

            /*
			if(fix_pic && pic_height>0 && pic_width>0){//压缩原图片
				int orig_w = Bi.getWidth();
				int orig_h = Bi.getHeight();
				if (orig_w > pic_width) {
					double r = (orig_w * 1.0) / pic_width;
					if (r > 0) {
						float tmp_height = (float) (orig_h / r);
						pic_height = (int) tmp_height;
					}
					BufferedImage tag = new BufferedImage((int) pic_width, (int) pic_height, BufferedImage.TYPE_INT_RGB);

					Graphics g =  tag.getGraphics();
					Image tmp = Bi.getScaledInstance(pic_width, pic_height, Image.SCALE_SMOOTH);
					g.drawImage(tmp, 0, 0, null);
					FileOutputStream out = new FileOutputStream(full_path);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam((BufferedImage) tag);
					param.setQuality(0.95f, true);
					encoder.encode(tag, param);
					tmp = null;
					g = null;
					tag = null;
					out.close();
					encoder = null;
					param = null;
				} else {
					SecureFileManager.jpegEncode(full_path, Bi);
				}
				if (in != null) {
					in.close();
					in = null;
				}
				//list.add(smallpic);
			}else{//不压缩原图
				try {
					BufferedImage bimage = new BufferedImage(real_width_big, real_height_big, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = bimage.createGraphics();
					g.drawImage(Bi, 0, 0, null);

					/*
					File wm = null;
					if (autowater && watermark_path != null) {
						wm = new File(watermark_path);
					}
					if (wm != null && wm.exists()) {//给图片加水印
						wm = null;
						imageMark.createMark((BufferedImage) bimage, full_path, watermark_path, "down");
					} else {//开始上传，不给图片加水印处理*//*
					SecureFileManager.jpegEncode(full_path, bimage);
					//}
					bimage = null;
					g  = null;
					if (log.isInfoEnabled())log.info("原始图片处理完成");
				} catch (Exception ex) {
					log.error(ex.getMessage());
					throw new IOException("写文件时出错 ");
				} finally {
					//ThR.close();
					if (in != null)
						in.close();
				}
			}
			list.add(filefullpath);
			*/
			//生成缩略图
			if (auto_small_pic) { // jpg图片或jpeg图片可以进行缩小处理
				double Ratio = 1.0;
				if (auto_small_pic && small_pic_height > 0 && small_pic_width > 0) {
					if ((in_h > small_pic_height) || (in_w > small_pic_width)) {
						if (in_h < in_w) // 产生缩放比率，以数值大的为基准
							Ratio = small_pic_width * 1.0 / in_w;
						else
							Ratio = small_pic_height * 1.0 / in_h;
					}

					Image Itemp = Bi.getScaledInstance(small_pic_width, small_pic_height, Image.SCALE_SMOOTH);

					AffineTransformOp op = new AffineTransformOp(AffineTransform .getScaleInstance(Ratio, Ratio), null);
					Itemp = op.filter(Bi, null);

					if (Ratio < 1.0) {
						BufferedImage bimage = new BufferedImage(small_pic_width, small_pic_height, BufferedImage.TYPE_INT_RGB);

						int t_width = ((BufferedImage) Itemp).getWidth();
						int t_height = ((BufferedImage) Itemp).getHeight();

						FileOutputStream out = new FileOutputStream(small_path);

						Graphics2D g = bimage.createGraphics();
						g.fillRect(0, 0, small_pic_width, small_pic_height);
						g.drawImage(Itemp, (small_pic_width - t_width) / 2, (small_pic_height - t_height) / 2, null); // 首先加入源图片

						SecureFileManager.jpegEncode(small_path, bimage);
						out.close();
						Itemp = null;
						bimage = null;
						g = null;
						if (log.isInfoEnabled())
							log.info("缩略图处理完成");
					} else {
						SecureFileManager.jpegEncode(small_path, (BufferedImage) Itemp);
					}
					Itemp = null;
					op = null;

				}
				Bi = null;
				if (in != null) {
					in.close();
					in = null;
				}
				list.add(smallpicfullpath);
			} else if (!auto_small_pic && autowater && (!".gif".equalsIgnoreCase(ext)) && watermark_path != null) {
				//开始上传须加水印但不须要缩略的图片,不改变原图片大小
				/*
				if (log.isDebugEnabled())
					log.debug("开始上传须加水印但不须要缩略的图片");

				BufferedImage bimage = new BufferedImage(real_width_big,
						real_height_big, BufferedImage.TYPE_INT_RGB);

				Graphics2D g = bimage.createGraphics();
				g.drawImage(Bi, 0, 0, null); // 首先加入源图片

				File wm = new File(watermark_path);
				if (wm.exists()) {
					imageMark.createMark((BufferedImage) bimage, full_path,
							watermark_path, "down");// realPath => full_path
				} else {
					SecureFileManager.jpegEncode(full_path, bimage);
				}
				wm = null;
				g = null;
				bimage = null;
				Bi = null;
				list.add(filefullpath);*/
			} else if (!auto_small_pic && small_pic_height > 0 && small_pic_width > 0) { // 不生成缩略,但改变原图片的大小. && (!".gif".equalsIgnoreCase(ext))
				/*InputStream in = file.getInputStream();
				BufferedImage Bi = ImageIO.read(in);
				String smallpic = autodir
				+ SecureFileManager.getRamdomFileName(file.getFileName());

				String smallpicfullpath = BlogConstants.PHOTO_FULL_UPLOAD_DIR
				+ smallpic;

				int orig_w = Bi.getWidth();
				int orig_h = Bi.getHeight();
				if (orig_w > small_pic_width) {
					double r = (orig_w * 1.0) / small_pic_width;
					if (r > 0) {
						float tmp_height = (float) (orig_h / r);
						small_pic_height = (int) tmp_height;
					}
					BufferedImage tag = new BufferedImage((int) small_pic_width,
							(int) small_pic_height, BufferedImage.TYPE_INT_RGB);

					Graphics g = tag.getGraphics();
					Image tmp = Bi.getScaledInstance(small_pic_width,
							small_pic_height, Image.SCALE_SMOOTH);
					g.drawImage(tmp, 0, 0, null);
					FileOutputStream out = new FileOutputStream(smallpicfullpath);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					JPEGEncodeParam param = encoder
					.getDefaultJPEGEncodeParam((BufferedImage) tag);
					param.setQuality(0.95f, true);
					encoder.encode(tag, param);
					tag = null;
					out.close();
					encoder = null;
					param = null;
				} else {
					SecureFileManager.jpegEncode(smallpicfullpath, Bi);
				}
				Bi = null;
				if (in != null) {
					in.close();
					in = null;
				}
				list.add(smallpic);*/
			}
			Bi=null;
		} else { // 它文件，不能进行缩小等处理
			if (log.isDebugEnabled())
				log.debug("开始上传其它文件，不能进行缩小等处理");
			/*InputStream in = null;
			BufferedOutputStream fos = null;
			try {
				in = file.getInputStream();

				fos = new BufferedOutputStream(new FileOutputStream(new File(
						full_path)),// realPath => full_path
						10 * 1024);
				int read = 0;
				byte buffer[] = new byte[10 * 1024];
				while ((read = in.read(buffer, 0, 10 * 1024)) > 0) {
					fos.write(buffer, 0, read);
				}
				buffer = null;
				fos.flush();
			} finally {
				in.close();
				in = null;
				fos.close();
				fos = null;
			}
			list.add(filefullpath);*/
		}
		file = null;
		if (list.size() <= 1)
			list.add(list.get(0));
		return list;// filefullpath;
	}
}
