package com.technoetic.xplanner.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.events.EventManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.DomainContext;
import com.technoetic.xplanner.tx.CheckedExceptionHandlingTransactionTemplate;
import com.technoetic.xplanner.util.Callable;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.util.RequestUtils;

/**
 * The Class AbstractAction.
 *
 * @param <T>
 *            the generic type
 */
public abstract class AbstractAction<T extends Identifiable> extends Action {
	
	/** The log. */
	private static Logger LOG = LogUtil.getLogger();

	/** The Constant TYPE_KEY. */
	public static final String TYPE_KEY = "@type";
	
	/** The Constant TARGET_OBJECT. */
	static final String TARGET_OBJECT = "targetObject";
	
	/** The type. */
	private String type;
	
	/** The event bus. */
	private EventManager eventBus;
	
	/** The history support. */
	protected HistorySupport historySupport;

	/** The transaction template. */
	protected CheckedExceptionHandlingTransactionTemplate transactionTemplate;

	/** The domain class. */
	protected final Class<T> domainClass;
	
	/** The common dao. */
	private CommonDao<T> commonDao;

	/**
     * Instantiates a new abstract action.
     */
	public AbstractAction() {
		final Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			final Object object = ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments()[0];
			if (object instanceof Class<?>) {
				this.domainClass = (Class<T>) object;
			} else {
				this.domainClass = null; // (Class<T>) ((TypeVariable)
											// object).getClass();
			}
		} else {
			this.domainClass = null; // (Class<T>) ((TypeVariable)
										// object).getClass();
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		if (AbstractAction.LOG.isDebugEnabled()) {
			final String name = mapping.getName();
			final String requestParams = RequestUtils.toString(request);
			final String action = "\naction=\"" + name + "\" requestParams="
					+ requestParams;
			AbstractAction.LOG.debug(action);
		}
		try {
			final ActionForward forward = (ActionForward) this.transactionTemplate
					.execute(new Callable() {
						@Override
						public Object run() throws Exception {
							final ActionForward forward = AbstractAction.this
									.doExecute(mapping, form, request, response);
							// DEBT JM DEBT: Should be a instance member. Try
							// converting
							// to SpringAction to have a stateful action with
							// members
							final T object = AbstractAction.this
									.getTargetObject(request);
							if (object != null) {
								AbstractAction.this.beforeObjectCommit(object,
										mapping, form, request, response);
							}
							return forward;

						}
					});
			this.afterObjectCommit(mapping, form, request, response);
			return forward;
		} catch (final Exception e) {
			AbstractAction.LOG.error(e);
			throw e;
		}
	}

	// DEBT(SPRING) Remove access to session. Access to the db should always go
	/**
     * Before object commit.
     *
     * @param object
     *            the object
     * @param actionMapping
     *            the action mapping
     * @param actionForm
     *            the action form
     * @param request
     *            the request
     * @param reply
     *            the reply
     * @throws Exception
     *             the exception
     */
	// through repositories or queries that are injected
	protected void beforeObjectCommit(final T object,
			final ActionMapping actionMapping, final ActionForm actionForm,
			final HttpServletRequest request, final HttpServletResponse reply)
			throws Exception {
	}

	/**
     * After object commit.
     *
     * @param actionMapping
     *            the action mapping
     * @param actionForm
     *            the action form
     * @param request
     *            the request
     * @param reply
     *            the reply
     * @throws Exception
     *             the exception
     */
	protected void afterObjectCommit(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
	}

	/**
     * Do execute.
     *
     * @param mapping
     *            the mapping
     * @param form
     *            the form
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the action forward
     * @throws Exception
     *             the exception
     */
	protected abstract ActionForward doExecute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
     * Gets the object type.
     *
     * @param actionMapping
     *            the action mapping
     * @param request
     *            the request
     * @return the object type
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws ServletException
     *             the servlet exception
     */
	protected Class getObjectType(final ActionMapping actionMapping,
			final HttpServletRequest request) throws ClassNotFoundException,
			ServletException {
		String className = this.type;
		if (className == null) {
			className = this.getObjectTypeFromForward(actionMapping, className);
		}
		if (className == null) {
			className = request.getParameter(AbstractAction.TYPE_KEY);
		}
		if (className != null) {
			return Class.forName(className);
		} else {
			throw new ServletException(
					"no object type is specified in mapping or request");
		}
	}

	/**
     * Gets the object type from forward.
     *
     * @param actionMapping
     *            the action mapping
     * @param className
     *            the class name
     * @return the object type from forward
     */
	private String getObjectTypeFromForward(final ActionMapping actionMapping,
			String className) {
		final ActionForward forward = actionMapping
				.findForward(AbstractAction.TYPE_KEY);
		if (forward != null) {
			className = forward.getPath();
		}
		return className;
	}

	/**
     * Sets the domain context.
     *
     * @param request
     *            the request
     * @param object
     *            the object
     * @param actionMapping
     *            the action mapping
     * @return the domain context
     * @throws Exception
     *             the exception
     */
	protected DomainContext setDomainContext(final HttpServletRequest request,
			final Object object, final ActionMapping actionMapping)
			throws Exception {
		DomainContext domainContext = DomainContext.get(request);
		if (domainContext != null) {
			return domainContext;
		}
		domainContext = new DomainContext();
		domainContext.populate(object);
		domainContext.setActionMapping(actionMapping);
		final String projectIdParam = request.getParameter("projectId");
		if (domainContext.getProjectId() == 0
				&& StringUtils.isNotEmpty(projectIdParam)
				&& !projectIdParam.equals("0")) {
			final Project project = this.getCommonDao().getById(Project.class,
					Integer.parseInt(request.getParameter("projectId")));
			domainContext.populate(project);
		}
		domainContext.save(request);
		return domainContext;
	}

