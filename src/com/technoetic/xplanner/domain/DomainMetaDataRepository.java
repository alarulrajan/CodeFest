package com.technoetic.xplanner.domain;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.views.IterationPage;
import com.technoetic.xplanner.views.IterationStoriesPage;

/**
 * The Class DomainMetaDataRepository.
 */
//DEBT Should merge with RelationshipMappingRegistry
public class DomainMetaDataRepository {
	
	/** The log. */
	private static Logger log = Logger
			.getLogger(DomainMetaDataRepository.class);

	/** The metadata by class. */
	private final HashMap metadataByClass = new HashMap();
	
	/** The metadata by type name. */
	private final HashMap metadataByTypeName = new HashMap();

	// DEBT Start using these constant everywhere especially in the web tier
	/** The Constant PROJECT_TYPE_NAME. */
	// (all links)
	public static final String PROJECT_TYPE_NAME = "project";
	
	/** The Constant ITERATION_TYPE_NAME. */
	public static final String ITERATION_TYPE_NAME = "iteration";
	
	/** The Constant STORY_TYPE_NAME. */
	public static final String STORY_TYPE_NAME = "userstory";
	
	/** The Constant TASK_TYPE_NAME. */
	public static final String TASK_TYPE_NAME = "task";
	
	/** The Constant FEATURE_TYPE_NAME. */
	public static final String FEATURE_TYPE_NAME = "feature";
	
	/** The Constant TIME_ENTRY_TYPE_NAME. */
	public static final String TIME_ENTRY_TYPE_NAME = "timeentry";
	
	/** The Constant INTEGRATION_TYPE_NAME. */
	public static final String INTEGRATION_TYPE_NAME = "integration";
	
	/** The Constant PERSON_TYPE_NAME. */
	public static final String PERSON_TYPE_NAME = "person";
	
	/** The Constant NOTE_TYPE_NAME. */
	public static final String NOTE_TYPE_NAME = "note";

	/** The instance. */
	private static DomainMetaDataRepository instance = DomainMetaDataRepository
			.createInstance();

	/**
     * Instantiates a new domain meta data repository.
     */
	public DomainMetaDataRepository() {
	}

	/**
     * Creates the instance.
     *
     * @return the domain meta data repository
     */
	private static DomainMetaDataRepository createInstance() {
		final DomainMetaDataRepository rep = new DomainMetaDataRepository();
		rep.init();
		return rep;
	}

