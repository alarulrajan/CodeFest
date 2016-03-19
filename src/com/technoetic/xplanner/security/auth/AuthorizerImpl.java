package com.technoetic.xplanner.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Setting;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.DataAccessException;

import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.forms.FeatureEditorForm;
import com.technoetic.xplanner.forms.IterationEditorForm;
import com.technoetic.xplanner.forms.PersonEditorForm;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import com.technoetic.xplanner.forms.TaskEditorForm;
import com.technoetic.xplanner.forms.UserStoryEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class AuthorizerImpl.
 */
public class AuthorizerImpl implements Authorizer {
	
	/** The resource types. */
	private final Map<Class, String> resourceTypes = new HashMap<Class, String>();
	
	/** The Constant ANY_PROJECT. */
	public static final Integer ANY_PROJECT = new Integer(0);
	
	/** The authorizer query helper. */
	private AuthorizerQueryHelper authorizerQueryHelper;
	
	/** The principal specific permission helper. */
	private PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;

	/**
     * Instantiates a new authorizer impl.
     */
	public AuthorizerImpl() {
		// ChangeSoon: Extract these constants
		// DEBT(METADATA) Move these to the DomainMetaDataRepository
		this.resourceTypes.put(Project.class, "system.project");
		this.resourceTypes.put(Iteration.class, "system.project.iteration");
		this.resourceTypes.put(UserStory.class,
				"system.project.iteration.story");
		this.resourceTypes.put(Task.class,
				"system.project.iteration.story.task");
		this.resourceTypes.put(Feature.class,
				"system.project.iteration.story.feature");
		this.resourceTypes.put(TimeEntry.class,
				"system.project.iteration.story.task.time_entry");
		this.resourceTypes.put(Integration.class, "system.project.integration");
		this.resourceTypes.put(Person.class, "system.person");
		this.resourceTypes.put(Note.class, "system.note");
		this.resourceTypes.put(ProjectEditorForm.class, "system.project");
		this.resourceTypes.put(IterationEditorForm.class,
				"system.project.iteration");
		this.resourceTypes.put(UserStoryEditorForm.class,
				"system.project.iteration.story");
		this.resourceTypes.put(TaskEditorForm.class,
				"system.project.iteration.story.task");
		this.resourceTypes.put(FeatureEditorForm.class,
				"system.project.iteration.story.feature");
		this.resourceTypes.put(PersonEditorForm.class, "system.person");
		this.resourceTypes.put(Setting.class, "system.setting");
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermission(int, int, java.lang.Object, java.lang.String)
	 */
	// ChangeSoon resource should be a DomainObject
	@Override
	public boolean hasPermission(final int projectId, final int personId,
			final Object resource, final String permission)
			throws AuthenticationException {
		int id;
		try {
			id = ((Integer) PropertyUtils.getProperty(resource, "id"))
					.intValue();
		} catch (final Exception e) {
			throw new AuthenticationException(e);
		}
		return this.hasPermission(projectId, personId,
				this.getTypeOfResource(resource), id, permission);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermission(int, int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean hasPermission(final int projectId, final int personId,
			final String resourceType, final int resourceId,
			final String permissionName) throws AuthenticationException {
		try {
			final Map permissionsByProjectMap = this.principalSpecificPermissionHelper
					.getPermissionsForPrincipal(personId);
			return this.permissionMatches(permissionName, resourceType,
					resourceId,
					(List) permissionsByProjectMap.get(new Integer(projectId)))
					||
					// For 0.7 only sysadmins have any project permissions
					this.permissionMatches(permissionName, resourceType,
							resourceId, (List) permissionsByProjectMap
									.get(AuthorizerImpl.ANY_PROJECT));
		} catch (final Exception e) {
			throw new AuthenticationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.auth.Authorizer#getPeopleWithPermissionOnProject(java.util.List, int)
	 */
	@Override
	public Collection getPeopleWithPermissionOnProject(final List allPeople,
			final int projectId) throws AuthenticationException {
		final Collection people = new ArrayList();
		for (int i = 0; i < allPeople.size(); i++) {
			final Person person = (Person) allPeople.get(i);
			if (this.hasPermission(projectId, person.getId(), "system.project",
					projectId, "edit")) {
				people.add(person);
			}
		}
		return people;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.auth.Authorizer#getRolesForPrincipalOnProject(int, int, boolean)
	 */
	@Override
	public Collection getRolesForPrincipalOnProject(final int principalId,
			final int projectId, final boolean includeWildcardProject)
			throws AuthenticationException {
		try {
			return this.authorizerQueryHelper.getRolesForPrincipalOnProject(
					principalId, projectId, includeWildcardProject);
		} catch (final DataAccessException e) {
			throw new AuthenticationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermissionForSomeProject(int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean hasPermissionForSomeProject(final int personId,
			final String resourceType, final int resourceId,
			final String permission) throws AuthenticationException {
		try {
			final List projects = this.authorizerQueryHelper
					.getAllUnhidenProjects();
			return this.hasPermissionForSomeProject(projects, personId,
					resourceType, resourceId, permission);
		} catch (final AuthenticationException e) {
			throw e;
		} catch (final Exception e) {
			throw new AuthenticationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermissionForSomeProject(java.util.Collection, int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean hasPermissionForSomeProject(final Collection projects,
			final int personId, final String resourceType,
			final int resourceId, final String permission)
			throws AuthenticationException {
		for (final Iterator iterator = projects.iterator(); iterator.hasNext();) {
			final Project project = (Project) iterator.next();
			if (this.hasPermission(project.getId(), personId, resourceType,
					resourceId, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
     * Gets the type of resource.
     *
     * @param resource
     *            the resource
     * @return the type of resource
     */
	public String getTypeOfResource(final Object resource) {
		final String keyClass = resource.getClass().getName();
		for (final Object clazz : this.resourceTypes.keySet()) {
			if (keyClass.startsWith(((Class) clazz).getName())) {
				return this.resourceTypes.get(clazz);
			}
		}
		return null;
	}

	/**
     * Sets the principal specific permission helper.
     *
     * @param principalSpecificPermissionHelper
     *            the new principal specific permission helper
     */
	public void setPrincipalSpecificPermissionHelper(
			final PrincipalSpecificPermissionHelper principalSpecificPermissionHelper) {
		this.principalSpecificPermissionHelper = principalSpecificPermissionHelper;
	}

	/**
     * Sets the authorizer query helper.
     *
     * @param authorizerQueryHelper
     *            the new authorizer query helper
     */
	public void setAuthorizerQueryHelper(
			final AuthorizerQueryHelper authorizerQueryHelper) {
		this.authorizerQueryHelper = authorizerQueryHelper;
	}

	/**
     * Checks if is matching.
     *
     * @param pattern
     *            the pattern
     * @param string
     *            the string
     * @return true, if is matching
     */
	private boolean isMatching(final String pattern, final String string) {
		return pattern.endsWith("%") ? string.startsWith(pattern.substring(0,
				pattern.length() - 1)) : string.equals(pattern);
	}

	/**
     * Permission matches.
     *
     * @param permissionName
     *            the permission name
     * @param resourceType
     *            the resource type
     * @param resourceId
     *            the resource id
     * @param permissionsForProject
     *            the permissions for project
     * @return true, if successful
     */
	private boolean permissionMatches(final String permissionName,
			final String resourceType, final int resourceId,
			final List permissionsForProject) {
		boolean hasNegativePermission = false;
		boolean hasPositivePermission = false;
		if (permissionsForProject != null) {
			for (int i = 0; i < permissionsForProject.size(); i++) {
				final Permission permission = (Permission) permissionsForProject
						.get(i);
				if (this.isMatching(permission.getResourceType(), resourceType)
						&& (permission.getResourceId() == 0 || permission
								.getResourceId() == resourceId)) {
					if (this.isMatching(permission.getName(), permissionName)) {
						if (permission.isPositive()) {
							hasPositivePermission = true;
						} else {
							hasNegativePermission = true;
						}
					}
				}
			}
		}
		return hasPositivePermission && !hasNegativePermission;
	}
}
