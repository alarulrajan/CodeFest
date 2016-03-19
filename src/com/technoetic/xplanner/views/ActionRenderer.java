package com.technoetic.xplanner.views;

import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.domain.ActionMapping;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.util.StringUtilities;

/**
 * The Class ActionRenderer.
 */
public class ActionRenderer {
	
	/** The log. */
	private static Logger log = LogUtil.getLogger();
	
	/** The use return to. */
	private boolean useReturnTo;
	
	/** The action. */
	private final ActionMapping action;
	
	/** The object. */
	private final Nameable object;
	
	/** The displayed as icon. */
	private final boolean displayedAsIcon;

	/**
     * Instantiates a new action renderer.
     *
     * @param action
     *            the action
     * @param object
     *            the object
     * @param inParentView
     *            the in parent view
     * @param displayAsIcon
     *            the display as icon
     * @throws Exception
     *             the exception
     */
	public ActionRenderer(final ActionMapping action, final Nameable object,
			final boolean inParentView, final boolean displayAsIcon)
			throws Exception {
		this.action = action;
		this.object = object;
		this.displayedAsIcon = displayAsIcon;
		if (inParentView) {
			this.useReturnTo = false;
		} else {
			this.useReturnTo = action.isBackToParent();
		}
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
	public String getName() {
		return this.action.getName();
	}

	/**
     * Gets the domain type.
     *
     * @return the domain type
     */
	public String getDomainType() {
		return this.action.getDomainType();
	}

	/**
     * Gets the target page.
     *
     * @return the target page
     */
	public String getTargetPage() {
		return this.action.getTargetPage();
	}

	/**
     * Gets the icon path.
     *
     * @return the icon path
     */
	public String getIconPath() {
		return this.action.getIconPath();
	}

	/**
     * Use return to.
     *
     * @return true, if successful
     */
	public boolean useReturnTo() {
		return this.useReturnTo;
	}

	/**
     * Sets the use return to.
     *
     * @param useReturnTo
     *            the new use return to
     */
	public void setUseReturnTo(final boolean useReturnTo) {
		this.useReturnTo = useReturnTo;
	}

	/**
     * Gets the permission.
     *
     * @return the permission
     */
	public String getPermission() {
		return this.action.getPermission();
	}

	/**
     * Gets the onclick.
     *
     * @return the onclick
     */
	public String getOnclick() {
		if (!this.shouldUseOnclick()) {
			return "";
		}
		final Object[] messageArgs = new Object[2];
		// DEBT Spring load
		final String format = ResourceBundle.getBundle("ResourceBundle")
				.getString(this.action.getConfirmationKey());
		messageArgs[0] = this.getDomainType();
		messageArgs[1] = StringUtilities.replaceQuotationMarks(this.object
				.getName());
		return "return confirm('" + MessageFormat.format(format, messageArgs)
				+ "')";
	}

	/**
     * Should use onclick.
     *
     * @return true, if successful
     */
	public boolean shouldUseOnclick() {
		return StringUtils.isNotEmpty(this.action.getConfirmationKey());
	}

	/**
     * Gets the title key.
     *
     * @return the title key
     */
	public String getTitleKey() {
		return this.action.getTitleKey();
	}

	/**
     * Checks if is displayed as icon.
     *
     * @return true, if is displayed as icon
     */
	public boolean isDisplayedAsIcon() {
		return this.displayedAsIcon;
	}

	/**
     * Should pass oid param.
     *
     * @return true, if successful
     */
	public boolean shouldPassOidParam() {
		return this.action.shouldPassOidParam();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ActionRenderer)) {
			return false;
		}
		final PropertyDescriptor[] descriptors = PropertyUtils
				.getPropertyDescriptors(ActionRenderer.class);
		for (int i = 0; i < descriptors.length; i++) {
			final String propertyName = descriptors[i].getName();
			try {
				if (!PropertyUtils.getProperty(this, propertyName).equals(
						PropertyUtils.getProperty(obj, propertyName))) {
					return false;
				}
			} catch (final Exception e) {
				ActionRenderer.log.error("exception: ", e);
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final PropertyDescriptor[] descriptors = PropertyUtils
				.getPropertyDescriptors(this);
		final StringBuffer str = new StringBuffer();
		for (int i = 0; i < descriptors.length; i++) {
			try {
				str.append("[")
						.append(descriptors[i].getName())
						.append("=")
						.append(PropertyUtils.getProperty(this,
								descriptors[i].getName())).append("], ");
			} catch (final Exception e) {
				ActionRenderer.log.error("exception: ", e);
			}
		}
		str.delete(str.length() - 2, str.length() - 1);
		return str.toString();
	}

	/**
     * Gets the oid param.
     *
     * @return the oid param
     */
	public String getOidParam() {
		return this.shouldPassOidParam() ? "oid" : "fkey";
	}
}
