// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   PooledConnection.java

package qt.utils.DBCP;

import java.sql.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import qt.utils.DBCP.log.BatchLog;

// Referenced classes of package net.snapbug.util.dbtool:
//            PooledStatement

public class PooledConnection
    implements Connection
{

    private Connection FConn;
    private String FStr;
    private long FidleTimeout;
    private long FidleConnTimeout;
    private PooledStatement FplStmt[];
    private int FstatementPoolSize;
    private int FiMark;
    private boolean FBool;
    private long FCreateTime;
    private long FLastActiveTime;
    private int FActiveNumber;
    private int FiPos;
    private BatchLog FBatchLog;
    //private boolean FNeedFree = false; //标记此连接是否需要被回收

  /**public void setNeedFree(boolean FNeedFree) {
    this.FNeedFree = FNeedFree;
  }

  public boolean isNeedFree() {
    return FNeedFree;
  }**/

  public PooledConnection(int AiPos, Connection AConn, String AStr, int AstatementPoolSize, long AidleTimeout, long AidleConnTimeout, BatchLog ABatchLog)
        throws SQLException
    {
        this.FConn = null;
        //FNeedFree = false;
        FiMark = 0;
        FBool = false;
        FLastActiveTime = System.currentTimeMillis();
        FActiveNumber = 0;
        this.FBatchLog = new BatchLog(false);
        this.FConn = AConn;
        this.FStr = AStr;
        this.FidleTimeout = AidleTimeout;
        this.FidleConnTimeout = AidleConnTimeout;

        this.FstatementPoolSize = AstatementPoolSize;
        this.FiPos = AiPos;

        if(ABatchLog != null)
            this.FBatchLog = ABatchLog;
        FplStmt = new PooledStatement[FstatementPoolSize];
        for(int i = 0; i < FplStmt.length; i++)
            FplStmt[i] = null;

        FCreateTime = System.currentTimeMillis();
    }

    public static void booked()
    {
    }

    public int getPosition()
    {
        return FiPos;
    }

    public String toString()
    {
        try
        {
            String s = String.valueOf(String.valueOf((new StringBuffer("[")).append(FiPos).append("] closed=").append(isClosed()).append(", createdTime=").append((new Date(FCreateTime)).toString()).append(", lastActionTime=").append((new Date(FLastActiveTime)).toString()).append(", curPos=").append(FiMark).append(", activeNumber=").append(FActiveNumber).append(", statementPoolSize=").append(FstatementPoolSize)));
            return s;
        }
        catch(SQLException et)
        {
            String s1 = null;
            return s1;
        }
    }

    public boolean isTimeout()
    {
        return System.currentTimeMillis() - FLastActiveTime > FidleConnTimeout * (long)1000;
    }

    public void active()
    {
        FLastActiveTime = System.currentTimeMillis();
    }

    public long getCreatedTime()
    {
        return FCreateTime;
    }

    public int getActiveNumber()
    {
        return FActiveNumber;
    }

    public synchronized int shrink()
        throws Exception
    {
        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : start shrink process."))));
        int AActiveNumber = 0;
        int AiFreeStmt = 0;
        try
        {
            int AiMark = -1;
            for(int i = 0; i < FstatementPoolSize;i++)
            {
                FiMark = FiMark % FstatementPoolSize;
                if(FplStmt[FiMark] == null)
                {
                    if(AiMark < 0)
                        AiMark = FiMark;
                } else
                if(FplStmt[FiMark].isTimeout() || FplStmt[FiMark].isClosed())
                {
                    if(FBatchLog.isDebug())
                        FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : free timeout or closed statement:").append(FplStmt[FiMark].toString()))));
                    try
                    {
//FBatchLog.info("close Statement....." + FiMark);
                        FplStmt[FiMark].close();
//FBatchLog.info("Close Statement..OK________________...");
                    }
                    catch(SQLException sqlexception) { }
                    FplStmt[FiMark] = null;
                    FActiveNumber--;
                    AiFreeStmt++;
                    if(AiMark < 0)
                        AiMark = FiMark;
                } else
                {
                    AActiveNumber++;
                }

                FiMark++;
            }

            if(AiMark >= 0)
            {
                if(FBatchLog.isDebug())
                    FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : reset curPos from ").append(FiMark).append(" to ").append(AiMark))));
                FiMark = AiMark;
            }
        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : has an error in shrink :").append(toString()).append(". error:").append(et.toString()))));
            throw et;
        }
        this.FActiveNumber = AActiveNumber;
        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : has ").append(AiFreeStmt).append(" statement was be Free."))));
        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : successfully shrink, has ").append(FActiveNumber).append(" active statement in pool."))));
        if(FBatchLog.isDebug())
            FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] :").append(toString()))));
        return this.FActiveNumber;
    }

    public synchronized int searchFreeCell()
    {   int AiMark = -1;
        for(int i = 0; i < FstatementPoolSize;i++)
        {
            FiMark = FiMark % FstatementPoolSize;
            if(FplStmt[FiMark] == null) {
              AiMark = FiMark;
              //FiMark++;
              return AiMark;
            }
            if(FplStmt[FiMark].isClosed())
            {
                FplStmt[FiMark] = null;
                FActiveNumber--;
                AiMark = FiMark;
                //FiMark++;
                return AiMark;
            }

            if(FplStmt[FiMark].isTimeout())
            {
                if(!FplStmt[FiMark].isClosed())
                    try
                    {
                        if(FBatchLog.isDebug())
                            FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : free timeout statement:").append(FplStmt[FiMark].toString()))));
                        FplStmt[FiMark].close();
                    }
                    catch(SQLException sqlexception) { }
                FplStmt[FiMark] = null;
                FActiveNumber--;
                AiMark = FiMark;
                //FiMark++;
                return AiMark;
            }

            FiMark++;
        }

        FBatchLog.warning(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : connection pool is fulled with active statement."))));
        if(FBatchLog.isDebug())
            FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] :").append(toString()))));
        return -1;
    }

    public static void main(String args[])
    {
    }

    private void CloseAllStmt()
    {
        for(int i = 0; i < FstatementPoolSize; i++)
        {
            if(FplStmt[i] == null)
                continue;
            try
            {
                FplStmt[i].close();
            }
            catch(SQLException sqlexception) { }
            FplStmt[i] = null;
        }
        FActiveNumber = 0;

    }

    private PooledStatement CreateOnePooledStmt(Statement AStmt)
        throws SQLException
    {
        active();
        PooledStatement plStmt = null;
        int AiMark = 0;
        try
        {
            AiMark = searchFreeCell();
            if(AiMark < 0)
            {
                AStmt.close();
                return null;
            }
            plStmt = new PooledStatement(AiMark, FStr, this, AStmt, FidleTimeout);
        }
        catch(SQLException et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : has error in create statement:").append(et.toString()))));
            if(FBatchLog.isDebug())
                FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : ").append(toString()))));
            AStmt.close();
            throw et;
        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : has error in create statement:").append(et.toString()))));
            if(FBatchLog.isDebug())
                FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : ").append(toString()))));
            AStmt.close();
            throw new SQLException("unknow error:".concat(String.valueOf(String.valueOf(et.getMessage()))));
        }
        FplStmt[AiMark] = plStmt;
        FActiveNumber++;
        if(FBatchLog.isDebug())
            FBatchLog.debug(String.valueOf(String.valueOf((new StringBuffer("conn[")).append(FStr).append("-").append(FiPos).append("] : create one statement:").append(FplStmt[AiMark].toString()))));
        return plStmt;
    }

    public Statement createStatement()
        throws SQLException
    {
        return CreateOnePooledStmt(FConn.createStatement());
    }

    public Statement createStatement(int resultSetType,
                                 int resultSetConcurrency,
                                 int resultSetHoldability)
                          throws SQLException
    {
       return CreateOnePooledStmt(FConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
    }
    public PreparedStatement prepareStatement(String sSql)
        throws SQLException
    {
        return CreateOnePooledStmt(FConn.prepareStatement(sSql));
    }

    public PreparedStatement prepareStatement(String sql,
                                              String[] columnNames)
                                       throws SQLException
   {
        return CreateOnePooledStmt(FConn.prepareStatement(sql, columnNames));
   }

   public PreparedStatement prepareStatement(String sql,
                                          int[] columnIndexes)
                                   throws SQLException
   {
       return CreateOnePooledStmt(FConn.prepareStatement(sql, columnIndexes));
   }

   public PreparedStatement prepareStatement(String sql,
                                          int autoGeneratedKeys)
                                   throws SQLException
   {
       return CreateOnePooledStmt(FConn.prepareStatement(sql, autoGeneratedKeys));
   }

   public PreparedStatement prepareStatement(String sql,
                                          int resultSetType,
                                          int resultSetConcurrency,
                                          int resultSetHoldability)
                                   throws SQLException
   {
       return CreateOnePooledStmt(FConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
   }

    public CallableStatement prepareCall(String sSql)
        throws SQLException
    {
        PreparedStatement AResult = CreateOnePooledStmt(FConn.prepareCall(sSql));
        return (CallableStatement)AResult;
    }


    public CallableStatement prepareCall(String sql,
                                    int resultSetType,
                                    int resultSetConcurrency,
                                    int resultSetHoldability)
                             throws SQLException
   {
       PreparedStatement AResult = CreateOnePooledStmt(FConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
       return (CallableStatement)AResult;
   }


    public Statement createStatement(int sSql, int resultSetType)
        throws SQLException
    {
        return CreateOnePooledStmt(FConn.createStatement(sSql, resultSetType));
    }

    public PreparedStatement prepareStatement(String sSql, int resultSetType, int resultSetConcurrency)
        throws SQLException
    {
        return CreateOnePooledStmt(FConn.prepareStatement(sSql, resultSetType, resultSetConcurrency));
    }

    public CallableStatement prepareCall(String sSql, int resultSetType, int resultSetConcurrency)
        throws SQLException
    {
        PreparedStatement AResult = CreateOnePooledStmt(FConn.prepareCall(sSql, resultSetType, resultSetConcurrency));
        return (CallableStatement)AResult;
    }

    public String nativeSQL(String sSql)
        throws SQLException
    {
        return FConn.nativeSQL(sSql);
    }

    public void setAutoCommit(boolean ABool)
        throws SQLException
    {
        FConn.setAutoCommit(ABool);
    }

    public boolean getAutoCommit()
        throws SQLException
    {
        return FConn.getAutoCommit();
    }

    public void commit()
        throws SQLException
    {
        active();
        FConn.commit();
    }

    public void rollback()
        throws SQLException
    {
        FConn.rollback();
    }
    public void rollback(Savepoint savepoint)
              throws SQLException
    {
        FConn.rollback(savepoint);
    }

    public void releaseSavepoint(Savepoint savepoint)
                      throws SQLException
    {
       FConn.releaseSavepoint(savepoint);
    }

    public void ReallyClose()
        throws SQLException
    {

        CloseAllStmt();
        if(!isClosed())
        {
            FConn.close();
       }
        FConn = null;
    }

    public void close() {
        try {
            if (isClosed()) {
                return;
            }
            else {
                CloseAllStmt();
                active();
                return;
            }
        }catch(Exception et) {

        }

    }

    public boolean isClosed()
        throws SQLException
    {
        return (FConn == null || FConn.isClosed());
        //return FConn == null;
    }

    public DatabaseMetaData getMetaData()
        throws SQLException
    {
        return FConn.getMetaData();
    }

    public void setReadOnly(boolean ABool)
        throws SQLException
    {
        FConn.setReadOnly(ABool);
    }

    public boolean isReadOnly()
        throws SQLException
    {
        return FConn.isReadOnly();
    }

    public void setCatalog(String AStr)
        throws SQLException
    {
        FConn.setCatalog(AStr);
    }

    public String getCatalog()
        throws SQLException
    {
        return FConn.getCatalog();
    }

    public void setTransactionIsolation(int Aint)
        throws SQLException
    {
        FConn.setTransactionIsolation(Aint);
    }

    public int getTransactionIsolation()
        throws SQLException
    {
        return FConn.getTransactionIsolation();
    }

    public SQLWarning getWarnings()
        throws SQLException
    {
        return FConn.getWarnings();
    }

    public int getHoldability()
                   throws SQLException
    {
       return FConn.getHoldability();
    }

    public void setHoldability(int holdability)
                    throws SQLException
    {
       FConn.setHoldability(holdability);
    }

    public void clearWarnings()
        throws SQLException
    {
        FConn.clearWarnings();
    }

    public Map getTypeMap()
        throws SQLException
    {
        return FConn.getTypeMap();
    }


    public Savepoint setSavepoint(String name)
                       throws SQLException
    {
       return FConn.setSavepoint(name);
    }

    public Savepoint setSavepoint()
                       throws SQLException
    {
       return FConn.setSavepoint();
    }

	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		// TODO 自动生成方法存根
		
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



}
