package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.AttributeDao;
import net.sf.xplanner.domain.Attribute;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import com.technoetic.xplanner.wiki.WikiFormat;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */

public class EditProjectAction extends EditObjectAction<Project> {
    
    /** The attribute dao. */
    private AttributeDao attributeDao;

    /** The domain specific properties factory. */
    DomainSpecificPropertiesFactory domainSpecificPropertiesFactory;

    /**
     * Sets the domain specific properties factory.
     *
     * @param domainSpecificPropertiesFactory
     *            the new domain specific properties factory
     */
    public void setDomainSpecificPropertiesFactory(
            final DomainSpecificPropertiesFactory domainSpecificPropertiesFactory) {
        this.domainSpecificPropertiesFactory = domainSpecificPropertiesFactory;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.EditObjectAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward doExecute(final ActionMapping actionMapping,
            final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse reply) throws Exception {
        final ProjectEditorForm pef = (ProjectEditorForm) actionForm;
        // DEBT Move the notification management actions to its own action:
        // NotificationAction.add() & delete().
        if (pef.getAction() != null
                && (pef.getAction().equals(UpdateTimeNotificationReceivers.ADD) || pef
                        .getAction().equals(
                                UpdateTimeNotificationReceivers.DELETE))) {

            // return actionMapping.findForward("project/notification");
            return new ActionForward("/do/edit/project/notification", false);
        }
        return super.doExecute(actionMapping, actionForm, request, reply); // To
                                                                            // change
                                                                            // body
                                                                            // of
                                                                            // overridden
                                                                            // methods
                                                                            // use
                                                                            // File
                                                                            // |
                                                                            // Settings
                                                                            // |
                                                                            // File
                                                                            // Templates.
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.EditObjectAction#saveForm(com.technoetic.xplanner.forms.AbstractEditorForm, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected void saveForm(final AbstractEditorForm form,
            final ActionMapping actionMapping, final HttpServletRequest request)
            throws Exception {
        final String oid = form.getOid();
        final Class objectClass = this.getObjectType(actionMapping, request);
        Project object;
        final String action = form.getAction();
        if (action.equals(EditObjectAction.UPDATE_ACTION)) {
            object = this.getCommonDao().getById(Project.class,
                    Integer.parseInt(oid));
            this.populateObject(request, object, form);
            this.getCommonDao().save(object);
        } else if (action.equals(EditObjectAction.CREATE_ACTION)) {
            object = this.createObject(objectClass, request, form);
        } else {
            throw new ServletException("Unknown editor action: " + action);
        }
        this.setTargetObject(request, object);
        form.setAction(null);
        this.saveOrUpdateAttribute(XPlannerProperties.WIKI_URL_KEY, object,
                ((ProjectEditorForm) form).getWikiUrl());
        this.saveOrUpdateAttribute(
                XPlannerProperties.SEND_NOTIFICATION_KEY,
                object,
                new Boolean(((ProjectEditorForm) form)
                        .isSendingMissingTimeEntryReminderToAcceptor())
                        .toString());
        this.saveOrUpdateAttribute(WikiFormat.ESCAPE_BRACKETS_KEY, object,
                new Boolean(((ProjectEditorForm) form).isOptEscapeBrackets())
                        .toString());
    }

    /**
     * Save or update attribute.
     *
     * @param attributeName
     *            the attribute name
     * @param object
     *            the object
     * @param currentAttributeValue
     *            the current attribute value
     * @throws Exception
     *             the exception
     */
    private void saveOrUpdateAttribute(final String attributeName,
            final Nameable object, final String currentAttributeValue)
            throws Exception {
        final String attr = object.getAttribute(attributeName);
        if (attr != null) {
            final Attribute attribute = new Attribute(object.getId(),
                    attributeName, currentAttributeValue);
            this.attributeDao.save(attribute);
        } else {
            final String existingAttributeValue = new XPlannerProperties()
                    .getProperty(attributeName);
            if (existingAttributeValue != null
                    && !existingAttributeValue.equals(currentAttributeValue)
                    && !currentAttributeValue.equals("")) {
                final Attribute attribute = new Attribute(object.getId(),
                        attributeName, currentAttributeValue);
                this.attributeDao.save(attribute);
            }
            if (existingAttributeValue == null
                    && !currentAttributeValue.equals("")) {
                final Attribute attribute = new Attribute(object.getId(),
                        attributeName, currentAttributeValue);
                this.attributeDao.save(attribute);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.EditObjectAction#populateForm(com.technoetic.xplanner.forms.AbstractEditorForm, net.sf.xplanner.domain.DomainObject)
     */
    @Override
    protected void populateForm(final AbstractEditorForm form,
            final DomainObject object) throws Exception {
        super.populateForm(form, object);
        final ProjectEditorForm pef = (ProjectEditorForm) form;

        final Properties properties = this.domainSpecificPropertiesFactory
                .createPropertiesFor(object);
        pef.setWikiUrl(properties.getProperty(XPlannerProperties.WIKI_URL_KEY,
                "http://"));
        pef.setSendemail(Boolean.valueOf(
                properties.getProperty(
                        XPlannerProperties.SEND_NOTIFICATION_KEY, "true"))
                .booleanValue());
        pef.setOptEscapeBrackets(Boolean.valueOf(
                properties.getProperty(WikiFormat.ESCAPE_BRACKETS_KEY, "true"))
                .booleanValue());
        final Project project = (Project) object;
        final ProjectEditorForm projectEditorForm = (ProjectEditorForm) form;
        final Iterator itr = project.getNotificationReceivers().iterator();
        while (itr.hasNext()) {
            final Person person = (Person) itr.next();
            projectEditorForm.addPersonInfo("" + person.getId(),
                    person.getUserId(), person.getInitials(), person.getName());
        }

    }

    /**
     * Sets the attribute dao.
     *
     * @param attributeDao
     *            the new attribute dao
     */
    public void setAttributeDao(final AttributeDao attributeDao) {
        this.attributeDao = attributeDao;
    }

}
