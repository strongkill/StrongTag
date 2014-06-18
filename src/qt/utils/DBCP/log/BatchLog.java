// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   BatchLog.java

package qt.utils.DBCP.log;

import java.io.*;
import java.util.Calendar;
import java.util.Properties;

public class BatchLog
{

    private static final int O00OooooOo0O000o0o0O0 = 1000;
    private int FBufferSize;
    private StringBuffer FBuffer;
    private String FStr;
    private String FThisLogStr;
    private boolean FbUsingBuffer;
    private boolean isMessageDisplay;
    private boolean FisDebug;
    private boolean isNotWriteFile;
    private boolean isSysDebug;
    private String FLogStr;
    private String FWriteLogType = "error"; //保存要写日志的类别

  public void setWriteLogType(String FWriteLogType) {
    this.FWriteLogType = FWriteLogType;
  }

  public String getWriteLogType() {
    return FWriteLogType;
  }

  public boolean isDebug()
    {
        return FisDebug;
    }

    public boolean isSysDebug()
    {
        return isSysDebug;
    }

    public boolean isNotWriteFile()
    {
        return isNotWriteFile;
    }

    public boolean isMessageDisplay()
    {
        return isMessageDisplay;
    }

    public BatchLog()
    {
        this("logs");
    }

    public BatchLog(boolean ABool)
    {
        FbUsingBuffer = false;
        isMessageDisplay = false;
        FisDebug = false;
        isNotWriteFile = false;
        isSysDebug = false;
        FLogStr = "logs";
        isNotWriteFile = true;
        isMessageDisplay = true;
    }

    public BatchLog(int ABufferSize, String ALogStr)
    {
        FbUsingBuffer = false;
        isMessageDisplay = false;
        FisDebug = false;
        isNotWriteFile = false;
        isSysDebug = false;
        this.FLogStr = "logs";
        FBuffer = new StringBuffer("");
        FBufferSize = ABufferSize;
        FStr = NowDateStr();
        this.FLogStr = ALogStr;
        String AStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(System.getProperties().getProperty("user.dir"))))).append(File.separator).append(FLogStr)));
        File AFile = new File(AStr);
        if(!AFile.exists())
            AFile.mkdirs();
        FThisLogStr = AStr;
    }

    public BatchLog(String ALogStr)
    {
        FbUsingBuffer = false;
        isMessageDisplay = false;
        FisDebug = false;
        isNotWriteFile = false;
        isSysDebug = false;
        this.FLogStr = "logs";
        FBuffer = new StringBuffer("");
        FBufferSize = 1000;
        FStr = NowDateStr();
        this.FLogStr = ALogStr;
        //String AStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(System.getProperties().getProperty("user.dir"))))).append(File.separator).append(FLogStr)));
        File AFile = new File(ALogStr);
//        System.out.println("88888");
        if(!AFile.exists())
          AFile.mkdirs();
        FThisLogStr = ALogStr;
    }

    public String getLogName()
    {
        return FLogStr;
    }

    public void setDebug(boolean AisDebug)
    {
        FisDebug = AisDebug;
    }

    public void setStatus(boolean AisNotWriteFile)
    {
        isNotWriteFile = AisNotWriteFile;
    }

    public void setSysDebug(boolean AisSysDebug)
    {
        isSysDebug = AisSysDebug;
    }

    public void setUsingBuffer(boolean AUsingBuffer)
    {
        FbUsingBuffer = AUsingBuffer;
    }

    public void setBufferSize(int ABufferSize)
    {
        FBufferSize = ABufferSize;
    }

    public void setMessageDisplay(boolean AisMessageDisplay)
    {
        isMessageDisplay = AisMessageDisplay;
    }

    public void sysDebug(String AStr)
    { if(FWriteLogType.toLowerCase().indexOf("sysdg")<0)   //如果不需要写此类日志
          return;

        if(isSysDebug)
            WriteStr("SYSDG", AStr);
    }

    public void debug(String AStr)
    {   if(FWriteLogType.toLowerCase().indexOf("debug")<0)   //如果不需要写此类日志
          return;

        if(FisDebug) {
            WriteStr("DEBUG", AStr);
        }
    }

    public void info(String AStr)
    {
      if(FWriteLogType.toLowerCase().indexOf("info")<0)   //如果不需要写此类日志
          return;

//        System.out.println("iNFO outPut");
//        System.out.println(AStr);
        WriteStr("INFO", AStr);
    }

    public void warning(String AStr)
    {
      if(FWriteLogType.toLowerCase().indexOf("warn")<0)   //如果不需要写此类日志
          return;

        WriteStr("WARNING", AStr);
    }

    public void error(String AStr)
    {
      if(FWriteLogType.toLowerCase().indexOf("error")<0)   //如果不需要写此类日志
          return;
        WriteStr("ERROR", AStr);
    }

    public void fatal(String AStr)
    {
      if(FWriteLogType.toLowerCase().indexOf("fatal")<0)   //如果不需要写此类日志
          return;
        WriteStr("FATAL", AStr);
    }

    private void WriteStr(String AsMark, String AStr)
    {

        if(AStr == null || "".equals(AStr))
            return;
        String ATmp = NowDateStr();
        if(isMessageDisplay)
            System.out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AsMark)))).append("\t").append(NowTimeStr()).append("\t").append(AStr))));
        if(isNotWriteFile)
            return;
        FBuffer.append(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AsMark)))).append("\t").append(NowTimeStr()).append("\t").append(AStr).append("\r\n"))));
        if(!FbUsingBuffer || FBuffer.length() > FBufferSize)
            flush();
    }

    public synchronized void flush()
    {
        if("".equals(FBuffer))
            return;
        String AStr = NowDateStr();
        String Atmp = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(FThisLogStr)))).append(File.separator).append(AStr).append(".log")));
