package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.File;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.technoetic.mocks.hibernate.MockQuery;
import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class ContinuerBaseTestCase.
 */
// DEBT(SETUP) Use ObjectMother to simplify the setup of business objects
public abstract class ContinuerBaseTestCase extends AbstractUnitTestCase {
   
   /** The Constant ITERATION_NAME. */
   protected static final String ITERATION_NAME = "Test Iteration";
   
   /** The Constant TARGET_ITERATION_NAME. */
   protected static final String TARGET_ITERATION_NAME = "Target Test Iteration";
   
   /** The Constant STORY_DESCRIPTION. */
   protected static final String STORY_DESCRIPTION = "Story description.";
   
   /** The Constant STORY_NAME. */
   protected static final String STORY_NAME = "Test Story";
   
   /** The Constant DESCRIPTION. */
   protected static final String DESCRIPTION = "";

   /** The iteration. */
   protected Iteration iteration;
   
   /** The target iteration. */
   protected Iteration targetIteration;
   
   /** The story. */
   protected UserStory story;
   
   /** The incomplete task1. */
   protected Task incompleteTask1;
   
   /** The incomplete task2. */
   protected Task incompleteTask2;
   
   /** The notes list. */
   private List notesList;
   
   /** The story note. */
   private Note storyNote;

   /** The initial postponed hours. */
   private final double initialPostponedHours = 4.0;
   
   /** The story continuer. */
   protected StoryContinuer storyContinuer;
   
   /** The task continuer. */
   protected TaskContinuer taskContinuer;
   
   /** The meta data repository. */
   protected DomainMetaDataRepository metaDataRepository;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   @Override
protected void setUp() throws Exception {
      super.setUp();

      support = new XPlannerTestSupport();
      ThreadSession.set(support.hibernateSession);

      support.resources.setMessage("continue.description.from", "Continued as {2} in {3}");
      support.resources.setMessage("continue.description.to", "Continued from {0} in {1}");
      support.resources.setMessage("continue.description.to_parent", "Continued {2} from {0} in {1}");
      support.resources.setMessage("continue.description.from_parent", "Continued {0} as {2} in {3}");
      support.resources.setMessage("story.disposition.continued", "Continued");
      support.resources.setMessage("story.disposition.spilled", "Spilled");
      support.resources.setMessage("task.continued.to", "Continued as task:{0}");
      support.resources.setMessage("task.continued.from", "Continued from task:{0}");

      support.hibernateSession.saveIdProperty = "id";
      support.hibernateSession.saveIds = new Object[]{
            new Integer(1111),
            new Integer(2222),
            new Integer(3333),
            new Integer(4444),
            new Integer(5555),
            new Integer(6666),
            new Integer(7777),
            new Integer(8888),
            new Integer(9999),
            new Integer(1121),
            new Integer(1122),
            new Integer(1123),
            new Integer(1124),
            new Integer(1125),
            new Integer(1126),
            new Integer(1127),
            new Integer(1128),
            new Integer(1129),
            new Integer(1211),
            new Integer(1212),
            new Integer(1213),
            new Integer(1214),
            new Integer(1215),
            new Integer(1216),
            new Integer(1217),
            new Integer(1218),
            new Integer(1219),
            new Integer(1311),
            new Integer(1312),
            new Integer(1313),
            new Integer(1314)
      };

      support.setUpSubjectInRole("editor");
      mockObjectRepository = createMockObjectRepository();
      mockMetaRepository = createMockMetaRepository(mockObjectRepository);
   }

   /** Sets the up continuers.
     *
     * @throws AuthenticationException
     *             the authentication exception
     */
   protected void setUpContinuers() throws AuthenticationException {
      storyContinuer = new StoryContinuer();
      taskContinuer = new TaskContinuer();
      metaDataRepository = DomainMetaDataRepository.getInstance();
      storyContinuer.setTaskContinuer(taskContinuer);
      taskContinuer.setMetaDataRepository(metaDataRepository);
      storyContinuer.setMetaDataRepository(metaDataRepository);
      storyContinuer.init(support.hibernateSession, support.request);
   }
   
