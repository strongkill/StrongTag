// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   ConnectionPool.java

package qt.utils.DBCP;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import qt.utils.DBCP.log.BatchLog;

// Referenced classes of package net.snapbug.util.dbtool:
//            PooledConnection, PoolFulledException

public class ConnectionPool extends Thread
{

    private boolean FBool;
    private String FsPoolName;
    private long FidleTimeout;
    private PooledConnection FarrPools[];
    private int FconnectionPoolSize;
    private int FminConnectionPoolSize;
    private int FstatementPoolSize;
    private int FiMark;
    private boolean FBool2;
    private int FshrinkInterval;
    private long FcreatedTime;
    private long FlastShrinkTime;
    private String FStateStr;
    private BatchLog FBatchLog;
    private int FactiveConnectionNumber;
    private String FdriverClass;
    private String FdbUrl;
    private String FdbUser;
    private String FdbPass;
    private long FidleConnTimeout;

    protected void resetProfile(String AsPoolName, String AdriverClass, String AdbUrl, String AdbUser, String AdbPass, int AminConnectionPoolSize, int AconnectionPoolSize, int AstatementPoolSize,
            int AidleTimeout, int AidleConnTimeout, int AshrinkInterval)
        throws Exception
    {
        this.close();
        init(AsPoolName, AdriverClass, AdbUrl, AdbUser, AdbPass, AminConnectionPoolSize,
             AconnectionPoolSize, AstatementPoolSize, AidleTimeout, AidleConnTimeout, AshrinkInterval, FBatchLog);
    }

    protected ConnectionPool()
    {
        FBool = false;
        FidleTimeout = 60L;
        FidleConnTimeout = 120L;
        FminConnectionPoolSize = 0;
        FconnectionPoolSize = 10;
        FstatementPoolSize = 100;
        FiMark = 0;
        FBool2 = false;
        FshrinkInterval = 70;
        FcreatedTime = System.currentTimeMillis();
        FlastShrinkTime = System.currentTimeMillis();
        FStateStr = "no active";
        FBatchLog = new BatchLog(false);
        FactiveConnectionNumber = 0;
        setDaemon(true);
    }

    private void init(String AsPoolName, String AdriverClass, String AdbUrl, String AdbUser, String AdbPass, int AminConnectionPoolSize, int AconnectionPoolSize, int AstatementPoolSize,
            int AidleTimeout, int AidleConnTimeout, int AshrinkInterval, BatchLog ABatchLog)
        throws Exception
    {
        FBool = false;
        this.FidleTimeout = 60L;
        this.FidleConnTimeout = 120L;
        this.FminConnectionPoolSize = 0;
        this.FconnectionPoolSize = 10;
        this.FstatementPoolSize = 100;
        FiMark = 0;
        FBool2 = false;
        this.FshrinkInterval = 70;
        FcreatedTime = System.currentTimeMillis();
        FlastShrinkTime = System.currentTimeMillis();
        FStateStr = "no active";
        this.FBatchLog = new BatchLog(false);
        FactiveConnectionNumber = 0;
        this.FsPoolName = AsPoolName;
        this.FdriverClass = AdriverClass;
        this.FdbUrl = AdbUrl;
        this.FdbUser = AdbUser;
        this.FdbPass = AdbPass;
        if(AidleTimeout > 0)
            this.FidleTimeout = AidleTimeout;
        if(AconnectionPoolSize > 0)
            this.FconnectionPoolSize = AconnectionPoolSize;
        if(AminConnectionPoolSize>FconnectionPoolSize)
            AminConnectionPoolSize = FconnectionPoolSize;

        if(AminConnectionPoolSize>0)
            this.FminConnectionPoolSize = AminConnectionPoolSize;

        if(AstatementPoolSize > 0)
            this.FstatementPoolSize = AstatementPoolSize;
        if(AshrinkInterval > 0)
            this.FshrinkInterval = AshrinkInterval;
        if(AidleConnTimeout >0)
            this.FidleConnTimeout = AidleConnTimeout;

        if(FBatchLog != null)
            this.FBatchLog = ABatchLog;
        if(FsPoolName == null)
            throw new Exception("poolName is NULL");
        if(FdriverClass == null)
            throw new Exception("driverClass is NULL");
        if(FdbUrl == null)
            throw new Exception("dbUrl is NULL");
        if(FdbUser == null)
            throw new Exception("dbUser is NULL");
        if(FdbPass == null)
            throw new Exception("dbPass is NULL");
        FarrPools = new PooledConnection[FconnectionPoolSize];
        for(int i = 0; i < FarrPools.length; i++)
            FarrPools[i] = null;

        Class.forName(FdriverClass).newInstance();

        for(int i=0;i<FminConnectionPoolSize;i++) {
            FarrPools[i] = CreatePooledConnection();

        }
        setDaemon(true);
    }


