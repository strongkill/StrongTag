// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   ProfileItem.java

package qt.utils.DBCP.profile;

import java.io.*;

public class ProfileItem
{

    private int FiGroup;
    private String FName;
    private String FContent;
    private ProfileItem FItem1;
    private ProfileItem FItem2;
    private ProfileItem FItem3;

    public ProfileItem(String AName, String AContent)
    {
        FName = AName;
        FContent = AContent;
        FItem1 = null;
        FItem2 = null;
        FItem3 = null;
        FiGroup = 0;
    }

    public ProfileItem(String AName)
    {
        FName = AName;
        FItem1 = null;
        FItem2 = null;
        FContent = null;
        FItem3 = null;
        FiGroup = 1;
    }

    public ProfileItem copy()
    {
        if(!isGroup())
            return new ProfileItem(FName, FContent);
        ProfileItem AItem = new ProfileItem(FName);
        for(ProfileItem AforItem = FItem2; AforItem != null; AforItem = AforItem.FItem1)
            AItem.addItem(AforItem.copy());

        return AItem;
    }

    public String getContent()
    {
        return FContent;
    }

    public String getName()
    {
        return FName;
    }

    public boolean isGroup()
    {
        return FiGroup == 1;
    }

    public void clear()
    {
        if(FItem2 != null)
            FItem2.clear();
        FItem2 = null;
        if(FItem1 != null)
            FItem1.clear();
        FItem1 = null;
    }

    public boolean addItem(ProfileItem AItem)
    {
        if(FiGroup != 1 && AItem == null)
            return false;
        ProfileItem AThisItem1 = null;
        ProfileItem FThisItem2;
        for(FThisItem2 = FItem2; FThisItem2 != null && FThisItem2.FName.compareTo(AItem.FName) < 0; FThisItem2 = FThisItem2.FItem1)
            AThisItem1 = FThisItem2;

        if(FThisItem2 == null && AThisItem1 == null)
            FItem2 = AItem;
        else
        if(FThisItem2 == null)
            AThisItem1.FItem1 = AItem;
        else
        if(FThisItem2.FName.compareTo(AItem.FName) == 0)
        {
            AItem.FItem1 = FThisItem2.FItem1;
            if(AThisItem1 == null)
                FItem2 = AItem;
            else
                AThisItem1.FItem1 = AItem;
        } else
        {
            AItem.FItem1 = FThisItem2;
            if(AThisItem1 == null)
                FItem2 = AItem;
            else
                AThisItem1.FItem1 = AItem;
        }
        return true;
    }

    public String toString(int Aint, String AStr1, char AChar1, char AChar2)
    {
        StringWriter AWriter = new StringWriter();
        PrintWriter APtWriter = new PrintWriter(AWriter);
        toWriter(APtWriter, Aint, AStr1, AChar1, AChar2);
        return AWriter.getBuffer().toString();
    }

