package net.strong.exutil;


/**
 *
 * <p>Title:初始化xmlConstant </p>
 * <p>Description:在action中，传入servlet参数，即可获取xmlConstant对象，并自动将此对象存入
 * servlet中，平常的类可以传入path来获取xmlConstant对象 </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class InitXmlConstant {

  protected String path = null;
  /**
   * realPath为数据库设置文件database-config.xml文件所在的目录
   */
  protected String realPath = "/WEB-INF/classes/";
  private xmlConstant xmlCon = null;

  public void setRealPath(String newRealPath)
  {
    this.realPath = newRealPath;
  }
  public String getRealPath()
  {
    return this.realPath;
  }
  public xmlConstant getXmlConstant()
  {
    return this.xmlCon;
  }
  public InitXmlConstant(javax.servlet.ServletContext servletContext)
  {
    path = servletContext.getRealPath("/WEB-INF/classes/");
    String trade_id = (String)servletContext.getAttribute("trade_id");
    xmlCon = (xmlConstant)servletContext.getAttribute("xmlConstant");
    boolean newXml = false;
    if(xmlCon==null)
    {
      try
      {
        xmlCon = new xmlConstant(path,trade_id);
        newXml = true;
      }
      catch(Exception e)
      {
        //获取xmlConstant时出现异常，设其值为null;
         xmlCon = null;
      }
    }
    if(newXml)
    {
      servletContext.setAttribute("xmlConstant",xmlCon);
    }

  }
  public InitXmlConstant(org.apache.struts.action.ActionServlet servlet,String xmlName)
  {
    path = servlet.getServletContext().getRealPath("/WEB-INF/classes/");
    String trade_id = (String)servlet.getServletContext().getAttribute("trade_id");
    xmlCon = (xmlConstant)servlet.getServletContext().getAttribute("xmlConstant");
    boolean newXml = false;
    if(xmlCon==null)
    {
      try
      {
        xmlCon = new xmlConstant(path,xmlName);
        newXml = true;
      }
      catch(Exception e)
      {
        //获取xmlConstant时出现异常，设其值为null;
         xmlCon = null;
      }
    }
    if(newXml)
    {
      servlet.getServletContext().setAttribute("xmlConstant",xmlCon);
    }

  }

  public InitXmlConstant(org.apache.struts.action.ActionServlet servlet)
  {
    path = servlet.getServletContext().getRealPath("/WEB-INF/classes/");
    String trade_id = (String)servlet.getServletContext().getAttribute("trade_id");
    xmlCon = (xmlConstant)servlet.getServletContext().getAttribute("xmlConstant");
    boolean newXml = false;
    if(xmlCon==null)
    {
      try
      {
        xmlCon = new xmlConstant(path,"pro-config.xml");
        newXml = true;
      }
      catch(Exception e)
      {
        //获取xmlConstant时出现异常，设其值为null;
         xmlCon = null;
      }
    }
    if(newXml)
    {
      servlet.getServletContext().setAttribute("xmlConstant",xmlCon);
    }
  }

  public InitXmlConstant(String path,String trade_id)
  {
    try
    {
      xmlCon = new xmlConstant(path,trade_id);
    }
    catch(Exception e)
    {
      //获取xmlConstant时出现异常，设其值为null;
       xmlCon = null;
    }
  }
}
