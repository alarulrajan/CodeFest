package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.technoetic.xplanner.security.AuthenticationException;

public class PersonOptionsTag extends OptionsTag {
	private String filtered;
	private int projectId;
	public static final String ALL_ACTIVE_PEOPLE_QUERY = "from "
			+ Person.class.getName()
			+ " as p where p.hidden = false order by p.name";

	@Override
	public void release() {
		super.release();
		this.filtered = null;
		this.projectId = 0;
	}

	@Override
	protected List getOptions() throws HibernateException,
			AuthenticationException {
		final List<Person> allPeople = this.fetchAllPersons();
		List<Person> selectedPeople;
		final int projectId = this.findProjectId();
		if (projectId != 0 && this.isFilteringRequested()) {
			selectedPeople = new ArrayList<Person>();
			selectedPeople.addAll(this.getAuthorizer()
					.getPeopleWithPermissionOnProject(allPeople, projectId));
		} else {
			selectedPeople = allPeople;
		}
		return selectedPeople;
	}

	private List<Person> fetchAllPersons() throws HibernateException {
		final Query query = this.getSession().createQuery(
				PersonOptionsTag.ALL_ACTIVE_PEOPLE_QUERY);
		// query.setCacheable(true);
		return query.list();
	}

	private boolean isFilteringRequested() {
		return this.filtered == null
				|| Boolean.valueOf(this.filtered).booleanValue();
	}

	public String getFiltered() {
		return this.filtered;
	}

	public void setFiltered(final String filtered) {
		this.filtered = filtered;
	}

	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public int findProjectId() {
		int projectId = this.projectId;
		if (projectId == 0) {
			final DomainContext context = DomainContext.get(this.pageContext
					.getRequest());
			if (context != null) {
				projectId = context.getProjectId();
			}
		}
		final String id = this.pageContext.getRequest().getParameter(
				"projectId");
		if (projectId == 0 && !StringUtils.isEmpty(id)) {
			projectId = Integer.parseInt(id);
		}
		return projectId;
	}

}
