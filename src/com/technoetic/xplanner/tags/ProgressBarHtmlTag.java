package com.technoetic.xplanner.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * The Class ProgressBarHtmlTag.
 */
public class ProgressBarHtmlTag extends TagSupport implements ProgressBarTag {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9020272510102623317L;
    
    /** The actual. */
    private double actual;
    
    /** The estimate. */
    private double estimate;
    
    /** The complete. */
    private boolean complete = false;
    
    /** The width. */
    private String width;
    
    /** The height. */
    private int height;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setActual(double)
     */
    @Override
    public void setActual(final double actual) {
        this.actual = actual;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setEstimate(double)
     */
    @Override
    public void setEstimate(final double estimate) {
        this.estimate = estimate;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setComplete(boolean)
     */
    @Override
    public void setComplete(final boolean complete) {
        this.complete = complete;
    }

    /**
     * Sets the width.
     *
     * @param width
     *            the new width
     */
    public void setWidth(final int width) {
        this.width = String.valueOf(width);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setWidth(java.lang.String)
     */
    @Override
    public void setWidth(final String width) {
        this.width = width;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setHeight(int)
     */
    @Override
    public void setHeight(final int height) {
        this.height = height;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            final double total = Math.max(this.actual, this.estimate);
            int workedPercent = 0;
            if (total == 0.0) {
                workedPercent = this.complete ? 100 : 0;
            } else {
                workedPercent = (int) Math.round(Math.min(this.actual,
                        this.estimate) / total * 100);
            }
            final int unworkedPercent = 100 - workedPercent;

            this.pageContext.getOut().println(
                    "<table cellspacing=\"0\" cellpadding=\"0\" ");
            this.pageContext.getOut().println(" class=\"progressbar\"");
            if (StringUtils.isNotBlank(this.width)) {
                this.pageContext.getOut().println(
                        " width=\"" + this.width + "\"");
            }
            if (this.height > 0) {
                // pageContext.getOut().println(" height=\"" + height + "\"");
            }
            this.pageContext.getOut().println(">");
            this.pageContext.getOut().println("<tr>");
            if (workedPercent > 0) {
                this.pageContext.getOut().println("<td class=\"");
                this.pageContext.getOut().print(
                        (this.complete ? "progressbar_completed"
                                : "progressbar_uncompleted")
                                + "\" width=\""
                                + workedPercent + "%\" >");
                this.pageContext.getOut().println("&nbsp;</td>");
            }
            if (unworkedPercent > 0) {
                this.pageContext.getOut().println("<td");
                this.pageContext
                        .getOut()
                        .print(" class=\""
                                + (this.actual > this.estimate ? "progressbar_exceeded"
                                        : "progressbar_unworked")
                                + "\" width=\"" + unworkedPercent + "%\" >");
                this.pageContext.getOut().println("&nbsp;</td>");
            }
            this.pageContext.getOut().println("</tr>");
            this.pageContext.getOut().println("</table>");
        } catch (final IOException ex) {
            throw new JspException("Caught IOException: " + ex.getMessage());
        } catch (final NumberFormatException ex) {
            throw new JspException("Caught NumberFormatException: "
                    + ex.getMessage());
        }
        return Tag.EVAL_PAGE;
    }
}