package com.technoetic.xplanner.acceptance;

import java.sql.SQLException;

import junit.framework.TestCase;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.acceptance.security.ServerCacheManager;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.HibernateTemplateSimulation;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.domain.PersistentObjectMother;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.AuthorizerImpl;
import com.technoetic.xplanner.security.auth.AuthorizerQueryHelper;
import com.technoetic.xplanner.security.auth.PrincipalSpecificPermissionHelper;

/**
 * The Class AbstractDatabaseTestScript.
 */
public abstract class AbstractDatabaseTestScript extends TestCase {
   
   /** The mom. */
   protected PersistentObjectMother mom;
   
   /** The db support. */
   protected DatabaseSupport dbSupport;
   
   /** The hibernate template. */
   protected HibernateTemplate hibernateTemplate;
   
   /** The server cache manager. */
   private ServerCacheManager serverCacheManager;

   /** Instantiates a new abstract database test script.
     */
   public AbstractDatabaseTestScript() { }

   /** Instantiates a new abstract database test script.
     *
     * @param name
     *            the name
     */
   public AbstractDatabaseTestScript(String name) {
      super(name);
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   @Override
protected void setUp() throws Exception {
      super.setUp();
      serverCacheManager = new ServerCacheManager();
      dbSupport = new DatabaseSupport();
      dbSupport.setUp();
      mom = dbSupport.getMom();
      dbSupport.openSession();
       hibernateTemplate = new HibernateTemplate(GlobalSessionFactory.get());
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#tearDown()
    */
   @Override
protected void tearDown() throws Exception {
      super.tearDown();
      dbSupport.tearDown();
      System.gc();
   }

   /** Commit close and open session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
   public Session commitCloseAndOpenSession() throws HibernateException, SQLException {
      commitAndCloseSession();
      return openSession();
   }

   /** Invalidate server cache if needed.
     */
   private void invalidateServerCacheIfNeeded() {
      serverCacheManager.invalidateServerCacheIfNeeded();
   }

   /** Request server cache invalidation.
     */
   protected void requestServerCacheInvalidation() {
      serverCacheManager.requestServerCacheInvalidation();
   }

   /** Open session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     */
   public Session openSession() throws HibernateException {
      invalidateServerCacheIfNeeded();
      return dbSupport.openSession();
   }

   /** Rollback session.
     */
   public void rollbackSession() {
      dbSupport.rollbackSession();
   }

   /** Commit session.
     *
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
   public void commitSession() throws HibernateException, SQLException {
      dbSupport.commitSession();
   }

   /** Close session.
     */
   public void closeSession() {
      dbSupport.closeSession();
   }

   /** New project.
     *
     * @return the project
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Project newProject() throws HibernateException, RepositoryException {
      return mom.newProject();
   }

   /** New project.
     *
     * @param name
     *            the name
     * @param description
     *            the description
     * @return the project
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Project newProject(String name, String description) throws HibernateException, RepositoryException {
      return mom.newProject(name, description);
   }

   /** New iteration.
     *
     * @param project
     *            the project
     * @return the iteration
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Iteration newIteration(Project project) throws HibernateException, RepositoryException {
      return mom.newIteration(project);
   }

   /** New user story.
     *
     * @param iteration
     *            the iteration
     * @return the user story
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public UserStory newUserStory(Iteration iteration) throws HibernateException, RepositoryException {
      return mom.newUserStory(iteration);
   }

   /** New task.
     *
     * @param story
     *            the story
     * @return the task
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Task newTask(UserStory story) throws HibernateException, RepositoryException {
      return mom.newTask(story);
   }

   /** New feature.
     *
     * @param story
     *            the story
     * @return the feature
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Feature newFeature(UserStory story) throws HibernateException, RepositoryException {
      return mom.newFeature(story);
   }

   /** New time entry.
     *
     * @param task
     *            the task
     * @param person1
     *            the person1
     * @return the time entry
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public TimeEntry newTimeEntry(Task task, Person person1) throws HibernateException, RepositoryException {
      return mom.newTimeEntry(task, person1);
   }

   /** New time entry.
     *
     * @param task
     *            the task
     * @param person1
     *            the person1
     * @param durationInHours
     *            the duration in hours
     * @return the time entry
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public TimeEntry newTimeEntry(Task task, Person person1, double durationInHours) throws HibernateException,
                                                                                           RepositoryException {
      return mom.newTimeEntry(task, person1, durationInHours);
   }

   /** New note.
     *
     * @param container
     *            the container
     * @param author
     *            the author
     * @return the note
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Note newNote(DomainObject container, Person author) throws HibernateException, RepositoryException {
      return mom.newNote(container, author);
   }

   /** New history.
     *
     * @return the history
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public History newHistory() throws HibernateException, RepositoryException {
      return mom.newHistory();
   }

   /** New integration.
     *
     * @return the integration
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   public Integration newIntegration() throws HibernateException, RepositoryException {
      return mom.newIntegration();
   }

   /** New person.
     *
     * @return the person
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     * @throws AuthenticationException
     *             the authentication exception
     */
   public Person newPerson() throws HibernateException, RepositoryException, AuthenticationException {
      requestServerCacheInvalidation();
      return mom.newPerson();
   }

   /** New person.
     *
     * @param userId
     *            the user id
     * @return the person
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     * @throws AuthenticationException
     *             the authentication exception
     */
   public Person newPerson(String userId) throws HibernateException, RepositoryException, AuthenticationException {
      requestServerCacheInvalidation();
      return mom.newPerson(userId);
   }

   /** New permission.
     *
     * @return the permission
     * @throws Exception
     *             the exception
     */
   public Permission newPermission() throws Exception {
      requestServerCacheInvalidation();
      return mom.newPermission();
   }

   /** Sets the up person role.
     *
     * @param project
     *            the project
     * @param person
     *            the person
     * @param roleName
     *            the role name
     * @throws Exception
     *             the exception
     */
   public void setUpPersonRole(final Project project, final Person person, final String roleName)
         throws Exception {
	 //FIXME: rewrite in spring     mom.setUpPersonRole(project, person, roleName);

      requestServerCacheInvalidation();
   }

  /**
     * Save.
     *
     * @param object
     *            the object
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
  public void save(Object object) throws HibernateException, RepositoryException {
     mom.save(object);
  }

//   public void deleteTestObjects() throws Exception, SQLException, RepositoryException, AuthenticationException {
//      commitCloseAndOpenSession();
//      mom.deleteTestObjects();
//   }

   /**
 * Delete object.
 *
 * @param toIteration
 *            the to iteration
 * @throws HibernateException
 *             the hibernate exception
 */
public void deleteObject(Object toIteration) throws HibernateException {
      mom.deleteObject(toIteration);
   }

   /** Register object to be deleted on tear down.
     *
     * @param object
     *            the object
     */
   public void registerObjectToBeDeletedOnTearDown(Object object) {
      mom.registerObjectToBeDeletedOnTearDown(object);
   }

   /** Gets the session.
     *
     * @return the session
     * @throws HibernateException
     *             the hibernate exception
     */
   public Session getSession() throws HibernateException {
      return dbSupport.getSession();
   }

   /** Commit and close session.
     *
     * @throws HibernateException
     *             the hibernate exception
     * @throws SQLException
     *             the SQL exception
     */
   public void commitAndCloseSession() throws HibernateException, SQLException {
      commitSession();
      closeSession();
   }

   /** Creates the authorizer.
     *
     * @return the authorizer
     */
   public Authorizer createAuthorizer() {
      AuthorizerImpl authorizer = new AuthorizerImpl();
      AuthorizerQueryHelper authorizerQueryHelper = new AuthorizerQueryHelper();
      authorizerQueryHelper.setHibernateTemplate(new HibernateTemplateSimulation(dbSupport));
      PrincipalSpecificPermissionHelper principalSpecificPermissionHelper = new PrincipalSpecificPermissionHelper();
      principalSpecificPermissionHelper.setAuthorizerQueryHelper(authorizerQueryHelper);
      authorizer.setAuthorizerQueryHelper(authorizerQueryHelper);
      authorizer.setPrincipalSpecificPermissionHelper(principalSpecificPermissionHelper);
      return authorizer;
   }

}