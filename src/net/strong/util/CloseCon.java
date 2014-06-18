package net.strong.util;

import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * <p>Title:数据库连接关闭类 </p>
 * <p>Description:在编写action或标签等类时，如需要对数据库进行操作，经常会取得数据库连接
 * 后忘记关闭连接，或出现异常而导致数据库连接没有正常关闭就退出程序，由于这些情况的存在，所以
 * 编写了此类，以便在每个类的出口处都调用此类。
 * 由于此类提供的是静态函数，所以可以直接用以下语句：
 * CloseCon.Close(con);//con 为数据库连接对象Connection
 *  </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class CloseCon {
  public static void Close(Connection con)
  {
    org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.
        getLog("CloseCon");
    try
    {
      if(con!=null && !con.isClosed())
        con.close();
      con = null;
    }
    catch(SQLException ee)
    {
      log.debug(ee.getMessage());
    }
    catch(Exception e)
    {
      log.error(e.getMessage());
//      System.out.println("can not close con at CloseCon 1 ");
    }
  }
}
