package com.technoetic.xplanner.tags;

import java.util.List;

import javax.servlet.ServletRequest;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.OLDIdSearchHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Feature;

// todo - Get rid of this once we have a true object model with relationships

/**
 * The Class DomainContext.
 */
public class DomainContext {
    
    /** The Constant REQUEST_KEY. */
    public static final String REQUEST_KEY = "domainContext";

    /** The project name. */
    private String projectName;
    
    /** The project id. */
    private int projectId;
    
    /** The iteration name. */
    private String iterationName;
    
    /** The iteration id. */
    private int iterationId;
    
    /** The story name. */
    private String storyName;
    
    /** The story id. */
    private int storyId;
    
    /** The task name. */
    private String taskName;
    
    /** The task id. */
    private int taskId;
    
    /** The feature name. */
    private String featureName;
    
    /** The feature id. */
    private int featureId;
    
    /** The target object. */
    private Object targetObject;
    
    /** The action mapping. */
    private ActionMapping actionMapping;
    
    /** The id search helper. */
    private static OLDIdSearchHelper idSearchHelper = new OLDIdSearchHelper();

    /**
     * Gets the action mapping.
     *
     * @return the action mapping
     */
    public ActionMapping getActionMapping() {
        return this.actionMapping;
    }

    /**
     * Sets the action mapping.
     *
     * @param actionMapping
     *            the new action mapping
     */
    public void setActionMapping(final ActionMapping actionMapping) {
        this.actionMapping = actionMapping;
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     */
    public String getProjectName() {
        return this.projectName;
    }

    /**
     * Sets the project name.
     *
     * @param projectName
     *            the new project name
     */
    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    public int getProjectId() {
        return this.projectId;
    }

    /**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
    public void setProjectId(final int projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the iteration name.
     *
     * @return the iteration name
     */
    public String getIterationName() {
        return this.iterationName;
    }

    /**
     * Sets the iteration name.
     *
     * @param iterationName
     *            the new iteration name
     */
    public void setIterationName(final String iterationName) {
        this.iterationName = iterationName;
    }

    /**
     * Gets the iteration id.
     *
     * @return the iteration id
     */
    public int getIterationId() {
        return this.iterationId;
    }

    /**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
    public void setIterationId(final int iterationId) {
        this.iterationId = iterationId;
    }

    /**
     * Gets the story name.
     *
     * @return the story name
     */
    public String getStoryName() {
        return this.storyName;
    }

    /**
     * Sets the story name.
     *
     * @param storyName
     *            the new story name
     */
    public void setStoryName(final String storyName) {
        this.storyName = storyName;
    }

    /**
     * Gets the story id.
     *
     * @return the story id
     */
    public int getStoryId() {
        return this.storyId;
    }

    /**
     * Sets the story id.
     *
     * @param storyId
     *            the new story id
     */
    public void setStoryId(final int storyId) {
        this.storyId = storyId;
    }

    /**
     * Gets the task name.
     *
     * @return the task name
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Sets the task name.
     *
     * @param taskName
     *            the new task name
     */
    public void setTaskName(final String taskName) {
        this.taskName = taskName;
    }

    /**
     * Gets the task id.
     *
     * @return the task id
     */
    public int getTaskId() {
        return this.taskId;
    }

    /**
     * Gets the feature name.
     *
     * @return the feature name
     */
    public String getFeatureName() {
        return this.featureName;
    }

    /**
     * Sets the feature name.
     *
     * @param featureName
     *            the new feature name
     */
    public void setFeatureName(final String featureName) {
        this.featureName = featureName;
    }

    /**
     * Gets the feature id.
     *
     * @return the feature id
     */
    public int getFeatureId() {
        return this.featureId;
    }

    /**
     * Sets the feature id.
     *
     * @param featureId
     *            the new feature id
     */
    public void setFeatureId(final int featureId) {
        this.featureId = featureId;
    }

    /**
     * Sets the task id.
     *
     * @param taskId
     *            the new task id
     */
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }

    /**
     * Gets the target object.
     *
     * @return the target object
     */
    public Object getTargetObject() {
        return this.targetObject;
    }

    /**
     * Populate.
     *
     * @param object
     *            the object
     * @throws Exception
     *             the exception
     */
    public void populate(final Object object) throws Exception {
        final Session session = ThreadSession.get();
        this.targetObject = object;
        if (object instanceof Project) {
            this.populate((Project) object);
        } else if (object instanceof Iteration) {
            this.populate(session, (Iteration) object);
        } else if (object instanceof UserStory) {
            this.populate(session, (UserStory) object);
        } else if (object instanceof Task) {
            this.populate(session, (Task) object);
        } else if (object instanceof Feature) {
            this.populate(session, (Feature) object);
        } else if (object instanceof Note) {
            this.populate((Note) object);
        } else if (object instanceof TimeEntry) {
            this.populate(session, (TimeEntry) object);
        }
    }

    /**
     * Populate.
     *
     * @param project
     *            the project
     * @throws Exception
     *             the exception
     */
    private void populate(final Project project) throws Exception {
        this.setProjectName(project.getName());
        this.setProjectId(project.getId());
    }

    /**
     * Populate.
     *
     * @param session
     *            the session
     * @param iteration
     *            the iteration
     * @throws Exception
     *             the exception
     */
    private void populate(final Session session, final Iteration iteration)
            throws Exception {
        final Project project = (Project) session.load(Project.class,
                new Integer(iteration.getProject().getId()));
        this.populate(project);
        this.setIterationName(iteration.getName());
        this.setIterationId(iteration.getId());
    }

