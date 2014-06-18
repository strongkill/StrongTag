package net.strong.taglib.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;

import net.strong.bean.Constants;

//import net.strong.database.PoolBean;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 * @deprecated
 */

public class dbListArrayPTag extends dbListArrayTag {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected int index = 0;
  protected int level = 0;
  protected ArrayList<HashMap<String, Object>> rows = null;

  private Connection con = null;
  //private PoolBean pool = null;

  protected String parentIdName = "PARENT_ID";
  protected String temp_sqlWhere = null;
  public void setParentIdName(String parentIdName)
  {
    this.parentIdName = parentIdName;
  }
  public String getParentIdName()
  {
    return this.parentIdName;
  }

  public int doEndTag() throws JspException
  {
    //String path = pageContext.getServletContext().getRealPath("/WEB-INF/classes/");
    //获取当前所在页
    Object pageObj = pageContext.getRequest().getParameter("page");
    //获取最大页数
    Object maxPageObj = pageContext.getAttribute("maxPage");
    //获取最大记录数
    Object maxRecordObj = pageContext.getAttribute("maxRecord");
    if(pageObj!=null)
      pageIndex = Integer.valueOf(String.valueOf(pageObj)).intValue();
    if(maxPageObj!=null)
      maxPageIndex = Integer.valueOf(String.valueOf(maxPageObj)).intValue();
    if(maxRecordObj!=null)
      maxRecord = Integer.valueOf(String.valueOf(maxRecordObj)).intValue() ;

    db_sql_where = parentIdName + "=0";

/*    pool = (PoolBean )pageContext.getAttribute("pool",scope_type);
    boolean newPool = false;
    if(pool==null)
    {
      //将新生成一个InitAction的方式改为通过单例类获取的方式以减少新对象的生成 2006-07-10
      net.strong.exutil.InitAction initAction = net.strong.exutil.
          singleInitAction.getInitAction(pageContext);
//      net.strong.exutil.InitAction initAction = new net.strong.exutil.InitAction(pageContext);
      pool = initAction.getPool();
      log.error("pool is null,create new");
    }
*/
    
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    rows = new ArrayList<HashMap<String, Object>>();
    //ArrayList temp_rows = new ArrayList();
/*
    try
    {
      if(!pool.isStarted())
      {
        pool.setPath(path);
        pool.initializePool();
      }
      con = pool.getConnection();
    }
    catch(Exception e)
    {
      log.error(e.getMessage());
      throw new JspException("initializePool error!" + e.getMessage());
    }

    if(newPool)
    {
      pageContext.setAttribute("pool",pool,scope_type);
    }
*/
    long p_max = 0;
    String max_sql = null;
    try
    {
      max_sql = createMaxSQL();

      PreparedStatement pstmt2 = con.prepareStatement(max_sql);
      ResultSet rs2 = pstmt2.executeQuery();
      if(rs2.next())
      {
        Object max_obj = rs2.getObject(1);
        if(max_obj!=null)
          maxPageIndex = Integer.valueOf(String.valueOf(max_obj)).intValue();
      }
      rs2.close();
      pstmt2.close();
      p_max = Math.round(Math.ceil(maxPageIndex*1.0/pageMax));
      if(maxPageIndex<pageIndex)
        pageIndex = 0;
    }
    catch(SQLException e)
    {
      log.error(e.getMessage());
      log.error("sql:"+max_sql);

      throw new JspException("database perform error : " + e.getMessage());
    }
    String this_sql = createSQL();

    try
    {
      pstmt = con.prepareStatement(this_sql);
      rs = pstmt.executeQuery();
      //ResultSetMetaData rsmd = rs.getMetaData();
      //int colCount = rsmd.getColumnCount();
      index = 0;
      //String str_single = "false";
      int rowCount = 0;
      rs.last();
      rowCount = rs.getRow();
      rs.beforeFirst();
      int b_id[] = new int[rowCount];
      int p_id[] = new int[rowCount];
      int thisRow = 0;

      while(rs.next())
      {
        b_id[thisRow] = rs.getInt("BAS_ID");
        p_id[thisRow] = rs.getInt(parentIdName);
        thisRow++;
      }
      rs.close();
      pstmt.close();
      showMenuTree(b_id,rowCount);
      net.strong.util.Helper.cleanup(con);
//      pool.releaseConnection(con);
    }
    catch(SQLException e)
    {
      throw new JspException("database perform error : " + e.getMessage());
    }


    pageContext.getRequest().setAttribute(this.name,rows);
    pageContext.setAttribute("maxPage",String.valueOf(p_max));
    pageContext.setAttribute("maxRecord",String.valueOf(maxPageIndex));

    return (EVAL_PAGE);
  }

