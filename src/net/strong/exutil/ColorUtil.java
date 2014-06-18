package net.strong.exutil;

import java.awt.Color;
/**
 * <p>Title: 获取颜色类</p>
 * <p>Description: 给定一个字符串，获取对应的颜色对象（java.awt.Color)，
 * 字符串必须是用16进制表示颜色的字符串，如（#FFAA00或FFAA00)</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */

public class ColorUtil {
  public ColorUtil() {
  }

  /**
   * 给定一个字符串，返回对应的颜色对象
   * @param color 颜色字符串，格式为 #FFAA00 或 FFAA00
   * @return
   */
  public static Color getColor(String color)
  {
    String temp = color;
    int pos = temp.indexOf("#");
    int len = temp.length();
    if(len!=7 && len !=6)
      return null;
    if(pos==0)
      temp = temp.substring(1);
    String str_r = temp.substring(0,2);
    String str_g = temp.substring(2,4);
    String str_b = temp.substring(4);
    int int_r = HexConverter.getIntValue(str_r);
    int int_g = HexConverter.getIntValue(str_g);
    int int_b = HexConverter.getIntValue(str_b);
    Color t_color = new Color(int_r,int_g,int_b);
    return t_color;
  }

}