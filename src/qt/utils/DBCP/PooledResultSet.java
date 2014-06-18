// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   PooledResultSet.java

package qt.utils.DBCP;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

// Referenced classes of package net.snapbug.util.dbtool:
//            PooledStatement

public class PooledResultSet
    implements ResultSet
{

    private ResultSet FResultSet;
    private PooledStatement FStmt;

    public PooledResultSet(PooledStatement AStmt, ResultSet AResultSet)
    {
        this.FResultSet = AResultSet;
        this.FStmt = AStmt;
    }

    private void active()
    {
        FStmt.active();
    }

    public static void main(String args[])
    {
    }

    public static void booked()
    {
    }

    public boolean next()
        throws SQLException
    {
        active();
        return FResultSet.next();
    }

    public void close()
        throws SQLException
    {
        FResultSet.close();
        FResultSet = null;
    }

    public boolean wasNull()
        throws SQLException
    {
        active();
        return FResultSet.wasNull();
    }

    public String getString(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getString(Index);
    }

    public boolean getBoolean(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getBoolean(Index);
    }

    public byte getByte(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getByte(Index);
    }

    public short getShort(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getShort(Index);
    }

    public int getInt(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getInt(Index);
    }

    public long getLong(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getLong(Index);
    }

    public float getFloat(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getFloat(Index);
    }

    public double getDouble(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getDouble(Index);
    }

    public BigDecimal getBigDecimal(int Aint1, int Aint2)
        throws SQLException
    {
        active();
        return null;
    }

    public byte[] getBytes(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getBytes(Index);
    }

    public Date getDate(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getDate(Index);
    }

    public Time getTime(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getTime(Index);
    }

    public Timestamp getTimestamp(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getTimestamp(Index);
    }

    public InputStream getAsciiStream(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getAsciiStream(Index);
    }

    public InputStream getUnicodeStream(int Index)
        throws SQLException
    {
        active();
        return null;
    }

    public InputStream getBinaryStream(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getBinaryStream(Index);
    }

    public String getString(String Index)
        throws SQLException
    {
        active();
        return FResultSet.getString(Index);
    }

    public boolean getBoolean(String Index)
        throws SQLException
    {
        active();
        return FResultSet.getBoolean(Index);
    }

    public byte getByte(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getByte(FieldName);
    }

    public short getShort(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getShort(FieldName);
    }

    public int getInt(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getInt(FieldName);
    }

    public long getLong(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getLong(FieldName);
    }

    public float getFloat(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getFloat(FieldName);
    }

    public double getDouble(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getDouble(FieldName);
    }

    public BigDecimal getBigDecimal(String AStr, int Aint)
        throws SQLException
    {
        active();
        return null;
    }

    public byte[] getBytes(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getBytes(FieldName);
    }

    public Date getDate(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getDate(FieldName);
    }

    public Time getTime(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getTime(FieldName);
    }

    public Timestamp getTimestamp(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getTimestamp(FieldName);
    }

    public InputStream getAsciiStream(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getAsciiStream(FieldName);
    }

    public InputStream getUnicodeStream(String FieldName)
        throws SQLException
    {
        active();
        return null;
    }

    public URL getURL(String columnName)
           throws SQLException
    {
         active();
         return FResultSet.getURL(columnName);
    }

    public URL getURL(int columnIndex)
           throws SQLException
    {
         active();
         return FResultSet.getURL(columnIndex);
    }

    public InputStream getBinaryStream(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getBinaryStream(FieldName);
    }

    public SQLWarning getWarnings()
        throws SQLException
    {
        active();
        return FResultSet.getWarnings();
    }

    public void clearWarnings()
        throws SQLException
    {
        active();
        FResultSet.clearWarnings();
    }

    public String getCursorName()
        throws SQLException
    {
        active();
        return FResultSet.getCursorName();
    }

    public ResultSetMetaData getMetaData()
        throws SQLException
    {
        active();
        return FResultSet.getMetaData();
    }

    public Object getObject(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getObject(Index);
    }

    public Object getObject(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getObject(FieldName);
    }

    public int findColumn(String AStr)
        throws SQLException
    {
        active();
        return FResultSet.findColumn(AStr);
    }

    public Reader getCharacterStream(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getCharacterStream(Index);
    }

    public Reader getCharacterStream(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getCharacterStream(FieldName);
    }

    public BigDecimal getBigDecimal(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getBigDecimal(Index);
    }

    public BigDecimal getBigDecimal(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getBigDecimal(FieldName);
    }

    public boolean isBeforeFirst()
        throws SQLException
    {
        active();
        return FResultSet.isBeforeFirst();
    }

    public boolean isAfterLast()
        throws SQLException
    {
        active();
        return FResultSet.isAfterLast();
    }

    public boolean isFirst()
        throws SQLException
    {
        active();
        return FResultSet.isFirst();
    }

    public boolean isLast()
        throws SQLException
    {
        active();
        return FResultSet.isLast();
    }

    public void beforeFirst()
        throws SQLException
    {
        active();
        FResultSet.beforeFirst();
    }

    public void afterLast()
        throws SQLException
    {
        active();
        FResultSet.afterLast();
    }

    public boolean first()
        throws SQLException
    {
        active();
        return FResultSet.first();
    }

    public boolean last()
        throws SQLException
    {
        active();
        return FResultSet.last();
    }

    public int getRow()
        throws SQLException
    {
        active();
        return FResultSet.getRow();
    }

    public boolean absolute(int Aint)
        throws SQLException
    {
        active();
        return FResultSet.absolute(Aint);
    }

    public boolean relative(int Aint)
        throws SQLException
    {
        active();
        return FResultSet.relative(Aint);
    }

    public boolean previous()
        throws SQLException
    {
        active();
        return FResultSet.previous();
    }

    public void setFetchDirection(int Aint)
        throws SQLException
    {
        active();
        FResultSet.setFetchDirection(Aint);
    }

    public int getFetchDirection()
        throws SQLException
    {
        active();
        return FResultSet.getFetchDirection();
    }

    public void setFetchSize(int Aint)
        throws SQLException
    {
        active();
        FResultSet.setFetchSize(Aint);
    }

    public int getFetchSize()
        throws SQLException
    {
        active();
        return FResultSet.getFetchSize();
    }

    public int getType()
        throws SQLException
    {
        active();
        return FResultSet.getType();
    }

    public int getConcurrency()
        throws SQLException
    {
        active();
        return FResultSet.getConcurrency();
    }

    public boolean rowUpdated()
        throws SQLException
    {
        active();
        return FResultSet.rowUpdated();
    }

    public boolean rowInserted()
        throws SQLException
    {
        active();
        return FResultSet.rowInserted();
    }

    public boolean rowDeleted()
        throws SQLException
    {
        active();
        return FResultSet.rowDeleted();
    }

    public void updateNull(int Index)
        throws SQLException
    {
        active();
        FResultSet.updateNull(Index);
    }

    public void updateBoolean(int Index, boolean Value)
        throws SQLException
    {
        active();
        FResultSet.updateBoolean(Index, Value);
    }

    public void updateByte(int Index, byte Value)
        throws SQLException
    {
        active();
        FResultSet.updateByte(Index, Value);
    }

    public void updateShort(int Index, short Value)
        throws SQLException
    {
        active();
        FResultSet.updateShort(Index, Value);
    }

    public void updateInt(int Index, int Value)
        throws SQLException
    {
        active();
        FResultSet.updateInt(Index, Value);
    }

    public void updateLong(int Index, long Value)
        throws SQLException
    {
        active();
        FResultSet.updateLong(Index, Value);
    }

    public void updateFloat(int Index, float Value)
        throws SQLException
    {
        active();
        FResultSet.updateFloat(Index, Value);
    }

    public void updateDouble(int Index, double Value)
        throws SQLException
    {
        active();
        FResultSet.updateDouble(Index, Value);
    }

    public void updateBigDecimal(int Index, BigDecimal Value)
        throws SQLException
    {
        active();
        FResultSet.updateBigDecimal(Index, Value);
    }

    public void updateString(int Index, String Value)
        throws SQLException
    {
        active();
        FResultSet.updateString(Index, Value);
    }

    public void updateBytes(int Index, byte Value[])
        throws SQLException
    {
        active();
        FResultSet.updateBytes(Index, Value);
    }

    public void updateDate(int Index, Date Value)
        throws SQLException
    {
        active();
        FResultSet.updateDate(Index, Value);
    }

    public void updateTime(int Index, Time Value)
        throws SQLException
    {
        active();
        FResultSet.updateTime(Index, Value);
    }

    public void updateTimestamp(int Index, Timestamp Value)
        throws SQLException
    {
        active();
        FResultSet.updateTimestamp(Index, Value);
    }

    public void updateAsciiStream(int Index, InputStream Ainput, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateAsciiStream(Index, Ainput, Aint);
    }

    public void updateArray(String columnName,
                        Array x)
                 throws SQLException
    {
        active();
        FResultSet.updateArray(columnName, x);
    }

    public void updateArray(int columnIndex,
                        Array x)
                 throws SQLException
    {
        active();
        FResultSet.updateArray(columnIndex, x);
    }

    public void updateBinaryStream(int Index, InputStream Ainput, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateBinaryStream(Index, Ainput, Aint);
    }

    public void updateClob(String columnName,
                       Clob x)
                throws SQLException
    {
        active();
        FResultSet.updateClob(columnName, x);
    }

    public void updateClob(int columnIndex,
                       Clob x)
                throws SQLException
    {
         active();
        FResultSet.updateClob(columnIndex, x);
    }

    public void updateBlob(String columnName,
                        Blob x)
                 throws SQLException
    {
        active();
        FResultSet.updateBlob(columnName, x);
    }

    public void updateBlob(int columnIndex,
                       Blob x)
                throws SQLException
    {
        active();
        FResultSet.updateBlob(columnIndex, x);
    }

    public void updateCharacterStream(int Index, Reader AReader, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateCharacterStream(Index, AReader, Aint);
    }

    public void updateObject(int Index, Object AObject, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateObject(Index, AObject, Aint);
    }

    public void updateObject(int Index, Object Value)
        throws SQLException
    {
        active();
        FResultSet.updateObject(Index, Value);
    }

    public void updateNull(String FieldName)
        throws SQLException
    {
        active();
        FResultSet.updateNull(FieldName);
    }

    public void updateBoolean(String FieldName, boolean Value)
        throws SQLException
    {
        active();
        FResultSet.updateBoolean(FieldName, Value);
    }

    public void updateByte(String FieldName, byte Value)
        throws SQLException
    {
        active();
        FResultSet.updateByte(FieldName, Value);
    }

    public void updateShort(String FieldName, short Value)
        throws SQLException
    {
        active();
        FResultSet.updateShort(FieldName, Value);
    }

    public void updateInt(String FieldName, int Value)
        throws SQLException
    {
        active();
        FResultSet.updateInt(FieldName, Value);
    }

    public void updateLong(String FieldName, long Value)
        throws SQLException
    {
        active();
        FResultSet.updateLong(FieldName, Value);
    }

    public void updateFloat(String FieldName, float Value)
        throws SQLException
    {
        active();
        FResultSet.updateFloat(FieldName, Value);
    }

    public void updateDouble(String FieldName, double Value)
        throws SQLException
    {
        active();
        FResultSet.updateDouble(FieldName, Value);
    }

    public void updateBigDecimal(String FieldName, BigDecimal Value)
        throws SQLException
    {
        active();
        FResultSet.updateBigDecimal(FieldName, Value);
    }

    public void updateString(String FieldName, String Value)
        throws SQLException
    {
        active();
        FResultSet.updateString(FieldName, Value);
    }

    public void updateBytes(String FieldName, byte Value[])
        throws SQLException
    {
        active();
        FResultSet.updateBytes(FieldName, Value);
    }

    public void updateDate(String FieldName, Date Value)
        throws SQLException
    {
        active();
        FResultSet.updateDate(FieldName, Value);
    }

    public void updateTime(String FieldName, Time Value)
        throws SQLException
    {
        active();
        FResultSet.updateTime(FieldName, Value);
    }

    public void updateTimestamp(String FieldName, Timestamp Value)
        throws SQLException
    {
        active();
        FResultSet.updateTimestamp(FieldName, Value);
    }

    public void updateAsciiStream(String FieldName, InputStream Ainput, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateAsciiStream(FieldName, Ainput, Aint);
    }

    public void updateBinaryStream(String FieldName, InputStream Ainput, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateBinaryStream(FieldName, Ainput, Aint);
    }

    public void updateCharacterStream(String FieldName, Reader AReader, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateCharacterStream(FieldName, AReader, Aint);
    }

    public void updateObject(String FieldName, Object AObject, int Aint)
        throws SQLException
    {
        active();
        FResultSet.updateObject(FieldName, AObject, Aint);
    }

    public void updateObject(String FieldName, Object Value)
        throws SQLException
    {
        active();
        FResultSet.updateObject(FieldName, Value);
    }

    public void insertRow()
        throws SQLException
    {
        active();
        FResultSet.insertRow();
    }

    public void updateRow()
        throws SQLException
    {
        active();
        FResultSet.updateRow();
    }

    public void updateRef(String columnName,
                      Ref x)
               throws SQLException
    {
        active();
        FResultSet.updateRef(columnName, x);
    }

    public void updateRef(int columnIndex,
                      Ref x)
               throws SQLException
    {
        active();
        FResultSet.updateRef(columnIndex, x);
    }

    public void deleteRow()
        throws SQLException
    {
        active();
        FResultSet.deleteRow();
    }

    public void refreshRow()
        throws SQLException
    {
        active();
        FResultSet.refreshRow();
    }

    public void cancelRowUpdates()
        throws SQLException
    {
        active();
        FResultSet.cancelRowUpdates();
    }

    public void moveToInsertRow()
        throws SQLException
    {
        active();
        FResultSet.moveToInsertRow();
    }

    public void moveToCurrentRow()
        throws SQLException
    {
        active();
        FResultSet.moveToCurrentRow();
    }

    public Statement getStatement()
        throws SQLException
    {
        active();
        return FResultSet.getStatement();
    }


    public Ref getRef(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getRef(Index);
    }

    public Blob getBlob(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getBlob(Index);
    }

    public Clob getClob(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getClob(Index);
    }

    public Array getArray(int Index)
        throws SQLException
    {
        active();
        return FResultSet.getArray(Index);
    }
/*
    public Object getObject(String FieldName, Map AMap)
        throws SQLException
    {
        active();
        return FResultSet.getObject(FieldName, AMap);
    }
*/
    public Ref getRef(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getRef(FieldName);
    }

    public Blob getBlob(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getBlob(FieldName);
    }

    public Clob getClob(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getClob(FieldName);
    }

    public Array getArray(String FieldName)
        throws SQLException
    {
        active();
        return FResultSet.getArray(FieldName);
    }

    public Date getDate(int Index, Calendar ACalendar)
        throws SQLException
    {
        active();
        return FResultSet.getDate(Index, ACalendar);
    }

    public Date getDate(String FieldName, Calendar ACalendar)
        throws SQLException
    {
        active();
        return FResultSet.getDate(FieldName, ACalendar);
    }

    public Time getTime(int Index, Calendar ACalendar)
        throws SQLException
    {
        active();
        return FResultSet.getTime(Index, ACalendar);
    }

    public Time getTime(String FieldName, Calendar ACalendar)
        throws SQLException
    {
        active();
        return FResultSet.getTime(FieldName, ACalendar);
    }

    public Timestamp getTimestamp(int Index, Calendar ACalendar)
        throws SQLException
    {
        active();
        return FResultSet.getTimestamp(Index, ACalendar);
    }

    public Timestamp getTimestamp(String FieldName, Calendar ACalendar)
        throws SQLException
    {
        active();
        return FResultSet.getTimestamp(FieldName, ACalendar);
    }

	public Object getObject(String FieldName, Map<String, Class<?>> AMap) throws SQLException {
        active();
        return FResultSet.getObject(FieldName, AMap);
	}

	public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {
		// TODO 自动生成方法存根
		return null;
	}

	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String columnLabel, NClob clob) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNString(int columnIndex, String string)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNString(String columnLabel, String string)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
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
