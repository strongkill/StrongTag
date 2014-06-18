package net.strong.exutil;


import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * <p>Title:XML常量类 </p>
 * <p>Description: 由于项目服务器的移动或其它别的因原而导至一些常量的值变动，
 * 现将这些常量存放在XML文件中，如果有变动的话，就直接修改XML文件就可以了。</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class xmlConstant {

  protected String path = null;
  protected String filename = "pro-config.xml";
  protected String realPath = null;
  protected String sys_separator = "/"; //文件系统的路径符，"\"(linux)或"/"(windows)
  protected  InputStream in;
  public String upload_pic_dir = null; //上传文件的存放目录
  public String url_base = null; //网站的域名
  public String smtp = null;  //smtp服务器名
  public String smtpU = null; //smtp用户名
  public String smtpP = null; //smtp密码
  public String masterMail = null; //管理员邮箱
  public String secWeb = null; //二级网域名
  public String picUrl = null; //图片显示路径

  Log log = LogFactory.getLog(this.getClass().getName());

  public xmlConstant() throws Exception
  {
    this(System.getProperty("user.dir"));
  }
  public xmlConstant(String path) throws Exception
  {
    this(path,"pro-config.xml");
  }
  public xmlConstant(String path,String filename) throws Exception
  {
    sys_separator = System.getProperty("file.separator");
    this.path = path;
    this.filename = filename;
    try
    {
      iniXmlConstant();
    }
    catch(Exception e)
    {
      // do nothing
    }
  }

  public xmlConstant(String path,String filename,boolean auto) throws Exception
  {
    sys_separator = System.getProperty("file.separator");
    this.path = path;
    this.filename = filename;
    if(auto)
      iniXmlConstant();
  }
  public void iniXmlConstant() throws Exception
  {
    if(filename==null || filename.length() <1 || filename.equalsIgnoreCase("null"))
      filename = "pro-config.xml";

    realPath = path + sys_separator + filename;
//    log.warn("realPath:"+realPath);

    InputStream in = null;
    //String resultStr = null;
    try {
      in = new FileInputStream(realPath);
      SAXBuilder builder = new SAXBuilder();
      Document anotherDocument =
          builder.build(in);
      Element root = anotherDocument.getRootElement();
      upload_pic_dir = root.getChildTextTrim("uploadPicDir");
      url_base = root.getChildTextTrim("urlBase");
      smtp = root.getChildTextTrim("smtp");
      smtpU = root.getChildTextTrim("smtpU");
      smtpP = root.getChildTextTrim("smtpP");
      masterMail = root.getChildTextTrim("masterMail");
      secWeb = root.getChildTextTrim("secWeb");
      picUrl = root.getChildTextTrim("picUrl");
//      in.close();
    } catch(JDOMException e) {
      log.error(e.getMessage());
      throw new Exception("JDOM 出现异常");
    } catch(NullPointerException e) {
      log.error(e.getMessage());
      throw new Exception("处理JDOM时出现异常");
    }
    catch(Exception e)
    {
      log.error(e.getMessage());
      throw new Exception("处理JDOM时出现异常");
    }
    finally
    {
      in.close();
    }

  }
  public String getString(String element) throws Exception {
  {
    realPath = path + sys_separator + filename;
    try {
      in = new FileInputStream(realPath);
      String resultStr;
      SAXBuilder builder = new SAXBuilder();
      Document anotherDocument =
          builder.build(in);
      Element root = anotherDocument.getRootElement();
      resultStr = root.getChildTextTrim(element);
//      in.close();
      return resultStr;
    }
    catch (JDOMException e) {
      throw new Exception("JDOM 出现异常");
    }
    catch (NullPointerException e) {
      throw new Exception("处理JDOM时出现异常");
    }
    finally
    {
      in.close();
    }
  }
}
}
