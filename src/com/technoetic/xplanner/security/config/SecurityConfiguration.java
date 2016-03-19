package com.technoetic.xplanner.security.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * The Class SecurityConfiguration.
 */
public class SecurityConfiguration {
	
	/** The security constraints. */
	private final ArrayList securityConstraints = new ArrayList();
	
	/** The security roles. */
	private final ArrayList securityRoles = new ArrayList();
	
	/** The security bypass. */
	private WebResourceCollection securityBypass;

	/**
     * Adds the security constraint.
     *
     * @param constraint
     *            the constraint
     */
	public void addSecurityConstraint(final SecurityConstraint constraint) {
		this.securityConstraints.add(constraint);
	}

	/**
     * Adds the security role.
     *
     * @param securityRole
     *            the security role
     */
	public void addSecurityRole(final SecurityRole securityRole) {
		this.securityRoles.add(securityRole);
	}

	/**
     * Gets the security constraints.
     *
     * @return the security constraints
     */
	public ArrayList getSecurityConstraints() {
		return this.securityConstraints;
	}

	/**
     * Gets the security bypass.
     *
     * @return the security bypass
     */
	public WebResourceCollection getSecurityBypass() {
		return this.securityBypass;
	}

	/**
     * Sets the security bypass.
     *
     * @param securityBypass
     *            the new security bypass
     */
	public void setSecurityBypass(final WebResourceCollection securityBypass) {
		this.securityBypass = securityBypass;
	}

	/**
     * Checks if is authorized.
     *
     * @param request
     *            the request
     * @return true, if is authorized
     */
	public boolean isAuthorized(final HttpServletRequest request) {
		if (!this.isSecureRequest(request)) {
			return true;
		} else {
			final Iterator securityConstraints = this.getSecurityConstraints()
					.iterator();
			while (securityConstraints.hasNext()) {
				final SecurityConstraint securityConstraint = (SecurityConstraint) securityConstraints
						.next();
				if (securityConstraint.isApplicable(request)) {
					return securityConstraint.isAuthorized(request);
				}
			}
			return true;
		}
	}

	/**
     * Checks if is secure request.
     *
     * @param request
     *            the request
     * @return true, if is secure request
     */
	public boolean isSecureRequest(final HttpServletRequest request) {
		return !(this.securityBypass != null && this.securityBypass
				.matches(request));
	}

	/**
     * Gets the security roles.
     *
     * @return the security roles
     */
	public ArrayList getSecurityRoles() {
		return this.securityRoles;
	}

	/**
     * Load.
     *
     * @param filename
     *            the filename
     * @return the security configuration
     * @throws SAXException
     *             the SAX exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public static SecurityConfiguration load(final String filename)
			throws SAXException, IOException {
		return SecurityConfiguration.load(new FileInputStream(filename));
	}

	/**
     * Load.
     *
     * @param in
     *            the in
     * @return the security configuration
     * @throws SAXException
     *             the SAX exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public static SecurityConfiguration load(final InputStream in)
			throws SAXException, IOException {
		final Digester digester = new Digester();
		digester.setValidating(false);
		digester.setClassLoader(SecurityConfiguration.class.getClassLoader());

		digester.addObjectCreate("security", SecurityConfiguration.class);

		digester.addObjectCreate("security/security-constraint",
				SecurityConstraint.class);
		digester.addBeanPropertySetter(
				"security/security-constraint/display-name", "displayName");
		digester.addSetNext("security/security-constraint",
				"addSecurityConstraint");

		digester.addObjectCreate("security/security-bypass",
				WebResourceCollection.class);
		digester.addCallMethod("security/security-bypass/url-pattern",
				"addUrlPattern", 0);
		digester.addSetNext("security/security-bypass", "setSecurityBypass");

		digester.addObjectCreate(
				"security/security-constraint/web-resource-collection",
				WebResourceCollection.class);
		digester.addBeanPropertySetter(
				"security/security-constraint/web-resource-collection/web-resource-name",
				"name");
		digester.addCallMethod(
				"security/security-constraint/web-resource-collection/url-pattern",
				"addUrlPattern", 0);
		digester.addSetNext(
				"security/security-constraint/web-resource-collection",
				"addWebResourceCollection");

		digester.addObjectCreate(
				"security/security-constraint/auth-constraint",
				AuthConstraint.class);
		digester.addCallMethod(
				"security/security-constraint/auth-constraint/role-name",
				"addRoleName", 0);
		digester.addSetNext("security/security-constraint/auth-constraint",
				"addAuthConstraint");

		digester.addObjectCreate("security/security-role", SecurityRole.class);
		digester.addBeanPropertySetter("security/security-role/role-name",
				"name");
		digester.addSetNext("security/security-role", "addSecurityRole");

		return (SecurityConfiguration) digester.parse(in);
	}
}
