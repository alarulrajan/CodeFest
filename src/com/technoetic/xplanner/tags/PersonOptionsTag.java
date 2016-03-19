package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;

import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class PersonOptionsTag.
 */
public class PersonOptionsTag extends OptionsTag {
	
	/** The filtered. */
	private String filtered;
	
	/** The project id. */
	private int projectId;
	
	/** The Constant ALL_ACTIVE_PEOPLE_QUERY. */
	public static final String ALL_ACTIVE_PEOPLE_QUERY = "from "
			+ Person.class.getName()
			+ " as p where p.hidden = false order by p.name";

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.OptionsTag#release()
	 */
	@Override
	public void release() {
		super.release();
		this.filtered = null;
		this.projectId = 0;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.OptionsTag#getOptions()
	 */
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

	/**
     * Fetch all persons.
     *
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     */
	private List<Person> fetchAllPersons() throws HibernateException {
		final Query query = this.getSession().createQuery(
				PersonOptionsTag.ALL_ACTIVE_PEOPLE_QUERY);
		// query.setCacheable(true);
		return query.list();
	}

	/**
     * Checks if is filtering requested.
     *
     * @return true, if is filtering requested
     */
	private boolean isFilteringRequested() {
		return this.filtered == null
				|| Boolean.valueOf(this.filtered).booleanValue();
	}

	/**
     * Gets the filtered.
     *
     * @return the filtered
     */
	public String getFiltered() {
		return this.filtered;
	}

	/**
     * Sets the filtered.
     *
     * @param filtered
     *            the new filtered
     */
	public void setFiltered(final String filtered) {
		this.filtered = filtered;
	}

	/**
     * Sets the project id.
     *
     * @param projectId
     *            the new project id
     */
	public void setProjectId(final int projectId) {
		this.projectId = projectId;
	}

	/**
     * Gets the project id.
     *
     * @return the project id
     */
	public int getProjectId() {
		return this.projectId;
	}

	/**
     * Find project id.
     *
     * @return the int
     */
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