    public void toWriter(PrintWriter AprWriter, int Aint, String AStr, char AChar1, char AChar2)
    {
        ProfileItem FThisItem = FItem2;
        String AThisStr1 = "   ";
        String AThisStr2 = "";
        String AThisStr3 = "";
        for(int i = 0; i < Aint; i++)
            AThisStr2 = String.valueOf(AThisStr2) + String.valueOf(AThisStr1);

        if(FiGroup == 0)
        {
            if(FContent == null || FContent.length() <= 0)
                AThisStr3 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AThisStr2)))).append(FName).append("\n")));
            else
                AThisStr3 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AThisStr2)))).append(FName).append(AStr).append(FContent).append("\n")));
            if(AprWriter == null)
                System.out.print(AThisStr3);
            else
                AprWriter.write(AThisStr3);
        } else
        {
            AThisStr3 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AThisStr2)))).append(FName).append(AChar1).append("\n")));
            if(AprWriter == null)
                System.out.print(AThisStr3);
            else
                AprWriter.write(AThisStr3);
            for(; FThisItem != null; FThisItem = FThisItem.FItem1)
                if(FThisItem.FiGroup == 0)
                {
                    if(FThisItem.FContent == null || FThisItem.FContent.length() <= 0)
                        AThisStr3 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AThisStr2)))).append(AThisStr1).append(FThisItem.FName).append("\n")));
                    else
                        AThisStr3 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AThisStr2)))).append(AThisStr1).append(FThisItem.FName).append(AStr).append(FThisItem.FContent).append("\n")));
                    if(AprWriter == null)
                        System.out.print(AThisStr3);
                    else
                        AprWriter.write(AThisStr3);
                } else
                {
                    FThisItem.toWriter(AprWriter, Aint + 1, AStr, AChar1, AChar2);
                }

            AThisStr3 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AThisStr2)))).append(AChar2).append("\n")));
            if(AprWriter == null)
                System.out.print(AThisStr3);
            else
                AprWriter.write(AThisStr3);
        }
    }

    public ProfileItem searchItem(String AStr)
    {
        ProfileItem AThisItem = FItem2;
        if(AStr == null)
            return null;
        for(; AThisItem != null; AThisItem = AThisItem.FItem1)
            if(AThisItem.FName.compareTo(AStr) == 0)
                return AThisItem;

        return null;
    }

    public ProfileItem scanFirstItem()
    {
        FItem3 = FItem2;
        return FItem3;
    }

    public ProfileItem scanNextItem()
    {
        if(FItem3 == null)
        {
            return null;
        } else
        {
            FItem3 = FItem3.FItem1;
            return FItem3;
        }
    }

    public static String bytesToHexstring(byte AarrByte[])
    {
        String AStr = "";
        try
        {
            for(int i = 0; i < AarrByte.length; i++)
            {
                String ATmp = Integer.toHexString(AarrByte[i]);
                if(ATmp.length() <= 1)
                    ATmp = "0".concat(String.valueOf(String.valueOf(ATmp)));
                if(AarrByte[i] < 0)
                    ATmp = ATmp.substring(ATmp.length() - 2);
                AStr = String.valueOf(AStr) + String.valueOf(ATmp);
            }

            String s = AStr;
            return s;
        }
        catch(Exception et)
        {
            String s1 = null;
            return s1;
        }
    }

    public static byte[] hexstringToBytes(String AStr)
    {
        byte abyte0[];
        try
        {
            byte arrByte[] = new byte[AStr.length() / 2];
            int Aint = 0;
            for(int i = 0; i < AStr.length();)
            {
                String ATmp = AStr.substring(i, i + 2);
                byte aByte = (byte)Integer.parseInt(ATmp, 16);
                arrByte[Aint] = aByte;
                i += 2;
                Aint++;
            }

            byte abyte1[] = arrByte;
            return abyte1;
        }
        catch(Exception et)
        {
            abyte0 = null;
        }
        return abyte0;
    }

    public static void main(String arrStr[])
    {
        ProfileItem AItem1 = new ProfileItem("maingroup");
        ProfileItem AItem2 = new ProfileItem("item01", "asdfasdfad");
        AItem1.addItem(AItem2);
        AItem1.toWriter(null, 0, "=", '{', '}');
        AItem1.searchItem(null);
        AItem2 = new ProfileItem("item02", "asa23232dfasdfad");
        AItem1.addItem(AItem2);
        AItem2 = new ProfileItem("item03", "876grrdfasdfad");
        AItem1.addItem(AItem2);
        ProfileItem FItem2 = new ProfileItem("group01");
        AItem1.addItem(FItem2);
        AItem2 = new ProfileItem("item01-01", "asdddddasdf,asdfad");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-02", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-03", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-04", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-05", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-06", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-07", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-08", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-09", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-10", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item01-11", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        FItem2 = new ProfileItem("group02");
        AItem1.addItem(FItem2);
        AItem2 = new ProfileItem("item02-01", "asdddddasdf,asdfad");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-02", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-03", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-04", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-05", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-06", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-07", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-08", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-09", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-10", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem2 = new ProfileItem("item02-11", "23123412,asdqwerq");
        FItem2.addItem(AItem2);
        AItem1.toWriter(null, 0, "=", '{', '}');
        ProfileItem ATmpItem = AItem1.copy();
        AItem1.clear();
    }
}