    /**
     * Populate.
     *
     * @param session
     *            the session
     * @param story
     *            the story
     * @throws Exception
     *             the exception
     */
    private void populate(final Session session, final UserStory story)
            throws Exception {
        final List results = session.find("select p.name, p.id, i.name, i.id "
                + "from net.sf.xplanner.domain.Project as p, "
                + "net.sf.xplanner.domain.Iteration as i, "
                + "net.sf.xplanner.domain.UserStory as s  "
                + "where s.id = ? and  s.iteration = i and i.project = p",
                new Integer(story.getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            final Object[] result = (Object[]) results.iterator().next();
            this.setProjectName((String) result[0]);
            this.setProjectId(((Integer) result[1]).intValue());
            this.setIterationName((String) result[2]);
            this.setIterationId(((Integer) result[3]).intValue());
        }
        this.setStoryName(story.getName());
        this.setStoryId(story.getId());
    }

    /**
     * Populate.
     *
     * @param session
     *            the session
     * @param task
     *            the task
     * @throws Exception
     *             the exception
     */
    private void populate(final Session session, final Task task)
            throws Exception {
        final List results = session
                .find("select p.name, p.id, i.name, i.id, s.name, s.id "
                        + "from net.sf.xplanner.domain.Project as p, "
                        + "net.sf.xplanner.domain.Iteration as i, "
                        + "net.sf.xplanner.domain.UserStory as s, "
                        + "net.sf.xplanner.domain.Task as t "
                        + "where t.id = ? and t.userStory.id = s.id and s.iteration = i and i.project = p",
                        new Integer(task.getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            final Object[] result = (Object[]) results.iterator().next();
            this.setProjectName((String) result[0]);
            this.setProjectId(((Integer) result[1]).intValue());
            this.setIterationName((String) result[2]);
            this.setIterationId(((Integer) result[3]).intValue());
            this.setStoryName((String) result[4]);
            this.setStoryId(((Integer) result[5]).intValue());
        }
        this.setTaskName(task.getName());
        this.setTaskId(task.getId());
    }

    /**
     * Populate.
     *
     * @param session
     *            the session
     * @param timeEntry
     *            the time entry
     * @throws Exception
     *             the exception
     */
    private void populate(final Session session, final TimeEntry timeEntry)
            throws Exception {
        final List results = session
                .find("select p.name, p.id, i.name, i.id, s.name, s.id, t.name "
                        + "from net.sf.xplanner.domain.Project as p, "
                        + "net.sf.xplanner.domain.Iteration as i, "
                        + "net.sf.xplanner.domain.UserStory as s, "
                        + "net.sf.xplanner.domain.Task as t "
                        + "where t.id = ? and t.userStory.id = s.id and s.iteration = i and i.project = p",
                        new Integer(timeEntry.getTask().getId()),
                        Hibernate.INTEGER);
        if (results.size() > 0) {
            final Object[] result = (Object[]) results.iterator().next();
            this.setProjectName((String) result[0]);
            this.setProjectId(((Integer) result[1]).intValue());
            this.setIterationName((String) result[2]);
            this.setIterationId(((Integer) result[3]).intValue());
            this.setStoryName((String) result[4]);
            this.setStoryId(((Integer) result[5]).intValue());
            this.setTaskName((String) result[6]);
        }
        this.setTaskId(timeEntry.getId());
    }

    /**
     * Populate.
     *
     * @param session
     *            the session
     * @param feature
     *            the feature
     * @throws Exception
     *             the exception
     */
    private void populate(final Session session, final Feature feature)
            throws Exception {
        final List results = session
                .find("select p.name, p.id, i.name, i.id, s.name, s.id "
                        + "from net.sf.xplanner.domain.Project as p, "
                        + "net.sf.xplanner.domain.Iteration as i, "
                        + "net.sf.xplanner.domain.UserStory as s, "
                        + "net.sf.xplanner.domain.Feature as f "
                        + "where f.id = ? and f.userStory.id = s.id and s.iteration = i and i.project = p",
                        new Integer(feature.getId()), Hibernate.INTEGER);
        if (results.size() > 0) {
            final Object[] result = (Object[]) results.iterator().next();
            this.setProjectName((String) result[0]);
            this.setProjectId(((Integer) result[1]).intValue());
            this.setIterationName((String) result[2]);
            this.setIterationId(((Integer) result[3]).intValue());
            this.setStoryName((String) result[4]);
            this.setStoryId(((Integer) result[5]).intValue());
        }
        this.setFeatureName(feature.getName());
        this.setFeatureId(feature.getId());
    }

    /**
     * Populate.
     *
     * @param note
     *            the note
     * @throws Exception
     *             the exception
     */
    private void populate(final Note note) throws Exception {
        this.populate(DomainContext.getNoteTarget(note.getAttachedToId()));
    }

    /**
     * Gets the note target.
     *
     * @param attachedToId
     *            the attached to id
     * @return the note target
     */
    public static DomainObject getNoteTarget(final int attachedToId) {
        try {
            return DomainContext.idSearchHelper.search(attachedToId);
        } catch (final HibernateException e) {
            return null;
        }
    }

    /**
     * Save.
     *
     * @param request
     *            the request
     */
    public void save(final ServletRequest request) {
        request.setAttribute(DomainContext.REQUEST_KEY, this);
    }

    /**
     * Gets the.
     *
     * @param request
     *            the request
     * @return the domain context
     */
    public static DomainContext get(final ServletRequest request) {
        return (DomainContext) request.getAttribute(DomainContext.REQUEST_KEY);
    }
}
