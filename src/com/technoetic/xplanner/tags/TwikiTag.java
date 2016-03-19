package com.technoetic.xplanner.tags;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;
import org.hibernate.SessionFactory;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.wiki.SchemeHandler;
import com.technoetic.xplanner.wiki.SimpleSchemeHandler;
import com.technoetic.xplanner.wiki.TwikiFormat;
import com.technoetic.xplanner.wiki.WikiFormat;

/**
 * The Class TwikiTag.
 */
public class TwikiTag extends RequestContextAwareTag {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1524094715817563540L;
    
    /** The log. */
    private static Logger LOG = LogUtil.getLogger();
    
    /** The name. */
    private String name;
    
    /** The property. */
    private String property;
    
    /** The formatter. */
    private static WikiFormat formatter;
    
    /** The scheme handlers. */
    private HashMap schemeHandlers;

    /** The prefix. */
    final private String prefix = "twiki.scheme.";
    
    /** The handler suffix. */
    final private String handlerSuffix = ".handler";
    
    /** The wiki key. */
    final private String wikiKey = this.prefix + "wiki";

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            final Object obj = RequestUtils.lookup(this.pageContext, this.name,
                    null);
            final String content = (String) PropertyUtils.getProperty(obj,
                    this.property);
            final Properties properties = new DomainSpecificPropertiesFactory(
                    this.getRequestContext().getWebApplicationContext()
                            .getBean(SessionFactory.class),
                    XPlannerProperties.getProperties())
                    .createPropertiesFor(obj);
            final WikiFormat formatter = this.getFormatter(properties);
            if (content != null) {
                formatter.setProperties(properties);
                this.pageContext.getOut().print(
                        formatter.format(content.toString()));
            }
        } catch (final Exception ex) {
            throw new JspException(ex);
        }
        return Tag.EVAL_PAGE;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the property.
     *
     * @param property
     *            the new property
     */
    public void setProperty(final String property) {
        this.property = property;
    }

    /**
     * Gets the property.
     *
     * @return the property
     */
    public String getProperty() {
        return this.property;
    }

    /**
     * Gets the formatter.
     *
     * @param properties
     *            the properties
     * @return the formatter
     */
    private synchronized WikiFormat getFormatter(final Properties properties) {
        if (TwikiTag.formatter == null) {
            // Read formatter class name from properties
            if (properties.getProperty("wiki.format") != null) {
                try {
                    TwikiTag.formatter = (WikiFormat) Class.forName(
                            properties.getProperty("wiki.format"))
                            .newInstance();
                } catch (final Exception e) {
                    TwikiTag.LOG.error(
                            "Cannot instantiate wiki format, using default: ",
                            e);
                    // Fall back to default
                    TwikiTag.formatter = new TwikiFormat();
                }
            } else {
                // Fall back to default
                TwikiTag.formatter = new TwikiFormat();
            }
        }
        TwikiTag.formatter.setProperties(properties);
        if (this.schemeHandlers == null || this.schemeHandlers.isEmpty()) {
            this.schemeHandlers = this.loadSchemeHandlers(properties);
        } else {
            final String translation = properties.getProperty(this.wikiKey);
            this.schemeHandlers.put(
                    this.wikiKey.substring(this.prefix.length()),
                    new SimpleSchemeHandler(translation));
        }
        TwikiTag.formatter = new TwikiFormat(this.schemeHandlers);
        return TwikiTag.formatter;
    }

    /**
     * Load scheme handlers.
     *
     * @param properties
     *            the properties
     * @return the hash map
     */
    private HashMap loadSchemeHandlers(final Properties properties) {
        final HashMap schemeHandlers = new HashMap();
        final Enumeration keys = properties.keys();
        while (keys.hasMoreElements()) {
            final String key = (String) keys.nextElement();
            if (key.startsWith(this.prefix)) {
                final String translation = properties.getProperty(key);
                if (key.endsWith(this.handlerSuffix)
                        && !key.equals(this.prefix
                                + this.handlerSuffix.substring(1))) {
                    try {
                        String className = translation;
                        String argument = null;
                        final int argumentOffset = translation.indexOf(";");
                        if (argumentOffset != -1) {
                            className = translation
                                    .substring(0, argumentOffset);
                            argument = translation
                                    .substring(argumentOffset + 1);
                        }
                        final Class handlerClass = Class.forName(className);
                        SchemeHandler handler = null;
                        try {
                            final Constructor argConstructor = handlerClass
                                    .getConstructor(new Class[] { String.class });
                            if (argConstructor != null) {
                                handler = (SchemeHandler) argConstructor
                                        .newInstance(new Object[] { argument });
                            }
                        } catch (final Exception e) {
                            handler = (SchemeHandler) handlerClass
                                    .newInstance();
                        }
                        final String handlerKey = key.substring(
                                this.prefix.length(), key.length()
                                        - this.handlerSuffix.length());
                        schemeHandlers.put(handlerKey, handler);
                    } catch (final Exception e) {
                        TwikiTag.LOG.error("error", e);
                    }
                } else {
                    schemeHandlers.put(key.substring(this.prefix.length()),
                            new SimpleSchemeHandler(translation));
                }
            }
        }
        return schemeHandlers;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
     */
    @Override
    protected int doStartTagInternal() throws Exception {
        return 0;
    }
}