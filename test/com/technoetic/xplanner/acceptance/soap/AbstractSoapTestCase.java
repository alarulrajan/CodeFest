package com.technoetic.xplanner.acceptance.soap;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.soap.XPlanner;
import com.technoetic.xplanner.soap.domain.IterationData;
import com.technoetic.xplanner.soap.domain.ProjectData;
import com.technoetic.xplanner.soap.domain.TaskData;
import com.technoetic.xplanner.soap.domain.TimeEntryData;
import com.technoetic.xplanner.soap.domain.UserStoryData;

//ChangeSoon: Add test for the getXXXs() methods

/**
 * The Class AbstractSoapTestCase.
 */
public abstract class AbstractSoapTestCase extends AbstractDatabaseTestScript {
    
    /** The Constant MS_IN_DAY. */
    private static final long MS_IN_DAY = 3600000 * 24;
    
    /** The xplanner. */
    protected XPlanner xplanner;
    
    /** The an assert. */
    private final SoapAdapterEqualAssert anAssert = new SoapAdapterEqualAssert();
    
    /** The user id. */
    private String userId;
    
    /** The password. */
    private String password;

    /** Instantiates a new abstract soap test case.
     *
     * @param s
     *            the s
     */
    public AbstractSoapTestCase(String s) {
        super(s);
    }

    /** Creates the x planner.
     *
     * @return the x planner
     * @throws Exception
     *             the exception
     */
    public abstract XPlanner createXPlanner() throws Exception;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        xplanner = createXPlanner();
        commitCloseAndOpenSession();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#tearDown()
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    /** Test project crud.
     *
     * @throws Exception
     *             the exception
     */
    public void testProjectCRUD() throws Exception {
        ProjectData expected = new ProjectData();
        expected.setName("Test project");
        expected.setDescription("This is the test project for CRUD");
        ProjectData actual = xplanner.addProject(expected);
        int id = actual.getId();
        try {
            assertEquals(expected, actual, new String[]{"name", "description"});

            expected = actual;
            actual = xplanner.getProject(id);
            assertEquals(expected, actual, new String[]{"id", "name", "description"});

            expected = actual;
            expected.setName("Other Test project");
            expected.setName("Other Test project description");
            xplanner.update(expected);

            actual = xplanner.getProject(id);
            assertEquals(expected, actual, new String[]{"id", "name", "description"});

        } finally {
            xplanner.removeProject(id);
            assertNull(xplanner.getProject(id));
        }
    }

    /** Test project not found.
     *
     * @throws Exception
     *             the exception
     */
    public void testProjectNotFound() throws Exception {
        try {
            xplanner.removeProject(Integer.MAX_VALUE);
        } catch (RemoteException e) {
            assertTrue(e.getMessage().indexOf("ObjectNotFoundException") != -1);
            return;
        }
        fail("did not throw a ObjectNotFoundException");
    }

   /** Test get current iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetCurrentIteration() throws Exception {
        Project project = newProject();
        Iteration iteration = newIteration(project);
        iteration.setStartDate(new Date(System.currentTimeMillis() - MS_IN_DAY));
        iteration.setEndDate(new Date(System.currentTimeMillis() + MS_IN_DAY));
        commitCloseAndOpenSession();

        IterationData data = xplanner.getCurrentIteration(project.getId());
        assertNotNull("no iteration", data);
        assertEquals("wrong iteration", iteration.getId(), data.getId());
    }

   /** Test viewer access to project.
     *
     * @throws Exception
     *             the exception
     */
   public void testViewerAccessToProject() throws Exception {
       Project project = newProject();
       Person viewer = newPerson("soapviewer");
       setUpPersonRole(project, viewer, "viewer");
       commitCloseAndOpenSession();

       setUserId(viewer.getUserId());
       setPassword("test");
       xplanner = createXPlanner();
       ProjectData[] projects = xplanner.getProjects();

       assertEquals("wrong # of project", 1, projects.length);

       ProjectData soapProject = xplanner.getProject(project.getId());
       assertNotNull("project not found", soapProject);
   }


