package net.strong.taglib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.strong.util.upload.MultipartElement;
import net.strong.util.upload.MultipartIterator;

/**
 * <p>Title: Multipart类型的Request解码</p>
 * <p>Description: 对Multipart类型的Request进行解码，并将所得的结果
 * 存入fMap及fList对象中，如有文件，则文件先上传至服务器，并将文件名存入
 * fMap及fList对象,并将fMap以paramMap存入pageContext对象中,将fList以
 * paramList存入pageContext对象中,JSP中使用时，只需从pageContext对象
 * 取出fMap及fList，再进行处理即可</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author jony
 * @version 1.0
 * @deprecated
 */

public class MultipartDecodeTag extends TagSupport {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String imgPath = "upload_pic";
  private String subImgPath = null;
  private String isFileCanNull = "true";
  private String isOnlyImage = "true";
  private String noChangeFileName = "false";

  private StringBuffer sqlBuf = new StringBuffer();
  private HashMap<String, String> fMap = null;  //记录由request对象传过来的参数及其对应的值
  private HashMap<String, String> fSizeMap = null;
  private ArrayList<String> fList = null;//记录由request对象传过来的参数
  private String sys_separator = null;
/*  private ProDebug myDebug = new ProDebug();
  private int encodingIndex = 1;  //1--gbk or gb2312*/
  private String thisImgPath = null;
  private String thisSubImgPath = null;

//  private HashMap customFieldTypeMap = null;
  private MultipartIterator iterator = null;
//  private HashMap fMapParam = null;
  private HashMap<String, File> fileMap = null;
  private int fileCount = 0;
  private ArrayList<String> fileNameList = null;
  private ArrayList<String> etNameList = null;

  public void release()
  {
    imgPath = "upload_pic";
    subImgPath = null;
    isFileCanNull = "true";
    isOnlyImage = "true";
    noChangeFileName="false";
  }
  public void setIsOnlyImage(String isOnlyImage)
  {
    this.isOnlyImage = isOnlyImage;
  }
  public String getIsOnlyImage()
  {
    return this.isOnlyImage;
  }
  public void setImgPath(String imgPath)
  {
    this.imgPath = imgPath;
  }
  public String getImgPath()
  {
    return this.imgPath;
  }
  public void setSubImgPath(String subImgPath)
  {
    this.subImgPath = subImgPath;
  }
  public String getSubImgPath()
  {
    return this.subImgPath;
  }
  public void setIsFileCanNull(String isFileCanNull)
  {
    this.isFileCanNull = isFileCanNull;
  }
  public String getIsFileCanNull()
  {
    return this.isFileCanNull;
  }
  public void setNoChangeFileName(String noChangeFileName)
  {
    this.noChangeFileName = noChangeFileName;
  }
  public String getNoChangeFileName()
  {
    return this.noChangeFileName;
  }
  public String getSqlResult()
  {
    return sqlBuf.toString();
  }
  public int doStartTag() throws JspException {
    return (SKIP_BODY);

  }

  public int doEndTag() throws JspException {

    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    try
    {

      validate(request);
      iterator = new MultipartIterator(request,1024,4*1024*1024,null);
      if(imgPath!=null)
      {
        thisImgPath = pageContext.getServletContext().getRealPath("/"+imgPath+"/");
      }
      else
      {
        thisImgPath = pageContext.getServletContext().getRealPath("/");
      }

//      initMapParam(request);
//      analyseSubImgPath();
      fileMap = new HashMap<String, File>();
      fileNameList = new ArrayList<String>();
      etNameList = new ArrayList<String>();
      fileCount  = 0;

      initMap(request);
      analyseSubImgPath();
      uploadFile();
    }
    catch(Exception e)
    {
      throw new JspException("some exception occur : " + e.getMessage());
    }
    pageContext.setAttribute("paramMap",fMap);
    pageContext.setAttribute("paramList",fList);
    return (EVAL_PAGE);
  }

