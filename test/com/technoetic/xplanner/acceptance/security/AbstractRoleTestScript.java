package com.technoetic.xplanner.acceptance.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.AuthorizerImpl;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;

/**
 * The Class AbstractRoleTestScript.
 */
public abstract class AbstractRoleTestScript extends AbstractDatabaseTestScript {
    
    /** The authenticator. */
    protected XPlannerLoginModule authenticator;
    
    /** The project. */
    private Project project;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        project = newProject();
        SystemAuthorizer.set(new AuthorizerImpl());
        authenticator = new XPlannerLoginModule(new LoginSupportImpl());
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#tearDown()
     */
    public void tearDown() throws Exception {
        SystemAuthorizer.set(null);
        super.tearDown();
    }

    /** Removes the test people.
     *
     * @throws HibernateException
     *             the hibernate exception
     */
    protected void removeTestPeople() throws HibernateException {
        getSession().delete("from person in "+Person.class+" where person.userId like 'testperson%'");
    }

    /** Creates the person.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     * @return the person
     * @throws Exception
     *             the exception
     */
    protected Person createPerson(String userId, String password) throws Exception {
        Person person = new Person(IdGenerator.getUniqueId(userId));
        person.setName(userId);
        person.setInitials("");
        person.setEmail("");
        if (password != null) {
            person.setPassword(authenticator.encodePassword(password, null));
        }
        getSession().save(person);
        super.registerObjectToBeDeletedOnTearDown(person);
        return person;
    }

    /** Assert person present in role with password.
     *
     * @param userId
     *            the user id
     * @param rolename
     *            the rolename
     * @param password
     *            the password
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     */
    protected void assertPersonPresentInRoleWithPassword(String userId, String rolename, String password)
            throws HibernateException, AuthenticationException {
        Person person = getPerson(getSession(), userId);
        assertNotNull("person doesn't exist: "+userId, person);
        if (password != null) {
            try {
                authenticator.authenticate(userId, password);
            } catch (AuthenticationException e) {
                fail("auth failed: "+e.getMessage());
            }
        }
        Collection roles = SystemAuthorizer.get().
                getRolesForPrincipalOnProject(person.getId(), project.getId(), true);
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role)iterator.next();
            if (role.getName().equals(rolename)) {
                return;
            }
        }
        fail("missing role: person="+userId+", role="+rolename);
    }

    /** Gets the person.
     *
     * @param session
     *            the session
     * @param userId
     *            the user id
     * @return the person
     * @throws HibernateException
     *             the hibernate exception
     */
    protected Person getPerson(Session session, String userId) throws HibernateException {
        List people = session.find("from person in class " +
                Person.class.getName() + " where userid = ?", userId, Hibernate.STRING);
        Person person = null;
        Iterator peopleIterator = people.iterator();
        if (peopleIterator.hasNext()) {
            person = (Person)peopleIterator.next();
        }
        return person;
    }
}