    /** The Constant ITERATION_PROPERTIES. */
    final static private String[] ITERATION_PROPERTIES = {
        "id", "name", "statusKey", "description", "projectId",
        "estimatedHours", "startDate", "endDate", "actualHours", "adjustedEstimatedHours", "remainingHours"};

    /** Test iteration crud.
     *
     * @throws Exception
     *             the exception
     */
    //ChangeSoon: test createiteration with some story so we can test all readonly prop of iteration
    public void testIterationCRUD() throws Exception {
        Project project = newProject();
        commitAndCloseSession();
        IterationData expected = new IterationData();
        expected.setProjectId(project.getId());
        expected.setName("Test iteration");
        expected.setDescription("This is the test iteration for CRUD");
        expected.setStartDate(newCalendar(2000, 1, 2));
        expected.setEndDate(newCalendar(2000, 1, 22));
        expected.setStatusKey(IterationStatus.INACTIVE_KEY);

        IterationData actual = xplanner.addIteration(expected);
        int id = actual.getId();
        Iteration addedIteration = (Iteration)getSession().get(Iteration.class, new Integer(id));
        registerObjectToBeDeletedOnTearDown(addedIteration);

        UserStory story = null;
        Task task = null;
        TimeEntry timeEntry = null;
        Person person = null;
        try {
            expected.setId(id);
            assertEquals(expected, actual, ITERATION_PROPERTIES);
            IterationData[] iterations = xplanner.getIterations(project.getId());
            anAssert.assertEquals(new IterationData[]{expected}, iterations, ITERATION_PROPERTIES);

            story = newUserStory(addedIteration);
            story.setEstimatedHoursField(10);
            task = newTask(story);
            task.setEstimatedHours(11);
            task.setEstimatedOriginalHours(10);
//FEATURE:
//            newFeature(story);
            person = newPerson(IdGenerator.getUniqueId("UserId"));
            timeEntry = newTimeEntry(task, person, 8.0);
            addedIteration.setIterationStatus(IterationStatus.ACTIVE);
            assertEquals(11.0, story.getEstimatedHours(), 0);
            assertEquals(8.0, story.getCachedActualHours(), 0);
            commitCloseAndOpenSession();

            actual = xplanner.getIteration(id);
            assertEquals(addedIteration, actual, ITERATION_PROPERTIES);

            expected = actual;
            expected.setName("Other Test iteration");
            expected.setDescription("Other Test iteration description");
            expected.setStartDate(newCalendar(2001, 1, 2, 0, 0, 0));
            expected.setEndDate(newCalendar(2001, 1, 12, 0, 0, 0));

            xplanner.update(expected);

            actual = xplanner.getIteration(id);
            assertEquals(expected, actual, ITERATION_PROPERTIES);
        } finally {
            if (person != null)
                xplanner.removePerson(person.getId());
            if (timeEntry != null)
                xplanner.removeTimeEntry(timeEntry.getId());
            if (task != null)
                xplanner.removeTask(task.getId());
            if (story != null)
                xplanner.removeUserStory(story.getId());
            xplanner.removeIteration(id);
            assertNull(xplanner.getIteration(id));
        }
    }

    /** New calendar.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param day
     *            the day
     * @return the calendar
     */
    private Calendar newCalendar(int year, int month, int day) {
        Calendar start = newCalendar(year, month, day, 0, 0, 0);
        return start;
    }

    /** New calendar.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param day
     *            the day
     * @param hr
     *            the hr
     * @param min
     *            the min
     * @param sec
     *            the sec
     * @return the calendar
     */
    private Calendar newCalendar(int year, int month, int day, int hr, int min, int sec) {
        Calendar start = Calendar.getInstance();
        start.set(year, month, day, hr, min, sec);
        start.set(Calendar.MILLISECOND, 0);
        return start;
    }

    /** Test iteration not found.
     *
     * @throws Exception
     *             the exception
     */
    public void testIterationNotFound() throws Exception {
        try {
            xplanner.removeIteration(Integer.MAX_VALUE);
        } catch (RemoteException e) {
            assertTrue(e.getMessage().indexOf("ObjectNotFoundException") != -1);
            return;
        }
        fail("did not throw a ObjectNotFoundException");
    }

