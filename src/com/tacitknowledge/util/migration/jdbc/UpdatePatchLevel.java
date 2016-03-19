/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.db.hsqldb.HsqlServer;

/**
 * The Class UpdatePatchLevel.
 */
public class UpdatePatchLevel {
	
	/** The autopatch support. */
	private final AutopatchSupport autopatchSupport;

	/**
     * Instantiates a new update patch level.
     *
     * @param autopatchSupport
     *            the autopatch support
     */
	public UpdatePatchLevel(final AutopatchSupport autopatchSupport) {
		this.autopatchSupport = autopatchSupport;
	}

	/**
     * The main method.
     *
     * @param arguments
     *            the arguments
     * @throws Exception
     *             the exception
     */
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

	/**
     * Update patch level.
     *
     * @param migrationName
     *            the migration name
     * @param patchLevel
     *            the patch level
     * @throws Exception
     *             the exception
     */
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
