package net.strong.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.WeakHashMap;

import net.strong.util.Helper;
/**
 * <p>Title: 从数据库中取指定的值</p>
 * <p>Description: 根据给定的参数，从数据库中取出所要的值</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class weakDbSpecifyValueBean {
  private String tableName = null;
  private String valueField = null;
  private String sqlWhere = null;
  private String isCount = "false";
  private String isSum = "false";
  private String sql = null;

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }
  public String getTableName()
  {
    return tableName;
  }
  public void setValueField(String valueField)
  {
    this.valueField = valueField;
  }
  public String getValueField()
  {
    return valueField;
  }
  public void setSqlWhere(String sqlWhere)
  {
    this.sqlWhere = sqlWhere;
  }
  public String getSqlWhere()
  {
    return sqlWhere;
  }
  public void setIsCount(String isCount)
  {
    this.isCount = isCount;
  }
  public String getIsCount()
  {
    return isCount;
  }
  public void setIsSum(String isSum)
  {
    this.isSum = isSum;
  }
  public String getIsSum()
  {
    return isSum;
  }
  public void setSql(String sql)
  {
    this.sql = sql;
  }
  public String getSql()
  {
    return sql;
  }

  public String createSql()
  {
    String temp_sql = null;
    if(sql!=null)
      temp_sql = sql;
    else if(tableName!=null)
    {
      if(isCount!=null && isCount.compareToIgnoreCase("true")==0)
      {
        if(sqlWhere!=null)
          temp_sql = "select count(*) as COUNT from " + tableName + " where " + sqlWhere;
        else
          temp_sql = "select count(*) as COUNT from " + tableName;
      }
      if(valueField!=null)
      {
        if(isSum!=null && isSum.compareToIgnoreCase("true")==0)
        {
          if(sqlWhere!=null)
            temp_sql = "select sum(" + valueField + ") as SUM from " + tableName + " where " +sqlWhere;
          else
            temp_sql = "select sum(" + valueField + ") as SUM from " + tableName;
        }
        else
        {
          if(sqlWhere!=null)
            temp_sql = "select " + valueField + " from " + tableName + " where " + sqlWhere;
          else
            temp_sql = "select " + valueField + " from " + tableName;
        }
      }
    }
    return temp_sql;
  }

  public WeakHashMap<String, Object> getdbValue(Connection con) throws Exception
  {
    WeakHashMap<String, Object> result = null;
    String temp_sql = createSql();
    if(temp_sql == null)
      throw new SQLException("not sql statement");

    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try
    {
		long cost_1 = System.currentTimeMillis();
      pstmt = con.prepareStatement(temp_sql);
      rs = pstmt.executeQuery();
		long cost_2 = System.currentTimeMillis();
		if(cost_2-cost_1 > 1000*4){
			System.out.println("*********************=== PAY ATTENTION:SLOW SQL(Cost:" +(cost_2-cost_1)+ " Millis) ===*********************");
			System.out.println(temp_sql);
			System.out.println("**********************************************END*************************************************");
		}
      if (rs != null) {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        if (rs.next()) {
          result = new WeakHashMap<String, Object>();
          for (int i = 1; i <= colCount; i++) {
            String name = rsmd.getColumnName(i);
            name = name.toLowerCase();
            Object obj_value = rs.getObject(i);
            if (obj_value instanceof java.lang.String) {
              String t_str = null;
              if (obj_value == null)
                t_str = "";
              else
                t_str = String.valueOf(obj_value).trim();
              result.put(name.trim(), t_str);
            }
            else {
              result.put(name.trim(), obj_value);
            }
          }
        }
        rsmd = null;
        Helper.cleanup(pstmt,con,rs);
      }
    }
    catch(SQLException e)
    {
    	Helper.cleanup(con);
      throw new SQLException("database perform error : " + e.getMessage());
    }

    Helper.cleanup(con);
    return result;

  }
/*
  public WeakHashMap getdbValue(String path) throws Exception
  {
    WeakHashMap result = null;
    String temp_sql = createSql();
    if(temp_sql == null)
      throw new SQLException("not sql statement");

//    PoolBean pool = new PoolBean();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    InitDatabase initDb = new InitDatabase(path);
    DatabaseInfo dbInfo = initDb.getDatabaseInfo();

    try
    {
      Class.forName(dbInfo.getDriver());
      con = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUsername(),
                                        dbInfo.getPassword());
      stmt = con.createStatement();
      rs = stmt.executeQuery(temp_sql);
//      rs = stmt.executeQuery(sql);
      if (rs != null) {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        if (rs.next()) {
          result = new WeakHashMap();
          for (int i = 1; i <= colCount; i++) {
            String name = rsmd.getColumnName(i);
            name = name.toLowerCase();
            Object obj_value = rs.getObject(i);
            if (obj_value instanceof java.lang.String) {
              String t_str = null;
              if (obj_value == null)
                t_str = "";
              else
                t_str = String.valueOf(obj_value).trim();
              result.put(name.trim(), t_str);
            }
            else {
              result.put(name.trim(), obj_value);
            }
          }
        }
        rsmd = null;
        rs.close();
        stmt.close();
        con.close();
        rs = null;
        stmt = null;
        con = null;
      }
    }
    catch(SQLException e)
    {
      ProDebug.addDebugLog("database perform error : " + e.getMessage());
      ProDebug.addDebugLog("sql:"+temp_sql);
      ProDebug.saveToFile();
      CloseCon.Close(con);
      throw new SQLException("database perform error : " + e.getMessage());
    }

    CloseCon.Close(con);
    return result;
  }

  public WeakHashMap getdbValue(DatabaseInfo dbInfo) throws Exception
  {
    WeakHashMap result = null;
    String temp_sql = createSql();
    if(temp_sql == null)
      throw new SQLException("not sql statement");

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    try
    {
      Class.forName(dbInfo.getDriver());
      con = DriverManager.getConnection(dbInfo.getUrl(),dbInfo.getUsername(),
                                        dbInfo.getPassword());
      stmt = con.createStatement();
      rs = stmt.executeQuery(temp_sql);
//      rs = stmt.executeQuery(sql);
      ResultSetMetaData rsmd = rs.getMetaData();
      int colCount = rsmd.getColumnCount();
      if(rs.next())
      {
        result = new WeakHashMap();
        for(int i=1;i<=colCount;i++)
        {
          String name = rsmd.getColumnName(i);
          name = name.toLowerCase();
          Object obj_value = rs.getObject(i);
          if(obj_value instanceof java.lang.String)
          {
            String t_str = null;
            if(obj_value == null)
              t_str = "";
            else
              t_str = String.valueOf(obj_value).trim();
            result.put(name.trim(),t_str);
          }
          else
          {
            result.put(name.trim(),obj_value);
          }
        }
      }
      rs.close();
      stmt.close();
      con.close();
    }
    catch(SQLException e)
    {
      ProDebug.addDebugLog("database perform error : " + e.getMessage());
      ProDebug.addDebugLog("sql:"+temp_sql);
      ProDebug.saveToFile();
      CloseCon.Close(con);
      throw new SQLException("database perform error : " + e.getMessage());
    }

    CloseCon.Close(con);
    return result;
  }*/
}