    /** The Constant STORY_PROPERTIES. */
    final public static String[] STORY_PROPERTIES = {
          "id", "iterationId", "name", "dispositionName", "description", "customerId", "trackerId", UserStory.ESTIMATED_HOURS, "priority", "actualHours"};

    /** Test story crud.
     *
     * @throws Exception
     *             the exception
     */
    public void testStoryCRUD() throws Exception {
        Project project = newProject();
        Iteration iteration = newIteration(project);
        Person person1 = newPerson(IdGenerator.getUniqueId("UserId"));
        Person person2 = newPerson(IdGenerator.getUniqueId("UserId"));
        commitCloseAndOpenSession();

        // Test add & get
        UserStoryData expected = new UserStoryData();
        expected.setIterationId(iteration.getId());
        expected.setName("Test story");
        expected.setDispositionName(StoryDisposition.ADDED_NAME);
        expected.setDescription("This is the test story for CRUD");
        expected.setCustomerId(person1.getId());
        expected.setTrackerId(person2.getId());
        expected.setEstimatedHours(15);
        expected.setPriority(3);

        UserStoryData actual = xplanner.addUserStory(expected);
        int id = actual.getId();
        registerObjectToBeDeletedOnTearDown(getSession().get(UserStory.class, new Integer(id)));

        UserStory addedStory = null;
        TimeEntry timeEntry = null;
        Task task = null;
        Person person = null;
        try {
            expected.setId(id);
            assertEquals(expected, actual, STORY_PROPERTIES);
            UserStoryData[] stories = xplanner.getUserStories(iteration.getId());
            anAssert.assertEquals(new UserStoryData[]{expected}, stories, STORY_PROPERTIES);

            // Test update
            expected = actual;
            expected.setName("Other Test story");
            expected.setDescription("Other Test story description");
            expected.setCustomerId(person2.getId());
            expected.setTrackerId(person1.getId());
            expected.setPriority(2);
            expected.setEstimatedHours(10);
            expected.setEstimatedOriginalHours(expected.getEstimatedHours());
            xplanner.update(expected);

            actual = xplanner.getUserStory(id);
            assertEquals(expected, actual, STORY_PROPERTIES);

            // Test get calculated fields
            commitCloseAndOpenSession();
            addedStory = (UserStory)getSession().load(UserStory.class, new Integer(id));
            task = newTask(addedStory);
            task.setEstimatedHours(11);
            task.setEstimatedOriginalHours(10);
//FEATURE:
//            newFeature(addedStory);
            person = newPerson();
            timeEntry = newTimeEntry(task, person, 8.0);
            assertEquals(11.0, addedStory.getEstimatedHours(), 0);
            assertEquals(8.0, addedStory.getCachedActualHours(), 0);
            commitCloseAndOpenSession();

            actual = xplanner.getUserStory(id);
            assertEquals(addedStory, actual, STORY_PROPERTIES);

        } finally {
            // test remove
            if (person != null)
                xplanner.removePerson(person.getId());
            if (timeEntry != null)
                xplanner.removeTimeEntry(timeEntry.getId());
            if (task != null)
                xplanner.removeTask(task.getId());

            xplanner.removeUserStory(id);
            assertNull(xplanner.getUserStory(id));
        }
    }

    /** Test remove user story not found.
     *
     * @throws Exception
     *             the exception
     */
    public void testRemoveUserStoryNotFound() throws Exception {
        try {
            xplanner.removeUserStory(Integer.MAX_VALUE);
        } catch (RemoteException e) {
            assertTrue(e.getMessage().indexOf("ObjectNotFoundException") != -1);
            return;
        }
        fail("did not throw a ObjectNotFoundException");
    }

    /** The Constant TASK_PROPERTIES. */
    public final static String[] TASK_PROPERTIES = {
        "acceptorId", "actualHours", "adjustedEstimatedHours", "completed",
        "createdDate", "description", "dispositionName", "estimatedHours", "id",
        "name", "estimatedOriginalHours", "remainingHours",
        "storyId", "type"};

