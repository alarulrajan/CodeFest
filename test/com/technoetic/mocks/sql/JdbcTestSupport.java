package com.technoetic.mocks.sql;

import java.util.ArrayList;

/**
 * The Class JdbcTestSupport.
 */
public class JdbcTestSupport {
    
    /** The mock connection. */
    public MockConnection mockConnection;
    
    /** The mock prepared statement. */
    public MockPreparedStatement mockPreparedStatement;
    
    /** The mock statement. */
    public MockStatement mockStatement;
    
    /** The mock result set. */
    public MockResultSet mockResultSet;

    /** Instantiates a new jdbc test support.
     */
    public JdbcTestSupport() {
        mockConnection = new MockConnection();
        mockPreparedStatement = new MockPreparedStatement();
        mockConnection.prepareStatementReturn = mockPreparedStatement;
        mockStatement = new MockStatement();
        mockConnection.createStatementReturn = mockStatement;
        mockResultSet = new MockResultSet();
        mockResultSet.returnedRows = new ArrayList();
        mockPreparedStatement.executeQueryReturn = mockResultSet;
        mockStatement.executeQueryReturn = mockResultSet;
    }

}