	/**
     * Inits the.
     */
	public void init() {
		DomainClass domainClass;

		// DEBT(DATADRIVEN) Why do we need to specify the /do/ in all entities
		// except iteration?
		// DEBT(DATADRIVEN) Should have an acceptance tests to check all the
		// links values accross all entities
		// DEBT(DATADRIVEN) Remove duplication with edit/delete actions
		// DEBT(DATADRIVEN) Remove duplication within the same action mapping

		domainClass = new DomainClass(
				DomainMetaDataRepository.PROJECT_TYPE_NAME, Project.class);
		domainClass.addMapping(new ActionMapping("edit", "action.edit.project",
				"admin.edit", "/images/edit.png", "/do/edit/project",
				"project", false));
		domainClass.addMapping(new ActionMapping("delete",
				"action.delete.project", "sysadmin.delete",
				"/images/delete.png", "/do/delete/project", "project", true,
				true, "action.delete.confirmation"));
		this.add(domainClass);

		domainClass = new DomainClass(
				DomainMetaDataRepository.ITERATION_TYPE_NAME, Iteration.class,
				"project", Project.class, "iterations");
		domainClass.addMapping(new ActionMapping("edit",
				"action.edit.iteration", "edit", "/images/edit.png",
				"edit/iteration", "iteration", false));
		domainClass.addMapping(new ActionMapping("delete",
				"action.delete.iteration", "delete", "/images/delete.png",
				"delete/iteration", "iteration", true, true,
				"action.delete.confirmation"));
		domainClass.addMapping(new ActionMapping("createStory",
				"iteration.link.create_story", "edit", null, "edit/userstory",
				"iteration", false, false, null));
		domainClass.addMapping(new ActionMapping("start",
				IterationPage.START_ACTION, "edit", null, "start/iteration",
				"iteration", false) {
			@Override
			public boolean isVisible(final Nameable object) {
				return !((Iteration) object).isActive();
			}
		});
		domainClass.addMapping(new ActionMapping("close",
				IterationPage.CLOSE_ACTION, "edit", null, "close/iteration",
				"iteration", false) {
			@Override
			public boolean isVisible(final Nameable object) {
				return ((Iteration) object).isActive();
			}
		});
		domainClass.addMapping(new ActionMapping("importStories",
				IterationStoriesPage.IMPORT_STORIES_LINK, "edit", null,
				"import/stories", "iteration", false));

		this.add(domainClass);

		domainClass = new DomainClass(DomainMetaDataRepository.STORY_TYPE_NAME,
				UserStory.class, "iteration", Iteration.class, "userStories");
		domainClass.addMapping(new ActionMapping("edit", "action.edit.story",
				"edit", "/images/edit.png", "/do/edit/userstory", "story",
				false));
		domainClass.addMapping(new ActionMapping("delete",
				"action.delete.story", "delete", "/images/delete.png",
				"/do/delete/userstory", "story", true, true,
				"action.delete.confirmation"));
		domainClass.addMapping(new ActionMapping("movecontinue",
				"action.movecontinue.story", "edit",
				"/images/movecontinue.png", "/do/move/continue/userstory",
				"story", true));
		this.add(domainClass);

		domainClass = new DomainClass(DomainMetaDataRepository.TASK_TYPE_NAME,
				Task.class, "userStory", UserStory.class, "tasks");
		domainClass.addMapping(new ActionMapping("edit", "action.edit.task",
				"edit", "/images/edit.png", "/do/edit/task", "task", false));
		domainClass.addMapping(new ActionMapping("delete",
				"action.delete.task", "delete", "/images/delete.png",
				"/do/delete/task", "task", true, true,
				"action.delete.confirmation"));
		domainClass.addMapping(new ActionMapping("movecontinue",
				"action.movecontinue.task", "edit", "/images/movecontinue.png",
				"/do/move/continue/task", "task", true));
		domainClass.addMapping(new ActionMapping("edittime",
				"action.edittime.task", "edit", "/images/clock2.png",
				"/do/edit/time", "task", false));
		this.add(domainClass);

		this.add(new DomainClass(DomainMetaDataRepository.TIME_ENTRY_TYPE_NAME,
				TimeEntry.class, "task.id", Task.class, "timeEntries"));

		this.add(new DomainClass(
				DomainMetaDataRepository.INTEGRATION_TYPE_NAME,
				Integration.class));

		domainClass = new DomainClass(
				DomainMetaDataRepository.PERSON_TYPE_NAME, Person.class);
		domainClass
				.addMapping(new ActionMapping("edit", "action.edit.person",
						"edit", "/images/edit.png", "/do/edit/person",
						"person", false));
		this.add(domainClass);

		this.add(new DomainClass(DomainMetaDataRepository.NOTE_TYPE_NAME,
				Note.class, "attachedToId", DomainObject.class, null));
	}

	/**
     * Adds the.
     *
     * @param domainClass
     *            the domain class
     */
	public void add(final DomainClass domainClass) {
		this.metadataByClass.put(domainClass.getJavaClass(), domainClass);
		this.metadataByTypeName.put(domainClass.getTypeName(), domainClass);
	}

	/**
     * Gets the single instance of DomainMetaDataRepository.
     *
     * @return single instance of DomainMetaDataRepository
     */
	// DEBT(SPRING) Finish injecting fully this singleton
	public static DomainMetaDataRepository getInstance() {
		return DomainMetaDataRepository.instance;
	}

	/**
     * Class to type name.
     *
     * @param objectClass
     *            the object class
     * @return the string
     */
	public String classToTypeName(final Class objectClass) {
		return this.getMetaData(objectClass).getTypeName();
	}

	/**
     * Gets the parent.
     *
     * @param child
     *            the child
     * @return the parent
     * @throws HibernateException
     *             the hibernate exception
     */
	public DomainObject getParent(final DomainObject child)
			throws HibernateException {
		final DomainClass metaData = this.getMetaData(child.getClass());
		final int parentId = DomainMetaDataRepository.getId(child,
				metaData.getParentProperty());
		return (DomainObject) ThreadSession.get().get(
				metaData.getParentClass(), new Integer(parentId));
	}

