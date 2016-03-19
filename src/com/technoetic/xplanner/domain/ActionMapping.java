package com.technoetic.xplanner.domain;

/**
 * The Class ActionMapping.
 */
//DEBT(DATADRIVEN) Rename to ActionDef
public class ActionMapping {
    
    /** The name. */
    private String name;
    
    /** The title key. */
    private String titleKey;
    
    /** The permission. */
    private String permission;
    
    /** The domain type. */
    private String domainType;
    
    /** The target page. */
    private String targetPage;
    
    /** The icon path. */
    private String iconPath;
    
    /** The pass oid param. */
    private boolean passOidParam;
    
    /** The confirmation key. */
    private String confirmationKey;
    
    /** The back to parent. */
    private boolean backToParent;

    /**
     * Instantiates a new action mapping.
     *
     * @param name
     *            the name
     * @param titleKey
     *            the title key
     * @param permission
     *            the permission
     * @param iconPath
     *            the icon path
     * @param targetPage
     *            the target page
     * @param domainType
     *            the domain type
     * @param backToParent
     *            the back to parent
     * @param passOidParam
     *            the pass oid param
     * @param confirmationKey
     *            the confirmation key
     */
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

    /**
     * Instantiates a new action mapping.
     *
     * @param action
     *            the action
     * @param actionKey
     *            the action key
     * @param permission
     *            the permission
     * @param iconPath
     *            the icon path
     * @param targetPage
     *            the target page
     * @param domainType
     *            the domain type
     * @param backToParent
     *            the back to parent
     */
    public ActionMapping(final String action, final String actionKey,
            final String permission, final String iconPath,
            final String targetPage, final String domainType,
            final boolean backToParent) {
        this(action, actionKey, permission, iconPath, targetPage, domainType,
                backToParent, true, null);
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the icon path.
     *
     * @return the icon path
     */
    public String getIconPath() {
        return this.iconPath;
    }

    /**
     * Sets the icon path.
     *
     * @param iconPath
     *            the new icon path
     */
    public void setIconPath(final String iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * Gets the target page.
     *
     * @return the target page
     */
    public String getTargetPage() {
        return this.targetPage;
    }

    /**
     * Sets the target page.
     *
     * @param targetPage
     *            the new target page
     */
    public void setTargetPage(final String targetPage) {
        this.targetPage = targetPage;
    }

    /**
     * Gets the domain type.
     *
     * @return the domain type
     */
    public String getDomainType() {
        return this.domainType;
    }

    /**
     * Sets the domain type.
     *
     * @param domainType
     *            the new domain type
     */
    public void setDomainType(final String domainType) {
        this.domainType = domainType;
    }

    /**
     * Gets the permission.
     *
     * @return the permission
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * Sets the permission.
     *
     * @param permission
     *            the new permission
     */
    public void setPermission(final String permission) {
        this.permission = permission;
    }

    /**
     * Gets the confirmation key.
     *
     * @return the confirmation key
     */
    public String getConfirmationKey() {
        return this.confirmationKey;
    }

    /**
     * Sets the confirmation key.
     *
     * @param confirmationKey
     *            the new confirmation key
     */
    public void setConfirmationKey(final String confirmationKey) {
        this.confirmationKey = confirmationKey;
    }

    /**
     * Checks if is back to parent.
     *
     * @return true, if is back to parent
     */
    public boolean isBackToParent() {
        return this.backToParent;
    }

    /**
     * Sets the back to parent.
     *
     * @param backToParent
     *            the new back to parent
     */
    public void setBackToParent(final boolean backToParent) {
        this.backToParent = backToParent;
    }

    /**
     * Gets the title key.
     *
     * @return the title key
     */
    public String getTitleKey() {
        return this.titleKey;
    }

    /**
     * Sets the title key.
     *
     * @param titleKey
     *            the new title key
     */
    public void setTitleKey(final String titleKey) {
        this.titleKey = titleKey;
    }

    /**
     * Should pass oid param.
     *
     * @return true, if successful
     */
    public boolean shouldPassOidParam() {
        return this.passOidParam;
    }

    /**
     * Sets the pass oid param.
     *
     * @param passOidParam
     *            the new pass oid param
     */
    public void setPassOidParam(final boolean passOidParam) {
        this.passOidParam = passOidParam;
    }

    /**
     * Checks if is visible.
     *
     * @param object
     *            the object
     * @return true, if is visible
     */
    public boolean isVisible(final Nameable object) {
        return true;
    }

}
