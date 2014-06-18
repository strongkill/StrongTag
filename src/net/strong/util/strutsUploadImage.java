package net.strong.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.struts.upload.FormFile;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p>Title:  struts结构的图片文件上传类</p>
 * <p>Description: struts结构的图片文件上传类，
 * 首先在Form中设置一org.apach.struts.upload.formFile * 类的一对象，
 * 然后可以在Action中应用此类进行保存文件，
 * 有如下功能：
 * 1.上传图片，图片的名称可以指定，也可以自动生成。
 * 2。通过setAutoDir来设置是否允许自动跟据当前月份建立目录，默认为true
 * 3.对上传的图片限制大小，即只允许上传指定高范围(0,300)、宽范围(0,300)的图片，
 * 能过setHeightLimit(),setWidthLimit来设置。
 * 4.自动生成缩略图,并指定缩略图的尺寸，缩略图会跟据原来的比例设置大小，通过setAutoSmallPic（）来设置
 * 5.通过getRealFileName（）获取生成的原始文件名
 * 6.通过getsmallFileName（）获取生成的缩略呼的文件名
 * 7.限制上传图片的最大值，通过setMaxSize()来设置
 * 8.通过setWatermark_path（）设置水印图片的路径，如设置了此值，则将对图片加水印。
 * 如下例子：
 *<br>
 * FormFile logo_File = form.getImgFile();

    strutsUploadImage suFile = null;  //上传文件
    try
    {
      int size = logo_File.getFileSize();
       suFile = new strutsUploadImage(logo_File,"E:\\source");
       suFile.setAutoSmallPic(true,100,100); //自动生成缩略图
       suFile.uploadFile();
    }
    catch(IOException e)
    {
      return actionMapping.findForward("failure");
    }


 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */

public class strutsUploadImage extends strutsUploadFile {

//  protected FormFile upFormFile = null;//保存文件的路径

  protected String realFileName = null; //原始文件名
  protected String smallFileName = null; //生成的文件名

  protected boolean auto_small_pic = false; //是否自动生成小图
  protected int small_pic_height = 120; //生成的小图的高
  protected int small_pic_width = 120; //生成的小图的宽

  //最大，最小高度用于指定上传的图片只能在这个高度范围内，宽度也一样
  protected int max_height = 300; //最大高度
  protected int min_height = 0; //最小高度

  protected int max_width = 300;
  protected int min_width = 0;
  private int real_height_big;
  private int real_width_big;
  private int real_height_small;
  private int real_width_small;
  private String watermark_path;
  private boolean fixed_flag = true;
  private int bk_color_index = 2;

  private Color color[]={Color.red,Color.black,Color.white,Color.yellow,Color.orange,Color.green};
  private String markString;
  private int watermark_pos;
  private String str_pos;

  private boolean logit = false;

  private int run_step = 0;

  public void setHeightLimit(int max_height,int min_height)
  {
    this.max_height = max_height;
    this.min_height = min_height;
  }
  public void setWidthLimit(int max_width,int min_width)
  {
    this.max_width = max_width;
    this.min_width = min_width;
  }
  /**
   * 设置是否需要产生缩略图及其长宽
   * @param auto_small_pic 是否需要产生缩略图 true为要，默认为false
   * @param small_pic_height 缩略图的高
   * @param small_pic_width 缩略图的宽
   */
  public void setAutoSmallPic(boolean auto_small_pic,int small_pic_height,int small_pic_width)
  {
    this.auto_small_pic = auto_small_pic;
    this.small_pic_height = small_pic_height;
    this.small_pic_width = small_pic_width;
  }
  /*
  public void setUpFormFile(FormFile upFormFile)
  {
    this.upFormFile = upFormFile;
  }
  public void setFile_path(String file_path)
  {
    this.file_path = file_path;
  }
*/
  public strutsUploadImage()
  {
    //
  }

  /**
   * 获取上传后的原始文件名
   * @return
   */
  public String getRealFileName()
  {
    String temp = this.realFileName;
//    log.warn("开始获取文件名 1 1 ,temp:"+temp);
    if(temp==null || temp.length() <1)
      return null;

//    log.warn("开始获取文件名 2");
    char [] temps = temp.toCharArray();
    for(int i=0 ;i<temps.length;i++)
    {
      if(temps[i]=='\\')
        temps[i] = '/';
    }
//    log.warn("开始获取文件名 3");
    temp = String.valueOf(temps);
    return temp;//this.fileName;

//    return this.realFileName;
  }
  public void setRealFileName(String realFileName)
  {
    this.realFileName = realFileName;
  }

