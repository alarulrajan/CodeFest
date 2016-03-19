package com.technoetic.xplanner.tags;

import static org.easymock.EasyMock.expect;

import java.util.Collections;

import javax.servlet.jsp.tagext.Tag;

import org.easymock.EasyMock;

import junit.framework.TestCase;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.AbstractRequestUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class TestIsUserAuthorizedTag.
 */
public class TestIsUserAuthorizedTag extends AbstractRequestUnitTestCase {
    
    /** The tag. */
    private IsUserAuthorizedTag tag;
    
    /** The support. */
    private XPlannerTestSupport support;
    
    /** The mock authorizer. */
    private MockAuthorizer mockAuthorizer;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.AbstractRequestUnitTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        support.setUpSubject("user", new String[0]);
        mockAuthorizer = new MockAuthorizer();
        support.hibernateSession.findReturn = Collections.EMPTY_LIST;
        GlobalSessionFactory.set(mockSessionFactory);
        SystemAuthorizer.set(mockAuthorizer);
        tag = new IsUserAuthorizedTag();
        tag.setPageContext(pageContext);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.AbstractRequestUnitTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        GlobalSessionFactory.set(null);
        SystemAuthorizer.set(null);
        super.tearDown();
    }

    /** Test user authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorized() throws Exception {
        setUpTagWithAuthorization(Boolean.TRUE);

        replay();

        int result = tag.doStartTag();

        assertTagResult(Tag.EVAL_BODY_INCLUDE, result);
        verify();
    }

    /** Test user not authorized with allowe user override.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserNotAuthorizedWithAlloweUserOverride() throws Exception {
        replay();
        setUpTagWithAuthorization(Boolean.FALSE);
        tag.setAllowedUser(XPlannerTestSupport.DEFAULT_PERSON_ID);

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
        verify();
    }

    /** Test user not authorized.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserNotAuthorized() throws Exception {
        replay();
        setUpTagWithAuthorization(Boolean.FALSE);

        int result = tag.doStartTag();

        assertTagResult(Tag.SKIP_BODY, result);
        verify();
    }

    /** Test user authorized for object.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorizedForObject() throws Exception {
        expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(null);
        replay();
        Project project = setUpTagWithObjectAuthorization(Boolean.TRUE);

        int result = tag.doStartTag();

        assertTagResultForObject(Tag.EVAL_BODY_INCLUDE, result, project);
        verify();
    }

    /** Test user not authorized for object.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserNotAuthorizedForObject() throws Exception {
        expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(null);
        replay();
        Project project = setUpTagWithObjectAuthorization(Boolean.FALSE);

        int result = tag.doStartTag();

        assertTagResultForObject(Tag.SKIP_BODY, result, project);
        verify();
    }

    /** Test user authorized with default principal id.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorizedWithDefaultPrincipalId() throws Exception {
        replay();
        mockAuthorizer.hasPermissionReturn = Boolean.TRUE;
        tag.setResourceType("system.project");
        tag.setProjectId(11);
        tag.setResourceId(2);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", XPlannerTestSupport.DEFAULT_PERSON_ID,
                mockAuthorizer.hasPermissionPersonId);
        verify();
    }

    /** Test user authorized with object attribute string.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorizedWithObjectAttributeString() throws Exception {
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        Object mockObject = new UserStory();
        expect(pageContext.findAttribute("x")).andReturn(mockObject);
        replay();
        tag.setObject("x");
        tag.setProjectId(11);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", mockObject, mockAuthorizer.hasPermission2DomainObject);
        verify();
    }

    /** Test user authorized when object is from default attribute.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorizedWhenObjectIsFromDefaultAttribute() throws Exception {
        Object mockObject = new UserStory();
        expect(pageContext.findAttribute("project")).andReturn(mockObject);
        replay();
        pageContext.findAttribute("project");
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        tag.setProjectId(11);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", mockObject, mockAuthorizer.hasPermission2DomainObject);
        verify();
    }

    /** Test user authorized when project id from object.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorizedWhenProjectIdFromObject() throws Exception {
        expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(null);
        replay();
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        Project mockProject = new Project();
        mockProject.setId(11);
        tag.setObject(mockProject);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", 11, mockAuthorizer.hasPermission2ProjectId);
        verify();
    }

    /** Test user authorized when project id from request param.
     *
     * @throws Exception
     *             the exception
     */
    public void testUserAuthorizedWhenProjectIdFromRequestParam() throws Exception {
        expect(request.getAttribute(DomainContext.REQUEST_KEY)).andReturn(null);
        expect(request.getParameter("projectId")).andReturn("33");
        replay();
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        UserStory mockStory = new UserStory();
        tag.setObject(mockStory);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", 33, mockAuthorizer.hasPermission2ProjectId);
        verify();
    }

    /** Sets the up tag with object authorization.
     *
     * @param authorization
     *            the authorization
     * @return the project
     */
    private Project setUpTagWithObjectAuthorization(Boolean authorization) {
        Project project = new Project();
        project.setId(11);
        tag.setPrincipalId(1);
        tag.setObject(project);
        tag.setPermission("get");
        mockAuthorizer.hasPermission2Return = authorization;
        return project;
    }

    /** Assert tag result for object.
     *
     * @param expectedResult
     *            the expected result
     * @param result
     *            the result
     * @param object
     *            the object
     */
    private void assertTagResultForObject(int expectedResult, int result, DomainObject object) {
        assertEquals("wrong result", expectedResult, result);
        assertEquals("wrong param to authorizer", 1, mockAuthorizer.hasPermission2PersonId);
        assertEquals("wrong param to authorizer", object, mockAuthorizer.hasPermission2DomainObject);
        assertEquals("wrong param to authorizer", "get", mockAuthorizer.hasPermission2Permission);
    }

    /** Sets the up tag with authorization.
     *
     * @param authorized
     *            the new up tag with authorization
     */
    private void setUpTagWithAuthorization(Boolean authorized) {
        mockAuthorizer.hasPermissionReturn = authorized;
        tag.setProjectId(11);
        tag.setPrincipalId(1);
        tag.setResourceType("system.project");
        tag.setResourceId(2);
        tag.setPermission("set");
    }

    /** Assert tag result.
     *
     * @param expectedResult
     *            the expected result
     * @param result
     *            the result
     */
    private void assertTagResult(int expectedResult, int result) {
        assertEquals("wrong result", expectedResult, result);
        assertEquals("wrong param to authorizer", 1, mockAuthorizer.hasPermissionPersonId);
        assertEquals("wrong param to authorizer", "system.project", mockAuthorizer.hasPermissionResourceType);
        assertEquals("wrong param to authorizer", 2, mockAuthorizer.hasPermissionResourceId);
        assertEquals("wrong param to authorizer", "set", mockAuthorizer.hasPermissionPermission);
    }
}
