package com.technoetic.xplanner.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.easymock.MockControl;
import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 * The Class TestIterationMetrics.
 */
public class TestIterationMetrics extends TestCase{
    
    /** The iteration metrics. */
    private IterationMetrics iterationMetrics;
    
    /** The person1 metrics. */
    private DeveloperMetrics person1Metrics;
    
    /** The person2 metrics. */
    private DeveloperMetrics person2Metrics;
    
    /** The Constant PERSON1_ID. */
    public static final int PERSON1_ID = 123;
    
    /** The Constant PERSON2_ID. */
    public static final int PERSON2_ID = 234;
    
    /** The PERSO n1_ name. */
    private final String PERSON1_NAME = "name1";
    
    /** The PERSO n2_ name. */
    private final String PERSON2_NAME = "name2";
    
    /** The Constant ITERATION_ID. */
    public static final int ITERATION_ID = 1;


    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
	protected void setUp() throws Exception {
        iterationMetrics = new FakeIterationMetrics(createTestIteration());
        iterationMetrics.getNamesMap(null);
        verifyTestNames();
    }

    /** Test calculate developer metrics.
     *
     * @throws Exception
     *             the exception
     */
    public void testCalculateDeveloperMetrics() throws Exception {
        iterationMetrics.calculateDeveloperMetrics();
        assertNotNull(iterationMetrics.developerMetrics);
        assertEquals("iteration story count",
                     3,
                     iterationMetrics.getIterationObject().getUserStories().size());
        assertEquals("metrics size", 3, iterationMetrics.developerMetrics.size());
        verifyPersonMetrics(person1Metrics, 0.0, 3.0, PERSON1_NAME);
        verifyPersonMetrics(person2Metrics, 4.5, 2.3, PERSON2_NAME);
        DeveloperMetrics unassignedHours = (DeveloperMetrics) iterationMetrics.developerMetrics.get(IterationMetrics.UNASSIGNED_NAME);
        verifyPersonMetrics(unassignedHours, 10.0, 7.7, IterationMetrics.UNASSIGNED_NAME);
    }

    /** Test calculate developer metrics with zero hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testCalculateDeveloperMetricsWithZeroHours() throws Exception {
        iterationMetrics = new FakeIterationMetrics(createNoHoursIteration());
        iterationMetrics.getNamesMap(null);
        iterationMetrics.calculateDeveloperMetrics();
        assertEquals("metrics size", 0, iterationMetrics.developerMetrics.size());

        DeveloperMetrics person1Metrics = (DeveloperMetrics) iterationMetrics.developerMetrics.get(PERSON1_NAME);
        assertNull(person1Metrics);
    }

    /** Test get metrics data with only story hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetMetricsDataWithOnlyStoryHours() throws Exception {
    	List<UserStory> stories = new ArrayList<UserStory>();
        UserStory story = new UserStory();
        story.setEstimatedHoursField(1.0);
        story.setTrackerId(PERSON2_ID);
        stories.add(story);
        Map metrics = iterationMetrics.getMetricsData(stories);

        DeveloperMetrics developerMetricsForSecondPerson = (DeveloperMetrics) metrics.get(PERSON2_NAME);

        assertEquals("developer name", PERSON2_NAME, developerMetricsForSecondPerson.getName());
        assertEquals(1.0, developerMetricsForSecondPerson.getAcceptedStoryHours(), 0.0);
        assertEquals(0.0, developerMetricsForSecondPerson.getAcceptedTaskHours(), 0.0);

        DeveloperMetrics developerMetricsForFirstPerson = (DeveloperMetrics) metrics.get(PERSON1_NAME);
        assertEquals("developer name", PERSON1_NAME, developerMetricsForFirstPerson.getName());
        assertEquals(0.0, developerMetricsForFirstPerson.getAcceptedStoryHours(), 0.0);
        assertEquals(0.0, developerMetricsForFirstPerson.getAcceptedTaskHours(), 0.0);

    }

    /** Test get metrics data with story and task hours.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetMetricsDataWithStoryAndTaskHours() throws Exception {
        Collection stories = createListOfStories();

        HashMap metrics = iterationMetrics.getMetricsData(stories);

        DeveloperMetrics person2Metrics = (DeveloperMetrics) metrics.get(PERSON2_NAME);
        verifyPersonMetrics(person2Metrics, 4.5, 2.3, PERSON2_NAME);

        DeveloperMetrics person1Metrics = (DeveloperMetrics) metrics.get(PERSON1_NAME);
        verifyPersonMetrics(person1Metrics, 0.0, 3.0, PERSON1_NAME);
    }

    /** Test assign hours to user.
     *
     * @throws Exception
     *             the exception
     */
    public void testAssignHoursToUser() throws Exception {
        assertEquals(PERSON1_NAME,iterationMetrics.getName(PERSON1_ID));
        iterationMetrics.assignHoursToUser(PERSON1_ID, 2.4, true);
        assertEquals("person name", PERSON1_NAME, person1Metrics.getName());
        assertEquals("story hour", 2.4, person1Metrics.getAcceptedStoryHours(), 0.0);
    }

