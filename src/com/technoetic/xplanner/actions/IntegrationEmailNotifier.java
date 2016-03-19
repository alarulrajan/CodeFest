package com.technoetic.xplanner.actions;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.mail.EmailMessage;
import com.technoetic.xplanner.mail.EmailMessageFactory;

/**
 * The Class IntegrationEmailNotifier.
 */
public class IntegrationEmailNotifier implements IntegrationListener {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());

    /** The properties. */
    Properties properties;
    
    /** The meta repository. */
    MetaRepository metaRepository;

    /**
     * Sets the properties.
     *
     * @param properties
     *            the new properties
     */
    public void setProperties(final Properties properties) {
        this.properties = properties;
    }

    /**
     * Sets the meta repository.
     *
     * @param metaRepository
     *            the new meta repository
     */
    public void setMetaRepository(final MetaRepository metaRepository) {
        this.metaRepository = metaRepository;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.IntegrationListener#onEvent(int, com.technoetic.xplanner.domain.Integration, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void onEvent(final int eventType, final Integration integration,
            final HttpServletRequest request) {
        if (eventType == IntegrationListener.INTEGRATION_READY_EVENT) {
            try {
                final MessageResources resources = (MessageResources) request
                        .getAttribute(Globals.MESSAGES_KEY);
                final EmailMessage email = new EmailMessageFactory(
                        this.metaRepository.getRepository(Person.class))
                        .createMessage();
                email.setFrom(this.properties
                        .getProperty(XPlannerProperties.EMAIL_FROM));
                email.setRecipient(integration.getPersonId());
                email.setCcRecipients(this.properties
                        .getProperty("xplanner.integration.mail.cc"));
                email.setSubject(resources
                        .getMessage("integrations.notification.subject"));
                final String link = request.getScheme() + "://"
                        + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath()
                        + "/do/view/integrations?projectId="
                        + integration.getProjectId();
                email.setBody(resources.getMessage(
                        "integrations.notification.text", link));
                email.send();
            } catch (final Exception ex) {
                this.log.error("couldn't send notification", ex);
            }
        }
    }
}