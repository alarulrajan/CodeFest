/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tacitknowledge.util.migration.MigrationException;

public class AutopatchSupport {
	private static final Logger log = Logger.getLogger(AutopatchSupport.class);
	private final JdbcMigrationLauncher launcher;

	public AutopatchSupport(final String systemName) throws MigrationException {
		this(MigrationLauncherFactoryLoader.createFactory(), systemName);
	}

	public AutopatchSupport(final JdbcMigrationLauncherFactory launcherFactory,
			final String systemName) throws MigrationException {
		this(launcherFactory.createMigrationLauncher(systemName));
	}

	public AutopatchSupport(final JdbcMigrationLauncher launcher) {
		this.launcher = launcher;
	}

	public PatchTable makePatchTable() throws SQLException {
		final JdbcMigrationContext jdbcMigrationContext = this.launcher
				.getJdbcMigrationContext();
		return new PatchTable(jdbcMigrationContext,
				jdbcMigrationContext.getConnection());
	}

	public void setPatchLevel(final int patchLevel) throws SQLException {
		final PatchTable patchTable = this.makePatchTable();
		patchTable.lockPatchTable();
		patchTable.updatePatchLevel(patchLevel);
		AutopatchSupport.log.info("Set the patch level to " + patchLevel);
		patchTable.unlockPatchTable();
	}

	public int getPatchLevel() throws SQLException {
		final PatchTable patchTable = this.makePatchTable();
		return patchTable.getPatchLevel();
	}

	public int getHighestPatchLevel() throws MigrationException {
		return this.launcher.getNextPatchLevel() - 1;
	}

}
