package com.technoetic.xplanner.mail;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.util.HttpClient;

/**
 * User: Mateusz Prokopowicz Date: May 19, 2005 Time: 4:15:39 PM.
 */
public class EmailFormatterImpl implements EmailFormatter {
    
    /** The Constant SUBJECT. */
    public static final String SUBJECT = "subject";
    
    /** The Constant TEMPLATE. */
    public static final String TEMPLATE = "template";
    
    /** The Constant TITLE. */
    public static final String TITLE = "title";
    
    /** The Constant HEADER. */
    public static final String HEADER = "header";
    
    /** The Constant FOOTER. */
    public static final String FOOTER = "footer";
    
    /** The velocity engine. */
    private VelocityEngine velocityEngine;
    
    /** The http client. */
    private HttpClient httpClient;

    /**
     * Sets the velocity engine.
     *
     * @param velocityEngine
     *            the new velocity engine
     */
    public void setVelocityEngine(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * Sets the http client.
     *
     * @param httpClient
     *            the new http client
     */
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailFormatter#formatEmailEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public String formatEmailEntry(final String header, final String footer,
            final String storyLabel, final String taskLabel,
            final List bodyEntryList) throws Exception {
        final Template template = this.velocityEngine
                .getTemplate("com/technoetic/xplanner/mail/velocity/email_notifications.vm");
        final VelocityContext velocityContext = new VelocityContext();
        final XPlannerProperties properties = new XPlannerProperties();
        final String applicationUrl = properties
                .getProperty(XPlannerProperties.APPLICATION_URL_KEY);
        final String style = this.httpClient.getPage(applicationUrl
                + "/css/email.css");
        velocityContext.put("hostUrl", applicationUrl);
        velocityContext.put("style", style);
        velocityContext.put("header", header);
        velocityContext.put("footer", footer);
        velocityContext.put("taskLabel", taskLabel);
        velocityContext.put("storyLabel", storyLabel);
        velocityContext.put("bodyEntries", bodyEntryList);
        final Writer stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        stringWriter.close();
        return stringWriter.toString();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.mail.EmailFormatter#formatEmailEntry(java.util.List, java.util.Map)
     */
    @Override
    public String formatEmailEntry(final List bodyEntryList,
            final Map<String, Object> params) throws Exception {
        final Template template = this.velocityEngine
                .getTemplate((String) params.get(EmailFormatterImpl.TEMPLATE));
        final Map<String, Object> context = new HashMap<String, Object>();
        final ResourceBundle bundle = ResourceBundle
                .getBundle("EmailResourceBundle");
        for (final String paramName : params.keySet()) {
            if (paramName.equals(EmailFormatterImpl.TITLE)
                    || paramName.equals(EmailFormatterImpl.HEADER)
                    || paramName.equals(EmailFormatterImpl.FOOTER)) {
                final String paramKey = (String) params.get(paramName);
                context.put(paramName, bundle.getString(paramKey));
            } else {
                context.put(paramName, params.get(paramName));
            }
        }
        final VelocityContext velocityContext = new VelocityContext(context);
        final XPlannerProperties properties = new XPlannerProperties();
        final String applicationUrl = properties
                .getProperty(XPlannerProperties.APPLICATION_URL_KEY);
        final String style = this.httpClient.getPage(applicationUrl
                + "/css/email.css");
        velocityContext.put("style", style);
        velocityContext.put("hostUrl", applicationUrl);
        final Writer stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        stringWriter.close();
        return stringWriter.toString();
    }
}
