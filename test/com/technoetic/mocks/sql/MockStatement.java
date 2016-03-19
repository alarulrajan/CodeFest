package com.technoetic.mocks.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The Class MockStatement.
 */
public class MockStatement implements Statement {

    /** The execute query called. */
    public boolean executeQueryCalled;
    
    /** The execute query return. */
    public java.sql.ResultSet executeQueryReturn;
    
    /** The execute query sql exception. */
    public java.sql.SQLException executeQuerySQLException;
    
    /** The execute query sql. */
    public java.lang.String executeQuerySql;

    /* (non-Javadoc)
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public java.sql.ResultSet executeQuery(java.lang.String sql) throws java.sql.SQLException {
        executeQueryCalled = true;
        executeQuerySql = sql;
        if (executeQuerySQLException != null) {
            throw executeQuerySQLException;
        }
        return executeQueryReturn;
    }

    /** The execute update called. */
    public boolean executeUpdateCalled;
    
    /** The execute update return. */
    public Integer executeUpdateReturn;
    
    /** The execute update sql exception. */
    public java.sql.SQLException executeUpdateSQLException;
    
    /** The execute update sql. */
    public java.lang.String executeUpdateSql;

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(java.lang.String sql) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    /** The close called. */
    public boolean closeCalled;
    
    /** The close sql exception. */
    public java.sql.SQLException closeSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#close()
     */
    public void close() throws java.sql.SQLException {
        closeCalled = true;
        if (closeSQLException != null) {
            throw closeSQLException;
        }
    }

    /** The get max field size called. */
    public boolean getMaxFieldSizeCalled;
    
    /** The get max field size return. */
    public Integer getMaxFieldSizeReturn;
    
    /** The get max field size sql exception. */
    public java.sql.SQLException getMaxFieldSizeSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws java.sql.SQLException {
        getMaxFieldSizeCalled = true;
        if (getMaxFieldSizeSQLException != null) {
            throw getMaxFieldSizeSQLException;
        }
        return getMaxFieldSizeReturn.intValue();
    }

    /** The set max field size called. */
    public boolean setMaxFieldSizeCalled;
    
    /** The set max field size sql exception. */
    public java.sql.SQLException setMaxFieldSizeSQLException;
    
    /** The set max field size max. */
    public int setMaxFieldSizeMax;

    /* (non-Javadoc)
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int max) throws java.sql.SQLException {
        setMaxFieldSizeCalled = true;
        setMaxFieldSizeMax = max;
        if (setMaxFieldSizeSQLException != null) {
            throw setMaxFieldSizeSQLException;
        }
    }

    /** The get max rows called. */
    public boolean getMaxRowsCalled;
    
    /** The get max rows return. */
    public Integer getMaxRowsReturn;
    
    /** The get max rows sql exception. */
    public java.sql.SQLException getMaxRowsSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws java.sql.SQLException {
        getMaxRowsCalled = true;
        if (getMaxRowsSQLException != null) {
            throw getMaxRowsSQLException;
        }
        return getMaxRowsReturn.intValue();
    }

    /** The set max rows called. */
    public boolean setMaxRowsCalled;
    
    /** The set max rows sql exception. */
    public java.sql.SQLException setMaxRowsSQLException;
    
    /** The set max rows max. */
    public int setMaxRowsMax;

    /* (non-Javadoc)
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws java.sql.SQLException {
        setMaxRowsCalled = true;
        setMaxRowsMax = max;
        if (setMaxRowsSQLException != null) {
            throw setMaxRowsSQLException;
        }
    }

    /** The set escape processing called. */
    public boolean setEscapeProcessingCalled;
    
    /** The set escape processing sql exception. */
    public java.sql.SQLException setEscapeProcessingSQLException;
    
    /** The set escape processing enable. */
    public boolean setEscapeProcessingEnable;

    /* (non-Javadoc)
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws java.sql.SQLException {
        setEscapeProcessingCalled = true;
        setEscapeProcessingEnable = enable;
        if (setEscapeProcessingSQLException != null) {
            throw setEscapeProcessingSQLException;
        }
    }

    /** The get query timeout called. */
    public boolean getQueryTimeoutCalled;
    
    /** The get query timeout return. */
    public Integer getQueryTimeoutReturn;
    
