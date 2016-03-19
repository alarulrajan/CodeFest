package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.struts.tiles.DirectStringAttribute;

/**
 * The Class ContentTag.
 */
public class ContentTag extends InsertTag implements BodyTag {
    
    /** The body content. */
    private BodyContent bodyContent;

    /* (non-Javadoc)
     * @see org.apache.struts.taglib.tiles.InsertTag#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        if (PrintLinkTag.isInPrintMode(this.pageContext)) {
            this.definitionName = "tiles:print";
        } else if (this.definitionName == null
                || this.definitionName == "tiles:print") {
            this.definitionName = "tiles:default";
        }
        super.doStartTag();
        return BodyTag.EVAL_BODY_BUFFERED;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTag#doInitBody()
     */
    @Override
    public void doInitBody() throws JspException {
        // empty
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTag#setBodyContent(javax.servlet.jsp.tagext.BodyContent)
     */
    @Override
    public void setBodyContent(final BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.taglib.tiles.InsertTag#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        this.putAttribute("body",
                new DirectStringAttribute(this.bodyContent.getString()));
        return super.doEndTag();
    }

    /* (non-Javadoc)
     * @see org.apache.struts.taglib.tiles.InsertTag#release()
     */
    @Override
    public void release() {
        this.bodyContent = null;
        super.release();
    }
}
