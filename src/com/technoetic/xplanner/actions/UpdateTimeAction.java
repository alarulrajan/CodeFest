package com.technoetic.xplanner.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.format.DecimalFormat;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.forms.TimeEditorForm;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class UpdateTimeAction.
 */
public class UpdateTimeAction extends AbstractAction<TimeEntry> {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger("UpdateTimeAction");
	
	/** The Constant UPDATE_TIME_ACTION. */
	public static final String UPDATE_TIME_ACTION = "UPDATE_TIME";
	
	/** The Constant UPDATE_ESTIMATE_ACTION. */
	public static final String UPDATE_ESTIMATE_ACTION = "UPDATE_ESTIMATE";
	
	/** The Constant SAVED_FORM_KEY. */
	private static final String SAVED_FORM_KEY = "UpdateTimeAction.savedForm";
	
	/** The task dao. */
	private TaskDao taskDao;

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final TimeEditorForm form = this
				.loadFormFromSessionIfExistsOrFromRequest(actionForm, request);
		try {
			try {
				ActionForward forward;
				final Task task = this.taskDao.getById(Integer.parseInt(form
						.getOid()));
				if (!form.isSubmitted()) {
					this.populateForm(task, form, request);
					forward = new ActionForward(actionMapping.getInput());
				} else {
					if (!this.isIterationStarted(request, task)) {
						this.saveFormInSession(request, form);
						request.setAttribute("from_edit/time", "true");
						return new ActionForward("/do/start/iteration", false);
					}
					forward = this.doAction(form, request, actionMapping, task);
				}
				return forward;
				// DEBT: Remove exception handling since it is completely
				// handled in super class.
			} catch (final Exception ex) {
				UpdateTimeAction.log.error("error", ex);
				this.getCommonDao().rollback();
				throw new ServletException(ex);
			}
		} catch (final ServletException ex) {
			throw ex;
		} catch (final Exception ex) {
			UpdateTimeAction.log.error("error", ex);
			throw new ServletException(ex);
		}
	}

	/**
     * Load form from session if exists or from request.
     *
     * @param actionForm
     *            the action form
     * @param request
     *            the request
     * @return the time editor form
     */
	private TimeEditorForm loadFormFromSessionIfExistsOrFromRequest(
			final ActionForm actionForm, final HttpServletRequest request) {
		TimeEditorForm form = (TimeEditorForm) actionForm;
		final TimeEditorForm savedForm = this.loadFormFromSession(request);
		if (savedForm != null) {
			form = savedForm;
			this.deleteFormFromSession(request);
		}
		return form;
	}

	/**
     * Save form in session.
     *
     * @param request
     *            the request
     * @param form
     *            the form
     */
	private void saveFormInSession(final HttpServletRequest request,
			final TimeEditorForm form) {
		request.getSession(true).setAttribute(UpdateTimeAction.SAVED_FORM_KEY,
				form);
	}

	/**
     * Delete form from session.
     *
     * @param request
     *            the request
     */
	private void deleteFormFromSession(final HttpServletRequest request) {
		request.getSession(true).removeAttribute(
				UpdateTimeAction.SAVED_FORM_KEY);
	}

	/**
     * Load form from session.
     *
     * @param request
     *            the request
     * @return the time editor form
     */
	private TimeEditorForm loadFormFromSession(final HttpServletRequest request) {
		return (TimeEditorForm) request.getSession(true).getAttribute(
				UpdateTimeAction.SAVED_FORM_KEY);
	}

	/**
     * Do action.
     *
     * @param form
     *            the form
     * @param request
     *            the request
     * @param actionMapping
     *            the action mapping
     * @param task
     *            the task
     * @return the action forward
     * @throws Exception
     *             the exception
     */
	private ActionForward doAction(final TimeEditorForm form,
			final HttpServletRequest request,
			final ActionMapping actionMapping, final Task task)
			throws Exception {
		if (form.getAction().equals(UpdateTimeAction.UPDATE_TIME_ACTION)) {
			return this.doUpdateTimeAction(form, request, actionMapping, task);
		} else if (form.getAction().equals(
				UpdateTimeAction.UPDATE_ESTIMATE_ACTION)) {
			return this.doUpdateEstimateAction(task, form, request,
					actionMapping);
		} else {
			throw new ServletException("Unknown action: " + form.getAction());
		}
	}

	/**
     * Do update time action.
     *
     * @param form
     *            the form
     * @param request
     *            the request
     * @param actionMapping
     *            the action mapping
     * @param task
     *            the task
     * @return the action forward
     * @throws Exception
     *             the exception
     */
	private ActionForward doUpdateTimeAction(final TimeEditorForm form,
			final HttpServletRequest request,
			final ActionMapping actionMapping, final Task task)
			throws Exception {
		final DecimalFormat decimalParser = new DecimalFormat(request);
		final SimpleDateFormat dateTimeFormat = this.getDateTimeFormat(request);
		final SimpleDateFormat dateFormat = this.getDateFormat(request);
		try {
			// List timeEntries =
			// session.find("from timeEntry in class net.sf.xplanner.domain.TimeEntry "
			// +
			// "where timeEntry.task.id = ? order by timeEntry.reportDate",
			// form.getOid(), Hibernate.STRING);

			final List<TimeEntry> timeEntries = task.getTimeEntries();
			double hoursWorked = 0;
			boolean rowDeleted = false;
			for (int i = 0; i < form.getRowcount(); i++) {

				int id = 0;
				if (this.isPresent(form.getEntryId(i))) {
					id = Integer.parseInt(form.getEntryId(i));
				}

				if (form.getDeleted(i) != null
						&& form.getDeleted(i).equals("true")) {
					final TimeEntry entry = this.getEntry(timeEntries, id);
					this.getCommonDao().delete(entry);
					rowDeleted = true;
					continue;
				}

				Date startTime = null;
				if (this.isPresent(form.getStartTime(i))) {
					startTime = dateTimeFormat.parse(form.getStartTime(i));
				}

				Date endTime = null;
				if (this.isPresent(form.getEndTime(i))) {
					endTime = dateTimeFormat.parse(form.getEndTime(i));
				}

				int person1Id = 0;
				if (this.isPresent(form.getPerson1Id(i))) {
					person1Id = Integer.parseInt(form.getPerson1Id(i));
				}

				int person2Id = 0;
				if (this.isPresent(form.getPerson2Id(i))) {
					person2Id = Integer.parseInt(form.getPerson2Id(i));
				}

				double duration = 0;
				if (this.isPresent(form.getDuration(i))) {
					duration = decimalParser.parse(form.getDuration(i));
				}

				Date reportDate = null;
				if (this.isPresent(form.getReportDate(i))) {
					reportDate = dateFormat.parse(form.getReportDate(i));
				}

				final String description = form.getDescription(i);

				if (id == 0) {
					if (startTime != null || duration > 0) {
						final TimeEntry entry = new TimeEntry();
						this.editEntry(entry, startTime, endTime, duration,
								person1Id, person2Id, reportDate, description);
						entry.setTask(task);
						this.getCommonDao().save(entry);

						// task = taskDao.getById(task .getId());
						hoursWorked += entry.getEffort();
						if (timeEntries == null || timeEntries.isEmpty()) {
							task.start();
						}
						this.taskDao.save(task);
					}
				} else {
					final TimeEntry entry = this.getEntry(timeEntries, id);
					this.editEntry(entry, startTime, endTime, duration,
							person1Id, person2Id, reportDate, description);
					hoursWorked += entry.getEffort();
				}
			}

			if (this.isPresent(form.getRemainingHours())) {
				final Double remainingHours = new Double(
						form.getRemainingHours());
				if (remainingHours.doubleValue() >= 0.0) {
					task.setEstimatedHours(hoursWorked
							+ remainingHours.doubleValue());
					this.taskDao.save(task);
				}
			}

			if (rowDeleted) {
				form.reset(actionMapping, request);
			}
			return new ActionForward(request.getParameter("returnto"), true);
		} finally {
			this.getCommonDao().flush();
		}
	}

	/**
     * Edits the entry.
     *
     * @param entry
     *            the entry
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * @param duration
     *            the duration
     * @param person1Id
     *            the person1 id
     * @param person2Id
     *            the person2 id
     * @param reportDate
     *            the report date
     * @param description
     *            the description
     */
	private void editEntry(final TimeEntry entry, final Date startTime,
			final Date endTime, final double duration, final int person1Id,
			final int person2Id, final Date reportDate, final String description) {
		entry.setStartTime(startTime);
		entry.setEndTime(endTime);
		entry.setDuration(duration);
		entry.setPerson1Id(person1Id);
		entry.setPerson2Id(person2Id);
		entry.setReportDate(reportDate);
		entry.setDescription(description);
	}

	/**
     * Checks if is iteration started.
     *
     * @param request
     *            the request
     * @param task
     *            the task
     * @return true, if is iteration started
     * @throws RepositoryException
     *             the repository exception
     * @throws HibernateException
     *             the hibernate exception
     */
	private boolean isIterationStarted(final HttpServletRequest request,
			final Task task) throws RepositoryException, HibernateException {// Autostart
																				// iteration
		final UserStory story = task.getUserStory();
		final Iteration iteration = story.getIteration();
		if (!iteration.isActive()) {
			request.setAttribute("edit/iteration", iteration);
			request.setAttribute(IterationStatusEditorForm.SAVE_TIME_ATTR,
					Boolean.TRUE);
			return false;
		}
		return true;
	}

	/**
     * Do update estimate action.
     *
     * @param task
     *            the task
     * @param form
     *            the form
     * @param request
     *            the request
     * @param actionMapping
     *            the action mapping
     * @return the action forward
     * @throws Exception
     *             the exception
     */
	private ActionForward doUpdateEstimateAction(final Task task,
			final TimeEditorForm form, final HttpServletRequest request,
			final ActionMapping actionMapping) throws Exception {
		try {
			final String oldEstimation = DecimalFormat.format(request,
					task.getEstimatedHours());
			this.historySupport.saveEvent(
					task,
					History.REESTIMATED,
					"Estimate changed from " + oldEstimation + " to "
							+ request.getParameter("estimate"),
					SecurityHelper.getRemoteUserId(request), new Date());
			task.setEstimatedHours(new DecimalFormat(request).parse(request
					.getParameter("estimate")));
			if (request.getSession().getAttribute("edit/iteration") != null) {
				request.setAttribute("edit/iteration", request.getSession()
						.getAttribute("edit/iteration"));
				request.getSession().removeAttribute("edit/iteration");
				return actionMapping.findForward("start/iteration");
			} else {
				return new ActionForward(request.getParameter("returnto"), true);
			}
		} finally {
			this.getCommonDao().flush();
		}
	}

	/**
     * Checks if is present.
     *
     * @param value
     *            the value
     * @return true, if is present
     */
	private boolean isPresent(final String value) {
		return value != null && !value.equals("") && !value.equals("null");
	}

	/**
     * Populate form.
     *
     * @param task
     *            the task
     * @param form
     *            the form
     * @param request
     *            the request
     * @throws Exception
     *             the exception
     */
	private void populateForm(final Task task, final TimeEditorForm form,
			final HttpServletRequest request) throws Exception {
		final SimpleDateFormat dateTimeFormat = this.getDateTimeFormat(request);
		final SimpleDateFormat dateFormat = this.getDateFormat(request);
		List<TimeEntry> entries = task.getTimeEntries();
		if (entries == null) {
			entries = new ArrayList<TimeEntry>();
		}
		// getCommonDao().rollback();

		int i = 0;

		TimeEntry timeEntry = null;
		for (final TimeEntry entry : entries) {
			form.setEntryId(i, Integer.toString(entry.getId()));
			if (entry.getStartTime() != null) {
				form.setStartTime(i,
						dateTimeFormat.format(entry.getStartTime()));
			} else {
				form.setStartTime(i, "");
			}
			if (entry.getEndTime() != null) {
				form.setEndTime(i, dateTimeFormat.format(entry.getEndTime()));
			} else {
				form.setEndTime(i, "");
			}
			form.setDuration(i,
					DecimalFormat.format(request, entry.getDuration()));
			form.setPerson1Id(i, Integer.toString(entry.getPerson1Id()));
			form.setPerson2Id(i, Integer.toString(entry.getPerson2Id()));
			if (entry.getReportDate() != null) {
				form.setReportDate(i, dateFormat.format(entry.getReportDate()));
			} else {
				form.setReportDate(i, "");
			}
			form.setDescription(i, entry.getDescription());
			i++;
			timeEntry = entry;
		}
		if (timeEntry == null || timeEntry.getEndTime() != null
				|| timeEntry.getDuration() > 0) {
			// start new row
			form.setEntryId(i, "0");
			form.setDeleted(i, null);
			form.setStartTime(i, "");
			form.setEndTime(i, "");
			form.setReportDate(i, dateFormat.format(new Date()));
			if (i == 0) {
				final int personId = task.getAcceptorId();
				form.setPerson1Id(i,
						personId == 0 ? null : Integer.toString(personId));
				form.setPerson2Id(i, null);
			} else {
				form.setPerson1Id(i, form.getPerson1Id(i - 1));
				form.setPerson2Id(i, form.getPerson2Id(i - 1));
			}
			i++;
		}
		form.setRowcount(i);
	}

	/**
     * Gets the entry.
     *
     * @param entries
     *            the entries
     * @param entryId
     *            the entry id
     * @return the entry
     */
	private TimeEntry getEntry(final List<TimeEntry> entries, final int entryId) {
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getId() == entryId) {
				return entries.get(i);
			}
		}
		return null;
	}

	/**
     * Gets the date time format.
     *
     * @param request
     *            the request
     * @return the date time format
     */
	private SimpleDateFormat getDateTimeFormat(final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return new SimpleDateFormat(this.getResources(request).getMessage(
				locale, "format.datetime"));
	}

	/**
     * Gets the date format.
     *
     * @param request
     *            the request
     * @return the date format
     */
	private SimpleDateFormat getDateFormat(final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return new SimpleDateFormat(this.getResources(request).getMessage(
				locale, "format.date"));
	}

	/**
     * Checks if is from update time.
     *
     * @param request
     *            the request
     * @return true, if is from update time
     */
	public static boolean isFromUpdateTime(final HttpServletRequest request) {
		return request.getAttribute("from_edit/time") != null;
	}

	/**
     * Sets the task dao.
     *
     * @param taskDao
     *            the new task dao
     */
	public void setTaskDao(final TaskDao taskDao) {
		this.taskDao = taskDao;
	}

}
