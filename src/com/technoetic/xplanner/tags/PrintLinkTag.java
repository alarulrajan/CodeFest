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

public class PrintLinkTag extends LinkTag {
	public static final String PRINT_PARAMETER_NAME = "print";

	public static boolean isInPrintMode(final PageContext pageContext) {
		return pageContext.getRequest().getParameter(
				PrintLinkTag.PRINT_PARAMETER_NAME) != null;
	}

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