package com.technoetic.xplanner.tags;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.xplanner.domain.Project;

import org.apache.struts.util.RequestUtils;
import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.auth.AuthorizationHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

/**
 * The Class IsUserAuthorizedForAnyTag.
 *
 * @jira XPR-15 Cannot deploy xplanner.war to Weblogic 8.1
 */
public class IsUserAuthorizedForAnyTag extends DatabaseTagSupport {
	
	/** The collection. */
	private Collection collection;
	
	/** The name. */
	private String name;
	
	/** The property. */
	private String property;
	
	/** The permissions. */
	private String permissions;
	
	/** The project id. */
	private int projectId;
	
	/** The negate. */
	private boolean negate;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
	 */
	@Override
	protected int doStartTagInternal() throws Exception {
		final String[] permissionArray = this.permissions.split(",");
		final Collection objects = this.getCollection();
		boolean isAuthorized = false;
		isAuthorized = AuthorizationHelper.hasPermissionToAny(permissionArray,
				objects, this.pageContext.getRequest(), this.projectId);

		return (this.negate ? !isAuthorized : isAuthorized) ? Tag.EVAL_BODY_INCLUDE
				: Tag.SKIP_BODY;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	@Override
	public void release() {
		this.collection = null;
		this.name = null;
		this.property = null;
		this.permissions = null;
		this.projectId = 0;
		this.negate = false;
		super.release();
	}

	/**
     * Gets the collection.
     *
     * @return the collection
     * @throws JspException
     *             the jsp exception
     */
	private Collection getCollection() throws JspException {
		if (this.collection != null) {
			return this.collection;
		}
		if (this.name != null) {
			return (Collection) RequestUtils.lookup(this.pageContext,
					this.name, this.property, null);
		}
		try {
			return ThreadSession.get().find("from project in " + Project.class);
		} catch (final HibernateException e) {
			throw new JspException(e);
		}
	}

	/**
     * Sets the collection.
     *
     * @param collection
     *            the new collection
     */
	public void setCollection(final Collection collection) {
		this.collection = collection;
	}

	/**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Sets the property.
     *
     * @param property
     *            the new property
     */
	public void setProperty(final String property) {
		this.property = property;
	}

	/**
     * Sets the permissions.
     *
     * @param permissions
     *            the new permissions
     */
	public void setPermissions(final String permissions) {
		this.permissions = permissions;
	}

	/**
     * Sets the negate.
     *
     * @param negate
     *            the new negate
     */
	public void setNegate(final boolean negate) {
		this.negate = negate;
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
}