//        System.out.println("This File is:"+Atmp);
        File AFile = new File(Atmp);
        RandomAccessFile ARDFile=null;
        try {
            ARDFile = new RandomAccessFile(AFile, "rw");
            ARDFile.seek(ARDFile.length());
            String AStr2 = FBuffer.toString();
            ARDFile.write(AStr2.getBytes());
            FBuffer = new StringBuffer("");
        } catch (Exception et) {
            et.printStackTrace();
        } finally {//added on 2005-10-31 by zhuhe
        	if(ARDFile!=null){
        		try {
					ARDFile.close();
					ARDFile=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }

    private String NowDateStr()
    {
        Calendar ACalendar = Calendar.getInstance();
        String AStr = String.valueOf(ACalendar.get(1));
        String AStr2 = String.valueOf(ACalendar.get(2) + 1);
        String AStr3 = String.valueOf(ACalendar.get(5));
        AStr2 = 1 != AStr2.length() ? AStr2 : "0".concat(String.valueOf(String.valueOf(AStr2)));
        AStr3 = 1 != AStr3.length() ? AStr3 : "0".concat(String.valueOf(String.valueOf(AStr3)));
        return String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AStr)))).append(AStr2).append(AStr3)));
    }

    private String NowTimeStr()
    {
        Calendar ACalendar = Calendar.getInstance();
        String AStr = String.valueOf(ACalendar.get(11));
        String AStr2 = String.valueOf(ACalendar.get(12));
        String AStr3 = String.valueOf(ACalendar.get(13));
        AStr = 1 != AStr.length() ? AStr : "0".concat(String.valueOf(String.valueOf(AStr)));
        AStr2 = 1 != AStr2.length() ? AStr2 : "0".concat(String.valueOf(String.valueOf(AStr2)));
        AStr3 = 1 != AStr3.length() ? AStr3 : "0".concat(String.valueOf(String.valueOf(AStr3)));
        return String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AStr)))).append(":").append(AStr2).append(":").append(AStr3)));
    }

    protected void finalize()
    {
        if(!isNotWriteFile)
            flush();
    }

    static
    {
        //O00OooooOo0O000o0o0O0 = 1000;
    }
}
