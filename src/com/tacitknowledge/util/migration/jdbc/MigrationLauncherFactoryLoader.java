package com.tacitknowledge.util.migration.jdbc;

public class MigrationLauncherFactoryLoader {
	public static JdbcMigrationLauncherFactory createFactory() {
		String factoryName = System.getProperties().getProperty(
				"migration.factory");
		if (factoryName == null) {
			factoryName = JdbcMigrationLauncherFactory.class.getName();
		}
		Class factoryClass = null;
		try {
			factoryClass = Class.forName(factoryName);
		} catch (final ClassNotFoundException e) {
			throw new IllegalArgumentException("Migration factory class '"
					+ factoryName + "' not found.  Aborting." + e.getMessage());
		}
		try {
			return (JdbcMigrationLauncherFactory) factoryClass.newInstance();
		} catch (final Exception e) {
			throw new RuntimeException(
					"Problem while instantiating factory class '" + factoryName
							+ "'.  Aborting.", e);
		}
	}
}