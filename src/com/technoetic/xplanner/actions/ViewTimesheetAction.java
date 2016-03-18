package com.technoetic.xplanner.actions;

import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.PersonTimesheetQuery;
import com.technoetic.xplanner.forms.PersonTimesheetForm;

public class ViewTimesheetAction extends AbstractAction {
	private static final Logger log = Logger.getLogger("ViewTimesheetAction");

	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final PersonTimesheetForm form = (PersonTimesheetForm) actionForm;
		try {
			final Session session = this.getSession(request);
			try {
				final PersonTimesheetQuery query = new PersonTimesheetQuery(
						this.getSession(request));
				query.setPersonId(form.getPersonId());
				query.setStartDate(form.getStartDate());
				query.setEndDate(form.getEndDate());
				form.setTimesheet(query.getTimesheet());
				if (form.getDateFormat() == null) {
					final String format = this.getResources(request)
							.getMessage("format.date");
					form.setDateFormat(new SimpleDateFormat(format));
				}

				return actionMapping.findForward("view/timesheet");
			} catch (final Exception ex) {
				session.connection().rollback();
				ViewTimesheetAction.log.error("error", ex);
				throw new ServletException(ex);
			}
		} catch (final ServletException ex) {
			throw ex;
		} catch (final Exception ex) {
			ViewTimesheetAction.log.error("error", ex);
			throw new ServletException(ex);
		}
	}
}
