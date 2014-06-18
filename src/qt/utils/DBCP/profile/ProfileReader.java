// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   ProfileReader.java

package qt.utils.DBCP.profile;

import java.io.*;
import java.util.*;

// Referenced classes of package net.snapbug.profile:
//            ProfileItem

public class ProfileReader
{

    public static char signComment = '#';
    public static String signParam = "=";
    public static String signValue = ",";
    public static char signGroupBegin = '{';
    public static char signGroupEnd = '}';
    private String FFileName;
    private ProfileItem FpfItem;
    private static BitSet FBitSet;
    private static final int Fint = 32;
    private static String FEncodeName = null;

    public ProfileReader(String AFileName)
    {
        FFileName = null;
        FpfItem = null;
        FFileName = AFileName;
    }

    public ProfileItem getRootItem()
    {
        return FpfItem;
    }

    public void clear()
    {
        FpfItem.clear();
    }

    public void loadProfile()
        throws Exception
    {
        Stack AStack = new Stack();
        ProfileItem AItem1 = null;
        ProfileItem AItem2 = null;
        ProfileItem AItem3 = null;
        int ALine = 0;
        FpfItem = new ProfileItem("maingroup");
        AItem1 = FpfItem;
        try
        {
            BufferedReader AReader = new BufferedReader(new FileReader(FFileName));
            String AStr;
            do
            {
                AStr = AReader.readLine();
                if(AStr == null)
                    continue;
                ALine++;
                AStr = AStr.trim();
                if(AStr.length() <= 0)
                    continue;
                int Aint1 = AStr.indexOf(signComment);
                if(Aint1 >= 0)
                {
                    AStr = AStr.substring(0, Aint1).trim();
                    if(AStr.length() <= 0)
                        continue;
                }
                if(AStr.charAt(AStr.length() - 1) == signGroupBegin)
                {
                    String ATmp = AStr.substring(0, AStr.length() - 1);
                    ATmp = ATmp.trim();
                    if(ATmp.length() <= 0)
                        throw new Exception(String.valueOf(String.valueOf((new StringBuffer("line ")).append(ALine).append(" : no group name"))));
                    AItem2 = new ProfileItem(ATmp);
                    AItem1.addItem(AItem2);
                    AStack.push(AItem1);
                    AItem1 = AItem2;
                } else
                if(AStr.charAt(0) == signGroupEnd)
                {
                    if(AStack.isEmpty())
                        throw new Exception(String.valueOf(String.valueOf((new StringBuffer("line ")).append(ALine).append(" : unexpected group end"))));
                    AItem1 = (ProfileItem)AStack.pop();
                } else
                {
                    String ATmp1 = parseString(AStr, signParam, 1);
                    String ATmp2 = parseString(AStr, signParam, 10001);
                    ATmp1 = ATmp1.trim();
                    ATmp2 = ATmp2.trim();
                    if(ATmp1.length() > 0)
                    {
                        AItem3 = new ProfileItem(ATmp1, ATmp2);
                        AItem1.addItem(AItem3);
                    }
                }
            } while(AStr != null);
            AReader.close();
            if(!AStack.isEmpty())
                throw new Exception("expected the group end at line:".concat(String.valueOf(String.valueOf(ALine))));
        }
        catch(Exception et)
        {
            String ATmp = String.valueOf(String.valueOf((new StringBuffer("Have error when reading '")).append(FFileName).append("':").append(et.toString())));
            FpfItem = null;
            throw new Exception(ATmp);
        }
    }

    public ProfileItem findItem(String AStr)
    {
        ProfileItem AItem = FpfItem;
        int Aint = 1;
        try
        {
            String ATmp = parseString(AStr, ".", Aint++);
            do
            {
                AItem = AItem.searchItem(ATmp);
                if(AItem == null)
                    break;
                ATmp = parseString(AStr, ".", Aint++);
            } while(ATmp.length() > 0);
        }
        catch(Exception et)
        {
            return null;
        }
        return AItem;
    }

    public static String getValue(ProfileItem AItem, int Aint)
    {
        if(AItem == null)
            return null;
        String AStr = AItem.getContent();
        if(AStr == null)
            return null;
        if(Aint <= 0)
            return AStr;
        try
        {
            String s = parseString(AStr, signValue, Aint);
            return s;
        }
        catch(Exception et)
        {
            String s1 = null;
            return s1;
        }
    }

