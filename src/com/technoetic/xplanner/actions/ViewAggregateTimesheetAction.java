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

import com.technoetic.xplanner.db.AggregateTimesheetQuery;
import com.technoetic.xplanner.forms.AggregateTimesheetForm;

public class ViewAggregateTimesheetAction extends AbstractAction {
	private static final Logger log = Logger
			.getLogger("ViewAggregateTimesheetAction");

	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final AggregateTimesheetForm form = (AggregateTimesheetForm) actionForm;
		try {
			final Session session = this.getSession(request);
			try {

				form.setAllPeople(session
						.find("from people in class net.sf.xplanner.domain.Person "
								+ "where people.hidden = false order by name"));
				final AggregateTimesheetQuery query = new AggregateTimesheetQuery(
						this.getSession(request));
				query.setPersonIds(form.getSelectedPeople());
				query.setStartDate(form.getStartDate());
				query.setEndDate(form.getEndDate());
				form.setTimesheet(query.getTimesheet());
				if (form.getDateFormat() == null) {
					final String format = this.getResources(request)
							.getMessage("format.date");
					form.setDateFormat(new SimpleDateFormat(format));
				}
				return actionMapping.findForward("view/aggregateTimesheet");
			} catch (final Exception ex) {
				session.connection().rollback();
				ViewAggregateTimesheetAction.log.error("error", ex);
				throw new ServletException(ex);
			}
		} catch (final ServletException ex) {
			throw ex;
		} catch (final Exception ex) {
			ViewAggregateTimesheetAction.log.error("error", ex);
			throw new ServletException(ex);
		}
	}
}
