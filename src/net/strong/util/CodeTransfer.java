package net.strong.util;


import java.io.UnsupportedEncodingException;
/**
 * <p>Title: 编码转换及加可逆加密与解密</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class CodeTransfer {

  private String ENCODE_KEY1;
  private String ENCODE_KEY_A;
  private String ENCODE_KEY2;
  private String ENCODE_KEY_B;
  private String ENCODE_KEY3;
  private String ENCODE_KEY_C;
  private String ENCODE_KEY4;
  private String ENCODE_KEY_D;
  private String ENCODE_KEY5;
  private String ENCODE_KEY_E;
  private String ENCODE_KEY6;

  public CodeTransfer() {
    ENCODE_KEY1 	= "zxcvbnm,./asdfg";
    ENCODE_KEY_A 	= "cjk;";
    ENCODE_KEY2 	= "hjkl;'qwertyuiop";
    ENCODE_KEY_B 	= "cai2";
    ENCODE_KEY3 	= "[]\\1234567890-";
    ENCODE_KEY_C 	= "%^@#";
    ENCODE_KEY4 	= "=` ZXCVBNM<>?:LKJ";
    ENCODE_KEY_D 	= "*(N";
    ENCODE_KEY5 	= "HGUAQWSRTFYEDI";
    ENCODE_KEY_E 	= "%^HJ";
    ENCODE_KEY6 	= "OP%$}|#_)(*{+^&@!~";
  }
  /**
  *解密函数
  *参数：varCode--需解密的字符串
  *返回值：解密后的字符串
  */
  public String decryption(String varCode) throws Exception {
    String DecodeStr;
    try {
      if(varCode.length() == 0){
        DecodeStr = "";
        return DecodeStr;
      }
      String strKey = ENCODE_KEY1 + ENCODE_KEY2 + ENCODE_KEY3 + ENCODE_KEY4 + ENCODE_KEY5 + ENCODE_KEY6;
      if(varCode.length() % 2 == 1)  varCode = varCode + "?";
      String des = "";
      int midNum = Math.round(varCode.length() / 2 - 1);
      int n;
      for(n = 0; n <= midNum; n++) {
        int num1 = varCode.charAt(n * 2);
        int num2 = strKey.indexOf(varCode.charAt(n * 2 + 1));
        int num3 = num1 ^ num2;
        char xorstr = (char)num3;
        des = des + xorstr;
      }

      int onenum = 1;
      char onestr = (char)onenum;
      n = des.indexOf(onestr);
      if(n >= 0) DecodeStr = des.substring(0, n);
      else DecodeStr = des;
    } catch(Exception ex) {
      DecodeStr = "Exception/Message:" + ex.getMessage();
    }
    return DecodeStr;
  }
  /**
   *加密函数
   */
  public String encryption(String sSource) throws Exception {
    String EncodeStr;
    try {
      if(sSource.length() == 0) {
        EncodeStr = "";
        return EncodeStr;
      }
      String strKey = ENCODE_KEY1 + ENCODE_KEY2 + ENCODE_KEY3 + ENCODE_KEY4 + ENCODE_KEY5 + ENCODE_KEY6;
      int onenum = 1;
      char onestr = (char)onenum;
      for(; sSource.length() < 8; sSource = sSource + onestr);
      String des = "";
      for(int n = 1; n <= sSource.length(); n++) {
        int code;
        int num1;
        char charcode;
        do {
          double myrandom = Math.random();
          code = (int)Math.round(myrandom * 100D);
          for(num1 = sSource.charAt(n - 1); code > 0 && ((code ^ num1) < 0 || (code ^ num1) > 90); code--);
          charcode = (char)code;
        } while(code <= 35 || code >= 122 || charcode == '|' || charcode == '\'' || charcode == ',' || strKey.charAt(code ^ num1) == '|' || strKey.charAt(code ^ num1) == '\'' || strKey.charAt(code ^ num1) == ',');
        charcode = (char)code;
        int num2 = sSource.charAt(n - 1);
        des = des + charcode + strKey.charAt(code ^ num2);
      }
      EncodeStr = des;
    } catch(Exception ex) {
      EncodeStr = "Exception/Message:" + ex.getMessage();
    }
    return EncodeStr;
  }


/**
*convert gb2312 to unicode
*/
  public static String gbToUnicode(String s)
  {
    if(s==null)
      return s;
    try
    {
      return new String(s.getBytes("ISO8859_1"), "gb2312");
    }
    catch (UnsupportedEncodingException uee)
    {
      return s;
    }
  }
/**
*convert unicode to gb2312\uE6C4
*/
  public static String unicodeTogb(String s)
  {
    if(s==null)
      return s;
    try
    {
      return new String(s.getBytes("gb2312"), "ISO8859_1");
    }
    catch (UnsupportedEncodingException uee)
    {
      return s;
    }
  }

  /**
  *convert gbk to unicode
  */
    public static String gbkToUnicode(String s)
    {
      if(s==null)
        return s;

      try
      {
        return new String(s.getBytes("ISO8859_1"), "GBK");
      }
      catch (UnsupportedEncodingException uee)
      {
        return s;
      }
    }
  /**
  *convert unicode to gbk\uE6C4
  */
    public static String unicodeTogbk(String s)
    {
      if(s==null)
        return s;
      try
      {
        return new String(s.getBytes("GBK"), "ISO8859_1");
      }
      catch (UnsupportedEncodingException uee)
      {
        return s;
      }
    }

    public static String UnicodeToISO(String s)
    {
      if(s==null)
        return s;
      try
      {
        return new String(s.getBytes("UTF-8"), "ISO8859_1");
      }
      catch (UnsupportedEncodingException uee)
      {
        return s;
      }

    }

    public static String ISOToUnicode(String s)
    {
      if(s==null)
        return s;
      try
      {
        return new String(s.getBytes("ISO8859_1"), "UTF-8");
      }
      catch (UnsupportedEncodingException uee)
      {
        return s;
      }
    }
    /**
    *convert gbk to unicode
    */
      public static String big5ToUnicode(String s)
      {
        if(s==null)
          return s;
        try
        {
          return new String(s.getBytes("ISO8859_1"), "BIG5");
        }
        catch (UnsupportedEncodingException uee)
        {
          return s;
        }
      }
    /**
    *convert unicode to gbk\uE6C4
    */
      public static String unicodeTobig5(String s)
      {
        if(s==null)
          return s;
        try
        {
          return new String(s.getBytes("BIG5"), "ISO8859_1");
        }
        catch (UnsupportedEncodingException uee)
        {
          return s;
        }
      }

/**
*convert string to hex
*/
  public static String toHexString(String s)
  {
    if(s==null)
      return s;
    String str="";
    for (int i=0; i<s.length(); i++)
    {
      int ch=(int)s.charAt(i);
      String s4="0000"+Integer.toHexString(ch);
      str=str+s4.substring(s4.length()-4)+" ";
    }
    return str;
  }
  /**
   *将字符串转换成Html可读格式。
   */
  public static String toHtmlString(String value)
  {
    if (value == null)
        return (null);

    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    for (int i = 0; i < content.length; i++) {
        switch (content[i]) {
        case '<':
            result.append("&lt;");
            break;
        case '>':
            result.append("&gt;");
            break;
        case '&':
            result.append("&amp;");
            break;
        case '"':
            result.append("&quot;");
            break;
        case '\'':
            result.append("&#39;");
            break;
        case '\n':
          result.append("<br>");
          break;
        default:
            result.append(content[i]);
				break;
        }
    }
    return (result.toString());

  }
}