    /** Verify test names.
     */
    private void verifyTestNames() {
        assertEquals("total names", 3, iterationMetrics.names.size());
        person1Metrics = iterationMetrics.getDeveloperMetrics(PERSON1_NAME, PERSON1_ID, ITERATION_ID);
        person2Metrics = iterationMetrics.getDeveloperMetrics(PERSON2_NAME, PERSON2_ID, ITERATION_ID);
        assertNotSame(person1Metrics, person2Metrics);
        assertTrue(person1Metrics.getId() != person2Metrics.getId());
        assertEquals("person1 id", PERSON1_ID, person1Metrics.getId());
        assertEquals("person2 id", PERSON2_ID, person2Metrics.getId());

        String unassignedPerson = iterationMetrics.getName(IterationMetrics.UNASSIGNED_ID);
        assertEquals("unassigned name", IterationMetrics.UNASSIGNED_NAME, unassignedPerson);
    }

    /** Verify person metrics.
     *
     * @param person1Metrics
     *            the person1 metrics
     * @param expectedStoryHours
     *            the expected story hours
     * @param expectedTaskHours
     *            the expected task hours
     * @param expectedPersonName
     *            the expected person name
     */
    private void verifyPersonMetrics(DeveloperMetrics person1Metrics,
                                     double expectedStoryHours,
                                     double expectedTaskHours,
                                     String expectedPersonName) {
        assertEquals(expectedStoryHours, person1Metrics.getAcceptedStoryHours(), 0.0);
        assertEquals(expectedTaskHours, person1Metrics.getAcceptedTaskHours(), 0.0);
        assertEquals("person name", expectedPersonName, person1Metrics.getName());
    }

    /** Creates the test iteration.
     *
     * @return the iteration
     */
    private Iteration createTestIteration() {
        Iteration iteration = new Iteration();
        iteration.setUserStories(createListOfStories());
        return iteration;
    }

    /** Creates the no hours iteration.
     *
     * @return the iteration
     */
    private Iteration createNoHoursIteration() {
        Iteration iteration = new Iteration();
        List<UserStory> stories = new ArrayList<UserStory>();
        UserStory story1 = createUserStory(PERSON1_ID, 0);
        stories.add(story1);
        iteration.setUserStories(stories);
        return iteration;
    }


    /** Creates the list of stories.
     *
     * @return the list
     */
    private List<UserStory> createListOfStories() {
        UserStory story1 = createUserStory(PERSON1_ID, 1.0);
        story1.setTasks(createListOfTasks());
        UserStory story2 = createUserStory(PERSON2_ID, 4.5);
        UserStory story3 = createUserStory(IterationMetrics.UNASSIGNED_ID, 10);

        List<UserStory> stories = new ArrayList<UserStory>();
        stories.add(story1);
        stories.add(story2);
        stories.add(story3);
        return stories;
    }

    /** Creates the list of tasks.
     *
     * @return the list
     */
    private List<Task> createListOfTasks() {
        List<Task> tasks = new ArrayList<Task>();
        Task task1 = createTask(PERSON1_ID, 3.0);
        Task task2 = createTask(PERSON2_ID, 2.3);
        Task task3 = createTask(IterationMetrics.UNASSIGNED_ID, 7.7);

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        return tasks;
    }

