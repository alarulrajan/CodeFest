package com.technoetic.xplanner.actions;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.DomainObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.ContentSearchHelper;
import com.technoetic.xplanner.db.IdSearchHelper;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * @noinspection UnusedAssignment
 */
public class ContentSearchAction extends AbstractAction {
	public static final String SEARCH_CRITERIA_KEY = "searchedContent";
	private ContentSearchHelper searchHelper;
	private IdSearchHelper idSearchHelper;

	protected static final String RESTRICTED_PROJECT_ID_KEY = "restrictToProjectId";

	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final int remoteUserId = SecurityHelper.getRemoteUserId(request);
		final String searchCriteria = request
				.getParameter(ContentSearchAction.SEARCH_CRITERIA_KEY);
		if (StringUtils.isEmpty(searchCriteria)) {
			request.setAttribute("exception", this.produceException(request));
			return mapping.findForward("error");
		}
		final XPlannerProperties xPlannerProperties = new XPlannerProperties();
		final Boolean isGlobalSearchScope = Boolean.valueOf(xPlannerProperties
				.getProperty("search.content.globalScopeEnable"));
		int projectId = 0;
		if (!isGlobalSearchScope.booleanValue()) {
			projectId = Integer
					.parseInt(request
							.getParameter(ContentSearchAction.RESTRICTED_PROJECT_ID_KEY));
		}
		final List results = this.search(searchCriteria, remoteUserId,
				projectId);
		if (NumberUtils.toInt(searchCriteria) != 0 && projectId == 0) {
			final DomainObject domainObject = this.idSearchHelper
					.search(NumberUtils.toInt(searchCriteria));
			if (domainObject instanceof Nameable) {
				results.add(0, this.searchHelper.convertToSearchResult(
						(Nameable) domainObject, searchCriteria));
			}
		}
		request.setAttribute("searchResults", results);
		request.setAttribute(ContentSearchAction.SEARCH_CRITERIA_KEY,
				searchCriteria);

		return mapping.findForward("success");
	}

	protected List search(final String searchCriteria, final int userId,
			final int restrictedProjectId) throws RepositoryException {
		this.searchHelper.search(searchCriteria, userId, restrictedProjectId);
		return this.searchHelper.getSearchResults();
	}

	private Exception produceException(final HttpServletRequest request) {
		return this.produceException(
				(MessageResources) request.getAttribute(Globals.MESSAGES_KEY),
				request.getLocale(), "missing content");
	}

	private Exception produceException(final MessageResources messageResources,
			final Locale locale, final String message) {
		final String invalidMessage = messageResources.getMessage(locale,
				"contentsearch.invalid_id");
		final String exceptionMessage = invalidMessage
				+ (message != null ? ": " + message : message);
		return new Exception(exceptionMessage);
	}

	public void setContentSearchHelper(final ContentSearchHelper searchHelper) {
		this.searchHelper = searchHelper;
	}

	public void setIdSearchHelper(final IdSearchHelper idSearchHelper) {
		this.idSearchHelper = idSearchHelper;
	}
}
