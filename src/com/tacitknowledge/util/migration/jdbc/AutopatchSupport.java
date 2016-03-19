/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tacitknowledge.util.migration.MigrationException;

/**
 * The Class AutopatchSupport.
 */
public class AutopatchSupport {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(AutopatchSupport.class);
    
    /** The launcher. */
    private final JdbcMigrationLauncher launcher;

    /**
     * Instantiates a new autopatch support.
     *
     * @param systemName
     *            the system name
     * @throws MigrationException
     *             the migration exception
     */
    public AutopatchSupport(final String systemName) throws MigrationException {
        this(MigrationLauncherFactoryLoader.createFactory(), systemName);
    }

    /**
     * Instantiates a new autopatch support.
     *
     * @param launcherFactory
     *            the launcher factory
     * @param systemName
     *            the system name
     * @throws MigrationException
     *             the migration exception
     */
    public AutopatchSupport(final JdbcMigrationLauncherFactory launcherFactory,
            final String systemName) throws MigrationException {
        this(launcherFactory.createMigrationLauncher(systemName));
    }

    /**
     * Instantiates a new autopatch support.
     *
     * @param launcher
     *            the launcher
     */
    public AutopatchSupport(final JdbcMigrationLauncher launcher) {
        this.launcher = launcher;
    }

    /**
     * Make patch table.
     *
     * @return the patch table
     * @throws SQLException
     *             the SQL exception
     */
    public PatchTable makePatchTable() throws SQLException {
        final JdbcMigrationContext jdbcMigrationContext = this.launcher
                .getJdbcMigrationContext();
        return new PatchTable(jdbcMigrationContext,
                jdbcMigrationContext.getConnection());
    }

    /**
     * Sets the patch level.
     *
     * @param patchLevel
     *            the new patch level
     * @throws SQLException
     *             the SQL exception
     */
    public void setPatchLevel(final int patchLevel) throws SQLException {
        final PatchTable patchTable = this.makePatchTable();
        patchTable.lockPatchTable();
        patchTable.updatePatchLevel(patchLevel);
        AutopatchSupport.log.info("Set the patch level to " + patchLevel);
        patchTable.unlockPatchTable();
    }

    /**
     * Gets the patch level.
     *
     * @return the patch level
     * @throws SQLException
     *             the SQL exception
     */
    public int getPatchLevel() throws SQLException {
        final PatchTable patchTable = this.makePatchTable();
        return patchTable.getPatchLevel();
    }

    /**
     * Gets the highest patch level.
     *
     * @return the highest patch level
     * @throws MigrationException
     *             the migration exception
     */
    public int getHighestPatchLevel() throws MigrationException {
        return this.launcher.getNextPatchLevel() - 1;
    }

}
