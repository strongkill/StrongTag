// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   NoinitialException.java

package qt.utils.DBCP;

import java.sql.SQLException;

public class NoinitialException extends SQLException
{

    public NoinitialException(String AStr)
    {
        super(AStr);
    }
}
