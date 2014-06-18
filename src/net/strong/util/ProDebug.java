package net.strong.util;

/**
 * <p>Title: 调试类</p>
 * <p>Description: 用于开发调试，能够将调试信息以文件的形式保存起来，
 * 默认情况下，在window下保存在c:\winnt\system32下，在linux下保存在tomcat
 * 启动目录
 * 使用方法：
 * １．addDebugLog(String log),增加log
 * ２．saveToFile()，保存至文件</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author jony
 * @version 1.0
 */


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 * @deprecated 此类弃用，改用log4j
 */

public class ProDebug {
  /*
  private static StringBuffer debugString = new StringBuffer();
  private static String path = null;
  private static String path_sep = "/";  //文件路径符,"\"(windows),"/"(linux)
  private static String fileName = "Debug";
  private static String errorMessage = null;
*/
  public ProDebug()
  {
    /*
    path = System.getProperty("user.dir");
    path_sep = System.getProperty("file.separator");
*/
  }
  public ProDebug(String fileName)
  {
    /*
    this.fileName = fileName;
    path = System.getProperty("user.dir");
    path_sep = System.getProperty("file.separator");
*/
  }
  public String getErrorMessage()
  {
    return null;
  }
  public void setPath(String path)
  {
   // this.path = path;
  }
  public String getPath()
  {
    return null;
  }
  public void setFileName(String fileName)
  {
//    this.fileName = fileName;
  }
  public String getFileName()
  {
    /*
    SimpleDateFormat formatter
        = new SimpleDateFormat ("yyyy_MM_dd hh:mm:ss ");//"yyyy.MM.dd G 'at' hh:mm:ss a zzz");
    Date curDate = new Date();
    String curDateStr = formatter.format(curDate);
    return fileName + curDateStr.substring(0,10) + ".log";
*/
    return null;
  }
  public static void addDebugLog(String log)
  {
  //   "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM

//  if(!isDebug()) //不在调试期
//    return;
  /*
   String str =  System.getProperty("os.name");
   if("linux".equalsIgnoreCase(str))
     return;

    SimpleDateFormat formatter
        = new SimpleDateFormat ("yyyy_MM_dd hh:mm:ss aaa");//"yyyy.MM.dd G 'at' hh:mm:ss a zzz");
    Date curDate = new Date();
    String curDateStr = formatter.format(curDate);
    String templog = curDateStr +"---- " + log+"\n";
    debugString.append(templog);
   */
  }
  public String getDebugString()
  {
    return null;

//    return this.debugString.toString();
  }
  public static boolean saveToFile(String fileName)
  {
//    if(!isDebug()) //不在调试期
//      return true;
    /*
    String str =  System.getProperty("os.name");
    if("linux".equalsIgnoreCase(str))
      return true;

    boolean result = true;
    try
    {
      String testStr = debugString.toString();
      PrintWriter out = new PrintWriter(
          new BufferedWriter(
          new FileWriter(fileName)));
      out.print(testStr);
      out.close();
    }
    catch(Exception e)
    {
      errorMessage += e.getMessage();
      result = false;
    }
    return result;
*/
    return true;
  }
  public static boolean saveToFile()
  {
//    if(!isDebug()) //不在调试期
//      return true;
    /*
    String str =  System.getProperty("os.name");
    if("linux".equalsIgnoreCase(str))
      return true;

    SimpleDateFormat formatter
        = new SimpleDateFormat ("yyyy_MM_dd hh:mm:ss aaa ");//"yyyy.MM.dd G 'at' hh:mm:ss a zzz");
    Date curDate = new Date();
    String curDateStr = formatter.format(curDate);
    String thisfileName = fileName + curDateStr.substring(0,10);

    boolean result = true;
    StringBuffer inStringBuf = new StringBuffer();
    String inString;
    String totalPath = null;
    if(path==null || path.length() ==0)
      totalPath = thisfileName + ".log";
    else
      totalPath = path + path_sep +thisfileName + ".log";
*/
/*
    try
    {
      BufferedReader in = new BufferedReader(new FileReader(totalPath));
      while((inString=in.readLine())!=null)
      {
        inStringBuf.append(inString+"\n");
      }
      in.close();
    }
    catch(Exception e)
    {
      errorMessage = errorMessage + e.getMessage();
      result = false;
    }
*/
/*
    try
    {
      String testStr = inStringBuf.toString() + debugString.toString();
      PrintWriter out = new PrintWriter(
          new BufferedWriter(
          new FileWriter(totalPath)));
      out.print(testStr);
      out.close();
    }
    catch(Exception e)
    {
      errorMessage += e.getMessage();
      result = false;
    }
    return result;
        */
       return true;
  }
  private static boolean isDebug()
  {
    /*
    boolean result = true;
    try
    {
      BufferedReader in = new BufferedReader(new FileReader("/home/prodebug.flag"));
      String str = new String();
      while((str=in.readLine())!=null)
      {
        if("true".equalsIgnoreCase(str))
        {
          result = false;
          break;
        }
      }
      in.close();
    }
    catch(Exception e)
    {
      result = true;
    }
        */
    return false;
  }
}