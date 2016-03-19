package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Integration;

// todo - add history event for completed integration.

/**
 * The Class IntegrationAction.
 */
public class IntegrationAction extends AbstractAction implements
		BeanFactoryAware {
	
	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());
	
	/** The Constant NOTIFICATIONS_DISABLED. */
	private static final String NOTIFICATIONS_DISABLED = "nonotify";

	/** The listeners. */
	private Collection listeners = Collections
			.synchronizedList(new ArrayList());
	
	/** The bean factory. */
	private AutowireCapableBeanFactory beanFactory;

	/**
     * Inits the.
     */
	public void init() {
		final XPlannerProperties props = new XPlannerProperties();
		final String listenersString = props
				.getProperty("xplanner.integration.listeners");
		if (StringUtils.isNotEmpty(listenersString)) {
			final String[] listeners = listenersString.split(",");
			for (int i = 0; i < listeners.length; i++) {
				try {
					this.addIntegrationListener((IntegrationListener) Class
							.forName(listeners[i]).newInstance());
				} catch (final Exception ex) {
					this.log.error("error initializing listeners", ex);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		try {
			final Session session = this.getSession(request);
			session.connection().setAutoCommit(false);
			try {
				ActionForward forward = actionMapping.findForward("display");
				final int projectId = Integer.parseInt(request
						.getParameter("projectId"));
				if (request.getParameter("action.join") != null) {
					forward = this.onJoinRequest(session, request,
							actionMapping, projectId);
				} else if (this.isLeaveRequest(request)) {
					this.onLeaveRequest(session, request, projectId);
				} else if (request.getParameter("action.start") != null) {
					forward = this.onStartRequest(session, actionMapping,
							request, projectId);
				} else if (request.getParameter("action.finish") != null) {
					this.onFinishRequest(session, request, projectId);
				} else if (request.getParameter("action.cancel") != null) {
					this.onCancelRequest(session, request, projectId);
				} else if (request.getParameter("personId") != null
						&& request.getParameter("comment") != null) {
					// default if no action, <cr> in comment field instead of
					// pressing button
					forward = this.onStartRequest(session, actionMapping,
							request, projectId);
				}
				session.flush();
				session.connection().commit();
				return this.addProjectId(request, forward);
			} catch (final Exception ex) {
				session.connection().rollback();
				return actionMapping.findForward("error");
			}
		} catch (final Exception ex) {
			throw new ServletException("session error", ex);
		}
	}

	/**
     * Adds the project id.
     *
     * @param request
     *            the request
     * @param forward
     *            the forward
     * @return the action forward
     */
	private ActionForward addProjectId(final HttpServletRequest request,
			final ActionForward forward) {
		return new ActionForward(forward.getPath() + "?projectId="
				+ request.getParameter("projectId"), forward.getRedirect());
	}

	/**
     * On join request.
     *
     * @param session
     *            the session
     * @param request
     *            the request
     * @param actionMapping
     *            the action mapping
     * @param projectId
     *            the project id
     * @return the action forward
     * @throws Exception
     *             the exception
     */
	private ActionForward onJoinRequest(final Session session,
			final HttpServletRequest request,
			final ActionMapping actionMapping, final int projectId)
			throws Exception {
		final String personId = request.getParameter("personId");
		final String comment = request.getParameter("comment");

		if (StringUtils.isEmpty(personId) || personId.equals("0")) {
			this.saveError(request, "integrations.error.noperson");
			return actionMapping.findForward("error");
		}
		final Integration integration = new Integration();
		integration.setProjectId(Integer.parseInt(request
				.getParameter("projectId")));
		integration.setPersonId(Integer.parseInt(personId));
		integration.setComment(comment);
		integration.setState(Integration.PENDING);
		integration.setWhenRequested(new Date());

		final List pendingIntegrations = this.getIntegrationsInState(session,
				Integration.PENDING, projectId);
		if (pendingIntegrations.size() == 0) {
			final Integration activeIntegration = this
					.getFirstIntegrationInState(session, Integration.ACTIVE,
							projectId);
			if (activeIntegration == null) {
				this.startIntegration(integration);
			}
		}

		session.save(integration);
		return actionMapping.findForward("display");
	}

	/**
     * On leave request.
     *
     * @param session
     *            the session
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @throws Exception
     *             the exception
     */
	private void onLeaveRequest(final Session session,
			final HttpServletRequest request, final int projectId)
			throws Exception {
		final Integer oid = this.getLeaveOid(request);

		final List pendingIntegrations = this.getIntegrationsInState(session,
				Integration.PENDING, projectId);
		final Integration firstPendingIntegration = (Integration) pendingIntegrations
				.get(0);

		final Integration leavingIntegration = (Integration) session.load(
				Integration.class, oid);
		session.delete(leavingIntegration);

		if (this.isNotificationEnabled(request)
				&& leavingIntegration.getId() == firstPendingIntegration
						.getId() && pendingIntegrations.size() > 1) {
			this.fireIntegrationEvent(
					IntegrationListener.INTEGRATION_READY_EVENT,
					(Integration) pendingIntegrations.get(1), request);
		}
	}

	/**
     * Gets the leave oid.
     *
     * @param request
     *            the request
     * @return the leave oid
     */
	private Integer getLeaveOid(final HttpServletRequest request) {
		return new Integer(this.getLeavesParameter(request).substring(13));
	}

	/**
     * Checks if is leave request.
     *
     * @param request
     *            the request
     * @return true, if is leave request
     */
	private boolean isLeaveRequest(final HttpServletRequest request) {
		return this.getLeavesParameter(request) != null;
	}

	/**
     * Gets the leaves parameter.
     *
     * @param request
     *            the request
     * @return the leaves parameter
     */
	private String getLeavesParameter(final HttpServletRequest request) {
		final Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			final String name = (String) names.nextElement();
			if (name.startsWith("action.leave")) {
				return name;
			}
		}
		return null;
	}

	/**
     * On start request.
     *
     * @param session
     *            the session
     * @param actionMapping
     *            the action mapping
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @return the action forward
     * @throws Exception
     *             the exception
     */
	private ActionForward onStartRequest(final Session session,
			final ActionMapping actionMapping,
			final HttpServletRequest request, final int projectId)
			throws Exception {
		final Integration activeIntegration = this.getFirstIntegrationInState(
				session, Integration.ACTIVE, projectId);
		if (activeIntegration == null) {
			final Integration integration = this.getFirstIntegrationInState(
					session, Integration.PENDING, projectId);
			this.startIntegration(integration);
			return actionMapping.findForward("display");
		} else {
			this.saveError(request, "integrations.error.alreadyactive");
			return actionMapping.findForward("error");
		}
	}

	/**
     * Save error.
     *
     * @param request
     *            the request
     * @param key
     *            the key
     */
	private void saveError(final HttpServletRequest request, final String key) {
		ActionErrors errors = (ActionErrors) request
				.getAttribute(Globals.ERROR_KEY);
		if (errors == null) {
			errors = new ActionErrors();
			// saveErrors() will not save an empty error collection
			request.setAttribute(Globals.ERROR_KEY, errors);
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError(key));
	}

	/**
     * Start integration.
     *
     * @param integration
     *            the integration
     */
	private void startIntegration(final Integration integration) {
		integration.setState(Integration.ACTIVE);
		integration.setWhenStarted(new Date());
	}

	/**
     * On finish request.
     *
     * @param session
     *            the session
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @throws Exception
     *             the exception
     */
	private void onFinishRequest(final Session session,
			final HttpServletRequest request, final int projectId)
			throws Exception {
		this.terminateIntegration(session, Integration.FINISHED, request,
				projectId);
	}

	/**
     * Terminate integration.
     *
     * @param session
     *            the session
     * @param terminalState
     *            the terminal state
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @throws Exception
     *             the exception
     */
	private void terminateIntegration(final Session session,
			final char terminalState, final HttpServletRequest request,
			final int projectId) throws Exception {
		final Integration integration = this.getFirstIntegrationInState(
				session, Integration.ACTIVE, projectId);
		integration.setState(terminalState);
		integration.setWhenComplete(new Date());

		final Integration readyIntegration = this.getFirstIntegrationInState(
				session, Integration.PENDING, projectId);
		if (this.isNotificationEnabled(request) && readyIntegration != null) {
			this.fireIntegrationEvent(
					IntegrationListener.INTEGRATION_READY_EVENT,
					readyIntegration, request);
		}
	}

	/**
     * Gets the integrations in state.
     *
     * @param session
     *            the session
     * @param state
     *            the state
     * @param projectId
     *            the project id
     * @return the integrations in state
     * @throws Exception
     *             the exception
     */
	private List getIntegrationsInState(final Session session,
			final char state, final int projectId) throws Exception {
		return session.find("from integration in " + Integration.class
				+ " where integration.state = ? and integration.projectId = ?",
				new Object[] { new Character(state), new Integer(projectId) },
				new Type[] { Hibernate.CHARACTER, Hibernate.INTEGER });
	}

	/**
     * Gets the first integration in state.
     *
     * @param session
     *            the session
     * @param state
     *            the state
     * @param projectId
     *            the project id
     * @return the first integration in state
     * @throws Exception
     *             the exception
     */
	private Integration getFirstIntegrationInState(final Session session,
			final char state, final int projectId) throws Exception {
		final Iterator iter = this.getIntegrationsInState(session, state,
				projectId).iterator();
		return (Integration) (iter.hasNext() ? iter.next() : null);
	}

	/**
     * On cancel request.
     *
     * @param session
     *            the session
     * @param request
     *            the request
     * @param projectId
     *            the project id
     * @throws Exception
     *             the exception
     */
	private void onCancelRequest(final Session session,
			final HttpServletRequest request, final int projectId)
			throws Exception {
		this.terminateIntegration(session, Integration.CANCELED, request,
				projectId);
	}

	/**
     * Sets the integration listeners.
     *
     * @param listeners
     *            the new integration listeners
     */
	public void setIntegrationListeners(final ArrayList listeners) {
		this.listeners = listeners;
	}

	/**
     * Adds the integration listener.
     *
     * @param listener
     *            the listener
     */
	public void addIntegrationListener(final IntegrationListener listener) {
		this.beanFactory.autowireBeanProperties(listener,
				AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
		this.listeners.add(listener);
	}

	/**
     * Fire integration event.
     *
     * @param eventType
     *            the event type
     * @param integration
     *            the integration
     * @param request
     *            the request
     */
	private void fireIntegrationEvent(final int eventType,
			final Integration integration, final HttpServletRequest request) {
		final Iterator iter = this.listeners.iterator();
		while (iter.hasNext()) {
			final IntegrationListener listener = (IntegrationListener) iter
					.next();
			try {
				listener.onEvent(eventType, integration, request);
			} catch (final Exception ex) {
				this.log.error("error dispatching integration event", ex);
			}
		}
	}

	// This can be used to disable notifications during acceptance/functional
	/**
     * Checks if is notification enabled.
     *
     * @param request
     *            the request
     * @return true, if is notification enabled
     */
	// testing
	private boolean isNotificationEnabled(final HttpServletRequest request) {
		return StringUtils.isEmpty(request
				.getParameter(IntegrationAction.NOTIFICATIONS_DISABLED))
				&& StringUtils
						.isEmpty((String) request
								.getAttribute(IntegrationAction.NOTIFICATIONS_DISABLED));
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(final BeanFactory beanFactory)
			throws BeansException {
		this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
	}
}