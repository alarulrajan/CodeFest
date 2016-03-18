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

public class DomainContext {
	public static final String REQUEST_KEY = "domainContext";

	private String projectName;
	private int projectId;
	private String iterationName;
	private int iterationId;
	private String storyName;
	private int storyId;
	private String taskName;
	private int taskId;
	private String featureName;
	private int featureId;
	private Object targetObject;
	private ActionMapping actionMapping;
	private static OLDIdSearchHelper idSearchHelper = new OLDIdSearchHelper();

	public ActionMapping getActionMapping() {
		return this.actionMapping;
	}

	public void setActionMapping(final ActionMapping actionMapping) {
		this.actionMapping = actionMapping;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public String getIterationName() {
		return this.iterationName;
	}

	public void setIterationName(final String iterationName) {
		this.iterationName = iterationName;
	}

	public int getIterationId() {
		return this.iterationId;
	}

	public void setIterationId(final int iterationId) {
		this.iterationId = iterationId;
	}

	public String getStoryName() {
		return this.storyName;
	}

	public void setStoryName(final String storyName) {
		this.storyName = storyName;
	}

	public int getStoryId() {
		return this.storyId;
	}

	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(final String taskName) {
		this.taskName = taskName;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public String getFeatureName() {
		return this.featureName;
	}

	public void setFeatureName(final String featureName) {
		this.featureName = featureName;
	}

	public int getFeatureId() {
		return this.featureId;
	}

	public void setFeatureId(final int featureId) {
		this.featureId = featureId;
	}

	public void setTaskId(final int taskId) {
		this.taskId = taskId;
	}

	public Object getTargetObject() {
		return this.targetObject;
	}

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

	private void populate(final Project project) throws Exception {
		this.setProjectName(project.getName());
		this.setProjectId(project.getId());
	}

	private void populate(final Session session, final Iteration iteration)
			throws Exception {
		final Project project = (Project) session.load(Project.class,
				new Integer(iteration.getProject().getId()));
		this.populate(project);
		this.setIterationName(iteration.getName());
		this.setIterationId(iteration.getId());
	}

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

	private void populate(final Note note) throws Exception {
		this.populate(DomainContext.getNoteTarget(note.getAttachedToId()));
	}

	public static DomainObject getNoteTarget(final int attachedToId) {
		try {
			return DomainContext.idSearchHelper.search(attachedToId);
		} catch (final HibernateException e) {
			return null;
		}
	}

	public void save(final ServletRequest request) {
		request.setAttribute(DomainContext.REQUEST_KEY, this);
	}

	public static DomainContext get(final ServletRequest request) {
		return (DomainContext) request.getAttribute(DomainContext.REQUEST_KEY);
	}
}