    /** The get query timeout sql exception. */
    public java.sql.SQLException getQueryTimeoutSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws java.sql.SQLException {
        getQueryTimeoutCalled = true;
        if (getQueryTimeoutSQLException != null) {
            throw getQueryTimeoutSQLException;
        }
        return getQueryTimeoutReturn.intValue();
    }

    /** The set query timeout called. */
    public boolean setQueryTimeoutCalled;
    
    /** The set query timeout sql exception. */
    public java.sql.SQLException setQueryTimeoutSQLException;
    
    /** The set query timeout seconds. */
    public int setQueryTimeoutSeconds;

    /* (non-Javadoc)
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws java.sql.SQLException {
        setQueryTimeoutCalled = true;
        setQueryTimeoutSeconds = seconds;
        if (setQueryTimeoutSQLException != null) {
            throw setQueryTimeoutSQLException;
        }
    }

    /** The cancel called. */
    public boolean cancelCalled;
    
    /** The cancel sql exception. */
    public java.sql.SQLException cancelSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws java.sql.SQLException {
        cancelCalled = true;
        if (cancelSQLException != null) {
            throw cancelSQLException;
        }
    }

    /** The get warnings called. */
    public boolean getWarningsCalled;
    
    /** The get warnings return. */
    public java.sql.SQLWarning getWarningsReturn;
    
