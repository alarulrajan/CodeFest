package com.technoetic.xplanner.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

public class ProgressBarHtmlTag extends TagSupport implements ProgressBarTag {
	private static final long serialVersionUID = 9020272510102623317L;
	private double actual;
	private double estimate;
	private boolean complete = false;
	private String width;
	private int height;

	@Override
	public void setActual(final double actual) {
		this.actual = actual;
	}

	@Override
	public void setEstimate(final double estimate) {
		this.estimate = estimate;
	}

	@Override
	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	public void setWidth(final int width) {
		this.width = String.valueOf(width);
	}

	@Override
	public void setWidth(final String width) {
		this.width = width;
	}

	@Override
	public void setHeight(final int height) {
		this.height = height;
	}

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