  /**
   * 获取自动生成的文件的名称
   * 当小图片文件名为NULL时（即没有产生小图片），则用大图片名代替
   * @return
   */
  public String getSmallFileName()
  {
    String temp = this.smallFileName;
    if(temp==null)
      temp = this.realFileName;

    char [] temps = temp.toCharArray();
    for(int i=0 ;i<temps.length;i++)
    {
      if(temps[i]=='\\')
        temps[i] = '/';
    }
    temp = String.valueOf(temps);
    return temp;//this.fileName;
//    return this.smallFileName ;
  }
  public void setSmallFileName(String smallFileName)
  {
    this.smallFileName = smallFileName;
  }

  public strutsUploadImage(FormFile upFormFile,String file_path)
  {
    this.upFormFile = upFormFile;
    this.file_path = file_path;
  }

  public strutsUploadImage(FormFile upFormFile,String file_path,boolean autoSmallPic)
  {
    this.upFormFile = upFormFile;
    this.file_path = file_path;
    this.auto_small_pic = autoSmallPic;
  }

  /**
   * 通过图片的宽度获取合适的字体大小，字体最大定在18，当 (图片宽*2/3)/字符串长度<18时，
   * <br>字体取(图片宽*2/3)/字符串长度
   * @param width
   * @return
   */
  private int getFontSize(int width,int str_len)
  {
    int result = (width*5/6)/str_len;
    if(result<5)
      result = -1;
    else
    {
      result = result > 10 ? result : 10;
      result = result < 25 ? result : 25;
    }
    return result;//(width*2/3)/str_len>15?(int)(width*2/3)/str_len:15;
  }

