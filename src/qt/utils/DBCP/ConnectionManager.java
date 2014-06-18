// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   ConnectionManager.java

package qt.utils.DBCP;

import java.util.Hashtable;

import qt.utils.DBCP.log.BatchLog;
import qt.utils.DBCP.profile.ProfileItem;
import qt.utils.DBCP.profile.ProfileReader;

// Referenced classes of package net.snapbug.util.dbtool:
//            ConnectionPool, NoinitialException

public final class ConnectionManager
{

    private static volatile Hashtable FHashtable; //全局变量，保存每一个连接池
    private static int FiUserCount; //全局变量，保存FHashtable的使用个数
    private static volatile BatchLog FBatchLog = new BatchLog(true);
    private static String FConfigFile = "defaultPool.conf";
    private static int FiMark = 0;
    private static String FsApplicationStr = "DBCP_ConnectionManager";
    public static final String version = "v1.06 1231";


   /** public static ConnectionManager getConnectionManager(ServletContext aApplication, String AFile) {
        Object oResult = null;
        ConnectionManager AConnM = null;
        oResult = aApplication.getAttribute(FsApplicationStr);
        if(oResult==null) {
            AConnM = new ConnectionManager(AFile);
            aApplication.setAttribute(FsApplicationStr, AConnM);
        }else {
            AConnM = (ConnectionManager)oResult;
            if ( AConnM.isActive() == false) {
                try {
                    AConnM.init(AFile);
                }
                catch (Exception et) {
                    return null;
                }
            }
            if(AFile.equals(AConnM.getConfigFile())==false) {
                AConnM.close();
                try {
                    AConnM.init(AFile);
                }
                catch (Exception et) {
                    return null;
                }
            }
        }
        return AConnM;
    }**/

    public static String getConfigFile() {
        return FConfigFile;
    }

   public static boolean isActive() {
        if(FiMark==0)
            return false;
        else
            return true;
    }

    public static void debug(boolean Abl)
    {
        if(FBatchLog == null)
        {
            return;
        } else
        {
            FBatchLog.setDebug(Abl);
            return;
        }
    }

     protected ConnectionManager() { };

    public static synchronized void init(String AFile)
        throws Exception
    {
        if(FiMark != 0)
        {
            if(FiMark == 1)
                FBatchLog.warning("ConnectionManager is self-inited from defaultPool.conf");
            else
                FBatchLog.warning("ConnectionManager is inited by others process");
            return;
        }
        ProfileReader AProfile = new ProfileReader(AFile);
        AProfile.loadProfile();
        ProfileItem AItem = AProfile.findItem("logger");
        if(AItem != null)
        {
            String AStr = ProfileReader.getStringProfile(AItem, "logDir", "");
//            System.out.println(AStr);
            if(AStr.length() > 0)
            {
                FBatchLog = new BatchLog(AStr);
                FBatchLog.setDebug(ProfileReader.getBooleanProfile(AItem, "debug", false));
                FBatchLog.setWriteLogType(ProfileReader.getStringProfile(AItem, "WriteLogType", "sysdg,debug,warn,error,fatal"));
            }
        }


        if(FHashtable==null) {
          FHashtable = new Hashtable(10);
          FBatchLog.info("Create Hashtable");
        }

        PoolsFromConfig(AProfile, AFile);
        FConfigFile = AFile;
        FiMark = 2;
    }

    public static synchronized void init(String AFile, BatchLog ALog)
        throws Exception
    {
        if(FiMark != 0)
        {
            if(FiMark == 1)
                FBatchLog.warning("ConnectionManager is self-inited from defaultPool.conf");
            else
                FBatchLog.warning("ConnectionManager is inited by others process");
            return;
        }

        if(ALog != null)
            FBatchLog = ALog;
        ProfileReader pfReader = new ProfileReader(AFile);
        pfReader.loadProfile();

        if(FHashtable==null) {
          FHashtable = new Hashtable(10);
          FBatchLog.info("Create Hashtable");
        }

        PoolsFromConfig(pfReader, AFile);
        FConfigFile = AFile;
        FiMark = 2;
    }

    public static void reloadProfile()
        throws Exception
    {
        FBatchLog.info("[ConnectionManager] reload profile:".concat(String.valueOf(String.valueOf(FConfigFile))));
        ProfileReader pfReader = new ProfileReader(FConfigFile);
        pfReader.loadProfile();
        ProfileItem pfItem = pfReader.findItem("DatabaseProfile");
        if(pfItem == null)
            throw new Exception("can NOT find profile item 'DatabaseProfile' in file:".concat(String.valueOf(String.valueOf(FConfigFile))));
        for(ProfileItem tmppfItem = pfItem.scanFirstItem(); tmppfItem != null; tmppfItem = pfItem.scanNextItem())
        {
            ConnectionPool ctPool = (ConnectionPool)FHashtable.get(tmppfItem.getName().trim());
            if(ctPool == null)
            {
                if(FBatchLog.isDebug())
                    FBatchLog.debug("[ConnectionManager] add new connection pool:".concat(String.valueOf(String.valueOf(tmppfItem.getName()))));
                CreateOnePool(tmppfItem);
                continue;
            }
            if(FBatchLog.isDebug())
                FBatchLog.debug("[ConnectionManager] update connection pool profile:".concat(String.valueOf(String.valueOf(tmppfItem.getName()))));
            resetPool(ctPool, tmppfItem);
        }

        FBatchLog.info("[ConnectionManager] reload profile finished.");
    }

