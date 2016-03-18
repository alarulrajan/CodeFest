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

public class DomainObjectWikiLinkFormatter {
	String fromText;
	String toText;

	Map schemeByClass = new HashMap();

	public DomainObjectWikiLinkFormatter() {
		this.initSchemeByClassMap();
	}

	private void initSchemeByClassMap() {
		this.schemeByClass.put(Project.class, "project");
		this.schemeByClass.put(Iteration.class, "iteration");
		// schemeByClass.put(Feature.class, "feature");
		this.schemeByClass.put(UserStory.class, "story");
		this.schemeByClass.put(Task.class, "task");
	}

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