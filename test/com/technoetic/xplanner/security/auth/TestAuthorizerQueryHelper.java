/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.auth;
/**
 * User: mprokopowicz
 * Date: Mar 30, 2006
 * Time: 4:19:03 PM
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Project;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * The Class TestAuthorizerQueryHelper.
 */
public class TestAuthorizerQueryHelper extends TestCase {
   
   /** The authorizer query helper. */
   private AuthorizerQueryHelper authorizerQueryHelper;
   
   /** The mock hibernate template control. */
   private MockControl mockHibernateTemplateControl;
   
   /** The mock hibernate template. */
   private HibernateTemplate mockHibernateTemplate;
   
   /** The permission1. */
   private Permission permission1;
   
   /** The permission2. */
   private Permission permission2;

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      permission1 = new Permission();
      permission2 = new Permission();
      permission1.setId(1);
      permission2.setId(2);
      authorizerQueryHelper = new AuthorizerQueryHelper();
      mockHibernateTemplateControl = MockClassControl.createControl(HibernateTemplate.class);
      mockHibernateTemplate = (HibernateTemplate) mockHibernateTemplateControl.getMock();
      authorizerQueryHelper.setHibernateTemplate(mockHibernateTemplate);
   }

   /** Test get all permissions.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAllPermissions() throws Exception {
      Collection permissionCol = new ArrayList();
      permissionCol.add(permission1);
      mockHibernateTemplateControl.expectAndReturn(
            mockHibernateTemplate.findByNamedQuery("security.personal.permissions"), permissionCol);
      mockHibernateTemplateControl.expectAndReturn(
            mockHibernateTemplate.findByNamedQuery("security.role.permissions"),
            Arrays.asList(new Object[]{permission2}));
      mockHibernateTemplateControl.replay();
      Collection expectedPermissionCol = authorizerQueryHelper.getAllPermissions();
      mockHibernateTemplateControl.verify();
      assertEquals(2, expectedPermissionCol.size());
   }

   /** Test get all permissions to person.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAllPermissionsToPerson() throws Exception {
      mockHibernateTemplateControl.expectAndReturn(mockHibernateTemplate.findByNamedQuery("security.person.permissions"),
                                                   Collections.EMPTY_LIST);
      mockHibernateTemplateControl.replay();
      authorizerQueryHelper.getAllPermissionsToPerson();
      mockHibernateTemplateControl.verify();
   }

   /** Test get roles for principal on project.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetRolesForPrincipalOnProject() throws Exception {
      mockHibernateTemplate.findByNamedQueryAndNamedParam("security.roles",
                                                          new String[]{
                                                                "personId",
                                                                "projectId",
                                                                "includeWildcardProject"},
                                                          new Object[]{new Integer(
                                                                1),
                                                                       new Integer(
                                                                             2),
                                                                       new Integer(
                                                                             1)});
      mockHibernateTemplateControl.setMatcher(MockControl.ARRAY_MATCHER);
      mockHibernateTemplateControl.setReturnValue(Collections.EMPTY_LIST);
      mockHibernateTemplateControl.replay();
      authorizerQueryHelper.getRolesForPrincipalOnProject(1, 2, true);
      mockHibernateTemplateControl.verify();
   }

   /** Test get all unhiden projects.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetAllUnhidenProjects() throws Exception {
      mockHibernateTemplate.find("from project in " + Project.class + " where project.hidden = false");
      mockHibernateTemplateControl.setReturnValue(Collections.EMPTY_LIST);
      mockHibernateTemplateControl.replay();
      authorizerQueryHelper.getAllUnhidenProjects();
      mockHibernateTemplateControl.verify();
   }
}