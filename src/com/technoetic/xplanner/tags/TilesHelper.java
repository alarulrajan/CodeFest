package com.technoetic.xplanner.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.tiles.ComponentConstants;
import org.apache.struts.tiles.ComponentContext;

/**
 * The Class TilesHelper.
 */
public class TilesHelper {
	
	/**
     * Gets the attribute.
     *
     * @param name
     *            the name
     * @param pageContext
     *            the page context
     * @return the attribute
     * @throws JspException
     *             the jsp exception
     */
	public static Object getAttribute(final String name,
			final PageContext pageContext) throws JspException {
		final ComponentContext compContext = (ComponentContext) pageContext
				.getAttribute(ComponentConstants.COMPONENT_CONTEXT,
						PageContext.REQUEST_SCOPE);

		if (compContext == null) {
			throw new JspException(
					"Error - tag.getAttribute : component context is not defined. Check tag syntax");
		}

		return compContext.getAttribute(name);
	}
}
