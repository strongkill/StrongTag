package net.strong.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.strong.util.upload.MultipartElement;
import net.strong.util.upload.MultipartIterator;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class UploadTestBean {
  private String boundary = null;
  private String contentType = null;
  protected HttpServletRequest request = null;
  public static String HEADER_CONTENT_TYPE = "Content-Type";
  private static final String PARAMETER_BOUNDARY = "boundary=";
  private HashMap map = null;

  public void doUpload(HttpServletRequest request) throws
  IOException {
    ProDebug myDebug = new ProDebug();
    myDebug.addDebugLog("start do upload");
    myDebug.saveToFile();

     map = new HashMap();
    MultipartIterator iterator = new MultipartIterator(request);
    MultipartElement element;
    while ((element = iterator.getNextElement()) != null) {
      if(element.isFile())
      {
        map.put(element.getName(),element.getFileName());
      }
      else
      {
        map.put(element.getName(),element.getValue());
      }
      myDebug.addDebugLog("name :" + element.getName());
      myDebug.saveToFile();
    }

    this.request = request;
    getContentTypeOfRequest();
    getBoundaryFromContentType();
    PrintWriter pw = new PrintWriter(
        new BufferedWriter(new FileWriter("Demo.out")));
    ServletInputStream in = request.getInputStream();
    int i = in.read();
    while (i != -1) {
      pw.print((char) i);
      i = in.read();
    }
    pw.close();
  }

  public String getContentType()
  {
    return this.contentType;
  }
  public String getBoundary()
  {
    return this.boundary;
  }
  public HashMap getMap()
  {
    return map;
  }
  private final void getContentTypeOfRequest()
  {
      this.contentType = request.getContentType();
      if (this.contentType == null)
      {
          this.contentType = this.request.getHeader(HEADER_CONTENT_TYPE);
      }
  }

  private final void getBoundaryFromContentType() throws IOException
  {
      if (this.contentType.lastIndexOf(PARAMETER_BOUNDARY) != -1)
      {
          String _boundary = this.contentType.substring(this.contentType.lastIndexOf(PARAMETER_BOUNDARY) + 9);
          if (_boundary.endsWith("\n"))
          {
              //strip it off
              this.boundary = _boundary.substring(0, _boundary.length()-1);
          }
          this.boundary = _boundary;
      }
      else
      {
          this.boundary = null;
      }
      //throw an exception if we're unable to obtain a boundary at this point
      if ((this.boundary == null) || (this.boundary.length() < 1))
      {
          throw new IOException("io exception");
      }
  }

}