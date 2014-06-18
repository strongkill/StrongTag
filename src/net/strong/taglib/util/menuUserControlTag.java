package net.strong.taglib.util;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;

import net.strong.bean.Constants;

public class menuUserControlTag extends menuTag {

  /**
	 *
	 */
	private static final long serialVersionUID = 1L;

public int doEndTag() throws JspException {

    long start_time = System.currentTimeMillis();
    //boolean newPool = false;
    sql_where =null;
    locale = pageContext.getRequest().getLocale();

//    MessageResources messages =
//        MessageResources.getMessageResources("net.strong.resources.ApplicationResources");
//    String test = messages.getMessage(locale,".index.title");

    xmlPath = pageContext.getServletContext().getRealPath("/WEB-INF/classes/");
    /*
    PoolBean pool = (PoolBean )pageContext.getAttribute("pool",this.scope_type);
    if(pool==null)
    {
      pool = new PoolBean();
      newPool = true;
    }
    */
    if(sqlWhere!=null)
      sql_where = sqlWhere;

    //获取当前用户信息
    net.strong.User user = (net.strong.User) pageContext.getSession().getAttribute(Constants.USER_KEY);
    if(user==null)
    {
      try
      {
        pageContext.getOut().write("<br><b>you have not login<br>please login again!<br>");
        //added by jony 2005-11-02
        pageContext.getOut().flush();
//        pageContext.getOut().close();
      }
      catch(java.io.IOException e)
      {
        log.error(e.getMessage());
        return (EVAL_PAGE);
//        throw new JspException("IO Error: " + e.getMessage());
      }
      return (EVAL_PAGE);
    }
    long user_id = user.getUserId();

//need to control menu by user
      String user_where = "(user_id="+user_id;//
      user_where += ") ";
      if(sql_where!=null)
        sql_where += " and " + user_where;
      else
        sql_where = user_where;


        String sql = "select * from " + tableName ;
        if(sql_where != null)
          sql = sql + " where " +sql_where;

        if(orderBy!=null)
          sql = sql+ " order by " +orderBy;

        try
        {
          showMenuTree(xmlPath, sql);
        }
        catch(Exception e)
        {
          log.error(e.getMessage());
          return (EVAL_PAGE);
//          System.out.println("showMenuTree exception :"+e.getMessage() );
        }

    try
    {
      pageContext.getOut().write(resultBuf.toString());
      //added by jony 2005-11-02
      pageContext.getOut().flush();
//      pageContext.getOut().close();
    }
    catch(java.io.IOException e)
    {
      resultBuf = null;
      log.error(e.getMessage());
      return (EVAL_PAGE);
//      throw new JspException("IO Error: " + e.getMessage());
    }
    resultBuf = null;

    long end_time = System.currentTimeMillis();
    long take_time = end_time - start_time;
    if(log.isDebugEnabled())
      log.debug("menu list spend time:"+take_time);
//    System.out.println("menu list spend time:"+take_time);
    return (EVAL_PAGE);
  }

  public void showMenuTree(String path,String sql) throws Exception
  {
/*    pool = (PoolBean )pageContext.getAttribute("pool",this.scope_type);
    boolean newPool = false;
    if(pool==null)
    {
      //将新生成一个InitAction的方式改为通过单例类获取的方式以减少新对象的生成 2006-07-10
      net.strong.exutil.InitAction initAction = net.strong.exutil.
          singleInitAction.getInitAction(pageContext);
          //new net.strong.exutil.InitAction(pageContext);
//      net.strong.exutil.InitAction initAction = new net.strong.exutil.InitAction(pageContext);
      pool = initAction.getPool();
      log.error("pool is null,create new");
//      pool = new PoolBean();
//      newPool = true;
    }

    if (!pool.isStarted()) {
      log.error("pool is not started");


      pool.setPath(path);
      pool.initializePool();
      System.out.println("run pool.setPath() at dbSelectTag");
    }
    if(newPool)
    {
      pageContext.setAttribute("pool",pool,this.scope_type);

    }
    */
//    con = pool.getConnection();

    net.strong.taglib.util.ClassItems c_item = new net.strong.taglib.util.
        ClassItems();
    c_item.setIdFieldName("bas_id");
    c_item.setParentFieldName(parentIdName);
    c_item.initData(DriverManager.getConnection(Constants.getProxool_alias_name(pageContext)),sql);
//    c_item.initData(con,sql);

    HashMap<String, Object> t_map = null;
    String id = "0";
    String p_id = "0";
    Object hfObj = null;
    Object targetObj = null;
//    String str_msg = null;
    String str_disp = null;
    if (resultBuf==null)
      resultBuf = new StringBuffer();
    ArrayList<String> id_list = new ArrayList<String>(); //记录临时ID号
    while((t_map = c_item.getDataItem(id,p_id))!=null)
    {
      hfObj = t_map.get(pathFieldName); //链接路径
      str_disp = (String)t_map.get(dispFieldName);//显示内容
      targetObj = t_map.get("target");
      //String str_value = String.valueOf(hfObj);
      //String level = String.valueOf(t_map.get("level"));
      String isLeaf = String.valueOf(t_map.get("isLeaf"));
      String str_id = String.valueOf(t_map.get("bas_id"));
      String str_p_id = String.valueOf(t_map.get(parentIdName.toLowerCase() ));
      if("false".equals(isLeaf))
      {
        //含有子菜单
        if(id_list.size()>0)
        {
          //原来已存在父菜单
          String t_id = (String)id_list.get(id_list.size() -1);
          if(t_id!=null && !t_id.equals(str_p_id))
          {
            resultBuf.append("</div>");
            id_list.remove(id_list.size() - 1);
          }

        }
        id_list.add(str_id);
        ID++;
        resultBuf.append("<div id=\"main" + ID + "\" class = \"menu\"");
        resultBuf.append(" onclick = \"expandIt('" + ID + "');return false\">");
        resultBuf.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
        resultBuf.append("<tr><td width='35'");
        resultBuf.append("><img src='images/icon-folder1-close.gif' width='15' height='13'>" );
        resultBuf.append("<img src='images/icon-folder-close.gif' width='16' height='15' align='absmiddle'></td>");
        resultBuf.append("<td>");

        resultBuf.append(str_disp);

        resultBuf.append("</td>");
        resultBuf.append("</tr></table></div>");
        resultBuf.append("<div id='page" + (ID++) +
                         "' class='child' style='padding-left:15px'>");

      }
      else
      {

        resultBuf.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'> <tr> <td></td>");
        resultBuf.append("<td height='20' " );
        resultBuf.append("><img src='images/icon-folder1-open.gif' width='15' height='13'>");
        resultBuf.append("<img src='images/icon-page.gif' width='16' height='15' align='absmiddle' hspace='4'>");
//        Object hfObj = hm.get(pathFieldName);
        String hf = "";
        if(hfObj!=null)
          hf = ((String)hfObj.toString()).trim();

        String str_target = "main";
        if(targetObj!=null)
          str_target = ((String)targetObj.toString()).trim();

        resultBuf.append("<a href='" + hf );
        if (hf.indexOf("?") > 0)
          resultBuf.append("&bas_id=" + str_id + "&ck_menu=true' target='" +
                           str_target + "'>");
        else
          resultBuf.append("?bas_id=" + str_id + "&ck_menu=true' target='" +
                           str_target + "'>");
        resultBuf.append( str_disp);
        resultBuf.append("</a></td> </tr></table>");
      }
      t_map = null;
    }
    resultBuf.append("</div>");
    c_item.clearData();
    CloseCon.Close(con);
  }

}
