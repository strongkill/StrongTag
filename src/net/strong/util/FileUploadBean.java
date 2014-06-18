package net.strong.util;

/**
 * <p>Title: 文件上传类（功能不强）</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;


public class FileUploadBean
{
private String savePath, filepath, filename, contentType;
private Dictionary fields;

public String getFilename()
{
  return filename;
}
public String getFilepath()
{
  return filepath;
}

public void setSavePath(String savePath)
{
  this.savePath = savePath;
}

public String getContentType()
{
  return contentType;
}
public String getFieldValue(String fieldName)
{
  if (fields == null || fieldName == null)
    return null;
  return (String) fields.get(fieldName);
}

private void setFilename(String s)
{
  if(s==null)
    return;
  int pos = s.indexOf("filename=\"");
  if (pos != -1)
  {
    filepath = s.substring(pos+10, s.length()-1);
// Windows浏览器发送完整的文件路径和名字
// 但Linux/Unix和Mac浏览器只发送文件名字
    pos = filepath.lastIndexOf("\\");
    if (pos != -1)
      filename = filepath.substring(pos + 1);
    else
      filename = filepath;
  }
}

private void setContentType(String s)
{
  if (s==null)
    return;
  int pos = s.indexOf(": ");
  if (pos != -1)
    contentType = s.substring(pos+2, s.length());
}
private void cpyBytes(byte[] bytes1,byte[] bytes2,int len)
{
  for(int i = 0;i<len;i++)
  {
    bytes2[i] = bytes1[i];
  }
}
public void doUpload(HttpServletRequest request) throws IOException
{
  ServletInputStream in = request.getInputStream();
//  DataInputStream is = new DataInputStream(in);
//  is.readByte();
//  File test = new File("D:\test.txt");
  byte[] line = new byte[1024];
  byte[] line_temp = new byte[1024];
  int l_len;
  int i = in.readLine(line, 0, 1024);
  if (i < 3)
    return;
  int boundaryLength = i - 2;
  String boundary = new String(line, 0, boundaryLength); //-2丢弃换行字符
  fields = new Hashtable();
  while (i != -1)
  {
    String newLine = new String(line, 0, i);
    if (newLine.startsWith("Content-Disposition: form-data; name=\""))
    {
      if (newLine.indexOf("filename=\"") != -1)
      {
        setFilename(new String(line, 0, i-2));
        if (filename==null)
          return;
//文件内容
        i = in.readLine(line, 0, 128);
        setContentType(new String(line, 0, i-2));
        i = in.readLine(line, 0, 128);//空行

        i = in.readLine(line, 0, 128);
        newLine = new String(line, 0, i);
        l_len = i; //记下line的长度
        cpyBytes(line,line_temp,i);
        FileOutputStream fout = new FileOutputStream(new
            File((savePath==null?"":savePath)+filename));
//        PrintWriter pw = new PrintWriter(new BufferedWriter(new
//            FileWriter((savePath==null? "" : savePath) + filename)));
        while (i != -1 && !newLine.startsWith(boundary))
        {
// 文件内容的最后一行包含换行字符
// 因此我们必须检查当前行是否是最
// 后一行
          i = in.readLine(line, 0, 128);
          if ((i==boundaryLength+2 || i==boundaryLength+4)
              && (new String(line, 0, i).startsWith(boundary)))
          {
            fout.write(line_temp,0,l_len - 2);
//            pw.print(newLine.substring(0, newLine.length()-2));
          }
          else
          {
           fout.write(line_temp);
//            pw.print(newLine);
          }
//          i = in.readLine(line, 0, 128);
          newLine = new String(line, 0, i);
          cpyBytes(line,line_temp,i);
          l_len = i;
        }
        fout.close();
//        pw.close();
     }
     else
     {
// 普通表单输入元素
// 获取输入元素名字
       int pos = newLine.indexOf("name=\"");
       String fieldName = newLine.substring(pos+6, newLine.length()-3);
       i = in.readLine(line, 0, 128);
       i = in.readLine(line, 0, 128);
       newLine = new String(line, 0, i);
       StringBuffer fieldValue = new StringBuffer(128);
       while (i != -1 && !newLine.startsWith(boundary))
       {
// 最后一行包含换行字符
// 因此我们必须检查当前行是否是最后一行
         i = in.readLine(line, 0, 128);
         if ((i==boundaryLength+2 || i==boundaryLength+4)
             && (new String(line, 0, i).startsWith(boundary)))
           fieldValue.append(newLine.substring(0, newLine.length()-2));
         else
           fieldValue.append(newLine);
         newLine = new String(line, 0, i);
       }
       fields.put(fieldName, fieldValue.toString());
     }
    }
    i = in.readLine(line, 0, 128);
  }
}
}
