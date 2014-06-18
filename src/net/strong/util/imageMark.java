package net.strong.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p>Title:对上传的图片加水印 </p>
 * <p>Description: 在添加水印时，当源图片的宽度小于４００时，将检测是否有不小的水印图
 * （将原来的.png用-s.png来替换取得小图的路径），如果有，就用小水印图来加水印。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */

public class imageMark {
  //System.out.println("time:"+(System.currentTimeMillis()-before));
  static private int stat_X = 0;
  static private int stat_Y = 0;
//  static private FileInputStream fin = null;
  String ext = "png";

  private static Color color[]={Color.red,Color.black,Color.white,Color.yellow,Color.orange,Color.green};

  private imageMark(){}
  /*
  public void compress(String file_path, String realPath) throws Exception {
    BufferedImage Bi = null;
    float Ratio = (float) 0.3;
    try {
      // File file = new File();
      in = new FileInputStream(file_path);
      Bi = ImageIO.read(in);
    }
    catch (Exception es) {
      System.out.println(es.getMessage());
    }
    int in_h = Bi.getHeight();
    int in_w = Bi.getWidth();
    double s_h = in_h * Ratio;
    double s_w = in_w * Ratio;
    int int_s_h = (int) s_h;
    int int_s_w = (int) s_w;
    File ThF = new File(realPath);
    Image Itemp = Bi.getScaledInstance(int_s_w, int_s_h, Bi.SCALE_SMOOTH);
    AffineTransformOp op = new AffineTransformOp(AffineTransform.
                                                 getScaleInstance(Ratio, Ratio), null);
    Itemp = op.filter(Bi, null);
    try {
      //生成缩略图片
      ImageIO.write( (BufferedImage) Itemp, ext, ThF);
    }
    catch (Exception ex) {
      throw new IOException("写文件时出错 ");
    }
  }
*/
  /**
   * 对水印图片进行缩放
   * @param file_path 水印图片的路径
   * @param Ratio 缩放比率
   * @return
   */
  private static Image compress(String file_path, int width, int height
                                ) {
    BufferedImage Bi = null;
    FileInputStream fin = null;
    //float Ratio =(float)0.3;
    try {
      if(width<400 )
      {
        String t_path = file_path.replaceFirst(".png","-s.png");
        try
        {
          fin = new FileInputStream(t_path);
        }
        catch(Exception e)
        {
          System.out.println("can not find small watermark");
          fin = new FileInputStream(file_path);
        }
      }
      else
        fin = new FileInputStream(file_path);

      Bi = ImageIO.read(fin);
      fin.close();
    }
    catch (Exception es) {
      System.out.println(es.getMessage());
    }
    int in_h = Bi.getHeight();
    int in_w = Bi.getWidth();
    float Ratio = getRatio(width,height,in_w,in_h);

    double s_h = in_h * Ratio;
    double s_w = in_w * Ratio;
    int int_s_h = (int) s_h;
    int int_s_w = (int) s_w;
    // File ThF = new File(realPath);

    Graphics2D g = Bi.createGraphics();
    g.rotate(Math.toRadians(30));

    Image Itemp = Bi.getScaledInstance(int_s_w, int_s_h, Bi.SCALE_SMOOTH);
    AffineTransformOp op = new AffineTransformOp(AffineTransform.
                                                 getScaleInstance(Ratio, Ratio), null);
    Itemp = op.filter(Bi, null);

    return Itemp;
  }
  /**
   * 对水印图片进行缩放
   * @param bufImage 水印图片的对象
   * @param Ratio 缩放比率
   * @return
   */
  private static Image compress(BufferedImage bufImage, float Ratio) {
    if(bufImage==null)
      return null;
    BufferedImage Bi = bufImage;
    //float Ratio =(float)0.3;
    int in_h = Bi.getHeight();
    int in_w = Bi.getWidth();
    double s_h = in_h * Ratio;
    double s_w = in_w * Ratio;
    int int_s_h = getIntValue(String.valueOf(s_h),in_h);
    int int_s_w = getIntValue(String.valueOf(s_w),in_w);
    // File ThF = new File(realPath);
    Image Itemp = Bi.getScaledInstance(int_s_w, int_s_h, Bi.SCALE_SMOOTH);
    AffineTransformOp op = new AffineTransformOp(AffineTransform.
                                                 getScaleInstance(Ratio, Ratio), null);
    Itemp = op.filter(Bi, null);
    return Itemp;
  }

  private static int getIntValue(String int_value,int default_value)
  {
    int result = default_value;
    try
    {
      result = Integer.valueOf(int_value).intValue();
    }
    catch (Exception e) {
      result = default_value;
    }
    return result;
  }
  /**
   *
   * @param X 大图片的宽度；
   * @param Y 大图片的高度；
   * @param Xw 缩略之后水印的宽度；
   * @param Yw 缩略之后水印的高度；
   * @return 起始坐标 stat_cood[0],stat_cood[1]
   */
  private static int[] getMidPels(int X, int Y, int Xw, int Yw) {
    //1
    int[] stat_cood = new int[2];
    stat_cood[0] = 0;
    stat_cood[1] = 0;
    int Xend = 0;
    int Yend = 0;
    if (X >= Xw && Y >= Yw) {
      Xend = (int) (X / 2 - Xw / 2);
      Yend = (int) (Y / 2 - Yw / 2);
      stat_cood[0] = Xend;
      stat_cood[1] = Yend;
      return stat_cood;
    }
    return stat_cood;
  }

