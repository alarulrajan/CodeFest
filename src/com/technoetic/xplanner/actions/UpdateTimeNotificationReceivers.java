package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.forms.ProjectEditorForm;

/**
 * User: Mateusz Prokopowicz Date: Dec 30, 2004 Time: 10:43:51 AM.
 */
public class UpdateTimeNotificationReceivers extends AbstractAction {
	
	/** The Constant ADD. */
	public static final String ADD = "addTimeNotification";
	
	/** The Constant DELETE. */
	public static final String DELETE = "delTimeNotification";
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger("UpdateTimeAction");

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final ProjectEditorForm form = (ProjectEditorForm) actionForm;
		try {
			final Session session = this.getSession(request);
			try {
				ActionForward forward = null;
				if (!form.isSubmitted()) {
					forward = new ActionForward(mapping.getInput());
				} else {
					forward = this.doAction(session, form, request, mapping);
				}
				this.populateForm(session, form, request);
				return forward;
			} catch (final Exception ex) {
				session.connection().rollback();
				UpdateTimeNotificationReceivers.log.error("error", ex);
				throw new ServletException(ex);
			}
		} catch (final ServletException ex) {
			throw ex;
		} catch (final Exception ex) {
			UpdateTimeNotificationReceivers.log.error("error", ex);
			throw new ServletException(ex);
		}
	}

	/**
     * Do action.
     *
     * @param session
     *            the session
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
	private ActionForward doAction(final Session session,
			final ProjectEditorForm form, final HttpServletRequest request,
			final ActionMapping actionMapping) throws Exception {
		final Project project = (Project) this.getCommonDao().getById(
				Project.class, Integer.parseInt(form.getOid()));
		final List<Person> receivers = project.getNotificationReceivers();
		if (form.getAction().equals(UpdateTimeNotificationReceivers.ADD)) {
			if (form.getPersonToAddId() != null
					&& !form.getPersonToAddId().equals("0")) {
				final Person person = (Person) this.getCommonDao()
						.getById(Person.class,
								Integer.parseInt(form.getPersonToAddId()));
				receivers.add(person);
				project.setNotificationReceivers(receivers);
				form.setPersonToAddId("0");
			}
		} else if (form.getAction().equals(
				UpdateTimeNotificationReceivers.DELETE)) {
			if (form.getPersonToDelete() != null) {
				final String personToDelId = form.getPersonToDelete();
				final Person person = (Person) this.getCommonDao().getById(
						Person.class, Integer.parseInt(personToDelId));
				receivers.remove(person);
				form.setPersonToDelete(null);
			}
		} else {
			throw new ServletException("Unknown action: " + form.getAction());
		}
		return new ActionForward(actionMapping.getInput());
	}

	/**
     * Populate form.
     *
     * @param session
     *            the session
     * @param form
     *            the form
     * @param request
     *            the request
     * @throws Exception
     *             the exception
     */
	private void populateForm(final Session session,
			final ProjectEditorForm form, final HttpServletRequest request)
			throws Exception {
		final Project project = (Project) session.load(Project.class,
				new Integer(form.getOid()));
		final Iterator itr = project.getNotificationReceivers().iterator();
		while (itr.hasNext()) {
			final Person person = (Person) itr.next();
			form.addPersonInfo("" + person.getId(), person.getUserId(),
					person.getInitials(), person.getName());
		}
	}
}
