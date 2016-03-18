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
 * @jira XPR-15 Cannot deploy xplanner.war to Weblogic 8.1
 */
public class IsUserAuthorizedForAnyTag extends DatabaseTagSupport {
	private Collection collection;
	private String name;
	private String property;
	private String permissions;
	private int projectId;
	private boolean negate;

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

	public void setCollection(final Collection collection) {
		this.collection = collection;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setProperty(final String property) {
		this.property = property;
	}

	public void setPermissions(final String permissions) {
		this.permissions = permissions;
	}

	public void setNegate(final boolean negate) {
		this.negate = negate;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}
}