    /** Test task crud.
     *
     * @throws Exception
     *             the exception
     */
    public void testTaskCRUD() throws Exception {
        Project project = newProject();
        Iteration iteration = newIteration(project);
        UserStory story = newUserStory(iteration);
        //start iteration
        story.setEstimatedOriginalHours(new Double(10.0));
        Person person1 = newPerson(IdGenerator.getUniqueId("UserId"));
        Person person2 = newPerson(IdGenerator.getUniqueId("UserId"));
        commitCloseAndOpenSession();

        // test add & get
        TaskData expected = new TaskData();
        expected.setName("Test task");
        expected.setStoryId(story.getId());
        expected.setType("Defect");
        expected.setDispositionName(TaskDisposition.ADDED_NAME);
        expected.setAcceptorId(person1.getId());
        expected.setEstimatedHours(10);
        expected.setDescription("This is the test task for CRUD");

        TaskData actual = xplanner.addTask(expected);
        int id = actual.getId();
        registerObjectToBeDeletedOnTearDown(getSession().get(Task.class, new Integer(id)));
        TimeEntry timeEntry  = null;
//        try {

            expected.setId(id);
            expected.setAdjustedEstimatedHours(expected.getEstimatedHours());
            expected.setEstimatedOriginalHours(expected.getEstimatedHours());
            expected.setRemainingHours(expected.getEstimatedHours());
            assertEquals(expected, actual, TASK_PROPERTIES);
            TaskData[] tasks = xplanner.getTasks(story.getId());
            anAssert.assertEquals(new TaskData[]{expected}, tasks, TASK_PROPERTIES);

            expected = actual;
            expected.setName("Other Test task");
            expected.setType("Debt");
            expected.setDispositionName(TaskDisposition.DISCOVERED_NAME);
            expected.setAcceptorId(person2.getId());
            expected.setEstimatedHours(20);
            expected.setDescription("Other Test task description");
            xplanner.update(expected);

            actual = xplanner.getTask(id);

            expected.setAdjustedEstimatedHours(expected.getEstimatedHours());
            expected.setRemainingHours(expected.getEstimatedHours());
            assertEquals(expected, actual, TASK_PROPERTIES);

            // Test get calculated fields
            commitCloseAndOpenSession();
            Task task = (Task)getSession().load(Task.class, new Integer(id));
            timeEntry = newTimeEntry(task, person2, 8.0);
            expected.setEstimatedHours(15);
            assertEquals(20.0, task.getEstimatedHours(), 0);
            assertEquals(8.0, task.getActualHours(), 0);
            commitCloseAndOpenSession();

            actual = xplanner.getTask(id);
            assertEquals(task, actual, TASK_PROPERTIES);

//        } finally {
//            if (timeEntry != null)
//                xplanner.removeTimeEntry(timeEntry.getId());
            xplanner.removeTask(id);
            assertNull(xplanner.getTask(id));
//        }
    }

    /** Test task not found.
     *
     * @throws Exception
     *             the exception
     */
    public void testTaskNotFound() throws Exception {
        try {
            xplanner.removeTask(Integer.MAX_VALUE);
        } catch (RemoteException e) {
            assertTrue(e.getMessage().indexOf("ObjectNotFoundException") != -1);
            return;
        }
        fail("did not throw a ObjectNotFoundException");
    }


