package com.technoetic.xplanner.soap;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.context.ContextLoaderListener;
import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ByteArrayConverter;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.QueryException;
import com.technoetic.xplanner.db.TaskQueryHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.domain.repository.AttributeRepository;
import com.technoetic.xplanner.domain.repository.AttributeRepositoryImpl;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.soap.domain.DomainData;
import com.technoetic.xplanner.soap.domain.IterationData;
import com.technoetic.xplanner.soap.domain.NoteData;
import com.technoetic.xplanner.soap.domain.PersonData;
import com.technoetic.xplanner.soap.domain.ProjectData;
import com.technoetic.xplanner.soap.domain.TaskData;
import com.technoetic.xplanner.soap.domain.TimeEntryData;
import com.technoetic.xplanner.soap.domain.UserStoryData;
import com.technoetic.xplanner.tags.DomainContext;

// TODO: SOAP input validation.
// Ideally, extract the validation out of the struts forms into reusable
// validation to be used by soap

public class XPlanner {
	private final Logger log = Logger.getLogger(this.getClass());
	private final AttributeRepository attributes = new AttributeRepositoryImpl();

	public XPlanner() {
		// The SOAP interface is required to use Calendars for dates. This
		// converter is intended to be an adapter for the Date usage in the
		// XPlanner domain objects. However, I'm not comfortable with this since
		// the converters are global objects.
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(final Class type, final Object value) {
				if (value == null) {
					return null;
				}
				if (value instanceof Calendar) {
					return ((Calendar) value).getTime();
				}
				return value;
			}
		}, Date.class);
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(final Class type, final Object value) {
				if (value == null) {
					return null;
				}
				if (value instanceof Date) {
					final Calendar calendar = Calendar.getInstance();
					calendar.setTime((Date) value);
					return calendar;
				}
				return value;
			}
		}, Calendar.class);
		ConvertUtils.register(new ByteArrayConverter(null), byte[].class); // by
																			// default
																			// a
																			// null
																			// value
																			// is
																			// not
																			// converted
																			// in
																			// a
																			// null
																			// array.
	}

	//
	// Projects
	//

	public ProjectData[] getProjects() throws Exception {
		return (ProjectData[]) this.getObjects(ProjectData.class, null, null,
				null, null);
	}

	public ProjectData getProject(final int id) throws Exception {
		return (ProjectData) this.getObject(ProjectData.class, id);
	}

	public ProjectData addProject(final ProjectData project) throws Exception {
		return (ProjectData) this.addObject(0, project);
	}

	public void removeProject(final int id) throws Exception {
		this.removeObject(ProjectData.class, id);
	}

	public void update(final ProjectData object) throws Exception {
		this.updateObject(object);
	}

	public IterationData getCurrentIteration(final int projectId)
			throws Exception {
		final IterationData[] iterations = (IterationData[]) this
				.getObjects(
						IterationData.class,
						"object.startDate <= ? and object.endDate >= ? and object.projectId = ?",
						new Object[] { new Date(), new Date(),
								new Integer(projectId) }, new Type[] {
								Hibernate.DATE, Hibernate.DATE,
								Hibernate.INTEGER }, null);
		return iterations.length > 0 ? iterations[0] : null;
	}

	public IterationData[] getIterations(final int projectId) throws Exception {
		return (IterationData[]) this.getObjects(ProjectData.class, projectId,
				"iterations", IterationData.class);
	}

	//
	// Iterations
	//

	public IterationData getIteration(final int id) throws Exception {
		return (IterationData) this.getObject(IterationData.class, id);
	}

	public IterationData addIteration(final IterationData iteration)
			throws Exception {
		return (IterationData) this.addObject(
				this.getProjectId(Project.class, iteration.getProjectId()),
				iteration);
	}

	public void removeIteration(final int id) throws Exception {
		this.removeObject(IterationData.class, id);
	}

	public void update(final IterationData object) throws Exception {
		this.updateObject(object);
	}

	public UserStoryData[] getUserStories(final int containerId)
			throws Exception {
		return (UserStoryData[]) this.getObjects(IterationData.class,
				containerId, "userStories", UserStoryData.class);
	}

	//
	// User Stories
	//

	public UserStoryData getUserStory(final int id) throws Exception {
		return (UserStoryData) this.getObject(UserStoryData.class, id);
	}

	public UserStoryData addUserStory(final UserStoryData story)
			throws Exception {
		return (UserStoryData) this.addObject(
				this.getProjectId(Iteration.class, story.getIterationId()),
				story);
	}

	public void removeUserStory(final int id) throws Exception {
		this.removeObject(UserStoryData.class, id);
	}

	public void update(final UserStoryData object) throws Exception {
		this.updateObject(object);
	}

	public TaskData[] getTasks(final int containerId) throws Exception {
		return (TaskData[]) this.getObjects(UserStoryData.class, containerId,
				"tasks", TaskData.class);
	}

	// FEATURE:
	// public FeatureData[] getFeatures(int containerId) throws Exception {
	// return (FeatureData[])getObjects(UserStoryData.class, containerId,
	// "features", FeatureData.class);
	// }

	//
	// Features
	//

	// public FeatureData getFeature(int id) throws Exception {
	// return (FeatureData)getObject(FeatureData.class, id);
	// }
	//
	// public FeatureData addFeature(FeatureData feature) throws Exception {
	// return (FeatureData)addObject(getProjectId(UserStory.class,
	// feature.getStoryId()), feature);
	// }
	//
	// public void removeFeature(int id) throws Exception {
	// removeObject(FeatureData.class, id);
	// }
	//
	// public void update(FeatureData object) throws Exception {
	// updateObject(object);
	// }

	//
	// Tasks
	//

	public TaskData getTask(final int id) throws Exception {
		return (TaskData) this.getObject(TaskData.class, id);
	}

	public TaskData[] getCurrentTasksForPerson(final int personId)
			throws QueryException {
		final TaskQueryHelper taskQueryHelper = (TaskQueryHelper) this
				.getSpringBean("taskQueryHelper");
		taskQueryHelper.setPersonId(personId);
		return (TaskData[]) this.toArray(TaskData.class,
				taskQueryHelper.getCurrentActiveTasksForPerson());
	}

	public TaskData[] getPlannedTasksForPerson(final int personId)
			throws QueryException {
		final TaskQueryHelper taskQueryHelper = (TaskQueryHelper) this
				.getSpringBean("taskQueryHelper");
		taskQueryHelper.setPersonId(personId);
		taskQueryHelper.setPersonId(personId);
		return (TaskData[]) this.toArray(TaskData.class,
				taskQueryHelper.getCurrentPendingTasksForPerson());
	}

	private Object getSpringBean(final String string) {
		return ContextLoaderListener.getContext().getBean(string);
	}

	public TaskData addTask(final TaskData task) throws Exception {
		return (TaskData) this.addObject(
				this.getProjectId(UserStory.class, task.getStoryId()), task);
	}

	public void removeTask(final int id) throws Exception {
		this.removeObject(TaskData.class, id);
	}

	public void update(final TaskData object) throws Exception {
		this.updateObject(object);
	}

	//
	// Time Entries
	//

	public TimeEntryData[] getTimeEntries(final int containerId)
			throws Exception {
		return (TimeEntryData[]) this.getObjects(TaskData.class, containerId,
				"timeEntries", TimeEntryData.class);
	}

	public TimeEntryData getTimeEntry(final int id) throws Exception {
		return (TimeEntryData) this.getObject(TimeEntryData.class, id);
	}

	public TimeEntryData addTimeEntry(final TimeEntryData timeEntry)
			throws Exception {
		return (TimeEntryData) this
				.addObject(
						this.getProjectId(Task.class, timeEntry.getTaskId()),
						timeEntry);
	}

	public void removeTimeEntry(final int id) throws Exception {
		this.removeObject(TimeEntryData.class, id);
	}

	public void update(final TimeEntryData object) throws Exception {
		this.updateObject(object);
	}

	//
	// Notes
	//

	public NoteData getNote(final int id) throws Exception {
		return (NoteData) this.getObject(NoteData.class, id);
	}

	public NoteData addNote(final NoteData note) throws Exception {
		return (NoteData) this.addObject(this.getProjectId(DomainContext
				.getNoteTarget(note.getAttachedToId())), note);
	}

	public void removeNote(final int id) throws Exception {
		this.removeObject(NoteData.class, id);
	}

	public void update(final NoteData note) throws Exception {
		this.updateObject(note);
	}

	public NoteData[] getNotesForObject(final int attachedToId)
			throws Exception {
		return (NoteData[]) this.getObjects(NoteData.class, "attachedTo_Id = "
				+ attachedToId, null, null, null);
	}

	//
	// People
	//

	public PersonData getPerson(final int id) throws Exception {
		return (PersonData) this.getObject(PersonData.class, id);
	}

	public PersonData addPerson(final PersonData object) throws Exception {
		return (PersonData) this.addObject(0, object);
	}

	public void removePerson(final int id) throws Exception {
		this.removeObject(PersonData.class, id);
	}

	public void update(final PersonData object) throws Exception {
		this.updateObject(object);
	}

	public PersonData[] getPeople() throws Exception {
		return (PersonData[]) this.getObjects(PersonData.class, null, null,
				null, null);
	}

	//
	// Attributes
	//

	public void setAttribute(final int objectId, final String key,
			final String value) throws Exception {
		this.attributes.setAttribute(objectId, key, value);
		this.commit(ThreadSession.get());
	}

	public String getAttribute(final int objectId, final String key)
			throws Exception {
		return this.attributes.getAttribute(objectId, key);
	}

	public void deleteAttribute(final int objectId, final String key)
			throws Exception {
		this.attributes.delete(objectId, key);
		this.commit(ThreadSession.get());
	}

	public Map getAttributes(final int objectId) throws Exception {
		return this.attributes.getAttributes(objectId, null);
	}

	public Map getAttributesWithPrefix(final int objectId, final String prefix)
			throws Exception {
		return this.attributes.getAttributes(objectId, prefix);
	}

	//
	// Support Functions
	//

	private int getProjectId(final Class containerClass, final int containerId)
			throws Exception {
		final Object object = ThreadSession.get().load(containerClass,
				new Integer(containerId));
		final DomainContext context = new DomainContext();
		context.populate(object);
		return context.getProjectId();
	}

	private int getProjectId(final Object object) throws Exception {
		final DomainContext context = new DomainContext();
		context.populate(object);
		return context.getProjectId();
	}

	private Object[] getObjects(final Class dataClass, final String where,
			final Object[] values, final Type[] types, final String orderBy)
			throws Exception {
		try {
			final Session session = ThreadSession.get();
			final Class objectClass = this.getInternalClass(dataClass);
			String query = "from " + objectClass.getName();
			if (where != null) {
				query += " where " + where;
			}
			if (orderBy != null) {
				query += " order by " + orderBy;
			}
			final List objects = values != null ? session.find(query, values,
					types) : session.find(query);
			return this.toArray(dataClass, objects);
		} catch (final Exception ex) {
			this.log.error("error loading objects", ex);
			throw ex;
		}
	}

	private Object[] getObjects(final Class fromDataClass, final int id,
			final String propertyName, final Class toDataClass)
			throws Exception {
		try {
			final Session session = ThreadSession.get();
			final Class objectClass = this.getInternalClass(fromDataClass);
			this.log.debug("getting object: " + id);
			final Object object = session.load(objectClass, new Integer(id));
			this.log.debug("loaded object: " + object);
			final Collection objects = (Collection) PropertyUtils.getProperty(
					object, propertyName);
			final Object[] dataArray = this.toArray(toDataClass, objects);
			return dataArray;
		} catch (final Exception ex) {
			this.log.error("error loading objects", ex);
			throw ex;
		}
	}

	private Object getObject(final Class dataClass, final int id)
			throws Exception {
		final Session session = ThreadSession.get();
		try {
			final Class objectClass = this.getInternalClass(dataClass);
			this.log.debug("getting object: " + id);
			final DomainObject object = (DomainObject) session.load(
					objectClass, new Integer(id));
			this.log.debug("loaded object: " + object);
			final DomainData data = (DomainData) dataClass.newInstance();
			if (this.hasPermission(this.getProjectId(object), object, "read")) {
				this.populateDomainData(data, object);
				return data;
			} else {
				throw new AuthenticationException(
						"no permission to read object");
			}
		} catch (final ObjectNotFoundException ex) {
			return null;
		} catch (final Exception ex) {
			this.log.error("error loading objects", ex);
			throw ex;
		}
	}

	static Integer NULL = new Integer(-1);

	private void updateObject(final DomainData data) throws Exception {
		Session session = null;
		try {
			Integer id = XPlanner.NULL;
			session = ThreadSession.get();
			final Class objectClass = this.getInternalClass(data.getClass());
			id = this.getObjectId(data);
			final DomainObject object = (DomainObject) session.load(
					objectClass, id);
			if (this.hasPermission(this.getProjectId(object), object, "edit")) {
				// JM: no need to write lock the object
				// There is a lot more chance to get the client out-of-sync
				// during a get/update than just in that method
				// See to-do at the top of the file for better implementation
				if (object != null) {
					this.populateDomainObject(object, data);
				}
				this.saveHistory(session, object, History.UPDATED);
				this.commit(session);
			} else {
				throw new AuthenticationException(
						"no permission to update object");
			}
		} catch (final Exception ex) {
			this.rollback(session);
			throw ex;
		}
	}

	private void saveHistory(final Session session, final DomainObject object,
			final String eventType) throws AuthenticationException {
		String description = null;
		if (eventType.equals(History.DELETED)) {
			try {
				description = BeanUtils.getProperty(object, "name");
			} catch (final Exception e) {
				description = "unknown name";
			}
		}
		((HistorySupport) this.getSpringBean("historySupport")).saveEvent(
				object, eventType, description,
				SecurityHelper.getRemoteUserId(ThreadServletRequest.get()),
				new Date());
	}

	private Integer getObjectId(final Object data)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return (Integer) PropertyUtils.getProperty(data, "id");
	}

	protected void removeObject(final Class dataClass, final int id)
			throws Exception {
		// DEBT Should use the metarepository
		Session session = null;
		try {
			session = ThreadSession.get();
			this.log.debug("removing object: " + id);
			final Class objectClass = this.getInternalClass(dataClass);
			final DomainObject object = (DomainObject) session.load(
					objectClass, new Integer(id));
			if (this.hasPermission(this.getProjectId(object), object, "delete")) {
				session.delete(object);
				this.saveHistory(session, object, History.DELETED);
				this.commit(session);
			} else {
				throw new AuthenticationException(
						"no permission to delete object");
			}
		} catch (final ObjectNotFoundException ex) {
			throw ex;
		} catch (final Exception ex) {
			this.rollback(session);
			throw ex;
		}
	}

	protected Object addObject(final int projectId, final DomainData data)
			throws Exception {
		Session session = null;
		try {
			session = ThreadSession.get();
			final Class objectClass = this.getInternalClass(data.getClass());
			final DomainObject object = (DomainObject) objectClass
					.newInstance();
			if (this.hasPermission(projectId, object, "create")) {
				this.populateDomainObject(object, data);
				this.log.debug("adding object: " + object);
				session.save(object);
				this.saveHistory(session, object, History.CREATED);
				this.commit(session);
				// return getObject(data.getClass(), ((Integer) id).intValue());
				this.populateDomainData(data, object);
				return data;
			} else {
				throw new AuthenticationException(
						"no permission to create object");
			}
		} catch (final Exception ex) {
			this.rollback(session);
			throw ex;
		}
	}

	private void commit(final Session session) {
		if (session == null) {
			return;
		}
		try {
			session.flush();
			session.connection().commit();
		} catch (final Exception ex) {
			this.log.error("error", ex);
			throw new RuntimeException(ex);
		}
	}

	private void rollback(final Session session) {
		if (session == null) {
			return;
		}
		try {
			session.connection().rollback();
		} catch (final Exception e) {
			this.log.error("error", e);
		}
	}

	private Object[] toArray(final Class dataClass, final Collection objects) {
		try {
			final ArrayList accessibleObjects = this
					.selectAccessibleObjects(objects);
			final Object[] dataObjects = this.createArray(dataClass,
					accessibleObjects);
			final Iterator iter = accessibleObjects.iterator();
			for (int i = 0; iter.hasNext(); i++) {
				this.populateDomainData((DomainData) dataObjects[i],
						(DomainObject) iter.next());
			}
			return dataObjects;
		} catch (final Exception ex) {
			this.log.error("error in toArray", ex);
			return null;
		}
	}

	private ArrayList selectAccessibleObjects(final Collection objects)
			throws Exception {
		final ArrayList accessibleObjects = new ArrayList();
		for (final Iterator objectIterator = objects.iterator(); objectIterator
				.hasNext();) {
			final DomainObject object = (DomainObject) objectIterator.next();
			if (this.hasPermission(this.getProjectId(object), object, "read")) {
				accessibleObjects.add(object);
			}

		}
		return accessibleObjects;
	}

	private boolean hasPermission(final int projectId,
			final DomainObject sourceObject, final String permission)
			throws Exception {
		final int remoteUserId = SecurityHelper
				.getRemoteUserId(ThreadServletRequest.get());
		this.log.info("Checking permission for userid " + remoteUserId);
		return SystemAuthorizer.get().hasPermission(projectId, remoteUserId,
				sourceObject, permission);
	}

	private void populateDomainData(final DomainData data,
			final DomainObject sourceObject) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		BeanUtils.copyProperties(data, sourceObject);
		final Map description = PropertyUtils.describe(data);
		final Iterator keyItr = description.keySet().iterator();
		while (keyItr.hasNext()) {
			final String key = (String) keyItr.next();
			if ("class".equals(key) || "relationshipMapping".equals(key)) {
				continue;
			}
			if (this.isRelationship(sourceObject, key)) {
				RelationshipMappingRegistry.getInstance()
						.getRelationshipMapping(sourceObject, key)
						.populateAdapter(data, sourceObject);
			}
		}
	}

	private void populateDomainObject(final DomainObject targetObject,
			final DomainData data) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			HibernateException {
		BeanUtils.copyProperties(targetObject, data);
		final Map description = PropertyUtils.describe(data);
		final Iterator keyItr = description.keySet().iterator();
		while (keyItr.hasNext()) {
			final String key = (String) keyItr.next();
			if ("class".equals(key) || "relationshipMapping".equals(key)) {
				continue;
			}
			if (this.isRelationship(targetObject, key)) {
				// FIXME: soap service
				RelationshipMappingRegistry
						.getInstance()
						.getRelationshipMapping(targetObject, key)
						.populateDomainObject(targetObject, data,
								(CommonDao<?>) this.getSpringBean("commonDao"));
			}
		}
	}

	private boolean isRelationship(final DomainObject domainObject,
			final String key) {
		return RelationshipMappingRegistry.getInstance()
				.getRelationshipMapping(domainObject, key) != null;
	}

	private Object[] createArray(final Class dataClass, final Collection objects)
			throws InstantiationException, IllegalAccessException {
		final Object[] dataObjects = (Object[]) Array.newInstance(dataClass,
				objects.size());
		for (int i = 0; i < dataObjects.length; i++) {
			dataObjects[i] = dataClass.newInstance();
		}
		return dataObjects;
	}

	private Class getInternalClass(final Class dataClass) {
		try {
			final Method method = dataClass.getMethod("getInternalClass", null);
			return (Class) method.invoke(dataClass, null);
		} catch (final Exception e) {
			return this.getDomainClassForDataClass(dataClass);
		}
	}

	private Class getDomainClassForDataClass(final Class dataClass) {
		return (Class) XPlanner.dataToDomainClassMap.get(dataClass);
	}

	static Map dataToDomainClassMap = XPlanner.createDataToDomainClassMap();

	private static Map createDataToDomainClassMap() {
		final HashMap map = new HashMap();
		map.put(ProjectData.class, Project.class);
		map.put(IterationData.class, Iteration.class);
		map.put(UserStoryData.class, UserStory.class);
		map.put(TaskData.class, Task.class);
		map.put(TimeEntryData.class, TimeEntry.class);
		map.put(PersonData.class, Person.class);
		map.put(NoteData.class, Note.class);
		return map;
	}

}
