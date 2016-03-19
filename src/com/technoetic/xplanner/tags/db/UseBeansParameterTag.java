package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;

/**
 * The Class UseBeansParameterTag.
 */
public class UseBeansParameterTag extends TagSupport {
    
    /** The Constant TYPE_RESOLVER. */
    private static final TypeResolver TYPE_RESOLVER = new TypeResolver();
    
    /** The name. */
    private String name;
    
    /** The value. */
    private Object value;
    
    /** The type. */
    private Type type;

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(final Object value) {
        this.value = value;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(final String type) {
        this.type = UseBeansParameterTag.TYPE_RESOLVER.basic(type);
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

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        final Tag parent = this.getParent();
        if (parent instanceof UseBeansTag) {
            if (this.name == null) {
                ((UseBeansTag) parent).addParameter(this.value, this.type);
            } else {
                ((UseBeansTag) parent).addParameter(this.name, this.value,
                        this.type);
            }
        }
        return Tag.EVAL_PAGE;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#release()
     */
    @Override
    public void release() {
        this.name = null;
        this.value = null;
        this.type = null;
        super.release();
    }
}
