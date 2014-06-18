package net.strong.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 多国语言类</p>
 * <p>Description: 实现多国语言功能</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */

public class MultiLanguage {

  /**
   * 获取默认Locale的资源字符
   * @param key 资源的键值
   * @param resourceName 资源名
   * @return 取得的资源值
   */
  public static String getLocaleStr(String key,String resourceName)
  {
    Locale cur_locale = Locale.getDefault();
    ResourceBundle rb;
    rb = ResourceBundle.getBundle(resourceName,cur_locale);
    String result = rb.getString(key);
    return result;
  }

  /**
   * 获取指定Locale的资源字符
   * @param key 资源的键值
   * @param resourceName 资源名
   * @param cur_locale Locale对象
   * @return 取得的资源值
   */
  public static String getLocaleStr(String key,String resourceName,Locale cur_locale)
  {
    ResourceBundle rb;
    rb = ResourceBundle.getBundle(resourceName,cur_locale);
    String result = rb.getString(key);
    return result;
  }

  /**
   * 将init_str转换为Unicode的编码，一般用于struts中jsp传数据至Action时，
   * 由于Action中取得的编码为gb2312、GBK编码，须将它转换也Unicode编码再存入
   * 数据库，这样就不会出现乱码。
   * @param init_str 须转换的字符串
   * @param request Request对象，将从此对象取得当前的语言，并根据此语言自动调用相应的转换函数
   * @return 转换后的字符串。
   */
  public static String getUnicode(String init_str, HttpServletRequest request)
  {

    return init_str;
/*
    String ser_name = request.getServerName();
    if(!ser_name.equals("www.lighting86.com.cn"))

    if(init_str == null || init_str.length() <= 0)
      return null;
    String result = null;
    String clientLanguage = request.getHeader("Accept-Language");
//    ProDebug.addDebugLog("clientLanguage:"+clientLanguage);
//    ProDebug.saveToFile();
    if(clientLanguage == null)
    {
      clientLanguage = request.getLocale().toString();
    }
//    ProDebug.addDebugLog("clientLanguage2:"+clientLanguage);
//    ProDebug.saveToFile();
    if (clientLanguage != null) {

      if (clientLanguage.equalsIgnoreCase("zh_cn") ||
          clientLanguage.equalsIgnoreCase("zh-cn"))
      {
        //for Simplied Chinese
        result = CodeTransfer.gbToUnicode(init_str);
//        ProDebug.addDebugLog("test at zh_ch ,result :"+result);
//        ProDebug.saveToFile();
      }
      else if (clientLanguage.equalsIgnoreCase("zh_tw") ||
               clientLanguage.equalsIgnoreCase("zh-tw"))
      {

        //for Traditional Chinese
        result = CodeTransfer.big5ToUnicode(init_str);
//        ProDebug.addDebugLog("test at zh_tw ,result :"+result);
//        ProDebug.saveToFile();
      }
      else {
        //default encoding
        result = init_str;
//        ProDebug.addDebugLog("test at else ,result :"+result);
//        ProDebug.saveToFile();
      }

    }
    else
    {
      result = init_str;
    }
    return result;
*/
  }
}