    public ConnectionPool(String AsPoolName, String AdriverClass, String AdbUrl, String AdbUser, String AdbPass, int AminConnectionPoolSize, int AconnectionPoolSize, int AstatementPoolSize,
            int AidleTimeout, int AidleConnTimeout, int AshrinkInterval, BatchLog ABatchLog)
        throws Exception
    {
        init(AsPoolName, AdriverClass, AdbUrl, AdbUser, AdbPass, AminConnectionPoolSize,
             AconnectionPoolSize, AstatementPoolSize, AidleTimeout, AidleConnTimeout, AshrinkInterval, ABatchLog);
    }

    public void booked()
    {
    }

    public void close()
    {
        for(int i=0;i<FconnectionPoolSize; i++) {
            if(FarrPools[i]!=null) {
                FreeConnect(i);
            }
            FarrPools[i] = null;
        }
        FiMark = 0;
    }

    public boolean isAutoShrink()
    {
        return FshrinkInterval > 0;
    }

    public String getPoolName()
    {
        return FsPoolName;
    }

    public String getDriverName()
    {
        return FdriverClass;
    }

    public String getDbUrl()
    {
        return FdbUrl;
    }

    public String toString()
    {
        StringBuffer ABuffer = (new StringBuffer(FsPoolName)).append(" { ");
        ABuffer.append("driverClass=").append(FdriverClass).append(", ");
        ABuffer.append("dbUrl=").append(FdbUrl).append(", ");
        ABuffer.append("createdTime=").append((new Date(FcreatedTime)).toString()).append(", ");
        ABuffer.append("lastShrinkTime=").append((new Date(FlastShrinkTime)).toString()).append(", ");
        ABuffer.append("curPos=").append(FiMark).append(", ");
        ABuffer.append("connectionPoolSize=").append(FconnectionPoolSize).append(", ");
        ABuffer.append("activeConnectionNumber=").append(FactiveConnectionNumber).append(", ");
        ABuffer.append(".\n");
        ABuffer.append("build connection:").append(FStateStr).append(".");
        for(int i = 0; i < FconnectionPoolSize; i++)
            if(FarrPools[i] != null)
                ABuffer.append("\n\t").append(FarrPools[i].toString());

        ABuffer.append("\n}");
        return ABuffer.toString();
    }

    public static void main(String args[])
    {
    }

    public void run()
    {
        if(FBool)
            return;
        FBool = true;
        do
            try
            {
                Thread.sleep(FshrinkInterval * 1000);
                Shrink();  //不在线程中检查资源
                //LogInfo();
            }
            catch(Exception et)
            {
                et.printStackTrace();
            }
        while(true);
    }

