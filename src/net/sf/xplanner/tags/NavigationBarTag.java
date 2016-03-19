package net.sf.xplanner.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;
import net.sf.xplanner.tags.domain.Link;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.taglib.TagUtils;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.IdSearchHelper;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.tags.AccessKeyTransformer;
import com.technoetic.xplanner.tags.DomainContext;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym. Copyright (C) 2009 Maksym Chyrkov This program is free
 *         software: you can redistribute it and/or modify it under the terms of
 *         the GNU General Public License as published by the Free Software
 *         Foundation, either version 3 of the License, or (at your option) any
 *         later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */
public class NavigationBarTag extends DatabaseTagSupport {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4509893685722734469L;
	
	/** The object. */
	private Object object;
	
	/** The inclusive. */
	private boolean inclusive;
	
	/** The back. */
	private boolean back;
	
	/** The oid. */
	private int oid;
	
	/** The type. */
	private String type;
	
	/** The Constant PROJECT_NAVIGATION_LINK_KEY. */
	public static final String PROJECT_NAVIGATION_LINK_KEY = "navigation.project";
	
	/** The Constant ITERATION_NAVIGATION_LINK_KEY. */
	public static final String ITERATION_NAVIGATION_LINK_KEY = "navigation.iteration";
	
	/** The Constant STORY_NAVIGATION_LINK_KEY. */
	public static final String STORY_NAVIGATION_LINK_KEY = "navigation.story";
	
	/** The Constant TASK_NAVIGATION_LINK_KEY. */
	public static final String TASK_NAVIGATION_LINK_KEY = "navigation.task";
	
	/** The Constant FEATURE_NAVIGATION_LINK_KEY. */
	public static final String FEATURE_NAVIGATION_LINK_KEY = "navigation.feature";

	/**
     * Sets the object.
     *
     * @param object
     *            the new object
     */
	public void setObject(final Object object) {
		this.object = object;
	}

	/**
     * Sets the oid.
     *
     * @param oid
     *            the new oid
     */
	public void setOid(final int oid) {
		this.oid = oid;
	}

