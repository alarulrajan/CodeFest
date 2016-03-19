package com.technoetic.mocks.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

/**
 * The Class MockPreparedStatement.
 */
public class MockPreparedStatement implements PreparedStatement {
    
    /** The execute query called. */
    public boolean executeQueryCalled;
    
    /** The execute query return. */
    public ResultSet executeQueryReturn;

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        executeQueryCalled = true;
        return executeQueryReturn;
    }

    /** The execute update called. */
    public boolean executeUpdateCalled;
    
    /** The execute update return. */
    public Integer executeUpdateReturn;

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        executeUpdateCalled = true;
        return executeUpdateReturn.intValue();
    }

    /** The bound variables. */
    public Object[] boundVariables;

    /** Sets the bound variable count.
     *
     * @param i
     *            the new bound variable count
     */
    public void setBoundVariableCount(int i) {
        boundVariables = new Object[i + 1];
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        boundVariables[parameterIndex] = null;
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        boundVariables[parameterIndex] = new Boolean(x);
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setByte() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setShort() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        boundVariables[parameterIndex] = new Integer(x);
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        boundVariables[parameterIndex] = new Long(x);
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFloat() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDouble() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBigDecimal() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        boundVariables[parameterIndex] = x;
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(int parameterIndex, Date x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setAsciiStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
     */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setUnicodeStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBinaryStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearParameters() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setObject() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(int parameterIndex, Object value) throws SQLException {
        boundVariables[parameterIndex] = value;
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method addBatch() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setCharacterStream() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(int i, Ref x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setRef() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(int i, Blob x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBlob() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(int i, Clob x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setClob() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(int i, Array x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setArray() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMetaData() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDate() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTime() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTimestamp() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setNull() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeQuery() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeUpdate() not yet implemented.");
    }

    /** The close called. */
    public boolean closeCalled;
    
    /* (non-Javadoc)
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        closeCalled = true;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMaxFieldSize() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int max) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setMaxFieldSize() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMaxRows() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setMaxRows() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setEscapeProcessing() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getQueryTimeout() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setQueryTimeout() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method cancel() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getWarnings() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearWarnings() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(String name) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setCursorName() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getResultSet() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getUpdateCount() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMoreResults() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFetchDirection() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFetchDirection() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFetchSize() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFetchSize() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getResultSetConcurrency() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getResultSetType() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method addBatch() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearBatch() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeBatch() not yet implemented.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getConnection() not yet implemented.");
    }

    /** The set bytes called. */
    public boolean setBytesCalled;
    
    /** The set bytes sql exception. */
    public java.sql.SQLException setBytesSQLException;
    
    /** The set bytes parameter index. */
    public int setBytesParameterIndex;
    
    /** The set bytes x. */
    public byte[] setBytesX;

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(int parameterIndex, byte[] x) throws java.sql.SQLException {
        setBytesCalled = true;
        setBytesParameterIndex = parameterIndex;
        setBytesX = x;
        if (setBytesSQLException != null) {
            throw setBytesSQLException;
        }
    }

    /** The set url called. */
    public boolean setURLCalled;
    
    /** The set urlsql exception. */
    public java.sql.SQLException setURLSQLException;
    
    /** The set url parameter index. */
    public int setURLParameterIndex;
    
    /** The set urlx. */
    public java.net.URL setURLX;

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(int parameterIndex, java.net.URL x) throws java.sql.SQLException {
        setURLCalled = true;
        setURLParameterIndex = parameterIndex;
        setURLX = x;
        if (setURLSQLException != null) {
            throw setURLSQLException;
        }
    }

    /** The get parameter meta data called. */
    public boolean getParameterMetaDataCalled;
    
    /** The get parameter meta data return. */
    public java.sql.ParameterMetaData getParameterMetaDataReturn;
    
    /** The get parameter meta data sql exception. */
    public java.sql.SQLException getParameterMetaDataSQLException;

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public java.sql.ParameterMetaData getParameterMetaData() throws java.sql.SQLException {
        getParameterMetaDataCalled = true;
        if (getParameterMetaDataSQLException != null) {
            throw getParameterMetaDataSQLException;
        }
        return getParameterMetaDataReturn;
    }

    /** The get more results called. */
    public boolean getMoreResultsCalled;
    
    /** The get more results return. */
    public Boolean getMoreResultsReturn;
    
    /** The get more results sql exception. */
    public java.sql.SQLException getMoreResultsSQLException;
    
    /** The get more results current. */
    public int getMoreResultsCurrent;

    /* (non-Javadoc)
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(int current) throws java.sql.SQLException {
        getMoreResultsCalled = true;
        getMoreResultsCurrent = current;
        if (getMoreResultsSQLException != null) {
            throw getMoreResultsSQLException;
        }
        return getMoreResultsReturn.booleanValue();
    }

    /** The get generated keys called. */
    public boolean getGeneratedKeysCalled;
    
    /** The get generated keys return. */
    public java.sql.ResultSet getGeneratedKeysReturn;
    
    /** The get generated keys sql exception. */
    public java.sql.SQLException getGeneratedKeysSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public java.sql.ResultSet getGeneratedKeys() throws java.sql.SQLException {
        getGeneratedKeysCalled = true;
        if (getGeneratedKeysSQLException != null) {
            throw getGeneratedKeysSQLException;
        }
        return getGeneratedKeysReturn;
    }

    /** The execute update sql exception. */
    public java.sql.SQLException executeUpdateSQLException;
    
    /** The execute update sql. */
    public java.lang.String executeUpdateSql;
    
    /** The execute update auto generated keys. */
    public int executeUpdateAutoGeneratedKeys;

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        executeUpdateAutoGeneratedKeys = autoGeneratedKeys;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    /** The execute update column indexes. */
    public int[] executeUpdateColumnIndexes;

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        executeUpdateColumnIndexes = columnIndexes;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    /** The execute update column names. */
    public java.lang.String[] executeUpdateColumnNames;

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        executeUpdateColumnNames = columnNames;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    /** The execute called. */
    public boolean executeCalled;
    
    /** The execute return. */
    public Boolean executeReturn;
    
    /** The execute sql exception. */
    public java.sql.SQLException executeSQLException;
    
    /** The execute sql. */
    public java.lang.String executeSql;
    
    /** The execute auto generated keys. */
    public int executeAutoGeneratedKeys;

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeAutoGeneratedKeys = autoGeneratedKeys;
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    /** The execute column indexes. */
    public int[] executeColumnIndexes;

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeColumnIndexes = columnIndexes;
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    /** The execute column names. */
    public java.lang.String[] executeColumnNames;

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeColumnNames = columnNames;
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    /** The get result set holdability called. */
    public boolean getResultSetHoldabilityCalled;
    
    /** The get result set holdability return. */
    public Integer getResultSetHoldabilityReturn;
    
    /** The get result set holdability sql exception. */
    public java.sql.SQLException getResultSetHoldabilitySQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws java.sql.SQLException {
        getResultSetHoldabilityCalled = true;
        if (getResultSetHoldabilitySQLException != null) {
            throw getResultSetHoldabilitySQLException;
        }
        return getResultSetHoldabilityReturn.intValue();
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
     */
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
     */
    public void setNString(int parameterIndex, String value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
     */
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
     */
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
     */
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
     */
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
     */
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
     */
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
     */
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
     */
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
     */
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
     */
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
     */
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
     */
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#isClosed()
     */
    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setPoolable(boolean)
     */
    public void setPoolable(boolean poolable) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#isPoolable()
     */
    public boolean isPoolable() throws SQLException {
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