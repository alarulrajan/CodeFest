package com.technoetic.xplanner.tags;

//
// This is based on contributed code from the Struts website.
// See: http://husted.com/struts/resources/linkParam.htm
//

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The Class LinkTag.
 */
public class LinkTag extends org.apache.struts.taglib.html.LinkTag {
	
	/** The log. */
	private final Logger log = Logger.getLogger(this.getClass());
	// ------------------------------------------------------ Instance Vartables
	/** The full HREF URL. */
	private StringBuffer hrefURL = new StringBuffer();

	/** Tag ID. */
	private String id;

	/** Foreign Key for returnto link. */
	private int fkey;

	/**
	 * Flag for determining if projectId parameter is included.
	 */
	private boolean includeProjectId = true;

	// DEBT: rename useReturnto to useReturnTo. Eventually will be returnToUrl
	/** The use returnto. */
	// when Domain object actions/views refactoring is complete
	private boolean useReturnto = false;

	/** The remove quotes. */
	private boolean removeQuotes = false;

	// ------------------------------------------------------------- Properties

	// --------------------------------------------------------- Public Methods

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	 */
	@Override
	public void setId(final String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
     * Sets the use returnto.
     *
     * @param useReturnto
     *            the new use returnto
     */
	public void setUseReturnto(final boolean useReturnto) {
		this.useReturnto = useReturnto;
	}

	/**
     * Intialize the hyperlink.
     *
     * @return the int
     * @throws JspException
     *             if a JSP exception has occurred
     */
	@Override
	public int doStartTag() throws JspException {

		// Special case for name anchors
		if (this.linkName != null) {
			final StringBuffer results = new StringBuffer("<a name=\"");
			results.append(this.linkName);
			results.append("\">");
			return BodyTag.EVAL_BODY_BUFFERED;
		}

		// Generate the hyperlink URL
		Map params = RequestUtils.computeParameters(this.pageContext,
				this.paramId, this.paramName, this.paramProperty,
				this.paramScope, this.name, this.property, this.scope,
				this.transaction);
		params = this.addNavigationParameters(params);
		String url = null;
		try {
			url = RequestUtils.computeURL(this.pageContext, this.forward,
					this.href, this.page, params, this.anchor, false);
		} catch (final MalformedURLException e) {
			RequestUtils.saveException(this.pageContext, e);
			throw new JspException(LinkTag.messages.getMessage("rewrite.url",
					e.toString()));
		}

		// Generate the opening anchor element
		this.hrefURL = new StringBuffer("<a href=\"");
		this.hrefURL.append(url);

		if (this.log.isDebugEnabled()) {
			this.log.debug("hrefURL = '" + this.hrefURL.toString());
		}

		// Evaluate the body of this tag
		this.text = null;
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	/**
     * Adds the navigation parameters.
     *
     * @param parameters
     *            the parameters
     * @return the map
     * @throws JspTagException
     *             the jsp tag exception
     */
	private Map addNavigationParameters(Map parameters) throws JspTagException {
		final HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		if (parameters == null) {
			parameters = new HashMap();
		}
		String returnToUri = null;
		if (this.useReturnto) {
			returnToUri = request.getParameter("returnto");
			if (returnToUri == null) {
				returnToUri = "/do/view/projects";
			}
		} else {
			final ActionMapping mapping = (ActionMapping) request
					.getAttribute(Globals.MAPPING_KEY);
			if (mapping == null) {
				// throw new
				// JspTagException("can't find ActionMapping in request");
				returnToUri = ((ServletRequestAttributes) request
						.getAttribute("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES"))
						.getRequest().getRequestURI();
			} else {
				returnToUri = "/do" + mapping.getPath();
				final String oid = request.getParameter("oid");
				if (oid != null) {
					returnToUri += "?oid=" + oid;
					parameters.put("fkey",
							this.fkey == 0 ? oid : Integer.toString(this.fkey));
				}
				if (this.includeProjectId) {
					final DomainContext context = DomainContext.get(request);
					if (context != null) {
						int projectId = context.getProjectId();
						final String projectIdParam = request
								.getParameter("projectId");
						if (projectId == 0 && projectIdParam != null) {
							projectId = Integer.parseInt(projectIdParam);
						}
						parameters.put("projectId", new Integer(projectId));
					}
				}
			}
		}
		parameters.put("returnto", returnToUri);
		return parameters;
	}

	/**
     * Add a new parameter to the request.
     *
     * @param name
     *            the name of the request parameter
     * @param value
     *            the value of the request parameter
     */
	public void addRequestParameter(final String name, final String value) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("Adding '" + name + "' with value '" + value + "'");
		}

		final boolean question = this.hrefURL.toString().indexOf('?') >= 0;

		if (question) { // There are request parameter already
			this.hrefURL.append('&');
		} else {
			this.hrefURL.append('?');
		}

		this.hrefURL.append(name);
		this.hrefURL.append('=');
		try {
			this.hrefURL.append(URLEncoder.encode(value, "UTF-8"));
		} catch (final UnsupportedEncodingException ex) {
		}

		if (this.log.isDebugEnabled()) {
			this.log.debug("hrefURL = '" + this.hrefURL.toString() + "'");
		}
	}

