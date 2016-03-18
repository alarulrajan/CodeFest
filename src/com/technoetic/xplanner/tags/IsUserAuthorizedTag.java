package com.technoetic.xplanner.tags;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.xplanner.domain.Project;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.RequestUtils;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.AuthorizationHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

public class IsUserAuthorizedTag extends DatabaseTagSupport {
	private int projectId;
	private int principalId;
	private Object object;
	private String name;
	private String property;
	private String resourceType;
	private int resourceId;
	private String permission;
	private int allowedUser;
	private boolean negate;

	@Override
	protected int doStartTagInternal() throws Exception {
		boolean skipBody = true;
		if (this.allowedUser != 0
				&& this.allowedUser == SecurityHelper
						.getRemoteUserId((HttpServletRequest) this.pageContext
								.getRequest())) {
			skipBody = false;
		} else {
			Session session;
			final int projectId = this.getProjectId();

			session = this.getSession();
			skipBody = AuthorizationHelper.hasPermission(projectId,
					this.principalId, this.resourceId, this.resourceType,
					this.permission, this.getResource(),
					this.pageContext.getRequest());
			if (skipBody == true && projectId == 0) {
				// Has permission for any...
				final Collection projects = session.find("from project in "
						+ Project.class);
				skipBody = AuthorizationHelper.hasPermissionToAny(
						new String[] { this.permission }, projects,
						this.pageContext.getRequest());
			}
		}
		return (this.negate ? !skipBody : skipBody) ? Tag.SKIP_BODY
				: Tag.EVAL_BODY_INCLUDE;
	}

	private Object getResource() throws JspException {
		Object resource = this.object;
		if (this.object instanceof String) {
			resource = this.pageContext.findAttribute((String) this.object);
		}
		if (resource == null && this.name != null) {
			resource = RequestUtils.lookup(this.pageContext, this.name,
					this.property, null);
		}
		if (resource == null) {
			resource = this.pageContext.findAttribute("project");
		}
		if (resource == null && this.resourceType == null) {
			throw new JspException(
					"object or resource type/id must be specified");
		}
		return resource;
	}

	private int getProjectId() throws JspException {
		if (this.projectId != 0) {
			return this.projectId;
		}
		final DomainContext context = DomainContext.get(this.pageContext
				.getRequest());
		if (context != null && context.getProjectId() != 0) {
			return context.getProjectId();
		}
		if (this.object instanceof Project) {
			return ((Project) this.object).getId();
		}
		final String id = this.pageContext.getRequest().getParameter(
				"projectId");
		if (!StringUtils.isEmpty(id)) {
			return Integer.parseInt(id);
		}
		final Object resource = this.getResource();
		if (resource instanceof Project) {
			return ((Project) resource).getId();
		}
		return 0;
	}

	@Override
	public void release() {
		this.projectId = 0;
		this.principalId = 0;
		this.resourceType = null;
		this.resourceId = 0;
		this.permission = null;
		this.object = null;
		this.name = null;
		this.property = null;
		super.release();
	}

	public void setNegate(final boolean negate) {
		this.negate = negate;
	}

	public boolean isNegate() {
		return this.negate;
	}

	public void setPrincipalId(final int principalId) {
		this.principalId = principalId;
	}

	public void setResourceType(final String resourceType) {
		this.resourceType = resourceType;
	}

	public void setResourceId(final int resourceId) {
		this.resourceId = resourceId;
	}

	public void setPermission(final String permission) {
		this.permission = permission;
	}

	public void setObject(final Object object) {
		this.object = object;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setProperty(final String property) {
		this.property = property;
	}

	public void setAllowedUser(final int allowedUser) {
		this.allowedUser = allowedUser;
	}
}