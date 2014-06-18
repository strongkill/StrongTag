package net.strong.util;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

/**
 * <p>Title: 取得图片的尺寸</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class ImageProp {

  private int imgHeight = 0;
  private int imgWidth = 0;
  private int fileSize = 0;
  private String path = null;
  private String fileName = null;
  private String realFileName = null;
  private String sys_separator = "/"; //文件系统的路径符，"\"(linux)或"/"(windows)

  public int getImgHeight()
  {
    return imgHeight;
  }
  public int getImgWidth()
  {
    return imgWidth;
  }
  public int getFileSize()
  {
    return fileSize;
  }
  public  ImageProp(String realFileName) throws Exception
  {
    this.realFileName = realFileName;
    Init(realFileName);
  }
  public  ImageProp(String path,String fileName) throws Exception
  {
    sys_separator = System.getProperty("file.separator");
    this.realFileName = path + sys_separator + fileName;
    Init(realFileName);
  }
  private String getExt(String realFileName)
  {
    String result = null;
    int l_index = realFileName.lastIndexOf(".");
    result = realFileName.substring(l_index+1,realFileName.length());
    return result;
  }
  /**
   *初始化，给定全路径（含有文件名），然后即可取得文件信息
   */
  private void Init(String realFileName) throws Exception
  {
    if(realFileName==null)
      throw new Exception("file name and path can not be null");
    String file_ext = getExt(realFileName);
    if(file_ext.compareToIgnoreCase("BMP")==0)
    {
      InputStream imgIn = new FileInputStream(realFileName);
      DataInputStream in = new DataInputStream(imgIn);
      if (in.read() != 'B') {
          throw new Exception("Not a .BMP file");
      }
      if (in.read() != 'M') {
          throw new Exception("Not a .BMP file");
      }
      fileSize =  intelInt(in.readInt());

      in.readUnsignedShort();
      in.readUnsignedShort();
      int bitmapOffset = intelInt(in.readInt());
      int bitmapInfoSize = intelInt(in.readInt());

      imgWidth = intelInt(in.readInt());
      imgHeight = intelInt(in.readInt());
      in.close();
      imgIn.close();
    }
    else if(file_ext.compareToIgnoreCase("JPEG")==0 ||
            file_ext.compareToIgnoreCase("JPG")==0)
    {
      InputStream imgIn = new FileInputStream(realFileName);
      JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imgIn);
      BufferedImage image = decoder.decodeAsBufferedImage();
      imgHeight = image.getHeight();
      imgWidth = image.getWidth();
      imgIn.close();
    }
    else
    {
      throw new Exception("the file format is not support");
    }

  }
  private static int intelInt(int i)
  {
    return ((i & 0xff) << 24) + ((i & 0xff00) << 8) +
        ((i & 0xff0000) >> 8) + ((i >> 24) & 0xff);
  }

}