	/**
     * Adds the error.
     *
     * @param request
     *            the request
     * @param errorKey
     *            the error key
     */
	public void addError(final HttpServletRequest request, final String errorKey) {
		this.addError(request, new ActionError(errorKey));
	}

	/**
     * Adds the error.
     *
     * @param request
     *            the request
     * @param errorKey
     *            the error key
     * @param arg
     *            the arg
     */
	public void addError(final HttpServletRequest request,
			final String errorKey, final Object arg) {
		this.addError(request, new ActionError(errorKey, arg));
	}

	/**
     * Adds the error.
     *
     * @param request
     *            the request
     * @param error
     *            the error
     */
	private void addError(final HttpServletRequest request,
			final ActionError error) {
		final ActionErrors errors = this.getActionErrors(request);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
	}

	/**
     * Gets the action errors.
     *
     * @param request
     *            the request
     * @return the action errors
     */
	private ActionErrors getActionErrors(final HttpServletRequest request) {
		ActionErrors errors = (ActionErrors) request
				.getAttribute(Globals.ERROR_KEY);
		if (errors == null) {
			errors = new ActionErrors();
			request.setAttribute(Globals.ERROR_KEY, errors);
		}
		return errors;
	}

	/**
     * Gets the general error forward.
     *
     * @param mapping
     *            the mapping
     * @param request
     *            the request
     * @param errorKey
     *            the error key
     * @return the general error forward
     */
	public ActionForward getGeneralErrorForward(final ActionMapping mapping,
			final HttpServletRequest request, final String errorKey) {
		this.addError(request, errorKey);
		return mapping.findForward("error");
	}

	/**
     * Gets the general error forward.
     *
     * @param mapping
     *            the mapping
     * @param request
     *            the request
     * @param errorKey
     *            the error key
     * @param arg
     *            the arg
     * @return the general error forward
     */
	public ActionForward getGeneralErrorForward(final ActionMapping mapping,
			final HttpServletRequest request, final String errorKey,
			final Object arg) {
		this.addError(request, errorKey, arg);
		return mapping.findForward("error");

	}

	/**
     * Gets the target object.
     *
     * @param request
     *            the request
     * @return the target object
     */
	@SuppressWarnings("unchecked")
	protected T getTargetObject(final HttpServletRequest request) {
		return (T) request.getAttribute(AbstractAction.TARGET_OBJECT);
	}

	/**
     * Sets the target object.
     *
     * @param request
     *            the request
     * @param target
     *            the target
     */
	protected void setTargetObject(final HttpServletRequest request,
			final Object target) {
		request.setAttribute(AbstractAction.TARGET_OBJECT, target);
	}

	/**
     * Gets the session.
     *
     * @param request
     *            the request
     * @return the session
     */
	protected Session getSession(final HttpServletRequest request) {
		return HibernateHelper.getSession(request);
	}

	/**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
	public void setType(final String type) {
		this.type = type;
	}

	/**
     * Sets the transaction template.
     *
     * @param transactionTemplate
     *            the new transaction template
     */
	public void setTransactionTemplate(
			final CheckedExceptionHandlingTransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
     * Gets the iteration.
     *
     * @param id
     *            the id
     * @return the iteration
     * @throws RepositoryException
     *             the repository exception
     */
	public Iteration getIteration(final int id) throws RepositoryException {
		return this.getCommonDao().getById(Iteration.class, id);
	}

	/**
     * Sets the event bus.
     *
     * @param eventBus
     *            the new event bus
     */
	public void setEventBus(final EventManager eventBus) {
		this.eventBus = eventBus;
	}

	/**
     * Gets the event bus.
     *
     * @return the event bus
     */
	public EventManager getEventBus() {
		return this.eventBus;
	}

	/**
     * Gets the logged in user.
     *
     * @param request
     *            the request
     * @return the logged in user
     */
	protected final Person getLoggedInUser(final HttpServletRequest request) {
		try {
			final int remoteUserId = SecurityHelper.getRemoteUserId(request);
			return this.getCommonDao().getById(Person.class, remoteUserId);
		} catch (final AuthenticationException e) {
			AbstractAction.LOG.error("", e);
		}
		return null;

	}

	/**
     * Gets the common dao.
     *
     * @return the common dao
     */
	public CommonDao<T> getCommonDao() {
		return this.commonDao;
	}

	/**
     * Sets the common dao.
     *
     * @param commonDao
     *            the new common dao
     */
	@Autowired
	@Required
	public void setCommonDao(final CommonDao<T> commonDao) {
		this.commonDao = commonDao;
	}

	/**
     * Sets the history support.
     *
     * @param historySupport
     *            the new history support
     */
	@Autowired
	@Required
	public void setHistorySupport(final HistorySupport historySupport) {
		this.historySupport = historySupport;
	}

}
