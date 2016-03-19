/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.HashMap;

import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionMapping;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.forms.PersonEditorForm;

/**
 * The Class TestEditPersonAction.
 */
public class TestEditPersonAction extends AbstractUnitTestCase {
   
   /** The mock editor form. */
   private PersonEditorForm mockEditorForm;
   
   /** The action. */
   private EditPersonAction action;
   
   /** The support. */
   private XPlannerTestSupport support;
   
   /** The mock person. */
   private Person mockPerson;
   
   /** The person id. */
   private int PERSON_ID = XPlannerTestSupport.DEFAULT_PERSON_ID;
   
   /** The mock edit person helper control. */
   private MockControl mockEditPersonHelperControl;
   
   /** The mock edit person helper. */
   private EditPersonHelper mockEditPersonHelper;
   
   /** The Constant PERSON_USER_ID. */
   static final String PERSON_USER_ID = "mock";

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      support = new XPlannerTestSupport();
      support.setUpSubjectInRole("xyz");
      mockEditorForm = new PersonEditorForm();
      mockEditorForm.reset(support.mapping, support.request);
      mockPerson = new Person(PERSON_USER_ID);
      mockPerson.setId(PERSON_ID);
      action = new EditPersonAction();
      mockEditPersonHelperControl = MockClassControl.createControl(EditPersonHelper.class);
      mockEditPersonHelper = (EditPersonHelper) mockEditPersonHelperControl.getMock();
      action.setEditPersonHelper(mockEditPersonHelper);
   }

   /** Test before object commit.
     *
     * @throws Exception
     *             the exception
     */
   public void testBeforeObjectCommit() throws Exception {
      mockEditPersonHelper.modifyRoles(new HashMap(), mockPerson, false, PERSON_ID);
      replay();
      action.beforeObjectCommit(mockPerson,
                                new ActionMapping(),
                                mockEditorForm,
                                support.request,
                                support.response);
      verify();
   }

   /** Test after object commit.
     *
     * @throws Exception
     *             the exception
     */
   public void testAfterObjectCommit() throws Exception {
      String newPassword = "xyz";
      mockEditorForm.setNewPassword(newPassword);
      mockEditPersonHelper.changeUserPassword(newPassword, null, null);
      replay();
      action.afterObjectCommit(new ActionMapping(),
                               mockEditorForm,
                               support.request,
                               support.response);
      verify();
   }

}

