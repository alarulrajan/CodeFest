/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.db.hsqldb.HsqlServer;

public class UpdatePatchLevel {
	private final AutopatchSupport autopatchSupport;

	public UpdatePatchLevel(final AutopatchSupport autopatchSupport) {
		this.autopatchSupport = autopatchSupport;
	}

	public static void main(final String[] arguments) throws Exception {
		final String migrationName = System.getProperty("migration.systemname");
		final AutopatchSupport autopatchSupport = new AutopatchSupport(
				migrationName);
		final UpdatePatchLevel dummyMigrationLauncher = new UpdatePatchLevel(
				autopatchSupport);
		String patchLevel = null;
		if (arguments != null && arguments.length > 0) {
			patchLevel = arguments[0].trim();
		}
		dummyMigrationLauncher.updatePatchLevel(migrationName, patchLevel);
		HsqlServer.shutdown();
	}

	protected void updatePatchLevel(final String migrationName,
			final String patchLevel) throws Exception {
		if (migrationName == null) {
			throw new IllegalArgumentException("The migration.systemname "
					+ "system property is required");
		}
		int patchLevelVal;

		if (StringUtils.isEmpty(patchLevel)) {
			patchLevelVal = this.autopatchSupport.getHighestPatchLevel();
		} else {
			patchLevelVal = Integer.parseInt(patchLevel);
		}
		this.autopatchSupport.setPatchLevel(patchLevelVal);
	}

}
