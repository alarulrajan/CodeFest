package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.util.LogUtil;

/**
 * Implements a custom tag to add parameter to a href request.<br/>
 * This tag is intended to be nested in a <code>hm:link</code> tag.
 * 
 * Title: BSquare Description: Bsquare Projects Copyright: Copyright (c) 2001
 * Company: HubMethods
 * 
 * @author Eric Fesler
 * @version 1.0
 */

public class LinkParamTag extends BodyTagSupport {
    
    /** The cat. */
    // ----------------------------------------------------- Logging
    Logger cat = LogUtil.getLogger();

    // ----------------------------------------------------- Instance variables
    /** The name of the request parameter. */
    private String id = null;

    /** The value of the request parameter. */
    private String value = null;

    /** The source bean. */
    private String name = null;

    /** The source bean property. */
    private String property = null;

    /** The scope of the source bean. */
    private String scope = null;

    // ----------------------------------------------------- Properties

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
     */
    @Override
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Returns the request parameter name.
     *
     * @return the request parameter name
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Sets the request parameter value.
     *
     * @param value
     *            the request parameter value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Returns the request parameter value.
     *
     * @return the request parameter value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the source bean name.
     *
     * @param sourceBean
     *            the source bean name
     */
    public void setName(final String sourceBean) {
        this.name = sourceBean;
    }

    /**
     * Returns the source bean name.
     *
     * @return the source bean name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the source bean property.
     *
     * @param sourceProperty
     *            the source property
     */
    public void setProperty(final String sourceProperty) {
        this.property = sourceProperty;
    }

    /**
     * Returns the source bean property.
     *
     * @return the source property
     */
    public String getProperty() {
        return this.property;
    }

    /**
     * Set the source bean scope.
     * 
     * @param sourceScope
     *            the source bean scope
     */
    public void setScope(final String sourceScope) {
        this.scope = sourceScope;
    }

    /**
     * Returns the source bean scope.
     *
     * @return the source bean scope
     */
    public String getScope() {
        return this.scope;
    }

    // ------------------------------------------------------ Public Methods
    /**
     * Add the parameter and its value to the link tag.
     *
     * @return the int
     * @throws JspException
     *             the jsp exception
     */
    @Override
    public int doAfterBody() throws JspException {
        // parent tag must be a LinkTag, gives access to methods in parent
        final LinkTag myparent = (LinkTag) javax.servlet.jsp.tagext.TagSupport
                .findAncestorWithClass(this, LinkTag.class);

        if (myparent == null) {
            throw new JspException("linkparam tag not nested within link tag");
        } else {
            final BodyContent bodyContent = this.getBodyContent();
            if (bodyContent != null && !bodyContent.getString().equals("")) {
                this.setValue(bodyContent.getString());
            } else if (this.getValue() == null) {
                this.setValue("null");
            }
        }
        return Tag.SKIP_BODY;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        // parent tag must be a LinkTag, gives access to methods in parent
        final LinkTag myparent = (LinkTag) javax.servlet.jsp.tagext.TagSupport
                .findAncestorWithClass(this, LinkTag.class);

        if (myparent == null) {
            throw new JspException("linkparam tag not nested within link tag");
        }
        myparent.addRequestParameter(this.getId(), this.getValue());
        return Tag.EVAL_PAGE;
    }

    /**
     * Process the start tag.
     *
     * @return the int
     * @throws JspException
     *             the jsp exception
     */
    @Override
    public int doStartTag() throws javax.servlet.jsp.JspException {

        // Look up the requested property value
        if (this.name != null) {
            final Object beanValue = RequestUtils.lookup(this.pageContext,
                    this.name, this.property, this.scope);
            if (this.cat.isDebugEnabled()) {
                this.cat.debug("Value is : '" + beanValue + "'");
            }
            if (beanValue == null) {
                return BodyTag.EVAL_BODY_BUFFERED;
            }

            // set the property as value
            this.setValue(beanValue.toString());
        }

        // Continue processing this page
        return BodyTag.EVAL_BODY_BUFFERED;

    }
}