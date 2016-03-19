/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 28, 2004
 * Time: 9:58:41 PM
 */
package com.technoetic.xplanner.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.displaytag.util.DefaultRequestHelper;
import org.displaytag.util.Href;
import org.displaytag.util.RequestHelper;

/**
 * The Class PrintLinkTag.
 */
public class PrintLinkTag extends LinkTag {
    
    /** The Constant PRINT_PARAMETER_NAME. */
    public static final String PRINT_PARAMETER_NAME = "print";

    /**
     * Checks if is in print mode.
     *
     * @param pageContext
     *            the page context
     * @return true, if is in print mode
     */
    public static boolean isInPrintMode(final PageContext pageContext) {
        return pageContext.getRequest().getParameter(
                PrintLinkTag.PRINT_PARAMETER_NAME) != null;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.LinkTag#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        final RequestHelper helper = new DefaultRequestHelper(
                (HttpServletRequest) this.pageContext.getRequest(),
                (HttpServletResponse) this.pageContext.getResponse());
        final Href basehref = helper.getHref();
        final Href href = new Href("");
        href.setParameterMap(basehref.getParameterMap());
        href.addParameter(PrintLinkTag.PRINT_PARAMETER_NAME, "");
        this.setHref(href.toString());
        return super.doStartTag();
    }
}