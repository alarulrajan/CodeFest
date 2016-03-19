package net.sf.xplanner.acceptance.web;

import net.sf.xplanner.acceptance.util.PersonHelper;
import net.sf.xplanner.domain.Person;
import net.sourceforge.jwebunit.junit.WebTestCase;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import org.junit.Before;

/**
 * The Class BaseTest.
 */
public class BaseTest extends WebTestCase {
    
    /** The Constant ADMIN_LOGIN. */
    protected static final String ADMIN_LOGIN = "sysadmin";
    
    /** The Constant TOP_LINK_ID. */
    public static final String TOP_LINK_ID = "topLink";
    
    /** The Constant PEOPLE_LINK_ID. */
    public static final String PEOPLE_LINK_ID = "aKO";
    
    /** The Constant EDIT_LINK_ID. */
    public static final String EDIT_LINK_ID = "aKE";
    
    /** The Constant ADD_USER_LINK_ID. */
    public static final String ADD_USER_LINK_ID = "aKA";
    
    /** The person helper. */
    protected PersonHelper personHelper;
    
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    @Before
    public void setUp() throws Exception {
        setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
        getTestContext().setResourceBundleName("ResourceBundle");
        super.setUp();
        setBaseUrl("http://localhost:8080/xplanner");
        personHelper = new PersonHelper(this);
    }
    
    /**
     * Do login.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     */
    public void doLogin(String userId, String password) {
        beginAt("/do/login");
        assertFormPresent("loginForm");
        setTextField("userId", userId);
        setTextField("password", password);
        submit();
    }
    
    /**
     * Login as admin.
     */
    protected void loginAsAdmin() {
        doLogin(ADMIN_LOGIN, "admin");
        assertLinkPresent(PEOPLE_LINK_ID);
    }

    /**
     * Creates the sysadmin.
     *
     * @param userId
     *            the user id
     * @param name
     *            the name
     * @param email
     *            the email
     * @param password
     *            the password
     */
    protected void createSysadmin(String userId, String name, String email, String password) {
        personHelper.createSysadmin(userId, name, email, password);
    }

    /**
     * Gets the and login as user.
     *
     * @return the and login as user
     */
    protected Person getAndLoginAsUser() {
        loginAsAdmin();
        return personHelper.getUser();
    }

}