    /** The get warnings sql exception. */
    public java.sql.SQLException getWarningsSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getWarnings()
     */
    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        getWarningsCalled = true;
        if (getWarningsSQLException != null) {
            throw getWarningsSQLException;
        }
        return getWarningsReturn;
    }

    /** The clear warnings called. */
    public boolean clearWarningsCalled;
    
    /** The clear warnings sql exception. */
    public java.sql.SQLException clearWarningsSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws java.sql.SQLException {
        clearWarningsCalled = true;
        if (clearWarningsSQLException != null) {
            throw clearWarningsSQLException;
        }
    }

    /** The set cursor name called. */
    public boolean setCursorNameCalled;
    
    /** The set cursor name sql exception. */
    public java.sql.SQLException setCursorNameSQLException;
    
    /** The set cursor name name. */
    public java.lang.String setCursorNameName;

    /* (non-Javadoc)
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(java.lang.String name) throws java.sql.SQLException {
        setCursorNameCalled = true;
        setCursorNameName = name;
        if (setCursorNameSQLException != null) {
            throw setCursorNameSQLException;
        }
    }

    /** The execute called. */
    public boolean executeCalled;
    
    /** The execute return. */
    public Boolean executeReturn;
    
    /** The execute sql exception. */
    public java.sql.SQLException executeSQLException;
    
    /** The execute sql. */
    public java.lang.String executeSql;
    
    /** The execute sql list. */
    public ArrayList executeSqlList = new ArrayList();

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(java.lang.String sql) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeSqlList.add(sql);
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    /** The get result set called. */
    public boolean getResultSetCalled;
    
    /** The get result set return. */
    public java.sql.ResultSet getResultSetReturn;
    
    /** The get result set sql exception. */
    public java.sql.SQLException getResultSetSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSet()
     */
    public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
        getResultSetCalled = true;
        if (getResultSetSQLException != null) {
            throw getResultSetSQLException;
        }
        return getResultSetReturn;
    }

    /** The get update count called. */
    public boolean getUpdateCountCalled;
    
    /** The get update count return. */
    public Integer getUpdateCountReturn;
    
    /** The get update count sql exception. */
    public java.sql.SQLException getUpdateCountSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws java.sql.SQLException {
        getUpdateCountCalled = true;
        if (getUpdateCountSQLException != null) {
            throw getUpdateCountSQLException;
        }
        return getUpdateCountReturn.intValue();
    }

    /** The get more results called. */
    public boolean getMoreResultsCalled;
    
    /** The get more results return. */
    public Boolean getMoreResultsReturn;
    
    /** The get more results sql exception. */
    public java.sql.SQLException getMoreResultsSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws java.sql.SQLException {
        getMoreResultsCalled = true;
        if (getMoreResultsSQLException != null) {
            throw getMoreResultsSQLException;
        }
        return getMoreResultsReturn.booleanValue();
    }

    /** The set fetch direction called. */
    public boolean setFetchDirectionCalled;
    
    /** The set fetch direction sql exception. */
    public java.sql.SQLException setFetchDirectionSQLException;
    
    /** The set fetch direction direction. */
    public int setFetchDirectionDirection;

    /* (non-Javadoc)
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws java.sql.SQLException {
        setFetchDirectionCalled = true;
        setFetchDirectionDirection = direction;
        if (setFetchDirectionSQLException != null) {
            throw setFetchDirectionSQLException;
        }
    }

    /** The get fetch direction called. */
    public boolean getFetchDirectionCalled;
    
    /** The get fetch direction return. */
    public Integer getFetchDirectionReturn;
    
    /** The get fetch direction sql exception. */
    public java.sql.SQLException getFetchDirectionSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws java.sql.SQLException {
        getFetchDirectionCalled = true;
        if (getFetchDirectionSQLException != null) {
            throw getFetchDirectionSQLException;
        }
        return getFetchDirectionReturn.intValue();
    }

    /** The set fetch size called. */
    public boolean setFetchSizeCalled;
    
    /** The set fetch size sql exception. */
    public java.sql.SQLException setFetchSizeSQLException;
    
    /** The set fetch size rows. */
    public int setFetchSizeRows;

    /* (non-Javadoc)
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws java.sql.SQLException {
        setFetchSizeCalled = true;
        setFetchSizeRows = rows;
        if (setFetchSizeSQLException != null) {
            throw setFetchSizeSQLException;
        }
    }

    /** The get fetch size called. */
    public boolean getFetchSizeCalled;
    
    /** The get fetch size return. */
    public Integer getFetchSizeReturn;
    
    /** The get fetch size sql exception. */
    public java.sql.SQLException getFetchSizeSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws java.sql.SQLException {
        getFetchSizeCalled = true;
        if (getFetchSizeSQLException != null) {
            throw getFetchSizeSQLException;
        }
        return getFetchSizeReturn.intValue();
    }

    /** The get result set concurrency called. */
    public boolean getResultSetConcurrencyCalled;
    
    /** The get result set concurrency return. */
    public Integer getResultSetConcurrencyReturn;
    
    /** The get result set concurrency sql exception. */
    public java.sql.SQLException getResultSetConcurrencySQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws java.sql.SQLException {
        getResultSetConcurrencyCalled = true;
        if (getResultSetConcurrencySQLException != null) {
            throw getResultSetConcurrencySQLException;
        }
        return getResultSetConcurrencyReturn.intValue();
    }

    /** The get result set type called. */
    public boolean getResultSetTypeCalled;
    
    /** The get result set type return. */
    public Integer getResultSetTypeReturn;
    
    /** The get result set type sql exception. */
    public java.sql.SQLException getResultSetTypeSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws java.sql.SQLException {
        getResultSetTypeCalled = true;
        if (getResultSetTypeSQLException != null) {
            throw getResultSetTypeSQLException;
        }
        return getResultSetTypeReturn.intValue();
    }

    /** The add batch called. */
    public boolean addBatchCalled;
    
    /** The add batch sql exception. */
    public java.sql.SQLException addBatchSQLException;
    
    /** The add batch sql. */
    public java.lang.String addBatchSql;

    /* (non-Javadoc)
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(java.lang.String sql) throws java.sql.SQLException {
        addBatchCalled = true;
        addBatchSql = sql;
        if (addBatchSQLException != null) {
            throw addBatchSQLException;
        }
    }

    /** The clear batch called. */
    public boolean clearBatchCalled;
    
    /** The clear batch sql exception. */
    public java.sql.SQLException clearBatchSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws java.sql.SQLException {
        clearBatchCalled = true;
        if (clearBatchSQLException != null) {
            throw clearBatchSQLException;
        }
    }

    /** The execute batch called. */
    public boolean executeBatchCalled;
    
    /** The execute batch return. */
    public int[] executeBatchReturn;
    
    /** The execute batch sql exception. */
    public java.sql.SQLException executeBatchSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws java.sql.SQLException {
        executeBatchCalled = true;
        if (executeBatchSQLException != null) {
            throw executeBatchSQLException;
        }
        return executeBatchReturn;
    }

    /** The get connection called. */
    public boolean getConnectionCalled;
    
    /** The get connection return. */
    public java.sql.Connection getConnectionReturn;
    
    /** The get connection sql exception. */
    public java.sql.SQLException getConnectionSQLException;

    /* (non-Javadoc)
     * @see java.sql.Statement#getConnection()
     */
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        getConnectionCalled = true;
        if (getConnectionSQLException != null) {
            throw getConnectionSQLException;
        }
        return getConnectionReturn;
    }

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