   /** Gets the saved instances of.
     *
     * @param objectClass
     *            the object class
     * @return the saved instances of
     */
   protected List getSavedInstancesOf(Class objectClass) {
      ArrayList foundObjects = new ArrayList();
      Iterator it = support.hibernateSession.saveObjects.iterator();
      while (it.hasNext()) {
         Object obj = it.next();
         if (objectClass.isAssignableFrom(obj.getClass())) {
            foundObjects.add(obj);
         }
      }
      return foundObjects;
   }

   /** Assert task properties.
     *
     * @param originalTask
     *            the original task
     * @param continuedTask
     *            the continued task
     */
   protected void assertTaskProperties(Task originalTask, Task continuedTask) {
      assertEquals("acceptor", 0, continuedTask.getAcceptorId());
      assertEquals("name", originalTask.getName(), continuedTask.getName());
      assertTrue("task id should be different", originalTask.getId() != continuedTask.getId());
      assertTrue("time", originalTask.getCreatedDate().getTime() <= continuedTask.getCreatedDate().getTime());
      assertEquals("original task description.",
                   "Continued as task:" +
                   continuedTask.getId() +
                   " in story:" +
                   continuedTask.getUserStory().getId() +
                   "\n\n",
                   originalTask.getDescription());
      assertEquals("continued task description.",
                   "Continued from task:" +
                   originalTask.getId() +
                   " in story:" +
                   originalTask.getUserStory().getId() +
                   "\n\n",
                   continuedTask.getDescription());
      assertEquals("task name not same", originalTask.getName(), continuedTask.getName());
      assertTrue("task create time",
                 originalTask.getCreatedDate().getTime() < continuedTask.getCreatedDate().getTime());
      assertEquals("task hours", originalTask.getEstimatedHours() - originalTask.getActualHours(),
                   continuedTask.getEstimatedHours(), 0);
      assertEquals("original task new estimated hours", 5, originalTask.getEstimatedHours(), 0);
      assertEquals("original task original estimated hours", 2, originalTask.getEstimatedOriginalHours(), 0);
      assertEquals("continued task estimated hours", 3, continuedTask.getEstimatedHours(), 0);
      assertEquals("continued task actual hours", 0, continuedTask.getActualHours(), 0);
   }

   /** Creates the task.
     *
     * @param taskId
     *            the task id
     * @param acceptorId
     *            the acceptor id
     * @param estimatedHours
     *            the estimated hours
     * @param estimatedOriginalHours
     *            the estimated original hours
     * @param timeEntries
     *            the time entries
     * @return the task
     */
   protected Task createTask(int taskId,
                             int acceptorId,
                             double estimatedHours,
                             double estimatedOriginalHours,
                             ArrayList timeEntries) {
      Task task = new Task();
      task.setId(taskId);
      task.setAcceptorId(acceptorId);
      task.setEstimatedHours(estimatedHours);
      task.setEstimatedOriginalHours(estimatedOriginalHours);
      task.setTimeEntries(timeEntries);
      task.setCreatedDate(new Date(0));
      task.setDescription(DESCRIPTION);
      return task;
   }


   /** Creates the time entries.
     *
     * @return the array list
     */
   protected ArrayList createTimeEntries() {
      long now = new Date().getTime();
      TimeEntry t1 = new TimeEntry();
      t1.setStartTime(new Date(now - 3600000));
      t1.setEndTime(new Date(now));
      TimeEntry t2 = new TimeEntry();
      t2.setStartTime(new Date(now + 3600000));
      t2.setEndTime(new Date(now + 7200000));
      ArrayList timeEntries = new ArrayList();
      timeEntries.add(t1);
      timeEntries.add(t2);
      return timeEntries;
   }

   /** Sets the up target iteration.
     *
     * @throws RepositoryException
     *             the repository exception
     * @throws HibernateException
     *             the hibernate exception
     */
   protected void setUpTargetIteration() throws RepositoryException, HibernateException {
      targetIteration = new Iteration();
      targetIteration.setName(TARGET_ITERATION_NAME);
      targetIteration.setStartDate(new Date(System.currentTimeMillis() + 2880));
      mockObjectRepository.insert(targetIteration);
   }