  public void showMenuTree(int bas_id[],int rowCount) throws SQLException
  {
    String test = "";
    for(int ii = 0;ii<rowCount;ii++)
    {
      test += "bas_id="+bas_id[ii]+",";
    }
    for(int j=0;j<rowCount;j++)
    {
      String sql1 = null;
      String sql2 = null;
      try
      {
    	  
        if(con==null)
        {
        	con = DriverManager.getConnection(Constants.getProxool_ReadOnly_Pools(pageContext));
          //con = pool.getConnection();
        }

        String t_where = createWhereSQL(false);
        if(t_where!=null)
          sql1 = "select * from "+sqlTablename + " where BAS_ID="+
          String.valueOf(bas_id[j])+ " and " + t_where +" order by BAS_ID";
        else
          sql1 = "select * from "+sqlTablename + " where BAS_ID="+
          String.valueOf(bas_id[j])+" order by BAS_ID";
        PreparedStatement pstmt_sub = con.prepareStatement(sql1);
        
        ResultSet rs_sub = pstmt_sub.executeQuery();
        ResultSetMetaData rsmd = rs_sub.getMetaData();
        int colCount = rsmd.getColumnCount();

        if(rs_sub.next())
        {
          HashMap<String, Object> row = new HashMap<String, Object>();
          row.put("INDEX",String.valueOf(index));
          row.put("LEVEL",String.valueOf(level));

          for(int i=1;i<=colCount;i++)
          {
            String name = rsmd.getColumnName(i);
            name = name.toLowerCase();
            row.put(name.trim(),rs_sub.getObject(i));
          }
          index++;
          rows.add(row);
        }

        rs_sub.close();
        pstmt_sub.close();


        String t_where2 = createWhereSQL(false);
        if(t_where2!=null)
          sql2 = "select * from " + sqlTablename + " where " +parentIdName + "="+
          String.valueOf(bas_id[j])+" and " + t_where2 +" order by BAS_ID";
        else
          sql2 = "select * from " + sqlTablename + " where " +parentIdName + "="+
          String.valueOf(bas_id[j])+" order by BAS_ID";
        
        PreparedStatement pstmt_sub2 = con.prepareStatement(sql2);
        
        ResultSet rs_sub2 = pstmt_sub2.executeQuery();

        int childCount;
        //boolean childEof;
        rs_sub2.last();
        childCount = rs_sub2.getRow();
        rs_sub2.beforeFirst();
        int childRowCount=0;
        int bas_id2[] = new int[childCount];

        while(rs_sub2.next())
        {
          bas_id2[childRowCount] = rs_sub2.getInt("BAS_ID");
          childRowCount++;
        }

        rs_sub2.close();
        pstmt_sub2.close();
        if(childCount>0)
        {
          level++;
          showMenuTree(bas_id2,childCount);
          level--;
        }
        net.strong.util.Helper.cleanup(con);
        //pool.releaseConnection(con);
      }
      catch(SQLException e)
      {
        throw new SQLException("show menu tree error:\n" + e.getMessage());
      }
      catch(Exception e1)
      {
        throw new SQLException(e1.getMessage());
      }
    }
  }
}