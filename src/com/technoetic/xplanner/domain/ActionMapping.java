package com.technoetic.xplanner.domain;

//DEBT(DATADRIVEN) Rename to ActionDef
public class ActionMapping {
	private String name;
	private String titleKey;
	private String permission;
	private String domainType;
	private String targetPage;
	private String iconPath;
	private boolean passOidParam;
	private String confirmationKey;
	private boolean backToParent;

	public ActionMapping(final String name, final String titleKey,
			final String permission, final String iconPath,
			final String targetPage, final String domainType,
			final boolean backToParent, final boolean passOidParam,
			final String confirmationKey) {
		this.name = name;
		this.titleKey = titleKey;
		this.permission = permission;
		this.iconPath = iconPath;
		this.targetPage = targetPage;
		this.domainType = domainType;
		this.confirmationKey = null;
		this.backToParent = backToParent;
		this.passOidParam = passOidParam;
		this.confirmationKey = confirmationKey;
	}

	public ActionMapping(final String action, final String actionKey,
			final String permission, final String iconPath,
			final String targetPage, final String domainType,
			final boolean backToParent) {
		this(action, actionKey, permission, iconPath, targetPage, domainType,
				backToParent, true, null);
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(final String iconPath) {
		this.iconPath = iconPath;
	}

	public String getTargetPage() {
		return this.targetPage;
	}

	public void setTargetPage(final String targetPage) {
		this.targetPage = targetPage;
	}

	public String getDomainType() {
		return this.domainType;
	}

	public void setDomainType(final String domainType) {
		this.domainType = domainType;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setPermission(final String permission) {
		this.permission = permission;
	}

	public String getConfirmationKey() {
		return this.confirmationKey;
	}

	public void setConfirmationKey(final String confirmationKey) {
		this.confirmationKey = confirmationKey;
	}

	public boolean isBackToParent() {
		return this.backToParent;
	}

	public void setBackToParent(final boolean backToParent) {
		this.backToParent = backToParent;
	}

	public String getTitleKey() {
		return this.titleKey;
	}

	public void setTitleKey(final String titleKey) {
		this.titleKey = titleKey;
	}

	public boolean shouldPassOidParam() {
		return this.passOidParam;
	}

	public void setPassOidParam(final boolean passOidParam) {
		this.passOidParam = passOidParam;
	}

	public boolean isVisible(final Nameable object) {
		return true;
	}

}
