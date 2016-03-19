package com.technoetic.mocks.sql;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * The Class MockConnection.
 */
public class MockConnection implements Connection {
    
    /** The create statement called. */
    public boolean createStatementCalled;
    
    /** The create statement return. */
    public Statement createStatementReturn;
    
    /** The create statement sql exception. */
    public SQLException createStatementSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        createStatementCalled = true;
        if (createStatementSQLException != null) {
            throw createStatementSQLException;
        }
        return createStatementReturn;
    }

    /** The prepare statement called. */
    public boolean prepareStatementCalled;
    
    /** The prepare statement return. */
    public PreparedStatement prepareStatementReturn;
    
    /** The prepare statement return map. */
    public HashMap prepareStatementReturnMap;
    
    /** The prepare statement sql exception. */
    public SQLException prepareStatementSQLException;
    
    /** The prepare statement sql. */
    public java.lang.String prepareStatementSql;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(java.lang.String sql) throws SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        if (prepareStatementReturnMap != null) {
            return (PreparedStatement)prepareStatementReturnMap.get(sql);
        } else {
            return prepareStatementReturn;
        }
    }

    /** The prepare call called. */
    public boolean prepareCallCalled;
    
    /** The prepare call return. */
    public CallableStatement prepareCallReturn;
    
    /** The prepare call sql exception. */
    public SQLException prepareCallSQLException;
    
    /** The prepare call sql. */
    public java.lang.String prepareCallSql;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(java.lang.String sql) throws SQLException {
        prepareCallCalled = true;
        prepareCallSql = sql;
        if (prepareCallSQLException != null) {
            throw prepareCallSQLException;
        }
        return prepareCallReturn;
    }

    /** The native sql called. */
    public boolean nativeSQLCalled;
    
    /** The native sql return. */
    public java.lang.String nativeSQLReturn;
    
    /** The native sqlsql exception. */
    public SQLException nativeSQLSQLException;
    
    /** The native sql sql. */
    public java.lang.String nativeSQLSql;

    /* (non-Javadoc)
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public java.lang.String nativeSQL(java.lang.String sql) throws SQLException {
        nativeSQLCalled = true;
        nativeSQLSql = sql;
        if (nativeSQLSQLException != null) {
            throw nativeSQLSQLException;
        }
        return nativeSQLReturn;
    }

    /** The set auto commit called. */
    public boolean setAutoCommitCalled;
    
    /** The set auto commit sql exception. */
    public SQLException setAutoCommitSQLException;
    
    /** The set auto commit auto commit. */
    public boolean setAutoCommitAutoCommit;

    /* (non-Javadoc)
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        setAutoCommitCalled = true;
        setAutoCommitAutoCommit = autoCommit;
        if (setAutoCommitSQLException != null) {
            throw setAutoCommitSQLException;
        }
    }

    /** The get auto commit called. */
    public boolean getAutoCommitCalled;
    
    /** The get auto commit return. */
    public Boolean getAutoCommitReturn;
    
    /** The get auto commit sql exception. */
    public SQLException getAutoCommitSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {
        getAutoCommitCalled = true;
        if (getAutoCommitSQLException != null) {
            throw getAutoCommitSQLException;
        }
        return getAutoCommitReturn.booleanValue();
    }

    /** The commit called. */
    public boolean commitCalled;
    
    /** The commit sql exception. */
    public SQLException commitSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {
        commitCalled = true;
        if (commitSQLException != null) {
            throw commitSQLException;
        }
    }

    /** The rollback called. */
    public boolean rollbackCalled;
    
    /** The rollback sql exception. */
    public SQLException rollbackSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {
        rollbackCalled = true;
        if (rollbackSQLException != null) {
            throw rollbackSQLException;
        }
    }

    /** The close called. */
    public boolean closeCalled;
    
    /** The close sql exception. */
    public SQLException closeSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        closeCalled = true;
        if (closeSQLException != null) {
            throw closeSQLException;
        }
    }

    /** The is closed called. */
    public boolean isClosedCalled;
    
    /** The is closed return. */
    public Boolean isClosedReturn;
    
    /** The is closed sql exception. */
    public SQLException isClosedSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {
        isClosedCalled = true;
        if (isClosedSQLException != null) {
            throw isClosedSQLException;
        }
        return isClosedReturn.booleanValue();
    }

    /** The get meta data called. */
    public boolean getMetaDataCalled;
    
    /** The get meta data return. */
    public DatabaseMetaData getMetaDataReturn;
    
    /** The get meta data sql exception. */
    public SQLException getMetaDataSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        getMetaDataCalled = true;
        if (getMetaDataSQLException != null) {
            throw getMetaDataSQLException;
        }
        return getMetaDataReturn;
    }

    /** The set read only called. */
    public boolean setReadOnlyCalled;
    
    /** The set read only sql exception. */
    public SQLException setReadOnlySQLException;
    
    /** The set read only read only. */
    public boolean setReadOnlyReadOnly;

    /* (non-Javadoc)
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly) throws SQLException {
        setReadOnlyCalled = true;
        setReadOnlyReadOnly = readOnly;
        if (setReadOnlySQLException != null) {
            throw setReadOnlySQLException;
        }
    }

    /** The is read only called. */
    public boolean isReadOnlyCalled;
    
    /** The is read only return. */
    public Boolean isReadOnlyReturn;
    
    /** The is read only sql exception. */
    public SQLException isReadOnlySQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        isReadOnlyCalled = true;
        if (isReadOnlySQLException != null) {
            throw isReadOnlySQLException;
        }
        return isReadOnlyReturn.booleanValue();
    }

    /** The set catalog called. */
    public boolean setCatalogCalled;
    
    /** The set catalog sql exception. */
    public SQLException setCatalogSQLException;
    
    /** The set catalog catalog. */
    public java.lang.String setCatalogCatalog;

    /* (non-Javadoc)
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    public void setCatalog(java.lang.String catalog) throws SQLException {
        setCatalogCalled = true;
        setCatalogCatalog = catalog;
        if (setCatalogSQLException != null) {
            throw setCatalogSQLException;
        }
    }

    /** The get catalog called. */
    public boolean getCatalogCalled;
    
    /** The get catalog return. */
    public java.lang.String getCatalogReturn;
    
    /** The get catalog sql exception. */
    public SQLException getCatalogSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getCatalog()
     */
    public java.lang.String getCatalog() throws SQLException {
        getCatalogCalled = true;
        if (getCatalogSQLException != null) {
            throw getCatalogSQLException;
        }
        return getCatalogReturn;
    }

    /** The set transaction isolation called. */
    public boolean setTransactionIsolationCalled;
    
    /** The set transaction isolation sql exception. */
    public SQLException setTransactionIsolationSQLException;
    
    /** The set transaction isolation level. */
    public int setTransactionIsolationLevel;

    /* (non-Javadoc)
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(int level) throws SQLException {
        setTransactionIsolationCalled = true;
        setTransactionIsolationLevel = level;
        if (setTransactionIsolationSQLException != null) {
            throw setTransactionIsolationSQLException;
        }
    }

    /** The get transaction isolation called. */
    public boolean getTransactionIsolationCalled;
    
    /** The get transaction isolation return. */
    public Integer getTransactionIsolationReturn;
    
    /** The get transaction isolation sql exception. */
    public SQLException getTransactionIsolationSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {
        getTransactionIsolationCalled = true;
        if (getTransactionIsolationSQLException != null) {
            throw getTransactionIsolationSQLException;
        }
        return getTransactionIsolationReturn.intValue();
    }

    /** The get warnings called. */
    public boolean getWarningsCalled;
    
    /** The get warnings return. */
    public SQLWarning getWarningsReturn;
    
    /** The get warnings sql exception. */
    public SQLException getWarningsSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        getWarningsCalled = true;
        if (getWarningsSQLException != null) {
            throw getWarningsSQLException;
        }
        return getWarningsReturn;
    }

    /** The clear warnings called. */
    public boolean clearWarningsCalled;
    
    /** The clear warnings sql exception. */
    public SQLException clearWarningsSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        clearWarningsCalled = true;
        if (clearWarningsSQLException != null) {
            throw clearWarningsSQLException;
        }
    }

    /** The create statement result set type. */
    public int createStatementResultSetType;
    
    /** The create statement result set concurrency. */
    public int createStatementResultSetConcurrency;

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        createStatementCalled = true;
        createStatementResultSetType = resultSetType;
        createStatementResultSetConcurrency = resultSetConcurrency;
        if (createStatementSQLException != null) {
            throw createStatementSQLException;
        }
        return createStatementReturn;
    }

    /** The prepare statement2 result set type. */
    public int prepareStatement2ResultSetType;
    
    /** The prepare statement2 result set concurrency. */
    public int prepareStatement2ResultSetConcurrency;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