  private static int[] getMidPels(int X, int Y, Image img) {
    //1
    int[] stat_cood = new int[2];
    stat_cood[0] = 0;
    stat_cood[1] = 0;
    int Xend = 0;
    int Yend = 0;
    int Xw = 0;
    int Yw = 0;
    Xw = img.getWidth(null);
    Yw = img.getHeight(null);
    if(X >= Xw)
    {
      Xend = (int) (X/2 - Xw/2);
      stat_cood[0] = Xend;
    }
    if(Y >= Yw)
    {
      Yend = (int) (Y/2 - Yw/2);
      stat_cood[1] = Yend;
    }

    return stat_cood;
  }

  /**
   *
   * @param X 缩略图外框长
   * @param Y 缩略图外框高
   * @param X1 原图按比例缩小后的长
   * @param Y1 缩小后的高
   * @param img 水印图
   * @return
   */
  private static int[] getMidDownPels(int X,int Y,int X1,int Y1,Image img)
  {
    int[] stat_cood = new int[2];
    stat_cood[0] = 0;
    stat_cood[1] = 0;
    int Xend = 0;
    int Yend = 0;
    int Xw = 0;
    int Yw = 0;
    Xw = img.getWidth(null);
    Yw = img.getHeight(null);
    if (X >= Xw) {
      Xend = (int) (X / 2 - Xw / 2);
      stat_cood[0] = Xend;
    }
    if(Y >= Yw)
    {
      Yend = (int) ((Y+Y1)/2 - Yw );
      stat_cood[1] = Yend;
    }
    return stat_cood;

  }

  private static int[] getMidDownPels(int X, int Y, Image img) {
    //1
    int[] stat_cood = new int[2];
    stat_cood[0] = 0;
    stat_cood[1] = 0;
    int Xend = 0;
    int Yend = 0;
    int Xw = 0;
    int Yw = 0;
    Xw = img.getWidth(null);
    Yw = img.getHeight(null);
    if (X >= Xw ) {
      Xend = (int) (X / 2 - Xw / 2);
      stat_cood[0] = Xend;
    }
    if(  Y >= Yw)
    {
      Yend = (int) (Y - Yw );
      stat_cood[1] = Yend;
    }
    return stat_cood;
  }


  /**
   * 获取左边的起始坐标
   * @param X
   * @param Y
   * @param img
   * @return
   */
  private static int[] getLeftDownPels(int X, int Y, Image img) {
    //1
    int[] stat_cood = new int[2];
    stat_cood[0] = 0;
    stat_cood[1] = 0;
    int Xend = 0;
    int Yend = 0;
    int Xw = 0;
    int Yw = 0;
    Xw = img.getWidth(null);
    Yw = img.getHeight(null);

    if (X >= Xw ) {
      Xend = (int) (0 + X * 0.02345);
      stat_cood[0] = Xend;
    }
    if(Y >= Yw)
    {
      Yend = (int) (Y - Yw - Y * 0.02345);
      stat_cood[1] = Yend;
    }
    return stat_cood;
  }

  /**
   * 获取右边的起始坐标
   * @param X
   * @param Y
   * @param img
   * @return
   */
  private static int[] getRightDownPels(int X, int Y, Image img) {
    //1
    int[] stat_cood = new int[2];
    stat_cood[0] = 0;
    stat_cood[1] = 0;
    int Xend = 0;
    int Yend = 0;
    int Xw = 0;
    int Yw = 0;
    Xw = img.getWidth(null);
    Yw = img.getHeight(null);

    if (X >= Xw && Y >= Yw) {
      if ( ( (X - Xw) > (int) (X * 0.02345)) && ( (Y - Yw) > (int) (Y * 0.02345))) {
        Xend = (int) (X - Xw - X * 0.02345);
        Yend = (int) (Y - Yw - Y * 0.02345);
      }
      else {
        Xend = (int) (X - Xw);
        Yend = (int) (Y - Yw);
      }
      stat_cood[0] = Xend;
      stat_cood[1] = Yend;
      return stat_cood;
    }
    return stat_cood;
  }
  /**
   *
   * @param X 大图片的宽度；
   * @param Y 大图片的高度；
   * @param Xw 水印的宽度；
   * @param Yw 水印的高度
   * @return 缩略率
   */
  private static float getRatio(int X, int Y, int Xw, int Yw) {
    float x_r = (float)(X*4/5)/Xw;
    float y_r = (float)(Y*4/5)/Yw;

    if(x_r>1 && y_r>1)
      return 1;
    if(x_r<y_r)
      return x_r;
    else
      return y_r;
  }

