package com.technoetic.xplanner;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.xplanner.events.EventManager;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.ObjectMother;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.easymock.EasyMockController;
import com.technoetic.xplanner.easymock.EasyMockHelper;

/**
 * The Class AbstractUnitTestCase.
 */
public abstract class AbstractUnitTestCase extends TestCase implements EasyMockController {

    /** The easymock helper. */
    EasyMockHelper easymockHelper;
    
    /** The mock meta repository. */
    protected MetaRepository mockMetaRepository;
    
    /** The mock object repository. */
    protected ObjectRepository mockObjectRepository;
    
    /** The mock session. */
    public Session mockSession;
    
    /** The hibernate template. */
    public HibernateTemplate hibernateTemplate;
    
    /** The mock transaction. */
    protected Transaction mockTransaction;
    
    /** The support. */
    protected XPlannerTestSupport support;
    
    /** The mom. */
    protected ObjectMother mom;
	
	/** The event bus. */
	protected EventManager eventBus;
	
	/** The mock session factory. */
	protected SessionFactory mockSessionFactory;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
	protected void setUp() throws Exception {
        super.setUp();
        easymockHelper = new EasyMockHelper();
        support = new XPlannerTestSupport();
        mom = new ObjectMother();
    }

    /** Sets the up thread session.
     *
     * @throws HibernateException
     *             the hibernate exception
     */
    protected void setUpThreadSession() throws HibernateException {
        setUpThreadSession(true);
    }

    /** Sets the up thread session.
     *
     * @param expectCommit
     *            the new up thread session
     * @throws HibernateException
     *             the hibernate exception
     */
    protected void setUpThreadSession(boolean expectCommit) throws HibernateException {
    	mockSession = easymockHelper.createGlobalMock(Session.class);
    	mockSessionFactory = easymockHelper.createGlobalMock(SessionFactory.class);
    	hibernateTemplate = easymockHelper.createGlobalMock(HibernateTemplate.class);
        mockTransaction = easymockHelper.createNiceGlobalMock(Transaction.class);
        if (expectCommit) {
        	expect(mockSession.beginTransaction()).andReturn(mockTransaction);
        }
        ThreadSession.set(mockSession);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
	protected void tearDown() throws Exception {
        super.tearDown();
        ThreadSession.set(null);
    }

    /** Creates the local mock.
     *
     * @param <T>
     *            the generic type
     * @param class1
     *            the class1
     * @return the t
     */
    protected final <T> T createLocalMock(Class<T> class1) {
    	return easymockHelper.createLocalMock(class1);
	}

    /** Creates the global mock.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the t
     */
    protected final <T> T createGlobalMock(Class<T> clazz) {
        return easymockHelper.createGlobalMock(clazz);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.easymock.EasyMockController#replay()
     */
    public void replay() {
        assertHelperPresent();
        easymockHelper.replayMocks();
    }

    /** Assert helper present.
     */
    private void assertHelperPresent() {
        Assert.assertNotNull("no EasyMock helper: was super.setUp() called?", easymockHelper);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.easymock.EasyMockController#verify()
     */
    public void verify() {
        assertHelperPresent();
        easymockHelper.verifyMocks();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.easymock.EasyMockController#reset()
     */
    public void reset() {
        assertHelperPresent();
        easymockHelper.resetMocks();
    }

    /** Expect object repository access.
     *
     * @param objectClass
     *            the object class
     */
    @SuppressWarnings("unchecked")
	protected void expectObjectRepositoryAccess(Class objectClass) {
        if (mockMetaRepository == null) {
            setUpRepositories();
        }
        expect(mockMetaRepository.getRepository(objectClass)).andReturn(mockObjectRepository).atLeastOnce();
    }

    /** Sets the up repositories.
     */
    protected void setUpRepositories() {
        mockMetaRepository = easymockHelper.createGlobalMock(MetaRepository.class);
        mockObjectRepository = easymockHelper.createGlobalMock(ObjectRepository.class);
        eventBus = easymockHelper.createGlobalMock(EventManager.class);
    }
}
