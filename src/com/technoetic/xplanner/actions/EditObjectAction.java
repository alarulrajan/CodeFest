package com.technoetic.xplanner.actions;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.DomainObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class EditObjectAction.
 *
 * @param <T>
 *            the generic type
 */
public class EditObjectAction<T extends Identifiable> extends AbstractAction<T> {
	
	/** The Constant log. */
	protected static final Logger log = LogUtil.getLogger();
	
	/** The Constant UPDATE_ACTION. */
	public static final String UPDATE_ACTION = "Update";
	
	/** The Constant CREATE_ACTION. */
	public static final String CREATE_ACTION = "Create";

	/** The Constant RETURNTO_PARAM. */
	public static final String RETURNTO_PARAM = "returnto";
	
	/** The Constant MERGE_PARAM. */
	public static final String MERGE_PARAM = "merge";

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		final AbstractEditorForm form = (AbstractEditorForm) actionForm;
		if (form.isSubmitted()) {
			this.saveForm(form, actionMapping, request);
			this.setCookies(form, actionMapping, request, reply);
			final String returnto = request
					.getParameter(EditObjectAction.RETURNTO_PARAM);
			return returnto != null ? new ActionForward(returnto, true)
					: actionMapping.findForward("view/projects");
		} else {
			this.populateForm(form, actionMapping, request);
			return new ActionForward(actionMapping.getInput());
		}
	}

	/**
     * Sets the cookies.
     *
     * @param form
     *            the form
     * @param mapping
     *            the mapping
     * @param request
     *            the request
     * @param response
     *            the response
     */
	protected void setCookies(final AbstractEditorForm form,
			final ActionMapping mapping, final HttpServletRequest request,
			final HttpServletResponse response) {
	}

	/**
     * Save form.
     *
     * @param form
     *            the form
     * @param actionMapping
     *            the action mapping
     * @param request
     *            the request
     * @throws Exception
     *             the exception
     */
	@SuppressWarnings("unchecked")
	protected void saveForm(final AbstractEditorForm form,
			final ActionMapping actionMapping, final HttpServletRequest request)
			throws Exception {
		final String oid = form.getOid();
		final Class<? extends T> objectClass = this.getObjectType(
				actionMapping, request);
		T object;
		final String action = form.getAction();
		if (action.equals(EditObjectAction.UPDATE_ACTION)) {
			object = this.updateObject(oid, request, form);
		} else if (action.equals(EditObjectAction.CREATE_ACTION)) {
			object = this.createObject(objectClass, request, form);
		} else {
			throw new ServletException("Unknown editor action: " + action);
		}
		this.setTargetObject(request, object);
		form.setAction(null);
	}

	/**
     * Update object.
     *
     * @param oid
     *            the oid
     * @param request
     *            the request
     * @param form
     *            the form
     * @return the t
     * @throws Exception
     *             the exception
     */
	protected T updateObject(final String oid,
			final HttpServletRequest request, final AbstractEditorForm form)
			throws Exception {
		final T object = this.getCommonDao().getById(this.domainClass,
				Integer.parseInt(oid));
		this.getEventBus().publishUpdateEvent(form, (Nameable) object,
				this.getLoggedInUser(request));
		this.populateObject(request, object, form);
		this.getCommonDao().save(object);
		return object;
	}

	/**
     * Creates the object.
     *
     * @param objectClass
     *            the object class
     * @param request
     *            the request
     * @param form
     *            the form
     * @return the t
     * @throws Exception
     *             the exception
     */
	protected final T createObject(final Class<? extends T> objectClass,
			final HttpServletRequest request, final AbstractEditorForm form)
			throws Exception {
		final T object = objectClass.newInstance();

		this.populateObject(request, object, form);
		final int savedObjectId = this.getCommonDao().save(object);
		form.setId(savedObjectId);
		this.getEventBus().publishCreateEvent(object,
				this.getLoggedInUser(request));
		return object;
	}

	/**
     * Populate form.
     *
     * @param form
     *            the form
     * @param actionMapping
     *            the action mapping
     * @param request
     *            the request
     * @throws Exception
     *             the exception
     */
	protected void populateForm(final AbstractEditorForm form,
			final ActionMapping actionMapping, final HttpServletRequest request)
			throws Exception {
		final String oid = form.getOid();
		if (oid != null) {
			final DomainObject object = (DomainObject) this.getCommonDao()
					.getById(this.domainClass, Integer.parseInt(oid));
			this.populateForm(form, object);
		}
	}

	/**
     * Populate form.
     *
     * @param form
     *            the form
     * @param object
     *            the object
     * @throws Exception
     *             the exception
     */
	protected void populateForm(final AbstractEditorForm form,
			final DomainObject object) throws Exception {
		this.copyProperties(form, object);
		this.populateManyToOneIds(form, object);
	}

	/**
     * Copy properties.
     *
     * @param destination
     *            the destination
     * @param source
     *            the source
     * @throws Exception
     *             the exception
     */
	protected final void copyProperties(final Object destination,
			final Object source) throws Exception {
		final BeanInfo info = Introspector.getBeanInfo(source.getClass());
		final PropertyDescriptor[] properties = info.getPropertyDescriptors();
		for (int i = 0; i < properties.length; i++) {
			final PropertyDescriptor sourceProperty = properties[i];
			final PropertyDescriptor destinationProperty = this.findProperty(
					destination, sourceProperty.getName());
			if (destinationProperty != null
					&& destinationProperty.getWriteMethod() != null
					&& sourceProperty.getReadMethod() != null) {
				final Object value = sourceProperty.getReadMethod().invoke(
						source, new Object[0]);
				EditObjectAction.log.debug("  " + destinationProperty.getName()
						+ "=" + value);
				destinationProperty.getWriteMethod().invoke(destination,
						new Object[] { value });
			}
		}
	}

	/**
     * Find property.
     *
     * @param object
     *            the object
     * @param name
     *            the name
     * @return the property descriptor
     * @throws IntrospectionException
     *             the introspection exception
     */
	private PropertyDescriptor findProperty(final Object object,
			final String name) throws IntrospectionException {
		final BeanInfo info = Introspector.getBeanInfo(object.getClass());
		final PropertyDescriptor[] properties = info.getPropertyDescriptors();
		for (int i = 0; i < properties.length; i++) {
			final PropertyDescriptor property = properties[i];
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}

	/**
     * Populate many to one ids.
     *
     * @param form
     *            the form
     * @param object
     *            the object
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
	protected void populateManyToOneIds(final ActionForm form,
			final DomainObject object) throws IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final Collection mappings = RelationshipMappingRegistry.getInstance()
				.getRelationshipMappings(object);
		for (final Iterator iterator = mappings.iterator(); iterator.hasNext();) {
			final RelationshipConvertor convertor = (RelationshipConvertor) iterator
					.next();
			convertor.populateAdapter(form, object);
		}
	}

	/**
     * Populate object.
     *
     * @param request
     *            the request
     * @param object
     *            the object
     * @param form
     *            the form
     * @throws Exception
     *             the exception
     */
	protected void populateObject(final HttpServletRequest request,
			final Object object, final ActionForm form) throws Exception {
		EditObjectAction.log.debug("Populating object "
				+ object.getClass().getName() + " "
				+ ((DomainObject) object).getId());
		if ("true".equals(request.getParameter(EditObjectAction.MERGE_PARAM))) {
			RequestUtils.populate(object, request);
			// ChangeSoon: should we populate many-to-one rels in this mode?
		} else {
			this.copyProperties(object, form);
			this.populateManyToOneRelationships((DomainObject) object, form);
		}
	}

	/**
     * Populate many to one relationships.
     *
     * @param object
     *            the object
     * @param form
     *            the form
     * @throws Exception
     *             the exception
     */
	protected void populateManyToOneRelationships(final DomainObject object,
			final ActionForm form) throws Exception {
		final Collection mappings = RelationshipMappingRegistry.getInstance()
				.getRelationshipMappings(object);
		for (final Iterator iterator = mappings.iterator(); iterator.hasNext();) {
			final RelationshipConvertor convertor = (RelationshipConvertor) iterator
					.next();
			convertor.populateDomainObject(object, form, this.getCommonDao());
		}
	}

}
