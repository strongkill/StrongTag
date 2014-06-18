package net.strong.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;


/**
 * <p>Title:  struts结构的文件上传类</p>
 * <p>Description: struts结构的文件上传类，首先在Form中设置一org.apach.struts.upload.formFile
 * 类的一对象，然后可以在Action中应用此类进行保存文件。
 * 本类可以通过构造函数的参数自动进行上传，也可以不设置构造函数的参数，手动设置参数，然后调用doUploadFile进行上传。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class strutsUploadFile {

  /**
   *一次向文件系统写入10k的数据
   */
  protected int diskBufferSize = 10 * 1024;

  Log log = LogFactory.getLog(this.getClass().getName());

  protected String fileName = null;
  protected String realPath = null;

  protected String thisFileName = null; //临时文件名，此文件名只用于此类及其子类

  protected String error_msg = null; //出错信息

  //自动根据当前月份生成一目录，取得的文件存放于生成的目录，文件名称含生成的目录，格式如： 0401/1268555.jpg
  protected boolean autoDir = true ;

  protected String newDir = "";

  protected String isDebug = "false";

  /**
   * 设置上传文件最大允许的大小，以byte为单位，默认为文件最大为200k大小。
   */
  protected long maxSize = 200 * 1024;
  protected org.apache.struts.upload.FormFile upFormFile;
  protected String file_path;
  protected String oldFileName;

  /**
   * 设置上传文件最大允许的大小，以byte为单位，默认为文件最大为200K大小。
   * @param maxSize 文件大小，如１００k，此参数设置为：100×1024或102400
   */
  public void setMaxSize(long maxSize)
  {
    this.maxSize = maxSize;
  }
  public long getMaxSize()
  {
    return this.maxSize;
  }
  public void setAutoDir(boolean autoDir)
  {
    this.autoDir = autoDir;
  }
  public boolean getAutoDir()
  {
    return this.autoDir;
  }

  public String getError_msg()
  {
    return this.error_msg;
  }
  /**
   * 将struts中Form所取到的FormFile对象转化为真正的文件，并存入文件系统中
   * @param newFile  对应于stuts中Form中取到的FormFile对象
   * @param file_path  文件存放路径
   * @param fileName 文件名，如果为空或为null，则文件名取随机文件名
   */
  public strutsUploadFile(FormFile newFile,String file_path,String fileName) throws IOException
  {
    initUploadFile(newFile,file_path,fileName);
  }

  public strutsUploadFile(FormFile newFile,String file_path,String fileName,String isDebug) throws IOException
  {
    initUploadFile(newFile,file_path,fileName);
    this.isDebug = isDebug;
  }

  /**
   * 将struts中Form所取到的FormFile对象转化为真正的文件，并存入文件系统中
   * @param newFile
   * @param file_path
   * @throws IOException
   */
  public strutsUploadFile(FormFile newFile,String file_path) throws IOException
  {
    initUploadFile(newFile,file_path,null);
  }

  /**
   * 将struts中Form所取到的FormFile对象转化为真正的文件，并存入文件系统中,文件存放在系统
   * 默认目录，文件名取随机文件名
   * @param newFile
   * @throws IOException
   */
  public strutsUploadFile(FormFile newFile) throws IOException
  {
    initUploadFile(newFile,System.getProperty("java.io.tmpdir"),null);
  }

  public strutsUploadFile()
  {
    //do nothing;
  }
  public String getFileName()
  {
    String temp = this.fileName;
    char [] temps = temp.toCharArray();
    for(int i=0 ;i<temps.length;i++)
    {
      if(temps[i]=='\\')
        temps[i] = '/';
    }
    temp = String.valueOf(temps);
    return temp;//this.fileName;
  }
  public String getRealPath()
  {
    return this.realPath;
  }
  public void doUploadFile() throws IOException
  {
    if(upFormFile==null)
      throw new IOException("upFormFile can not be null,please set it first!");
    if(file_path == null)
      throw new IOException("file_path can not be null,please set it first!");

    initUploadFile(upFormFile,file_path,fileName);
  }
  protected String initUploadFile(FormFile newFile,String file_path,String fileName)
      throws IOException
  {
    if(newFile==null)
      return null;
    long fileSize = newFile.getFileSize();

    if(fileSize <=0)
      return null;

    if(fileSize > maxSize)
    {
      error_msg = "上传的文件的大小超过最大值，允许上传的最大值为：" + maxSize/1024 + "K";
      throw new IOException(error_msg);
    }

    oldFileName = newFile.getFileName();
    realPath = getRealPath(file_path,fileName,oldFileName);

    if("true".equals(isDebug))
    {
      log.info("realPath:"+realPath);
//      System.out.println("realPath:" + realPath);
//      ProDebug.addDebugLog("realPath:"+realPath);
//      ProDebug.saveToFile();
    }
    InputStream in = newFile.getInputStream();
    BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(realPath)),
        this.diskBufferSize);
    int read = 0;
    byte buffer[] = new byte[this.diskBufferSize];
    while ((read = in.read(buffer, 0, this.diskBufferSize)) > 0)
    {
        fos.write(buffer, 0, read);
    }

    buffer = null;
    fos.flush();
    fos.close();
    fos = null;
    thisFileName = this.fileName;
    in.close();
    in = null;
    newFile.destroy();
    newFile = null;

    return this.fileName; //返回生成的文件名
  }

  /**
   * 获取文件的扩展名
   * @param fileName
   * @return
   */
  protected String getExt(String fileName)
  {
    int pos = fileName.indexOf(".");
    String extName = fileName.substring(pos+1,fileName.length()); //获到文件的扩展名
    return extName;
  }

  /**
   * 根据当前时间生成图片存放的目录
   * @return
   */
  protected String createAutoDir()
  {
    Date now_date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String str_date = sdf.format(now_date);
    int len = str_date.length();

    String str_dd = "01";
    if(len == 8)
       str_dd = str_date.substring(6, 8);
    if(len > 6)
      str_date = str_date.substring(0,6);


    String sys_separator = System.getProperty("file.separator");

    String str_file = str_date + sys_separator + str_dd;


    this.newDir = str_file;
    return str_file;
  }

  /**
   * 获取文件的全路径
   * @param file_path
   * @param fileName
   * @param oldFileName
   * @return
   */
  protected String getRealPath(String file_path,String fileName,String oldFileName)
  {
    String tempRealPath = null;
    String tempFileName = null;
    String extName = ".jpg";
    int pos = 0;
    try
    {
      if(oldFileName!=null && oldFileName.length() >0)
      {
        pos = oldFileName.lastIndexOf(".");
        extName = oldFileName.substring(pos, oldFileName.length()); //获到文件的扩展名
      }
    }
    catch(Exception e)
    {
      log.error("oldFileName:"+oldFileName);
    }
    if(".jpeg".equalsIgnoreCase(extName) || ".jpg".equalsIgnoreCase(extName))
    {
      extName = ".jpg";
    }
    else if(".gif".equalsIgnoreCase(extName))
    {
      extName = ".png";
    }

    if(fileName==null || fileName.length() <1)
    {
      //产生随机的文件名
      long time = System.currentTimeMillis();
      int i=(int)(Math.random()*1000+1);
      long result = time*1000 + i;

      tempFileName = fileName.valueOf(result) + extName;
    }
    else
      tempFileName = fileName;

      this.fileName = tempFileName; //保存文件名

      String sys_separator = System.getProperty("file.separator");

      //判断是否需要自动生成目录，如要，以下进行处理
      if(autoDir)
      {
        String newPath  = null;
//        log.warn("test");
        String tempPath = createAutoDir();
//        log.warn("tempPath:"+tempPath+",file_path:"+file_path);
        if("\\".equals(file_path.substring(file_path.length() -1,file_path.length()))
           ||"/".equals(file_path.substring(file_path.length() -1,file_path.length())))
        {
          newPath = file_path + tempPath;
        }
        else
        {
          newPath = file_path + sys_separator + tempPath;
        }
//        log.warn("newPath:"+newPath);
        if(newPath!=null)
        {
          File f = new File(newPath);
          if (!f.exists())
            f.mkdirs();
          file_path = newPath; //重新设置路径
        }
      }

    if("\\".equals(file_path.substring(file_path.length() -1,file_path.length()))
       ||"/".equals(file_path.substring(file_path.length() -1,file_path.length())))
    {
      tempRealPath = file_path + tempFileName;
    }
    else
    {
      tempRealPath = file_path + sys_separator + tempFileName;
    }

//    log.warn("newDir:"+newDir+" , fileName:"+this.fileName);
    this.fileName = this.newDir + sys_separator + this.fileName;

    return tempRealPath;
  }
  public void setRealPath(String realPath) {
    this.realPath = realPath;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public org.apache.struts.upload.FormFile getUpFormFile() {
    return upFormFile;
  }
  /**
   * 上传页面提供的FormFile
   * @param upFormFile
   */
  public void setUpFormFile(org.apache.struts.upload.FormFile upFormFile) {
    this.upFormFile = upFormFile;
  }
  public String getFile_path() {
    return file_path;
  }
  /**
   * 设置文件路径，文件路径是指文件存到服务器的路径。
   * @param filePath
   */
  public void setFile_path(String file_path) {
    this.file_path = file_path;
  }
  public String getOldFileName() {
    return oldFileName;
  }

}