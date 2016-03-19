package com.technoetic.xplanner.domain;

import junit.framework.TestCase;
import net.sf.xplanner.domain.DomainObject;

/**
 * The Class TestDomainObject.
 */
public class TestDomainObject extends TestCase{
   
   /** The domain object. */
   DomainObject domainObject;


   /** Test equals.
     *
     * @throws Exception
     *             the exception
     */
   public void testEquals() throws Exception
   {
      DomainObject object1 = new DummyDomainObject();
      DomainObject object2 = new DummyDomainObject();
      assertEquals(object1,object2);
   }

}