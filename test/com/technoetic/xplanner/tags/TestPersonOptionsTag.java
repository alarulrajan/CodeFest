package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import junitx.framework.ArrayAssert;
import net.sf.xplanner.domain.Person;

import com.technoetic.mocks.hibernate.MockQuery;

/**
 * The Class TestPersonOptionsTag.
 */
public class TestPersonOptionsTag extends AbstractOptionsTagTestCase {
    
    /** The project id. */
    private int PROJECT_ID = 11;
    
    /** The person2. */
    private Person person2;
    
    /** The person1. */
    private Person person1;

    /** Test project specific list with explicit project id.
     *
     * @throws Throwable
     *             the throwable
     */
    public void testProjectSpecificListWithExplicitProjectId() throws Throwable {
        getTag().setProjectId(PROJECT_ID);
        setUpMockQuery();
        setUpAuthorizedPerson(person1);

        List options = tag.getOptions();

       assertProjectPeopleCount(1);
    }

   /** Assert project people count.
     *
     * @param expectedCount
     *            the expected count
     */
   private void assertProjectPeopleCount(int expectedCount) {
      assertTrue("database not accessed", support.hibernateSession.createQueryCalled);
      assertEquals(PersonOptionsTag.ALL_ACTIVE_PEOPLE_QUERY, support.hibernateSession.createQueryQueryString);
      assertEquals("authorizer not used", expectedCount, authorizer.getPeopleForPrincipalOnProjectCount);
   }

   /** Test project specific list with implicit project id.
     *
     * @throws Throwable
     *             the throwable
     */
   public void testProjectSpecificListWithImplicitProjectId() throws Throwable {
       setUpDomainContext();
       setUpMockQuery();
       setUpAuthorizedPerson(person1);

       List options = tag.getOptions();

       Person[] expectedPersons = new Person[]{person1};
       ArrayAssert.assertEquals(expectedPersons, options.toArray());

      assertProjectPeopleCount(1);
   }

   /** Assert b.
     */
   private void assertB() {
      assertTrue("database not accessed", support.hibernateSession.findCalled);
      assertEquals("authorizer not used", 1, authorizer.getPeopleForPrincipalOnProjectCount);
   }

   /** Test project specific list with project id in request.
     *
     * @throws Throwable
     *             the throwable
     */
   public void testProjectSpecificListWithProjectIdInRequest() throws Throwable {
       support.request.setParameterValue("projectId", new String[]{"11"});
       setUpMockQuery();
       setUpAuthorizedPerson(person1);

      List options = tag.getOptions();

       Person[] expectedPersons = new Person[]{person1};
       ArrayAssert.assertEquals(expectedPersons, options.toArray());

      assertProjectPeopleCount(1);
   }

   /** Sets the up authorized person.
     *
     * @param person
     *            the new up authorized person
     */
   private void setUpAuthorizedPerson(Person person) {
      if (authorizer.getPeopleForPrincipalOnProjectReturn == null)
         authorizer.getPeopleForPrincipalOnProjectReturn = new ArrayList();
      authorizer.getPeopleForPrincipalOnProjectReturn.add(person);
   }

   /** Test system person options with domain context.
     *
     * @throws Throwable
     *             the throwable
     */
   public void testSystemPersonOptionsWithDomainContext() throws Throwable {
       setUpDomainContext();
       getTag().setFiltered("false");
       setUpMockQuery();

       List options = tag.getOptions();

       Person[] expectedPersons = new Person[]{person1, person2};
       ArrayAssert.assertEquals(expectedPersons, options.toArray());

      assertProjectPeopleCount(0);
   }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.AbstractOptionsTagTestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new PersonOptionsTag();
        super.setUp();
        authorizer.hasPermissionReturns = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
    }

    /** Gets the tag.
     *
     * @return the tag
     */
    public PersonOptionsTag getTag() {
        return (PersonOptionsTag) tag;
    }

    /** Sets the up domain context.
     */
    private void setUpDomainContext() {
        DomainContext context = new DomainContext();
        context.save(support.pageContext.getRequest());
        context.setProjectId(PROJECT_ID);
    }

    /** Sets the up mock query.
     */
    private void setUpMockQuery() {
        ArrayList results = new ArrayList();
        person1 = new Person();
        person1.setId(22);
        person1.setName("XYZ");
        person1.setUserId("userId1");
        results.add(person1);
        person2 = new Person();
        person2.setId(33);
        person2.setName("ABC");
        person2.setUserId("userId2");
        results.add(person2);
        MockQuery mockQuery = new MockQuery();
        mockQuery.listReturn = results;
        support.hibernateSession.createQueryReturn = mockQuery;
    }
}
