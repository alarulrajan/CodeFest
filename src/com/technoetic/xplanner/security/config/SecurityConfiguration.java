package com.technoetic.xplanner.security.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class SecurityConfiguration {
	private final ArrayList securityConstraints = new ArrayList();
	private final ArrayList securityRoles = new ArrayList();
	private WebResourceCollection securityBypass;

	public void addSecurityConstraint(final SecurityConstraint constraint) {
		this.securityConstraints.add(constraint);
	}

	public void addSecurityRole(final SecurityRole securityRole) {
		this.securityRoles.add(securityRole);
	}

	public ArrayList getSecurityConstraints() {
		return this.securityConstraints;
	}

	public WebResourceCollection getSecurityBypass() {
		return this.securityBypass;
	}

	public void setSecurityBypass(final WebResourceCollection securityBypass) {
		this.securityBypass = securityBypass;
	}

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

	public boolean isSecureRequest(final HttpServletRequest request) {
		return !(this.securityBypass != null && this.securityBypass
				.matches(request));
	}

	public ArrayList getSecurityRoles() {
		return this.securityRoles;
	}

	public static SecurityConfiguration load(final String filename)
			throws SAXException, IOException {
		return SecurityConfiguration.load(new FileInputStream(filename));
	}

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