    /** Creates the user story.
     *
     * @param personId
     *            the person id
     * @param estimatedHours
     *            the estimated hours
     * @return the user story
     */
    private UserStory createUserStory(int personId, double estimatedHours) {
        UserStory story = new UserStory();
        story.setTrackerId(personId);
        story.setEstimatedHoursField(estimatedHours);
        return story;
    }

    /** Creates the task.
     *
     * @param personId
     *            the person id
     * @param estimatedHours
     *            the estimated hours
     * @return the task
     */
    private Task createTask(int personId, double estimatedHours) {
        Task task = new Task();
        task.setAcceptorId(personId);
        task.setEstimatedHours(estimatedHours);
        return task;
    }

    /** Test get hours worked.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetHoursWorked() throws Exception {
        MockControl mockSessionControl;
        Session mockSession;
        mockSessionControl = MockControl.createControl(Session.class);
        mockSession = (Session) mockSessionControl.getMock();
        MockControl mockQueryControl = MockControl.createControl(Query.class);
        Query mockQuery = (Query) mockQueryControl.getMock();
        List resultList = new ArrayList();
        resultList.add(new Object[]{new Integer(PERSON1_ID), new Integer(0), null, null, new Double(1.0), new Integer(PERSON1_ID)});
        resultList.add(new Object[]{new Integer(PERSON1_ID), new Integer(PERSON2_ID), new Date(0), new Date(7200000), null, new Integer(PERSON1_ID)});
        resultList.add(new Object[]{new Integer(PERSON2_ID), new Integer(0), null, null, new Double(3.0), new Integer(PERSON2_ID)});
        resultList.add(new Object[]{new Integer(PERSON1_ID), new Integer(PERSON2_ID), null, null, new Double(4.0), new Integer(PERSON2_ID)});
        mockQueryControl.expectAndReturn(mockQuery.setInteger("iterationId", 0), mockQuery);
        mockQueryControl.expectAndReturn(mockQuery.list(), resultList);
        mockSessionControl.expectAndReturn(mockSession.getNamedQuery("iterationHoursWorkedQuery"), mockQuery);
        mockSessionControl.replay();
        mockQueryControl.replay();
        iterationMetrics.getHoursWorked(mockSession, "iterationHoursWorkedQuery", iterationMetrics.names);
        mockSessionControl.verify();
        mockQueryControl.verify();
        assertEquals("Wrong person1 worked hours", 7.0, iterationMetrics.getDeveloperMetrics(PERSON1_NAME, PERSON1_ID, ITERATION_ID).getHours(), 0.0);
        assertEquals("Wrong person2 worked hours", 9.0, iterationMetrics.getDeveloperMetrics(PERSON2_NAME, PERSON2_ID, ITERATION_ID).getHours(), 0.0);
        assertEquals("Wrong person1 own tasks worked hours", 3.0, iterationMetrics.getDeveloperMetrics(PERSON1_NAME, PERSON1_ID, ITERATION_ID).getOwnTaskHours(), 0.0);
        assertEquals("Wrong person1 own tasks worked hours", 7.0, iterationMetrics.getDeveloperMetrics(PERSON2_NAME, PERSON2_ID, ITERATION_ID).getOwnTaskHours(), 0.0);
    }

    /** The Class FakeIterationMetrics.
     */
    private class FakeIterationMetrics extends IterationMetrics {
        
        /** The no hours iteration. */
        private final Iteration noHoursIteration;

        /** Instantiates a new fake iteration metrics.
         *
         * @param iteration
         *            the iteration
         */
        public FakeIterationMetrics(Iteration iteration) {
            this.noHoursIteration = iteration;
        }

        /* (non-Javadoc)
         * @see com.technoetic.xplanner.metrics.IterationMetrics#getIterationObject()
         */
        @Override
		protected Iteration getIterationObject() {
            return noHoursIteration;
        }

        /* (non-Javadoc)
         * @see com.technoetic.xplanner.metrics.IterationMetrics#getNamesMap(org.hibernate.classic.Session)
         */
        @Override
		protected void getNamesMap(Session session) {
            names.put(new Integer(PERSON1_ID), PERSON1_NAME);
            names.put(new Integer(PERSON2_ID), PERSON2_NAME);
            addUnassignedName();
        }
    }
}