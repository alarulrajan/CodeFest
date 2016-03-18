package com.technoetic.xplanner.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class IsUserInRoleTag extends TagSupport {
	private String role;
	private String adminRole;
	private boolean negate;
	private String userid;

	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public void setNegate(final boolean negate) {
		this.negate = negate;
	}

	public boolean isNegate() {
		return this.negate;
	}

	public void setUserid(final String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return this.userid;
	}

	public String getAdminRole() {
		return this.adminRole;
	}

	public void setAdminRole(final String adminRole) {
		this.adminRole = adminRole;
	}

	@Override
	public int doStartTag() throws JspException {
		boolean skipBody = true;
		final HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		if (this.adminRole != null
				&& SecurityHelper.isUserInRole(request, this.adminRole)) {
			skipBody = false;
		} else {
			final String[] roles = this.role.split(",");
			for (int i = 0; i < roles.length; i++) {
				final String role = roles[i];
				if (SecurityHelper.isUserInRole(request, role)) {
					skipBody = false;
					break;
				}
			}
			try {
				if (this.userid != null
						&& !SecurityHelper.getUserPrincipal(request).getName()
								.equals(this.userid)) {
					skipBody = true;
				}
			} catch (final AuthenticationException e) {
				throw new JspException(e);
			}
		}
		return (this.negate ? !skipBody : skipBody) ? Tag.SKIP_BODY
				: Tag.EVAL_BODY_INCLUDE;
	}
}