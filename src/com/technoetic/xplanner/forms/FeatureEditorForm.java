package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * The Class FeatureEditorForm.
 */
public class FeatureEditorForm extends AbstractEditorForm {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The story id. */
	private int storyId;

	/**
     * Gets the container id.
     *
     * @return the container id
     */
	public String getContainerId() {
		return Integer.toString(this.getStoryId());
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(final ActionMapping mapping,
			final HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		if (this.isSubmitted()) {
			AbstractEditorForm.require(errors, this.name,
					"feature.editor.missing_name");
		}
		return errors;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.name = null;
		this.description = null;
		this.storyId = 0;
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
     * Sets the description.
     *
     * @param description
     *            the new description
     */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return this.description;
	}

	/**
     * Sets the story id.
     *
     * @param storyId
     *            the new story id
     */
	public void setStoryId(final int storyId) {
		this.storyId = storyId;
	}

	/**
     * Gets the story id.
     *
     * @return the story id
     */
	public int getStoryId() {
		return this.storyId;
	}
}