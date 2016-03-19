/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.domain.IterationStatus;

/**
 * The Class IterationStatusIntegrationTestScript.
 */
public class IterationStatusIntegrationTestScript extends AbstractDatabaseTestScript {
   
   /** Test.
     *
     * @throws Exception
     *             the exception
     */
   public void test() throws Exception {
      Project project = newProject();
      Iteration iteration = newIteration(project);
      commitCloseAndOpenSession();

      iteration.setIterationStatus(IterationStatus.ACTIVE);
      getSession().save(iteration);
      assertEquals(IterationStatus.ACTIVE, iteration.getStatus());
      
      commitCloseAndOpenSession();

      Iteration toIt = (Iteration) getSession().load(Iteration.class, new Integer(iteration.getId()));
      assertNotSame(iteration, toIt);
      assertEquals(IterationStatus.ACTIVE, toIt.getStatus());
   }

}