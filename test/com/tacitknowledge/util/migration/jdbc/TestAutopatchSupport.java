/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

/**
 * The Class TestAutopatchSupport.
 */
public class TestAutopatchSupport extends TestCase {
   
   /** The autopatch support. */
   private AutopatchSupport autopatchSupport;
   
   /** The mock patch table control. */
   private MockControl mockPatchTableControl;
   
   /** The mock patch table. */
   private PatchTable mockPatchTable;

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      mockPatchTableControl = MockClassControl.createControl(PatchTable.class);
      mockPatchTable = (PatchTable) mockPatchTableControl.getMock();
      autopatchSupport = new AutopatchSupport(new JdbcMigrationLauncher()) {
         public PatchTable makePatchTable() {
            return mockPatchTable;
         }
      };
   }

   /** Test set patch level.
     *
     * @throws Exception
     *             the exception
     */
   public void testSetPatchLevel() throws Exception {
      mockPatchTable.lockPatchTable();
      mockPatchTable.updatePatchLevel(2);
      mockPatchTable.unlockPatchTable();
      mockPatchTableControl.replay();
      autopatchSupport.setPatchLevel(2);
      mockPatchTableControl.verify();
   }
}