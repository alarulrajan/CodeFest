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

public class ActionRenderer {
	private static Logger log = LogUtil.getLogger();
	private boolean useReturnTo;
	private final ActionMapping action;
	private final Nameable object;
	private final boolean displayedAsIcon;

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

	public String getName() {
		return this.action.getName();
	}

	public String getDomainType() {
		return this.action.getDomainType();
	}

	public String getTargetPage() {
		return this.action.getTargetPage();
	}

	public String getIconPath() {
		return this.action.getIconPath();
	}

	public boolean useReturnTo() {
		return this.useReturnTo;
	}

	public void setUseReturnTo(final boolean useReturnTo) {
		this.useReturnTo = useReturnTo;
	}

	public String getPermission() {
		return this.action.getPermission();
	}

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

	public boolean shouldUseOnclick() {
		return StringUtils.isNotEmpty(this.action.getConfirmationKey());
	}

	public String getTitleKey() {
		return this.action.getTitleKey();
	}

	public boolean isDisplayedAsIcon() {
		return this.displayedAsIcon;
	}

	public boolean shouldPassOidParam() {
		return this.action.shouldPassOidParam();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

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

	public String getOidParam() {
		return this.shouldPassOidParam() ? "oid" : "fkey";
	}
}