	/**
     * Gets the oid.
     *
     * @return the oid
     */
	public int getOid() {
		return this.oid;
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
     * Gets the type.
     *
     * @return the type
     */
	public String getType() {
		return this.type;
	}

	/**
     * Sets the back.
     *
     * @param back
     *            the new back
     */
	public void setBack(final boolean back) {
		this.back = back;
	}

	/**
     * Gets the back.
     *
     * @return the back
     */
	public boolean getBack() {
		return this.back;
	}

	/**
     * Sets the inclusive.
     *
     * @param inclusive
     *            the new inclusive
     */
	public void setInclusive(final boolean inclusive) {
		this.inclusive = inclusive;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		try {
			int objectId = this.oid;

			if (objectId == 0
					&& this.pageContext.getRequest().getParameter("oid") != null) {
				objectId = NumberUtils.toInt(this.pageContext.getRequest()
						.getParameter("oid"));
			}

			if (objectId == 0
					&& this.pageContext.getRequest().getParameter("fkey") != null) {
				objectId = NumberUtils.toInt(this.pageContext.getRequest()
						.getParameter("fkey"));
			}

			if (this.object == null && objectId > 0 && this.type != null) {
				this.setObject(objectId);
			}

			final DomainContext domainContext = this.getContext(this.object);
			this.render(domainContext);

		} catch (final JspException e) {
			throw e;
		} catch (final Exception e) {
			throw new JspException(e);
		}

		return Tag.EVAL_PAGE;
	}

	/**
     * Sets the object.
     *
     * @param objectId
     *            the new object
     * @throws Exception
     *             the exception
     */
	private void setObject(final int objectId) throws Exception {
		if (StringUtils.isBlank(this.type)) {
			this.setObject(((IdSearchHelper) this.getRequestContext()
					.getWebApplicationContext().getBean("idSearchHelper"))
					.search(objectId));
		} else {
			this.setObject(this.getObject(objectId, this.type));
		}
		DomainContext domainContext = DomainContext.get(this.pageContext
				.getRequest());
		if (domainContext == null) {
			domainContext = new DomainContext();
			domainContext.populate(this.object);
			domainContext.save(this.pageContext.getRequest());
		}
	}

	/**
     * Gets the context.
     *
     * @param subject
     *            the subject
     * @return the context
     * @throws Exception
     *             the exception
     */
	private DomainContext getContext(final Object subject) throws Exception {
		DomainContext domainContext = DomainContext.get(this.pageContext
				.getRequest());
		if (domainContext == null) {
			domainContext = new DomainContext();
			domainContext.populate(subject);
			domainContext.save(this.pageContext.getRequest());
		}
		return domainContext;
	}

	/**
     * Gets the object.
     *
     * @param objectId
     *            the object id
     * @param typeName
     *            the type name
     * @return the object
     * @throws Exception
     *             the exception
     */
	private Object getObject(final int objectId, final String typeName)
			throws Exception {
		final Session session = this.getSession();
		return session.load(Class.forName(typeName), new Integer(objectId));
	}

	/**
     * Render.
     *
     * @param context
     *            the context
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JspException
     *             the jsp exception
     */
	private void render(final DomainContext context) throws IOException,
			JspException {
		final List<Link> links = new ArrayList<Link>();
		links.add(this
				.createLink("topLink", "navigation.top", "projects", null));

		if (this.object != null) {
			final Class objectClass = this.object.getClass();
			if (context.getProjectName() != null
					&& (objectClass != Project.class || this.inclusive)) {
				links.add(this.createLink(context.getProjectName(), "project",
						context.getProjectId()));
				if (context.getIterationName() != null
						&& (objectClass != Iteration.class || this.inclusive)) {
					links.add(this.createLink(context.getIterationName(),
							"iteration", context.getIterationId()));
					if (context.getStoryName() != null
							&& (objectClass != UserStory.class || this.inclusive)) {
						links.add(this.createLink(context.getStoryName(),
								"userstory", context.getStoryId()));
						if (context.getTaskName() != null
								&& (objectClass != Task.class || this.inclusive)) {
							links.add(this.createLink(context.getTaskName(),
									"task", context.getTaskId()));
						}
						if (context.getFeatureName() != null
								&& (objectClass != Feature.class || this.inclusive)) {
							links.add(this.createLink(context.getFeatureName(),
									"feature", context.getFeatureId()));
						}
					}
				}
			}
		}

		if (this.back) {
			final List<Link> backLinks = new ArrayList<Link>();
			if (this.pageContext.getRequest().getParameter("returnto") != null) {
				backLinks
						.add(this.createLink("backLink", "navigation.back",
								null, null, this.pageContext.getRequest()
										.getParameter("returnto")));
			} else {
				backLinks.add(this.createLink("backLink", "navigation.back",
						"javascript:history.back()"));
			}
			this.pageContext.setAttribute("backNavigation", backLinks);
		}
		this.pageContext.setAttribute("navigation", links);
	}

	/**
     * Gets the message.
     *
     * @param key
     *            the key
     * @return the message
     * @throws JspException
     *             the jsp exception
     */
	protected String getMessage(final String key) throws JspException {
		return TagUtils.getInstance()
				.message(this.pageContext, null, null, key);
	}

	/**
     * Creates the link.
     *
     * @param titleKey
     *            the title key
     * @param objectType
     *            the object type
     * @param objectId
     *            the object id
     * @return the link
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JspException
     *             the jsp exception
     */
	private Link createLink(final String titleKey, final String objectType,
			final int objectId) throws IOException, JspException {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("oid", new Integer(objectId));
		return this.createLink(null, titleKey, objectType, params);
	}

	/**
     * Creates the link.
     *
     * @param linkId
     *            the link id
     * @param titleKey
     *            the title key
     * @param objectType
     *            the object type
     * @param params
     *            the params
     * @return the link
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JspException
     *             the jsp exception
     */
	private Link createLink(final String linkId, final String titleKey,
			final String objectType, final Map<String, Object> params)
			throws IOException, JspException {
		return this.createLink(linkId, titleKey, objectType, params, "");
	}

	/**
     * Creates the link.
     *
     * @param linkId
     *            the link id
     * @param titleKey
     *            the title key
     * @param objectType
     *            the object type
     * @param params
     *            the params
     * @param urlSuffix
     *            the url suffix
     * @return the link
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JspException
     *             the jsp exception
     */
	private Link createLink(final String linkId, final String titleKey,
			final String objectType, final Map<String, Object> params,
			final String urlSuffix) throws IOException, JspException {
		String page = "";
		if (objectType != null) {
			page = "/do/view/" + objectType;
		}

		final String url = TagUtils.getInstance().computeURL(this.pageContext,
				null, null, page, null, null, params, null, false)
				+ urlSuffix;
		if ("projects".equals(objectType)) {
			return this.createLink(linkId, titleKey, url);
		} else {
			return this.createLinkToContent(linkId, titleKey, url);
		}
	}

	/**
     * Creates the link to content.
     *
     * @param linkId
     *            the link id
     * @param title
     *            the title
     * @param url
     *            the url
     * @return the link
     */
	private Link createLinkToContent(final String linkId, final String title,
			final String url) {
		final Link link = new Link();
		link.setId(linkId);
		link.setUrl(url);
		link.setText(title);
		return link;
	}

	/**
     * Creates the link.
     *
     * @param linkId
     *            the link id
     * @param titleKey
     *            the title key
     * @param url
     *            the url
     * @return the link
     * @throws JspException
     *             the jsp exception
     */
	private Link createLink(final String linkId, final String titleKey,
			final String url) throws JspException {
		final Link link = new Link();
		link.setUrl(url);
		link.setId(linkId);
		final String title = this.getMessage(titleKey);
		link.setAccessKey(AccessKeyTransformer.getHtml(title));
		link.setText(AccessKeyTransformer.removeMnemonicMarkers(title));
		return link;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	@Override
	public void release() {
		this.object = null;
		this.inclusive = Boolean.FALSE;
		this.back = Boolean.FALSE;
		this.oid = 0;
		this.type = null;
		super.release();
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
	 */
	@Override
	protected int doStartTagInternal() throws Exception {
		return Tag.SKIP_BODY;
	}

}
