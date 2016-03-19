package com.technoetic.xplanner.tags;

import java.util.Date;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.SelectTag;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.ObjectMother;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class AbstractOptionsTagTestCase.
 */
public class AbstractOptionsTagTestCase extends TestCase {
    
    /** The support. */
    protected XPlannerTestSupport support;
    
    /** The tag. */
    protected OptionsTag tag;
    
    /** The authorizer. */
    protected MockAuthorizer authorizer;
    
    /** The Constant DAY. */
    public static final long DAY = ObjectMother.DAY;
    
    /** The next id. */
    private int nextId;
    
    /** The Constant ITERATION_PREFIX. */
    public static final String ITERATION_PREFIX = "Test iteration ";
    
    /** The Constant PROJECT_PREFIX. */
    public static final String PROJECT_PREFIX = "Test project ";
    
    /** The Constant STORY_PREFIX. */
    public static final String STORY_PREFIX = "Test story";

    /** The PROJEC t1. */
    //ChangeSoon Extract these variable to an ObjectMother pattern. Share implementation with AbstractDatabaseTestScript
    protected static Project PROJECT1;
    
    /** The PROJEC t2. */
    protected static Project PROJECT2;
    
    /** The ITERATIO n_0_1. */
    protected static Iteration ITERATION_0_1;
    
    /** The ITERATIO n_1_1. */
    protected static Iteration ITERATION_1_1;
    
    /** The ITERATIO n_2_1. */
    protected static Iteration ITERATION_2_1;
    
    /** The ITERATIO n_0_2. */
    protected static Iteration ITERATION_0_2;
    
    /** The Constant TASK_PREFIX. */
    private static final String TASK_PREFIX = "Test Task";

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
	protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        support.setUpSubject(XPlannerTestSupport.DEFAULT_PERSON_USER_ID,new String[]{});
        authorizer = new MockAuthorizer();
        SystemAuthorizer.set(authorizer);
        ThreadSession.set(support.hibernateSession);
        SelectTag selectTag = new SelectTag();
        support.pageContext.setAttribute(Constants.SELECT_KEY, selectTag);
        tag.setPageContext(support.pageContext);
        tag.setAuthorizer(authorizer);
        tag.setSession(support.hibernateSession);
        tag.setLoggedInUserId(XPlannerTestSupport.DEFAULT_PERSON_ID);
        PROJECT1 = newProject();
        PROJECT2 = newProject();
        ITERATION_0_1 = newIteration(PROJECT1, DAY * 4);
        ITERATION_1_1 = newIteration(PROJECT1, DAY * 3);
        ITERATION_2_1 = newIteration(PROJECT1, DAY * 2);
        ITERATION_0_2 = newIteration(PROJECT2, DAY * 1);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
	protected void tearDown() throws Exception {
        SystemAuthorizer.set(null);
        ThreadSession.set(null);
        super.tearDown();
    }

    /** New project.
     *
     * @return the project
     */
    // ChangeSoon: remove test object factory duplication (AbstractDatabaseTestScript)
    public Project newProject() {
        Project project = new Project();
        project.setId(nextId++);
        project.setName(PROJECT_PREFIX + project.getId());
        return project;
    }

    /** New iteration.
     *
     * @param project
     *            the project
     * @param numberOfDays
     *            the number of days
     * @return the iteration
     */
    public Iteration newIteration(Project project, long numberOfDays) {
        Iteration iteration = new Iteration();
        iteration.setProject(project);
        iteration.setStartDate(new Date(System.currentTimeMillis() - numberOfDays));
        iteration.setEndDate(new Date(System.currentTimeMillis() - numberOfDays + 1));
        project.getIterations().add(iteration);
        iteration.setId(nextId++);
        iteration.setName(ITERATION_PREFIX + iteration.getId());
        return iteration;
    }

    /** New user story.
     *
     * @param iteration
     *            the iteration
     * @return the user story
     */
    public UserStory newUserStory(Iteration iteration) {
        UserStory story = new UserStory();
        story.setId(nextId++);
        story.setName(STORY_PREFIX + iteration.getId());
        iteration.getUserStories().add(story);
        return story;
    }

    /** New task.
     *
     * @param story
     *            the story
     * @return the task
     */
    protected Task newTask(UserStory story) {
        Task task = new Task();
        task.setUserStory(story);
        task.setId(nextId++);
        task.setName(TASK_PREFIX + story.getId());
        story.getTasks().add(task);
        return task;
    }
}