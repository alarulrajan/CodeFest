package com.technoetic.xplanner.wiki;

import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.filters.ThreadServletRequest;

public class DomainObjectSchemeHandler implements SchemeHandler {
	private final Logger log = Logger.getLogger(this.getClass());
	private final String action;
	private static HashMap schemeClasses = new HashMap();

	static {
		DomainObjectSchemeHandler.schemeClasses.put("project", Project.class);
		DomainObjectSchemeHandler.schemeClasses.put("iteration",
				Iteration.class);
		DomainObjectSchemeHandler.schemeClasses.put("story", UserStory.class);
		DomainObjectSchemeHandler.schemeClasses.put("task", Task.class);
		DomainObjectSchemeHandler.schemeClasses.put("person", Person.class);
	}

	public DomainObjectSchemeHandler(final String action) {
		this.action = action;
	}

	@Override
	public String translate(final Properties properties, final String scheme,
			String location, String linkText) {
		final Class domainClass = (Class) DomainObjectSchemeHandler.schemeClasses
				.get(scheme);
		final Pattern pattern = Pattern.compile("^(\\d+)(.*)$");
		final Matcher matcher = pattern.matcher(location);
		try {
			matcher.find();
			location = matcher.group(1);
			final Object object = ThreadSession.get().load(domainClass,
					new Integer(location));
			if (linkText == null) {
				try {
					linkText = scheme + ": "
							+ BeanUtils.getProperty(object, "name");
				} catch (final Exception e) {
					// ignored
					linkText = scheme + ":" + location;
				}
			}
			// FIXME: Why using a second group? What is it for?
			linkText += matcher.group(2);
			final HttpServletRequest request = ThreadServletRequest.get();
			return "<a href='" + request.getContextPath() + "/do/view/"
					+ (this.action != null ? this.action : scheme) + "?oid="
					+ location + "'>" + linkText + "</a>";
		} catch (final Exception e) {
			return "[" + scheme + ": " + e.getMessage() + "]";
		}
	}
}
