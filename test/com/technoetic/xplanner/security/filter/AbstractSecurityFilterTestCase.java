package com.technoetic.xplanner.security.filter;

import java.io.ByteArrayInputStream;

import javax.security.auth.Subject;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;

import com.technoetic.mocks.servlets.MockFilterChain;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.config.SecurityConfiguration;

/**
 * The Class AbstractSecurityFilterTestCase.
 */
public abstract class AbstractSecurityFilterTestCase extends TestCase {
    
    /** The support. */
    protected XPlannerTestSupport support;
    
    /** The mock filter chain. */
    protected MockFilterChain mockFilterChain;
    
    /** The context. */
    protected final String CONTEXT = "/xplanner";
    
    /** The subject. */
    protected Subject subject;

    /** Instantiates a new abstract security filter test case.
     *
     * @param s
     *            the s
     */
    public AbstractSecurityFilterTestCase(String s) {
        super(s);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ByteArrayInputStream config = new ByteArrayInputStream((
                "<security>\n" +
                    "<security-bypass>" +
                        "<url-pattern>/do/login</url-pattern>" +
                    "</security-bypass>" +
                "    <security-constraint>\n" +
                "      <web-resource-collection>\n" +
                "        <web-resource-name>edit</web-resource-name>\n" +
                "        <url-pattern>/x/*</url-pattern>\n" +
                "      </web-resource-collection>\n" +
                "      <auth-constraint>\n" +
                "        <role-name>editor</role-name>\n" +
                "      </auth-constraint>\n" +
                "    </security-constraint>\n" +
                "    <security-role>\n" +
                "      <role-name>editor</role-name>\n" +
                "    </security-role>\n" +
                "</security>"
                ).getBytes()
        );
        getSecurityFilter().setSecurityConfiguration(SecurityConfiguration.load(config));
        mockFilterChain = new MockFilterChain();
        support = new XPlannerTestSupport();
        support.request.setContextPath(CONTEXT);
        support.request.setRequestURI("/do/foo");
        ThreadSession.set(support.hibernateSession);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        ThreadSession.set(null);
        super.tearDown();
    }

    /** Gets the security filter.
     *
     * @return the security filter
     */
    protected abstract AbstractSecurityFilter getSecurityFilter();

    /** Sets the up subject.
     */
    protected void setUpSubject() {
        subject = new Subject();
        subject.getPrincipals().add(new PersonPrincipal(new Person("xyz")));
        SecurityHelper.setSubject(support.request, subject);
    }

    /** Sets the up request.
     *
     * @param servletPath
     *            the servlet path
     * @param uri
     *            the uri
     */
    protected void setUpRequest(String servletPath, String uri) {
        support.request.setMethod(1);
        support.request.setServletPath(servletPath);
        support.request.setPathInfo(uri);
        support.request.setRequestURI(uri);
    }
}