    public static String getHexValue(ProfileItem AItem, int Aint)
    {
        if(AItem == null)
            return null;
        String AStr = AItem.getContent();
        if(AStr == null)
            return null;
        if(Aint <= 0)
        {
            byte arrByte[] = hexstringToBytes(AStr);
            if(arrByte != null)
                AStr = new String(arrByte);
            return AStr;
        }
        try
        {
            AStr = parseString(AStr, signValue, Aint);
            if(AStr == null)
            {
                String s = null;
                return s;
            }
            byte arrByte2[] = hexstringToBytes(AStr);
            if(arrByte2 != null)
                AStr = new String(arrByte2);
            String s1 = AStr;
            return s1;
        }
        catch(Exception et)
        {
            String s2 = null;
            return s2;
        }
    }

    public void printProfile()
    {
        if(FpfItem == null)
            System.out.println("profile empty");
        else
            System.out.println(FpfItem.toString(0, signParam, signGroupBegin, signGroupEnd));
    }

    public static void printProfile(ProfileItem AItem)
    {
        if(AItem == null)
            System.out.println("profile empty");
        else
            System.out.println(AItem.toString(0, signParam, signGroupBegin, signGroupEnd));
    }

    public static String parseString(String AStr1, String AStr2, int Aint)
        throws Exception
    {
        int AThisInt = 0;
        int AThisInt2 = 0;
        int AThisInt3 = 0;
        if(Aint >= 10000)
        {
            Aint -= 10000;
            AThisInt3 = 1;
        }
        for(int i = 0; i < Aint; i++)
        {
            if(i > 0)
                AThisInt += AStr2.length();
            AThisInt2 = AThisInt;
            AThisInt = AStr1.indexOf(AStr2, AThisInt);
            if(AThisInt < 0)
                if(AThisInt3 == 0 && i == Aint - 1)
                    return AStr1.substring(AThisInt2).trim();
                else
                    return "";
        }

        if(AThisInt3 == 0)
            return AStr1.substring(AThisInt2, AThisInt).trim();
        if(Aint > 0)
            AThisInt += AStr2.length();
        return AStr1.substring(AThisInt).trim();
    }

    public static int hexStringToInt(String AStr)
        throws Exception
    {
        return (int)Long.parseLong(AStr, 16);
    }

    public static byte[] hexStringToBytes(String AStr)
        throws Exception
    {
        if("null".compareToIgnoreCase(AStr) == 0)
            return null;
        else
            return hexstringToBytes(AStr);
    }

    public static String hexStringToString(String AStr1, String AStr2)
        throws Exception
    {
        if("null".compareToIgnoreCase(AStr1) == 0)
            return null;
        else
            return new String(hexstringToBytes(AStr1), AStr2);
    }

    public static String hexStringToString(String AStr)
        throws Exception
    {
        if("null".compareToIgnoreCase(AStr) == 0)
            return null;
        else
            return new String(hexstringToBytes(AStr));
    }

    public static Date hexStringToDate(String AStr)
        throws Exception
    {
        if("null".compareToIgnoreCase(AStr) == 0)
            return null;
        else
            return new Date(Long.parseLong(AStr, 16));
    }

    public static boolean hexStringToBoolean(String AStr)
        throws Exception
    {
        return "1".compareTo(AStr) == 0;
    }

    public static String toHexString(int Aint)
        throws Exception
    {
        return Integer.toHexString(Aint);
    }

    public static String toHexString(String AStr1, String AStr2)
        throws Exception
    {
        if(AStr1 == null)
            return "null";
        else
            return bytesToHexstring(AStr1.getBytes(AStr2));
    }

    public static String toHexString(String AStr)
        throws Exception
    {
        if(AStr == null)
            return "null";
        else
            return bytesToHexstring(AStr.getBytes());
    }

    public static String toHexString(boolean ABool)
        throws Exception
    {
        if(ABool)
            return "1";
        else
            return "0";
    }

    public static String toHexString(Date ADate)
        throws Exception
    {
        if(ADate == null)
            return "null";
        else
            return Long.toHexString(ADate.getTime());
    }

    public static String toHexString(byte arrByte[])
        throws Exception
    {
        if(arrByte == null)
            return "null";
        else
            return bytesToHexstring(arrByte);
    }

