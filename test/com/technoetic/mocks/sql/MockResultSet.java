package com.technoetic.mocks.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class MockResultSet.
 */
public class MockResultSet implements ResultSet {
    
    /** The returned rows. */
    public ArrayList returnedRows;
    
    /** The column map. */
    public HashMap columnMap;
    
    /** The current row. */
    private List currentRow;

    /** The next called. */
    public boolean nextCalled;
    
    /** The next called count. */
    public int nextCalledCount;
    
    /** The next return. */
    public Boolean nextReturn;
    
    /** The next sql exception. */
    public java.sql.SQLException nextSQLException;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#next()
     */
    public boolean next() throws java.sql.SQLException {
        nextCalled = true;
        nextCalledCount++;
        if (nextSQLException != null) {
            throw nextSQLException;
        }
        if (nextReturn != null || returnedRows == null) {
            return nextReturn.booleanValue();
        } else {
            if (nextCalledCount <= returnedRows.size()) {
                currentRow = (List)returnedRows.get(nextCalledCount - 1);
                return true;
            } else {
                return false;
            }
        }
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#wasNull()
     */
    public boolean wasNull() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method wasNull() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getString(int)
     */
    public String getString(int columnIndex) throws SQLException {
        return (String)currentRow.get(columnIndex-1);
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBoolean(int)
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBoolean() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getByte(int)
     */
    public byte getByte(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getByte() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getShort(int)
     */
    public short getShort(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getShort() not yet implemented.");
    }


    /* (non-Javadoc)
     * @see java.sql.ResultSet#getInt(int)
     */
    public int getInt(int columnIndex) throws SQLException {
        return ((Integer)currentRow.get(columnIndex - 1)).intValue();
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getLong(int)
     */
    public long getLong(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getLong() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getFloat(int)
     */
    public float getFloat(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFloat() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getDouble(int)
     */
    public double getDouble(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDouble() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBigDecimal(int, int)
     */
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBytes(int)
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBytes() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getDate(int)
     */
    public Date getDate(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTime(int)
     */
    public Time getTime(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTimestamp(int)
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getAsciiStream(int)
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getAsciiStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getUnicodeStream(int)
     */
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getUnicodeStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBinaryStream(int)
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBinaryStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getString(java.lang.String)
     */
    public String getString(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getString() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBoolean() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getByte(java.lang.String)
     */
    public byte getByte(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getByte() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getShort(java.lang.String)
     */
    public short getShort(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getShort() not yet implemented.");
    }

    /** Gets the column value.
     *
     * @param columnName
     *            the column name
     * @return the column value
     */
    private Object getColumnValue(String columnName) {
        return currentRow.get(((Integer)columnMap.get(columnName)).intValue() - 1);
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getInt(java.lang.String)
     */
    public int getInt(String columnName) throws SQLException {
        return ((Integer)getColumnValue(columnName)).intValue();
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getLong(java.lang.String)
     */
    public long getLong(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getLong() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getFloat(java.lang.String)
     */
    public float getFloat(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFloat() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getDouble(java.lang.String)
     */
    public double getDouble(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDouble() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
     */
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBytes(java.lang.String)
     */
    public byte[] getBytes(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBytes() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getDate(java.lang.String)
     */
    public Date getDate(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTime(java.lang.String)
     */
    public Time getTime(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
     */
    public InputStream getAsciiStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getAsciiStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
     */
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getUnicodeStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
     */
    public InputStream getBinaryStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBinaryStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getWarnings() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearWarnings() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getCursorName()
     */
    public String getCursorName() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getCursorName() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMetaData() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getObject(int)
     */
    public Object getObject(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getObject(java.lang.String)
     */
    public Object getObject(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#findColumn(java.lang.String)
     */
    public int findColumn(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method findColumn() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getCharacterStream(int)
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getCharacterStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
     */
    public Reader getCharacterStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getCharacterStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#isBeforeFirst()
     */
    public boolean isBeforeFirst() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isBeforeFirst() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#isAfterLast()
     */
    public boolean isAfterLast() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isAfterLast() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#isFirst()
     */
    public boolean isFirst() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isFirst() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#isLast()
     */
    public boolean isLast() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isLast() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#beforeFirst()
     */
    public void beforeFirst() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method beforeFirst() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#afterLast()
     */
    public void afterLast() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method afterLast() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#first()
     */
    public boolean first() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method first() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#last()
     */
    public boolean last() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method last() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getRow()
     */
    public int getRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#absolute(int)
     */
    public boolean absolute(int row) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method absolute() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#relative(int)
     */
    public boolean relative(int rows) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method relative() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#previous()
     */
    public boolean previous() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method previous() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFetchDirection() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFetchDirection() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFetchSize() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFetchSize() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getType()
     */
    public int getType() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getType() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getConcurrency()
     */
    public int getConcurrency() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getConcurrency() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#rowUpdated()
     */
    public boolean rowUpdated() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method rowUpdated() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#rowInserted()
     */
    public boolean rowInserted() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method rowInserted() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#rowDeleted()
     */
    public boolean rowDeleted() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method rowDeleted() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNull(int)
     */
    public void updateNull(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateNull() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBoolean(int, boolean)
     */
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBoolean() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateByte(int, byte)
     */
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateByte() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateShort(int, short)
     */
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateShort() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateInt(int, int)
     */
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInt() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateLong(int, long)
     */
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateLong() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateFloat(int, float)
     */
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateFloat() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateDouble(int, double)
     */
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDouble() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
     */
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateString(int, java.lang.String)
     */
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateString() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
     */
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
     */
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
     */
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
     */
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateAsciiStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
     */
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBinaryStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
     */
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateCharacterStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
     */
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
     */
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNull(java.lang.String)
     */
    public void updateNull(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateNull() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
     */
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBoolean() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
     */
    public void updateByte(String columnName, byte x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateByte() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateShort(java.lang.String, short)
     */
    public void updateShort(String columnName, short x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateShort() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateInt(java.lang.String, int)
     */
    public void updateInt(String columnName, int x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInt() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateLong(java.lang.String, long)
     */
    public void updateLong(String columnName, long x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateLong() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
     */
    public void updateFloat(String columnName, float x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateFloat() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
     */
    public void updateDouble(String columnName, double x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDouble() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
     */
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
     */
    public void updateString(String columnName, String x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateString() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
     */
    public void updateDate(String columnName, Date x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
     */
    public void updateTime(String columnName, Time x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
     */
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
     */
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateAsciiStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
     */
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBinaryStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
     */
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateCharacterStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
     */
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
     */
    public void updateObject(String columnName, Object x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#insertRow()
     */
    public void insertRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method insertRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateRow()
     */
    public void updateRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#deleteRow()
     */
    public void deleteRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method deleteRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#refreshRow()
     */
    public void refreshRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method refreshRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#cancelRowUpdates()
     */
    public void cancelRowUpdates() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method cancelRowUpdates() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#moveToInsertRow()
     */
    public void moveToInsertRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method moveToInsertRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#moveToCurrentRow()
     */
    public void moveToCurrentRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method moveToCurrentRow() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getStatement()
     */
    public Statement getStatement() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getStatement() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getObject(int, java.util.Map)
     */
    public Object getObject(int i, Map map) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getRef(int)
     */
    public Ref getRef(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getRef() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBlob(int)
     */
    public Blob getBlob(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBlob() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getClob(int)
     */
    public Clob getClob(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getClob() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getArray(int)
     */
    public Array getArray(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getArray() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
     */
    public Object getObject(String colName, Map map) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getRef(java.lang.String)
     */
    public Ref getRef(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getRef() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getBlob(java.lang.String)
     */
    public Blob getBlob(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBlob() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getClob(java.lang.String)
     */
    public Clob getClob(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getClob() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getArray(java.lang.String)
     */
    public Array getArray(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getArray() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
     */
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
     */
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
     */
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    /** The close called. */
    public boolean closeCalled;
    
    /** The close sql exception. */
    public java.sql.SQLException closeSQLException;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#close()
     */
    public void close() throws java.sql.SQLException {
        closeCalled = true;
        if (closeSQLException != null) {
            throw closeSQLException;
        }
    }

    /** The update bytes called. */
    public boolean updateBytesCalled;
    
    /** The update bytes sql exception. */
    public java.sql.SQLException updateBytesSQLException;
    
    /** The update bytes column index. */
    public int updateBytesColumnIndex;
    
    /** The update bytes x. */
    public byte[] updateBytesX;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBytes(int, byte[])
     */
    public void updateBytes(int columnIndex, byte[] x) throws java.sql.SQLException {
        updateBytesCalled = true;
        updateBytesColumnIndex = columnIndex;
        updateBytesX = x;
        if (updateBytesSQLException != null) {
            throw updateBytesSQLException;
        }
    }

    /** The update bytes2 called. */
    public boolean updateBytes2Called;
    
    /** The update bytes2 sql exception. */
    public java.sql.SQLException updateBytes2SQLException;
    
    /** The update bytes2 column name. */
    public String updateBytes2ColumnName;
    
    /** The update bytes2 x. */
    public byte[] updateBytes2X;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
     */
    public void updateBytes(java.lang.String columnName, byte[] x) throws java.sql.SQLException {
        updateBytes2Called = true;
        updateBytes2ColumnName = columnName;
        updateBytes2X = x;
        if (updateBytes2SQLException != null) {
            throw updateBytes2SQLException;
        }
    }

    /** The get url called. */
    public boolean getURLCalled;
    
    /** The get url return. */
    public java.net.URL getURLReturn;
    
    /** The get urlsql exception. */
    public java.sql.SQLException getURLSQLException;
    
    /** The get url column index. */
    public int getURLColumnIndex;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getURL(int)
     */
    public java.net.URL getURL(int columnIndex) throws java.sql.SQLException {
        getURLCalled = true;
        getURLColumnIndex = columnIndex;
        if (getURLSQLException != null) {
            throw getURLSQLException;
        }
        return getURLReturn;
    }

    /** The get url column name. */
    public java.lang.String getURLColumnName;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getURL(java.lang.String)
     */
    public java.net.URL getURL(java.lang.String columnName) throws java.sql.SQLException {
        getURLCalled = true;
        getURLColumnName = columnName;
        if (getURLSQLException != null) {
            throw getURLSQLException;
        }
        return getURLReturn;
    }

    /** The update ref called. */
    public boolean updateRefCalled;
    
    /** The update ref sql exception. */
    public java.sql.SQLException updateRefSQLException;
    
    /** The update ref column index. */
    public int updateRefColumnIndex;
    
    /** The update ref x. */
    public java.sql.Ref updateRefX;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
     */
    public void updateRef(int columnIndex, java.sql.Ref x) throws java.sql.SQLException {
        updateRefCalled = true;
        updateRefColumnIndex = columnIndex;
        updateRefX = x;
        if (updateRefSQLException != null) {
            throw updateRefSQLException;
        }
    }

    /** The update ref column name. */
    public java.lang.String updateRefColumnName;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
     */
    public void updateRef(java.lang.String columnName, java.sql.Ref x) throws java.sql.SQLException {
        updateRefCalled = true;
        updateRefColumnName = columnName;
        updateRefX = x;
        if (updateRefSQLException != null) {
            throw updateRefSQLException;
        }
    }

    /** The update blob called. */
    public boolean updateBlobCalled;
    
    /** The update blob sql exception. */
    public java.sql.SQLException updateBlobSQLException;
    
    /** The update blob column index. */
    public int updateBlobColumnIndex;
    
    /** The update blob x. */
    public java.sql.Blob updateBlobX;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
     */
    public void updateBlob(int columnIndex, java.sql.Blob x) throws java.sql.SQLException {
        updateBlobCalled = true;
        updateBlobColumnIndex = columnIndex;
        updateBlobX = x;
        if (updateBlobSQLException != null) {
            throw updateBlobSQLException;
        }
    }

    /** The update blob column name. */
    public java.lang.String updateBlobColumnName;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
     */
    public void updateBlob(java.lang.String columnName, java.sql.Blob x) throws java.sql.SQLException {
        updateBlobCalled = true;
        updateBlobColumnName = columnName;
        updateBlobX = x;
        if (updateBlobSQLException != null) {
            throw updateBlobSQLException;
        }
    }

    /** The update clob called. */
    public boolean updateClobCalled;
    
    /** The update clob sql exception. */
    public java.sql.SQLException updateClobSQLException;
    
    /** The update clob column index. */
    public int updateClobColumnIndex;
    
    /** The update clob x. */
    public java.sql.Clob updateClobX;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
     */
    public void updateClob(int columnIndex, java.sql.Clob x) throws java.sql.SQLException {
        updateClobCalled = true;
        updateClobColumnIndex = columnIndex;
        updateClobX = x;
        if (updateClobSQLException != null) {
            throw updateClobSQLException;
        }
    }

    /** The update clob column name. */
    public java.lang.String updateClobColumnName;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
     */
    public void updateClob(java.lang.String columnName, java.sql.Clob x) throws java.sql.SQLException {
        updateClobCalled = true;
        updateClobColumnName = columnName;
        updateClobX = x;
        if (updateClobSQLException != null) {
            throw updateClobSQLException;
        }
    }

    /** The update array called. */
    public boolean updateArrayCalled;
    
    /** The update array sql exception. */
    public java.sql.SQLException updateArraySQLException;
    
    /** The update array column index. */
    public int updateArrayColumnIndex;
    
    /** The update array x. */
    public java.sql.Array updateArrayX;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
     */
    public void updateArray(int columnIndex, java.sql.Array x) throws java.sql.SQLException {
        updateArrayCalled = true;
        updateArrayColumnIndex = columnIndex;
        updateArrayX = x;
        if (updateArraySQLException != null) {
            throw updateArraySQLException;
        }
    }

    /** The update array column name. */
    public java.lang.String updateArrayColumnName;

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
     */
    public void updateArray(java.lang.String columnName, java.sql.Array x) throws java.sql.SQLException {
        updateArrayCalled = true;
        updateArrayColumnName = columnName;
        updateArrayX = x;
        if (updateArraySQLException != null) {
            throw updateArraySQLException;
        }
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getRowId(int)
     */
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getRowId(java.lang.String)
     */
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
     */
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateRowId(java.lang.String, java.sql.RowId)
     */
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getHoldability()
     */
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#isClosed()
     */
    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNString(int, java.lang.String)
     */
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNString(java.lang.String, java.lang.String)
     */
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
     */
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNClob(java.lang.String, java.sql.NClob)
     */
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getNClob(int)
     */
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getNClob(java.lang.String)
     */
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getSQLXML(int)
     */
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getSQLXML(java.lang.String)
     */
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
     */
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateSQLXML(java.lang.String, java.sql.SQLXML)
     */
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getNString(int)
     */
    public String getNString(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getNString(java.lang.String)
     */
    public String getNString(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getNCharacterStream(int)
     */
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
     */
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
     */
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String, java.io.Reader, long)
     */
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
     */
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, long)
     */
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
     */
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, long)
     */
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, long)
     */
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, long)
     */
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
     */
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream, long)
     */
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
     */
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader, long)
     */
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
     */
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader, long)
     */
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
     */
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String, java.io.Reader)
     */
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
     */
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
     */
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
     */
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream)
     */
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream)
     */
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader)
     */
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
     */
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream)
     */
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
     */
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader)
     */
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
     */
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader)
     */
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}