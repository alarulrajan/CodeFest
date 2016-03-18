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
 * XplannerPlus, agile planning software
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
 * 
 */
public class NavigationBarTag extends DatabaseTagSupport {
	private static final long serialVersionUID = -4509893685722734469L;
	private Object object;
	private boolean inclusive;
	private boolean back;
	private int oid;
	private String type;
	public static final String PROJECT_NAVIGATION_LINK_KEY = "navigation.project";
	public static final String ITERATION_NAVIGATION_LINK_KEY = "navigation.iteration";
	public static final String STORY_NAVIGATION_LINK_KEY = "navigation.story";
	public static final String TASK_NAVIGATION_LINK_KEY = "navigation.task";
	public static final String FEATURE_NAVIGATION_LINK_KEY = "navigation.feature";

	public void setObject(final Object object) {
		this.object = object;
	}

	public void setOid(final int oid) {
		this.oid = oid;
	}

	public int getOid() {
		return this.oid;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setBack(final boolean back) {
		this.back = back;
	}

	public boolean getBack() {
		return this.back;
	}

	public void setInclusive(final boolean inclusive) {
		this.inclusive = inclusive;
	}

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

	private Object getObject(final int objectId, final String typeName)
			throws Exception {
		final Session session = this.getSession();
		return session.load(Class.forName(typeName), new Integer(objectId));
	}

	/**
	 * @param context
	 * @throws IOException
	 * @throws JspException
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

	protected String getMessage(final String key) throws JspException {
		return TagUtils.getInstance()
				.message(this.pageContext, null, null, key);
	}

	private Link createLink(final String titleKey, final String objectType,
			final int objectId) throws IOException, JspException {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("oid", new Integer(objectId));
		return this.createLink(null, titleKey, objectType, params);
	}

	private Link createLink(final String linkId, final String titleKey,
			final String objectType, final Map<String, Object> params)
			throws IOException, JspException {
		return this.createLink(linkId, titleKey, objectType, params, "");
	}

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
	 * @param titleKey
	 * @param url
	 * @return
	 */
	private Link createLinkToContent(final String linkId, final String title,
			final String url) {
		final Link link = new Link();
		link.setId(linkId);
		link.setUrl(url);
		link.setText(title);
		return link;
	}

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

	@Override
	public void release() {
		this.object = null;
		this.inclusive = Boolean.FALSE;
		this.back = Boolean.FALSE;
		this.oid = 0;
		this.type = null;
		super.release();
	}

	@Override
	protected int doStartTagInternal() throws Exception {
		return Tag.SKIP_BODY;
	}

}