	/**
     * Render the href reference.
     *
     * @return the int
     * @throws JspException
     *             if a JSP exception has occurred
     */
	@Override
	public int doEndTag() throws JspException {

		this.hrefURL.append("\"");
		if (this.target != null) {
			this.hrefURL.append(" target=\"");
			this.hrefURL.append(this.target);
			this.hrefURL.append("\"");
		}

		if (this.id != null) {
			this.hrefURL.append(" id=\"");
			this.hrefURL.append(this.id);
			this.hrefURL.append("\"");
		}

		this.hrefURL.append(AccessKeyTransformer.getHtml(this.text));
		this.hrefURL.append(this.prepareStyles());
		this.hrefURL.append(this.prepareEventHandlers());
		this.hrefURL.append(">");

		this.hrefURL.append(AccessKeyTransformer
				.removeMnemonicMarkers(this.text));

		this.hrefURL.append("</a>");

		if (this.log.isDebugEnabled()) {
			this.log.debug("Total request is = '" + this.hrefURL.toString()
					+ "'");
		}

		// Print this element to our output writer
		String url = this.hrefURL.toString();
		if (this.removeQuotes) {
			url = url.replaceAll("\"", "");
		}
		ResponseUtils.write(this.pageContext, url);
		return Tag.EVAL_PAGE;
	}

	/**
	 * Release any acquired resources.
	 */
	@Override
	public void release() {

		super.release();
		this.forward = null;
		this.href = null;
		this.name = null;
		this.property = null;
		this.target = null;
		this.fkey = 0;
		this.includeProjectId = false;
		this.removeQuotes = false;
	}

	// ----------------------------------------------------- Protected Methods

	/**
     * Return the specified hyperlink, modified as necessary with optional
     * request parameters.
     *
     * @return the string
     * @throws JspException
     *             if an error occurs preparing the hyperlink
     */
	protected String hyperlink() throws JspException {

		String href = this.href;

		// If "forward" was specified, compute the "href" to forward to
		if (this.forward != null) {
			final ModuleConfig moduleConfig = TagUtils.getInstance()
					.getModuleConfig(this.pageContext);

			if (moduleConfig == null) {
				throw new JspException(
						LinkTag.messages.getMessage("linkTag.forwards"));
			}
			final ForwardConfig forward = moduleConfig
					.findForwardConfig(this.forward);
			;
			if (forward == null) {
				throw new JspException(
						LinkTag.messages.getMessage("linkTag.forward"));
			}
			final HttpServletRequest request = (HttpServletRequest) this.pageContext
					.getRequest();
			href = request.getContextPath() + forward.getPath();
		}

		// Just return the "href" attribute if there is no bean to look up
		if (this.property != null && this.name == null) {
			throw new JspException(LinkTag.messages.getMessage("getter.name"));
		}
		if (this.name == null) {
			return href;
		}

		// Look up the map we will be using
		final Object bean = this.pageContext.findAttribute(this.name);
		if (bean == null) {
			throw new JspException(LinkTag.messages.getMessage("getter.bean",
					this.name));
		}
		Map map = null;
		if (this.property == null) {
			try {
				map = (Map) bean;
			} catch (final ClassCastException e) {
				throw new JspException(
						LinkTag.messages.getMessage("linkTag.type"));
			}
		} else {
			try {
				map = (Map) PropertyUtils.getProperty(bean, this.property);
				if (map == null) {
					throw new JspException(LinkTag.messages.getMessage(
							"getter.property", this.property));
				}
			} catch (final IllegalAccessException e) {
				throw new JspException(LinkTag.messages.getMessage(
						"getter.access", this.property, this.name));
			} catch (final InvocationTargetException e) {
				final Throwable t = e.getTargetException();
				throw new JspException(LinkTag.messages.getMessage(
						"getter.result", this.property, t.toString()));
			} catch (final ClassCastException e) {
				throw new JspException(
						LinkTag.messages.getMessage("linkTag.type"));
			} catch (final NoSuchMethodException e) {
				throw new JspException(LinkTag.messages.getMessage(
						"getter.method", this.property, this.name));
			}
		}

		// Append the required query parameters
		final StringBuffer sb = new StringBuffer(href);
		boolean question = href.indexOf("?") >= 0;
		final Iterator keys = map.keySet().iterator();
		while (keys.hasNext()) {
			final String key = (String) keys.next();
			final Object value = map.get(key);
			if (value instanceof String[]) {
				final String values[] = (String[]) value;
				for (int i = 0; i < values.length; i++) {
					if (question) {
						sb.append('&');
					} else {
						sb.append('?');
						question = true;
					}
					sb.append(key);
					sb.append('=');
					try {
						sb.append(URLEncoder.encode(values[i], "UTF-8"));
					} catch (final UnsupportedEncodingException ex) {
					}
				}
			} else {
				if (question) {
					sb.append('&');
				} else {
					sb.append('?');
					question = true;
				}
				sb.append(key);
				sb.append('=');
				try {
					sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
				} catch (final UnsupportedEncodingException ex) {
				}
			}
		}

		// Return the final result
		return sb.toString();

	}

	/**
     * Gets the fkey.
     *
     * @return the fkey
     */
	public int getFkey() {
		return this.fkey;
	}

	/**
     * Sets the fkey.
     *
     * @param fkey
     *            the new fkey
     */
	public void setFkey(final int fkey) {
		this.fkey = fkey;
	}

	/**
     * Checks if is include project id.
     *
     * @return the string
     */
	public String isIncludeProjectId() {
		return new Boolean(this.includeProjectId).toString();
	}

	/**
     * Sets the include project id.
     *
     * @param includeProjectId
     *            the new include project id
     */
	public void setIncludeProjectId(final String includeProjectId) {
		this.includeProjectId = new Boolean(includeProjectId).booleanValue();
	}

	/**
     * Sets the removes the quotes.
     *
     * @param removeQuotes
     *            the new removes the quotes
     */
	public void setRemoveQuotes(final boolean removeQuotes) {
		this.removeQuotes = removeQuotes;
	}
}