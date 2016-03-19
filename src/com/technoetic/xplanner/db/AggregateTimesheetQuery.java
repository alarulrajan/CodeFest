package com.technoetic.xplanner.db;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.domain.virtual.Timesheet;
import com.technoetic.xplanner.domain.virtual.TimesheetEntry;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class AggregateTimesheetQuery.
 */
public class AggregateTimesheetQuery {
	
	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());
	
	/** The query. */
	private static String query;
	
	/** The person ids. */
	private String[] personIds;
	
	/** The end date. */
	private java.util.Date endDate = new Date();
	
	/** The start date. */
	private java.util.Date startDate = new Date();
	
	/** The Constant IN_CLAUSE. */
	private static final String IN_CLAUSE = "AND person.id IN (";
	
	/** The Constant IN_CLAUSE_REPLACEMENT. */
	private static final String IN_CLAUSE_REPLACEMENT = "AND 1=1";
	
	/** The session. */
	private final Session session;

	/**
     * Instantiates a new aggregate timesheet query.
     *
     * @param session
     *            the session
     */
	public AggregateTimesheetQuery(final Session session) {
		this.session = session;
	}

	// todo - review why this is not using the hibernate query language
	/**
     * Gets the timesheet.
     *
     * @return the timesheet
     */
	// The current implementation will break if the Hibernate mappings change
	public Timesheet getTimesheet() {
		final Timesheet timesheet = new Timesheet(this.startDate, this.endDate);
		try {
			try {
				AggregateTimesheetQuery.query = "SELECT project.id, project.name, iteration.id, iteration.name,  story.id, story.name, "
						+ "Sum(time_entry.duration) "
						+ "FROM Person as person, Project as project inner join project.iterations as iteration "
						+ "inner join iteration.userStories as story inner join story.tasks as task inner join task.timeEntries as time_entry "
						+ "WHERE (person.id = time_entry.person1Id OR person.id = time_entry.person2Id) "
						+ "AND time_entry.reportDate >= ?  "
						+ "AND time_entry.reportDate <= ? "
						+ AggregateTimesheetQuery.IN_CLAUSE_REPLACEMENT
						+ " "
						+ "GROUP BY project.id, project.name, iteration.id,  "
						+ "iteration.name, story.id, story.name "
						+ "ORDER BY project.name, iteration.name, story.name ";

				if (this.personIds != null && this.personIds.length > 0) {
					// Set the in clause using String Manipulation
					final StringBuffer inClause = new StringBuffer(
							AggregateTimesheetQuery.IN_CLAUSE);
					for (int i = 0; i < this.personIds.length; i++) {
						if (i > 0) {
							inClause.append(",");
						}
						inClause.append(this.personIds[i]);
					}
					inClause.append(")");
					AggregateTimesheetQuery.query = AggregateTimesheetQuery.query
							.replaceAll(
									AggregateTimesheetQuery.IN_CLAUSE_REPLACEMENT,
									inClause.toString());
				}
				final Iterator iterator = this.session.iterate(
						AggregateTimesheetQuery.query, new Object[] {
								this.startDate, this.endDate }, new Type[] {
								Hibernate.DATE, Hibernate.DATE });
				while (iterator.hasNext()) {
					final int remoteUserId = SecurityHelper
							.getRemoteUserId(ThreadServletRequest.get());
					final Object[] row = (Object[]) iterator.next();
					final int projectId = ((Integer) row[0]).intValue();
					final String projectName = (String) row[1];
					final int iterationId = ((Integer) row[2]).intValue();
					final String iterationName = (String) row[3];
					final int storyId = ((Integer) row[4]).intValue();
					final String storyName = (String) row[5];
					final double totalDuration = ((Double) row[6])
							.doubleValue();

					if (SystemAuthorizer.get().hasPermission(projectId,
							remoteUserId, "system.project", projectId, "read")) {
						final TimesheetEntry time = new TimesheetEntry();
						time.setProjectId(projectId);
						time.setProjectName(projectName);
						time.setIterationId(iterationId);
						time.setIterationName(iterationName);
						time.setStoryId(storyId);
						time.setStoryName(storyName);
						time.setTotalDuration(totalDuration);
						timesheet.addEntry(time);
					}
				}
			} catch (final Exception ex) {
				this.log.error("query error", ex);
			}
		} catch (final Exception ex) {
			this.log.error("error in AggregateTimesheetQuery", ex);
		}
		return timesheet;
	}

	/**
     * Sets the person ids.
     *
     * @param personIds
     *            the new person ids
     */
	public void setPersonIds(final String[] personIds) {
		this.personIds = personIds;
	}

	/**
     * Gets the person id.
     *
     * @return the person id
     */
	public String[] getPersonId() {
		return this.personIds;
	}

	/**
     * Gets the start date.
     *
     * @return the start date
     */
	public java.util.Date getStartDate() {
		return this.startDate;
	}

	/**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
	public void setStartDate(final java.util.Date startDate) {
		this.startDate = startDate;
	}

	/**
     * Gets the end date.
     *
     * @return the end date
     */
	public java.util.Date getEndDate() {
		return this.endDate;
	}

	/**
     * Sets the end date.
     *
     * @param endDate
     *            the new end date
     */
	public void setEndDate(final java.util.Date endDate) {
		this.endDate = endDate;
	}
}
