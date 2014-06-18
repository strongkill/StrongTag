package net.strong.exutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 *
 * <p>Title: 以图片的形式输出文字的Servlet</p>
 * <p>Description:以图片的形式输出文字，可带以下参数：
 * 1.width 图片的宽，如为空，则默认为100
 * 2.high 图片的高，如为空，则默认为30
 * 3.bkColor 图片的背景色，如为空，则默认为白色
 * 4.ftColor 图片的字体色，如为空，则默认为黑色
 * 5. font_size  字体大小，如为空，则默认为18
 * 6.fontName 字体名称，如为空，则取文件系统的默认值
 * 7.fontStyle  字体类型，如bold,italic,plain
 * 8.pic_name 背景图片名，相对于URL根路径的文件名，即通过URL根路径＋pic_name即可在IE内打开此图片.
 * 9.xaxis 画图时x轴起点
 * 10.yaxis 画图时y轴起点
 * 11.shadow //是否需要打印阴影背影，如为false则不打印，默认打印。
 * 这些参数可以通过如/textImage?width=200&high=20的形式来设置
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class textImage extends HttpServlet {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//private static final String CONTENT_TYPE = "text/html; charset=GBK";
  //Initialize global variables
  public void init() throws ServletException {
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String width = request.getParameter("width"); //宽
    String high = request.getParameter("high"); //高
    String xaxis = request.getParameter("xaxis"); //在X轴起始点，对于以右对齐的方式，则此值是X轴在右边的起始点
    String yaxis = request.getParameter("yaxis");

    String strbkColor = request.getParameter("bkColor"); //背景色,值0-5，依次为：红、黑、白、黄、橙、绿
    String strftColor = request.getParameter("ftColor"); //字体色，值0-5
    String font_size = request.getParameter("font_size");
    String fontName = request.getParameter("fontName");

    fontName = net.strong.util.MultiLanguage.getUnicode(fontName,request);
    String fontStyle = request.getParameter("fontStyle");
    String pic_name = request.getParameter("picName");
//    String usePicSize = request.getParameter("usePicSize"); //是否使用图片的尺寸作为生成的图片尺寸
    String drawText = request.getParameter("drawText");
    String shadow = request.getParameter("shadow"); //是否需要打印阴影背影，如为false则不打印，默认打印。
    drawText = net.strong.util.MultiLanguage.getUnicode(drawText,request);
    //String isDebug  = request.getParameter("isDebug"); //是否需要进行调试
    String is_align_right = request.getParameter("is_align_right"); //是否右对齐,如为true，则右对齐
    String getRealUrl = request.getParameter("getRealURL");
//是否使用真实的URL作为获取图片的URL，默认为否，当为否时，将从XML文件中获取


    int iwidth = 100;
    int ihigh = 30;
    int ix = 20;
    int iy = 20;
    Color bkColor = new Color(Integer.parseInt("ffffff",16)); //白色
    Color ftColor = new Color(Integer.parseInt("000000",16)); //黑色
    Color ftColor2 = new Color(Integer.parseInt("7D7D7D",16)); //阴影色
    int ifontSize = 18;
    try
    {
      ifontSize = Integer.valueOf(font_size).intValue();
    }
    catch(Exception e)
    {
      ifontSize = 18;
    }
    try
    {
      ix = Integer.valueOf(xaxis).intValue();
    }
    catch(Exception e)
    {
      ix = 20;
    }
    try
    {
      iy = Integer.valueOf(yaxis).intValue();
    }
    catch(Exception e)
    {
      iy = ifontSize;
    }
    try
    {
      iwidth = Integer.valueOf(width).intValue();
    }
    catch(Exception e)
    {
      iwidth = 100;
    }
    try
    {
      ihigh = Integer.valueOf(high).intValue();
    }
    catch(Exception e)
    {
      ihigh = ifontSize + 10;
    }
    try
    {
      strbkColor.replaceFirst("#",""); //去掉strbkColor中的#字符

      bkColor = new Color(Integer.parseInt(strbkColor,16));
    }
    catch(Exception e)
    {
      bkColor = new Color(Integer.parseInt("ffffff",16)); //白色
    }
    try
    {
      strftColor.replaceFirst("#","");

      String colorR = strftColor.substring(0,2); //颜色的R值
      String colorG = strftColor.substring(2,4); //颜色的G值
      String colorB = strftColor.substring(4,6); //颜色的B值

      int cR = (Integer.parseInt(colorR,16) + 255)/2;
      int cG = (Integer.parseInt(colorG,16) + 255)/2;
      int cB = (Integer.parseInt(colorB,16) + 255)/2;
      int result = cR*256*256 + cG*256 + cB;

      ftColor = new Color(Integer.parseInt(strftColor,16));
      ftColor2 = new Color(result); //阴影背景色
    }
    catch(Exception e)
    {
      ftColor = new Color(Integer.parseInt("000000",16)); //黑色
      ftColor2 = new Color(Integer.parseInt("7D7D7D",16));
    }
    if(fontName==null)
      fontName = "";


//    String str=null;

     BufferedImage image = null;
     Graphics g = null;

     int img_width = 0;

    if(pic_name!=null)
    {
      /*
      String path = request.getRealPath("db_picture"); //获取db_picture对应的目录

      //将pic_name中的"/"转换成"\"
      String[] strL = pic_name.split("/");
      String result = "";
      for(int i=0;i<strL.length;i++)
      {
        result += strL[i];
        if(i<strL.length -1 )
          result += "\\";
      }
      pic_name = result;
      */
     String serverName = request.getServerName(); //服务器名
     int serverPort = request.getServerPort(); //端口号
     String contextName = request.getContextPath();  //context名
     String picUrl = null;
     if("true".equalsIgnoreCase(getRealUrl))
     {
       if (serverPort != 80)
         picUrl = "http://" + serverName + ":" + serverPort + "/";
       else
         picUrl = "http://" + serverName + "/";
       if (contextName != null && contextName.length() > 0)
         picUrl = picUrl + contextName + "/";

       picUrl = picUrl + pic_name;
       System.out.println(picUrl);
     }
     else
     {
       String t_path =getServletContext().getRealPath("/WEB-INF/classes");
       String t_server = "";
       try
       {
         xmlConstant xmlCon = new xmlConstant(t_path);
         t_server = xmlCon.url_base;
         if(!t_server.endsWith("/"))
           t_server = t_server + "/";
       }
       catch(Exception e)
       {
         t_server = "http://indexpro.lighting86.com/";
       }
       picUrl = t_server + pic_name;
     }

/*
      String realPath = request.getRealPath(pic_name) ;//path + "\\" + pic_name;

      if("true".equals(isDebug))
      {
        ProDebug.addDebugLog("realPath at textImage:" + realPath);
        ProDebug.saveToFile();
      }
*/
      try
      {

//        URL u = new URL(picUrl);
        URL u = new URL(picUrl);

        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(u.openStream());
        image = decoder.decodeAsBufferedImage();
        img_width = image.getWidth();
        g = image.getGraphics();
//        g.clipRect();

      }
      catch(Exception e)
      {

        img_width = iwidth;
        image = new BufferedImage(iwidth, ihigh,
                                                BufferedImage.TYPE_BYTE_INDEXED);
        g = (Graphics2D) image.getGraphics();
      }

    }
    else
    {
      img_width = iwidth;
      image = new BufferedImage(iwidth, ihigh,
                                              BufferedImage.TYPE_BYTE_INDEXED);
      g = (Graphics2D) image.getGraphics();
      g.setColor(bkColor);
      g.fillRect(0, 0, iwidth, ihigh);//画背景颜色

    }

    response.setHeader("Cache-Control", "no-store");
    response.setDateHeader("Expires", 0);
    response.setContentType("image/jpeg");
    ServletOutputStream out = response.getOutputStream();

    if(drawText==null)
      drawText = "测试文字";

    Font mFont = new Font(fontName, Font.PLAIN, ifontSize); //默认字体
    if ("italic".equalsIgnoreCase(fontStyle))
      mFont = new Font(fontName, Font.ITALIC, ifontSize);
    if ("bold".equalsIgnoreCase(fontStyle))
      mFont = new Font(fontName, Font.BOLD, ifontSize);
    if ("plain".equalsIgnoreCase(fontStyle))
      mFont = new Font(fontName, Font.PLAIN, ifontSize);

    if("true".equalsIgnoreCase(is_align_right))
    {
      //以右对齐
      int len = drawText.length();
      ix = img_width - len*(ifontSize +1) - ix;
    }

//    System.out.println("drawText:"+drawText);
    if("false".equals(shadow))
    {
      g.setFont(mFont); //设置字体
      g.setColor(ftColor); //设置颜色
      g.drawString(drawText, ix, iy);
    }
    else
    {
      g.setFont(mFont); //设置字体
      g.setColor(ftColor2); //设置阴影颜色
      g.drawString(drawText, ix+2, iy+1); //打印阴影颜色

      g.setColor(ftColor); //设置颜色
      g.drawString(drawText, ix, iy);
    }
    JPEGEncodeParam jparam = JPEGCodec.getDefaultJPEGEncodeParam(image);
    jparam.setQuality(1,true);
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder (out,jparam);

//    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder (out);
    encoder.encode(image);

    out.close();
  }
  //Clean up resources
  public void destroy() {
  }
}
