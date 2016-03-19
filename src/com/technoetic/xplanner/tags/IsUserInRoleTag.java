package com.technoetic.xplanner.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class IsUserInRoleTag.
 */
public class IsUserInRoleTag extends TagSupport {
	
	/** The role. */
	private String role;
	
	/** The admin role. */
	private String adminRole;
	
	/** The negate. */
	private boolean negate;
	
	/** The userid. */
	private String userid;

	/**
     * Gets the role.
     *
     * @return the role
     */
	public String getRole() {
		return this.role;
	}

	/**
     * Sets the role.
     *
     * @param role
     *            the new role
     */
	public void setRole(final String role) {
		this.role = role;
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
     * Checks if is negate.
     *
     * @return true, if is negate
     */
	public boolean isNegate() {
		return this.negate;
	}

	/**
     * Sets the userid.
     *
     * @param userid
     *            the new userid
     */
	public void setUserid(final String userid) {
		this.userid = userid;
	}

	/**
     * Gets the userid.
     *
     * @return the userid
     */
	public String getUserid() {
		return this.userid;
	}

	/**
     * Gets the admin role.
     *
     * @return the admin role
     */
	public String getAdminRole() {
		return this.adminRole;
	}

	/**
     * Sets the admin role.
     *
     * @param adminRole
     *            the new admin role
     */
	public void setAdminRole(final String adminRole) {
		this.adminRole = adminRole;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
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