	/**
     * Sets the parent.
     *
     * @param child
     *            the child
     * @param parent
     *            the parent
     */
	public void setParent(final DomainObject child, final DomainObject parent) {
		final DomainClass metaData = this.getMetaData(child.getClass());
		DomainMetaDataRepository.setObjectOrId(child,
				metaData.getParentProperty(), parent);
		final Collection children = (Collection) DomainMetaDataRepository
				.getProperty(parent, metaData.getChildrenProperty());
		children.add(child);
	}

	/**
     * Gets the object.
     *
     * @param type
     *            the type
     * @param id
     *            the id
     * @return the object
     * @throws HibernateException
     *             the hibernate exception
     */
	public DomainObject getObject(final String type, final int id)
			throws HibernateException {
		final Class targetClass = this.getMetaData(type).getJavaClass();
		return (DomainObject) ThreadSession.get().get(targetClass,
				new Integer(id));
	}

	/**
     * Gets the parent id.
     *
     * @param object
     *            the object
     * @return the parent id
     */
	public int getParentId(final Identifiable object) {
		return DomainMetaDataRepository.getId(object,
				this.getMetaData(object.getClass()).getParentProperty());
	}

	/**
     * Gets the metadata by type name.
     *
     * @return the metadata by type name
     */
	public HashMap getMetadataByTypeName() {
		return this.metadataByTypeName;
	}

	/**
     * Gets the meta data.
     *
     * @param objectClass
     *            the object class
     * @return the meta data
     */
	public DomainClass getMetaData(final Class objectClass) {
		final DomainClass dom = (DomainClass) this.metadataByClass
				.get(objectClass);
		if (dom == null) {
			final String className = objectClass.getName();
			for (final Object clazz : this.metadataByClass.keySet()) {
				if (className.startsWith(((Class) clazz).getName())) {
					return (DomainClass) this.metadataByClass.get(clazz);
				}
			}
		}
		return dom;
	}

	/**
     * Gets the meta data.
     *
     * @param type
     *            the type
     * @return the meta data
     */
	public DomainClass getMetaData(final String type) {
		return (DomainClass) this.metadataByTypeName.get(type);
	}

	/**
     * Gets the id.
     *
     * @param object
     *            the object
     * @param property
     *            the property
     * @return the id
     */
	private static int getId(final Identifiable object, final String property) {
		if (property == null) {
			return 0;
		}
		final Object value = DomainMetaDataRepository.getProperty(object,
				property);
		if (value == null) {
			return 0;
		}
		if (value instanceof Identifiable) {
			return ((Identifiable) value).getId();
		} else {
			return ((Integer) value).intValue();
		}
	}

	/**
     * Gets the property.
     *
     * @param object
     *            the object
     * @param property
     *            the property
     * @return the property
     */
	private static Object getProperty(final Identifiable object,
			final String property) {
		Object prop = null;
		try {
			prop = PropertyUtils.getProperty(object, property);
		} catch (final Exception e) {
			DomainMetaDataRepository.log.error("configuration error with "
					+ (object != null ? object.getClass().toString() : "null")
					+ "." + property, e);
		}
		return prop;
	}

	/**
     * Sets the object or id.
     *
     * @param object
     *            the object
     * @param property
     *            the property
     * @param value
     *            the value
     */
	private static void setObjectOrId(final DomainObject object,
			final String property, final DomainObject value) {
		if (property != null) {
			try {
				final PropertyDescriptor descriptor = PropertyUtils
						.getPropertyDescriptor(object, property);
				final Class type = descriptor.getPropertyType();
				Object target;
				if (DomainObject.class.isAssignableFrom(type)) {
					target = value;
				} else {
					target = new Integer(value.getId());
				}
				PropertyUtils.setProperty(object, property, target);
			} catch (final Exception e) {
				DomainMetaDataRepository.log.error("configuration error with "
						+ (object != null ? object.getClass().toString()
								: "null") + "." + property, e);
			}
		}
	}
}
