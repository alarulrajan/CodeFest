package com.technoetic.xplanner.db.hibernate;

import java.sql.Types;

import org.hibernate.dialect.MySQLDialect;

/**
 * The Class XPlannerMySQLDialect.
 */
public class XPlannerMySQLDialect extends MySQLDialect {
    
    /**
     * Instantiates a new x planner my sql dialect.
     */
    public XPlannerMySQLDialect() {
        this.registerColumnType(Types.BIT, "TINYINT(1)");
        this.registerColumnType(Types.TIMESTAMP, "DATETIME");
    }
}