  /**
   * 将文件存到指定目录
   * @throws Exception
   */
  private void uploadFile() throws Exception
  {
    String realFileName = null;
    for(int i = 0 ;i<fileNameList.size();i++)
    {
      String fileName = fileNameList.get(i);
      String e_name = etNameList.get(i);

      Date nowDate = new Date();
      if(noChangeFileName.compareToIgnoreCase("false")==0)
      {
        int pIndex = fileName.indexOf(".");
        int fLen = fileName.length();
        String fileExt = ".bmp";
        if(pIndex>0)
          fileExt = fileName.substring(pIndex,fLen);

        fileName = String.valueOf(nowDate.getTime())+fileExt;
      }

      if(thisSubImgPath!=null)
      {
        realFileName = thisImgPath + sys_separator + thisSubImgPath +
                       sys_separator + fileName;
      }
      else
      {
        realFileName = thisImgPath + sys_separator + fileName;
      }

//      String temp = "file"+(i+1);
      File myFile = fileMap.get(e_name.trim());

      FileInputStream fin = new FileInputStream(myFile);
      FileOutputStream fout = new FileOutputStream(realFileName);

      byte[] buffer = new byte[8192];
      int bytesRead = 0;
      while ((bytesRead = fin.read(buffer, 0, 8192)) != -1)
      {
        fout.write(buffer, 0, bytesRead);
      }
      fout.close();
      fin.close();
      fMap.put(e_name.trim(),fileName);
    }

  }
  /**
   * 判断对象某些必须的参数是否有设置完全
   * @param request
   * @throws Exception
   */
  private void validate(HttpServletRequest request) throws Exception
  {
    sys_separator = System.getProperty("file.separator");
    if(request==null)
      throw new Exception("request is null");

  }

  /**
   * 分析参数subImgPath,如为:xxx格式，则从fMapParam对象中取值。
   */
  private void analyseSubImgPath() throws Exception
  {
    String temp = subImgPath;
    if(temp==null)
      return;
    temp = temp.trim();
    if(temp.length() > 0)
    {
      int ii = -1;
      if((ii=temp.indexOf(":"))>=0)
      {
        String temp_str = temp.substring(ii+1);
        thisSubImgPath = fMap.get(temp_str.trim());
        if(thisSubImgPath!=null)
          thisSubImgPath = thisSubImgPath.trim();
      }
      else
        thisSubImgPath = temp;
    }
  }


  /**
   * 分解request对象，将其所含有的参数及其值存入fMap及fList对象中
   * 如果是文件是话，保存文件，并将文件名存入fMap中。
   */
  private void initMap(HttpServletRequest request) throws Exception
  {
    long fileSize = 0;
    fMap = new HashMap<String, String>();
    fSizeMap = new HashMap<String, String>();
    fList = new ArrayList<String>();
    MultipartElement element = null;
    while ((element = iterator.getNextElement()) != null)
    {
      if( element.isFile())
      {
        fileSize = element.getFileSize();
        if(fileSize>0)
        {
          if(isOnlyImage.compareToIgnoreCase("true")==0)
          {
            String contentType = element.getContentType();
            if(contentType.indexOf("image")<0)
              throw new Exception("you can only upload image file ");
          }
          File myFile = element.getFile();
          String fileName = element.getFileName();
          String e_name = element.getName();
          if(e_name==null || e_name.trim().length() ==0)
            throw new Exception("there are some com name is null");
          fileCount++;
//          String temp = "file"+fileCount;
          //先将文件保存至fileMap对象中.
          fileMap.put(e_name.trim(),myFile);
          fileNameList.add(fileName);
          etNameList.add(e_name);
          fSizeMap.put(e_name.trim(),String.valueOf(fileSize));
        }
        else
        {
          if(isFileCanNull.compareToIgnoreCase("false")==0)
            throw new Exception("file can not be null or empty");
        }
      }
      else
      {
        String e_name = element.getName();
        //String e_filename = element.getFileName();
        if(e_name==null || e_name.trim().length() ==0)
          throw new Exception("there are some com name is null");

        fMap.put(e_name.trim(),element.getValue());
        fList.add(e_name.trim());
      }
    }

  }

}