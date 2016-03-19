package com.technoetic.xplanner.domain.repository;

import static org.easymock.EasyMock.expect;
import net.sf.xplanner.domain.DomainObject;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.DummyDomainObject;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.auth.AuthorizationException;
import com.technoetic.xplanner.security.auth.Authorizer;

/**
 * The Class TestRepositorySecurityAdapter.
 */
public class TestRepositorySecurityAdapter extends AbstractUnitTestCase {
    
    /** The adapter. */
    private RepositorySecurityAdapter adapter;
    
    /** The mock hibernate template. */
    private HibernateTemplate mockHibernateTemplate;
    
    /** The oid. */
    private final int OID = 22;
    
    /** The domain object. */
    private DomainObject domainObject;
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The mock authorizer. */
    private Authorizer mockAuthorizer;

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
        mockAuthorizer = createLocalMock(Authorizer.class);
        domainObject = new DummyDomainObject();
        adapter = new RepositorySecurityAdapter(DomainObject.class, mockObjectRepository);
        adapter.setAuthorizer(mockAuthorizer);
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

    /** Test delete when authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testDeleteWhenAuthorized() throws Exception {
        expect(mockHibernateTemplate.load(DomainObject.class, new Integer(OID))).andReturn(domainObject);
        expect(mockAuthorizer.
                hasPermission(0,
                              XPlannerTestSupport.DEFAULT_PERSON_ID, domainObject, "delete")).andReturn(true);
        mockObjectRepository.delete(OID);
        replay();

        adapter.delete(OID);

        verify();
    }

    /** Test delete when not authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testDeleteWhenNotAuthorized() throws Exception {
        expect(mockHibernateTemplate.load(DomainObject.class, new Integer(OID))).andReturn(domainObject);
        expect(mockAuthorizer.
                hasPermission(0,
                              XPlannerTestSupport.DEFAULT_PERSON_ID, domainObject, "delete")).andReturn(false);
        replay();

        try {
            adapter.delete(OID);
            fail("no authorization exception");
        } catch (AuthorizationException e) {
            // expected
        }

        verify();
    }

    /** Test insert when authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testInsertWhenAuthorized() throws Exception {
        // do-before-release uncomment this test
//        mockAuthorizerControl.expectAndReturn(mockAuthorizer.hasPermission(0,
//                XPlannerTestSupport.DEFAULT_PERSON_ID, domainObject, "create"), true);
//        mockObjectRepositoryControl.expectAndReturn(mockObjectRepository.insert(domainObject), OID);
//        replay();
//
//        adapter.insert(domainObject);
//
//        verify();
    }

    /** Test insert when not authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testInsertWhenNotAuthorized() throws Exception {
        // do-before-release uncomment this test
//        mockAuthorizerControl.expectAndReturn(mockAuthorizer.hasPermission(0,
//                XPlannerTestSupport.DEFAULT_PERSON_ID, domainObject, "create"), false);
//        replay();
//
//        try {
//            adapter.insert(domainObject);
//            fail("no authorization exception");
//        } catch (AuthorizationException e) {
//            // expected
//        }
//
//        verify();
    }

    /** Test load when authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testLoadWhenAuthorized() throws Exception {
         expect(mockAuthorizer.hasPermission(0,XPlannerTestSupport.DEFAULT_PERSON_ID, domainObject, "read")).andReturn(true);
         expect(mockObjectRepository.load(OID)).andReturn(domainObject);
         replay();

         adapter.load(OID);

         verify();
     }

     /** Test load when not authorized.
         *
         * @throws Exception
         *             the exception
         */
     public void testLoadWhenNotAuthorized() throws Exception {
         expect(mockObjectRepository.load(OID)).andReturn(domainObject);
         expect(mockAuthorizer.hasPermission(0,
                                                                            XPlannerTestSupport.DEFAULT_PERSON_ID, domainObject, "read")).andReturn(false);
         replay();

         try {
             adapter.load(OID);
             fail("no authorization exception");
         } catch (AuthorizationException e) {
             // expected
         }

         verify();
     }

}
