package net.strong.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.strong.util.CloseCon;
/**
 * <p>Title:从数据库中取指定的值 </p>
 * <p>Description:根据给定的参数，从数据库中取出所要的值，将其输出或
 * 存入pageContext对象中 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class newDbSpecifyValueBean {
  private String tableName = null;
  private String valueField = null;
  private String sqlWhere = null;
  private String isCount = "false";
  private String isSum = "false";
  private String sql = null;
  private Connection con = null;

  public Connection getCon()
  {
    return this.con;
  }
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

  public ResultSet getdbValue(Connection con) throws Exception
  {
    this.con = con;

    String temp_sql = createSql();
    if(temp_sql == null)
      throw new SQLException("not sql statement");

    Statement stmt = null;
    ResultSet rs = null;

    try
    {

      stmt = con.createStatement();
      rs = stmt.executeQuery(temp_sql);

    }
    catch(SQLException e)
    {
/*      ProDebug.addDebugLog("database perform error : " + e.getMessage());
      ProDebug.addDebugLog("sql:"+temp_sql);
      ProDebug.saveToFile();*/
      CloseCon.Close(con);
      throw new SQLException("database perform error : " + e.getMessage());
    }

    return rs;

  }

/*
  public ResultSet getdbValue(String path) throws Exception
  {
    String temp_sql = createSql();
    if(temp_sql == null)
      throw new SQLException("not sql statement");

//    PoolBean pool = new PoolBean();
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

    }
    catch(SQLException e)
    {
      ProDebug.addDebugLog("database perform error : " + e.getMessage());
      ProDebug.addDebugLog("sql:"+temp_sql);
      ProDebug.saveToFile();
      CloseCon.Close(con);
      throw new SQLException("database perform error : " + e.getMessage());
    }

    dbInfo = null;
    initDb = null;
    return rs;
  }

  public ResultSet getdbValue(DatabaseInfo dbInfo) throws Exception
  {
    String temp_sql = createSql();
    if(temp_sql == null)
      throw new SQLException("not sql statement");

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
    }
    catch(SQLException e)
    {
      ProDebug.addDebugLog("database perform error : " + e.getMessage());
      ProDebug.addDebugLog("sql:"+temp_sql);
      ProDebug.saveToFile();
      CloseCon.Close(con);
      throw new SQLException("database perform error : " + e.getMessage());
    }

    return rs;
  }
*/
}