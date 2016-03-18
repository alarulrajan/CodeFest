package com.technoetic.xplanner.db.hibernate;

import java.sql.Types;

import org.hibernate.dialect.MySQLDialect;

public class XPlannerMySQLDialect extends MySQLDialect {
	public XPlannerMySQLDialect() {
		this.registerColumnType(Types.BIT, "TINYINT(1)");
		this.registerColumnType(Types.TIMESTAMP, "DATETIME");
	}
}
