/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: May 28, 2005
 * Time: 5:49:35 PM
 */
package com.technoetic.xplanner.domain;

import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class DomainObjectWikiLinkFormatter.
 */
public class DomainObjectWikiLinkFormatter {
	
	/** The from text. */
	String fromText;
	
	/** The to text. */
	String toText;

	/** The scheme by class. */
	Map schemeByClass = new HashMap();

	/**
     * Instantiates a new domain object wiki link formatter.
     */
	public DomainObjectWikiLinkFormatter() {
		this.initSchemeByClassMap();
	}

	/**
     * Inits the scheme by class map.
     */
	private void initSchemeByClassMap() {
		this.schemeByClass.put(Project.class, "project");
		this.schemeByClass.put(Iteration.class, "iteration");
		// schemeByClass.put(Feature.class, "feature");
		this.schemeByClass.put(UserStory.class, "story");
		this.schemeByClass.put(Task.class, "task");
	}

	/**
     * Format.
     *
     * @param object
     *            the object
     * @return the string
     */
	public String format(final net.sf.xplanner.domain.DomainObject object) {
		String link = "";
		if (object != null) {
			final String scheme = (String) this.schemeByClass.get(object
					.getClass());
			link = scheme + ":" + object.getId();
		}
		return link;
	}
}