   /** Sets the up tasks story and iteration.
     *
     * @throws HibernateException
     *             the hibernate exception
     * @throws RepositoryException
     *             the repository exception
     */
   protected void setUpTasksStoryAndIteration() throws HibernateException, RepositoryException {
      incompleteTask1 = createTask(1, 1010, 5.0, 2.0, createTimeEntries());
      incompleteTask2 = createTask(2, 1212, 5.0, 2.0, createTimeEntries());
      Task completedTask = createTask(3, 1313, 6.0, 4.0, new ArrayList());
      incompleteTask1.setCompleted(false);
      incompleteTask2.setCompleted(false);
      completedTask.setCompleted(true);
      support.hibernateSession.save(incompleteTask1);
      support.hibernateSession.save(incompleteTask2);
      support.hibernateSession.save(completedTask);

      ArrayList tasks = new ArrayList();
      tasks.add(incompleteTask1);
      tasks.add(incompleteTask2);
      tasks.add(completedTask);

      story = new UserStory();
      story.setId(9999);
      story.setTasks(tasks);
      story.setName(STORY_NAME);
      story.setCustomer(new Person());
      story.setDescription(STORY_DESCRIPTION);
      story.setPostponedHours(initialPostponedHours);
      support.hibernateSession.save(story);

      incompleteTask1.setUserStory(story);
      incompleteTask2.setUserStory(story);
      completedTask.setUserStory(story);

      iteration = new Iteration();
      iteration.setName(ITERATION_NAME);
      iteration.setStartDate(new Date(System.currentTimeMillis() + 1440));
      ArrayList stories = new ArrayList();
      stories.add(story);
      iteration.setUserStories(stories);
      mockObjectRepository.insert(iteration);
      support.hibernateSession.save(iteration);
      story.setIteration(iteration);

      createNotesListFor(story);
      storyNote = (Note) notesList.get(0);
      createNotesListFor(incompleteTask1);
   }

   /** Creates the mock meta repository.
     *
     * @param objectRepository
     *            the object repository
     * @return the meta repository
     */
   protected MetaRepository createMockMetaRepository(final ObjectRepository objectRepository) {
      return new MetaRepository() {

         public ObjectRepository getRepository(Class objectClass) {
            return objectRepository;
         }

         public void setRepository(Class objectClass, ObjectRepository repository) {
         }
      };
   }

   /** Creates the mock object repository.
     *
     * @return the object repository
     */
   protected ObjectRepository createMockObjectRepository() {
      return new ObjectRepository() {
         public void delete(int objectIdentifier) {
         }

         public Object load(int objectIdentifier) {
            return iteration;
         }

         public int insert(Nameable object) {
            return 0;
         }

         public void update(Nameable object) {

         }
      };
   }

   /** Verify continued story.
     *
     * @param continuedStory
     *            the continued story
     */
   protected void verifyContinuedStory(UserStory continuedStory) {
      assertEquals("iteration ID", 6666, continuedStory.getIteration().getId());
      assertEquals("name", story.getName(), continuedStory.getName());
      assertEquals("customer", story.getCustomer(), continuedStory.getCustomer());
      assertEquals("priority", story.getPriority(), continuedStory.getPriority());
      assertEquals("continued story disposition", StoryDisposition.CARRIED_OVER, continuedStory.getDisposition());
      assertEquals("original story disposition", StoryDisposition.PLANNED, story.getDisposition());
      assertEquals("original estimated hours", 0.0d, story.getTaskBasedRemainingHours(), 0.0d);
      assertEquals("story desc.",
                   "Continued as story:" + continuedStory.getId() + " in iteration:" +
                   continuedStory.getIteration().getId() + "\n\n" + STORY_DESCRIPTION,
                   story.getDescription());
      assertEquals("continued story desc.",
                   "Continued from story:" +
                   story.getId() +
                   " in iteration:" +
                   story.getIteration().getId() +
                   "\n\n" +
                   STORY_DESCRIPTION,
                   continuedStory.getDescription());
      List allSavedTasks = getSavedInstancesOf(Task.class);
      List continuedTasks = new ArrayList();
      for (Iterator iterator = allSavedTasks.iterator(); iterator.hasNext();) {
         Task task = (Task) iterator.next();
         if (task.getUserStory().getId() == continuedStory.getId()) {
            continuedTasks.add(task);
         }
      }
      assertEquals("# of tasks", 2, continuedTasks.size());
//        List features = getSavedInstancesOf(Feature.class);
//        assertEquals("# of features", 2, features.size());

      Task task = (Task) continuedTasks.get(0);
      assertTaskProperties(incompleteTask1, task);
      assertSame("story", continuedStory, task.getUserStory());
      task = (Task) continuedTasks.get(1);
      assertSame("story", continuedStory, task.getUserStory());
      assertTaskProperties(incompleteTask2, task);
      double uncompletedTaskRemainingHours = getUncompletedTaskRemaingHours(incompleteTask1) +
                                             getUncompletedTaskRemaingHours(incompleteTask2);
      double postponedHours = initialPostponedHours + uncompletedTaskRemainingHours;
      assertEquals("postponed hours", postponedHours, story.getPostponedHours(), 0.0d);

      assertEquals("continued note continuation - story id", storyNote.getAttachedToId(), story.getId());
      int numberOfFileAttachments = 0;
      try {
         assertEquals("note continuation - number of file attachment references",
                      numberOfFileAttachments,
                      storyNote.getAttachmentCount());
      } catch (HibernateException e) {
         fail(e.getMessage());
      }
   }