    private static void PoolsFromConfig(ProfileReader AReader, String AStr)
        throws Exception
    {
        FBatchLog.info("[ConnectionManager] initial process...");
        ProfileItem pfItem = AReader.findItem("DatabaseProfile");
        if(pfItem == null)
            throw new Exception("can NOT find profile item 'DatabaseProfile' in file:".concat(String.valueOf(String.valueOf(AStr))));
        int iTmp = 0;
        for(ProfileItem tmppfItem = pfItem.scanFirstItem(); tmppfItem != null; tmppfItem = pfItem.scanNextItem())
            if(CreateOnePool(tmppfItem))
                iTmp++;

        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("[ConnectionManager] loaded ")).append(iTmp).append(" connection pools."))));
    }

    public static ConnectionPool getConnectionPool(String AStr)
        throws NoinitialException
    {
        if(FiMark <= 0)
            throw new NoinitialException("Not initialized.");
        else
            return (ConnectionPool)FHashtable.get(AStr);
    }

    private static synchronized boolean CreateOnePool(ProfileItem ApfItem)
    {
        try
        {
            String sItemName = ApfItem.getName().trim();
            if(sItemName.length() <= 0)
                sItemName = null;
            ConnectionPool tmpPool = (ConnectionPool)FHashtable.get(sItemName);
            if(tmpPool != null) { //如果原来已存在连接
              FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("[ConnectionManager] alread has connection pool:")).append(sItemName).append(", use old connection pool!"))));
              return true;
            }
            ConnectionPool ctPool = null;
            ctPool = new ConnectionPool(sItemName, ProfileReader.getStringProfile(ApfItem, "driverClass", null),
                                        ProfileReader.getStringProfile(ApfItem, "dbUrl", null),
                                        ProfileReader.getStringProfile(ApfItem, "dbUser", null),
                                        ProfileReader.getStringProfile(ApfItem, "dbPass", null),
                                        ProfileReader.getIntProfile(ApfItem, "minConnectionPoolSize", 1),
                                        ProfileReader.getIntProfile(ApfItem, "connectionPoolSize", 50),
                                        ProfileReader.getIntProfile(ApfItem, "statementPoolSize", 300),
                                        ProfileReader.getIntProfile(ApfItem, "idleTimeout", 20),
                                        ProfileReader.getIntProfile(ApfItem, "idleConnTimeout", 60),
                                        ProfileReader.getIntProfile(ApfItem, "shrinkInterval", 10), FBatchLog);

            /*tmpPool = (ConnectionPool)FHashtable.get(sItemName);
            if(tmpPool != null) {
              tmpPool.close();
              tmpPool.stop();
              tmpPool = null;
              FHashtable.remove(sItemName);
              FBatchLog.info("Free Old Connect Pool: [" + sItemName + "]");
            }*/
            FBatchLog.info("SetUp new Connect Pool: [" + sItemName + "]");
            FHashtable.put(sItemName, ctPool);
            if(ctPool.isAutoShrink())
                ctPool.start();
        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("[ConnectionManager] load connection pool profile error:")).append(et.toString()).append(". poolItem=").append(ApfItem.toString(0, "=", '{', '}')))));
            boolean flag = false;
            return flag;
        }
        return true;
    }

    private static boolean resetPool(ConnectionPool ActPool, ProfileItem ApfItem)
    {
        try
        {
            String sItemName = ApfItem.getName().trim();
            if(sItemName.length() <= 0)
                sItemName = null;
            ActPool.resetProfile(sItemName, ProfileReader.getStringProfile(ApfItem, "driverClass", null),
                                 ProfileReader.getStringProfile(ApfItem, "dbUrl", null),
                                 ProfileReader.getStringProfile(ApfItem, "dbUser", null),
                                 ProfileReader.getStringProfile(ApfItem, "dbPass", null),
                                 ProfileReader.getIntProfile(ApfItem, "minConnectionPoolSize", 1),
                                 ProfileReader.getIntProfile(ApfItem, "connectionPoolSize", 10),
                                 ProfileReader.getIntProfile(ApfItem, "statementPoolSize", 300),
                                 ProfileReader.getIntProfile(ApfItem, "idleTimeout", 20),
                                 ProfileReader.getIntProfile(ApfItem, "idleConnTimeout", 60),
                                 ProfileReader.getIntProfile(ApfItem, "shrinkInterval", 10));
        }
        catch(Exception et)
        {
            FBatchLog.error(String.valueOf(String.valueOf((new StringBuffer("[ConnectionManager] reload connection pool profile error:")).append(et.toString()).append(". poolItem=").append(ApfItem.toString(0, "=", '{', '}')))));
            boolean flag = false;
            return flag;
        }
        return true;
    }


 /**   public ConnectionManager(String AFile)
    {

        if(AFile==null)
            AFile = "";
        AFile = AFile.trim();
        if(AFile.length()==0)
            AFile = "defaultPool.conf";
        FiUserCount++; //增加访问计数器

        try
        {
            init(AFile);
        }
        catch(Exception et)
        {
            FiMark = 0;
        }
    }**/

   /** public static synchronized void close() {
        FiUserCount--;
        if(FiUserCount>0) { //如果还有其它程序在使用此连接池
          return;
        }

        Object[] arrValues = FHashtable.values().toArray();
        if(arrValues==null)
            return;
        for(int i=0;i<arrValues.length; i++) {
            ConnectionPool plConn = (ConnectionPool)arrValues[i];
            plConn.close();
            plConn.stop();
            arrValues[i] = null;
        }
        FBatchLog.info(String.valueOf(String.valueOf((new StringBuffer("[ConnectionManager] closed ")).append(arrValues.length).append(" connection pools."))));
        FHashtable.clear();
        FHashtable = null;
        FiMark = 0;
    } **/

    public void finalize() throws Throwable {
      super.finalize();
        //close();
    }

    public void destroy() {
       // close();
    }

}
