package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

/**
 * The Class UseBeanTag.
 */
public class UseBeanTag extends DatabaseTagSupport {
    
    /** The Constant DEFAULT_OID_PARAMETER. */
    public static final String DEFAULT_OID_PARAMETER = "oid";

    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The id. */
    private String id;
    
    /** The type. */
    private String type;
    
    /** The oid. */
    private Object oid;
    
    /** The oid parameter. */
    private String oidParameter;
    
    /** The scope. */
    private String scope;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
     */
    @Override
    public void setId(final String id) {
        this.id = id;
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
     * Sets the oid.
     *
     * @param oid
     *            the new oid
     */
    public void setOid(final Object oid) {
        this.oid = oid;
    }

    /**
     * Sets the oid parameter.
     *
     * @param oidParameter
     *            the new oid parameter
     */
    public void setOidParameter(final String oidParameter) {
        this.oidParameter = oidParameter;
    }

    /**
     * Sets the scope.
     *
     * @param scope
     *            the new scope
     */
    public void setScope(final String scope) {
        this.scope = scope;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            final Session session = this.getSession();
            try {
                final Class clazz = Class.forName(this.type);
                final Object object = session.load(clazz, this.getObjectId());
                this.pageContext.setAttribute(this.id, object, this.getScope());
                if (this.log.isDebugEnabled()) {
                    this.log.debug("bean loaded: " + this.id + " " + object);
                }
            } catch (final Exception ex) {
                this.log.error("error", ex);
                throw new JspTagException(ex.toString());
            }
        } catch (final JspTagException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new JspTagException(ex.toString());
        }
        return Tag.EVAL_PAGE;
    }

    /**
     * Gets the object id.
     *
     * @return the object id
     */
    private Integer getObjectId() {
        Integer objectId = null;
        if (this.oid instanceof Integer) {
            objectId = (Integer) this.oid;
        } else if (this.oid instanceof String) {
            objectId = new Integer((String) this.oid);
        } else if (this.oidParameter != null) {
            objectId = new Integer(this.pageContext.getRequest().getParameter(
                    this.oidParameter));
        } else {
            final String oid = this.pageContext.getRequest().getParameter(
                    UseBeanTag.DEFAULT_OID_PARAMETER);
            if (oid != null) {
                objectId = new Integer(oid);
            }
        }
        return objectId;
    }

    /**
     * Gets the scope.
     *
     * @return the scope
     */
    private int getScope() {
        if ("application".equals(this.scope)) {
            return PageContext.APPLICATION_SCOPE;
        }
        if ("session".equals(this.scope)) {
            return PageContext.SESSION_SCOPE;
        }
        if ("request".equals(this.scope)) {
            return PageContext.REQUEST_SCOPE;
        }
        return PageContext.PAGE_SCOPE;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#release()
     */
    @Override
    public void release() {
        super.release();
        this.scope = null;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
     */
    @Override
    protected int doStartTagInternal() throws Exception {
        // ChangeSoon 
        return 0;
    }
}
