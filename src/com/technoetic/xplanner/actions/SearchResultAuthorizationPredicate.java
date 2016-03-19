package com.technoetic.xplanner.actions;

import org.apache.commons.collections.Predicate;

import com.technoetic.xplanner.domain.SearchResult;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.tags.DomainContext;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Dec 14, 2004 Time: 2:02:13 PM
 */
public class SearchResultAuthorizationPredicate implements Predicate {
	
	/** The remote user id. */
	private final int remoteUserId;

	/**
     * Instantiates a new search result authorization predicate.
     *
     * @param remoteUserId
     *            the remote user id
     */
	public SearchResultAuthorizationPredicate(final int remoteUserId) {
		this.remoteUserId = remoteUserId;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(final Object o) {
		try {
			return this.isResultReadableByUser((SearchResult) o,
					this.remoteUserId);
		} catch (final AuthenticationException e) {
			return false;
		}
	}

	// debt This code looks a bit like some code in
	/**
     * Checks if is result readable by user.
     *
     * @param searchResult
     *            the search result
     * @param remoteUserId
     *            the remote user id
     * @return true, if is result readable by user
     * @throws AuthenticationException
     *             the authentication exception
     */
	// RepositorySecurityAdapter.checkAuthorization
	protected boolean isResultReadableByUser(final SearchResult searchResult,
			final int remoteUserId) throws AuthenticationException {
		final Object object = searchResult.getMatchingObject();
		boolean result;
		final DomainContext context = new DomainContext();

		try {
			context.populate(object);
			result = SystemAuthorizer.get().hasPermission(
					context.getProjectId(),
					SecurityHelper.getRemoteUserId(ThreadServletRequest.get()),
					object, "read");
		} catch (final Exception e) {
			throw new AuthenticationException(e);
		}
		return result;
	}

}
