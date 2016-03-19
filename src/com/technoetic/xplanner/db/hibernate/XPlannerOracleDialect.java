package com.technoetic.xplanner.db.hibernate;

import java.sql.Types;

import org.hibernate.dialect.OracleDialect;

/**
 * The Class XPlannerOracleDialect.
 */
public class XPlannerOracleDialect extends OracleDialect {
	
	/**
     * Instantiates a new x planner oracle dialect.
     */
	public XPlannerOracleDialect() {
		super();
		this.registerColumnType(Types.VARBINARY, "LONG RAW");
	}
}