  /**
   *
   * @param sour_image
   * @param set_width 原图缩略后的宽
   * @param set_height 原图缩略后的高
   * @param bk_color
   * @param sour_filepath
   * @param watermark_path
   * @param position
   * @return
   * @throws IOException
   */
  public static boolean createMark(BufferedImage sour_image, int set_width,
                                   int set_height,int bk_color,
                                   String sour_filepath, String watermark_path,
                                   String position) throws IOException
  {
    int width = sour_image.getWidth();
    int height =sour_image.getHeight();
    BufferedImage bimage = new BufferedImage(width, height,
                                             BufferedImage.TYPE_INT_RGB);
    Image Itemp = compress(watermark_path, set_width,set_height); //水印缩略

    Graphics2D g = bimage.createGraphics();
    g.setBackground(color[bk_color]);
//    g.fillRect(0, 0, set_width, set_height);
//    g.drawImage(sour_image, (set_width-width)/2,(set_height-height)/2, null); //首先加入源图片
//    g.rotate(-Math.toRadians(30));
    g.drawImage(sour_image, 0,0, null); //首先加入源图片

    //获取水印合成的开始坐标
    // String temp_post = position;
    int pits[] = new int[2];
    int Xcoord = 0;
    int Ycoord = 0;
    if ("mid".equalsIgnoreCase(position)) {
      pits = getMidPels(width, height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标
    }
    if ("left".equalsIgnoreCase(position)) {
      pits = getLeftDownPels(width, height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标

    }
    if ("right".equalsIgnoreCase(position)) {
      pits = getRightDownPels(width, height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标

    }
    if ("down".equalsIgnoreCase(position)) {
      pits = getMidDownPels(width, height,set_width,set_height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标

    }

//    g.rotate(-Math.toRadians(30));
    g.drawImage(Itemp, Xcoord, Ycoord, null); //加入水印图
    g.dispose();

    FileOutputStream out = new FileOutputStream(sour_filepath);
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
    param.setQuality(0.95f, true);
    encoder.encode(bimage, param);

    out.close();

    Itemp.flush();
    Itemp = null;
    bimage.flush();
    bimage = null;

//    in.close();
    return true;

  }
  //添加水印,filePath 源图片路径， watermark 水印图片路径
  /**
   * 对源文件添加水印
   * @param sour_image 源文件对象
   * @param sour_filepath 源文件存放路径
   * @param watermark_path 水印文件路径
   * @param position 位置 mid left right 三个选项
   * @return
   */
  public static boolean createMark(BufferedImage sour_image,
                                   String sour_filepath, String watermark_path,
                                   String position) throws IOException
  {
    return createMark(sour_image, sour_image.getWidth(), sour_image.getHeight(),2,
                      sour_filepath, watermark_path, position);
  }

  /**
   * 添加水印
   * @param filePath 源图片路径
   * @param watermark 水印图片路径
   * @param position 位置 mid left right 三个选项
   * @return
   */
  public static boolean createMark(String filePath, String watermark,
                                   String position) {
    ImageIcon imgIcon = new ImageIcon(filePath);
    Image theImg = imgIcon.getImage();
//    ImageIcon waterIcon = new ImageIcon(watermark);
//    Image waterImg = waterIcon.getImage();
    int width = theImg.getWidth(null);
    int height = theImg.getHeight(null);

//    int width_water = waterImg.getWidth(null);
//    int height_water = waterImg.getHeight(null);

    BufferedImage bimage = new BufferedImage(width, height,
                                             BufferedImage.TYPE_INT_RGB);
    //Image Itemp =  waterImg.getScaledInstance(widht_water,height_water,waterImg.SCALE_SMOOTH);
    //计算图片的缩略率
//    float Ratio = getRatio(width, height, width_water, height_water);
    // Image Itemp =compress(watermark,(float)0.5);  //水印缩略
//    if(Ratio == 0f)
//          Ratio = 1f;  //限制住Ratio不能等于0;
    Image Itemp = compress(watermark, width,height); //水印缩略
    // Itemp.get
    Graphics2D g = bimage.createGraphics();
    //g.setColor(Color.red);
    g.setBackground(Color.white);
    g.drawImage(theImg, 0, 0, null);
    //获取水印合成的开始坐标
    // String temp_post = position;
    int pits[] = new int[2];
    int Xcoord = 0;
    int Ycoord = 0;
    if ("mid".equalsIgnoreCase(position)) {
      pits = getMidPels(width, height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标
    }
    if ("left".equalsIgnoreCase(position)) {
      pits = getLeftDownPels(width, height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标

    }
    if ("right".equalsIgnoreCase(position)) {
      pits = getRightDownPels(width, height, Itemp);
      Xcoord = pits[0]; //X坐标
      Ycoord = pits[1]; //Y坐标

    }

    g.drawImage(Itemp, Xcoord, Ycoord, null);
    g.rotate(Math.toRadians(30));
    //g.drawImage(Itemp,59,200,null);
    g.dispose();
    try {
      FileOutputStream out = new FileOutputStream(filePath);
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
      JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
      //param.setQuality(50f, true);
      //Some guidelines: 0.75 high quality
      //0.5  medium quality
      //0.25 low quality
      param.setQuality(0.5f, true);

      encoder.encode(bimage, param);
      out.close();
//      in.close();
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }
}