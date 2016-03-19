package com.technoetic.xplanner.acceptance;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.XPlannerLoginModule;

/**
 * The Class LargeDatabasePopulator.
 */
public class LargeDatabasePopulator {
    
    /** The log. */
    private static Logger log = Logger.getLogger(LargeDatabasePopulator.class);

    /** The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        try {
            HibernateHelper.initializeHibernate();
            for (int i = 0; i < 1; i++) {
                //new Thread(new PopulationTask(30,30,10,5, 100)).start();
                new Thread(new PopulationTask(0, 0, 0, 0, 100)).start();
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    /** The Class PopulationTask.
     */
    private static class PopulationTask implements Runnable {
        
        /** The log. */
        private final Logger log = Logger.getLogger(getClass());
        
        /** The project count. */
        private int projectCount = 100;
        
        /** The iteration count. */
        private int iterationCount = 100;
        
        /** The story count. */
        private int storyCount = 30;
        
        /** The task count. */
        private int taskCount = 5;
        
        /** The person count. */
        private int personCount = 100;
        
        /** The session. */
        private Session session;
        
        /** The Constant HOUR. */
        public static final long HOUR = 60 * 60 * 1000L;
        
        /** The Constant DAY. */
        public static final long DAY = 24 * HOUR;
        
        /** The iteration. */
        private Iteration iteration;

        /** Instantiates a new population task.
         *
         * @param projectCount
         *            the project count
         * @param iterationCount
         *            the iteration count
         * @param storyCount
         *            the story count
         * @param taskCount
         *            the task count
         * @param personCount
         *            the person count
         */
        public PopulationTask(int projectCount, int iterationCount, int storyCount,
                int taskCount, int personCount) {
            this.projectCount = projectCount;
            this.iterationCount = iterationCount;
            this.storyCount = storyCount;
            this.taskCount = taskCount;
            this.personCount = personCount;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                session = GlobalSessionFactory.get().openSession();
                for (int i = 0; i < projectCount; i++) {
                    Project project = newProject();
                    session.flush();
                    session.connection().commit();
                    log.info("project " + i);
                    for (int j = 0; j < iterationCount; j++) {
                        iteration = newIteration(project, iteration);
                        session.flush();
                        session.connection().commit();
                        log.info("iteration " + i + " " + j);
                        for (int k = 0; k < storyCount; k++) {
                            UserStory story = newUserStory(iteration);
                            session.flush();
                            session.connection().commit();
                            log.info("story " + i + " " + j + " " + k);
                            for (int l = 0; l < taskCount; l++) {
                                /*Task task = */newTask(story);
                                log.info("task " + i + " " + j + " " + k + " " + l);
                                session.flush();
                                session.connection().commit();
                            }
                        }
                        iteration = null;
                    }
                }
                List projects = session.find("from project in " + Project.class);
                XPlannerLoginModule encryptor = new XPlannerLoginModule(new LoginSupportImpl());
                for (int i = 0; i < personCount; i++) {
                    Person person = new Person();
                    session.save(person);
                    person.setName("Person " + person.getId());
                    String initials = "P" + person.getId();
                    person.setInitials(initials);
                    person.setPassword(encryptor.encodePassword(initials, null));
                    Role role = getRole(session, "sysadmin");
                    for (int j = 0; j < projects.size(); j++) {
                        Project project = (Project)projects.get(j);
                        session.save(new PersonRole(project.getId(), person.getId(), role.getId()));
                    }
                    System.out.println("person "+i);
                }
                session.flush();
                session.connection().commit();
            } catch (Exception e) {
                log.error("error", e);
            }
        }

        /** Gets the role.
         *
         * @param session
         *            the session
         * @param rolename
         *            the rolename
         * @return the role
         * @throws HibernateException
         *             the hibernate exception
         */
        public static Role getRole(Session session, String rolename) throws HibernateException {
            List roles = session.find("from role in class " +
                    Role.class.getName() + " where role.name = ?",
                    rolename, Hibernate.STRING);
            Role role = null;
            Iterator roleIterator = roles.iterator();
            if (roleIterator.hasNext()) {
                role = (Role)roleIterator.next();
            }
            return role;
        }

        /** New project.
         *
         * @return the project
         * @throws HibernateException
         *             the hibernate exception
         */
        public Project newProject() throws HibernateException {
            Project project = new Project();
            project.setName("Test project");
            session.save(project);
            project.setName("Test project " + project.getId());
            return project;
        }

        /** New iteration.
         *
         * @param project
         *            the project
         * @param previousIteration
         *            the previous iteration
         * @return the iteration
         * @throws HibernateException
         *             the hibernate exception
         */
        public Iteration newIteration(Project project, Iteration previousIteration) throws HibernateException {
            Iteration iteration = new Iteration();
            iteration.setProject(project);
            if (previousIteration == null) {
                iteration.setStartDate(new Date(System.currentTimeMillis() - DAY));
                iteration.setEndDate(new Date(System.currentTimeMillis() + DAY));
            } else {
                iteration.setStartDate(previousIteration.getEndDate());
                iteration.setEndDate(new Date(previousIteration.getEndDate().getTime() + DAY));

            }
            session.save(iteration);
            project.getIterations().add(iteration);
            iteration.setName("Test iteration");
            iteration.setName("Test iteration " + iteration.getId());
            return iteration;
        }

        /** New user story.
         *
         * @param iteration
         *            the iteration
         * @return the user story
         * @throws HibernateException
         *             the hibernate exception
         */
        public UserStory newUserStory(Iteration iteration) throws HibernateException {
            UserStory story = new UserStory();
            story.setName("Test userstory");
            story.setIteration(iteration);
            iteration.getUserStories().add(story);
            session.save(story);
            story.setName("Test userstory" + story.getId());
            return story;
        }

        /** New task.
         *
         * @param story
         *            the story
         * @return the task
         * @throws HibernateException
         *             the hibernate exception
         */
        public Task newTask(UserStory story) throws HibernateException {
            Task task = new Task();
            task.setUserStory(story);
            story.getTasks().add(task);
            task.setType("");
            task.setDisposition(TaskDisposition.PLANNED);
            task.setName("Test task");
            session.save(task);
            task.setName("Test task " + task.getId());
            return task;
        }

        /** New feature.
         *
         * @param story
         *            the story
         * @return the feature
         * @throws HibernateException
         *             the hibernate exception
         */
        public Feature newFeature(UserStory story) throws HibernateException {
        Feature feature = new Feature();
        feature.setStory(story);
        story.getFeatures().add(feature);
        feature.setDescription("Description of test feature");
        feature.setName("Test feature");
        session.save(feature);
        feature.setName("Test feature " + feature.getId());
        return feature;
    }
    }
}