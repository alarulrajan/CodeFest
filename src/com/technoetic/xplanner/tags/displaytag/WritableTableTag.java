package com.technoetic.xplanner.tags.displaytag;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.model.Row;
import org.displaytag.properties.TableProperties;
import org.displaytag.tags.TableTag;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.AuthorizationHelper;
import com.technoetic.xplanner.tags.WritableTag;

/**
 * The Class WritableTableTag.
 */
public class WritableTableTag extends TableTag implements WritableTag {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(WritableTableTag.class);
	
	/** The Constant IS_AUTHORIZED_FOR_ANY_PARAM_NAME. */
	static final String IS_AUTHORIZED_FOR_ANY_PARAM_NAME = "isAuthorizedForAny";
	static {
		TableProperties.setUserProperties(new XPlannerProperties().get());
	}
	
	/** The permissions. */
	private String permissions;
	
	/** The whole collection. */
	private Collection wholeCollection;

	/** Optional decorator for row objects. */
	private RowDecorator rowDecorator;

	/**
     * Instantiates a new writable table tag.
     */
	public WritableTableTag() {
		super();
		// todo uncomment to enable export links as default
		// this.setExport(true);
	}

	/**
     * Sets the row decorator.
     *
     * @param rowDecorator
     *            the new row decorator
     */
	public void setRowDecorator(Object rowDecorator) {
		if (rowDecorator instanceof String) {
			try {
				rowDecorator = Class.forName((String) rowDecorator)
						.newInstance();
			} catch (final Exception e) {
				WritableTableTag.log.error("Exception ", e);
			}
		}
		if (rowDecorator instanceof RowDecorator) {
			this.rowDecorator = (RowDecorator) rowDecorator;
		}
	}

	/* (non-Javadoc)
	 * @see org.displaytag.tags.TableTag#setRowObjectForCellValues(java.lang.Object, int)
	 */
	@Override
	public Row setRowObjectForCellValues(final Object iteratedObject,
			final int rowNumber) {
		return new com.technoetic.xplanner.tags.displaytag.Row(iteratedObject,
				rowNumber, this.rowDecorator);
	}

	/**
     * Gets the permissions.
     *
     * @return the permissions
     */
	public String getPermissions() {
		return this.permissions;
	}

	/**
     * Sets the permissions.
     *
     * @param permissions
     *            the new permissions
     */
	public void setPermissions(final String permissions) {
		this.permissions = permissions;
	}

	/**
     * Sets the whole collection.
     *
     * @param wholeCollection
     *            the new whole collection
     */
	public void setWholeCollection(final Collection wholeCollection) {
		this.wholeCollection = wholeCollection;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.tags.WritableTag#isWritable()
	 */
	@Override
	public boolean isWritable() throws Exception {
		Boolean isAuthorized = (Boolean) this.pageContext
				.getAttribute(WritableTableTag.IS_AUTHORIZED_FOR_ANY_PARAM_NAME);
		if (isAuthorized == null) {
			isAuthorized = new Boolean(this.hasPermissionToAny());
			this.pageContext.setAttribute(
					WritableTableTag.IS_AUTHORIZED_FOR_ANY_PARAM_NAME,
					isAuthorized);
		}
		return isAuthorized.booleanValue();
	}

	/**
     * Checks for permission to any.
     *
     * @return true, if successful
     * @throws AuthenticationException
     *             the authentication exception
     */
	private boolean hasPermissionToAny() throws AuthenticationException {
		if (StringUtils.isEmpty(this.permissions)) {
			return true;
		}
		final String[] permissionArray = this.permissions.split(",");
		final boolean isAuthorized = AuthorizationHelper.hasPermissionToAny(
				permissionArray, this.wholeCollection,
				this.pageContext.getRequest());
		return isAuthorized;
	}
}
