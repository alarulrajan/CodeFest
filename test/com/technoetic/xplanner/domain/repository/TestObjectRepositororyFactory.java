/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;
/**
 * User: mprokopowicz
 * Date: Feb 8, 2006
 * Time: 1:15:54 PM
 */

import java.util.Arrays;
import java.util.List;

import net.sf.xplanner.domain.Project;

import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.technoetic.xplanner.AbstractUnitTestCase;

/**
 * A factory for creating TestObjectRepositorory objects.
 */
public class TestObjectRepositororyFactory extends AbstractUnitTestCase {
   
   /** The object repositorory factory. */
   private ObjectRepositororyFactory objectRepositororyFactory;
   
   /** The mock bean factory control. */
   private MockControl mockBeanFactoryControl;
   
   /** The mock bean factory. */
   private AutowireCapableBeanFactory mockBeanFactory;
   
   /** The delegates chain. */
   private List delegatesChain;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      mockBeanFactoryControl = MockControl.createControl(AutowireCapableBeanFactory.class);
      mockBeanFactory = (AutowireCapableBeanFactory) mockBeanFactoryControl.getMock();
      objectRepositororyFactory = new ObjectRepositororyFactory();
      delegatesChain = Arrays.asList(new Class[]{RepositoryHistoryAdapter.class, RepositorySecurityAdapter.class});
      objectRepositororyFactory.setBeanFactory(mockBeanFactory);
      objectRepositororyFactory.setDelegates(delegatesChain);
   }

   /** Test create.
     *
     * @throws Exception
     *             the exception
     */
   public void testCreate() throws Exception {
      ObjectRepository repository = new HibernateObjectRepository(Project.class);
      mockBeanFactory.autowireBeanProperties(repository, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
      mockBeanFactoryControl.setMatcher(new AutowireBeanPropertiesArgumentsMatcher());
      repository = new RepositoryHistoryAdapter(Project.class, repository);
      mockBeanFactory.autowireBeanProperties(repository, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
      repository = new RepositorySecurityAdapter(Project.class, repository);
      mockBeanFactory.autowireBeanProperties(repository, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
      replay();
      ObjectRepository objectRepository = objectRepositororyFactory.create(Project.class);
      verify();
      assertEquals(repository.getClass(), objectRepository.getClass());
   }

   /** The Class AutowireBeanPropertiesArgumentsMatcher.
     */
   private static class AutowireBeanPropertiesArgumentsMatcher implements ArgumentsMatcher {
      
      /* (non-Javadoc)
       * @see org.easymock.ArgumentsMatcher#matches(java.lang.Object[], java.lang.Object[])
       */
      public boolean matches(Object[] expected, Object[] actual) {
         Class expectedRepositoryClass = expected[0].getClass();
         Class actualRepositoryClass = actual[0].getClass();
         return expectedRepositoryClass.equals(actualRepositoryClass) &&
                expected[1].equals(actual[1]) &&
                expected[2].equals(actual[2]);
      }

      /* (non-Javadoc)
       * @see org.easymock.ArgumentsMatcher#toString(java.lang.Object[])
       */
      public String toString(Object[] arguments) {
         return StringUtils.join(arguments, ", ");
      }
   }
}