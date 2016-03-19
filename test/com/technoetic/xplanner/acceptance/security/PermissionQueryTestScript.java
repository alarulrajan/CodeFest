package com.technoetic.xplanner.acceptance.security;

import java.sql.SQLException;
import java.util.List;

import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Role;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.domain.HibernateTemplateSimulation;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.AuthorizerImpl;
import com.technoetic.xplanner.security.auth.AuthorizerQueryHelper;
import com.technoetic.xplanner.security.auth.PrincipalSpecificPermissionHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class PermissionQueryTestScript.
 */
public class PermissionQueryTestScript extends AbstractDatabaseTestScript {
    
    /** The person. */
    private Person person;
    
    /** The permission tester. */
    private String permissionTester = "permissionTester";

    /** Instantiates a new permission query test script.
     *
     * @param name
     *            the name
     */
    public PermissionQueryTestScript(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#tearDown()
     */
    protected void tearDown() throws Exception {
       try {
          deletePersonIfExists(getSession());
       }
       finally {
          super.tearDown();
       }
    }

    /** Test simple permission query.
     *
     * @throws Exception
     *             the exception
     */
    public void testSimplePermissionQuery() throws Exception {
        person = (Person)getObject(getSession(), Person.class, permissionTester);
        deletePersonIfExists(getSession());

        getSession().delete("from object in class " + Permission.class.getName() + " where object.name like 'test%'");
        getSession().flush();
        getSession().clear();

        int projectId = 11;
        int personId = createPersonAndPermission(projectId);

        commitSession();

       AuthorizerQueryHelper authorizerQueryHelper = new AuthorizerQueryHelper();
       authorizerQueryHelper.setHibernateTemplate(new HibernateTemplateSimulation(getSession()));
       PrincipalSpecificPermissionHelper principalSpecificPermissionHelper = new PrincipalSpecificPermissionHelper();
       principalSpecificPermissionHelper.setAuthorizerQueryHelper(authorizerQueryHelper);
       AuthorizerImpl authorizer = new AuthorizerImpl();
       authorizer.setPrincipalSpecificPermissionHelper(principalSpecificPermissionHelper);
       authorizer.setAuthorizerQueryHelper(authorizerQueryHelper);
       SystemAuthorizer.set(authorizer);
       verifyPermission(projectId, personId);

    }

    /** Verify permission.
     *
     * @param projectId
     *            the project id
     * @param personId
     *            the person id
     * @throws AuthenticationException
     *             the authentication exception
     */
    private void verifyPermission(int projectId, int personId)
        throws AuthenticationException
    {
        boolean result = false;

        // exact match
        Authorizer authorizer = SystemAuthorizer.get();
        result = authorizer.
                hasPermission(projectId, personId, "system.project", 1, "testpermission");
        assertTrue("wrong result", result);

        // no match
        result = authorizer.
                hasPermission(projectId, personId, "system.project", 10, "testpermission");
        assertFalse("wrong result", result);

        // resource type wildcard, resourceId wildcard, permission name wildcard
        // viewer role
        result = authorizer.
                hasPermission(projectId, personId, "system.project.iteration.story", 10, "read.test");
        assertTrue("wrong result", result);

        // resource type wildcard, resourceId exact match, permission name wildcard
        // editor role
        result = authorizer.
                hasPermission(projectId, personId, "system.project.iteration.story", 10, "read.test");
        assertTrue("wrong result", result);

        // resource type wildcard, resourceId non match, permission name wildcard
        result = authorizer.
                hasPermission(projectId, personId, "system.project.iteration.story", 11, "write.test");
        assertFalse("wrong result", result);
    }

    /** Creates the person and permission.
     *
     * @param projectId
     *            the project id
     * @return the int
     * @throws HibernateException
     *             the hibernate exception
     */
    private int createPersonAndPermission(int projectId)
        throws HibernateException
    {
        person = new Person(permissionTester);
        person.setName(permissionTester);
        person.setEmail("permission@tester");
        person.setInitials("pt");
        int personId = ((Integer)getSession().save(person)).intValue();
        getSession().save(new PersonRole(projectId, personId, getRoleId(getSession(), "admin")));
        addPermission(getSession(), personId, "system.project", 1, "testpermission");
        return personId;
    }

    /** Delete person if exists.
     *
     * @param session
     *            the session
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
    private void deletePersonIfExists(Session session) throws HibernateException, SQLException
    {
        if (person != null) {
            session.delete("from object in " + Person.class + " where object.name = ?",  permissionTester, Hibernate.STRING);
            session.delete("from object in " + PersonRole.class + " where object.id.personId = ?", new Integer(person.getId()), Hibernate.INTEGER);
            commitSession();
        }
    }

    /** Gets the role id.
     *
     * @param session
     *            the session
     * @param roleName
     *            the role name
     * @return the role id
     * @throws HibernateException
     *             the hibernate exception
     */
    private int getRoleId(Session session, String roleName) throws HibernateException {
        return ((Role)getObject(session, Role.class, roleName)).getId();

    }

    /** Adds the permission.
     *
     * @param session
     *            the session
     * @param personId
     *            the person id
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param permissionName
     *            the permission name
     * @throws HibernateException
     *             the hibernate exception
     */
    private void addPermission(Session session, int personId, String resourceType, int resourceId, String permissionName)
            throws HibernateException {
        Permission permission = new Permission(resourceType, resourceId, personId, permissionName);
        session.save(permission);
    }

    /** Gets the object.
     *
     * @param session
     *            the session
     * @param clazz
     *            the clazz
     * @param name
     *            the name
     * @return the object
     * @throws HibernateException
     *             the hibernate exception
     */
    private Object getObject(Session session, Class clazz, String name) throws HibernateException {
        List objects = session.find("from object in class " + clazz.getName() + " where object.name = ?",
                name, Hibernate.STRING);
        return objects.size() > 0 ? objects.get(0) : null;
    }
}