   /** Gets the uncompleted task remaing hours.
     *
     * @param task
     *            the task
     * @return the uncompleted task remaing hours
     */
   private double getUncompletedTaskRemaingHours(Task task) {
      return Math.max(task.getEstimatedHours() - incompleteTask1.getActualHours(), 0.0);
   }

   /** Put content on mock query.
     */
   protected void putContentOnMockQuery() {
      support.hibernateSession.iterateReturn = notesList.iterator();
      MockNoteQuery query = createMockQuery();
      support.hibernateSession
            .getNamedQueryReturnMap
            .put(Note.class.getName() + Note.ATTACHED_NOTES_QUERY, query);
   }

   /** Creates the mock query.
     *
     * @return the mock note query
     */
   private MockNoteQuery createMockQuery() {
      return new MockNoteQuery(notesList);
   }

   /** Creates the notes list for.
     *
     * @param attachedToObject
     *            the attached to object
     */
   protected void createNotesListFor(DomainObject attachedToObject) {
      File file = new File();
      Note note = new Note();
      file.setId(1);
      file.setName("file.txt");
      file.setFileSize(3);
      file.setContentType("text/plain");
      file.setData(Hibernate.createBlob("data".getBytes(), mockSession));
      note.setAttachedToId(attachedToObject.getId());
      note.setSubject("TestNoteSubject");
      note.setBody("TestNoteBody");
      note.setFile(file);
      if (notesList == null) {
         notesList = new ArrayList();
      }
      notesList.add(note);
   }

   /** Assert history in object.
     *
     * @param object
     *            the object
     * @param description
     *            the description
     * @param eventType
     *            the event type
     */
   protected void assertHistoryInObject(DomainObject object, String description, String eventType) {
      support.assertHistoryInObject(object.getId(),
                                            eventType,
                                            description,
                                            XPlannerTestSupport.DEFAULT_PERSON_ID);
   }

   /** Verify notes being continued.
     *
     * @param expectedNoteCount
     *            the expected note count
     */
   protected void verifyNotesBeingContinued(int expectedNoteCount) {
      List savedNotes = getSavedInstancesOf(Note.class);
      assertEquals("number of saved notes", expectedNoteCount, savedNotes.size());
   }

   /** The Class MockNoteQuery.
     */
   private static class MockNoteQuery extends MockQuery {
      
      /** The notes list. */
      private final List notesList;

      /** Instantiates a new mock note query.
         *
         * @param notesList
         *            the notes list
         */
      public MockNoteQuery(List notesList) {
         this.notesList = notesList;
      }

      /* (non-Javadoc)
       * @see com.technoetic.mocks.hibernate.MockQuery#setString(java.lang.String, java.lang.String)
       */
      @Override
    public Query setString(String name, String val) {
         listReturn = new ArrayList();
         for (Iterator iterator = notesList.iterator(); iterator.hasNext();) {
            Note note = (Note) iterator.next();
            if (note.getAttachedToId() == Integer.valueOf(val).intValue()) {
               listReturn.add(note);
            }
         }
         return this;
      }

      /* (non-Javadoc)
       * @see com.technoetic.mocks.hibernate.MockQuery#iterate()
       */
      @Override
    public Iterator iterate() {
         return listReturn.iterator();
      }
   }
}