  public void uploadFile(boolean logit) throws IOException
  {
    this.logit = false;
//    log.warn("开始上传图片");
    try
    {
      uploadFile();
    }
    catch(Exception e)
    {
      log.warn("run step:"+run_step);
      log.warn(e.getMessage());
      e.printStackTrace();
    }
  }
  public void uploadFile()  throws IOException
  {

    if(log.isInfoEnabled())
      log.info("start");

    run_step = 1;

    String sys_separator = System.getProperty("file.separator");

    if(str_pos == null)
      str_pos = "down";

    if(upFormFile== null)
      return;

    long fileSize = upFormFile.getFileSize();

    if(fileSize <= 0)
      return ;
    run_step = 2;

    if(fileSize > maxSize)
    {
      log.warn("上传文件大小超过最大值，当前大小为："+fileSize);

      error_msg = "上传的文件的大小超过最大值，允许上传的最大值为：" + maxSize/1024 + "K";
      throw new IOException(error_msg);
    }

    run_step = 3;
    try
    {
      oldFileName = upFormFile.getFileName();
    }catch(Exception e)
    {
      log.error("获取文件名出错:"+e.getMessage());
    }

    run_step = 4;
    //获取原始图片的全路径,如为gif文件，则将文件名转为png文件
    realPath = getRealPath(file_path,null,oldFileName);
    run_step = 5;
    realFileName =  this.fileName;
    run_step = 6;

    String ext = getExt(fileName); //获取文件扩展名

    if (log.isInfoEnabled())
      log.info("开始上传原始图片");

    if(auto_small_pic &&( "jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)||
       "png".equalsIgnoreCase(ext) )) //jpg图片或jpeg图片可以进行缩小处理
    {

      InputStream in = upFormFile.getInputStream();

      BufferedImage Bi = ImageIO.read(in);

      int in_h = Bi.getHeight();
      int in_w = Bi.getWidth();
      real_height_big = in_h;
      real_width_big = in_w;

      if(logit)
        log.warn("开始了生成原始图片");

      FileOutputStream ThR = new FileOutputStream(realPath);
      try {
        //生成原始图片
//      ImageIO.write((BufferedImage)Bi, ext, ThR);
        BufferedImage bimage = new BufferedImage(real_width_big,
                                                 real_height_big,
                                                 BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();
        g.drawImage(Bi, 0, 0, null); //首先加入源图片
        if (markString != null && markString.length() > 0 &&
            real_width_big > 150) {
          if (log.isDebugEnabled())
            log.debug("在图片中加字符串");
          markString(markString,g,in_w,real_width_big,real_height_big);

        }
        if (watermark_path != null) { // && !"png".equalsIgnoreCase(ext))
          File wm = new File(watermark_path);
          if (wm.exists()) {
            if (log.isDebugEnabled())
              log.debug("给图片加水印");
            imageMark.createMark( (BufferedImage) bimage, realPath,
                                 watermark_path, str_pos);
          }
          else {
            jpegEncode(realPath, bimage);
          }
          wm = null;
        }
        else {
          jpegEncode(realPath, bimage);
        }
        g = null;
        if (log.isInfoEnabled())
          log.info("原始图片处理完成");
      }
      catch (Exception ex) {
        log.error(ex.getMessage());
        error_msg = "写文件时出错 ";
        throw new IOException("写文件时出错 ");
      }
      finally {
        ThR.close();
        in.close();
//        Bi = null;
      }

     ThR = null;

     if (log.isInfoEnabled())
       log.info("完成原始图片上传，开始处理缩略图");

      //生成缩略图
      double Ratio=1.0;
      if(auto_small_pic)
      {
        if ( (in_h > small_pic_height) || (in_w >small_pic_width)) {
          if (in_h < in_w) //产生缩放比率，以数值大的为基准
            Ratio = small_pic_width * 1.0 / in_w;
          else
            Ratio = small_pic_height * 1.0/ in_h;
        }

      double s_h = real_height_big * Ratio;
      double s_w = real_width_big * Ratio;
      try
      {
        real_height_small = (int)s_h;//Integer.valueOf(String.valueOf(s_h)).intValue();
        real_width_small = (int)s_w;//Integer.valueOf(String.valueOf(s_w)).intValue();
      }
      catch(Exception e)
      {
        real_height_small = 0;
        real_height_small = 0;
      }
        //获取缩略图文件全路径
        realPath = getRealPath(file_path,null,oldFileName);
        smallFileName = this.fileName;


        FileOutputStream ThF = null;//new File(realPath);
//假设图片宽 高 最大为in_w in_h
        Image Itemp = Bi.getScaledInstance(small_pic_width, small_pic_height, Bi.SCALE_SMOOTH);

        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), null);
        Itemp = op.filter(Bi, null);


        if(Ratio<1.0)// && ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)))
        {

          BufferedImage bimage = new BufferedImage(small_pic_width, small_pic_height,
                                             BufferedImage.TYPE_INT_RGB);
//          bimage = (BufferedImage) Itemp;

          int t_width = ((BufferedImage)Itemp).getWidth();
          int t_height =  ((BufferedImage)Itemp).getHeight();

          FileOutputStream out = new FileOutputStream(realPath);

          Graphics2D g = bimage.createGraphics();
          g.fillRect(0, 0, small_pic_width, small_pic_height);
          g.drawImage(Itemp, (small_pic_width-t_width)/2,(small_pic_height-t_height)/2, null); //首先加入源图片

          if(markString!=null && markString.length() >0 && t_width>150)
          {
            //在图片中直接打印字符串
            if (log.isDebugEnabled())
              log.debug("给缩略图加字符串");
            markString(markString,g,in_w,small_pic_width,small_pic_height);

          }

          if(watermark_path!=null )//&& !"png".equalsIgnoreCase(ext))
          {
            File wm = new File(watermark_path);
            if(wm.exists())
            {
              if(fixed_flag)
              {
                log.debug("给缩略图加水印");
                imageMark.createMark(bimage, t_width, t_height, bk_color_index,
                                     realPath,
                                     watermark_path, str_pos);
              }
            }
            else
            {
              jpegEncode(realPath,bimage);
            }
            wm = null;
          }
          else
          {
            jpegEncode(realPath,bimage);

          }
          out.close();
          Itemp = null;
          bimage = null;
          g = null;
          if (log.isInfoEnabled())
            log.info("缩略图处理完成");
        }
        else
        {
          jpegEncode(realPath,(BufferedImage)Itemp);
        }

        ThF = null;
        Itemp = null;
        op = null;

      }
      Bi = null;
      in.close();
      in = null;
    }
    else if(("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)||
       "png".equalsIgnoreCase(ext)) && watermark_path != null )
    {
      if (log.isDebugEnabled())
        log.debug("开始上传须加水印但不须要缩略的图片");
      InputStream in = upFormFile.getInputStream();

      BufferedImage Bi = ImageIO.read(in);

      int in_h = Bi.getHeight();
      int in_w = Bi.getWidth();
      real_height_big = in_h;
      real_width_big = in_w;

      BufferedImage bimage = new BufferedImage(real_width_big,
                                               real_height_big,
                                               BufferedImage.TYPE_INT_RGB);

      Graphics2D g = bimage.createGraphics();
      g.drawImage(Bi, 0, 0, null); //首先加入源图片

      if(markString!=null && markString.length() >0 && in_w>150)
      {
        //在图片中直接打印字符串
        if (log.isDebugEnabled())
          log.debug("给缩略图加字符串");
        markString(markString,g,in_w,real_width_big,real_height_big);

      }

      File wm = new File(watermark_path);
      if (wm.exists()) {
        imageMark.createMark( (BufferedImage) bimage, realPath,
                             watermark_path, str_pos);
      }
      else {
        jpegEncode(realPath, bimage);
      }
      wm = null;
      g = null;
      bimage = null;
      Bi = null;
    }
    else //BMP图片，或其它文件，不能进行缩小等处理
    {
      if (log.isDebugEnabled())
        log.debug("开始上传BMP图片，或其它文件，不能进行缩小等处理");
      InputStream in = null;
      BufferedOutputStream fos = null;
      try
      {
        in = upFormFile.getInputStream();

        fos = new BufferedOutputStream(new FileOutputStream(new File(realPath)),
                                       this.diskBufferSize);
        int read = 0;
        byte buffer[] = new byte[this.diskBufferSize];
        while ( (read = in.read(buffer, 0, this.diskBufferSize)) > 0) {
          fos.write(buffer, 0, read);
        }
        buffer = null;
        fos.flush();
      }
      finally
      {
        in.close();
        in = null;
        fos.close();
        fos = null;
      }
    }

  }

  protected void markString(String markString, Graphics2D g, int in_w,
                            int img_width, int img_height)
  {
    int mark_len = markString.getBytes().length; //获取打印字符串的长度，双字节的字以２计
    int font_size = getFontSize(in_w, mark_len);
    if (font_size > 0) {
      str_pos = "down";
      //在图片中直接打印字符串

      Font mFont = new Font ("宋体", Font.BOLD, font_size); //默认字体
      Color mClor = new Color(0,0,0);
      int str_len = markString.length();
      g.setFont(mFont);
      g.setColor(mClor);
      int font_w = (img_width - str_len * font_size) / 2 > 0 ?
          (img_width - str_len * font_size) / 2 : 10;
      g.drawString(markString, font_w, (img_height - font_size) / 2);

    }
  }
  /**
   * 通过图片流产生图片文件
   * @param realPath 图片文件路径
   * @param image 图片流
   * @throws IOException
   */
  protected void jpegEncode(String realPath,BufferedImage image) throws IOException
  {
    FileOutputStream ThF = null;
    try {
       ThF = new FileOutputStream(realPath);
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ThF);
      JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam((BufferedImage)image);
      param.setQuality(0.95f, true);
      encoder.encode((BufferedImage)image, param);
    }
    catch (Exception ex) {
      log.error(ex.getMessage());
      error_msg = "写文件时出错 ";
      throw new IOException("写文件时出错 ");
    }
    finally
    {
      ThF.close();
      ThF = null;
      image = null;
    }

  }
  public int getReal_height_big() {
    return real_height_big;
  }
  public int getReal_width_big() {
    return real_width_big;
  }
  public int getReal_height_small() {
    return real_height_small;
  }
  public int getReal_width_small() {
    return real_width_small;
  }
  public String getWatermark_path() {
    return watermark_path;
  }
  public void setWatermark_path(String watermark_path) {
    this.watermark_path = watermark_path;
  }
  public boolean isFixed_flag() {
    return fixed_flag;
  }
  /**
   * 对于缩略图固定背景面积，长、宽以缩略图的为准，此参数为true的话，缩略图将固定背景，默认为true
   * @param fixed_flag
   */
  public void setFixed_flag(boolean fixed_flag) {
    this.fixed_flag = fixed_flag;
  }
  public int getBk_color_index() {
    return bk_color_index;
  }
  /**
   * 缩略图背景颜色，值从0至5，依次为：红、黑、白、黄、橙、绿，默认为白色
   * @param bk_color_index
   */
  public void setBk_color_index(int bk_color_index) {
    this.bk_color_index = bk_color_index;
  }

  public String getMarkString() {
    return markString;
  }
  /**
   * 对于上传的图片加入字符串，如果此参数不为空，则将其打印到图片上。
   * @param markString
   */
  public void setMarkString(String markString) {
    this.markString = markString;
    str_pos = "down";
  }
  public int getWatermark_pos() {
    return watermark_pos;
  }
  /**
   * 设置水印的位置，默认为图片中间，当需要打印出字符串时，放在图片底部。
   * <br>本参数可设置
   * <br> 1 -- mid即在图片中间，默认
   * <br> 2 -- leftDown即图片左下方
   * <br> 3 -- rightDown即图片右下方
   * <br> 4 -- midDown即图片中下方
   * <br>如设置其它值，则系统自动设为默认值
   * @param watermark_pos
   */
  public void setWatermark_pos(int watermark_pos) {
    this.watermark_pos = watermark_pos;
    switch(watermark_pos)
    {
      case 1:
        str_pos = "mid";
        break;
      case 2:
        str_pos = "left";
        break;
      case 3:
        str_pos = "right";
        break;
      case 4:
        str_pos = "down";
        break;
      default:
        str_pos = "mid";
        break;
    }
  }

}