    public static String bytesToHexstring(byte arrByte[])
    {
        StringBuffer ABuffer = new StringBuffer("");
        try
        {
            for(int i = 0; i < arrByte.length; i++)
            {
                String AStr = Integer.toHexString(arrByte[i]);
                if(AStr.length() <= 1)
                    AStr = "0".concat(String.valueOf(String.valueOf(AStr)));
                if(arrByte[i] < 0)
                    AStr = AStr.substring(AStr.length() - 2);
                ABuffer.append(AStr);
            }

            String s = ABuffer.toString();
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

    public static final String encodeURL(String AStr)
        throws Exception
    {
        return encodeURL(AStr, FEncodeName);
    }

    public static final String decodeURL(String AStr)
        throws Exception
    {
        return decodeURL(AStr, FEncodeName);
    }

    public static String getDefaultEncodeName()
    {
        return FEncodeName;
    }

    public static String encodeURL(String AStr1, String AStr2)
        throws UnsupportedEncodingException
    {
        if(AStr1 == null)
            return null;
        if(AStr2 == null)
            return AStr1;
        boolean ABool1 = false;
        boolean ABool2 = false;
        int Aint = 10;
        StringBuffer ABuffer = new StringBuffer(AStr1.length());
        ByteArrayOutputStream AStream = new ByteArrayOutputStream(Aint);
        BufferedWriter AWriter = new BufferedWriter(new OutputStreamWriter(AStream, AStr2));
        for(int i = 0; i < AStr1.length(); i++)
        {
            int AiTmp = AStr1.charAt(i);
            if(FBitSet.get(AiTmp))
            {
                if(AiTmp == 32)
                {
                    AiTmp = 43;
                    ABool1 = true;
                }
                ABuffer.append((char)AiTmp);
                ABool2 = true;
                continue;
            }
            try
            {
                if(ABool2)
                {
                    AWriter = new BufferedWriter(new OutputStreamWriter(AStream, AStr2));
                    ABool2 = false;
                }
                AWriter.write(AiTmp);
                if(AiTmp >= 55296 && AiTmp <= 56319 && i + 1 < AStr1.length())
                {
                    int AiTmp2 = AStr1.charAt(i + 1);
                    if(AiTmp2 >= 56320 && AiTmp2 <= 57343)
                    {
                        AWriter.write(AiTmp2);
                        i++;
                    }
                }
                AWriter.flush();
            }
            catch(IOException et)
            {
                AStream.reset();
                continue;
            }
            byte AtmpArrByte[] = AStream.toByteArray();
            for(int j = 0; j < AtmpArrByte.length; j++)
            {
                ABuffer.append('%');
                char AChar = Character.forDigit(AtmpArrByte[j] >> 4 & 0xf, 16);
                if(Character.isLetter(AChar))
                    AChar -= ' ';
                ABuffer.append(AChar);
                AChar = Character.forDigit(AtmpArrByte[j] & 0xf, 16);
                if(Character.isLetter(AChar))
                    AChar -= ' ';
                ABuffer.append(AChar);
            }

            AStream.reset();
            ABool1 = true;
        }

        return ABool1 ? ABuffer.toString() : AStr1;
    }

    public static String decodeURL(String AStr1, String AStr2)
        throws UnsupportedEncodingException
    {
        if(AStr1 == null)
            return null;
        if(AStr2 == null)
            return AStr1;
        boolean ABool = false;
        StringBuffer ABuffer = new StringBuffer();
        int Aint1 = AStr1.length();
        int Aint2 = 0;
        if(AStr2.length() == 0)
            throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
        do
        {
            if(Aint2 >= Aint1)
                break;
            char AChar = AStr1.charAt(Aint2);
            switch(AChar)
            {
            case 43: // '+'
                ABuffer.append(' ');
                Aint2++;
                ABool = true;
                break;

            case 37: // '%'
                try
                {
                    byte AarrByte[] = new byte[(Aint1 - Aint2) / 3];
                    int AiTmp = 0;
                    do
                    {
                        if(Aint2 + 2 >= Aint1 || AChar != '%')
                            break;
                        AarrByte[AiTmp++] = (byte)Integer.parseInt(AStr1.substring(Aint2 + 1, Aint2 + 3), 16);
                        if((Aint2 += 3) < Aint1)
                            AChar = AStr1.charAt(Aint2);
                    } while(true);
                    if(Aint2 < Aint1 && AChar == '%')
                        throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
                    ABuffer.append(new String(AarrByte, 0, AiTmp, AStr2));
                }
                catch(NumberFormatException et)
                {
                    throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - ".concat(String.valueOf(String.valueOf(et.getMessage()))));
                }
                ABool = true;
                break;

            default:
                ABuffer.append(AChar);
                Aint2++;
                break;
            }
        } while(true);
        return ABool ? ABuffer.toString() : AStr1;
    }

    public static String getStringProfile(ProfileItem AItem, String AStr1, String AStr2)
    {
        ProfileItem AThisItem = AItem.searchItem(AStr1);
        if(AThisItem != null)
        {
            String ATmp = AThisItem.getContent();
            if(ATmp != null && ATmp.length() > 0)
                return ATmp;
        }
        return AStr2;
    }

    public static int getIntProfile(ProfileItem AItem, String AStr1, int Aint)
        throws NumberFormatException
    {
        ProfileItem AThisItem = AItem.searchItem(AStr1);
        if(AThisItem != null)
        {
            String ATmp = AThisItem.getContent();
            if(ATmp != null && ATmp.length() > 0)
                return Integer.parseInt(ATmp);
        }
        return Aint;
    }

    public static boolean getBooleanProfile(ProfileItem AItem, String AStr, boolean ABool)
    {
        ProfileItem AThisItem = AItem.searchItem(AStr);
        if(AThisItem != null)
        {
            String ATmp = AThisItem.getContent();
            if(ATmp != null)
                return "yes".equalsIgnoreCase(ATmp) || "true".equalsIgnoreCase(ATmp) || "on".equalsIgnoreCase(ATmp);
        }
        return ABool;
    }

    public static void main(String ArrStr[])
    {
        try
        {
            String AStr = "0000000100001ed200000000d24d966e000002123030303030313039303030303030303432364146394330303030303030303030303030303030303030313031303130393538353835333434343130303030303030303030303333313333333533323331333733323339333633313336303030303030303030303030303030303030303030303030304633393330333133303330333933303332333033303330333033313330303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303033313330333133303030303030303030303030303030303030303030303030303030303030303030303030313331333333353332333133373332333933363331333630303030303030303030303030303030303030303641423646394437443341334241423044364230443641334143434142324333423442444430434445324244424242434432413342464131423142364639443744334133424142384238433744374133424143444532424442424243443241334143434143374330434542434337433541454433443142354334433946414338443542324132434446434235463443424644423543344334454143314534423543344338434241314133413142313330324533314434414132464343463541334143434544454430454243444342423641383044304132303230323032303230323032303230            3314434414132464343463541334143434544454430454243444342423641383044304132303230323032303230323032303230";
            byte arrByte[] = hexStringToBytes(AStr);
            String AStr2 = "123&456&567&";
            System.out.println(parseString(AStr2, "&", 1));
            System.out.println(parseString(AStr2, "&", 10003));
            System.out.println(parseString("adfd", ",", 1));
            System.out.println(parseString("adfd,das,df,", ",", 10001));
            String AStr3 = "我的abc";
            String AStr4 = encodeURL(AStr3, "UTF-8");
            System.out.println(decodeURL(AStr4, "UTF-8"));
        }
        catch(Exception et)
        {
            System.out.println(et.toString());
        }
        ProfileReader AReader = new ProfileReader("E:/INSTALL.LOG");
        try
        {
            AReader.loadProfile();
            AReader.printProfile();
        }
        catch(Exception et)
        {
            System.out.println(et.toString());
        }
        ProfileItem AItem = AReader.findItem("Internet.Gateway");
        printProfile(AItem);
        String ATmp = getValue(AItem, 0);
        System.out.println(ATmp);
        ATmp = getHexValue(AItem, 3);
        System.out.println(ATmp);
        AItem = AReader.findItem("Internet.AccessList");
        for(ProfileItem AforItem = AItem.scanFirstItem(); AforItem != null; AforItem = AforItem.scanNextItem())
            if(AforItem.isGroup())
                System.out.println(String.valueOf(String.valueOf(AforItem.getName())).concat(" is a group"));
            else
                System.out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(AforItem.getName())))).append(" : ").append(getHexValue(AforItem, 0)))));

    }

    static
    {
        //Fint = 32;
        FBitSet = new BitSet(256);
        for(int i = 97; i <= 122; i++)
            FBitSet.set(i);

        for(int i = 65; i <= 90; i++)
            FBitSet.set(i);

        for(int i = 48; i <= 57; i++)
            FBitSet.set(i);

        FBitSet.set(32);
        FBitSet.set(45);
        FBitSet.set(95);
        FBitSet.set(46);
        FBitSet.set(42);
        FEncodeName = System.getProperty("file.encoding");
    }
}
