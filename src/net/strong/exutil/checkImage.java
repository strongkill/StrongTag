package net.strong.exutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.strong.HttpServlet.BaseHttpServlet;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 此servlet用于自动产生检验码，此检验码以图片方式显示，检验码的数据通过随机方式产生，
 * 并将所产生的检验码以 check_no 的名称存入session中，在提交后对应的Action中取出客户
 * 输入的检验码与session中的进行比较，如果不一至，则返回错误。
 * 此servlet可以带以下四个参数
 * 1.width -- 图片的宽，默认为100
 * 2.high -- 图片的高，默认为50
 * 3.bkColor -- 图片的背景色，值从0至5，依次为：红、黑、白、黄、橙、绿，默认为白色
 * 4.ftColor -- 字体的颜色，值从0至5，依次为：红、黑、白、黄、橙、绿，默认为黑色
 * 以上四个参数，如没有设或设不对，将采用默认设置
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class checkImage extends BaseHttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//	private static final String CONTENT_TYPE = "text/html; charset=GBK";

	private static Color color[] = { Color.red, Color.black, Color.white,
		Color.yellow, Color.orange, Color.green };

	//Initialize global variables
	//private String color_line[] = {"6699CC", "99CCFF", "66CC66", "99CCCC", "CC99FF", "99CCFF"};
	/*
	 private String color_font[] = {
	 "660066", "0000FF", "FF0000", "FF00FF","000000" ,
	 "000000"};
	 */
	private static String color_font[] = { "351878", "#6600CC", "#6600FF", "#6633CC",
		"351878", "351878" };
	private static	String str = "12345679abcdefkj";

	//Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		int iwidth = 55;
		int ihigh = 20;
		int ibkColor = 2;
		int iftColor = 1;
		String width = null;
		String high = null;
		String bkColor = null;
		String ftColor = null;

		width = request.getParameter("width"); //宽
		high = request.getParameter("high"); //高
		bkColor = request.getParameter("bkColor"); //背景色,值0-5，依次为：红、黑、白、黄、橙、绿
		ftColor = request.getParameter("ftColor"); //字体色，值0-5

		String str_aa = request.getParameter("str_key");
		String cale = request.getParameter("cale");

		if(str_aa!=null && str_aa.length()>0)
			str = str_aa;

		if (width != null) {
			try {
				iwidth = Integer.parseInt(width);
			} catch (Exception e) {
				iwidth = 100;
			}
		}
		if (high != null) {
			try {
				ihigh = Integer.parseInt(high);
			} catch (Exception e) {
				ihigh = 100;
			}
		}
		if (bkColor != null) {
			try {
				ibkColor = Integer.parseInt(bkColor);
			} catch (Exception e) {
				ibkColor = 100;
			}
		}
		if (ftColor != null) {
			try {
				iftColor = Integer.parseInt(ftColor);
			} catch (Exception e) {
				iftColor = 100;
			}
		}
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream out = response.getOutputStream();
		BufferedImage image = new BufferedImage(iwidth, ihigh,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color[ibkColor]);
		g.fillRect(0, 0, iwidth, ihigh);
		//AffineTransform at = null;

		g.setColor(color[iftColor]);
		String check_no = getRandStr();
		if("cale".equalsIgnoreCase(cale))
			check_no = getRandStr2();
		String path = getServletContext().getRealPath("/WEB-INF/");
		FileInputStream in = new FileInputStream(new File(path
				+ System.getProperty("file.separator") + "ck_image.jpg"));
		int loc_pos = (int) (Math.random() * 20);

		BufferedImage Bi = ImageIO.read(in);
		in.close();
		in = null;
		g.drawImage(Bi, null, 0 - loc_pos, 0);
		/*
		 g.setColor(ColorUtil.getColor(color_line[color_pos]));
		 for(int i=0;i<4;i++)
		 {
		 g.drawRect(1+i*2, 1+i*2, 48-i*4, 17-i*4);
		 //      g.drawRect();
		 }
		 */
		Font f = new Font(null, Font.BOLD, 18);
		g.setFont(f);

		String check_no_1 = check_no.substring(0, 2);
		String check_no_2 = check_no.substring(2, 4);
		String check_no_3 = check_no.substring(4);
		int color_pos = (int) (Math.random() * 5 + 1);
		g.setColor(ColorUtil.getColor(color_font[color_pos]));
		g.drawString(check_no_1, 4, 15);

		color_pos = (int) (Math.random() * 5 + 1);
		g.setColor(ColorUtil.getColor(color_font[color_pos]));
		g.drawString(check_no_2, 24, 15);

		color_pos = (int) (Math.random() * 5 + 1);
		g.setColor(ColorUtil.getColor(color_font[color_pos]));
		g.drawString(check_no_3, 43, 15);

		request.getSession().removeAttribute("check_no"); //移去原有检验码
		request.getSession().setAttribute("check_no", check_no); //将检验码保存入session中
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image);

		check_no = null;
		path = null;
		check_no_1=null;
		check_no_2 = null;
		check_no_3 = null;
		width = null;
		high = null;
		bkColor = null;
		ftColor = null;

		Bi = null;
		image = null;
		g = null;
		encoder = null;
		f = null;
		out.close();
		out = null;
	}

	//Clean up resources
	public void destroy() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 产生随机的检验码
	 * @return
	 */
	private String getRandStr() {
		String result = "";
		for (int i = 0; i < 5; i++) {
			int ind = (int) (Math.random() * (str.length() - 1) + 1);
			String str2 = str.substring(ind, ind + 1);
			result += str2;
		}
		return result;

	}
	public static void main(String[] args){
		for(int i=0;i<1000;i++)
			System.out.println(new checkImage().getRandStr2());
	}
	private String getRandStr2() {
		String str_src1 = "456789";
		String str_src2 = "012345";
		String opts = "+-x";

		int ind = (int) (Math.random() * (str_src1.length() - 1) + 1);
		String str1 = str_src1.substring(ind, ind + 1);
		ind = (int) (Math.random() * (str_src2.length() - 1) + 1);
		String str2 = str_src2.substring(ind, ind + 1);

		ind =(int) (Math.random() * (opts.length() - 1) + 1);
		String opt = opts.substring(ind, ind + 1);

		return str1+opt+str2+"=?";
	}
	/**
	 * 验证输入的验证码是否正确
	 * @param request HttpServletRequest
	 * @param check_value 输入的验证码
	 * @param cale 是否需要计算
	 * @return boolean
	 */
	public static boolean dual(HttpServletRequest request,String check_value,boolean cale){
		String medal_check_no = (String)request.getSession().getAttribute("check_no");
		boolean retval = false;
		if(medal_check_no == null)return retval;
		if(!cale){
			retval = medal_check_no.equalsIgnoreCase(check_value);
		}else{
			try{
				int value = Integer.parseInt(check_value);
				int str1 =Integer.parseInt(medal_check_no.substring(0, 1));
				int str2 = Integer.parseInt(medal_check_no.substring(2,3));

				String opt = medal_check_no.substring(1, 2);
				if("+".equalsIgnoreCase(opt)){
					retval = (value==(str1+str2));
				}else if("-".equalsIgnoreCase(opt)){
					retval = (value==(str1-str2));
				}else{
					retval = (value==(str1*str2));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return retval;
	}
}