//        prepareStatementCalled = true;
//        prepareStatementSql = sql;
//        prepareStatementResultSetType = resultSetType;
//        prepareStatementResultSetConcurrency = resultSetConcurrency;
//        if (prepareStatementSQLException != null) {
//            throw prepareStatementSQLException;
//        }
//        return prepareStatementReturn;
    }

    /** The prepare call result set type. */
    public int prepareCallResultSetType;
    
    /** The prepare call result set concurrency. */
    public int prepareCallResultSetConcurrency;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        prepareCallCalled = true;
        prepareCallSql = sql;
        prepareCallResultSetType = resultSetType;
        prepareCallResultSetConcurrency = resultSetConcurrency;
        if (prepareCallSQLException != null) {
            throw prepareCallSQLException;
        }
        return prepareCallReturn;
    }

    /** The get type map called. */
    public boolean getTypeMapCalled;
    
    /** The get type map return. */
    public java.util.Map getTypeMapReturn;
    
    /** The get type map sql exception. */
    public SQLException getTypeMapSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getTypeMap()
     */
    public java.util.Map getTypeMap() throws SQLException {
        getTypeMapCalled = true;
        if (getTypeMapSQLException != null) {
            throw getTypeMapSQLException;
        }
        return getTypeMapReturn;
    }

    /** The set type map called. */
    public boolean setTypeMapCalled;
    
    /** The set type map sql exception. */
    public SQLException setTypeMapSQLException;
    
    /** The set type map map. */
    public java.util.Map setTypeMapMap;

    /* (non-Javadoc)
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    public void setTypeMap(java.util.Map map) throws SQLException {
        setTypeMapCalled = true;
        setTypeMapMap = map;
        if (setTypeMapSQLException != null) {
            throw setTypeMapSQLException;
        }
    }

    /** The set holdability called. */
    public boolean setHoldabilityCalled;
    
    /** The set holdability sql exception. */
    public java.sql.SQLException setHoldabilitySQLException;
    
    /** The set holdability holdability. */
    public int setHoldabilityHoldability;

    /* (non-Javadoc)
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(int holdability) throws java.sql.SQLException {
        setHoldabilityCalled = true;
        setHoldabilityHoldability = holdability;
        if (setHoldabilitySQLException != null) {
            throw setHoldabilitySQLException;
        }
    }

    /** The get holdability called. */
    public boolean getHoldabilityCalled;
    
    /** The get holdability return. */
    public Integer getHoldabilityReturn;
    
    /** The get holdability sql exception. */
    public java.sql.SQLException getHoldabilitySQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#getHoldability()
     */
    public int getHoldability() throws java.sql.SQLException {
        getHoldabilityCalled = true;
        if (getHoldabilitySQLException != null) {
            throw getHoldabilitySQLException;
        }
        return getHoldabilityReturn.intValue();
    }

    /** The set savepoint called. */
    public boolean setSavepointCalled;
    
    /** The set savepoint return. */
    public java.sql.Savepoint setSavepointReturn;
    
    /** The set savepoint sql exception. */
    public java.sql.SQLException setSavepointSQLException;

    /* (non-Javadoc)
     * @see java.sql.Connection#setSavepoint()
     */
    public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
        setSavepointCalled = true;
        if (setSavepointSQLException != null) {
            throw setSavepointSQLException;
        }
        return setSavepointReturn;
    }

    /** The set savepoint name. */
    public java.lang.String setSavepointName;

    /* (non-Javadoc)
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    public java.sql.Savepoint setSavepoint(java.lang.String name) throws java.sql.SQLException {
        setSavepointCalled = true;
        setSavepointName = name;
        if (setSavepointSQLException != null) {
            throw setSavepointSQLException;
        }
        return setSavepointReturn;
    }

    /** The rollback savepoint. */
    public java.sql.Savepoint rollbackSavepoint;

    /* (non-Javadoc)
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    public void rollback(java.sql.Savepoint savepoint) throws java.sql.SQLException {
        rollbackCalled = true;
        rollbackSavepoint = savepoint;
        if (rollbackSQLException != null) {
            throw rollbackSQLException;
        }
    }

    /** The release savepoint called. */
    public boolean releaseSavepointCalled;
    
    /** The release savepoint sql exception. */
    public java.sql.SQLException releaseSavepointSQLException;
    
    /** The release savepoint savepoint. */
    public java.sql.Savepoint releaseSavepointSavepoint;

    /* (non-Javadoc)
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    public void releaseSavepoint(java.sql.Savepoint savepoint) throws java.sql.SQLException {
        releaseSavepointCalled = true;
        releaseSavepointSavepoint = savepoint;
        if (releaseSavepointSQLException != null) {
            throw releaseSavepointSQLException;
        }
    }

    /** The create statement result set holdability. */
    public int createStatementResultSetHoldability;

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
        createStatementCalled = true;
        createStatementResultSetType = resultSetType;
        createStatementResultSetConcurrency = resultSetConcurrency;
        createStatementResultSetHoldability = resultSetHoldability;
        if (createStatementSQLException != null) {
            throw createStatementSQLException;
        }
        return createStatementReturn;
    }

    /** The prepare statement result set type. */
    public int prepareStatementResultSetType;
    
    /** The prepare statement result set concurrency. */
    public int prepareStatementResultSetConcurrency;
    
    /** The prepare statement result set holdability. */
    public int prepareStatementResultSetHoldability;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementResultSetType = resultSetType;
        prepareStatementResultSetConcurrency = resultSetConcurrency;
        prepareStatementResultSetHoldability = resultSetHoldability;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    /** The prepare call result set holdability. */
    public int prepareCallResultSetHoldability;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public java.sql.CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
        prepareCallCalled = true;
        prepareCallSql = sql;
        prepareCallResultSetType = resultSetType;
        prepareCallResultSetConcurrency = resultSetConcurrency;
        prepareCallResultSetHoldability = resultSetHoldability;
        if (prepareCallSQLException != null) {
            throw prepareCallSQLException;
        }
        return prepareCallReturn;
    }

    /** The prepare statement auto generated keys. */
    public int prepareStatementAutoGeneratedKeys;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementAutoGeneratedKeys = autoGeneratedKeys;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    /** The prepare statement column indexes. */
    public int[] prepareStatementColumnIndexes;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementColumnIndexes = columnIndexes;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    /** The prepare statement column names. */
    public java.lang.String[] prepareStatementColumnNames;

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementColumnNames = columnNames;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createClob()
     */
    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createBlob()
     */
    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createNClob()
     */
    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createSQLXML()
     */
    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#isValid(int)
     */
    public boolean isValid(int timeout) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
     */
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setClientInfo(java.util.Properties)
     */
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getClientInfo(java.lang.String)
     */
    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getClientInfo()
     */
    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
     */
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
     */
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
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