    private int getConnectionNum() throws Exception {
        int ANum = 0;
        try
        {
            for(int i = 0; i < FconnectionPoolSize; i++)
            {
                /**if(FarrPools[i] != null)
                    if(FarrPools[i].isClosed())
                    {
                        try
                        {
                            if(FBatchLog.isDebug())
                                FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is closed connection:").append(FarrPools[FiMark].toString()))));
                            FarrPools[i].ReallyClose();
                            FarrPools[i] = null;
                        }
                        catch(SQLException et)
                        {
                            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in closing connection:").append(FarrPools[FiMark].toString()).append(". error:").append(et.toString()))));
                        }
                    } else
                    {
                        ANum++;
                    }
                **/
               if(FarrPools[i] != null)
                 ANum++;
                 /**if(FarrPools[i].isClosed()==true) {
                   FarrPools[i].ReallyClose();
                   FarrPools[i] = null;
                 }else
                   ANum++;**/
            }

        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has an error in getConnectNum :").append(toString()).append(". error:").append(et.toString()))));
            throw et;
        }
        return ANum;

    }
    private void LogInfo() {
      int ANum = 0;
      try
      {
          for(int i = 0; i < FconnectionPoolSize; i++)
          {
              if(FarrPools[i] != null)
              {
                      int iAct = FarrPools[i].getActiveNumber();
                      FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(i).append("] : has statement:").append(iAct))));
                      ANum++;
              }
          }

      }
      catch(Exception et)
      {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has an error in getConnectNum :").append(toString()).append(". error:").append(et.toString()))));

      }

      FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has connection:").append(ANum))));

    }


   //释放指定下标的连接连接
   private synchronized void FreeConnect(int iMark) {
     if(iMark<0 || iMark>=FconnectionPoolSize)
       return;

     try {
       FarrPools[iMark].ReallyClose();
       FarrPools[iMark] = null;
       FactiveConnectionNumber--;
     }catch (SQLException et) {
       FarrPools[iMark] = null;
       FactiveConnectionNumber--;
       FBatchLog.error(String.valueOf(String.valueOf( (new
           StringBuffer("pool[")).append(FsPoolName).
           append(
                 "] : ignored an error in closing connection:").
                  append(FarrPools[iMark].toString()).append(
                  ". error:").append(et.toString()))));
    }

   }

    private synchronized int Shrink()
        throws Exception
    {
        long lStartTime = System.currentTimeMillis();

        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : start shrink process..."))));
        if(FBatchLog.isDebug())
            FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ").append(toString()))));
        int AactiveConnectionNumber = 0;
        long ACreateTime = 0L;
        int AiMark = 0;
        int AiFreeCon = 0;
        int AictNum = getConnectionNum();
        try
        {
            FlastShrinkTime = System.currentTimeMillis();
            for(int i = 0; i < FconnectionPoolSize; i++)
            {
                FiMark = FiMark % FconnectionPoolSize;
                if(FarrPools[FiMark] != null) {
                    if (FarrPools[FiMark].isTimeout() || FarrPools[FiMark].isClosed()) {
                        FBatchLog.info(String.valueOf(String.valueOf( (new
                                    StringBuffer("pool[")).append(FsPoolName).
                                    append("][idleConnTimeout:").append(FidleConnTimeout).
                                    append("]: free timeout connection:").
                                    append(FarrPools[FiMark].toString()))));
                        FreeConnect(FiMark);
                        AictNum--;
                        AiFreeCon++;
                  }
                  if(FarrPools[FiMark] != null) {
                    try {
                       FBatchLog.info("String shrink statement....");
                       FarrPools[FiMark].shrink();
                    }
                    catch (Exception et) {
                        FBatchLog.error(String.valueOf(String.valueOf( (new
                            StringBuffer("pool[")).append(FsPoolName).
                            append(
                            "] : ignored an error in shrink connection:").
                            append(FarrPools[FiMark].toString()).append(
                            ". error:").append(et.toString()))));
                    }
                    if (FarrPools[FiMark].getCreatedTime() > ACreateTime) {
                        ACreateTime = FarrPools[FiMark].getCreatedTime();
                        AiMark = FiMark;
                    }
                        AactiveConnectionNumber++;
                  }
              }

              FiMark++;
            }

        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has an error in shrink :").append(toString()).append(". error:").append(et.toString()))));
            throw et;
        }
        FiMark = AiMark;

        if(FBatchLog.isDebug())
            FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] :").append(toString()))));
        /**
        if(AactiveConnectionNumber<FminConnectionPoolSize)
          for(int i=AactiveConnectionNumber; i<FminConnectionPoolSize; i++) {
            FiMark = FiMark % FconnectionPoolSize;
            if(FarrPools[FiMark]==null || FarrPools[FiMark].isClosed()) {
              FarrPools[FiMark] = CreatePooledConnection();
              AactiveConnectionNumber++;
            }
            FiMark++;
          }
        **/

        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has ").append(AiFreeCon).append(" connection was by Free."))));
        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : successfully shrink, has ").append(AactiveConnectionNumber).append(" active connection in pool."))));
        long lDoItTime = System.currentTimeMillis() - lStartTime;
        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] :  shrink time: ").append(lDoItTime))));
        this.FactiveConnectionNumber = AactiveConnectionNumber;
        return AactiveConnectionNumber;
    }

    private PooledConnection CreatePooledConnection()
        throws SQLException
    {
        FStateStr = "no error";
        try
        {
            PooledConnection pooledconnection = new PooledConnection(FiMark, DriverManager.getConnection(FdbUrl, FdbUser, FdbPass), FsPoolName, FstatementPoolSize, FidleTimeout, FidleConnTimeout, FBatchLog);
            FactiveConnectionNumber++;
            return pooledconnection;
        }
        catch(SQLException et)
        {
            FStateStr = et.toString();
            throw et;
        }
    }

    private int getActiveBefore(int AiMark) {
      int iLen = 0;
      if(AiMark > FconnectionPoolSize)
        iLen = FconnectionPoolSize;
      if(AiMark<0)
        iLen = 0;

      int iCount = 0;
      for (int i = 0; i < iLen; i++) {
        if (FarrPools[i] == null)
          continue;
        //if(FarrPools[i].isNeedFree())
        //  continue;

        iCount++;
        /*try {
          if (FarrPools[i].isClosed()==true){
            FarrPools[i].ReallyClose();
            FarrPools[i] = null;
          }else
            iCount++;
        }catch(Exception et) {}
       */
      }
      return iCount;
    }

    public Statement createStatement()
        throws PoolFulledException, SQLException
    {
        Statement AStmt = null;
        PooledConnection conn = getActiveConnection();
        if(conn==null) { //如果没有取到连接
          try{
            this.Shrink(); //检查资源
          }catch(Exception et) {}
          conn = getActiveConnection();
        }
        if(conn==null) {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
          throw new PoolFulledException("the database connection is fulled.");
        }

        try
        {
           AStmt = conn.createStatement();
        }catch(Exception et)
        {
           AStmt = null;
           FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in createStatement2 connection  ").append(et.toString()))));
        }

        return AStmt;

    }

    public PreparedStatement prepareStatement(String sSql)
        throws PoolFulledException, SQLException
    {
        Statement AStmt = null;

        PooledConnection conn = getActiveConnection();
        if(conn==null) { //如果没有取到连接
          try{
            this.Shrink(); //检查资源
          }catch(Exception et) {}
          conn = getActiveConnection();
        }
        if(conn==null) {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
          throw new PoolFulledException("the database connection is fulled.");
        }

        try
        {
           AStmt = conn.prepareStatement(sSql);
        }catch(Exception et)
        {
           AStmt = null;
           FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in createStatement2 connection  ").append(et.toString()))));
        }

        return (PreparedStatement)AStmt;



    }

    public CallableStatement prepareCall(String sSql)
        throws PoolFulledException, SQLException
    {
        Statement AStmt = null;

        PooledConnection conn = getActiveConnection();
        if(conn==null) { //如果没有取到连接
          try{
            this.Shrink(); //检查资源
          }catch(Exception et) {}
          conn = getActiveConnection();
        }
        if(conn==null) {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
          throw new PoolFulledException("the database connection is fulled.");
        }

        try
        {
           AStmt = conn.prepareCall(sSql);
        }catch(Exception et)
        {
           AStmt = null;
           FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in createStatement2 connection  ").append(et.toString()))));
        }

        return (CallableStatement)AStmt;
    }

    //取得一空闲的空间
    private int searchfreecell() {
      int AiMark = -1;
      for(int i = 0; i < FconnectionPoolSize;i++)
      {
        if(FarrPools[i]==null){
          AiMark = i;
          break;
        }
        try {
          if (FarrPools[i].isTimeout() || FarrPools[i].isClosed()) {

            FBatchLog.info(String.valueOf(String.valueOf( (new
                StringBuffer("pool[")).append(FsPoolName).
                append("][idleConnTimeout:").append(FidleConnTimeout).
                append("]: free timeout connection:").
                append(FarrPools[i].toString()))));

            FreeConnect(i);
            AiMark = i;
            break;
          }
        }catch(Exception et) {

        }


      }
      return AiMark;

    }

    //取得一个有效的连接
    private synchronized PooledConnection getActiveConnection()
        throws PoolFulledException, SQLException
    {
        int tmpID = 0;
        PooledConnection AConn = null;
        int AiMark = -1;
        try
        {
            for(int i = 0; i < FconnectionPoolSize;i++)
            {
                FiMark = FiMark % FconnectionPoolSize;
                if(FarrPools[FiMark] == null)
                {
                    if(AiMark < 0)
                        AiMark = FiMark;
                    FiMark++;
                    continue;
                }

                /**if(FarrPools[FiMark].isNeedFree()) { //如果此连接不可用
                  FiMark++;
                  continue;
                }**/


                /**if(System.currentTimeMillis()-FarrPools[FiMark].getCreatedTime()>1800000) { //如果此连接使用超过1800000秒，则不用了
                  AiMark = this.searchfreecell();
                  if(AiMark>=0) {//如果有空闲资源
                    FarrPools[AiMark] = this.CreatePooledConnection();
                    AConn = FarrPools[AiMark];
                    FarrPools[FiMark].setNeedFree(true); //设置原来连接池不可用

                    FiMark = AiMark;
                    FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : create now connection replace old"))));
                    break;
                  }
                }**/

                //if(AConn==null) {  //如果还没有找到
                  if(FarrPools[FiMark].isClosed()) {
                    FreeConnect(FiMark);
                    if(AiMark < 0)
                        AiMark = FiMark;
                    FiMark++;
                    continue;

                  }

                  if(FarrPools[FiMark].searchFreeCell() >= 0)
                  {
                      AConn = FarrPools[FiMark];
                      tmpID = FiMark;
                      if(getActiveBefore(FiMark)>=FminConnectionPoolSize)
                        FiMark = 0;
                      else
                        FiMark++;
                      break;
                  }
                //}

                FiMark++;
            }

            if(AConn != null) {  //如果找到了一个可用的连接
              if(this.FactiveConnectionNumber<this.FminConnectionPoolSize) {//如果连接数不够
                int itmpMark = -1;
                //查找一个空的空间

                itmpMark = this.searchfreecell();
                if(itmpMark>=0) {  //如果找到了可用的空间

                  FarrPools[itmpMark] = this.CreatePooledConnection();
                  tmpID = itmpMark;
                  AConn = FarrPools[itmpMark];

                  FiMark = itmpMark;
                }
              }
            }

            if(AConn != null)
            {
              AConn.active();

              return AConn;
            }

            if(AiMark < 0)
            {
                FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
                return null;
            }

            FiMark = AiMark;

            FarrPools[AiMark] = CreatePooledConnection();
            if (FBatchLog.isDebug())
              FBatchLog.debug(String.valueOf(String.valueOf( (new StringBuffer(
                    "pool[")).append(FsPoolName).append(
                    "] : create new pooled connection:").append(FarrPools[FiMark].
                    toString()))));
            AConn = FarrPools[AiMark];

              return AConn;
        }
        catch(SQLException et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has error in getConnection:").append(toString()).append(". error:").append(et.toString()))));
            throw et;
        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : has error in getConnection:").append(toString()).append(". error:").append(et.toString()))));
            throw new SQLException("unknow error:".concat(String.valueOf(String.valueOf(et.toString()))));
        }
    }


    public Connection getConnection()
        throws PoolFulledException, SQLException
    {
      Connection conn = getActiveConnection();
      if(conn==null) { //如果没有取到连接
        try{
          this.Shrink(); //检查资源
        }catch(Exception et) {}
        conn = getActiveConnection();
      }
      if(conn==null) {
        FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
        throw new PoolFulledException("the database connection is fulled.");
      }
      return conn;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws PoolFulledException, SQLException
    {
        Statement AStmt = null;
        PooledConnection conn = getActiveConnection();
        if(conn==null) { //如果没有取到连接
          try{
            this.Shrink(); //检查资源
          }catch(Exception et) {}
          conn = getActiveConnection();
        }
        if(conn==null) {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
          throw new PoolFulledException("the database connection is fulled.");
        }

        try
        {
           AStmt = conn.createStatement(resultSetType, resultSetConcurrency);
        }catch(Exception et)
        {
           AStmt = null;
           FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in createStatement2 connection  ").append(et.toString()))));
        }

        return AStmt;
    }



    public PreparedStatement prepareStatement(String sSql, int resultSetType, int resultSetConcurrency)
        throws PoolFulledException, SQLException
    {
        Statement AStmt = null;

        PooledConnection conn = getActiveConnection();
        if(conn==null) { //如果没有取到连接
          try{
            this.Shrink(); //检查资源
          }catch(Exception et) {}
          conn = getActiveConnection();
        }
        if(conn==null) {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
          throw new PoolFulledException("the database connection is fulled.");
        }

        try
        {
           AStmt = conn.prepareStatement(sSql, resultSetType, resultSetConcurrency);
        }catch(Exception et)
        {
           AStmt = null;
           FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in createStatement2 connection  ").append(et.toString()))));
        }

        return (PreparedStatement)AStmt;
    }

    public CallableStatement prepareCall(String sSql, int resultSetType, int resultSetConcurrency)
        throws PoolFulledException, SQLException
    {
        Statement AStmt = null;

        PooledConnection conn = getActiveConnection();
        if(conn==null) { //如果没有取到连接
          try{
            this.Shrink(); //检查资源
          }catch(Exception et) {}
          conn = getActiveConnection();
        }
        if(conn==null) {
          FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : is fulled:").append(toString()))));
          throw new PoolFulledException("the database connection is fulled.");
        }

        try
        {
           AStmt = conn.prepareCall(sSql, resultSetType, resultSetConcurrency);
        }catch(Exception et)
        {
           AStmt = null;
           FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("pool[")).append(FsPoolName).append("] : ignored an error in createStatement2 connection  ").append(et.toString()))));
        }

        return (CallableStatement)AStmt;
    }
}
