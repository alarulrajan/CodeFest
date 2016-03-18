package com.technoetic.xplanner.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.util.RequestUtils;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

@Deprecated
public class NavigationBarTag extends DatabaseTagSupport {
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
			Object object = this.object;
			int objectId = this.oid;

			if (objectId == 0
					&& this.pageContext.getRequest().getParameter("oid") != null) {
				objectId = Integer.parseInt(this.pageContext.getRequest()
						.getParameter("oid"));
			}

			if (objectId == 0
					&& this.pageContext.getRequest().getParameter("fkey") != null) {
				objectId = Integer.parseInt(this.pageContext.getRequest()
						.getParameter("fkey"));
			}

			if (object == null && objectId > 0 && this.type != null) {
				object = this.getObject(objectId, this.type);
			}

			final DomainContext domainContext = this.getContext(object);
			this.render(object, domainContext, this.inclusive);

		} catch (final JspException e) {
			throw e;
		} catch (final Exception e) {
			throw new JspException(e);
		}

		return Tag.EVAL_PAGE;
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

	private Object getObject(final int id, final String type) throws Exception {
		final Session session = this.getSession();
		return session.load(Class.forName(type), new Integer(id));
	}

	private void render(final Object object, final DomainContext context,
			final boolean inclusive) throws IOException, JspException {

		final JspWriter out = this.pageContext.getOut();
		out.print("<table bgcolor='#CCCCCC' width='100%' ");
		out.print("class='navbar' border='0' cellpadding='0'><tr><td>");
		this.renderLink(out, "navigation.top", "projects", null);

		if (object != null) {
			final Class objectClass = object.getClass();
			if (context.getProjectName() != null
					&& (objectClass != Project.class || inclusive)) {
				this.renderLink(out,
						NavigationBarTag.PROJECT_NAVIGATION_LINK_KEY,
						"project", context.getProjectId());
				if (context.getIterationName() != null
						&& (objectClass != Iteration.class || inclusive)) {
					this.renderLink(out,
							NavigationBarTag.ITERATION_NAVIGATION_LINK_KEY,
							"iteration", context.getIterationId());
					if (context.getStoryName() != null
							&& (objectClass != UserStory.class || inclusive)) {
						this.renderLink(out,
								NavigationBarTag.STORY_NAVIGATION_LINK_KEY,
								"userstory", context.getStoryId());
						if (context.getTaskName() != null
								&& (objectClass != Task.class || inclusive)) {
							this.renderLink(out,
									NavigationBarTag.TASK_NAVIGATION_LINK_KEY,
									"task", context.getTaskId());
						}
						if (context.getFeatureName() != null
								&& (objectClass != Feature.class || inclusive)) {
							this.renderLink(
									out,
									NavigationBarTag.FEATURE_NAVIGATION_LINK_KEY,
									"feature", context.getFeatureId());
						}
					}
				}
			}
		}

		if (this.back) {
			out.print(" | ");
			if (this.pageContext.getRequest().getParameter("returnto") != null) {
				this.renderLink(out, "navigation.back", null, null,
						this.pageContext.getRequest().getParameter("returnto"));
			} else {
				this.renderLink(out, "navigation.back",
						"javascript:history.back()");
			}
		}
		out.print("</td>");

		out.print("<td align='right'>");

		this.generateRightSideContent(out, context);

		out.print("</td></tr></table>");
	}

	private void generateRightSideContent(final JspWriter out,
			final DomainContext context) throws IOException, JspException {
		final XPlannerProperties xPlannerProperties = new XPlannerProperties();
		final boolean isGlobalContentSearchScope = Boolean.valueOf(
				xPlannerProperties
						.getProperty("search.content.globalScopeEnable"))
				.booleanValue();
		out.print("<table><tr>");
		if (isGlobalContentSearchScope || context.getProjectId() != 0) {
			out.print("<td>");
			this.generateContentSearchForm(out, context);
			out.print("</td>");
		}
		out.print("<td>");
		this.generateIdSearchForm(out);
		out.print("</td><td>");

		if (context.getProjectId() != 0) {
			final HashMap params = new HashMap();
			params.put("projectId", new Integer(context.getProjectId()));
			this.renderLink(out, "navigation.integrations", "integrations",
					params);
			out.print(" | ");
			out.print("</td><td>");
		}

		final int myId = this.getCurrentUserId();
		if (myId != 0) {
			final HashMap params = new HashMap();
			params.put("oid", new Integer(myId));
			this.renderLink(out, "navigation.me", "person", params);
		} else {
			this.renderLink(out, "navigation.people", "people", null);
		}
		out.print("</td></tr></table>");
	}

	private void generateIdSearchForm(final JspWriter out) throws IOException,
			JspException {
		out.print("<form id='idSearchForm' class='formnavbar' action='"
				+ RequestUtils.computeURL(this.pageContext, "search/id", null,
						null, null, null, null, false) + "'>");
		out.print("<span class='idsearch'>");
		out.print(this.getMessage("idsearch.label"));
		out.print(" <input type='text' size='5' name='searchedId' /> <input type='submit' name='action' value='"
				+ this.getMessage("idsearch.button.label") + "'/> ");
		out.print("</span> | </form>");
	}

	private void generateContentSearchForm(final JspWriter out,
			final DomainContext context) throws IOException, JspException {
		out.print("<form id='search' class='formnavbar' action='"
				+ RequestUtils.computeURL(this.pageContext, "search/content",
						null, null, null, null, null, false) + "'>");
		out.print("<span class='idsearch'>");
		out.print(this.getMessage("contentsearch.label"));
		out.print(" <input type='text' size='5' name='searchedContent' /> <input type='submit' name='action' value='"
				+ this.getMessage("contentsearch.button.label") + "'/> ");
		out.print(" <input type='hidden' name='restrictToProjectId' value='"
				+ context.getProjectId() + "'/>");
		out.print("</span> | </form>");
	}

	private int getCurrentUserId() throws JspException {
		if (SecurityHelper
				.isUserAuthenticated((HttpServletRequest) this.pageContext
						.getRequest())) {
			try {
				final PersonPrincipal me = (PersonPrincipal) SecurityHelper
						.getUserPrincipal((HttpServletRequest) this.pageContext
								.getRequest());
				return me.getPerson().getId();
			} catch (final AuthenticationException e) {
				throw new JspException(e);
			}
		}
		return 0;
	}

	// AccessKeyTransformer.removeMnemonicMarkers(
	// AccessKeyTransformer.getHtml(message)

	protected String getMessage(final String key) throws JspException {
		return RequestUtils.message(this.pageContext, null, null, key);
	}

	private void renderLink(final JspWriter out, final String titleKey,
			final String type, final int id) throws IOException, JspException {
		final HashMap params = new HashMap();
		params.put("oid", new Integer(id));
		out.print(" | ");
		this.renderLink(out, titleKey, type, params);
	}

	private void renderLink(final JspWriter out, final String titleKey,
			final String type, final Map params) throws IOException,
			JspException {
		this.renderLink(out, titleKey, type, params, "");
	}

	private void renderLink(final JspWriter out, final String titleKey,
			final String type, final Map params, final String urlSuffix)
			throws IOException, JspException {
		String page = "";
		if (type != null) {
			page = "/do/view/" + type;
		}
		final String url = RequestUtils.computeURL(this.pageContext, null,
				null, page, null, params, null, false) + urlSuffix;
		this.renderLink(out, titleKey, url);
	}

	private void renderLink(final JspWriter out, final String titleKey,
			final String url) throws IOException, JspException {
		final String title = this.getMessage(titleKey);
		out.print("<a href='");
		out.print(url);
		out.print("'" + AccessKeyTransformer.getHtml(title) + ">");
		out.print(AccessKeyTransformer.removeMnemonicMarkers(title));
		out.print("</a>");
	}

	@Override
	public void release() {
		this.object = null;
		this.inclusive = false;
		this.back = false;
		this.oid = 0;
		this.type = null;
		super.release();
	}

	@Override
	protected int doStartTagInternal() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
