package com.technoetic.xplanner.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;

//ChangeSoon Should be turn into a Query object
/**
 * The Class IterationRepository.
 */
//ChangeSoon Create a spring context including IterationRepository and others for every web session (logged in user)
public class IterationRepository {
	
	/** The log. */
	private static Logger log = Logger.getLogger(IterationRepository.class);
	
	/** The Constant EDITABLE_ITERATIONS_QUERY_STRING. */
	public static final String EDITABLE_ITERATIONS_QUERY_STRING = "select i from "
			+ Iteration.class.getName()
			+ " i, "
			+ Project.class.getName()
			+ " p "
			+ "where p.hidden = false and i.projectId = p.id order by p.name, i.startDate";
	
	/** The session. */
	private final Session session;
	
	/** The logged in user. */
	private final int loggedInUser;
	
	/** The authorizer. */
	private final Authorizer authorizer;

	/**
     * Instantiates a new iteration repository.
     *
     * @param session
     *            the session
     * @param authorizer
     *            the authorizer
     * @param loggedInUser
     *            the logged in user
     */
	public IterationRepository(final Session session,
			final Authorizer authorizer, final int loggedInUser) {
		this.session = session;
		this.loggedInUser = loggedInUser;
		this.authorizer = authorizer;
	}

	/**
     * Can user edit iteration.
     *
     * @param iteration
     *            the iteration
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
	private boolean canUserEditIteration(final Iteration iteration)
			throws AuthenticationException {
		return this.authorizer.hasPermission(iteration.getProject().getId(),
				this.loggedInUser, iteration, "edit");
	}

	/**
     * Fetch editable iterations.
     *
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     */
	public List fetchEditableIterations() throws HibernateException,
			AuthenticationException {
		final List allIterations = this
				.getSession()
				.getNamedQuery(
						"com.technoetic.xplanner.domain.GetEditableIterationQuery")
				.list();
		final List acceptedIterations = new ArrayList(allIterations.size());
		for (int i = 0; i < allIterations.size(); i++) {
			final Iteration it = (Iteration) allIterations.get(i);
			if (this.canUserEditIteration(it)) {
				acceptedIterations.add(it);
			}
		}
		return acceptedIterations;
	}

	/**
     * Fetch editable iterations.
     *
     * @param projectId
     *            the project id
     * @param startingAfter
     *            the starting after
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     */
	public List fetchEditableIterations(final int projectId,
			final Date startingAfter) throws HibernateException,
			AuthenticationException {
		final List allEditableIterations = this.fetchEditableIterations();
		final List acceptedIterations = new ArrayList(
				allEditableIterations.size());
		for (int i = 0; i < allEditableIterations.size(); i++) {
			final Iteration it = (Iteration) allEditableIterations.get(i);
			if (it.getProject().getId() == projectId
					&& it.getStartDate().after(startingAfter)) {
				acceptedIterations.add(it);
			}
		}
		return acceptedIterations;
	}

	/**
     * Gets the session.
     *
     * @return the session
     */
	protected Session getSession() {
		return this.session;
	}

	/**
     * Gets the iteration for story.
     *
     * @param story
     *            the story
     * @return the iteration for story
     * @throws HibernateException
     *             the hibernate exception
     */
	public Iteration getIterationForStory(final UserStory story)
			throws HibernateException {
		return story.getIteration();
	}

	/**
     * Gets the iteration.
     *
     * @param iterationId
     *            the iteration id
     * @return the iteration
     * @throws HibernateException
     *             the hibernate exception
     */
	public Iteration getIteration(final int iterationId)
			throws HibernateException {
		return (Iteration) this.session.load(Iteration.class, new Integer(
				iterationId));
	}

	/**
     * Gets the current iteration.
     *
     * @param projectId
     *            the project id
     * @return the current iteration
     */
	public static Iteration getCurrentIteration(final int projectId) {
		final java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
		try {
			final Query getCurrentIterationQuery = ThreadSession
					.get()
					.getNamedQuery(
							"com.technoetic.xplanner.domain.GetCurrentIterationQuery");
			getCurrentIterationQuery.setParameter(0, new Integer(projectId),
					Hibernate.INTEGER);
			getCurrentIterationQuery.setParameter(1, now, Hibernate.DATE);
			final List results = getCurrentIterationQuery.list();
			return results.size() > 0 ? (Iteration) results.get(0) : null;
		} catch (final Exception e) {
			IterationRepository.log.error("error", e);
		}
		return null;
	}
}