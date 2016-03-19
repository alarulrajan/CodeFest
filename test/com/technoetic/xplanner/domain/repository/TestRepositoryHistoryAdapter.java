package com.technoetic.xplanner.domain.repository;

import static org.easymock.EasyMock.expect;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Project;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.repository.RepositoryHistoryAdapter.SaveEventHibernateCallback;
import com.technoetic.xplanner.filters.ThreadServletRequest;

/**
 * The Class TestRepositoryHistoryAdapter.
 */
public class TestRepositoryHistoryAdapter extends AbstractUnitTestCase {
   
   /** The adapter. */
   private RepositoryHistoryAdapter adapter;
   
   /** The mock hibernate template. */
   private HibernateTemplate mockHibernateTemplate;
   
   /** The support. */
   private XPlannerTestSupport support;
   
   /** The oid. */
   private int OID = 123;
   
   /** The domain object. */
   private Project domainObject;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      mockObjectRepository = createLocalMock(ObjectRepository.class);
      mockHibernateTemplate = createLocalMock(HibernateTemplate.class);
      support = new XPlannerTestSupport();
      support.setUpSubjectInRole("editor");
      ThreadServletRequest.set(support.request);
      domainObject = new Project();
      adapter = new RepositoryHistoryAdapter(Project.class, mockObjectRepository);
      adapter.setHibernateTemplate(mockHibernateTemplate);
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      super.tearDown();
      ThreadSession.set(null);
      ThreadServletRequest.set(null);
   }

   /** Test delete history.
     *
     * @throws Exception
     *             the exception
     */
   public void testDeleteHistory() throws Exception {
      domainObject.setName("PROJECT_NAME");
      expect(mockHibernateTemplate.load(Project.class, new Integer(OID))).andReturn(domainObject);
      checkSaveHistory(domainObject,
                       History.DELETED,
                       domainObject.getName(),
                       XPlannerTestSupport.DEFAULT_PERSON_ID);
      mockObjectRepository.delete(OID);
      replay();

      adapter.delete(OID);

      verify();
   }

   /** Test insert history.
     *
     * @throws Exception
     *             the exception
     */
   public void testInsertHistory() throws Exception {
      expect(mockObjectRepository.insert(domainObject)).andReturn(111);
      checkSaveHistory(domainObject, History.CREATED, null, XPlannerTestSupport.DEFAULT_PERSON_ID);
      replay();

      adapter.insert(domainObject);

      verify();
   }

   /** Test update history.
     *
     * @throws Exception
     *             the exception
     */
   public void testUpdateHistory() throws Exception {
      mockObjectRepository.update(domainObject);
      checkSaveHistory(domainObject, History.UPDATED, null, XPlannerTestSupport.DEFAULT_PERSON_ID);
      replay();
      adapter.update(domainObject);
      verify();
   }

   /** Test load history.
     *
     * @throws Exception
     *             the exception
     */
   public void testLoadHistory() throws Exception {
      expect(mockObjectRepository.load(111)).andReturn(domainObject);
      replay();

      adapter.load(111);

      verify();
   }

   /** Check save history.
     *
     * @param domainObject
     *            the domain object
     * @param evenType
     *            the even type
     * @param description
     *            the description
     * @param personId
     *            the person id
     */
   private void checkSaveHistory(Nameable domainObject, String evenType, String description, int personId) {
      SaveEventHibernateCallback hibernateCallback =
            adapter.new SaveEventHibernateCallback(domainObject, evenType, description, personId);
      expect(mockHibernateTemplate.execute(hibernateCallback)).andReturn(null);
   }

}