    /** The Constant FEATURE_PROPERTIES. */
    public final static String[] FEATURE_PROPERTIES = {"id","name", "description", "storyId"};
//FEATURE:
//    public void testFeatureCRUD() throws Exception {
//        Project project = newProject();
//        Iteration iteration = newIteration(project);
//        UserStory story = newUserStory(iteration);
//        commitCloseAndOpenSession();
//
//        // test add & get
//        FeatureData expected = new FeatureData();
//        expected.setName("Test feature");
//        expected.setStoryId(story.getId());
//        expected.setDescription("This is the test feature for CRUD");
//
//        FeatureData actual = xplanner.addFeature(expected);
//        int id = actual.getId();
//        registerObjectToBeDeletedOnTearDown(session.get(UserStory.class, new Integer(id)));
//        try {
//
//            expected.setId(id);
//            assertEquals(expected, actual, FEATURE_PROPERTIES);
//            FeatureData[] features = xplanner.getFeatures(story.getId());
//            anAssert.assertArraysEqual(new FeatureData[]{expected}, features, FEATURE_PROPERTIES);
//
//            expected = actual;
//            expected.setName("Other Test feature");
//            expected.setDescription("Other Test feature description");
//            xplanner.update(expected);
//
//            actual = xplanner.getFeature(id);
//            assertEquals(expected, actual, FEATURE_PROPERTIES);
//        } finally {
//            xplanner.removeFeature(id);
//            assertNull(xplanner.getFeature(id));
//        }
//    }

//    public void testFeatureNotFound() throws Exception {
//        try {
//            xplanner.removeFeature(Integer.MAX_VALUE);
//        } catch (RemoteException e) {
//            assertTrue(e.getMessage().indexOf("ObjectNotFoundException") != -1);
//            return;
//        }
//        fail("did not throw a ObjectNotFoundException");
//    }

    /** The Constant TIME_ENTRY_PROPERTIES. */
public static final String[] TIME_ENTRY_PROPERTIES = {
        "id", "taskId", "startTime", "endTime", "person1Id", "person2Id", "description"
    };

    /** Test time entry crud.
     *
     * @throws Exception
     *             the exception
     */
    public void testTimeEntryCRUD() throws Exception {
       Project project = newProject();
       Iteration iteration = newIteration(project);
       UserStory story = newUserStory(iteration);
       Person person1 = newPerson(IdGenerator.getUniqueId("UserId"));
       Person person2 = newPerson(IdGenerator.getUniqueId("UserId"));
       Task task = newTask(story);
       commitCloseAndOpenSession();

       // test add & get
       TimeEntryData expected = new TimeEntryData();
       expected.setTaskId(task.getId());
       expected.setStartTime(newCalendar(2002, 2, 1, 0, 0, 0));
       expected.setEndTime(newCalendar(2002, 2, 1, 1, 0, 0));
       expected.setReportDate(newCalendar(2002, 2, 1));
       expected.setPerson1Id(person1.getId());
       expected.setPerson2Id(person2.getId());
       expected.setDescription("desc");
       TimeEntryData actual = xplanner.addTimeEntry(expected);
       int id = actual.getId();
       registerObjectToBeDeletedOnTearDown(getSession().get(TimeEntry.class, new Integer(id)));

       expected.setId(id);
       assertEquals(expected, actual, TIME_ENTRY_PROPERTIES);
       TimeEntryData[] entries = xplanner.getTimeEntries(task.getId());
       anAssert.assertEquals(new TimeEntryData[]{expected}, entries, TIME_ENTRY_PROPERTIES);

       // test update
       expected = actual;
       expected.setStartTime(newCalendar(2003, 2, 1, 0, 0, 0));
       expected.setEndTime(newCalendar(2003, 2, 1, 1, 0, 0));
       expected.setReportDate(newCalendar(2003, 2, 1));
       expected.setPerson1Id(expected.getPerson2Id());
       expected.setPerson2Id(expected.getPerson1Id());
       xplanner.update(expected);

       actual = xplanner.getTimeEntry(id);
       assertEquals(expected, actual, TIME_ENTRY_PROPERTIES);

       // test update removing persons.
       // ChangeSoon should not be possible to remove both persons ;-)
       expected.setPerson1Id(0);
       expected.setPerson2Id(0);
       xplanner.update(expected);

       actual = xplanner.getTimeEntry(id);
       assertEquals(expected, actual, TIME_ENTRY_PROPERTIES);

       commitCloseAndOpenSession();
       TimeEntry entry = (TimeEntry)getSession().load(TimeEntry.class, new Integer(id));

       assertEquals(entry, actual, TIME_ENTRY_PROPERTIES);

       xplanner.removeTimeEntry(id);
       assertNull(xplanner.getTimeEntry(id));

       commitCloseAndOpenSession();
    }


    /** Test person task queries.
     *
     * @throws Exception
     *             the exception
     */
    public void testPersonTaskQueries() throws Exception {
        Person person = newPerson();
        // Only checking that errors don't occur
        /*TaskData[] plannedTasks =*/ xplanner.getPlannedTasksForPerson(person.getId());
        /*TaskData[] currentTasks =*/ xplanner.getCurrentTasksForPerson(person.getId());
    }

   /** Test notes.
     *
     * @throws Exception
     *             the exception
     */
   public void testNotes() throws Exception {
       doGetNotesTest();
   }

   /** Test attributes.
     *
     * @throws Exception
     *             the exception
     */
   public void testAttributes() throws Exception {
       Project project = newProject();
       commitCloseAndOpenSession();

       xplanner.setAttribute(project.getId(), "test.foo", "bar");
       xplanner.setAttribute(project.getId(), "test.baz", "fargle");
       xplanner.setAttribute(project.getId(), "testx.baz", "fargle");

       Map attributes = xplanner.getAttributes(project.getId());
       assertEquals("wrong attribute count", 3, attributes.size());
       assertEquals("wrong value", "bar", attributes.get("test.foo"));
       assertEquals("wrong value", "fargle", attributes.get("test.baz"));
       assertEquals("wrong value", "fargle", attributes.get("testx.baz"));

       attributes = xplanner.getAttributesWithPrefix(project.getId(), "test.");
       assertEquals("wrong attribute count", 2, attributes.size());
       assertEquals("wrong value", "bar", attributes.get("foo"));
       assertEquals("wrong value", "fargle", attributes.get("baz"));

       xplanner.deleteAttribute(project.getId(), "test.foo");
       xplanner.deleteAttribute(project.getId(), "testx.baz");

       attributes = xplanner.getAttributes(project.getId());
       assertEquals("wrong attribute count", 1, attributes.size());
       assertEquals("wrong value", "fargle", attributes.get("test.baz"));

//        Project[] projects = xplanner.getProjectsWithAttribute("test.foo");
//        assertEquals("wrong count", 1, projects.length);
//        assertEquals("wrong project", project.getId(), projects[0].getId());
//
//        projects = xplanner.getProjectsWithAttributeAndValue("test.foo", "bar");
//        assertEquals("wrong count", 1, projects.length);
//        assertEquals("wrong project", project.getId(), projects[0].getId());
//
//        projects = xplanner.getProjectsWithAttributeAndValue("test.foo", "b%");
//        assertEquals("wrong count", 1, projects.length);
//        assertEquals("wrong project", project.getId(), projects[0].getId());
//
//        projects = xplanner.getProjectsWithAttributeAndValue("test.foo", "xyz");
//        assertEquals("wrong count", 0, projects.length);
   }


    /** Do get notes test.
     *
     * @throws Exception
     *             the exception
     */
    private void doGetNotesTest() throws Exception {
        List expectedNotes = getSession().find("from note in class " + Note.class.getName() + " where id < 1000");
        List soapNotes = new ArrayList();
        for (int i = 0; i < expectedNotes.size(); i++) {
            Note note = (Note)expectedNotes.get(i);
            soapNotes.add(xplanner.getNote(note.getId()));
        }
        // attachmentId is read-only!
        assertEqualCollections(expectedNotes, soapNotes, new String[]{
            "attachedToId", "authorId", "body", "subject", "submissionTime"});
    }

    /** Assert equal collections.
     *
     * @param expectedObjects
     *            the expected objects
     * @param actualObjects
     *            the actual objects
     * @param properties
     *            the properties
     * @throws Exception
     *             the exception
     */
    private void assertEqualCollections(List expectedObjects, List actualObjects, String[] properties)
            throws Exception {
        anAssert.assertEquals(expectedObjects, actualObjects, properties);
    }

    /** Assert equals.
     *
     * @param expectedObject
     *            the expected object
     * @param actualObject
     *            the actual object
     * @param properties
     *            the properties
     */
    private void assertEquals(Object expectedObject, Object actualObject, String[] properties) {
        anAssert.assertEquals(expectedObject, actualObject, properties);
    }

    /** Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /** Sets the user id.
     *
     * @param userId
     *            the new user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /** Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
